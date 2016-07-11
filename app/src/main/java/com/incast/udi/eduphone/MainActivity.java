package com.incast.udi.eduphone;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.cmm.incast.client;
import com.incast.udi.eduphone.utils.StatusBarUtils;
import com.incast.udi.eduphone.utils.ToastUtils;
import com.incast.udi.eduphone.utils.ValidateUtils;
import com.incast.udi.eduphone.vo.DeviceInfo;

public class MainActivity extends Activity {

    private Context mContext ;

    private EditText userNameEd;
    private EditText passWordEd;

    private LinearLayout loginLayout;
    private LinearLayout barcodeLayout;

    private final static int SCANNIN_GREQUEST_CODE = 1;

    private void initViews() {
        userNameEd = (EditText)findViewById(R.id.ed_username);
        passWordEd = (EditText)findViewById(R.id.ed_password);
        userNameEd.setText("13888888888");
        passWordEd.setText("123456");

        loginLayout = (LinearLayout)findViewById(R.id.linearlayout_login);
        loginLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        break;
                    case MotionEvent.ACTION_UP:

                        // 先判断是否有无网络
                        if (ValidateUtils.isNetworkAvailable(mContext)) {

                            if (userNameEd.getText().toString().equals("13888888888") && passWordEd.getText().toString().equals("123456")) {
                                ToastUtils.showToastCenter(mContext, "登录成功");
                                startActivity(new Intent(mContext, ViewPagerActivity.class));
                                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                            } else {
                                ToastUtils.showToastCenter(mContext, "用户名或密码不正确");
                            }

                        } else {
                            ToastUtils.showToastCenter(mContext, "网络连接不可用，请检查当前网络配置！");
                        }

                        break;
                }
                return true;
            }
        });

        barcodeLayout = (LinearLayout)findViewById(R.id.linearlayout_barcode);
        barcodeLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        break;
                    case MotionEvent.ACTION_UP:

                        Intent intent = new Intent();
                        intent.setClass(MainActivity.this, MipcaCaptureActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivityForResult(intent, SCANNIN_GREQUEST_CODE);

                        break;
                }
                return true;
            }
        });

        client.Init("192.168.1.1",123);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        StatusBarUtils.setWindowStatusBarTransparent(this);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        initViews();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SCANNIN_GREQUEST_CODE:
                if(resultCode == RESULT_OK){

                    Bundle bundle = data.getExtras();
                    String result = bundle.getString("result") ;

                    ToastUtils.showToastCenter(mContext,result);

                    String[] strs = result.split("#");
                    if(strs != null && strs.length == 3)  {
                        String devSerial = strs[0];
                        String devName = strs[1];
                        String devIp = strs[2];

                        DeviceInfo _info = new DeviceInfo();
                        _info.setIp(devIp);
                        _info.setName(devName);
                        _info.setSerial(devSerial);

                        Bundle _bundle = new Bundle();
                        _bundle.putSerializable("device_info", _info);
                        Intent _intent = new Intent(mContext,CategoryActivity.class);
                        _intent.putExtras(_bundle);

                        startActivity(_intent);
                    }
                }
                break;
        }
    }
}
