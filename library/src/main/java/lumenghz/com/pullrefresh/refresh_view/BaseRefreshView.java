package lumenghz.com.pullrefresh.refresh_view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Build;

import lumenghz.com.pullrefresh.PullToRefreshView;
import lumenghz.com.pullrefresh.util.Utils;

/**
 * @author lumeng on 2016-06-16.
 *         jiahehz@gmail.com
 */
public abstract class BaseRefreshView extends Drawable implements Drawable.Callback, Animatable {

    private PullToRefreshView mRefreshLayout;

    public BaseRefreshView(Context context, PullToRefreshView mRefreshLayout) {
        this.mRefreshLayout = mRefreshLayout;
    }

    protected Context getContext() {
        return mRefreshLayout == null ? null : mRefreshLayout.getContext();
    }

    public abstract void setPercent(float percent, boolean invalidate);

    public abstract void offsetTopAndBottom(int offset);

    protected abstract void initialDimens(int viewWidth);

    protected abstract void setupAnimations();

    protected int getPixel(int dp) {
        return Utils.convertDpToPixel(getContext(), dp);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void invalidateDrawable(Drawable who) {
        final Callback callback = getCallback();
        if (null != callback)
            callback.invalidateDrawable(who);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void scheduleDrawable(Drawable who, Runnable what, long when) {
        final Callback callback = getCallback();
        if (null != callback)
            callback.scheduleDrawable(who, what, when);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void unscheduleDrawable(Drawable who, Runnable what) {
        final Callback callback = getCallback();
        if (null != callback)
            callback.unscheduleDrawable(who, what);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public void draw(Canvas canvas) {
    }

    @Override
    public void setAlpha(int alpha) {
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
    }
}
