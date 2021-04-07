package com.copypaseit.quiz420_play_earn;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.copypaseit.quiz420_play_earn.Section_fragment.LeaderBoard;
import com.copypaseit.quiz420_play_earn.Section_fragment.SubjectBased;
import com.copypaseit.quiz420_play_earn.Section_fragment.Tournament;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView timer_count_tv,freeCoinTv;
    private static final long START_IN_TIME_MILLIS = 3600000;
    private boolean isTimerRunning;
    private CountDownTimer countDownTimer;
    private long mTimeMills = START_IN_TIME_MILLIS;
    private long endTime;
    private FrameLayout fragment_container;

    ColorStateList def;
    TextView item1;
    TextView item2;
    TextView item3;
    TextView select;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        timer_count_tv = findViewById(R.id.timer_count_tv);
        freeCoinTv = findViewById(R.id.coinTv_id);
        fragment_container = findViewById(R.id.fragment_container_id);
        item1 = findViewById(R.id.item1);
        item2 = findViewById(R.id.item2);
        item3 = findViewById(R.id.item3);
        item1.setOnClickListener(this);
        item2.setOnClickListener(this);
        item3.setOnClickListener(this);
        select = findViewById(R.id.select);
        def = item2.getTextColors();
        startStop();
    }

    private void startStop(){
        if (isTimerRunning){
            stopTimer();
        }
        else {
            startTime();
        }
    }
    private void stopTimer(){

    }
    private void startTime(){
        endTime = System.currentTimeMillis() + mTimeMills;
        countDownTimer = new CountDownTimer(mTimeMills,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeMills = millisUntilFinished;
                updateTimer();
            }

            @Override
            public void onFinish() {
                freeCoinTv.setText("100");
            }
        }.start();
        isTimerRunning = true;
    }

    private void updateTimer(){
        int hours = (int) (mTimeMills / 1000) / 60 / 60;
        int minutes = (int) (mTimeMills /1000) / 60;
        int seconds = (int) (mTimeMills /1000) % 60;
        String countTimeText = String.format(Locale.getDefault(),"%02d:%02d:%02d",hours,minutes,seconds);
        timer_count_tv.setText(countTimeText);
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences prefs = getSharedPreferences("prefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong("mTimeMillis",mTimeMills);
        editor.putLong("endTime",endTime);
        editor.putBoolean("isTimerRunning",isTimerRunning);
        editor.apply();
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences prefs =  getSharedPreferences("prefs",MODE_PRIVATE);
        mTimeMills = prefs.getLong("mTimeMillis",START_IN_TIME_MILLIS);
        isTimerRunning = prefs.getBoolean("isTimerRunning",false);
        updateTimer();
        if (isTimerRunning){
            endTime = prefs.getLong("endTime",0);
            mTimeMills = endTime - System.currentTimeMillis();
            if (mTimeMills < 0){
                mTimeMills = 0;
                isTimerRunning = false;
                updateTimer();
            }else {
                startTime();
            }
        }

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.item1){
            select.animate().x(0).setDuration(100);
            item1.setTextColor(Color.WHITE);
            item2.setTextColor(def);
            item3.setTextColor(def);
            replaceFragment(new SubjectBased());
        } else if (view.getId() == R.id.item2){
            item1.setTextColor(def);
            item2.setTextColor(Color.WHITE);
            item3.setTextColor(def);
            int size = item2.getWidth();
            select.animate().x(size).setDuration(100);
            replaceFragment(new LeaderBoard());
        } else if (view.getId() == R.id.item3){
            item1.setTextColor(def);
            item3.setTextColor(Color.WHITE);
            item2.setTextColor(def);
            int size = item2.getWidth() * 2;
            select.animate().x(size).setDuration(100);
            replaceFragment(new Tournament());

        }
    }
    private void replaceFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container_id,fragment);
        transaction.commit();
    }
}