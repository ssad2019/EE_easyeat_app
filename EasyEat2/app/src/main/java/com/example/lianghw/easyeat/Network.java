package com.example.lianghw.easyeat;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class Network {
    Context context;
    static String SECRET = "gg";
    Network(Context context){
        this.context = context;

    }

    private static String paramsToString(String[] paramsName, String[] paramsValu){
        String pStr="";
        String equal = "=",and = "&";
        pStr += paramsName[0];
        pStr += equal;
        pStr += paramsValu[0];

        for(int i = 1; i<paramsName.length;i++){
            pStr += and;
            pStr += paramsName[i];
            pStr += equal;
            pStr += paramsValu[i];
        }
        return pStr;
    }

    public static String submitOrders(String url,String id,String number,String order){
        url = url + "/order.php";
        String[] paramsName = {"s","n","order"};
        String[] paramsValue = {id,number,order};
        return doPost(url,paramsName,paramsValue);
    }


    public static Bitmap getBitmap(String path) {

        try{
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() == 200){
                InputStream inputStream = conn.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String doGet(String urlStr,String[] paramsName, String[] paramsValue) {
        String url = urlStr;
        if(paramsName.length != 0){
            url = urlStr + "?" + paramsToString(paramsName,paramsValue);
        }
        return doGet(url);
    }
    public static String doGet(String urlStr){

        HttpURLConnection conn = null;
        InputStream is = null;
        InputStreamReader reader = null;
        BufferedReader br = null;
        String str = "";
        try {
            URL weiUrl = new URL(urlStr);
            conn = (HttpURLConnection)weiUrl.openConnection();
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.connect();
            is = conn.getInputStream();
            reader = new InputStreamReader(is, "UTF-8");
            br = new BufferedReader(reader);
            String readLine = "";
            while((readLine=br.readLine())!=null){
                str+=readLine+"\n";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            try{
                if(br!=null){
                    br.close();
                }
                if(conn!=null){
                    conn.disconnect();
                }
            }catch(Exception e1){
                e1.printStackTrace();
            }
        }
        return str;
    }

    public static String doPost( String urlStr,String[] paramsName, String[] paramsValue){
        HttpURLConnection conn = null;
        InputStream is = null;
        InputStreamReader reader = null;
        BufferedReader br = null;
        String str = "";
        try {
            URL url = new URL(urlStr);
            conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);//默觉得false的，所以须要设置
            conn.setUseCaches(false);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.connect();

            OutputStream outStream = conn.getOutputStream();
            DataOutputStream out = new DataOutputStream(outStream);

            String pStr = paramsToString(paramsName,paramsValue);
            out.writeBytes(pStr);
            out.close();

            is = conn.getInputStream();
            reader = new InputStreamReader(is, "UTF-8");
            br = new BufferedReader(reader);
            String readLine = "";
            while((readLine=br.readLine())!=null){
                str+=readLine+"\n";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            try{
                if(br!=null){
                    br.close();
                }
                if(conn!=null){
                    conn.disconnect();
                }
            }catch(Exception e1){
                e1.printStackTrace();
            }
        }
        return str;
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
