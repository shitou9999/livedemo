package tv.kuainiu.utils;


import android.os.Environment;
import android.text.TextUtils;
import android.util.Base64;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

/**
 * Created by guxuan on 2016/2/29.
 */
public class StringUtils {


    private String decodeUnicode(String theString) {
        char aChar;
        int len = theString.length();
        StringBuffer outBuffer = new StringBuffer(len);
        for (int x = 0; x < len; ) {
            aChar = theString.charAt(x++);
            if (aChar == '\\') {
                aChar = theString.charAt(x++);
                if (aChar == 'u') {
                    // Read the xxxx
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = theString.charAt(x++);
                        switch (aChar) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                value = (value << 4) + aChar - '0';
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                value = (value << 4) + 10 + aChar - 'a';
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                value = (value << 4) + 10 + aChar - 'A';
                                break;
                            default:
                                throw new IllegalArgumentException(
                                        "Malformed   \\uxxxx   encoding.");
                        }

                    }
                    outBuffer.append((char) value);
                } else {
                    if (aChar == 't')
                        aChar = '\t';
                    else if (aChar == 'r')
                        aChar = '\r';
                    else if (aChar == 'n')
                        aChar = '\n';
                    else if (aChar == 'f')
                        aChar = '\f';
                    outBuffer.append(aChar);
                }
            } else
                outBuffer.append(aChar);
        }
        return outBuffer.toString();
    }

    /**
     * 电话号码、邮箱、身份证号文本加 *
     *
     * @param value
     * @return
     */
    public static String getX(String value) {
        if (TextUtils.isEmpty(value)) {
            return "";
        }
        String result;
        if (value.length() == 18 || value.length() == 11) {
            result = value.replace(value.substring(3, value.length() - 4), "****");
        } else {
            if (value.contains("@")) {
                int index = value.indexOf("@");
                if (index - 4 > 0) {
                    result = value.replace(value.substring(index - 3, index), "****");
                } else {
                    result = value.replace(value.substring(1, index), "****");
                }
            } else {
                result = "";
            }
        }
        return result;
    }

    /**
     * @param str
     * @return
     */
    public static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
//            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
//            Matcher m = p.matcher(str);
            dest = str.replace(" ", "");
        }
        return dest.trim();
    }


    /**
     * 格式化字符串
     *
     * @param format
     * @param args
     */
    public static String format(String format, Object... args) {
        return String.format(Locale.CHINA, format, args);
    }

    /**
     * 将null替换成空字符串
     *
     * @param object 被替换的字符
     * @return
     */
    public static String replaceNullToEmpty(Object object) {
        return replaceNullToEmpty(object, null);

    }

    /**
     * 将null替换成指定的字符串replaceNullString
     *
     * @param object            被替换的字符
     * @param replaceNullString 用来替换的字符
     * @return
     */
    public static String replaceNullToEmpty(Object object, String replaceNullString) {
        String _replaceNullString = replaceNullString == null ? "" : replaceNullString;
        if (object == null || object.toString().trim().length() < 1 || "null".equals(object.toString().trim())) {
            return _replaceNullString;
        }
        return object.toString();

    }

    /**
     * @param value   原始数据
     * @param flag    临界值
     * @param format1 大于临界值显示格式
     * @param format2 小于临界值显示格式
     * @return
     */
    public static String getDecimal(int value, int flag, String format1, String format2) {
        String strCount;
        if (value > flag) {
            float floCount = ((float) value / flag);
            DebugUtils.dd("float count : " + floCount);
            strCount = new java.text.DecimalFormat("0.0").format(floCount);
            return strCount.concat(format1);
        } else {
            return String.valueOf(value).concat(format2);
        }
    }

    // XXX
    public static String getDecimal2(int value, int flag, String format1, String format2) {
        String strCount;
        if (value > flag) {
            float floCount = ((float) value / flag);
            DebugUtils.dd("float count : " + floCount);
            strCount = new java.text.DecimalFormat("0.0").format(floCount);
            return strCount.concat(format1);
        } else {
            return format2;
        }
    }
    /**
     * 将文件转成base64字符串(用于发送语音文件)
     * @param path
     * @return
     * @throws Exception
     */
    public static String encodeBase64File(String path) throws Exception {
        File file = new File(path);
        FileInputStream inputFile = new FileInputStream(file);
        byte[] buffer = new byte[(int)file.length()];
        inputFile.read(buffer);
        inputFile.close();
        return Base64.encodeToString(buffer, Base64.DEFAULT);
    }

    /**
     * 将base64编码后的字符串转成文件
     * @param base64Code
     * @throws Exception
     */
    public static String decoderBase64File(String base64Code)  {
        String savePath = "";
        String saveDir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Yun/Sounds/";
        File dir = new File(saveDir);
        if(!dir.exists()){
            dir.mkdirs();
        }
        savePath = saveDir+getRandomFileName();
        byte[] buffer = Base64.decode(base64Code, Base64.DEFAULT);
        try {
            FileOutputStream out = new FileOutputStream(savePath);
            out.write(buffer);
            out.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return  savePath;
    }

    public static String getRandomFileName() {
        String rel="";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Date curDate = new Date(System.currentTimeMillis());
        rel = formatter.format(curDate);
        rel = rel + new Random().nextInt(1000);
        return rel+".amr";
    }

}
