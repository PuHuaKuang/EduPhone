package com.incast.udi.eduphone;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import com.incast.udi.eduphone.adapter.GuideFragmentAdapter;
import com.incast.udi.eduphone.utils.StatusBarUtils;

public class ViewPagerActivity extends FragmentActivity implements View.OnClickListener {

    private Context mContext;

    public static final int TAB_DEVICE = 0;
//    public static final int TAB_DISCOVER = 1;
    public static final int TAB_MYCENTER = 1;

    private ViewPager viewPager;
    private ImageView main_tab_device;
//    private ImageView main_tab_discover;
    private ImageView main_tab_mycenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);

        mContext = this;
        StatusBarUtils.setWindowStatusBarTransparent(this);
        initView();
        addListener();
    }

    private void initView() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        main_tab_device = (ImageView) findViewById(R.id.device_image_vp);
//        main_tab_discover = (ImageView) findViewById(R.id.discover_image_vp);
        main_tab_mycenter = (ImageView) findViewById(R.id.mycenter_image_vp);

        main_tab_device.setOnClickListener(this);
//        main_tab_discover.setOnClickListener(this);
        main_tab_mycenter.setOnClickListener(this);

        GuideFragmentAdapter adapter = new GuideFragmentAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(TAB_DEVICE);
    }

    private void addListener() {
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int id) {
                switch (id) {
                    case TAB_DEVICE:
                        main_tab_device.setImageResource(R.mipmap.icon_device);
//                        main_tab_discover.setImageResource(R.mipmap.icon_discover_transparent);
                        main_tab_mycenter.setImageResource(R.mipmap.icon_mycenter_transparent);
                        break;
//                    case TAB_DISCOVER:
//                        main_tab_device.setImageResource(R.mipmap.icon_device_transparent);
//                        main_tab_discover.setImageResource(R.mipmap.icon_discover);
//                        main_tab_mycenter.setImageResource(R.mipmap.icon_mycenter_transparent);
//                        break;
                    case TAB_MYCENTER:
                        main_tab_device.setImageResource(R.mipmap.icon_device_transparent);
//                        main_tab_discover.setImageResource(R.mipmap.icon_discover_transparent);
                        main_tab_mycenter.setImageResource(R.mipmap.icon_mycenter);
                        break;

                    default:
                        break;
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.device_image_vp:
                viewPager.setCurrentItem(TAB_DEVICE);
                break;
//            case R.id.discover_image_vp:
//                viewPager.setCurrentItem(TAB_DISCOVER);
//                break;
            case R.id.mycenter_image_vp:
                viewPager.setCurrentItem(TAB_MYCENTER);
                break;

            default:
                break;
        }
    }
}
