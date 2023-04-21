package com.example.juan.tabatatimer;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private Context mContext;
    private Button btnstart,btnreset;
    private TextView tvshow,timeTv,tvhot2,tvrest2,tvwork2,tvtimes2;
    private EditText ethot,etrest,etwork,ettimes;
    int hot,rest,work,times;
    int h ,r , w ,t ;
    private Timer timer = null , timer1 =null , timer2 =null ;
    private boolean one = true;
    private boolean start = true;
    private boolean first = true;

    public TimerTask timerTask = null;
    public TimerTask timerTask1 =null;
    public TimerTask timerTask2 =null;

    private SoundPool soundPool;
    private int[] soundID = new int[1];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findView();
        usesoundPool();
        setListener();

    }

    public void findView(){

        tvshow = findViewById(R.id.tvshow);
        timeTv = findViewById(R.id.timeTv);

        btnstart = findViewById(R.id.btnstart);
        btnreset = findViewById(R.id.btnreset);

        ethot = findViewById(R.id.ethot);
        etrest = findViewById(R.id.etrest);
        etwork = findViewById(R.id.etwork);
        ettimes = findViewById(R.id.ettimes);

        tvhot2 = findViewById(R.id.tvhot2);
        tvrest2  = findViewById(R.id.tvrest2);
        tvwork2 = findViewById(R.id.tvwork2);
        tvtimes2 = findViewById(R.id.tvtimes2);
    }
    public void usesoundPool(){
        if(Build.VERSION.SDK_INT > 21){
            SoundPool.Builder builder = new SoundPool.Builder();
            builder.setMaxStreams(5);
            AudioAttributes.Builder attrBuilder = new AudioAttributes.Builder();
            attrBuilder.setLegacyStreamType(AudioManager.STREAM_SYSTEM);
            builder.setAudioAttributes(attrBuilder.build());
            soundPool = builder.build();
        }else {
            soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        }
         soundID[0]=soundPool.load(this,R.raw.pianoc2,1);
    }

    public void setListener() {

        btnstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    hot = Integer.valueOf(ethot.getText().toString());
                    rest = Integer.valueOf(etrest.getText().toString());
                    work = Integer.valueOf(etwork.getText().toString());
                    times = Integer.valueOf(ettimes.getText().toString());
                }catch(Exception e){
                    hot=0;rest=0;work=0;times=0;
                }
                if(hot>0 && rest >0 && work>0 && times>0) {
                    if (start) {
                        if (first) {
                            setVisibility();
                            h = hot + 1;
                            r = rest + 1;
                            w = work + 1;
                            t = times;
                            one = true;
                        }
                        start = false;
                        btnstart.setText("Stop");
                        startTime();
                    } else if (!start) {
                        btnstart.setText("Start");
                        stopTime();
                    }
                }else {
                    timeTv.setText("Please enter a numerical value greater than 0");
                }
            }
        });
        btnreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!first) {
                    restTime();
                    ethot.setText(R.string.timerhot);
                    etrest.setText(R.string.timerrest);
                    etwork.setText(R.string.timerwork);
                    ettimes.setText(R.string.timertimes);
                    timeTv.setText(" ");
                }
            }
        });
    }

    public void setVisibility(){
        ethot.setVisibility(View.INVISIBLE);
        etrest.setVisibility(View.INVISIBLE);
        etwork.setVisibility(View.INVISIBLE);
        ettimes.setVisibility(View.INVISIBLE);

        tvhot2.setVisibility(View.VISIBLE);
        tvrest2.setVisibility(View.VISIBLE);
        tvwork2.setVisibility(View.VISIBLE);
        tvtimes2.setVisibility(View.VISIBLE);

        tvhot2.setText(ethot.getText().toString());
        tvrest2.setText(etrest.getText().toString());
        tvwork2.setText(etwork.getText().toString());
        tvtimes2.setText(ettimes.getText().toString());
    }

    public  void restsetVisibility(){
        ethot.setVisibility(View.VISIBLE);
        etrest.setVisibility(View.VISIBLE);
        etwork.setVisibility(View.VISIBLE);
        ettimes.setVisibility(View.VISIBLE);

        tvhot2.setVisibility(View.INVISIBLE);
        tvrest2.setVisibility(View.INVISIBLE);
        tvwork2.setVisibility(View.INVISIBLE);
        tvtimes2.setVisibility(View.INVISIBLE);
    }


    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            String time = String.valueOf(message.arg1);
            tvshow.setText(time);
            startTime();
            return false;
        }
    });

    public void startTime(){

        timer = new Timer();
        timer1 = new Timer();
        timer2 = new Timer();
        timerTask =new TimerTask() {
            @Override
            public void run() {
                h--;
                Message message= mHandler.obtainMessage();
                message. arg1= h;
                mHandler.sendMessage(message);
            }
        };
        timerTask1 =new TimerTask() {
            @Override
            public void run() {
                r--;
                Message message= mHandler.obtainMessage();
                message. arg1= r;
                mHandler.sendMessage(message);
            }
        };
        timerTask2 = new TimerTask(){
            @Override
            public void run() {
                w--;
                Message message= mHandler.obtainMessage();
                message. arg1= w;
                mHandler.sendMessage(message);
            }
        };

        if(one) {
            if(h==hot)timeTv.setText("Warm up");
            timer.schedule(timerTask,1000);
        }

        if(h==0) {
            if(r==(rest+1))
                soundPool.play(soundID[0], 1.0f, 1.0f, 0, 0, 1.0f);
            timer.cancel();
            one=false;
            timer1.schedule(timerTask1, 1000);
            if(r==rest)timeTv.setText("Rest ");

            if(r==0) {
                timer1.cancel();
                if(w==(work+1))
                    soundPool.play(soundID[0], 1.0f, 1.0f, 0, 0, 1.0f);
                timer2.schedule(timerTask2, 1000);
                if(w==work)timeTv.setText("Workout");

                if(w==0) {
                    timer2.cancel();
                    r = rest+1;
                    w = work+1;
                    t--;
                    if(t!=0) startTime();
                    if(t==0)
                    {
                        soundPool.play(soundID[0], 1.0f, 1.0f, 0, 0, 1.0f);
                        btnstart.setText("Start");
                        timeTv.setText("Good job!");
                        start=true;
                        first=true;
                        restsetVisibility();
                    }
                }
            }
        }
    }
    public void stopTime(){
        timer.cancel();
        timer1.cancel();
        timer2.cancel();
        start = true;
        first = false ;
    }
    public void restTime(){
        timer.cancel();
        timer1.cancel();
        timer2.cancel();
        tvshow.setText("0");
        first = true;
        one = true;
        start = true;
        btnstart.setText("Start");
        restsetVisibility();
    }

}
