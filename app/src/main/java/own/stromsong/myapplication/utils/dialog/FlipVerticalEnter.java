package own.stromsong.myapplication.utils.dialog;

import android.animation.ObjectAnimator;
import android.view.View;

/**
 * Created by Administrator on 2017/8/18.
 */

public class FlipVerticalEnter extends BaseAnimatorSet {
    @Override
    public void setAnimation(View view) {
        animatorSet.playTogether(//
                // ObjectAnimator.ofFloat(view, "rotationX", -90, 0));
                ObjectAnimator.ofFloat(view, "rotationX", 90, 0));
    }
}
