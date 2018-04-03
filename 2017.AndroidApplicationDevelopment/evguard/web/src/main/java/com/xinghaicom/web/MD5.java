package com.xinghaicom.web;

import java.security.MessageDigest;

import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

public class MD5 {
    
    /// <summary>
    /// 获得MD5加密密文
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
