package com.evguard.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordSort {

	private char[] chartable =  
    {  
      '啊', '芭', '擦', '搭', '蛾', '发', '噶', '哈', '哈',  
      '击', '喀', '垃', '妈', '拿', '哦', '啪', '期', '然',  
      '撒', '塌', '塌', '塌', '挖', '昔', '压', '匝', '座'  
     };  
	private char[] alphatableb =  
   {  
      'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I',  
      'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'  
    };  
	private char[] alphatables =  
   {  
      'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i',  
      'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'  
    };  
	private int[] table = new int[27];  //初始化  
   {  
          for (int i = 0; i < 27; ++i) {  
              table[i] = gbValue(chartable[i]);  
          }  
    }
   public char Char2Alpha(char ch,String type) {  
       if (ch >= 'a' && ch <= 'z')  
           return (char) (ch - 'a' + 'A');//为了按字母排序先返回大写字母  
       if (ch >= 'A' && ch <= 'Z')  
           return ch;  
          int gb = gbValue(ch);  
          if (gb < table[0])  
           return '0';  
       int i;  
          for (i = 0; i < 26; ++i) {  
           if (match(i, gb))  
                  break;  
       }  
          if (i >= 26){  
           return '0';}  
          else{  
              if("b".equals(type)){//大写  
                  return alphatableb[i];  
              }else{//小写  
                  return alphatables[i];  
              }  
          }  
   }  
 //根据一个包含汉字的字符串返回第一个汉字拼音首字母的字符串  
   public String String2AlphaFirst(String SourceStr,String type) {  
         String Result = "";  
       try {  
         Result += Char2Alpha(SourceStr.charAt(0),type);  
       } catch (Exception e) {  
         Result = "";  
       }  
    return Result;  
  }  
   private boolean match(int i, int gb) {  
          if (gb < table[i])  
             return false;  
           int j = i + 1;  
     
           //字母Z使用了两个标签  
           while (j < 26 && (table[j] == table[i]))  
               ++j;  
           if (j == 26)  
               return gb <= table[j];  
          else  
               return gb < table[j];  
        }  
   //取出汉字的编码  
   private int gbValue(char ch) {  
       String str = new String();  
       str += ch;  
       try {  
           byte[] bytes = str.getBytes("GBK");  
               if (bytes.length < 2)  
                   return 0;  
               return (bytes[0] << 8 & 0xff00) + (bytes[1] &  
                       0xff);  
           } catch (Exception e) {  
             return 0;  
           }  
       }  
   public Map sort(List list){  
        Map map=new HashMap();  
        ArrayList arraylist=new ArrayList();  
        String[] alphatableb = {  
               "A", "B", "C", "D", "E", "F", "G", "H", "I",  
               "J", "K", "L", "M", "N", "O", "P", "Q", "R", 
               "S", "T", "U", "V", "W", "X", "Y", "Z"  
         };  
        for(String a:alphatableb){  
            for(int i=0;i<list.size();i++){//为了排序都返回大写字母  
                if(a.equals(String2AlphaFirst(list.get(i).toString(),"b"))){  
                    arraylist.add(list.get(i).toString());  
                }  
            }
            if(arraylist.size()>0) map.put(a,arraylist);  
            arraylist=new ArrayList();  
        }  
        return map;  
    } 
}
