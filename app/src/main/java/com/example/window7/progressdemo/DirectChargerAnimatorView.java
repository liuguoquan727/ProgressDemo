package com.example.window7.progressdemo;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.ClipDrawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by lgq 7 on 2016/7/5
 * 直流充电动画
 */
public class DirectChargerAnimatorView extends RelativeLayout {

  private ImageView mImage;
  private TextView mProgress;
  private ClipDrawable mClipDrawable;
  private ValueAnimator animator;

  public DirectChargerAnimatorView(Context context) {
    this(context, null);
  }

  public DirectChargerAnimatorView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public DirectChargerAnimatorView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context);
  }

  private void init(Context context) {

    View.inflate(context, R.layout.view_animator_direct_charger, this);

    mImage = (ImageView) this.findViewById(R.id.iv_clip);
    mProgress = (TextView) this.findViewById(R.id.tv_progress);
    mClipDrawable = (ClipDrawable) mImage.getDrawable();

  }

  /**
   * 设置进度
   *
   * @param progress
   */
  public void setProgress(int progress) {

    int current = progress * 100;
    mClipDrawable.setLevel(current);

    String text = progress + "%";
    //TextView上显示不同的字体大小和颜色
    Spannable spannable = new SpannableString(text);
    int end = text.length();
    int start = end - 1;
    spannable.setSpan(new AbsoluteSizeSpan(35),start,end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    mProgress.setText(spannable);

  }

  /**
   * 开始动画
   *
   * @param duration 动画持续时间
   */
  public void startAnim(long duration) {

    stopAnim();
    animator = ObjectAnimator.ofInt(mClipDrawable, "level", 0, 10000);
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
