package com.incast.udi.eduphone.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.incast.udi.eduphone.fragment.DeviceFragment;
import com.incast.udi.eduphone.fragment.DiscoverFragment;
import com.incast.udi.eduphone.fragment.MyCenterFragment;

public class GuideFragmentAdapter extends FragmentPagerAdapter {
	private Fragment[] fragments = new Fragment[] { new DeviceFragment(), new MyCenterFragment() };

	public GuideFragmentAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		return fragments[position];
	}

	@Override
	public int getCount() {
		return fragments.length;
	}

}