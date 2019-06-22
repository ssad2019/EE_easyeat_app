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


    String str_id;
    String str_number;
    String PATH = "https://api.hatsune-miku.cn";
    static String SECRET = "gg";

    /**
     * 根据餐厅url，获取餐厅id和座位号
     * @param urlStr String
     */
    public void setIdAndNumber(String urlStr){
        try{
            URL url = new URL(urlStr);
            String str_query = url.getQuery();
            String[] querys = str_query.split("&");
            str_id = querys[0].substring(2);
            str_number = querys[1].substring(2);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将参数转为url
     * @param  params_name String[]
     * @param  params_value String[]
     * @return  url String
     */
    private String tranParamsToString(String[] params_name, String[] params_value){
        String str_p="";
        String str_equal = "=",str_and = "&";
        str_p += params_name[0];
        str_p += str_equal;
        str_p += params_value[0];

        for(int i = 1; i<params_name.length;i++){
            str_p += str_and;
            str_p += params_name[i];
            str_p += str_equal;
            str_p += params_value[i];
        }
        return str_p;
    }

    /**
     * 点餐
     * @param  str_order_json String 点餐json
     * @return  返回信息 String
     */
    public String orderFood(String str_order_json){
        String str_url = PATH + "/order.php";

        String[] paramsName = {"s","n","order"};
        String[] paramsValue = {str_id,str_number,str_order_json};
        return doPost(str_url,paramsName,paramsValue);
    }

    /**
     * 获取bitmap
     * @param  str_path String 图片url
     * @return  图片 Bitmap
     */
    public Bitmap getBitmap(String str_path) {
        try{
            URL str_url = new URL(str_path);
            HttpURLConnection conn = (HttpURLConnection) str_url.openConnection();
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

    /**
     * 使用Get方法访问网络
     * @param  str_url String url
     * @return  网页字符串 String
     */
    public String doGet(String str_url){
        HttpURLConnection conn = null;
        InputStream is = null;
        InputStreamReader reader = null;
        BufferedReader br = null;
        String str = "";
        try {
            URL weiUrl = new URL(str_url);
            conn = (HttpURLConnection)weiUrl.openConnection();
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.connect();
            is = conn.getInputStream();
            reader = new InputStreamReader(is, "UTF-8");
            br = new BufferedReader(reader);
            String str_readLine = "";
            while((str_readLine=br.readLine())!=null){
                str+=str_readLine+"\n";
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

    /**
     * 使用Pos方法访问网络
     * @param  url_str String url
     * @param  params_name String[]
     * @param  params_value String[]
     * @return  网页字符串 String
     */
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

            String str_p = tranParamsToString(params_name,params_value);
            out.writeBytes(str_p);
            out.close();

            is = conn.getInputStream();
            reader = new InputStreamReader(is, "UTF-8");
            br = new BufferedReader(reader);
            String str_readLine = "";
            while((str_readLine=br.readLine())!=null){
                str+=str_readLine+"\n";
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


    /**
     * 检测网络
     * @param context Context 上下文本
     * @return 网络连接成功 boolean
     */
    boolean isConnectInternet(Context context){

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



}
