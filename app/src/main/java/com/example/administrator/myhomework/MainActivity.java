package com.example.administrator.myhomework;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import static com.example.administrator.myhomework.R.id.LinearMain;

public class MainActivity extends AppCompatActivity {

    private ImageView imgPlay, imgSetting, imgBackground, imgQuit, imgLaba;
    private Intent intent;
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;
    private MediaPlayer mediaPlayer;

    private LinearLayout linearMain;

    private boolean isSound = true;

    private boolean isSwitchOn;
    private int index, indexC, indexY, indexM;
    private int imgID[] = {R.mipmap.bamboo, R.mipmap.ink, R.mipmap.shanshui, R.mipmap.zhu};
    private int Music[] = {R.raw.ling, R.raw.shan};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.main);


        initView();

        intent = getIntent();
        isSwitchOn = intent.getBooleanExtra("isSwitchOn", isSwitchOn);
        indexC = intent.getIntExtra("indexC", indexC);
        indexY = intent.getIntExtra("indexY", indexY);
        indexM = intent.getIntExtra("indexM", indexM);

        index = intent.getIntExtra("index", index);
        linearMain.setBackgroundResource(imgID[index]);


        mediaPlayer = MediaPlayer.create(MainActivity.this, Music[indexM]);
        mediaPlayer.isLooping();
        mediaPlayer.setLooping(true);
        mediaPlayer.start();

        imgPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(MainActivity.this, PlayActivity.class);
                intent.putExtra("indexC", indexC);
                intent.putExtra("indexY", indexY);
                intent.putExtra("isSwitchOn", isSwitchOn);
                intent.putExtra("index", index);
                startActivity(intent);
            }
        });


        imgSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(MainActivity.this, SettingActivity.class);
                intent.putExtra("index", index);
                if (mediaPlayer.isPlaying())
                    mediaPlayer.pause();
                startActivity(intent);
                MainActivity.this.finish();
            }
        });

        imgBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer.isPlaying())
                    mediaPlayer.pause();
                intent = new Intent(MainActivity.this, BackgroundActivity.class);
                startActivity(intent);
                MainActivity.this.finish();
            }
        });

        imgQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("是否确定退出游戏？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mediaPlayer.release();
                                MainActivity.this.finish();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                alertDialog = builder.create();
                alertDialog.show();
            }
        });

        imgLaba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isSound) {
                    if (mediaPlayer.isPlaying())
                        mediaPlayer.pause();
                    imgLaba.setImageResource(R.mipmap.laba2);
                    isSound = false;
                } else {
                    mediaPlayer.start();
                    imgLaba.setImageResource(R.mipmap.laba1);
                    isSound = true;
                }
            }
        });

    }

    void initView() {
        imgPlay = (ImageView) this.findViewById(R.id.imgPlay);
        imgSetting = (ImageView) this.findViewById(R.id.imgSetting);
        imgBackground = (ImageView) this.findViewById(R.id.imgBackg);
        imgQuit = (ImageView) this.findViewById(R.id.imgQuit);
        imgLaba = (ImageView) this.findViewById(R.id.imgLaba);
        linearMain = (LinearLayout) this.findViewById(LinearMain);
    }

}
