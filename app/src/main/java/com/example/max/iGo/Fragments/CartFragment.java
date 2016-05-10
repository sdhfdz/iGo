package com.example.max.iGo.Fragments;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.max.iGo.Activity.ConfirmedOrderActivity;
import com.example.max.iGo.Adapter.AdpCartFragment;
import com.example.max.iGo.Model.OrderMessageModel;
import com.example.max.iGo.R;

import java.io.Serializable;
import java.util.ArrayList;

public class CartFragment extends Fragment {

    private Button pay;
    private CheckBox allCheck;
    private TextView titleBarText;
    private ImageView image_back;
    private ListView cartListView;
    AdpCartFragment adpCartFragment;
    public ArrayList<OrderMessageModel> list;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, null);


        //初始化标题栏

        initView(view);
        initDate(view);
        initListener();
        return view;
    }

    private void initListener() {
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), ConfirmedOrderActivity.class);
                intent.putExtra("OrderMessage", (Serializable) list);

                startActivityForResult(intent, 0);

            }
        });
        cartListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TextView itemTv = (TextView) view.findViewById(R.id.tv);
                // String text = itemTv.getText().toString();
            }
        });

        //是否全选的监听
        allCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    // 遍历list的长度，将MyAdapter中的map值全部设为true
                    for (int i = 0; i < list.size(); i++) {
                        adpCartFragment.getIsSelected().put(i, true);
                    }

                    // 刷新listview和TextView的显示
                    adpCartFragment.notifyDataSetChanged();
                } else {
                    for (int i = 0; i < list.size(); i++) {
                        adpCartFragment.getIsSelected().put(i, false);
                    }
                    // 刷新listview和TextView的显示
                    adpCartFragment.notifyDataSetChanged();
                }
            }
        });
    }

    /**
     * 初始化数据
     * @param view
     */
    private void initDate(View view) {

        list = new ArrayList<OrderMessageModel>();

        for(int i=0;i<10;i++){
            OrderMessageModel orderMessagerModer = new OrderMessageModel("商品"+i,"155","302销量","http://pic18.nipic.com/20120108/6608733_091741091355_2.jpg");
            list.add(orderMessagerModer);
        }
        adpCartFragment = new AdpCartFragment(list,view.getContext());
        cartListView.setAdapter(adpCartFragment);


    }

    /**
     * 初始化view
     * @param view
     */

    public void initView(View view) {
        titleBarText = (TextView)view.findViewById(R.id.titlebar_text);
        image_back = (ImageView)view.findViewById(R.id.titlebar_back);
        pay = (Button)view.findViewById(R.id.pay);
        cartListView = (ListView) view.findViewById(R.id.listView_cart);
        allCheck = (CheckBox) view.findViewById(R.id.checkBox_framgentCart_selectAll);
        titleBarText.setText("购物车");
        image_back.setVisibility(View.INVISIBLE);

    }



}