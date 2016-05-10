package com.example.max.iGo.domain;

/**
 * Created by sdh on 2016/4/19.
 */
public class Countrys implements Comparable<Countrys>{
    private String country_code;
    private String country_name;
    private String name_pinyin;

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }

    public void setName_pinyin(String name_pinyin) {
        this.name_pinyin = name_pinyin;
    }

    public String getCountry_code() {
        return country_code;
    }

    public String getCountry_name() {
        return country_name;
    }

    public String getName_pinyin() {
        return name_pinyin;
    }

    @Override
    public int compareTo(Countrys another) {
        return this.name_pinyin.compareTo(another.getName_pinyin());
    }
}
