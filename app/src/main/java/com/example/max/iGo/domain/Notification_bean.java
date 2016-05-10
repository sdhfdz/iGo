package com.example.max.iGo.domain;

import java.util.ArrayList;

/**
 * Created by sdh on 2016/4/18.
 */
public class Notification_bean {
    public String status;
    public ArrayList<notification_content> value;
    public static class notification_content{
        public String id;
        public String pic;
        public  String title;
        public  String description;
        public  String createTime;
        public  String pid;

    }
}
