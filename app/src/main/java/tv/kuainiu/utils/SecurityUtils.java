package tv.kuainiu.utils;

import android.annotation.SuppressLint;
import android.util.Base64;

import tv.kuainiu.command.http.Api;

import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;


@SuppressLint("TrulyRandom")
public class SecurityUtils {

    public static class MD5 {

        public static String getMD5(String info) {
            try {
                MessageDigest md5 = MessageDigest.getInstance("MD5");
                md5.update(info.getBytes("UTF-8"));
                byte[] encryption = md5.digest();

                StringBuffer strBuf = new StringBuffer();
                for (int i = 0; i < encryption.length; i++) {
                    if (Integer.toHexString(0xff & encryption[i]).length() == 1) {
                        strBuf.append("0").append(Integer.toHexString(0xff & encryption[i]));
                    } else {
                        strBuf.append(Integer.toHexString(0xff & encryption[i]));
                    }
                }

                return strBuf.toString();
            } catch (NoSuchAlgorithmException e) {
                return "";
            } catch (UnsupportedEncodingException e) {
                return "";
            }
        }

    }


    public static String salt(String data) {
        return MD5.getMD5(Api.SALT + data);
    }


    /**
     * @author zhaokaiqiang
     * @ClassName: com.qust.rollcallstudent.utils.DESUtil
     * @Description: DES加密解密工具包
     * @date 2014-11-13 下午8:40:56
     */
    public static class DESUtil {
        protected static final String ALGORITHM_DES = "DES/ECB/PKCS7Padding";


        /**
         * DES算法，加密
         *
         * @param data 待加密字符串
         * @param key  加密私钥，长度不能够小于8位
         * @return 加密后的字节数组，一般结合Base64编码使用
         * @throws Exception
         */
        public static String en(String key, String data) {
            if (data == null)
                return null;
            try {
                DESKeySpec dks = new DESKeySpec(key.getBytes());
                SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
                // key的长度不能够小于8位字节
                Key secretKey = keyFactory.generateSecret(dks);
                Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
                cipher.init(Cipher.ENCRYPT_MODE, secretKey);
                byte[] bytes = cipher.doFinal(data.getBytes());
                return Base64.encodeToString(bytes, Base64.DEFAULT);
            } catch (Exception e) {
                e.printStackTrace();
                return data;
            }
        }

        /**
         * DES算法，解密
         *
         * @param data 待解密字符串
         * @param key  解密私钥，长度不能够小于8位
         * @return 解密后的字节数组
         * @throws Exception 异常
         */
        public static String de(String key, String data) {
            if (data == null)
                return null;
            try {
                DESKeySpec dks = new DESKeySpec(key.getBytes());
                SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
                // key的长度不能够小于8位字节
                Key secretKey = keyFactory.generateSecret(dks);
                @SuppressLint("GetInstance") Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
                cipher.init(Cipher.DECRYPT_MODE, secretKey);
                return new String(cipher.doFinal(Base64.decode(data.getBytes(), Base64.DEFAULT)));
            } catch (Exception e) {
                e.printStackTrace();
                return data;
            }
        }


        /**
         * 二进制转字符串
         *
         * @param b
         * @return
         */
        private static String byte2String(byte[] b) {
            StringBuilder hs = new StringBuilder();
            String stmp;
            for (int n = 0; b != null && n < b.length; n++) {
                stmp = Integer.toHexString(b[n] & 0XFF);
                if (stmp.length() == 1)
                    hs.append('0');
                hs.append(stmp);
            }
            return hs.toString().toUpperCase(Locale.CHINA);
        }

        /**
         * 二进制转化成16进制
         *
         * @param b
         * @return
         */
        private static byte[] byte2hex(byte[] b) {
            if ((b.length % 2) != 0) {
                throw new IllegalArgumentException();
            }
            byte[] b2 = new byte[b.length / 2];
            for (int n = 0; n < b.length; n += 2) {
                String item = new String(b, n, 2);
                b2[n / 2] = (byte) Integer.parseInt(item, 16);
            }
            return b2;
        }

        public static byte[] fromHexString(String s) {
            int len = s.length();
            byte[] data = new byte[len / 2];
            for (int i = 0; i < len; i += 2) {
                data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                        + Character.digit(s.charAt(i + 1), 16));
            }
            return data;
        }


    }


}

