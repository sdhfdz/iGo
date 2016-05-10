package com.example.max.iGo.domain;

import java.util.ArrayList;

/**
 * Created by sdh on 2016/4/14.
 */
public class Commet_article {
    public String id;
    public String[] description;
    public String[] description_title;
    public String[] pic;
    public ArrayList<comment_cls> comment;
    public Product product;

    public static class Product {
        public String id;
        public String sale;
        public String price;
        public String name;
        public String pic;
    }

    public static class comment_cls {
        public String time;
        public String id;
        public String comment;
        public User user;

        public static class User {
            public String icon;
            public String name;
            public String id;
        }

    }
}
