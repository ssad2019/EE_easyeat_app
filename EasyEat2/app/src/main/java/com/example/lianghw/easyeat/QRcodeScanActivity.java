package com.example.lianghw.easyeat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.bean.ZxingConfig;
import com.yzq.zxinglibrary.common.Constant;


public class QRcodeScanActivity extends AppCompatActivity {
    final String Path ="https://api.hatsune-miku.cn";
    // 所需的全部动态权限
    int RequestCode;
    boolean hasPermission = false;
    static final String[] permissions = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    int REQUEST_CODE_SCAN = 8;
    TextView result;
    ImageView imgView;
    String detail;
    Bitmap img;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0x002:
                    result.setText(detail);
                    Toast.makeText(QRcodeScanActivity.this, "HTML代码加载完毕", Toast.LENGTH_SHORT).show();
                    imgView.setImageBitmap(img);
                    break;
                default:
                    break;
            }
        }

        ;
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        CheckPermission();

        //加载主布居
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code_scan);

        //隐藏标题栏
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();



        result = findViewById(R.id.result);
        imgView = findViewById(R.id.img);
        ImageButton button1 = findViewById(R.id.btn1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hasPermission) {
                    Intent intent = initQR();
                    startActivityForResult(intent, REQUEST_CODE_SCAN);
                } else {
                    Toast.makeText(QRcodeScanActivity.this, "权限不足", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


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
    void CheckPermission(){
        boolean request = false;
        for (String permission :permissions) {
            int i = ContextCompat.checkSelfPermission(this,permission);
            // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
            if (i != PackageManager.PERMISSION_GRANTED) {
                request = true;
                break;
            }
        }
        if (request){
            ActivityCompat.requestPermissions(this, permissions, RequestCode);
        }else{
            hasPermission = true;
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==RequestCode) {
            int size = permissions.length;
            for(int i : grantResults){
                if (grantResults[0]== PackageManager.PERMISSION_DENIED){
                    Toast.makeText(this, "权限申请失败，用户拒绝权限", Toast.LENGTH_SHORT).show();
                    hasPermission = false;
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

                String content = data.getStringExtra(Constant.CODED_CONTENT);
                result.setText("扫描结果为：" + content);
                String url = Restaurant.getUrlFromQRcode(content);
                Network.getInstance().setIdAndNumber(url);
                Intent intent = new Intent(QRcodeScanActivity.this, MainActivity.class);
                intent.putExtra("data_url",url);
                startActivity(intent);
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
