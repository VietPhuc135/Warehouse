package com.example.warehousemanagement.user;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Base64;

import androidx.annotation.Keep;
import androidx.annotation.RequiresApi;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
@Keep
public class SaveLogin {
    private static final String PREF_NAME = "UserSession";
    private static final String KEY_USER_INFO = "userInfo";
    private static final String KEY_ALIAS = "MyKeyAlias";

    PublicKey publicKey;
    PrivateKey privateKey;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;

    public SaveLogin(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void saveUserInfo(String userInfoJson) {
        try {
            // Generate or retrieve the secret key
            SecretKey secretKey = getOrGenerateSecretKey();

            // Encrypt data with AES
            byte[] encryptedData = encryptAES(userInfoJson.getBytes(), secretKey);

            // Save the encrypted data
            editor.putString(KEY_USER_INFO, Base64.encodeToString(encryptedData, Base64.DEFAULT));
            editor.apply();

        } catch (Exception e) {
            e.printStackTrace();
            // Log or handle the exception in a detailed manner
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getUserInfo() throws Exception {
        try {
            // Retrieve the secret key
            SecretKey secretKey = getOrGenerateSecretKey();

            // Retrieve and decrypt the data
            String encryptedDataString = sharedPreferences.getString(KEY_USER_INFO, null);
            if (encryptedDataString != null) {
                byte[] encryptedData = Base64.decode(encryptedDataString, Base64.DEFAULT);
                byte[] decryptedData = decryptAES(encryptedData, secretKey);
                return new String(decryptedData);
            }

        } catch (Exception e) {
            e.printStackTrace();
            // Log or handle the exception in a detailed manner
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private SecretKey getOrGenerateSecretKey() throws Exception {
        // Check if the secret key exists in SharedPreferences
        String secretKeyString = sharedPreferences.getString(KEY_ALIAS, null);
        if (secretKeyString != null) {
            // Convert the stored key string to SecretKey
            return convertStringToSecretKey(secretKeyString);
        } else {
            // Generate a new secret key
            SecretKey secretKey = generateAESKey();
            // Save the secret key to SharedPreferences
            editor.putString(KEY_ALIAS, convertSecretKeyToString(secretKey));
            editor.apply();
            return secretKey;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private SecretKey generateAESKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(256, SecureRandom.getInstanceStrong());
        return keyGenerator.generateKey();
    }

    private byte[] encryptAES(byte[] inputData, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        // Generate Initialization Vector (IV)
        byte[] iv = cipher.getIV();

        // Prepend IV to the encrypted data
        byte[] encryptedDataWithIV = new byte[iv.length + cipher.doFinal(inputData).length];
        System.arraycopy(iv, 0, encryptedDataWithIV, 0, iv.length);
        System.arraycopy(cipher.doFinal(inputData), 0, encryptedDataWithIV, iv.length, cipher.doFinal(inputData).length);

        return encryptedDataWithIV;
    }

    private byte[] decryptAES(byte[] encryptedDataWithIV, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");

        // Extract IV from the encrypted data
        byte[] iv = new byte[cipher.getBlockSize()];
        System.arraycopy(encryptedDataWithIV, 0, iv, 0, iv.length);

        cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));

        // Decrypt data excluding IV
        return cipher.doFinal(encryptedDataWithIV, iv.length, encryptedDataWithIV.length - iv.length);
    }

    private String convertSecretKeyToString(SecretKey secretKey) {
        return Base64.encodeToString(secretKey.getEncoded(), Base64.DEFAULT);
    }

    private SecretKey convertStringToSecretKey(String secretKeyString) {
        byte[] encodedKey = Base64.decode(secretKeyString, Base64.DEFAULT);
        return new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
    }

    public void clearSession() {
        editor.clear();
        editor.commit();
    }

}
//
//package com.example.warehousemanagement.user;
//
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.os.Build;
//import android.security.KeyPairGeneratorSpec;
//import android.security.keystore.KeyGenParameterSpec;
//import android.security.keystore.KeyProperties;
//import android.util.Base64;
//
//import java.io.ByteArrayOutputStream;
//import java.math.BigInteger;
//import java.nio.charset.StandardCharsets;
//import java.security.InvalidAlgorithmParameterException;
//import java.security.KeyPair;
//import java.security.KeyPairGenerator;
//import java.security.KeyStore;
//import java.security.NoSuchAlgorithmException;
//import java.security.NoSuchProviderException;
//import java.security.PrivateKey;
//import java.security.PublicKey;
//import java.security.spec.ECGenParameterSpec;
//import java.util.Calendar;
//
//import javax.crypto.Cipher;
//import javax.crypto.CipherOutputStream;
//import javax.security.auth.x500.X500Principal;
//
//public class SaveLogin {
//    private static final String PREF_NAME = "UserSession";
//    private static final String KEY_USER_INFO = "userInfo";
//    private static final String KEY_ALIAS = "MyKeyAlias";
//    KeyPair keyPair;
//    private SharedPreferences sharedPreferences;
//    private SharedPreferences.Editor editor;
//    private Context context;
//
//    public SaveLogin(Context context) {
//        this.context = context;
//        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
//        editor = sharedPreferences.edit();
//    }
//
//
//    public void createKeys() throws NoSuchProviderException, NoSuchAlgorithmException, InvalidAlgorithmParameterException {
//        KeyGenParameterSpec spec =
//                new KeyGenParameterSpec.Builder(
//                        KEY_ALIAS,
//                        KeyProperties.PURPOSE_SIGN)
//                        .setKeySize(4096) // Chọn kích thước khóa RSA ở đây, có thể điều chỉnh tùy ý
//                        .setDigests(KeyProperties.DIGEST_SHA256,
//                                KeyProperties.DIGEST_SHA384,
//                                KeyProperties.DIGEST_SHA512)
//                        .setSignaturePaddings(KeyProperties.SIGNATURE_PADDING_RSA_PKCS1)
//                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)
//                        .setUserAuthenticationRequired(false)
//                        .setUserAuthenticationValidityDurationSeconds(5 * 60)
//                        .build();
//
//        KeyPairGenerator generator = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA, "AndroidKeyStore");
//        generator.initialize(spec);
//         keyPair = generator.generateKeyPair();
//    }
//
//
//    public void saveUserInfo(String userInfoJson) {
//        try {
//            KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
//            keyStore.load(null);
//
//            if (!keyStore.containsAlias(KEY_ALIAS)) {
////              createKeys();
//
//                Calendar start = Calendar.getInstance();
//                Calendar end = Calendar.getInstance();
//                end.add(Calendar.YEAR, 10);
//                KeyPairGeneratorSpec spec = new KeyPairGeneratorSpec.Builder(context)
//                        .setAlias(KEY_ALIAS)
//                        .setSubject(new X500Principal("CN=" + KEY_ALIAS))
//                        .setSerialNumber(BigInteger.ONE)
//                        .setStartDate(start.getTime())
//                        .setEndDate(end.getTime())
//                        .setKeySize(4096)
//                        .build();
//                KeyPairGenerator generator = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA, "AndroidKeyStore");
//                generator.initialize(spec);
//                 keyPair = generator.generateKeyPair();
//            }
//
//            PublicKey publicKey = keyStore.getCertificate(KEY_ALIAS).getPublicKey();
////            Cipher inputCipher =Cipher.getInstance("RSA");
////            inputCipher.init(Cipher.ENCRYPT_MODE, publicKey);
////            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
////            CipherOutputStream
//            String encodedInfo = encrypt(publicKey, userInfoJson.getBytes());
//
//            editor.putString(KEY_USER_INFO, encodedInfo);
//            editor.apply();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private static String encrypt(PublicKey publicKey, byte[] inputData) throws Exception {
//        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
//        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
//        byte[] encryptedInfo = cipher.doFinal(inputData);
//        String encodedEncryptedInfo = Base64.encodeToString(encryptedInfo, Base64.DEFAULT);
//        return encodedEncryptedInfo;
//    }
//
//    private static String decrypt(PrivateKey privateKey, byte[] encryptedData) throws Exception {
//        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
//        cipher.init(Cipher.DECRYPT_MODE, privateKey);
//        byte[] decryptedInfo = cipher.doFinal(encryptedData);
//        return new String(decryptedInfo, StandardCharsets.UTF_8);
//    }
//
//    public String getUserInfo() throws Exception {
//        KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
//        keyStore.load(null);
//
//        if (!keyStore.containsAlias(KEY_ALIAS)) {
//            Calendar start = Calendar.getInstance();
//            Calendar end = Calendar.getInstance();
//            end.add(Calendar.YEAR, 10);
//
//            KeyGenParameterSpec spec =
//                    new KeyGenParameterSpec.Builder(
//                            KEY_ALIAS,
//                            KeyProperties.PURPOSE_SIGN)
//                            .setKeySize(4096) // Chọn kích thước khóa RSA ở đây, có thể điều chỉnh tùy ý
//                            .setDigests(KeyProperties.DIGEST_SHA256,
//                                    KeyProperties.DIGEST_SHA384,
//                                    KeyProperties.DIGEST_SHA512)
//                            .setSignaturePaddings(KeyProperties.SIGNATURE_PADDING_RSA_PKCS1)
//                            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)
//                            .setUserAuthenticationRequired(false)
//                            .setUserAuthenticationValidityDurationSeconds(5 * 60)
//                            .build();
//            KeyPairGenerator generator = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA, "AndroidKeyStore");
//            generator.initialize(spec);
//
//            generator.generateKeyPair();
//        }
//        PrivateKey privateKey = (PrivateKey) keyStore.getKey(KEY_ALIAS, null);
//        String userInfo = sharedPreferences.getString(KEY_USER_INFO, null);
//        String deecrypt=  decrypt(privateKey, userInfo.getBytes());
//        return deecrypt;
//    }
//
//    public void clearSession() {
//        editor.clear();
//        editor.apply();
//    }
//
//}