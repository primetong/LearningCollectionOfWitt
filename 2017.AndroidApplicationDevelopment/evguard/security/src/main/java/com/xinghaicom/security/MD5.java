package com.xinghaicom.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.util.Base64;

public class MD5 {
    
    /// <summary>
    /// 鑾峰緱MD5鍔犲瘑瀵嗘枃
    /// </summary>
    /// <param name="strToHash"></param>
    /// <returns></returns>
    public String hashTextMD5(String strToHash) throws Exception {
    	
    	if (strToHash == null || strToHash.length() <= 0) throw new NullPointerException("要加密的密文无效！");
                            
        MessageDigest md = MessageDigest.getInstance("MD5");  
        md.update(strToHash.getBytes("UTF-8"));  
        byte[] digest = md.digest();

        //Base64.encode(digest,0);
        //String v = "gg";
        //String base64 = 
        //System.out.println(base64);
        return Base64.encodeToString(digest, Base64.DEFAULT);  
    }
    
    public String hashToDigestDes(String strToHash) throws Exception {
    	if (strToHash == null || strToHash.length() <= 0) throw new NullPointerException("要加密的密文无效！");
        
        MessageDigest md = MessageDigest.getInstance("MD5");  
        md.update(strToHash.getBytes("UTF-8"));  
        byte[] digest = md.digest();

        String result = "";
        for(byte singleDigest:digest){
        	result += String.format("%02x", singleDigest);
        }
        
        return result;
    }
}
