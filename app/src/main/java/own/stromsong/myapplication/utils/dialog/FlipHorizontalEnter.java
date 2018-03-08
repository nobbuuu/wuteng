package own.stromsong.myapplication.utils.dialog;

import android.animation.ObjectAnimator;
import android.view.View;


/**
 * Created by Administrator on 2017/8/18.
 */

public class FlipHorizontalEnter extends BaseAnimatorSet {
    @Override
    public void setAnimation(View view) {
        animatorSet.playTogether(//
                // ObjectAnimator.ofFloat(view, "rotationY", -90, 0));
                ObjectAnimator.ofFloat(view, "rotationY", 90, 0));
    }
}
