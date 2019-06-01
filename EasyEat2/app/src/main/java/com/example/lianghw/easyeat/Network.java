package com.example.lianghw.easyeat;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class Network {
    Context context;
    Network(Context context){
        this.context = context;

    }

    public String getHtml(String path) throws Exception {
        URL url = new URL(path);
        URLConnection conn = (URLConnection) url.openConnection();
        //HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5000);
        conn.set
        conn.setRequestMethod("GET");
        int code = conn.getResponseCode();
        if (code == 200) {
            InputStream in = conn.getInputStream();
            byte[] data = readStream(in);
            return new String(data, "UTF-8");
        }
        return null;
    }
    boolean isConnectInternet(){

        //是否飞行模式
        if(Settings.System.getInt(context.getContentResolver(),Settings.System.AIRPLANE_MODE_ON, 0)==1){
            Toast.makeText(context,"飞行模式",Toast.LENGTH_SHORT).show();
            return false;
        }

        //是否有可用网络
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if(networkInfo==null||(!networkInfo.isAvailable())){
            Toast.makeText(context,"网络连接失败",Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;

    }

    public static byte[] readStream(InputStream inStream) throws Exception{
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while((len = inStream.read(buffer)) != -1)
        {
            outStream.write(buffer,0,len);
        }
        inStream.close();
        return outStream.toByteArray();
    }

}
