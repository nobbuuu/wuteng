package own.stromsong.myapplication.utils.dialog;


/**
 * Created by Administrator on 2017/5/11.
 */

public class ApplicationSetting {
    //dialog进出动画设置
    public static BaseAnimatorSet showAnim(final int position) {
        final Class<?> cs[] = {BounceEnter.class,//
                BounceTopEnter.class,//
                BounceBottomEnter.class,//
                BounceLeftEnter.class,//
                BounceRightEnter.class,//
                FlipHorizontalEnter.class,//
                FlipHorizontalSwingEnter.class,//
                FlipVerticalEnter.class,//
                FlipVerticalSwingEnter.class,//
                FlipTopEnter.class,//
                FlipBottomEnter.class,//
                FlipLeftEnter.class,//
                FlipRightEnter.class,//
                FadeEnter.class, //
                FallEnter.class,//
                FallRotateEnter.class,//
                SlideTopEnter.class,//
                SlideBottomEnter.class,//
                SlideLeftEnter.class, //
                SlideRightEnter.class,//
                ZoomInEnter.class,//
                ZoomInTopEnter.class,//
                ZoomInBottomEnter.class,//
                ZoomInLeftEnter.class,//
                ZoomInRightEnter.class,//

                NewsPaperEnter.class,//
                Flash.class,//
                ShakeHorizontal.class,//
                ShakeVertical.class,//
                Jelly.class,//
                RubberBand.class,//
                Swing.class,//
                Tada.class,//
        };
        try {
            return (BaseAnimatorSet) cs[position].newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new BounceTopEnter();
    }

    public static BaseAnimatorSet dissmissAnim(final int position) {
        final Class<?> cs[] = {FlipHorizontalExit.class,//
                FlipVerticalExit.class,//
                FadeExit.class,//
                SlideTopExit.class,//
                SlideBottomExit.class,//
                SlideLeftExit.class, //
                SlideRightExit.class,//
                ZoomOutExit.class,//
                ZoomOutTopExit.class,//
                ZoomOutBottomExit.class,//
                ZoomOutLeftExit.class,//
                ZoomOutRightExit.class,//
                ZoomInExit.class,//
        };
        try {
            return (BaseAnimatorSet) cs[position].newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new SlideBottomExit();
    }
}
