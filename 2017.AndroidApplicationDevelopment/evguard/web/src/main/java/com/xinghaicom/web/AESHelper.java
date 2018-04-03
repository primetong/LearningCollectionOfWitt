package com.xinghaicom.web;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


public class AESHelper {


	/** 
	 * AES 算法 对称加密，密码学中的高级加密标准 2005年成为有效标准  
	 * @author stone 
	 * @date 2014-03-10 06:49:19 
	 */   
    static Cipher cipher;  
    static final String KEY_ALGORITHM = "AES";  
    static final String CIPHER_ALGORITHM_ECB = "AES/ECB/PKCS5Padding";  
    /* 
     *  
     */  
    static final String CIPHER_ALGORITHM_CBC = "AES/CBC/PKCS5Padding";  
    /*  
     * AES/CBC/NoPadding 要求 
     * 密钥必须是16位的；Initialization vector (IV) 必须是16位 
     * 待加密内容的长度必须是16的倍数，如果不是16的倍数，就会出如下异常： 
     * javax.crypto.IllegalBlockSizeException: Input length not multiple of 16 bytes 
     *  
     *  由于固定了位数，所以对于被加密数据有中文的, 加、解密不完整 
     *   
     *  可 以看到，在原始数据长度为16的整数n倍时，假如原始数据长度等于16*n，则使用NoPadding时加密后数据长度等于16*n， 
     *  其它情况下加密数据长 度等于16*(n+1)。在不足16的整数倍的情况下，假如原始数据长度等于16*n+m[其中m小于16]， 
     *  除了NoPadding填充之外的任何方 式，加密数据长度都等于16*(n+1). 
     */  
    static final String CIPHER_ALGORITHM_CBC_NoPadding = "AES/CBC/NoPadding";   
      
    static SecretKey secretKey;  
          
     
    /**
     * 获取key
     * @return
     */
    public static byte[] getKey() {  
        String key = "ABCDEFGHIJKLMNOP"; //IV length: must be 16 bytes long  
        return getUTF8Byte(key);
    }  
    /**
     * 获取算法向量
     * @return
     */
    private static byte[] getIV() {  
        String iv = "ABCDEFGHIJKLMNOP"; //IV length: must be 16 bytes long  
		return getUTF8Byte(iv);

    }  
    private static byte[] getUTF8Byte(String s)
    {
    	 try {
 			return s.getBytes("UTF-8");
 		} catch (UnsupportedEncodingException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 			return s.getBytes();
 		}
    }
    /** 
     * 使用AES 算法 加密，默认模式  AES/ECB 
     */  

	public static void method_AES_ECB(String str) throws Exception {  
    	
    	cipher = Cipher.getInstance(KEY_ALGORITHM);  
        //KeyGenerator 生成aes算法密钥  
        secretKey = KeyGenerator.getInstance(KEY_ALGORITHM).generateKey();  

        cipher.init(Cipher.ENCRYPT_MODE, secretKey);//使用加密模式初始化 密钥  
        byte[] encrypt = cipher.doFinal(getUTF8Byte(str)); //按单部分操作加密或解密数据，或者结束一个多部分操作。  	       
//        Log.i("xxx", "method1-加密后：" + Base64.encodeToString(encrypt, Base64.NO_WRAP));
        
        cipher.init(Cipher.DECRYPT_MODE, secretKey);//使用解密模式初始化 密钥  
        byte[] decrypt = cipher.doFinal(encrypt);  
//        Log.i("xxx", "method1-解密后：" + new String(decrypt,"UTF-8"));
        
        String encoding = System.getProperty("file.encoding");
//        Log.i("xxx", "method1-默认编码：" +encoding);
    }  
      
    /** 
     * 使用AES 算法 加密，默认模式 AES/ECB/PKCS5Padding 
     */  

	public static void method_AES_ECB_PKCS5Padding(String str) throws Exception {  
        cipher = Cipher.getInstance(CIPHER_ALGORITHM_ECB);  
        //KeyGenerator 生成aes算法密钥  
        secretKey = KeyGenerator.getInstance(KEY_ALGORITHM).generateKey();  

        cipher.init(Cipher.ENCRYPT_MODE, secretKey);//使用加密模式初始化 密钥  
        byte[] encrypt = cipher.doFinal(str.getBytes()); //按单部分操作加密或解密数据，或者结束一个多部分操作。   
//        Log.i("xxx", "method2-加密后：" + Base64.encodeToString(encrypt, Base64.NO_WRAP));
        
        cipher.init(Cipher.DECRYPT_MODE, secretKey);//使用解密模式初始化 密钥  
        byte[] decrypt = cipher.doFinal(encrypt);  
  
    }  
      
   
      
    /** 
     * 使用AES 算法 加密，默认模式 AES/CBC/PKCS5Padding --目前采用这种和c#保持一致
     */  
	public static String method_AES_CBC_PKCS5Padding(String str) {  
        try {
        	if(str==null||"".equals(str))return str;
			cipher = Cipher.getInstance(CIPHER_ALGORITHM_CBC);  
			//KeyGenerator 生成aes算法密钥  
			secretKey=new SecretKeySpec(getKey(),"AES");

			cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(getIV()));//使用加密模式初始化 密钥  
			byte[] encrypt = cipher.doFinal(str.getBytes()); //按单部分操作加密或解密数据，或者结束一个多部分操作。  
			  
//			System.out.println("method3-加密：" + Arrays.toString(encrypt));  
//			Log.i("xxx", "method3-加密后：" + Base64.encodeToString(encrypt, Base64.NO_WRAP));

			return Base64.encodeToString(encrypt, Base64.NO_WRAP);
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "";
		}

    }  
      
    /** 
     * 使用AES 算法 加密，默认模式 AES/CBC/NoPadding  参见上面对于这种mode的数据限制 
     */  
    public static void method_AES_CBC_NoPadding(String str) throws Exception {  
    	
    	cipher = Cipher.getInstance(CIPHER_ALGORITHM_CBC_NoPadding);  
        //KeyGenerator 生成aes算法密钥  
        secretKey = KeyGenerator.getInstance(KEY_ALGORITHM).generateKey();  

        cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(getIV()));//使用加密模式初始化 密钥  
        byte[] encrypt = cipher.doFinal(str.getBytes(), 0, str.length()); //按单部分操作加密或解密数据，或者结束一个多部分操作。  
           
        cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(getIV()));//使用解密模式初始化 密钥  
        byte[] decrypt = cipher.doFinal(encrypt);  

    }      
    
    //====================================解密============================
    /** 
     * 解密 模式 AES/CBC/PKCS5Padding --目前采用这种和c#保持一致
     */  
	public static String method_DeEncrpt_AES_CBC_PKCS5Padding(String str)  {  

		try {
			if(str==null||"".equals(str))return str;
			byte[] bdata=Base64.decode(str, Base64.NO_WRAP);
			cipher = Cipher.getInstance(CIPHER_ALGORITHM_CBC);  
			secretKey=new SecretKeySpec(getKey(),"AES");
			cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(getIV()));//使用解密模式初始化 密钥  
			byte[] decrypt = cipher.doFinal(bdata);  
//			Log.i("xxx","method3-解密后：" + new String(decrypt));  
			return new String(decrypt);
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}
}
