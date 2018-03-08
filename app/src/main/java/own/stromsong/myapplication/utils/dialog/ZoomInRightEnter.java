package own.stromsong.myapplication.utils.dialog;

import android.animation.ObjectAnimator;
import android.view.View;


/**
 * Created by Administrator on 2017/8/18.
 */

public class ZoomInRightEnter extends BaseAnimatorSet {
    public ZoomInRightEnter() {
        duration = 750;
    }

    @Override
    public void setAnimation(View view) {
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int w = view.getMeasuredWidth();

        animatorSet.playTogether(//
                ObjectAnimator.ofFloat(view, "scaleX", 0.1f, 0.475f, 1),//
                ObjectAnimator.ofFloat(view, "scaleY", 0.1f, 0.475f, 1),//
                ObjectAnimator.ofFloat(view, "translationX", w, -48, 0),//
                ObjectAnimator.ofFloat(view, "alpha", 0, 1, 1));
    }
}
