package com.soyoung.butler.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.soyoung.butler.R;
import com.soyoung.butler.landmark.EssentialLandmark;
import com.tsengvn.typekit.TypekitContextWrapper;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    ViewPager pager;
    Drawable drawable1;
    RadioGroup radioGroup1;
    EditText citySearch;
    RadioButton ch1;
    RadioButton ch2;
    RadioButton ch3;
    RadioButton ch4;
    Button search;
    private String cityName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        citySearch = (EditText)findViewById(R.id.city);
        ch1 = (RadioButton)findViewById(R.id.ch1);
        ch2 = (RadioButton)findViewById(R.id.ch2);
        ch3 = (RadioButton)findViewById(R.id.ch3);
        ch4 = (RadioButton)findViewById(R.id.ch4);
        search = (Button)findViewById(R.id.searchBtn);
        drawable1 = getResources().getDrawable(R.drawable.main_button_unchecked_a);

        radioGroup1 = (RadioGroup)findViewById(R.id.radioGroup1);
        radioGroup1.setOnCheckedChangeListener(this);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EssentialLandmark.class);

                //도시, 체크박스 정보 서버에 넘기기 구현
                cityName = citySearch.getText().toString();

                /*
                * 이 부분은 searchData 객체 만들어서 서버에 보낼 부분
                * 내일 일어나서 구현할 예정입니당ㅠㅠ 빠이빠이
                * */

                startActivity(intent);
            }
        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {  //글꼴을 사용할 액티비티에 이 메소드를 다 넣어줌
        super.attachBaseContext(TypekitContextWrapper.wrap(newBase));
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        Log.e("하이", String.valueOf(checkedId));
        switch (checkedId) {
            case R.id.ch1:
            {
                Log.e("emfdjdhktsl", String.valueOf(checkedId));
                if (ch1.getResources().equals(getResources().getDrawable(R.drawable.main_button_unchecked_a))) //이거 실행안됨..
                    ch1.setBackgroundResource(R.drawable.main_button_checked_a);
                else
                    ch1.setBackgroundResource(R.drawable.main_button_unchecked_a);
            }
            break;
            default:
                break;
        }
    }
}
