package own.stromsong.myapplication.utils.dialog;

import android.animation.ObjectAnimator;
import android.view.View;


/**
 * Created by Administrator on 2017/8/18.
 */

public class NewsPaperEnter extends BaseAnimatorSet {
    @Override
    public void setAnimation(View view) {
        animatorSet.playTogether(//
                ObjectAnimator.ofFloat(view, "scaleX", 0.1f, 0.5f, 1f), //
                ObjectAnimator.ofFloat(view, "scaleY", 0.1f, 0.5f, 1f),//
                ObjectAnimator.ofFloat(view, "alpha", 0f, 1f),//
                ObjectAnimator.ofFloat(view, "rotation", 1080, 720, 360, 0));
    }
}
