package jp.mirm.mirmapp.utils;

import android.util.Base64;
import jp.mirm.mirmapp.MainApplication;

import java.io.*;
import java.nio.charset.StandardCharsets;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Utils {

    public static String encrypt(String originalSource, String secretKey) {
        byte[] originalBytes = originalSource.getBytes();
        byte[] encryptBytes = cipher(true, originalBytes, secretKey);
        byte[] encryptBytesBase64 = android.util.Base64.encode(encryptBytes, android.util.Base64.DEFAULT);
        return new String(encryptBytesBase64);
    }

    public static String decrypt(String encryptBytesBase64String, String secretKey) {
        try {
            byte[] encryptBytes = android.util.Base64.decode(encryptBytesBase64String, Base64.DEFAULT);
            byte[] originalBytes = cipher(false, encryptBytes, secretKey);
            return new String(originalBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static byte[] cipher(boolean isEncrypt, byte[] source, String secretKey) {
        try {
            byte[] secretKeyBytes = secretKey.getBytes();

            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKeyBytes, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            if (isEncrypt) {
                cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            } else {
                cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            }

            return cipher.doFinal(source);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void writeFile(String fileName, String text) {
        try {
            FileOutputStream fileOutputstream = new FileOutputStream(new File(fileName));
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(fileOutputstream, StandardCharsets.UTF_8));
            writer.append(text);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void appendFile(String fileName, String text) {
        try {
            FileOutputStream fileOutputstream = new FileOutputStream(new File(fileName), true);
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(fileOutputstream, StandardCharsets.UTF_8));
            writer.append(text);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readFile(String fileName) {
        InputStream in;
        String lineBuffer;
        StringBuffer buf = new StringBuffer();

        try {
            in = new FileInputStream(new File(fileName));
            BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            while ((lineBuffer = reader.readLine()) != null) {
                buf.append("\n" + lineBuffer);
            }
        } catch (IOException e) {
            return null;
        }
        return buf.toString();
    }

    public static void deleteFile(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) return;
        file.delete();
    }

    //returns Megabyte
    public static double getFileSize(File file) {
        return (double) file.length() / 1024.0 / 1024.0;
    }

}
