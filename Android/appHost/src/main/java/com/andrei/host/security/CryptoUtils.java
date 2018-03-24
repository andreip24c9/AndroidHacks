package com.andrei.host.security;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Andrei on 30/10/2017.
 */

public class CryptoUtils {

    public static final String TAG = CryptoUtils.class.getSimpleName();

    public static final String CONFIG_FILE = "config";
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/CBC/NOPADDING";

    private static int APP_SIZE;
    private static String KEY;
    private static byte[] IV_BYTES;

    public static byte[] decryptApk(String fileName, Context context) {
        try {
            readFileContent(context);

            byte[] png = readBytes(fileName, context);
            byte[] decryptedAPK = CryptoUtils.fileProcessor(png);
            Log.d(TAG, "decrypted img size: " + String.valueOf(decryptedAPK.length));
            Log.d(TAG, "photo successfully decrypted");
            return decryptedAPK;
        } catch (IOException | NoSuchAlgorithmException | InvalidKeyException
                | InvalidAlgorithmParameterException | NoSuchPaddingException
                | BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static byte[] readBytes(String file, Context context) throws IOException {
        InputStream is = context.getAssets().open(file);
        int size = is.available();
        byte[] assets = new byte[size];
        is.read(assets);
        is.close();
        Log.d(TAG, "readBytes: bytes read");
        return assets;
    }

    private static byte[] fileProcessor(byte[] encryptedBytes) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        SecretKeySpec secretKey = new SecretKeySpec(KEY.getBytes(), ALGORITHM);
        IvParameterSpec ivSpec = new IvParameterSpec(IV_BYTES);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
        byte[] decrypted = cipher.doFinal(encryptedBytes);
        return Arrays.copyOf(decrypted, APP_SIZE);
    }

    private static void readFileContent(Context context) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(context.getAssets().open(CONFIG_FILE)))) {

            KEY = br.readLine();
            APP_SIZE = Integer.parseInt(br.readLine());
            IV_BYTES = hexStringToByteArray(br.readLine());
        }
    }

    private static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }
}
