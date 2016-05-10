package com.example.max.iGo.Activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.max.iGo.Base.BaseActivity;
import com.example.max.iGo.R;
import com.example.max.iGo.Utils.CircleImageView;
import com.example.max.iGo.Utils.GlobalCheckGet;
import com.example.max.iGo.Utils.GlobalCheckPost;
import com.example.max.iGo.Utils.PrefUtils;
import com.example.max.iGo.domain.Commet_article;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class GoodsDetailsActivity extends BaseActivity  {
    private TextView titleBarText;
    private ImageView image_back;
    private Commet_article data;
    private LinearLayout ll_description_img;
    private LinearLayout ll_description_text;
    private ListView comment_lv;
    private View view_above;
    private View view_item_text;
    private LinearLayout.LayoutParams params;
    private TextView article_price;
    private TextView sales_count;
    private ImageView description_pic_in_above;
    private BitmapUtils bitmapUtils;
    private TextView product_name;
    private ImageView iv_display;
    private TextView description_title;
    private TextView description_cotent;
    private TextView comment_total_count;
    private EditText input_comment;
    public ArrayList<Commet_article.comment_cls> comment_for_listView;
    private String spid;
    private ProgressBar article_pb_progress;
    private LinearLayout ll_click_to_commodityDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_goodsdetails);
        init();
        article_pb_progress.setVisibility(View.VISIBLE);

        Intent intent=getIntent();
        spid = intent.getStringExtra("spid");
        if (savedInstanceState != null && savedInstanceState.getBoolean("toBottom")== true)
        getDataFromServer(true);
        else
            getDataFromServer(false);


    }

    public void init() {
        comment_for_listView = new ArrayList<Commet_article.comment_cls>();
        titleBarText = (TextView) findViewById(R.id.titlebar_text);
        image_back = (ImageView) findViewById(R.id.titlebar_back);
        titleBarText.setText("文章");
        comment_lv = (ListView) findViewById(R.id.comment_lv);
        input_comment = (EditText) findViewById(R.id.input_comment);
        bitmapUtils = new BitmapUtils(GoodsDetailsActivity.this);
        view_above = View.inflate(GoodsDetailsActivity.this, R.layout.chat_above_of_comment, null);
        description_pic_in_above = (ImageView) view_above.findViewById(R.id.description_pic_in_above);
        product_name = (TextView) view_above.findViewById(R.id.product_name);
        article_price = (TextView) view_above.findViewById(R.id.article_price);
        sales_count = (TextView) view_above.findViewById(R.id.sales_count);
        article_pb_progress = (ProgressBar) findViewById(R.id.article_pb_progress);

        ll_description_img = (LinearLayout) view_above.findViewById(R.id.ll_description_img);
        comment_total_count = (TextView) view_above.findViewById(R.id.comment_total_count);
        ll_description_text = (LinearLayout) view_above.findViewById(R.id.ll_description_text);
        ll_click_to_commodityDetail = (LinearLayout) view_above.findViewById(R.id.ll_click_to_commodityDetail);

        params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        params.setMargins(20, 10, 20, 10);
        image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        });

    }


    class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return data.comment.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = null;
            comment_listView mHolder = null;
            if (convertView == null) {
                convertView = View.inflate(GoodsDetailsActivity.this, R.layout.chat_item_comment, null);
                mHolder = new comment_listView();
                mHolder.user_comment_img = (CircleImageView) convertView.findViewById(R.id.comment_img_user);
                mHolder.time = (TextView) convertView.findViewById(R.id.comment_time);
                mHolder.username = (TextView) convertView.findViewById(R.id.user_name);
                mHolder.floor_num = (TextView) convertView.findViewById(R.id.floor_comment);
                mHolder.comment_content = (TextView) convertView.findViewById(R.id.comment_content);
                convertView.setTag(mHolder);

            } else {
                mHolder = (comment_listView) convertView.getTag();
            }
            Commet_article.comment_cls comls = comment_for_listView.get(position);
            mHolder.username.setText(comls.user.name);
            mHolder.comment_content.setText(comls.comment);
            bitmapUtils.display(mHolder.user_comment_img, GlobalCheckGet.SERVER_URL + comls.user.icon);
            if (!TextUtils.isEmpty(comls.time) && !comls.time.equals("null"))
                mHolder.time.setText(ParseTime(Long.parseLong(comls.time)));
            else
                mHolder.time.setText("");
            mHolder.floor_num.setText((position + 1) + "楼");


            return convertView;
        }

        class comment_listView {
            CircleImageView user_comment_img;
            TextView time;
            TextView username;
            TextView floor_num;
            TextView comment_content;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }


    }

    public void goods_detail(View v) {
        System.out.println("Hello Wrold");
    }

    public void getDataFromServer(final Boolean toBottom) {
        HttpUtils http = new HttpUtils();
        http.configCurrentHttpCacheExpiry(1);

        http.send(HttpRequest.HttpMethod.GET, GlobalCheckGet.SERVER_URL + "/mobile_status/detail?id=" + spid, new RequestCallBack<String>() {
            @Override
            public Object getUserTag() {
                return super.getUserTag();
            }

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                article_pb_progress.setVisibility(View.GONE);
                String result = (String) responseInfo.result;
                //  System.out.println("MMMMMMMMMMMMMMM");
                System.out.println("result++++" + result);
                ParseData(result);
                ll_click_to_commodityDetail.setOnClickListener(new MyOnClickListener());
                comment_for_listView = data.comment;
                comment_total_count.setText("全部评论   (" + data.comment.size() + ")");
                // System.out.println(data.description[1] + "LLLLLLLLLLLLLLLLLLLLLLLLLL");

                fillBasedInfo();
                picDisplay();
                descriptionDisplay();
                comment_lv.addHeaderView(view_above);
                comment_lv.setAdapter(new MyAdapter());
                if (toBottom)
                    comment_lv.setSelection(comment_lv.getCount() - 1);

            }

            @Override
            public void onFailure(HttpException e, String s) {


            }
        });
    }

    public void ParseData(String result) {

        Gson gson = new Gson();
        data = gson.fromJson(result, Commet_article.class);
        System.out.println("result++++??" + data);
    }

    public void fillBasedInfo() {
        article_price.setText("￥" + data.product.price);
        sales_count.setText("销量" + data.product.sale);
        product_name.setText(data.product.name);
        bitmapUtils.display(description_pic_in_above, GlobalCheckGet.SERVER_URL + data.product.pic.split("\\|")[0]);
    }

    public void picDisplay() {
        for (int i = 0; i < data.pic.length; i++) {
            iv_display = new ImageView(this);
            bitmapUtils.display(iv_display, GlobalCheckGet.SERVER_URL + "/" + data.pic[i]);
            ll_description_img.addView(iv_display, params);

        }
    }

    public void descriptionDisplay() {
        for (int i = 0; i < data.description_title.length; i++) {
            view_item_text = View.inflate(GoodsDetailsActivity.this, R.layout.description_article_item, null);
            description_title = (TextView) view_item_text.findViewById(R.id.description_title);
            description_cotent = (TextView) view_item_text.findViewById(R.id.description_cotent);
            description_title.setText("[" + data.description_title[i] + "]");
            description_cotent.setText("\u3000\u3000" + data.description[i]);
            //    ((LinearLayout) view_item_text.getParent()).removeAllViews();
            ll_description_text.addView(view_item_text);

        }


    }

    public void click_to_comment(View view) {
        final View buttonview = view;
        final String input_content = input_comment.getText().toString();

        if (TextUtils.isEmpty(input_content)) {
            Toast.makeText(GoodsDetailsActivity.this, "输入内容不能为空", Toast.LENGTH_SHORT).show();
        } else {
            HttpUtils http = new HttpUtils();
            http.configCurrentHttpCacheExpiry(1);
            RequestParams params = new RequestParams();
            params.addBodyParameter("uid", PrefUtils.getId(GoodsDetailsActivity.this) + "");
            params.addBodyParameter("cid", data.id);
            params.addBodyParameter("comment", input_content);
            http.send(HttpRequest.HttpMethod.POST, GlobalCheckPost.SERVER_URL + "/mobile_status/comment", params, new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> responseInfo) {
                    System.out.println(responseInfo.result.toString() + "MMMMMMMMMMMMMMMMMMMLLLLLLLLLLLLLLLLLLLLLLLLLLLLL");
                    if (responseInfo.result.toString().equals("success")) {
                        Bundle mbundle=new Bundle();
                        mbundle.putBoolean("toBottom",true);
                        onCreate(mbundle);
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(buttonview.getWindowToken(), 0);


                    }else{
                        Toast.makeText(GoodsDetailsActivity.this,"请您先登录再评论",Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(HttpException e, String s) {


                }
            });

        }

    }

    public String ParseTime(Long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String time_str = format.format(time);
        return time_str;

    }
    class MyOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Intent intent=new Intent(GoodsDetailsActivity.this,CommodityDetailActivity.class);
            intent.putExtra("id",data.product.id);
            startActivity(intent);

        }
    }

}

