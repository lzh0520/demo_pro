package audio.test.com.audiodemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import audio.test.com.audiodemo.R;
import audio.test.com.audiodemo.bean.ItemBean;

public class RightMenuAdapter extends BaseAdapter {
	public List<ItemBean> mList = new ArrayList<ItemBean>();
	private LayoutInflater mInflater;


	public RightMenuAdapter(Context mContext, List<ItemBean> mList) {
		this.mList = mList;
		this.mInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {

		return mList.size();
	}

	@Override
	public ItemBean getItem(int position) {

		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;
		if (null == convertView) {
			convertView = mInflater.inflate(R.layout.ehuilib_right_menu_item, null);
			holder = new ViewHolder();
			holder.iconImageView = (ImageView) convertView
					.findViewById(R.id.pop_icon_ImageView);
			holder.nameTextView = (TextView) convertView
					.findViewById(R.id.pop_name_TextView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		ItemBean tempItemClass = mList.get(position);
		holder.iconImageView.setImageResource(tempItemClass.icon);
	
		holder.nameTextView.setText(tempItemClass.title);

		return convertView;
	}

	private class ViewHolder {
		ImageView iconImageView;
		TextView nameTextView;
	}

}
