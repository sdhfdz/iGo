package com.example.max.iGo.Utils;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.HashMap;

/**
 * Created by sdh on 2016/4/10.
 */
public class ReadDbCountry_Phone {
    // private static ArrayList arrayList=new ArrayList();
    private static HashMap<String, String> hashMap = new HashMap<>();
    private final static String path = "data/data/com.example.max.igo/files/country_phone.db";

    public static HashMap<String, String> GetCountryName_NUmber() {
        SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
        Cursor cursor = db.query("country", new String[]{"country_code", "country_name_cn"},
                null, null, null, null, null);
        while (cursor.moveToNext()) {
            String countryname = cursor.getString(cursor.getColumnIndex("country_name_cn"));
            String countrycode = cursor.getString(cursor.getColumnIndex("country_code"));
            // System.out.println(countrycode + ":" + countryname + "NNN");
            hashMap.put(cursor.getString(cursor.getColumnIndex("country_code")), cursor.getString(cursor.getColumnIndex("country_name_cn")));
        }
        cursor.close();
        return hashMap;
    }


}
