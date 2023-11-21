package com.example.warehousemanagement.other;

import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;

import java.security.KeyStore;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class MyAsyncTask {

    private static final String KEY_ALIAS = "MyKeyAlias";

    public static void generateKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(
                KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");

        KeyGenParameterSpec keyGenParameterSpec = new KeyGenParameterSpec.Builder(
                KEY_ALIAS,
                KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                .setRandomizedEncryptionRequired(false)
                .build();

        keyGenerator.init(keyGenParameterSpec);
        keyGenerator.generateKey();
    }

    public static byte[] encryptData(byte[] data) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");

        // Generate a new IV for each encryption
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey());
        byte[] iv = cipher.getIV();

        // Prepend IV to the encrypted data
        byte[] encryptedDataWithIV = new byte[iv.length + cipher.doFinal(data).length];
        System.arraycopy(iv, 0, encryptedDataWithIV, 0, iv.length);
        System.arraycopy(cipher.doFinal(data), 0, encryptedDataWithIV, iv.length, cipher.doFinal(data).length);

        return encryptedDataWithIV;
    }

    public static byte[] decryptData(byte[] encryptedDataWithIV) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");

        // Extract IV from the encrypted data
        byte[] iv = new byte[16];  // Assuming 16 bytes IV
        System.arraycopy(encryptedDataWithIV, 0, iv, 0, iv.length);

        // Initialize the decryption Cipher with IV
        cipher.init(Cipher.DECRYPT_MODE, getSecretKey(), new IvParameterSpec(iv));

        // Decrypt the data without the IV
        byte[] decryptedData = cipher.doFinal(encryptedDataWithIV, iv.length, encryptedDataWithIV.length - iv.length);

        return decryptedData;
    }
    private static SecretKey getSecretKey() throws Exception {
        KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
        keyStore.load(null);

        return (SecretKey) keyStore.getKey(KEY_ALIAS, null);
    }

}
