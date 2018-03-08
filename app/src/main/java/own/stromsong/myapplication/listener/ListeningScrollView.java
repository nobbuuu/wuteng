package own.stromsong.myapplication.listener;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class ListeningScrollView extends ScrollView {

    private ScrollListener scrollListener;

    public ListeningScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (scrollListener != null) {
            scrollListener.onScrollChanged(l, t, oldl, oldt, computeVerticalScrollRange());
        }
    }

    public void setScrollViewListener(ScrollListener scrollListener) {
        this.scrollListener = scrollListener;
    }

    /**
     * 滑动监听器
     */
    public interface ScrollListener {
        void onScrollChanged(int x, int y, int oldx, int oldy, int computeVerticalScrollRange);
    }

}