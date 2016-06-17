package lumenghz.com.pullrefresh.refresh_view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import lumenghz.com.pullrefresh.PullToRefreshView;
import lumenghz.com.pullrefresh.R;
import lumenghz.com.pullrefresh.util.Utils;

/**
 * @author lumeng on 2016-06-16.
 *         jiahehz@gmail.com
 */
public class RocketRefreshView extends BaseRefreshView {
    private final static float SKY_RATIO           = 0.65f;
    private static final float SKY_INITIAL_SCALE   = 1.05f;
    private static final float SCALE_START_PERCENT = 0.5f;

    private static final float ROCKET_INITIAL_SCALE = 1.1f;
    private static final float ROCKET_FINAL_SCALE   = 0.8f;

    private Bitmap mFire1;
    private Bitmap mFire2;
    private Bitmap mFire3;
    private Bitmap mRocket;
    private Bitmap mSky;

    private Animation mFireBurnAnimation;
    private Animation mFireScaleAnimation;

    private PullToRefreshView mParent;

    private float mFireBurn;
    private float mFireScale;
    private float mPercent;
    private int   mScreenWidth;
    private int   mTop;

    /**
     * 火焰属性
     */
    private float mFire1LeftOffset;
    private float mFire2LeftOffset;
    private float mFire3LeftOffset;
    private float mFireTopOffset;
    private float mFireMoveOffset;

    private Context mContext;

    private Matrix mMatrix;

    /**
     * 天空属性
     */
    private int   mSkyHeight;
    private float mSkyTopOffset;
    private float mSkyMoveOffset;

    /**
     * 火箭属性
     */
    private float mRocketTopOffset;
    private float mRocketMoveOffset;

    private boolean isRefreshing = false;

    public RocketRefreshView(Context context, final PullToRefreshView layout) {
        super(context, layout);
        mParent = layout;
        mMatrix = new Matrix();
        mContext = getContext();
        setupAnimations();
        layout.post(new Runnable() {
            @Override
            public void run() {
                initialDimens(layout.getWidth());
            }
        });
    }

    private void initialDimens(int viewWidth) {
        if (viewWidth <= 0 || viewWidth == mScreenWidth) return;
        mScreenWidth = viewWidth;

        mSkyHeight = (int) (SKY_RATIO * mScreenWidth);
        mSkyTopOffset = mSkyHeight * 0.38f;
        mSkyMoveOffset = Utils.convertDpToPixel(getContext(), 15);

        mRocketTopOffset = mParent.getTotalDragDistance() * 0.1f;
        mRocketMoveOffset = Utils.convertDpToPixel(mContext, 20);

        mTop = -mParent.getTotalDragDistance();

        mFireTopOffset = mParent.getTotalDragDistance() * 0.6f;
        mFireMoveOffset = Utils.convertDpToPixel(mContext, 20);

        mFire1LeftOffset = (mScreenWidth / 100) * 47.5f;
        mFire2LeftOffset = (mScreenWidth / 100) * 51f;
        mFire3LeftOffset = (mScreenWidth / 100) * 53.5f;

        createBitmaps();
    }

    private void createBitmaps() {
        mFire1 = CreateBitmapFactory.getBitmapFromImage(R.drawable.fire1, mContext);
        mFire2 = CreateBitmapFactory.getBitmapFromImage(R.drawable.fire2, mContext);
        mFire3 = CreateBitmapFactory.getBitmapFromImage(R.drawable.fire3, mContext);
        mRocket = CreateBitmapFactory.getBitmapFromImage(R.drawable.rocket, mContext);
        mSky = CreateBitmapFactory.getBitmapFromImage(R.drawable.out_space, mContext);
        mSky = Bitmap.createScaledBitmap(mSky, mScreenWidth, mSkyHeight, true);
    }

    @Override
    public void draw(Canvas canvas) {
        if (mScreenWidth <= 0) return;

        final int saveCount = canvas.save();

        canvas.translate(0, mTop);
        canvas.clipRect(0, -mTop, mScreenWidth, mParent.getTotalDragDistance());

        drawSky(canvas);
        drawRocket(canvas);
        drawFire(canvas);

        canvas.restoreToCount(saveCount);
    }

