package com.example.max.iGo.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.max.iGo.R;
import com.example.max.iGo.Utils.GlobalCheckGet;
import com.example.max.iGo.domain.Notification_bean;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.text.SimpleDateFormat;

public class HomeMessageActivity extends AppCompatActivity {
    TextView titleBarText;
    ImageView titleBack;
    private ListView lv_message;
    private RelativeLayout home_message_back;
    private TextView home_message_back_tvmiss;
    private Notification_bean data;
    private BitmapUtils bitmaputils;
    private ImageView home_message_musicpic;
    private TextView home_message_back_tv1;
    private TextView home_message_back_tv2;
    private String id_for_intent="";
    private TextView go_to_buy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_message);
        initData();
        GetDataFromServer();


    }

    private void initData() {
        lv_message = (ListView) findViewById(R.id.lv_message);
        lv_message = (ListView) findViewById(R.id.lv_message);
        titleBarText = (TextView) findViewById(R.id.titlebar_text);
        titleBarText.setText("消息");
        bitmaputils = new BitmapUtils(HomeMessageActivity.this);
        home_message_musicpic = (ImageView) findViewById(R.id.home_message_musicpic);
        home_message_back_tv1 = (TextView) findViewById(R.id.home_message_back_tv1);
        home_message_back_tv2 = (TextView) findViewById(R.id.home_message_back_tv2);
        titleBack = (ImageView) findViewById(R.id.titlebar_back);
        go_to_buy = (TextView)findViewById(R.id.go_to_buy);
        home_message_back = (RelativeLayout) findViewById(R.id.home_message_rela);
        home_message_back_tvmiss = (TextView) findViewById(R.id.home_message_back_tvmiss);
        home_message_back_tvmiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                home_message_back.setVisibility(View.INVISIBLE);
            }
        });
        titleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                //跳转动画
                overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

            }
        });
        go_to_buy.setOnClickListener(new MyOnClickListener());
        home_message_back.setVisibility(View.INVISIBLE);

    }


    class MyOnClickListener implements View.OnClickListener{
    @Override
    public void onClick(View v) {
        System.out.println("点击事件???????????????");
        Intent intent = new Intent(HomeMessageActivity.this, CommodityDetailActivity.class);
        intent.putExtra("id", id_for_intent);
        startActivity(intent);

    }
}


    class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return data.value.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        class Holder {
            ImageView home_message_img;
            TextView home_message_title;
            TextView home_message_date;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            Holder mHolder = null;
            if (convertView == null) {
                convertView = View.inflate(HomeMessageActivity.this, R.layout.home_message_listview_list, null);
                mHolder = new Holder();
                mHolder.home_message_date = (TextView) convertView.findViewById(R.id.home_message_date);
                mHolder.home_message_img = (ImageView) convertView.findViewById(R.id.home_message_img);
                mHolder.home_message_title = (TextView) convertView.findViewById(R.id.home_message_name);
                convertView.setTag(mHolder);
            } else {
                mHolder = (Holder) convertView.getTag();
            }
            Notification_bean.notification_content tmp_value = data.value.get(position);
            mHolder.home_message_title.setText(tmp_value.title);
            bitmaputils.display(mHolder.home_message_img, GlobalCheckGet.SERVER_URL + "/" + tmp_value.pic);
            mHolder.home_message_date.setText(ParseTime(Long.parseLong(tmp_value.createTime)));


            return convertView;
        }

    }

    class ItemListen implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Notification_bean.notification_content tmp_value = data.value.get(position);
            home_message_back_tv1.setText(tmp_value.title);
            home_message_back_tv2.setText(tmp_value.description);
            bitmaputils.display(home_message_musicpic, GlobalCheckGet.SERVER_URL + "/" + tmp_value.pic);
            id_for_intent=tmp_value.pid;
            home_message_back.setVisibility(View.VISIBLE);
        }
    }

    private void GetDataFromServer() {
        HttpUtils http = new HttpUtils();
        http.configCurrentHttpCacheExpiry(1);
        http.send(HttpRequest.HttpMethod.GET, GlobalCheckGet.SERVER_URL + "/mobile_notification", new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                System.out.println(responseInfo.result.toString() + "/notificationPPPPPPPPPPPPPPP");
                ParseData(responseInfo.result.toString());
                lv_message.setAdapter(new MyAdapter());
                lv_message.setOnItemClickListener(new ItemListen());


            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        });

    }

    public void ParseData(String result) {

        Gson gson = new Gson();
        data = gson.fromJson(result, Notification_bean.class);
        System.out.println("result++++??" + data);
    }

    public String ParseTime(Long time) {
        SimpleDateFormat format = new SimpleDateFormat("MM-dd");
        String time_str = format.format(time);
        return time_str;

    }
}
