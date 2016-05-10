package com.example.max.iGo.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.max.iGo.R;
import com.example.max.iGo.Utils.CountryAdapter;
import com.example.max.iGo.Utils.PinyinUtils;
import com.example.max.iGo.Utils.QuickIndexBar;
import com.example.max.iGo.Utils.ReadDbCountry_Phone;
import com.example.max.iGo.domain.Countrys;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;



public class CountryChooseActivity extends AppCompatActivity {
    private ListView mMainList;
    private ArrayList<Countrys> countrys;
    private ArrayList<Countrys> backcountrys;

    private TextView tv_center;
    private static HashMap<String, String> hashMap;
    private CountryAdapter mCountryAdapter;
    private SearchView searchView;
    private InputMethodManager inputMethodManager;
    private TextView tv_index;
    private TextView tv_name;
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choosecountry);

        inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

        hashMap = new HashMap<>();
        countrys = new ArrayList<Countrys>();
        backcountrys = new ArrayList<Countrys>();
       // dbCopy("country_phone.db");
        hashMap = ReadDbCountry_Phone.GetCountryName_NUmber();
        fillAndSortData();
        mCountryAdapter = new CountryAdapter(CountryChooseActivity.this,countrys);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        QuickIndexBar bar = (QuickIndexBar) findViewById(R.id.bar);
        // 设置监听
        bar.setListener(new QuickIndexBar.OnLetterUpdateListener() {
            @Override
            public void onLetterUpdate(String letter) {
//				Utils.showToast(getApplicationContext(), letter);

                showLetter(letter);
                // 根据字母定位ListView, 找到集合中第一个以letter为拼音首字母的对象,得到索引
                for (int i = 0; i < countrys.size(); i++) {
                    Countrys country = countrys.get(i);
                    String l = country.getName_pinyin().charAt(0) + "";
                    if (TextUtils.equals(letter, l)) {
                        // 匹配成功
                        mMainList.setSelection(i);
                        break;
                    }
                }
            }
        });

        mMainList = (ListView) findViewById(R.id.lv_main);
        mMainList.setAdapter(mCountryAdapter);
        mMainList.setOnItemClickListener(new MyCountryItemClickListener());
        tv_center = (TextView) findViewById(R.id.tv_center);
    }

    private Handler mHandler = new Handler();

    /**
     * 显示字母
     *
     * @param letter
     */
    protected void showLetter(String letter) {
        tv_center.setVisibility(View.VISIBLE);
        tv_center.setText(letter);

        mHandler.removeCallbacksAndMessages(null);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tv_center.setVisibility(View.GONE);
            }
        }, 2000);

    }

    private void fillAndSortData() {
        // 填充数据
        for (Map.Entry<String, String> entry : hashMap.entrySet()) {
            Countrys country = new Countrys();
            country.setCountry_code(entry.getKey());
            country.setCountry_name(entry.getValue());
            country.setName_pinyin(PinyinUtils.getPinyin(entry.getValue()));
            countrys.add(country);
        }

        Collections.sort(countrys);
    }

    public void dbCopy(String dbName) {
        FileOutputStream out = null;
        InputStream in = null;
        File dest = new File(getFilesDir(), dbName);
        if (dest.exists()) {
            System.out.println("已经存在！！！！");
            return;
        }
        try {
            in = getAssets().open(dbName);
            out = new FileOutputStream(dest);
            int len = 0;
            byte[] buffer = new byte[1024];
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        searchView = (SearchView) menu.findItem(
                R.id.action_search).getActionView();
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(new MyQueryTextListener());// 搜索的监听
        searchView.setOnCloseListener(new MyOnCloseListenter());
        return true;
    }

    class MyOnCloseListenter implements SearchView.OnCloseListener {
        @Override
        public boolean onClose() {
            mCountryAdapter.countrys = countrys;
            mCountryAdapter.notifyDataSetChanged();
            return true;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search) {

        } else if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    class MyQueryTextListener implements SearchView.OnQueryTextListener {
        @Override
        public boolean onQueryTextSubmit(String query) {
            //  Toast.makeText(MainActivity.this, query, Toast.LENGTH_SHORT).show();
            int i = 0;
            for (int j = backcountrys.size() - 1; j >= 0; j--) {
                backcountrys.remove(j);
            }
            for (i = 0; i < countrys.size(); i++) {
                if (countrys.get(i).getCountry_name().contains(query) ||
                        countrys.get(i).getName_pinyin().contains(query) ||
                        countrys.get(i).getName_pinyin().toLowerCase().contains(query)) {
                    System.out.println();
                    backcountrys.add(countrys.get(i));
                }

            }
            if (query.length() > 0) {
                //  Log.e("onQueryTextSubmit", "我是点击回车按钮");
                searchView.setIconified(true);
            }
            mCountryAdapter.countrys = backcountrys;
            mCountryAdapter.notifyDataSetChanged();
            hideSoftInput();
            return true;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            int i = 0;
            if (newText.length()>0){
                for (int j = backcountrys.size() - 1; j >= 0; j--) {
                    backcountrys.remove(j);
                }
                for (i = 0; i < countrys.size(); i++) {
                    if (countrys.get(i).getCountry_name().contains(newText) ||
                            countrys.get(i).getName_pinyin().contains(newText) ||
                            countrys.get(i).getName_pinyin().toLowerCase().contains(newText)) {
                        System.out.println();
                        backcountrys.add(countrys.get(i));
                    }

                }
                mCountryAdapter.countrys = backcountrys;
                mCountryAdapter.notifyDataSetChanged();


            }else{
                mCountryAdapter.countrys = countrys;
                mCountryAdapter.notifyDataSetChanged();
            }

            return true;
        }
    }
    class MyCountryItemClickListener implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            tv_index = (TextView) view.findViewById(R.id.tv_index);
            intent = getIntent();
            tv_name = (TextView)view.findViewById(R.id.tv_name);
            for (int i=0;i<countrys.size();i++){
                if (TextUtils.equals(countrys.get(i).getCountry_name(),tv_name.getText().toString()))
                {
                    intent.putExtra("name",countrys.get(i).getCountry_name());
                    intent.putExtra("code",countrys.get(i).getCountry_code());
                    setResult(2,intent);
                    finish();
                    break;


                }
            }

        }
    }

    private void hideSoftInput() {
        if (inputMethodManager != null) {
            View v = CountryChooseActivity.this.getCurrentFocus();
            if (v == null) {
                return;
            }

            inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
            searchView.clearFocus();
        }
    }
}