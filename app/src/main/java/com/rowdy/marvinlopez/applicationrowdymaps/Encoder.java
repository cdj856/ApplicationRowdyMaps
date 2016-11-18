package com.rowdy.marvinlopez.applicationrowdymaps;

import java.security.MessageDigest;

/**
 * Created by jonathan on 11/18/2016.
 */

public final class Encoder {
    public static String encode(String encodeThis){
        String newEncoded = "";

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(encodeThis.getBytes());

            byte byteData[] = md.digest();

            StringBuffer sbuf = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                sbuf.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }

            newEncoded = sbuf.toString();
        }catch (Exception e){
            e.printStackTrace();
        }
        return newEncoded;
    }
}
