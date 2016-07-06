package com.example.window7.progressdemo;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;


public class LoadingImageView extends ImageView {
    private Paint imagePaint;
    private BitmapShader shader;
    private Paint paint;
    private int imageHeight, imageWidth;
    private boolean autoStart = true;
    private int maskColor = Color.TRANSPARENT;

    public LoadingImageView(Context context) {
        this(context, null);
    }

    public LoadingImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        initAttrs(context, attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs){
        if(attrs == null){
            return;
        }
        TypedArray t = context.obtainStyledAttributes(attrs, R.styleable.LoadingImageView);
        maskColor = t.getColor(R.styleable.LoadingImageView_mask_color, maskColor);
        autoStart = t.getBoolean(R.styleable.LoadingImageView_anim_auto_start, autoStart);
        setMaskColor(maskColor);
        t.recycle();
    }

    private void init() {
        if (paint == null) {
            imagePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            imagePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
            imagePaint.setAlpha(128);
            paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        }
    }

    private float scaleX,scaleY;
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        float[] f = new float[9];
        getImageMatrix().getValues(f);

        scaleX = f[Matrix.MSCALE_X];
        scaleY = f[Matrix.MSCALE_Y];

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //canvas.drawRect(0, maskHeight, getWidth(), getHeight(), paint);
        canvas.save();
        canvas.scale(scaleX, scaleY);
        clipDrawable.setBounds(getDrawable().getBounds());
        clipDrawable.draw(canvas);
        canvas.restore();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAnim();
    }


    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        if(maskColor != Color.TRANSPARENT){
            init();
            initMaskBitmap(maskColor);
            initAnim();
        }
    }

    private Bitmap combineImages(Bitmap bgd, Bitmap fg) {
        Bitmap bmp = Bitmap.createBitmap(imageWidth, imageHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        canvas.drawBitmap(bgd, 0, 0, null);
        canvas.drawBitmap(fg, 0, 0, imagePaint);
        return bmp;
    }

    private ClipDrawable clipDrawable;
    private Drawable maskDrawable;
    private void initMaskBitmap(int maskColor) {
        Drawable drawable = getDrawable();
        if(drawable == null){
            return;
        }
        Bitmap bgd = ((BitmapDrawable) drawable).getBitmap();
        imageWidth = drawable.getIntrinsicWidth();
        imageHeight = drawable.getIntrinsicHeight();

        Bitmap fg = Bitmap.createBitmap(imageWidth, imageHeight, Bitmap.Config.ARGB_8888);
        Canvas fgCanvas = new Canvas(fg);
        fgCanvas.drawColor(maskColor);


        Bitmap bitmap = combineImages(bgd, fg);
        maskDrawable = new BitmapDrawable(bitmap);
        clipDrawable = new ClipDrawable(maskDrawable, gravity, orientaion);

        //shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        //paint.setShader(shader);
    }

    private int maskHeight;

    private void setMaskHeight(int y) {
        maskHeight = y;
        invalidate();
    }

    private ObjectAnimator animator;

    private void initAnim() {
        stopAnim();
        //animator = ObjectAnimator.ofInt(this, "maskHeight", 0, imageHeight);
        animator = ObjectAnimator.ofInt(clipDrawable, "level", 0, 10000);
        animator.setDuration(animDuration);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                invalidate();
            }
        });
        if(autoStart){
            animator.start();
        }
    }

    private void stopAnim(){
        if(animator != null){
            animator.cancel();
        }
    }


    private long animDuration = 1500;

    /**
     * 设置动画时长
     */
    public void setMaskAnimDuration(long duration) {
        animDuration = duration;
        initAnim();
    }

    /**
     * 开始播放动画
     */
    public void startMaskAnim() {
        if (animator != null) {
            animator.start();
        }
    }

    public void setMaskColor(int color) {
        initMaskBitmap(color);
        initAnim();
    }

    private int gravity = Gravity.BOTTOM;
    private int orientaion = ClipDrawable.VERTICAL;
    private int maskOrientation = MaskOrientation.BottomToTop;
    /**
     * 设置方向
     * @param orientation {@link MaskOrientation}
     */
    public void setMaskOrientation(int orientation){
        switch (orientation){
            case MaskOrientation.LeftToRight:
                gravity = Gravity.LEFT;
                orientaion = ClipDrawable.HORIZONTAL;
                break;
            case MaskOrientation.RightToLeft:
                gravity = Gravity.RIGHT;
                orientaion = ClipDrawable.HORIZONTAL;
                break;
            case MaskOrientation.TopToBottom:
                gravity = Gravity.TOP;
                orientaion = ClipDrawable.VERTICAL;
                break;
            case MaskOrientation.BottomToTop:
            default:
                gravity = Gravity.BOTTOM;
                orientaion = ClipDrawable.VERTICAL;
                break;
        }
        if(maskDrawable == null){
            return;
        }
        clipDrawable = new ClipDrawable(maskDrawable, gravity, orientaion);
        initAnim();
    }

    public int getMaskOrientation() {
        return maskOrientation;
    }

    public static final class MaskOrientation{
        public static final int LeftToRight = 1;
        public static final int RightToLeft = 2;
        public static final int TopToBottom = 3;
        public static final int BottomToTop = 4;
    }

}
