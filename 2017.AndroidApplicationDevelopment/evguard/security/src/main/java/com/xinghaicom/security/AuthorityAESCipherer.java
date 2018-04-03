package com.xinghaicom.security;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import android.util.Base64;

public class AuthorityAESCipherer {
	
	protected Cipher mCipher = null;
	protected SecretKeySpec mKey = null;
	
	public AuthorityAESCipherer() throws NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException, InvalidKeyException{		
		mCipher = Cipher.getInstance("AES/ECB/NoPadding");		        
		mKey = generateKey();
		mCipher.init(Cipher.DECRYPT_MODE, mKey);
	}
	
	
	public SecretKeySpec generateKey() throws NoSuchAlgorithmException, UnsupportedEncodingException{
				
//		KeyGenerator kgen = KeyGenerator.getInstance("AES");  
//		kgen.init(256);  
//	    SecretKey secretKey = kgen.generateKey();  
//	    byte[] raw = secretKey.getEncoded();
	    
	    String sTemp = "1wf!!#\\nc^Dmq*(hugd*FTp9edGUT?E*ldHsdi!}{+_hk$ojnh/#O8%mgcxcg@dddS";

        int rawLength = 32;//raw.length;
        if (sTemp.length() > rawLength)
            sTemp = sTemp.substring(0, rawLength);
        else if (sTemp.length() < rawLength)
            sTemp = String.format("%1$-" + String.valueOf(rawLength - sTemp.length()) + "s", sTemp);
        
        byte[] newRaw = sTemp.getBytes("ASCII");
	    
		
		return new SecretKeySpec(newRaw,"AES");
	}

    public byte[] unPaddingZeros(byte[] arr){  
          
        int i = 0;  
        for (; i < arr.length; i++) {  
            if(arr[i] == 0x00){  
                break;  
            }  
        }
        
        byte [] result = new byte [i];
        System.arraycopy(arr, 0, result, 0, i);

        return result;  
    }  

    public String decrypto(String source) throws UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException {
    	
        if (source == null || source.length() <= 0) return null;

        byte[] encrypted =  Base64.decode(source, Base64.DEFAULT);
        
        byte[] original = mCipher.doFinal(encrypted);
        //original = unPaddingZeros(original);
        
        return new String(original,"UTF-8").replace('\0', ' ').trim();
    }
	
	
	
}
