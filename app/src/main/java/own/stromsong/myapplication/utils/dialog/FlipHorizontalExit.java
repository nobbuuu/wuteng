package own.stromsong.myapplication.utils.dialog;

import android.animation.ObjectAnimator;
import android.view.View;


/**
 * Created by Administrator on 2017/8/18.
 */

public class FlipHorizontalExit extends BaseAnimatorSet {
    @Override
    public void setAnimation(View view) {
        animatorSet.playTogether(ObjectAnimator.ofFloat(view, "rotationY", 0, 90),//
                ObjectAnimator.ofFloat(view, "alpha", 1, 0));
    }
}
