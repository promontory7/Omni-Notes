/*
 * Copyright (C) 2015 Federico Iosue (federico.iosue@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package it.feio.android.omninotes.utils;

import android.util.Base64;
import roboguice.util.Ln;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;


public class Security {


    public static String md5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");//java.security.MessageDigest类提供信息摘要算法功能，此处为MD5算法。
            digest.update(s.getBytes());//处理数据
            byte messageDigest[] = digest.digest();//二进制数组

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));//二进制转HEX字符串
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }


    public static String encrypt(String value, String password) {
        String encrypedValue = "";
        try {
            DESKeySpec keySpec = new DESKeySpec(password.getBytes("UTF8"));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey key = keyFactory.generateSecret(keySpec);
            byte[] clearText = value.getBytes("UTF8");
            // Cipher is not thread safe
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            encrypedValue = Base64.encodeToString(cipher.doFinal(clearText), Base64.DEFAULT);
            return encrypedValue;
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
        return encrypedValue;
    }


    public static String decrypt(String value, String password) {
        String decryptedValue = "";
        try {
            DESKeySpec keySpec = new DESKeySpec(password.getBytes("UTF8"));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey key = keyFactory.generateSecret(keySpec);

            byte[] encrypedPwdBytes = Base64.decode(value, Base64.DEFAULT);
            // cipher is not thread safe
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decrypedValueBytes = (cipher.doFinal(encrypedPwdBytes));

            decryptedValue = new String(decrypedValueBytes);
        } catch (InvalidKeyException e) {
            Ln.e(e, "Error decrypting");
            return value;
        } catch (UnsupportedEncodingException e) {
            Ln.e(e, "Error decrypting");
            return value;
        } catch (InvalidKeySpecException e) {
            Ln.e(e, "Error decrypting");
            return value;
        } catch (NoSuchAlgorithmException e) {
            Ln.e(e, "Error decrypting");
            return value;
        } catch (BadPaddingException e) {
            Ln.e(e, "Error decrypting");
            return value;
        } catch (NoSuchPaddingException e) {
            Ln.e(e, "Error decrypting");
            return value;
        } catch (IllegalBlockSizeException e) {
            Ln.e(e, "Error decrypting");
            return value;
            // try-catch ensure compatibility with old masked (without encryption) values
        } catch (IllegalArgumentException e) {
            Ln.e(e, "Error decrypting");
            return value;
        }
        return decryptedValue;
    }


}
