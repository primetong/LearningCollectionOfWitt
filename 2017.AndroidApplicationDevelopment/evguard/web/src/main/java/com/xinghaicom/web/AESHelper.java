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
	 * AES �㷨 �ԳƼ��ܣ�����ѧ�еĸ߼����ܱ�׼ 2005���Ϊ��Ч��׼  
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
     * AES/CBC/NoPadding Ҫ�� 
     * ��Կ������16λ�ģ�Initialization vector (IV) ������16λ 
     * ���������ݵĳ��ȱ�����16�ı������������16�ı������ͻ�������쳣�� 
     * javax.crypto.IllegalBlockSizeException: Input length not multiple of 16 bytes 
     *  
     *  ���ڹ̶���λ�������Զ��ڱ��������������ĵ�, �ӡ����ܲ����� 
     *   
     *  �� �Կ�������ԭʼ���ݳ���Ϊ16������n��ʱ������ԭʼ���ݳ��ȵ���16*n����ʹ��NoPaddingʱ���ܺ����ݳ��ȵ���16*n�� 
     *  ��������¼������ݳ� �ȵ���16*(n+1)���ڲ���16��������������£�����ԭʼ���ݳ��ȵ���16*n+m[����mС��16]�� 
     *  ����NoPadding���֮����κη� ʽ���������ݳ��ȶ�����16*(n+1). 
     */  
    static final String CIPHER_ALGORITHM_CBC_NoPadding = "AES/CBC/NoPadding";   
      
    static SecretKey secretKey;  
          
     
    /**
     * ��ȡkey
     * @return
     */
    public static byte[] getKey() {  
        String key = "ABCDEFGHIJKLMNOP"; //IV length: must be 16 bytes long  
        return getUTF8Byte(key);
    }  
    /**
     * ��ȡ�㷨����
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
     * ʹ��AES �㷨 ���ܣ�Ĭ��ģʽ  AES/ECB 
     */  

	public static void method_AES_ECB(String str) throws Exception {  
    	
    	cipher = Cipher.getInstance(KEY_ALGORITHM);  
        //KeyGenerator ����aes�㷨��Կ  
        secretKey = KeyGenerator.getInstance(KEY_ALGORITHM).generateKey();  

        cipher.init(Cipher.ENCRYPT_MODE, secretKey);//ʹ�ü���ģʽ��ʼ�� ��Կ  
        byte[] encrypt = cipher.doFinal(getUTF8Byte(str)); //�������ֲ������ܻ�������ݣ����߽���һ���ಿ�ֲ�����  	       
//        Log.i("xxx", "method1-���ܺ�" + Base64.encodeToString(encrypt, Base64.NO_WRAP));
        
        cipher.init(Cipher.DECRYPT_MODE, secretKey);//ʹ�ý���ģʽ��ʼ�� ��Կ  
        byte[] decrypt = cipher.doFinal(encrypt);  
//        Log.i("xxx", "method1-���ܺ�" + new String(decrypt,"UTF-8"));
        
        String encoding = System.getProperty("file.encoding");
//        Log.i("xxx", "method1-Ĭ�ϱ��룺" +encoding);
    }  
      
    /** 
     * ʹ��AES �㷨 ���ܣ�Ĭ��ģʽ AES/ECB/PKCS5Padding 
     */  

	public static void method_AES_ECB_PKCS5Padding(String str) throws Exception {  
        cipher = Cipher.getInstance(CIPHER_ALGORITHM_ECB);  
        //KeyGenerator ����aes�㷨��Կ  
        secretKey = KeyGenerator.getInstance(KEY_ALGORITHM).generateKey();  

        cipher.init(Cipher.ENCRYPT_MODE, secretKey);//ʹ�ü���ģʽ��ʼ�� ��Կ  
        byte[] encrypt = cipher.doFinal(str.getBytes()); //�������ֲ������ܻ�������ݣ����߽���һ���ಿ�ֲ�����   
//        Log.i("xxx", "method2-���ܺ�" + Base64.encodeToString(encrypt, Base64.NO_WRAP));
        
        cipher.init(Cipher.DECRYPT_MODE, secretKey);//ʹ�ý���ģʽ��ʼ�� ��Կ  
        byte[] decrypt = cipher.doFinal(encrypt);  
  
    }  
      
   
      
    /** 
     * ʹ��AES �㷨 ���ܣ�Ĭ��ģʽ AES/CBC/PKCS5Padding --Ŀǰ�������ֺ�c#����һ��
     */  
	public static String method_AES_CBC_PKCS5Padding(String str) {  
        try {
        	if(str==null||"".equals(str))return str;
			cipher = Cipher.getInstance(CIPHER_ALGORITHM_CBC);  
			//KeyGenerator ����aes�㷨��Կ  
			secretKey=new SecretKeySpec(getKey(),"AES");

			cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(getIV()));//ʹ�ü���ģʽ��ʼ�� ��Կ  
			byte[] encrypt = cipher.doFinal(str.getBytes()); //�������ֲ������ܻ�������ݣ����߽���һ���ಿ�ֲ�����  
			  
//			System.out.println("method3-���ܣ�" + Arrays.toString(encrypt));  
//			Log.i("xxx", "method3-���ܺ�" + Base64.encodeToString(encrypt, Base64.NO_WRAP));

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
     * ʹ��AES �㷨 ���ܣ�Ĭ��ģʽ AES/CBC/NoPadding  �μ������������mode���������� 
     */  
    public static void method_AES_CBC_NoPadding(String str) throws Exception {  
    	
    	cipher = Cipher.getInstance(CIPHER_ALGORITHM_CBC_NoPadding);  
        //KeyGenerator ����aes�㷨��Կ  
        secretKey = KeyGenerator.getInstance(KEY_ALGORITHM).generateKey();  

        cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(getIV()));//ʹ�ü���ģʽ��ʼ�� ��Կ  
        byte[] encrypt = cipher.doFinal(str.getBytes(), 0, str.length()); //�������ֲ������ܻ�������ݣ����߽���һ���ಿ�ֲ�����  
           
        cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(getIV()));//ʹ�ý���ģʽ��ʼ�� ��Կ  
        byte[] decrypt = cipher.doFinal(encrypt);  

    }      
    
    //====================================����============================
    /** 
     * ���� ģʽ AES/CBC/PKCS5Padding --Ŀǰ�������ֺ�c#����һ��
     */  
	public static String method_DeEncrpt_AES_CBC_PKCS5Padding(String str)  {  

		try {
			if(str==null||"".equals(str))return str;
			byte[] bdata=Base64.decode(str, Base64.NO_WRAP);
			cipher = Cipher.getInstance(CIPHER_ALGORITHM_CBC);  
			secretKey=new SecretKeySpec(getKey(),"AES");
			cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(getIV()));//ʹ�ý���ģʽ��ʼ�� ��Կ  
			byte[] decrypt = cipher.doFinal(bdata);  
//			Log.i("xxx","method3-���ܺ�" + new String(decrypt));  
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
