package com.google.zxing;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;

import com.google.zxing.camera.CameraManager;
import com.google.zxing.decoding.CaptureActivityHandler;
import com.google.zxing.decoding.InactivityTimer;
import com.google.zxing.view.ViewfinderView;

import java.io.IOException;
import java.util.Vector;

import tv.kuainiu.R;
import tv.kuainiu.command.http.Api;
import tv.kuainiu.modle.QcText;
import tv.kuainiu.modle.cons.Constant;
import tv.kuainiu.ui.activity.WebActivity;
import tv.kuainiu.ui.me.LoginByQcActivity;
import tv.kuainiu.utils.DataConverter;
import tv.kuainiu.utils.DialogUtils;
import tv.kuainiu.utils.LogUtils;
import tv.kuainiu.utils.StringUtils;

public class CaptureActivity extends Activity implements Callback {

    public static final String RESULT = "result";
    private CaptureActivityHandler handler;
    private ViewfinderView viewfinderView;
    private boolean hasSurface;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private InactivityTimer inactivityTimer;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    private static final float BEEP_VOLUME = 0.10f;
    private boolean vibrate;
    private ImageView iv_back;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.qr_code_scan);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        CameraManager.init(getApplication());
        initControl();

        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);
    }

    private void initControl() {
        viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
    }


    @Override
    protected void onResume() {
        super.onResume();
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;

        playBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }

    @Override
    public void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    /**
     * @param result
     * @param barcode
     */
    public void handleDecode(Result result, Bitmap barcode) {
        inactivityTimer.onActivity();
        playBeepSoundAndVibrate();

//        Intent resultIntent = new Intent();
//        resultIntent.putExtra(RESULT, result.getText());
//        this.setResult(RESULT_OK, resultIntent);
//        finish();
        String resultString = result.getText();
        if (resultString != null && resultString.trim().length() > 0 && resultString.startsWith("http")) {
            if (resultString.startsWith(Api.DNS_HOST)) {
                resultString = resultString.substring(resultString.indexOf("?") + 1);
                resultString = resultString.replace("&", "\",\"");
                resultString = resultString.replace("=", "\":\"");
                resultString = "{\"" + resultString + "\"}";
                try {
                    DataConverter<QcText> da = new DataConverter<>();
                    QcText qt = da.JsonToObject(resultString, QcText.class);
                    if (qt != null && Constant.QR_CODE_LOGIN.equals(qt.getType())) {
                        Intent intent = new Intent(CaptureActivity.this,
                                LoginByQcActivity.class);
                        intent.putExtra(Constant.CLIENT_ID, qt.getClient_id());
                        intent.putExtra(Constant.CONNECT_TIME, qt.getConnect_time());
                        startActivity(intent);
                        finish();
                    } else {
                        failureAnalysisQcResult(result.getText());
                    }
                } catch (Exception e) {
                    LogUtils.e("CaptureActivity", "二维码扫描结果解析成json异常", e);
                    failureAnalysisQcResult(resultString);
                }
            } else {
                Intent intent = new Intent(CaptureActivity.this,
                        WebActivity.class);
                intent.putExtra(Constant.KEY_URL, resultString);
                startActivity(intent);
                finish();
            }
        } else {
            failureAnalysisQcResult(resultString);
        }
    }

    /**
     * 无法识别
     */
    private void failureAnalysisQcResult(String resultString) {
//        ToastUtils.showToast(this, StringUtils.replaceNullToEmpty(resultString, "无法识别"));
        DialogUtils.TipAccountConflict(this, StringUtils.replaceNullToEmpty(resultString, "无法识别"), new DialogUtils.IDialogButtonOnClickListener() {
            @Override public void cancel() {
                finish();
            }

            @Override public void sure() {
                finish();
            }
        });
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            return;
        }
        if (handler == null) {
            handler = new CaptureActivityHandler(this, decodeFormats,characterSet);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();

    }

    /**
     * 扫描正确后的震动声音,如果感觉apk大了,可以删除
     */
    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            // The volume on STREAM_SYSTEM is not adjustable, and users found it
            // too loud,
            // so we now play on the music stream.
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(
                    R.raw.beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(),
                        file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    private static final long VIBRATE_DURATION = 200L;

    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final OnCompletionListener beepListener = new OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };
}
