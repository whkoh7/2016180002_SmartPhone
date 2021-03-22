package kr.ac.kpu.game.s2016180002.firstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    int cnt = 0;
    int imgs[] = {R.drawable.cat1,R.drawable.cat2,R.drawable.cat3,R.drawable.cat4,R.drawable.cat5};
    ImageButton prevBtn;
    ImageButton nextBtn;
    ImageView image;
    TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prevBtn = (ImageButton)findViewById(R.id.button_prev);
        nextBtn = (ImageButton)findViewById(R.id.button_next);
        image = (ImageView)findViewById(R.id.img);
        txt = (TextView)findViewById(R.id.textView);
        findViewById(R.id.button_prev).setOnClickListener(mClickListener);
        findViewById(R.id.button_next).setOnClickListener(mClickListener);
    }

    View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // 무슨 일이 일어날지
            switch(v.getId()){
                case R.id.button_prev:
                    if(cnt>0) {
                        image.setImageResource(imgs[--cnt]);
                        txt.setText((cnt + 1)+"/"+5);
                        if(cnt == 0)
                            prevBtn.setEnabled(false);
                        else if(cnt == 3)
                            nextBtn.setEnabled(true);
                    }
                    break;
                case R.id.button_next:
                    if(cnt<imgs.length-1){
                        image.setImageResource(imgs[++cnt]);
                        txt.setText((cnt+1)+"/"+5);
                        if(cnt == 4)
                            nextBtn.setEnabled(false);
                        else if(cnt == 1)
                            prevBtn.setEnabled(true);
                    }
            }
        }
    };

    public void OnBtnHello(View view){}
}