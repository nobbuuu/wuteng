package own.stromsong.myapplication.utils.dialog;

import android.animation.ObjectAnimator;
import android.view.View;


/**
 * Created by Administrator on 2017/8/18.
 */

public class FadeExit extends BaseAnimatorSet {
    @Override
    public void setAnimation(View view) {
        animatorSet.playTogether(//
                ObjectAnimator.ofFloat(view, "alpha", 1, 0).setDuration(duration / 2));
    }
}
