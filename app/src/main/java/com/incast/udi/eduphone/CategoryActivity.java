package com.incast.udi.eduphone;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

import com.incast.udi.eduphone.utils.StatusBarUtils;
import com.incast.udi.eduphone.utils.ToastUtils;
import com.incast.udi.eduphone.vo.DeviceInfo;

public class CategoryActivity extends Activity implements View.OnTouchListener{

    private Context mContext;
    private DeviceInfo mDeviceInfo = null;

    private Button backBtn;
    private LinearLayout remotecontrolLayout,pushvideoLayout;

    protected void initViews(){
        backBtn = (Button)findViewById(R.id.button_category_backward);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        remotecontrolLayout = (LinearLayout)findViewById(R.id.linearlayout_remotecontrol);
        pushvideoLayout = (LinearLayout)findViewById(R.id.linearlayout_pushvideo);
        remotecontrolLayout.setOnTouchListener(this);
        pushvideoLayout.setOnTouchListener(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        StatusBarUtils.setWindowStatusBarTransparent(this);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_category);

        Bundle bundle = getIntent().getExtras();
        mDeviceInfo = (DeviceInfo) bundle.getSerializable("device_info");

        if(mDeviceInfo != null) {
            ToastUtils.showToastCenter(mContext,"设备名：" + mDeviceInfo.getName() + "\nIP：" + mDeviceInfo.getIp() + "\n序列号：" + mDeviceInfo.getSerial());
        }

        initViews();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        switch (v.getId()){
            case R.id.linearlayout_pushvideo:

                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:

                        break;
                    case MotionEvent.ACTION_UP:

                        //跳转到推送音视频界面
                        startActivity(new Intent(mContext,CameraActivity.class));
                        break;
                }

                break;
            case R.id.linearlayout_remotecontrol:

                 switch (event.getAction()){
                     case MotionEvent.ACTION_DOWN:
                         break;
                     case MotionEvent.ACTION_UP:

                        //跳转到远程控制界面
                         Bundle _bundle = new Bundle();
                         _bundle.putSerializable("device_info", mDeviceInfo);
                         Intent _intent = new Intent(mContext,RemoteControlActivity.class);
                         _intent.putExtras(_bundle);
                         startActivity(_intent);

                         break;
                 }
                break;
            default:
                break;
        }

        return true;
    }
}
