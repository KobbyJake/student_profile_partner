
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

//~--- non-JDK imports --------------------------------------------------------
import sun.misc.BASE64Encoder;

//~--- JDK imports ------------------------------------------------------------
import java.security.Key;

import java.security.MessageDigest;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import sun.misc.BASE64Decoder;

/**
 *
 * @author Jake
 */
public final class CryptMaster {

    public String encryptPass(String pass) throws Exception{
        MessageDigest md = null;

        md = MessageDigest.getInstance("SHA-256");
        md.update(pass.getBytes("UTF-8"));
        byte raw[] = md.digest();
        String hash = (new BASE64Encoder()).encode(raw);

        return hash;
    }

    private static final String ALGO = "AES";
    private static final byte[] keyValue = new byte[]{'K', 'o', 'b', 'b', 'y', 'J', 'C', 'S', 'e', 'c', 'r', 'e', 't', 'K', 'e', 'y'};

    public static String encryptData(String Data) throws Exception {
        Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = c.doFinal(Data.getBytes());
        String encryptedValue = new BASE64Encoder().encode(encVal);
        return encryptedValue;
    }

    public static String decryptData(String encryptedData) throws Exception {
        Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedData);
        byte[] decValue = c.doFinal(decordedValue);
        String decryptedValue = new String(decValue);
        return decryptedValue;
    }

    private static Key generateKey() throws Exception {
        Key key = new SecretKeySpec(keyValue, ALGO);
        return key;
    }
}


//~ Formatted by Jindent --- http://www.jindent.com
