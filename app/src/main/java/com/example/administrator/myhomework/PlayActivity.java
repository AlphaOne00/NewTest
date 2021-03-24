package com.example.administrator.myhomework;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PlayActivity extends AppCompatActivity {
    private RelativeLayout layoutPlay;


    private WuziqiPanel mGamePanel;
    private AlertDialog.Builder builder, builder1, builder2;
    private AlertDialog alertDialog;

    private TextView tvTime;
    private ImageView imgPause, imgHuizi, imgTop;

    private int indexC, indexY, index;
    private boolean isSwitchOn;
    private int C[] = {10, 20, 30};
    private int imgID[] = {R.mipmap.bamboo, R.mipmap.ink, R.mipmap.shanshui, R.mipmap.zhu};

    private boolean flag = true;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.play);


        builder = new AlertDialog.Builder(PlayActivity.this);
        builder.setPositiveButton("再来一局", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mGamePanel.restartGame();
            }
        })
                .setNegativeButton("退出游戏", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                        PlayActivity.this.finish();
                    }
                })
                .setCancelable(false)
                .setTitle("此局结束");


        initView();

        Intent intent = getIntent();
        isSwitchOn = intent.getBooleanExtra("isSwitchOn", isSwitchOn);
        indexC = intent.getIntExtra("indexC", indexC);
        indexY = intent.getIntExtra("indexY", indexY);
        index = intent.getIntExtra("index", index);

        layoutPlay.setBackgroundResource(imgID[index]);
        tvTime.setText("倒计时:" + C[indexC] + "秒");


        Vibrator vibrator;
        vibrator = (Vibrator) this.getSystemService(this.VIBRATOR_SERVICE);
        vibrator.vibrate(500);


        mGamePanel.setOnWhiteChangListener(new OnWhiteChangListener() {
            @Override
            public void onWhite(boolean mIsWhite) {

                if (mIsWhite) {
                    flag = false;
                    imgTop.setImageResource(R.mipmap.black);
                } else {
                    flag = !flag;
                    imgTop.setImageResource(R.mipmap.white);
                }
            }
        });

        mGamePanel = (WuziqiPanel) findViewById(R.id.id_wuziqi);
        mGamePanel.setOnGameStatusChangeListener(new OnGameStatusChangListener() {
            @Override
            public void onGameOver(int gameWinResult) {
                switch (gameWinResult) {
                    case WuziqiPanel.WHITE_WIN:
                        builder.setMessage("白棋胜利!");
                        break;
                    case WuziqiPanel.BLACK_WIN:
                        builder.setMessage("黑棋胜利!");
                        break;
                    case WuziqiPanel.NO_WIN:
                        builder.setMessage("和棋!");
                        break;
                }
                alertDialog = builder.create();
                alertDialog.show();
            }
        });

        imgPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder1 = new AlertDialog.Builder(PlayActivity.this);
                builder1.setMessage("暂停中")
                        .setNegativeButton("取消暂停", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setPositiveButton("结束游戏", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                PlayActivity.this.finish();
                            }
                        })
                        .setCancelable(false);
                alertDialog = builder1.create();
                alertDialog.show();
            }
        });

    }

    void initView() {
        mGamePanel = (WuziqiPanel) findViewById(R.id.id_wuziqi);
        imgPause = (ImageView) this.findViewById(R.id.img_play_pause);
        imgHuizi = (ImageView) this.findViewById(R.id.img_play_huizi);
        imgTop = (ImageView) this.findViewById(R.id.img_play_top);
        layoutPlay = (RelativeLayout) this.findViewById(R.id.layoutPlay);
        tvTime = (TextView) this.findViewById(R.id.tvTime);
    }

    public class MyCount extends CountDownTimer {

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public MyCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            long hour = millisUntilFinished / 1000 / 3600;
            long minute = millisUntilFinished / 1000 % 3600 / 60;
            long second = millisUntilFinished / 1000 % 3600 % 60;
            tvTime.setText("倒计时：" + second);

        }


        @Override
        public void onFinish() {
            tvTime.setText("时间到！");
            builder2 = new AlertDialog.Builder(PlayActivity.this);
            builder2.setPositiveButton("再来一局", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    mGamePanel.restartGame();
                }
            })
                    .setNegativeButton("退出游戏", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            PlayActivity.this.finish();
                        }
                    })
                    .setCancelable(false);
            if (!flag)
                builder2.setTitle("此局结束,白子获胜");
            if (flag)
                builder2.setTitle("此局结束，黑子获胜");

            alertDialog = builder2.create();
            alertDialog.show();
        }
    }
}
