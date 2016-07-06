package com.example.window7.progressdemo;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.drawable.ClipDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

  private ObjectAnimator animator;
  private ObjectAnimator animator2;

  private DirectChargerAnimatorView mDirectChargerAnimatorView;

  private TextView mProgressTxt;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main_res_2);

    ClipDrawable clipDrawable =
        (ClipDrawable) ((ImageView)findViewById(R.id.iv_clip)).getDrawable();

    animator = ObjectAnimator.ofInt(clipDrawable,"level",0,10000);
    animator.setDuration(3000);
    animator.setRepeatCount(ValueAnimator.INFINITE);
    animator.setRepeatMode(ValueAnimator.RESTART);
    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override public void onAnimationUpdate(ValueAnimator animation) {
      }
    });
    animator.start();


    mDirectChargerAnimatorView = (DirectChargerAnimatorView) findViewById(R.id.view_direct_charger);

    mProgressTxt = (TextView) findViewById(R.id.tv_progress);
    mProgressTxt.bringToFront();

    ClipDrawable clipDrawable2 =
        (ClipDrawable) ((ImageView)findViewById(R.id.iv_clip_second)).getDrawable();

    animator2 = ObjectAnimator.ofInt(clipDrawable2,"level",0,10000);
    animator2.setDuration(3000);
    animator2.setRepeatCount(3);
    animator2.setRepeatMode(ValueAnimator.RESTART);
    animator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
      @Override public void onAnimationUpdate(ValueAnimator animation) {

        int progress = (int) animation.getAnimatedValue() / 100 ;

        mProgressTxt.setText(progress +"%");
        mDirectChargerAnimatorView.setProgress(progress);
      }
    });
    animator2.start();


  }

  @Override protected void onDestroy() {
    super.onDestroy();
    if (animator != null) {
      animator.cancel();
    }
  }
}
