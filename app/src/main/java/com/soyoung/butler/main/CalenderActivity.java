package com.soyoung.butler.main;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.soyoung.butler.R;

import java.util.Calendar;

public class CalenderActivity extends AppCompatActivity {

    private TextView mDateDisplay;
    private TextView mDateDisplay2;
    private Button mPickDate;
    private Button mPickDate2;
    private int mYear;
    private int mMonth;
    private int mDay;
    public int cnt = 0;

    static final int DATE_DIALOG_ID = 0;
    final Calendar c = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);

        mDateDisplay = (TextView)findViewById(R.id.txt);
        mDateDisplay2 = (TextView)findViewById(R.id.txt2);
        mPickDate = (Button)findViewById(R.id.btn);
        mPickDate2 = (Button)findViewById(R.id.btn2);

        mPickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cnt = 0;
                showDialog(DATE_DIALOG_ID);
            }
        });

        mPickDate2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cnt = 1;
                showDialog(DATE_DIALOG_ID);
            }
        });

        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
    }

    private void updateDisplay()
    {
        mDateDisplay.setText(new StringBuilder()
                .append(mYear).append(" - ")
                .append(mMonth + 1).append(" - ")
                .append(mDay).append(" "));
    }

    private void updateDisplay2()
    {
        mDateDisplay2.setText(new StringBuilder()
                .append(mYear).append(" - ")
                .append(mMonth + 1).append(" - ")
                .append(mDay).append(" "));
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    // TODO Auto-generated method stub

                    if (mYear > year) {
                        Toast.makeText(getApplicationContext(), "출국일 이후의 날짜만 선택해주세요!", Toast.LENGTH_SHORT);
                        return;
                    }
                    else if (mYear == year) {
                        if (mMonth > monthOfYear) {
                            Toast.makeText(getApplicationContext(), "출국일 이후의 날짜만 선택해주세요!", Toast.LENGTH_SHORT);
                            return;
                        }
                        else if (mMonth == monthOfYear) {
                            if (mDay >= dayOfMonth) {
                                Toast.makeText(getApplicationContext(), "출국일 이후의 날짜만 선택해주세요!", Toast.LENGTH_SHORT);
                                return;
                            }
                        }
                    }

                    mYear = year;
                    mMonth = monthOfYear;
                    mDay = dayOfMonth;

                    if(cnt == 0)
                        updateDisplay();
                    else
                        updateDisplay2();

                }
            };

    @Override
    protected Dialog onCreateDialog(int id)
    {
        switch(id)
        {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this, AlertDialog.THEME_DEVICE_DEFAULT_DARK, mDateSetListener, mYear, mMonth, mDay);
        }
        return null;
    }
}
