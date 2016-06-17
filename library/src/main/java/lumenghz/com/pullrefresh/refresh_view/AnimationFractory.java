package lumenghz.com.pullrefresh.refresh_view;

import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Interpolator;

/**
 * @author lumeng on 2016-06-16.
 *         jiahehz@gmail.com
 */
public class AnimationFractory {
    private static final int ANIMATION_FIRE_BURN_DURATION  = 180;
    private static final int ANIMATION_FIRE_SCALE_DURATION = 100;

    private static final Interpolator ACCELERATE_INTERPOLATOR = new AccelerateInterpolator();
    private static final Interpolator DECELERATE_INTERPOLATOR = new AccelerateDecelerateInterpolator();

    Animation getFireScale(Animation animation) {
        configureAnimation(animation,
                ACCELERATE_INTERPOLATOR,
                ANIMATION_FIRE_SCALE_DURATION,
                0,
                0,
                0);

        return animation;
    }

    Animation getFireBurn(Animation animation) {
        configureAnimation(animation,
                DECELERATE_INTERPOLATOR,
                ANIMATION_FIRE_BURN_DURATION,
                0,
                Animation.REVERSE,
                Animation.INFINITE);

        return animation;
    }

    private void configureAnimation(Animation animation, Interpolator interpolator, int duration, int startOffset, int repeatMode, int repeatCount) {
        animation.setInterpolator(interpolator);
        animation.setDuration(duration);
        animation.setStartOffset(startOffset);
        animation.setRepeatMode(repeatMode);
        animation.setRepeatCount(repeatCount);
    }
}
