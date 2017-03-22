package guda.mvcx.session;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by well on 2017/3/22.
 */
public class CookieCheck {

    private final static String KEY = "a+_";

    private static String byteArrayToHex(byte[] byteArray) {
        char[] hexDigits = {'0','1','2','3','4','5','6','7','8','9', 'a','b','c','d','e','f' };
        char[] resultCharArray =new char[byteArray.length * 2];
        int index = 0;
        for (byte b : byteArray) {
            resultCharArray[index++] = hexDigits[b>>> 4 & 0xf];
            resultCharArray[index++] = hexDigits[b& 0xf];
        }
        return new String(resultCharArray);
    }
    public static String md5(String data) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            data += KEY;
            byte[] inputByteArray = data.getBytes();
            messageDigest.update(inputByteArray);
            byte[] resultByteArray = messageDigest.digest();
            String md= byteArrayToHex(resultByteArray);
            return md.substring(2,19);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    public static String mdCheck(String data) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            data += KEY;
            byte[] inputByteArray = data.getBytes();
            messageDigest.update(inputByteArray);
            byte[] resultByteArray = messageDigest.digest();
            String s = byteArrayToHex(resultByteArray);
            return s.substring(2,19);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }
}
