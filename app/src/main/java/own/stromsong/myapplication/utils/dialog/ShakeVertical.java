package own.stromsong.myapplication.utils.dialog;

import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.CycleInterpolator;


/**
 * Created by Administrator on 2017/8/18.
 */

public class ShakeVertical extends BaseAnimatorSet {
    public ShakeVertical() {
        duration = 1000;
    }

    @Override
    public void setAnimation(View view) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationY", -10, 10);
        animator.setInterpolator(new CycleInterpolator(5));
        animatorSet.playTogether(animator);
    }
}
