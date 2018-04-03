package com.xinghaicom.security;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import android.util.Base64;
import android.util.Log;
import android.util.Xml.Encoding;

public class AuthorityIDDecipherer {
	
    protected byte[] mkeyN = {-23, 46, -46, 103, 39, -124, 123, -121,
	    -5, -36, -74, -122, 117, 45, 86, -74,
	    -62, -14, 45, 5, -103, -60, 70, 34,
	    -32, -66, -95, -115, -27, 101, 112, -75,
	    -126, 97, 64, -38, 11, -25, 19, 12,
	    -85, 15, 38, 70, 92, 99, -92, -5,
	    12, -51, 52, -102, 72, -91, 2, 1,
	    110, -83, 87, -36, 76, 99, 46, 31,
	    27, -35, 74, -89, -25, 126, 1, 64,
	    -18, -116, 91, -2, -36, -24, 114, 22,
	    -53, -115, 97, -12, -113, 85, 48, -112,
	    108, 7, 13, -2, 93, -52, 0, -54,
	    -61, -116, 90, -62, 75, -58, 92, -77,
	    111, -126, 94, -55, -27, -68, 36, -34,
	    85, 62, 66, 41, -21, 123, 49, 91,
	    -64, 109, -100, -24, -34, 56, -46, -41
    };
    
    protected java.math.BigInteger mbiN = new java.math.BigInteger(1,mkeyN);

    //protected String mMyPublicKey = "EzK9HcfPA2zj4wouO9lMww==aCyQIQEEWMt/Kv3j1BHGdg==";
    protected String mMyPublicKey = "aCyQIQEEWMt/Kv3j1BHGdg==";
	
    protected String getPublicKey() throws Exception{

//        String strMd5 = mMyPublicKey.substring(0, 24);
//        String strSymm = mMyPublicKey.substring(24);//, mMyPublicKey.length() - 24);
//        AuthorityAESCipherer symm = new AuthorityAESCipherer();
//        String strOrignal = symm.decrypto(strSymm);
//        if (strOrignal == null || strOrignal.length() <= 0) return null;
//        MD5 md5 = new MD5();
//        String strMd5Another = md5.hashTextMD5(strOrignal).trim();
//        if (strMd5Another == null || strMd5Another.length() <= 0) return null;
//        if (strMd5.contentEquals(strMd5Another)) return strOrignal;
//        return null;
    	
    	return new AuthorityAESCipherer().decrypto(mMyPublicKey);
    }
   
	public String decipher(String sData) throws Exception{		
		if(sData == null || sData.length() <= 0) return null;        
		String strKey = getPublicKey();
        if(strKey == null || strKey.length() <= 0) return null;

        java.math.BigInteger bigInteger = java.math.BigInteger.valueOf(Long.parseLong(strKey)); //new java.math.BigInteger(Long.parseLong(strKey));
        byte[] bDataBytes = Base64.decode(sData, Base64.DEFAULT);
        //byte[] bDataBytes = Convert.FromBase64String(sData);

        int iLen = bDataBytes.length;
        int iLen1 = 0;
        int iLockLen = 0;
        if (iLen % 128 == 0)
        {
            iLen1 = iLen / 128;
        }
        else
        {
            iLen1 = iLen / 128 + 1;
        }
        List<Byte> lTempBytes = new ArrayList<Byte>();
        for (int i = 0; i < iLen1; i++)
        {
            if (iLen >= 128)
            {
                iLockLen = 128;
            }
            else
            {
                iLockLen = iLen;
            }
            byte[] bText = new byte[iLockLen];
            System.arraycopy(bDataBytes, i * 128, bText, 0, iLockLen);
            //Array.Copy(bDataBytes, i * 128, bText, 0, iLockLen);
            java.math.BigInteger biText = new java.math.BigInteger(1,bText);
            java.math.BigInteger biEnText = biText.modPow(bigInteger, mbiN);
            byte[] bTestByte = biEnText.toByteArray();//.getBytes();

            String sStr = new String(bTestByte, "UTF-8"); //Encoding.UTF_8..getString(bTestByte);
            
            for(byte singlebyte:bTestByte){
            	lTempBytes.add(singlebyte);//.AddRange(bTestByte);
            }
                        
            iLen -= iLockLen;
        }
        
        //Byte[] lTempBytesArray = lTempBytes.toArray(new Byte[]{});
        byte[] lTempBytesArray = new byte[lTempBytes.size()];
        for(int i=0; i<lTempBytesArray.length; i++){
        	lTempBytesArray[i] = lTempBytes.get(i);
        }
                        
        //System.arraycopy(lTempBytesArray, 0, lTempbytesArray, 0, lTempBytesArray.length);
                
        String deAuthorityID = new String(lTempBytesArray, "UTF-8");   //Encoding.UTF8.GetString(lTempBytes.ToArray());
        
        return deAuthorityID;
	}
//
//
//
//
//
//
//
//	public class BigInteger{
//		
//		
//		
//	}

}
