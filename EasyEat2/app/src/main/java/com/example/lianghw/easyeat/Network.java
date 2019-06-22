/**
 * 项目名称：易餐
 * 项目为系统分析与设计课程的课程实验项目
 * 整个项目为扫码点餐系统
 * 这部分是整个项目的手机客户端部分
 * github地址：https://github.com/ssad2019/EE_easyeat_app
 * 启动日期：2019.5.1
 */
/**
 * Network
 * 访问网络
 * 通过url使用post/get方法获取string/bitmap
 */
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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Network {

    private static Network instance = new Network();
    private Network(){}
    public static Network getInstance(){
        return instance;
    }

    Context context;
    String id;
    String number;
    String path = "https://api.hatsune-miku.cn";
    static String SECRET = "gg";


    public void setIdAndNumber(String urlStr){
        try{
            URL url = new URL(urlStr);
            String query = url.getQuery();
            String[] querys = query.split("&");
            id = querys[0].substring(2);
            number = querys[1].substring(2);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }


    private String tranParamsToString(String[] params_name, String[] params_value){
        String pStr="";
        String equal = "=",and = "&";
        pStr += params_name[0];
        pStr += equal;
        pStr += params_value[0];

        for(int i = 1; i<params_name.length;i++){
            pStr += and;
            pStr += params_name[i];
            pStr += equal;
            pStr += params_value[i];
        }
        return pStr;
    }

    public String orderFood(String order_json){
        String url = path + "/order.php";

        String[] paramsName = {"s","n","order"};
        String[] paramsValue = {id,number,order_json};
        return doPost(url,paramsName,paramsValue);
    }
    public Bitmap getBitmap(String path) {
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

    public String doGet(String url_str){

        HttpURLConnection conn = null;
        InputStream is = null;
        InputStreamReader reader = null;
        BufferedReader br = null;
        String str = "";
        try {
            URL weiUrl = new URL(url_str);
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

    public String doPost( String url_str,String[] params_name, String[] params_value){
        HttpURLConnection conn = null;
        InputStream is = null;
        InputStreamReader reader = null;
        BufferedReader br = null;
        String str = "";
        try {
            URL url = new URL(url_str);
            conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);//默觉得false的，所以须要设置
            conn.setUseCaches(false);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.connect();

            OutputStream outStream = conn.getOutputStream();
            DataOutputStream out = new DataOutputStream(outStream);

            String pStr = tranParamsToString(params_name,params_value);
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
        NetworkInfo network_info = manager.getActiveNetworkInfo();
        if(network_info==null||(!network_info.isAvailable())){
            Toast.makeText(context,"网络连接失败",Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;

    }

    public static byte[] readStream(InputStream in_stream) throws Exception{
        ByteArrayOutputStream out_stream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while((len = in_stream.read(buffer)) != -1)
        {
            out_stream.write(buffer,0,len);
        }
        in_stream.close();
        return out_stream.toByteArray();
    }


}
