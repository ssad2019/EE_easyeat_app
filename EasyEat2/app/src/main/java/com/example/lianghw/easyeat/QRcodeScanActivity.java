/**
 * 项目名称：易餐
 * 项目为系统分析与设计课程的课程实验项目
 * 整个项目为扫码点餐系统
 * 这部分是整个项目的手机客户端部分
 * github地址：https://github.com/ssad2019/EE_easyeat_app
 * 启动日期：2019.5.1
 */
package com.example.lianghw.easyeat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.bean.ZxingConfig;
import com.yzq.zxinglibrary.common.Constant;


/**
 * QRcodeScanActivity页面
 * 首页面
 * 显示：标题和扫码图标
 */
public class QRcodeScanActivity extends AppCompatActivity {

    // 所需的全部动态权限
    int int_request_code;
    boolean bln_is_has_permission = false;
    static final String[] permissions = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    int REQUEST_CODE_SCAN = 8;
    TextView txt_result;
    Bitmap bm_img;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //权限检测
        checkPermission();

        //加载主布居
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code_scan);

        //隐藏标题栏
        ActionBar ab_action_bar = getSupportActionBar();
        ab_action_bar.hide();


        txt_result = findViewById(R.id.txt_result);

        ImageButton ib_scan = findViewById(R.id.ib_scan);
        ib_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bln_is_has_permission) {
                    Intent intent = initQR();
                    startActivityForResult(intent, REQUEST_CODE_SCAN);
                } else {
                    Toast.makeText(QRcodeScanActivity.this, "权限不足", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


    /**
     * 二维码扫描初始化
     * @return  二维码扫描页面 Intent
     */
    Intent initQR(){
        Intent intent = new Intent(QRcodeScanActivity.this, CaptureActivity.class);
        ZxingConfig config = new ZxingConfig();
        config.setShowFlashLight(false);//不显示闪光灯按钮
        config.setShowAlbum(false);//不显示闪光灯按钮
        config.setPlayBeep(false);//不播放扫描声音
        config.setShake(true);//震动
        config.setDecodeBarCode(false);//不扫描条形码
        config.setReactColor(R.color.colorAccent);//设置扫描框四个角的颜色
        config.setFrameLineColor(R.color.colorAccent);//设置扫描框边框颜色
        config.setScanLineColor(R.color.colorAccent);//设置扫描线的颜色
        config.setFullScreenScan(false);//不全屏扫描
        intent.putExtra(Constant.INTENT_ZXING_CONFIG, config);
        return intent;

    }

    /**
     * 检查权限
     */
    void checkPermission(){
        boolean bln_is_request = false;
        for (String str_permission :permissions) {
            int i = ContextCompat.checkSelfPermission(this,str_permission);
            // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
            if (i != PackageManager.PERMISSION_GRANTED) {
                bln_is_request = true;
                break;
            }
        }
        if (bln_is_request){
            ActivityCompat.requestPermissions(this, permissions, int_request_code);
        }else{
            bln_is_has_permission = true;
        }

    }

    @Override
    public void onRequestPermissionsResult(int request_code, @NonNull String[] permissions, @NonNull int[]grant_results) {
        super.onRequestPermissionsResult(request_code, permissions, grant_results);
        if (request_code== this.int_request_code) {
            int int_size = permissions.length;
            for(int i : grant_results){
                if (grant_results[0]== PackageManager.PERMISSION_DENIED){
                    Toast.makeText(this, "权限申请失败，用户拒绝权限", Toast.LENGTH_SHORT).show();
                    bln_is_has_permission = false;
                    return;
                }
            }
            Toast.makeText(this, "权限申请成功", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 扫描二维码/条码回传
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {
                String str_content = data.getStringExtra(Constant.CODED_CONTENT);

                String str_url = Restaurant.getUrlFromQRcode(str_content);
                Network.getInstance().setIdAndNumber(str_url);
                Intent intent = new Intent(QRcodeScanActivity.this, MainActivity.class);
                intent.putExtra("data_url",str_url);
                startActivity(intent);
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
