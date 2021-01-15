package net.qzwxsaedc.hiunattendedreport;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {
    private static MessageDigest md5 = null;
    public static String cal(final byte[] buffer) {
        if(md5 == null)
            try{
                md5 = MessageDigest.getInstance("MD5");
            }catch (NoSuchAlgorithmException e){
                e.printStackTrace();
                return null;
            }
        return bytesToHexString(md5.digest(buffer));
    }
    private static String bytesToHexString(byte[] src){
        StringBuilder sb = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (byte b : src) {
            int v = b & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2)
                sb.append(0);
            sb.append(hv);
        }
        return sb.toString();
    }
}
