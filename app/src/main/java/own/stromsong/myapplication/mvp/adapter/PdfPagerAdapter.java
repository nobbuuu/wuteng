package own.stromsong.myapplication.mvp.adapter;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;

import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.List;

import own.stromsong.myapplication.R;
import own.stromsong.myapplication.app.MyApplication;

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class PdfPagerAdapter extends PagerAdapter {
    private List<Bitmap> mViewList = new ArrayList<>();
    public PdfPagerAdapter (List<Bitmap> views){
        mViewList.addAll(views);
    }
    @Override
    public int getCount() {
        return mViewList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView v = new ImageView(MyApplication.getInstance());
        v.setImageBitmap(mViewList.get(position));
        container.addView(v);
        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ImageView)object);
    }

}