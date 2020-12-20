package com.fei.lettersidebar;

import android.os.Bundle;
import android.util.Log;

import com.fei.lettersidebar.db.PlaceUtil;
import com.fei.lettersidebar.widget.LetterFilterListView;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private LetterFilterListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.list_view);

        //获取权限，这里就没写了

        Log.e("tag","需要获取权限");

        //获取城市
        List<String> citys = PlaceUtil.getCitys(this);
        listView.setData(citys);
    }


}