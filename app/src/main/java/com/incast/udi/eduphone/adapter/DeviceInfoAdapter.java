package com.incast.udi.eduphone.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.incast.udi.eduphone.vo.DeviceInfo;
import com.incast.udi.eduphone.R;
import java.util.ArrayList;


public class DeviceInfoAdapter extends BaseAdapter {

	
	private Context context;

	private ViewHolder holder;
	private DeviceInfo info;
	private ArrayList<DeviceInfo> listInfos;
	
	public ArrayList<DeviceInfo> getListInfos() {
		return listInfos;
	}

	public void setListInfos(ArrayList<DeviceInfo> listInfos) {
		this.listInfos = listInfos;
	}

	public DeviceInfoAdapter(Context mContext, ArrayList<DeviceInfo> list) {
		this.context = mContext;
		this.listInfos = list;
	}

	@Override
	public int getCount() {
		if (listInfos != null && listInfos.size() > 0) {
			return listInfos.size();
		} else {
			return 0;
		}

	}

	@Override
	public Object getItem(int position) {
		return listInfos.get(position);

	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@SuppressWarnings("static-access")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = convertView.inflate(context, R.layout.item_deviceinfo_layout, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (listInfos != null && listInfos.size() > 0) {
			info = listInfos.get(position);
			holder.icon.setBackgroundResource(info.getIcon());
			holder.name.setText(info.getName());
		}
		
		return convertView;
	}

	private static class ViewHolder {
		
		private ImageView icon;
		private TextView name;

		public ViewHolder(View view) {
			icon = (ImageView) view.findViewById(R.id.device_icon_id);
			name = (TextView) view.findViewById(R.id.device_name_id);
		}
	}
}
