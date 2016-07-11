package com.incast.udi.eduphone;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.incast.udi.eduphone.utils.StatusBarUtils;
import com.incast.udi.eduphone.view.RemoteControlHardDecodeView;
import com.incast.udi.eduphone.vo.DeviceInfo;

public class RemoteControlActivity extends Activity {

    private Context mContext;
    private DeviceInfo mDeviceInfo = null;

    private Button backBtn;
    private Button commandBtn,pushfileBtn;
    private RemoteControlHardDecodeView mRemoteControlView;

    protected void ShowPortraitViews(){
        backBtn = (Button)findViewById(R.id.button_remotecontrol_backward);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        commandBtn = (Button)findViewById(R.id.opt_command_btn);
        pushfileBtn = (Button)findViewById(R.id.opt_pushfile_btn);

        commandBtn.setBackgroundColor(Color.parseColor("#7D9EC0"));
        commandBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commandBtn.setBackgroundColor(Color.parseColor("#7D9EC0"));
                pushfileBtn.setBackgroundColor(Color.parseColor("#87CEFA"));
            }
        });

        pushfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commandBtn.setBackgroundColor(Color.parseColor("#87CEFA"));
                pushfileBtn.setBackgroundColor(Color.parseColor("#7D9EC0"));
            }
        });
    }

    protected void initViews(){

        mRemoteControlView = (RemoteControlHardDecodeView)findViewById(R.id.remote_control_view);
        mRemoteControlView.setDeviceInfo(mDeviceInfo);
        mRemoteControlView.setInitDeviceCallback_H(new RemoteControlHardDecodeView.InitDeviceCallback_H() {
            @Override
            public void onInitDevice() {

            }
        });

        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // 隐藏状态栏
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            mRemoteControlView.m = 1;
        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mRemoteControlView.m = 3;
            ShowPortraitViews();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        StatusBarUtils.setWindowStatusBarTransparent(this);
        // 保持屏幕长亮
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_remote_control);

        Bundle bundle = getIntent().getExtras();
        mDeviceInfo = (DeviceInfo) bundle.getSerializable("device_info");

        if(mDeviceInfo != null) {
//            ToastUtils.showToastCenter(mContext, "设备名：" + mDeviceInfo.getName() + "\nIP：" + mDeviceInfo.getIp() + "\n序列号：" + mDeviceInfo.getSerial());
        }

        initViews();
    }
    @Override
    protected void onPause() {
        if(mRemoteControlView != null) {
            mRemoteControlView.CloseDialog();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if(mRemoteControlView != null) {
            mRemoteControlView.CloseDialog();
        }
        super.onDestroy();
    }
}
