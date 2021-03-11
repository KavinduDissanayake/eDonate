package com.example.edonate.Screen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.edonate.R;

public class SplashActivity extends AppCompatActivity {
    ImageView img;
    TextView text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_splash );
        init();

    }

    private void init() {
        img=findViewById( R.id.image );
        text=findViewById( R.id.text );
        Animation myanim= AnimationUtils.loadAnimation( this,R.anim.mytransition );
        img.startAnimation(myanim);
        text.startAnimation(myanim);

        Thread timer= new Thread(){
            public void run(){
                try{
                    sleep( 5000 );
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    //enter here navigation actiovity to start using intent
                    startActivity(new Intent(SplashActivity.this,LoginActivity.class));

                    finish();
                }

            }
        };

        timer.start();

    }
}