package own.stromsong.myapplication.utils.dialog;

import android.animation.ObjectAnimator;
import android.util.DisplayMetrics;
import android.view.View;


/**
 * Created by Administrator on 2017/8/18.
 */

public class FlipBottomEnter extends BaseAnimatorSet {
    @Override
    public void setAnimation(View view) {
        DisplayMetrics dm = view.getContext().getResources().getDisplayMetrics();
        animatorSet.playTogether(//
                ObjectAnimator.ofFloat(view, "rotationX", -90, 0),//
                ObjectAnimator.ofFloat(view, "translationY", 200 * dm.density, 0), //
                ObjectAnimator.ofFloat(view, "alpha", 0.2f, 1));
    }
}
