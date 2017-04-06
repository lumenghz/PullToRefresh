package lumenghz.com.pulllaunchrocket.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import lumenghz.com.pulllaunchrocket.R;
import lumenghz.com.pullrefresh.PullToRefreshView;
import lumenghz.com.pullrefresh.internal.AnimationFactory;
import lumenghz.com.pullrefresh.internal.CreateBitmapFactory;
import lumenghz.com.pullrefresh.refresh_view.BaseRefreshView;

/**
 * @author lumeng on 2016-07-01.
 */
public class SunRefreshView extends BaseRefreshView {
    private static final float HEIGHT_RATIO           = 1.0f;
    private static final float BUILDING_INITIAL_SCALE = 1.0f;
    private static final float BUILDING_FINAL_SCALE   = 1.2f;

    private PullToRefreshView mParent;

    private Matrix mMatrix;

    private Context mContext;

    private Animation mSunAnimation;

    private Bitmap mSun;
    private Bitmap mBuilding;

    private Paint mBackgroundPaint;

    private boolean isRefreshing = false;
    private boolean isSunRise    = true;

    private float mBuildingTopOffset;

    private float mSunRotateAngle;
    private float mPercent;

    private int mSunWidth;
    private int mSunHeight;

    /**
     * height of landscape
     */
    private int mSenceHeight;
    /**
     * width of landscape
     */
    private int mScreenWidth;
    /**
     * distance between bottom of landscape and top of landscape
     */
    private int mTop;
    /**
     * max distance between bottom of landscape and top of landscape
     */
    private int totalDistance;

    public SunRefreshView(final PullToRefreshView layout) {
        super(layout);

        mParent = layout;
        mMatrix = new Matrix();
        mContext = getContext();
        setupAnimations();
        setupPaint();
        layout.post(new Runnable() {
            @Override
            public void run() {
                initialDimens(layout.getWidth());
            }
        });
    }

    @Override
    protected void initialDimens(int viewWidth) {
        if (viewWidth <= 0 || viewWidth == mScreenWidth) return;

        createBitmaps();

        mScreenWidth = viewWidth;
        mSenceHeight = (int) (HEIGHT_RATIO * mScreenWidth);

        mTop = -mParent.getTotalDragDistance();
        totalDistance = -mTop;

        mBuildingTopOffset = -mTop - mBuilding.getHeight();

        mSunWidth = mSun.getWidth();
        mSunHeight = mSun.getHeight();
    }

    private void createBitmaps() {
        mSun = CreateBitmapFactory.getBitmapFromImage(R.drawable.sun, mContext);
        mBuilding = CreateBitmapFactory.getBitmapFromImage(R.drawable.home_title_building_hz, mContext);
    }

    @Override
    public void draw(Canvas canvas) {
        if (mScreenWidth <= 0) return;

        final int saveCount = canvas.save();

        canvas.translate(0, mTop);
        canvas.clipRect(0, -mTop, mScreenWidth, mParent.getTotalDragDistance());
        canvas.drawRect(0, -mTop, mScreenWidth, mParent.getTotalDragDistance(), mBackgroundPaint);

        drawBuilding(canvas);
        drawSun(canvas);

        canvas.restoreToCount(saveCount);
    }

    /**
     * Draw building
     *
     * @param canvas canvas
     */
    private void drawBuilding(Canvas canvas) {
        final Matrix matrix = mMatrix;
        matrix.reset();

        float dragPercent = Math.min(1f, Math.abs(mPercent));

        float buildingScale;

        buildingScale = BUILDING_INITIAL_SCALE + (BUILDING_FINAL_SCALE - BUILDING_INITIAL_SCALE) * dragPercent;
        matrix.preScale(buildingScale, buildingScale);

        final float offsetX = mScreenWidth / 2
                - mBuilding.getWidth() / 2
                + (1f - buildingScale) * mBuilding.getWidth() / 2;
        final float offsetY = mBuildingTopOffset;

        matrix.postTranslate(offsetX, offsetY);
        canvas.drawBitmap(mBuilding, matrix, null);
    }

    /**
     * Draw sun
     *
     * @param canvas canvas
     */
    private void drawSun(Canvas canvas) {
        final Matrix matrix = mMatrix;
        matrix.reset();

        float dragPercent = Math.min(1f, Math.abs(mPercent));

        final float offsetX = isRefreshing ? mScreenWidth / 2 * (2 - dragPercent) - mSunWidth / 2 : (mScreenWidth * dragPercent - mSunWidth) / 2;
        final float offsetY = totalDistance * func(dragPercent);

        matrix.preRotate(mSunRotateAngle == 0.0f ? (360 * dragPercent) : mSunRotateAngle, mSunWidth / 2, mSunHeight / 2);
        matrix.postTranslate(offsetX, offsetY);
        canvas.drawBitmap(mSun, matrix, null);
    }

    @Override
    protected void setupAnimations() {
        AnimationFactory factory = new AnimationFactory();
        mSunAnimation = factory.getSunRotate(new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                mSunRotateAngle = 720 * setVariable(interpolatedTime);
            }
        });
        mSunAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                resetOrigins();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    private void setupPaint() {
        mBackgroundPaint = new Paint();
        mBackgroundPaint.setColor(Color.rgb(251, 66, 49));
        mBackgroundPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    public void setBounds(int left, int top, int right, int bottom) {
        super.setBounds(left, top, right, mSenceHeight + top);
    }

    @Override
    public void setPercent(float percent, boolean invalidate) {
        setPercent(percent);
    }

    private void setPercent(float percent) {
        this.mPercent = percent;
    }

    @Override
    public void offsetTopAndBottom(int offset) {
        mTop += offset;
        invalidateSelf();
    }

    @Override
    public void start() {
        isRefreshing = true;
        mSunAnimation.reset();

        mParent.startAnimation(mSunAnimation);
    }

    @Override
    public void stop() {
        mParent.clearAnimation();
        isRefreshing = false;
    }

    @Override
    public boolean isRunning() {
        return false;
    }

    private float setVariable(float value) {
        invalidateSelf();
        return value;
    }

    private void resetOrigins() {
        setPercent(0);
        mSunRotateAngle = 0.0f;
        isSunRise = true;
    }

    /**
     * This is sun's moving-equation
     * (y-3.1)²   (x-1.8)²
     * -------- + -------- = 1
     * 3.3*3.3      2*2
     *
     * @param degree x value in the equation
     * @return y value int the equation
     */
    public float func(float degree) {
        return -(float) Math.sqrt((1.00 - Math.pow(degree - 2.32, 2) / 7.29) * 5.9049) + 2.20f;
    }
}
