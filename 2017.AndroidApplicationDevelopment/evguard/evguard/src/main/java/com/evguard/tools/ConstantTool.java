package com.evguard.tools;

import android.graphics.Typeface;

/**
 * 
 */
public class ConstantTool {
    public final static int AC_RES_OK=0;
    public final static int AC_REQ_CLIPIMAGE=1;//请求裁剪图像ac
    public final static int AC_REQ_SELECTIMAGE=2;//请求选择图像ac
    public final static int AC_REQ_CAPTURE=3;//请求拍照ac
    public final static int AC_REQ_ADDFENCE=4;//请求添加围栏ac
    public final static int AC_RES_BACK=5;//ac返回
    
    //AC_KidInfoUpdate
    public final static String INTENT_EXTRANAME_DATA1="1";
    public final static String INTENT_EXTRANAME_DATA2="2";
    
    //头像尺寸
    public final static int KIDHEADIMGWIDTHMIN=48;//小图
    public final static int KIDHEADIMGWIDTHMIDDLE=64;//中图
    public final static int KIDHEADIMGWIDTHMAX=128;//大图
    public final static int KIDHEADIMGSIZE=32;//头像压缩后的最大值
    
    public final static int WEBREQ_CONNECTTIME=10000;//连接时间
    public final static int WEBREQ_TIMEOUT=10000;//超时时间

    public final static int LOGINTIMEOUT=1000*60*10;//ms 
    
   
    public final static int WEBREQ_CONNECTTIME_DEFAULT=15000;//连接时间
    public final static int WEBREQ_TIMEOUT_DEFAULT=15000;//超时时间


    public final static int REALTIMEPOSITION_SPAN=30*1000;//ms
    
  
    public final static int iMaxPlaySleepTime=600;
    public final static int iMinPlaySleepTime=100;
    
    public final static int TRACKMAXDAY=3;
    
    
    //消息是否已读状态
    public final static String MSG_READED = "1";
    public final static String MSG_UNREAD = "0";
    
    
    public final static String PUSH_INTENTDATA="com.evguard.pushdata";
    public final static String PUSH_BROCAST="com.evguard.push";
}
