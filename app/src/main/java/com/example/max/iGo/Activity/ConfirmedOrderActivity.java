package com.example.max.iGo.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.max.iGo.Adapter.*;
import com.example.max.iGo.Base.BaseActivity;
import com.example.max.iGo.Fragments.CartFragment;
import com.example.max.iGo.Model.AddressModel;
import com.example.max.iGo.Model.OrderMessageModel;
import com.example.max.iGo.R;

import java.util.List;

/**
 * 确认订单页面.
 *
 */
public class ConfirmedOrderActivity extends BaseActivity {
    private AdpConfirmedOrder adpConfirmedOrder;
    private TextView titleBarText;
    private ImageView image_back;
    private ListView confirmListView;
    private TextView addrmName;
    private TextView addriphone;
    private TextView address,count1;
    private RelativeLayout addrLayout;
    private AddressModel addressModel;
    private TextView countMoney;

    public List<OrderMessageModel> list;

    private static final String TAG = "ConfirmedOrderActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmed_order);
        init();

    }

    public void init() {
        initView();
        initData();
        initListener();
    }

    /**
     * 初始化数据
     */
    private void initData() {



        //设置lv里面的数据
        list=(List<OrderMessageModel>) getIntent().getSerializableExtra("OrderMessage");

        for(int i = list.size()-1; i >=0 ;i--){
            if (!list.get(i).isSure()) {
                list.remove(i);
            }
        }


        adpConfirmedOrder = new AdpConfirmedOrder(list,this);
        confirmListView.setAdapter(adpConfirmedOrder);

        //初始化数量
        System.out.println(list.size());
        count1.setText(list.size() + "");

        //初始化总价
        Double sum = 0.0;
        for(int i = 0; i<list.size(); i++){

            sum = sum +  Double.valueOf(list.get(i).getPrice()).doubleValue()* Integer.valueOf(list.get(i).getOrderVolum()).intValue();
        }
        Log.i(TAG, "sum = " + sum);
        countMoney.setText(sum + "");



    }

    /**
     * 初始化View
     */
    private void initView() {
        titleBarText = (TextView) findViewById(R.id.titlebar_text);
        image_back = (ImageView) findViewById(R.id.titlebar_back);
        count1 = (TextView) findViewById(R.id.confirm_count_1);
        addrmName = (TextView) findViewById(R.id.confirm_name);
        addriphone = (TextView) findViewById(R.id.confirm_phone);
        address = (TextView) findViewById(R.id.confirm_addr);
        addrLayout = (RelativeLayout) findViewById(R.id.confirm_addr_layout);
        countMoney = (TextView) findViewById(R.id.confirm_money);
        titleBarText.setText("确认订单");


        //Listview的设置

        confirmListView = (ListView) findViewById(R.id.listView_confirm);



    }

    private void initListener() {

        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });
        addrLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(ConfirmedOrderActivity.this, MagAddressActivity.class);
                startActivityForResult(intent, 1);
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
            }
        });
        confirmListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.getParent().requestDisallowInterceptTouchEvent(true);
                // TextView itemTv = (TextView) view.findViewById(R.id.tv);
                // String text = itemTv.getText().toString();

            }
        });
        //点击item内按钮，更新总价。实现回调函数。
        adpConfirmedOrder.setUpdateListener(new AdpConfirmedOrder.DataListener() {
            @Override
            public void dataUpdate(List<OrderMessageModel> list) {
                Double sum = 0.0;
                for (int i = 0; i < list.size(); i++) {

                    sum = sum + Double.valueOf(list.get(i).getPrice()).doubleValue() * Integer.valueOf(list.get(i).getOrderVolum()).intValue();
                }
                Log.i(TAG, "sum = " + sum);
                countMoney.setText(sum + "");
            }
        });
    }

    /**
     * 接受从MagAddressActivity页面传回来的数据。
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case RESULT_OK:
                //接受传递过来的数据
                Bundle bundle = data.getExtras();
                addressModel = (AddressModel) bundle.getSerializable("addr");
                if (addressModel == null){
                   return;
                }
                System.out.println(addressModel.getAddress());
                System.out.println(addressModel.getName());
                System.out.println(addressModel.getIphone());


                //设置接受到的数据
                addrmName.setText(addressModel.getName());
                addriphone.setText(addressModel.getIphone());
                if (addressModel.getAddress().length()<15) {
                    address.setText(addressModel.getAddress());
                }
                else{
                    address.setText(addressModel.getAddress().substring(0,15)+"...");
                }
                break;


        }

    }
}