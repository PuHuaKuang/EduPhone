package com.incast.udi.eduphone.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.cmm.incast.client;
import com.incast.udi.eduphone.CategoryActivity;
import com.incast.udi.eduphone.R;
import com.incast.udi.eduphone.adapter.DeviceInfoAdapter;
import com.incast.udi.eduphone.utils.ToastUtils;
import com.incast.udi.eduphone.utils.ValidateUtils;
import com.incast.udi.eduphone.vo.DeviceInfo;

import java.util.ArrayList;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

public class DeviceFragment extends Fragment {

	private static final int LIST_HAS_NO_ITEM = 0x01;
	private static final int LIST_HAS_ITEM = 0x02;

	private ArrayList<DeviceInfo> mListItems = new ArrayList<DeviceInfo>();
	private DeviceInfoAdapter mAdapter;
	private PtrClassicFrameLayout mPtrFrame;
	private TextView mTextView;
	private ListView mListView;

	private ArrayList<String> gradeList = null ;
	private Spinner sp ;

	private Handler _handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
				case LIST_HAS_NO_ITEM:

					displayNoData();
					break;
				case LIST_HAS_ITEM:

					displayData();
					break;
				default:
					break;
			}
			super.handleMessage(msg);
		}
	};

	private void getDeviceInfos() {

		int length = 1024;
		int nTimeOut = 2;
		mListItems.clear();
		int deviceCount = client.GetDevicesCount(nTimeOut);
		if(deviceCount > 0) {
			for(int i = 0 ; i < deviceCount ; i++) {
				byte[] deviceName = new byte[length];
				byte[] deviceIp = new byte[length];
				byte[] deviceSerial = new byte[length];

				int ret = client.GetDevicesInfo(i, deviceName, length, deviceIp, length, deviceSerial, length);
				if(ret!=(-1)){
					String deviceNameStr = new String(deviceName).trim();
					String deviceIpStr = new String(deviceIp).trim();
					String deviceSerialStr = new String(deviceSerial).trim();

					DeviceInfo info = new DeviceInfo();
					info.setName(deviceNameStr);
					info.setSerial(deviceSerialStr);
					info.setIp(deviceIpStr);
					info.setIcon(R.mipmap.device_item_logo);
					mListItems.add(info);
				}
			}
		}
	}

	private void initViews(View view){

		gradeList = new ArrayList<String>();
		gradeList.add("全部");
		gradeList.add("英佳云终端一年级");
		gradeList.add("英佳云终端二年级");
		gradeList.add("英佳云终端三年级");
		gradeList.add("请选择");

		sp = (Spinner) view.findViewById(R.id.sp_select_device);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_showed_item,gradeList) {
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {

				View v = super.getView(position, convertView, parent);
				return v;
			}

			@Override
			public int getCount() {
				return super.getCount()-1; // you don't display last item. It is used as hint.
			}

			@Override
			public View getDropDownView(int position, View convertView, ViewGroup parent) {

				View view = convertView.inflate(getContext(), R.layout.spinner_item_layout, null);
				TextView label = (TextView) view .findViewById(R.id.spinner_item_label);
				ImageView check = (ImageView) view .findViewById(R.id.spinner_item_checked_image);
				label.setText(gradeList.get(position));
				if (sp.getSelectedItemPosition() == position) {
					view.setVisibility(View.VISIBLE);
					label.setTextColor(getResources().getColor(android.R.color.holo_red_light));
					check.setImageResource(R.mipmap.check_selected);
				} else {
					label.setTextColor(getResources().getColor( android.R.color.darker_gray));
					check.setVisibility(View.GONE);
				}

				return view;
			}
		};

		adapter.setDropDownViewResource(R.layout.spinner_option_items);

		sp.setAdapter(adapter);
		sp.setSelection(adapter.getCount()); //display hint
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.fg_device, container,false);

		initViews(contentView);

		mTextView = (TextView) contentView.findViewById(R.id.list_view_with_empty_view_fragment_empty_view);
		mPtrFrame = (PtrClassicFrameLayout) contentView.findViewById(R.id.list_view_with_empty_view_fragment_ptr_frame);

		mTextView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mPtrFrame.setVisibility(View.VISIBLE);
				mTextView.setVisibility(View.INVISIBLE);
				mPtrFrame.autoRefresh();
				if(mListItems != null) {
					mListItems.clear();
					mAdapter.setListInfos(mListItems);
					mAdapter.notifyDataSetChanged();
					mListView.invalidate();
				}
			}
		});

		mListView = (ListView) contentView.findViewById(R.id.list_view_with_empty_view_fragment_list_view);
		mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (position >= 0) {
					// show load data
					// 进到相应的远程控制页面
					if (mListItems != null) {
						DeviceInfo _info = mListItems.get(position);
						Bundle _bundle = new Bundle();
						_bundle.putSerializable("device_info", _info);
						Intent _intent = new Intent(getActivity(), CategoryActivity.class);
						_intent.putExtras(_bundle);
						startActivity(_intent);
					}
				}
			}
		});
		mPtrFrame.setVisibility(View.INVISIBLE);
		mTextView.setVisibility(View.VISIBLE);

		mAdapter = new DeviceInfoAdapter(getActivity(),mListItems);
		mListView.setAdapter(mAdapter);

		mPtrFrame.setLastUpdateTimeRelateObject(this);
		mPtrFrame.setPtrHandler(new PtrHandler() {
			@Override
			public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {

				// here check $mListView instead of $content
				return PtrDefaultHandler.checkContentCanBePulledDown(frame, mListView, header);
			}

			@Override
			public void onRefreshBegin(PtrFrameLayout frame) {
				updateData();
			}
		});
		// the following are default settings
		mPtrFrame.setResistance(1.7f);
		mPtrFrame.setRatioOfHeaderHeightToRefresh(1.2f);
		mPtrFrame.setDurationToClose(200);
		mPtrFrame.setDurationToCloseHeader(1000);
		// default is false
		mPtrFrame.setPullToRefresh(false);
		// default is true
		mPtrFrame.setKeepHeaderWhenRefresh(true);
/*		mPtrFrame.postDelayed(new Runnable() {
			@Override
			public void run() {
				mPtrFrame.setVisibility(View.VISIBLE);
				mTextView.setVisibility(View.INVISIBLE);
				mPtrFrame.autoRefresh();
				if(mListItems != null) {
					mListItems.clear();
					mAdapter.setListInfos(mListItems);
					mAdapter.notifyDataSetChanged();
					mListView.invalidate();
				}
			}
		}, 100);*/
		return contentView;
	}

	protected void updateData() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				if(ValidateUtils.isNetworkAvailable(getActivity())) {
					getDeviceInfos();
					if(mListItems != null && mListItems.size() > 0) {
						_handler.sendEmptyMessage(LIST_HAS_ITEM);
					} else {
						_handler.sendEmptyMessage(LIST_HAS_NO_ITEM);
					}
				} else {
					_handler.sendEmptyMessage(LIST_HAS_NO_ITEM);
				}
			}
		}).start();
	}

	private void displayData() {

		mTextView.setVisibility(View.GONE);

		mAdapter.setListInfos(mListItems);
		mAdapter.notifyDataSetChanged();
		mListView.invalidate();

		mPtrFrame.refreshComplete();
	}

	private void displayNoData(){
		// show empty view
		mPtrFrame.setVisibility(View.INVISIBLE);
		mTextView.setVisibility(View.VISIBLE);

		mListItems.clear();
		mAdapter.setListInfos(mListItems);
		mAdapter.notifyDataSetChanged();
		mListView.invalidate();

		mPtrFrame.refreshComplete();
	}

}
