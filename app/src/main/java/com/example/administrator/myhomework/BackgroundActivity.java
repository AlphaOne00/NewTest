package com.example.administrator.myhomework;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class BackgroundActivity extends AppCompatActivity {

    private LinearLayout linearBackground;

    private ImageView imgBackground, imgBackgDown, imgBackgUp;
    private TextView tvName;
    private Button btn;

    private Intent intent;

    private int imgID[] = {R.mipmap.bamboo, R.mipmap.ink, R.mipmap.shanshui, R.mipmap.zhu};
    private String Name[] = {"竹意盎然", "水墨山水", "出淤不染", "门泊东吴"};
    private int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.background);

        initView();
        linearBackground.setBackgroundResource(imgID[index]);

        imgBackgDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index--;
                if (index < 0)
                    index = (imgID.length - 1);
                imgBackground.setImageResource(imgID[index]);
                tvName.setText(Name[index]);
            }
        });

        imgBackgUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index++;
                if (index > (imgID.length - 1))
                    index = 0;
                imgBackground.setImageResource(imgID[index]);
                tvName.setText(Name[index]);
            }
        });





        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(BackgroundActivity.this, MainActivity.class);
                intent.putExtra("index", index);
                startActivity(intent);
                BackgroundActivity.this.finish();
            }
        });


    }

    void initView() {
        imgBackgDown = (ImageView) this.findViewById(R.id.img_backg_down);
        imgBackgUp = (ImageView) this.findViewById(R.id.img_backg_up);
        imgBackground = (ImageView) this.findViewById(R.id.img_background);
        tvName = (TextView) this.findViewById(R.id.tv_backg);
        btn = (Button) this.findViewById(R.id.btn_backg);
        linearBackground = (LinearLayout) this.findViewById(R.id.LinearBackground);
    }
}