    private void drawSky(Canvas canvas) {
        Matrix matrix = mMatrix;
        matrix.reset();

        float dragPercent = Math.min(1f, Math.abs(mPercent));
        float skyScale;
        float scalePercentDelta = dragPercent - SCALE_START_PERCENT;
        if (scalePercentDelta > 0) {
            /** Change skyScale between {@link #SKY_INITIAL_SCALE} and 1.0f depending on {@link #mPercent} */
            float scalePercent = scalePercentDelta / (1.0f - SCALE_START_PERCENT);
            skyScale = SKY_INITIAL_SCALE - (SKY_INITIAL_SCALE - 1.0f) * scalePercent;
        } else
            skyScale = SKY_INITIAL_SCALE;

        float offsetX = -(mScreenWidth * skyScale - mScreenWidth) / 2.0f;
        float offsetY = (1.0f - dragPercent) * mParent.getTotalDragDistance() - mSkyTopOffset
                - mSkyHeight * (skyScale - 1.0f) / 2
                + mSkyMoveOffset * dragPercent;

        matrix.postScale(skyScale, skyScale);
        matrix.postTranslate(offsetX, offsetY);
        canvas.drawBitmap(mSky, matrix, null);
    }

    private void drawRocket(Canvas canvas) {
        final Matrix matrix = mMatrix;
        matrix.reset();

        float dragPercent = Math.min(1f, Math.abs(mPercent));

        float rocketScale;
        float rocketMoveOffset;
        float scalePercentDelta = dragPercent - SCALE_START_PERCENT;

        float scalePercent = scalePercentDelta / (1.0f - SCALE_START_PERCENT);
        rocketScale = ROCKET_INITIAL_SCALE + (ROCKET_FINAL_SCALE - ROCKET_INITIAL_SCALE) * scalePercent;
        rocketMoveOffset = mRocketMoveOffset * scalePercent;
        matrix.preScale(rocketScale, rocketScale);

        final float offsetX = mScreenWidth / 2
                - mRocket.getWidth() / 2
                + (1f - rocketScale) * mRocket.getWidth() / 2;

        final float offsetY = mRocketTopOffset
                + (1.0f - dragPercent) * (mParent.getTotalDragDistance())
                + rocketMoveOffset
                - mTop;

        matrix.postTranslate(offsetX, offsetY);
        canvas.drawBitmap(mRocket, matrix, null);
    }

    private void drawFire(final Canvas canvas) {
        float dragPercent = Math.min(1f, Math.abs(mPercent));
        final float offsetY = mFireTopOffset
                + (1.0f - dragPercent) * (mParent.getTotalDragDistance())
                + mFireMoveOffset
                - mTop;

        drawSmallFire(canvas, mFire1, mFire1LeftOffset, offsetY, mFireScale, mFire2LeftOffset - Utils.convertDpToPixel(mContext, 15), mFireTopOffset + 50);
        drawSmallFire(canvas, mFire2, mFire2LeftOffset, offsetY, mFireBurn, mFire2LeftOffset - Utils.convertDpToPixel(mContext, 11), mFireTopOffset);
        drawSmallFire(canvas, mFire3, mFire3LeftOffset, offsetY, mFireScale, mFire3LeftOffset, mFireTopOffset + 50);
    }

    private void drawSmallFire(final Canvas canvas, Bitmap bitmap, float fireOffsetX, float fireOffsetY, float scaleY,
                               float pivotX, float pivotY) {
        final Matrix matrix = mMatrix;
        matrix.reset();

        matrix.postTranslate(fireOffsetX, fireOffsetY);
        float fireMinScale = 0.9f;
        matrix.postScale(Math.max(fireMinScale, scaleY), Math.max(fireMinScale, scaleY), pivotX, pivotY);
        Paint paint = new Paint();
        float alpha = (Math.max(0.5f, mFireScale)) * 255;
        paint.setAlpha((int) alpha);
        canvas.drawBitmap(bitmap, matrix, paint);
    }

    @Override
    public void setPercent(float percent, boolean invalidate) {
        setPercent(percent);
        if (invalidate) mRocketMoveOffset = setVariable(percent);
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
        mFireScaleAnimation.reset();
        mFireBurnAnimation.reset();
        isRefreshing = true;

        mParent.startAnimation(mFireScaleAnimation);

        mFireScaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mParent.startAnimation(mFireBurnAnimation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    public void stop() {
        mParent.clearAnimation();
        isRefreshing = false;
        resetOrigins();
    }

    @Override
    public void setBounds(int left, int top, int right, int bottom) {
        super.setBounds(left, top, right, mSkyHeight + top);
    }

    @Override
    public boolean isRunning() {
        return false;
    }

    private void setupAnimations() {
        AnimationFractory animationFractory = new AnimationFractory();
        mFireBurnAnimation = animationFractory.getFireBurn(new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                mFireBurn = setVariable(1f - interpolatedTime);
                mFireScale = setVariable(interpolatedTime);
            }
        });

        mFireScaleAnimation = animationFractory.getFireScale(new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                mFireScale = setVariable(interpolatedTime);
            }
        });
    }

    private float setVariable(float value) {
        invalidateSelf();
        return value;
    }

    private void resetOrigins() {
        setPercent(0);
        mFireScale = setVariable(0);
    }
}
