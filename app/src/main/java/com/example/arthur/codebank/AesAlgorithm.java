package com.example.arthur.codebank;

import android.util.Base64;

import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AesAlgorithm {

    private static final String AES = "AES";

    public static String encrypt(String passwordInputBox, String encryptionKeyBox ) throws Exception{
        SecretKeySpec key = generateKey(encryptionKeyBox);
        Cipher cipher = Cipher.getInstance(AES);
        cipher.init(Cipher.ENCRYPT_MODE,key);
        byte[] encVal = cipher.doFinal(passwordInputBox.getBytes());
        String encryptedValue = Base64.encodeToString(encVal,Base64.DEFAULT);
        return encryptedValue;
    }
    public static String decrypt(String temporaryString, String encryptionKeyBox)throws Exception{
        SecretKeySpec key = generateKey(encryptionKeyBox);
        Cipher cipher = Cipher.getInstance(AES);
        cipher.init(Cipher.DECRYPT_MODE,key);
        byte[] decodedValue = Base64.decode(temporaryString, Base64.DEFAULT);
        byte[] decValue = cipher.doFinal(decodedValue);
        String decryptedValue = new String(decValue);
        return decryptedValue;
    }


    private static SecretKeySpec generateKey(String encryptionKeyBox ) throws Exception{
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = encryptionKeyBox.getBytes("UTF-8");
        digest.update(bytes,0,bytes.length);
        byte[] key = digest.digest();
        SecretKeySpec secretKeySpec = new SecretKeySpec(key,"AES");
        return secretKeySpec;
    }

}
