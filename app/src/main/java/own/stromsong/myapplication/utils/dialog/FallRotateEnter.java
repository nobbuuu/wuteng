package own.stromsong.myapplication.utils.dialog;

import android.animation.ObjectAnimator;
import android.view.View;


/**
 * Created by Administrator on 2017/8/18.
 */

public class FallRotateEnter extends BaseAnimatorSet {
    @Override
    public void setAnimation(View view) {
        animatorSet.playTogether(ObjectAnimator.ofFloat(view, "scaleX", 2, 1.5f, 1),//
                ObjectAnimator.ofFloat(view, "scaleY", 2, 1.5f, 1),//
                ObjectAnimator.ofFloat(view, "rotation", 45, 0),//
                ObjectAnimator.ofFloat(view, "alpha", 0, 1));
    }
}
