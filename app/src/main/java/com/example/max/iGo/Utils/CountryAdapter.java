package com.example.max.iGo.Utils;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.max.iGo.R;
import com.example.max.iGo.domain.Countrys;

import java.util.ArrayList;

public class CountryAdapter extends BaseAdapter {

	private Context mContext;
	public ArrayList<Countrys> countrys;

	public CountryAdapter(Context mContext, ArrayList<Countrys> countrys) {
		this.mContext = mContext;
		this.countrys = countrys;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return countrys.size();
	}

	@Override
	public Object getItem(int position) {
		return countrys.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View view = convertView;
		if(convertView == null){
			view = view.inflate(mContext, R.layout.item_list_country, null);
		}
		ViewHolder mViewHolder = ViewHolder.getHolder(view);
		
		Countrys p = countrys.get(position);
		
		String str = null;
		String currentLetter = p.getName_pinyin().charAt(0) + "";
		// 根据上一个首字母,决定当前是否显示字母
		if(position == 0){
			str = currentLetter;
		}else {
			// 上一个人的拼音的首字母
			String preLetter = countrys.get(position - 1).getName_pinyin().charAt(0) + "";
			if(!TextUtils.equals(preLetter, currentLetter)){
				str = currentLetter;
			}
		}
		
		// 根据str是否为空,决定是否显示索引栏
		mViewHolder.mIndex.setVisibility(str == null ? View.GONE : View.VISIBLE);
		mViewHolder.mIndex.setText(currentLetter);
		mViewHolder.mName.setText(p.getCountry_name());
		
		return view;
	}
	
	static class ViewHolder {
		TextView mIndex;
		TextView mName;

		public static ViewHolder getHolder(View view) {
			Object tag = view.getTag();
			if(tag != null){
				return (ViewHolder)tag;
			}else {
				ViewHolder viewHolder = new ViewHolder();
				viewHolder.mIndex = (TextView) view.findViewById(R.id.tv_index);
				viewHolder.mName = (TextView) view.findViewById(R.id.tv_name);
				view.setTag(viewHolder);
				return viewHolder;
			}
		}
		
	}

}
