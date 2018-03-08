package own.stromsong.myapplication.utils.dialog;

import android.animation.ObjectAnimator;
import android.util.DisplayMetrics;
import android.view.View;


/**
 * Created by Administrator on 2017/8/18.
 */

public class SlideLeftExit extends BaseAnimatorSet {
    @Override
    public void setAnimation(View view) {
        DisplayMetrics dm = view.getContext().getResources().getDisplayMetrics();
        animatorSet.playTogether(//
                ObjectAnimator.ofFloat(view, "translationX", 0, -250 * dm.density), //
                ObjectAnimator.ofFloat(view, "alpha", 1, 0));
    }
}
