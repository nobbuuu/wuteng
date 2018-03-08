package own.stromsong.myapplication.utils.dialog;

import android.animation.ObjectAnimator;
import android.view.View;


/**
 * Created by Administrator on 2017/8/18.
 */

public class ZoomInEnter extends BaseAnimatorSet {
    @Override
    public void setAnimation(View view) {
        animatorSet.playTogether(//
                ObjectAnimator.ofFloat(view, "scaleX", 0.5f, 1),//
                ObjectAnimator.ofFloat(view, "scaleY", 0.5f, 1),//
                ObjectAnimator.ofFloat(view, "alpha", 0, 1));//
    }
}
