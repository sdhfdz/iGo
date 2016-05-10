package com.example.max.iGo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.max.iGo.Model.OrderMessageModel;
import com.example.max.iGo.R;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Max on 2016/4/18.
 */
public class AdpCartFragment extends BaseAdapter {

    public List<OrderMessageModel> list;
    private Context context;
    private static HashMap<Integer, Boolean> isSelected;
    int id;

    public AdpCartFragment(List<OrderMessageModel> list, Context context) {
        this.list = list;
        this.context = context;
        isSelected = new HashMap<Integer, Boolean>();
        initDate();
    }

    public HashMap<Integer,Boolean> getIsSelected() {
        return isSelected;
    }

    public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {
        AdpCartFragment.isSelected = isSelected;
    }

    private void initDate() {
        for (int i = 0; i < list.size(); i++) {
            getIsSelected().put(i, false);
        }
    }

    public int getCount() {
        return 3;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.fragment_cart_listview, null);
            //存对象到view里。
            holder = new ViewHolder();
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        init(holder, convertView, position);

        return convertView;

    }
    private void init(ViewHolder holder, View convertView, int position) {
        initView(holder,convertView,position);
        //初始化数据
        OrderMessageModel orderMessageModel =list.get(position);
        if (orderMessageModel == null)
        {
            return ;
        }
        Picasso.with(context).load(orderMessageModel.getImgUrl()).error(R.drawable.icon).into(holder.imgUrl);
        holder.goodsName.setText(orderMessageModel.getGoodsName());
        holder.price.setText(orderMessageModel.getPrice());
        holder.salesVolume.setText(orderMessageModel.getSalesVolume());
        holder.checkbox.setChecked(getIsSelected().get(position));
    }

    private void initView(ViewHolder holder, final View convertView, final int position) {
        holder.goodsName = (TextView) convertView.findViewById(R.id.textView_framgentCart_goodsname);
        holder.price = (TextView) convertView.findViewById(R.id.textView_framgentCart_price);
        holder.salesVolume = (TextView) convertView.findViewById(R.id.textView_framgentCart_salecount);
        holder.imgUrl = (ImageView) convertView.findViewById(R.id.imageView_framgentCart_lv);
        holder.checkbox = (CheckBox) convertView.findViewById(R.id.checkBox_framgentCart);

        holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    //如果选中，不做处理
                    list.get(position).setIsSure(true);
                }
                else {
                    //如果未选中，把数据移除出list
                    list.get(position).setIsSure(false);
                }
            }
        });

    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }


    public class ViewHolder{
        TextView goodsName;
        TextView price;
        TextView salesVolume;
        ImageView imgUrl;
        CheckBox checkbox;

    }

}
