package com.example.warehousemanagement.other;


import android.content.Context;
import android.content.SharedPreferences;
import android.security.KeyPairGeneratorSpec;
import android.security.keystore.KeyProperties;
import android.util.Base64;

import com.example.warehousemanagement.DangNhap;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Calendar;

import javax.crypto.Cipher;
import javax.security.auth.x500.X500Principal;

public class SaveLoginKey {
    private static final String PREF_NAME = "UserSession";
    private static final String KEY_USER_INFO = "userInfo";
    private static final String KEY_ALIAS = "MyKeyAlias";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;

    public SaveLoginKey(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveUserInfo(String userInfoJson) {
        try {
            KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);

            if (!keyStore.containsAlias(KEY_ALIAS)) {
                Calendar start = Calendar.getInstance();
                Calendar end = Calendar.getInstance();
                end.add(Calendar.YEAR, 10);

                KeyPairGeneratorSpec spec = new KeyPairGeneratorSpec.Builder(context)
                        .setAlias(KEY_ALIAS)
                        .setSubject(new X500Principal("CN=" + KEY_ALIAS))
                        .setSerialNumber(BigInteger.ONE)
                        .setStartDate(start.getTime())
                        .setEndDate(end.getTime())
                        .build();

                KeyPairGenerator generator = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA, "AndroidKeyStore");
                generator.initialize(spec);

                generator.generateKeyPair();
            }

            PublicKey publicKey = keyStore.getCertificate(KEY_ALIAS).getPublicKey();

            String encodedInfo = encrypt(publicKey, userInfoJson.getBytes());

            editor.putString(KEY_USER_INFO, encodedInfo);
            editor.apply();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String encrypt(PublicKey publicKey, byte[] inputData) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedInfo = cipher.doFinal(inputData);
        String encodedEncryptedInfo = Base64.encodeToString(encryptedInfo, Base64.DEFAULT);
        return encodedEncryptedInfo;
    }

    private static String decrypt(PrivateKey privateKey, byte[] encryptedData) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedInfo = cipher.doFinal(encryptedData);
        String decryptedcodedInfo = new String(decryptedInfo, StandardCharsets.UTF_8);
        return decryptedcodedInfo;
    }

    public String getUserInfo() throws Exception {
        KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
        keyStore.load(null);

        if (!keyStore.containsAlias(KEY_ALIAS)) {
            Calendar start = Calendar.getInstance();
            Calendar end = Calendar.getInstance();
            end.add(Calendar.YEAR, 10);

            KeyPairGeneratorSpec spec = new KeyPairGeneratorSpec.Builder(context)
                    .setAlias(KEY_ALIAS)
                    .setSubject(new X500Principal("CN=" + KEY_ALIAS))
                    .setSerialNumber(BigInteger.ONE)
                    .setStartDate(start.getTime())
                    .setEndDate(end.getTime())
                    .build();

            KeyPairGenerator generator = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA, "AndroidKeyStore");
            generator.initialize(spec);

            generator.generateKeyPair();
        }
        PrivateKey privateKey = (PrivateKey) keyStore.getKey(KEY_ALIAS, null);
        String userInfo = sharedPreferences.getString(KEY_USER_INFO, null);
        String decryptedInfo = decrypt(privateKey, userInfo.getBytes());
        return decryptedInfo;
    }

    public void clearSession() {
        editor.clear();
        editor.apply();
    }

}