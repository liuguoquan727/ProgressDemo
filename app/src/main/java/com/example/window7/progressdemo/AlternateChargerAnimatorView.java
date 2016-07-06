package com.example.window7.progressdemo;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.ClipDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Created by lgq 7 on 2016/7/5
 * 交流充电动画
 */
public class AlternateChargerAnimatorView extends RelativeLayout {

  private ImageView mImage;
  private ClipDrawable mClipDrawable;
  private ValueAnimator animator;

  public AlternateChargerAnimatorView(Context context) {
    this(context,null);
  }

  public AlternateChargerAnimatorView(Context context, AttributeSet attrs) {
    this(context, attrs,0);
  }

  public AlternateChargerAnimatorView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context);
  }

  private void init(Context context) {

    View.inflate(context,R.layout.view_animator_alternate_charger,this);

    mImage = (ImageView) this.findViewById(R.id.iv_clip);
    mClipDrawable = (ClipDrawable) mImage.getDrawable();

    startAnim(2000);
  }

  /**
   * 开始动画
   * @param duration 动画持续时间
   */
  public void startAnim(long duration) {

    stopAnim();
    animator = ObjectAnimator.ofInt(mClipDrawable,"level",0,10000);
    animator.setDuration(duration);
    animator.setRepeatCount(ValueAnimator.INFINITE);
    animator.setRepeatMode(ValueAnimator.RESTART);
    animator.start();
  }

  /**
   * 停止动画
   */
  public void stopAnim() {

    if (animator != null) {
      animator.cancel();
    }

  }
}
