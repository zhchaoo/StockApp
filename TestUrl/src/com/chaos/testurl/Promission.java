package com.chaos.testurl;

import java.io.IOException;

import android.util.Log;

public class Promission {
	  private static final Runtime runtime = Runtime.getRuntime();
	  
	  public static void changePermission(String paramString, int paramInt)
	  {
	    String str1 = "changePermission: " + paramString;
	    int i = Log.d("DrocapService", str1);
	    try
	    {
	      String[] arrayOfString = new String[3];
	      arrayOfString[0] = "/system/bin/chmod";
	      Object[] arrayOfObject = new Object[1];
	      Integer localInteger = Integer.valueOf(paramInt);
	      arrayOfObject[0] = localInteger;
	      String str2 = String.format("%o", arrayOfObject);
	      arrayOfString[1] = str2;
	      arrayOfString[2] = paramString;
	      
	      Log.v("testurl", arrayOfString[0]+" "+arrayOfString[1]+" "+arrayOfString[2]);
	      
	      Process localProcess1 = runtime.exec(arrayOfString);
	      Process localProcess2 = localProcess1;
	      try
	      {
	        if (localProcess2.waitFor() != 0)
	        {
	          StringBuilder localStringBuilder1 = new StringBuilder("changePermission: chmod returns ");
	          int j = localProcess2.exitValue();
	          String str3 = String.valueOf(j);
	          int k = Log.d("DrocapService", str3);
	        }
	        return;
	      }
	      catch (InterruptedException localInterruptedException)
	      {
	        while (true)
	          localInterruptedException.printStackTrace();
	      }
	    }
	    catch (IOException localIOException)
	    {
	      while (true)
	      {
	        StringBuilder localStringBuilder2 = new StringBuilder("changePermission: ");
	        String str4 = localIOException.toString();
	        String str5 = str4;
	        int m = Log.e("DrocapService", str5);
	      }
	    }
	  }
	
}
