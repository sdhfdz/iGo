package com.example.max.iGo.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.RelativeLayout;

import com.example.max.iGo.Base.BaseActivity;
import com.example.max.iGo.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import cn.jpush.android.api.JPushInterface;

public class SplashIndexActivity extends BaseActivity {

    private RelativeLayout splash_img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_index);
        splash_img=(RelativeLayout)findViewById(R.id.splash_img);
        startAnim();
//        ReadDbCountry_Phone.GetCountryName_NUmber();


    }
    public void startAnim(){
        dbCopy("country_phone.db");
        AnimationSet amset=new AnimationSet(false);
        AlphaAnimation alpha = new AlphaAnimation(0, 1);
        alpha.setDuration(2000);// 动画时间
        alpha.setFillAfter(true);// 保持动画状态
        amset.addAnimation(alpha);
        amset.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                jump_to_nextpage();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        splash_img.startAnimation(amset);
    }
    public void jump_to_nextpage(){
        startActivity(new Intent(SplashIndexActivity.this, GuideIndexActivity.class));
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }
    public void dbCopy(String dbName){
        FileOutputStream out=null;
        InputStream in=null;
        File dest=new File(getFilesDir(),dbName);
        if(dest.exists()){
            System.out.println("已经存在！！！！");
            return;
        }
        try {
             in= getAssets().open(dbName);
             out= new FileOutputStream(dest);
            int len=0;
            byte[] buffer=new byte[1024];
            while((len=in.read(buffer))!=-1){
                out.write(buffer,0,len);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                in.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }
}
