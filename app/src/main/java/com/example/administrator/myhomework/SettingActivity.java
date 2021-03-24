package com.example.administrator.myhomework;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SettingActivity extends AppCompatActivity {
    private ImageView imgCountDown, imgCountUp, imgYinxiaoDown, imgYinxiaoUp, imgMusicDown, imgMusicUp, imgCount, imgSwitch;
    private TextView tvYinxiao, tvMusic;
    private Button btnOut;
    private Intent intent;

    private LinearLayout linearSettting;

    private int Count[] = {R.mipmap.ten, R.mipmap.t20, R.mipmap.t30};
    private int indexC = 0;

    private boolean isSwitchOn = true;

    private String[] Yinxiao = {"木琴", "快板", "棋子"};
    private int indexY = 0;

    private String[] Music = {"将军令", "高山流水"};
    private int indexM = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.setting);
        initView();


        imgCountDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                indexC--;
                if (indexC < 0)
                    indexC = (Count.length - 1);
                imgCount.setImageResource(Count[indexC]);
            }
        });
        imgCountUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                indexC++;
                if (indexC > (Count.length - 1))
                    indexC = 0;
                imgCount.setImageResource(Count[indexC]);
            }
        });

        imgSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSwitchOn)
                    imgSwitch.setImageResource(R.mipmap.guan);
                if (!isSwitchOn)
                    imgSwitch.setImageResource(R.mipmap.kai);
                isSwitchOn = !isSwitchOn;
            }
        });

        imgYinxiaoDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                indexY--;
                if (indexY < 0) ;
                indexY = (Yinxiao.length - 1);
                tvYinxiao.setText(Yinxiao[indexY]);
            }
        });
        imgYinxiaoUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                indexY++;
                if (indexY > (Yinxiao.length - 1))
                    indexY = 0;
                tvYinxiao.setText(Yinxiao[indexY]);
            }
        });

        imgMusicDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                indexM--;
                if (indexM < 0)
                    indexM = (Music.length - 1);
                tvMusic.setText(Music[indexM]);
            }
        });
        imgMusicUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                indexM++;
                if (indexM > Music.length - 1)
                    indexM = 0;
                tvMusic.setText(Music[indexM]);
            }
        });


        btnOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(SettingActivity.this, MainActivity.class);
                intent.putExtra("indexC", indexC);
                intent.putExtra("indexY", indexY);
                intent.putExtra("indexM", indexM);
                intent.putExtra("isSwitchOn", isSwitchOn);
                startActivity(intent);
                SettingActivity.this.finish();
            }
        });
    }

    void initView() {
        imgCountDown = (ImageView) this.findViewById(R.id.img_count_down);
        imgCountUp = (ImageView) this.findViewById(R.id.img_count_up);
        imgYinxiaoDown = (ImageView) this.findViewById(R.id.img_yinxiao_down);
        imgYinxiaoUp = (ImageView) this.findViewById(R.id.img_yinxiao_up);
        imgMusicDown = (ImageView) this.findViewById(R.id.img_music_down);
        imgMusicUp = (ImageView) this.findViewById(R.id.img_music_up);
        imgCount = (ImageView) this.findViewById(R.id.img_count);
        imgSwitch = (ImageView) this.findViewById(R.id.img_setting_switch);
        btnOut = (Button) this.findViewById(R.id.btn_setting_out);
        tvYinxiao = (TextView) this.findViewById(R.id.tv_setting_yinxiao);
        tvMusic = (TextView) this.findViewById(R.id.tv_settting_music);
        linearSettting = (LinearLayout) this.findViewById(R.id.LinearSetting);
    }
}
