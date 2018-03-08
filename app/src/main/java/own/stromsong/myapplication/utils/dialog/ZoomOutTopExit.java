package own.stromsong.myapplication.utils.dialog;

import android.animation.ObjectAnimator;
import android.view.View;


/**
 * Created by Administrator on 2017/8/18.
 */

public class ZoomOutTopExit extends BaseAnimatorSet {
    public ZoomOutTopExit() {
        duration = 600;
    }

    @Override
    public void setAnimation(View view) {
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int h = view.getMeasuredHeight();

        animatorSet.playTogether(//
                ObjectAnimator.ofFloat(view, "alpha", 1, 1, 0),//
                ObjectAnimator.ofFloat(view, "scaleX", 1, 0.475f, 0.1f),//
                ObjectAnimator.ofFloat(view, "scaleY", 1, 0.475f, 0.1f),//
                ObjectAnimator.ofFloat(view, "translationY", 0, 60, -h));
    }
}
