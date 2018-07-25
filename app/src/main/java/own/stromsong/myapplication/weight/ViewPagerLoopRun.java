package own.stromsong.myapplication.weight;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.support.annotation.RequiresApi;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.ToastUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import own.stromsong.myapplication.R;
import own.stromsong.myapplication.mvp.adapter.PdfPagerAdapter;

/**
 * 无限 循环 滚动 viewPager
 * Created by Administrator on 2017/6/9.
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class ViewPagerLoopRun extends RelativeLayout implements ViewPager.OnPageChangeListener, View.OnTouchListener {

    private ViewPager viewPager;
    private boolean borderTag = false;
    private int borderJumpIndex = 1;
    private boolean viewPagerTouchTag = false;
    private RunHandler runHandler;
    private Thread loopThread;

    private int pageCount;
    private String filePath;
    private ParcelFileDescriptor mFileDescriptor;
    private PdfRenderer mPdfRenderer;

    public ViewPagerLoopRun(Context context,String filePath) {
        super(context);
        this.filePath = filePath;
        init(context);
        setData();
    }

    public ViewPagerLoopRun(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
        setData();
    }

    private void setData() {
        File file = new File(filePath);
        if (file.exists()) {
            try {
                mFileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
                // This is the PdfRenderer we use to render the PDF.
                if (mFileDescriptor != null) {
                    mPdfRenderer = new PdfRenderer(mFileDescriptor);
                    pageCount = mPdfRenderer.getPageCount();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (mPdfRenderer != null && pageCount != 0) {
            PdfRenderer.Page mCurrentPage = null;
            List<Bitmap> views = new ArrayList<>();
            for (int i = 0; i < pageCount; i++) {
                if (mCurrentPage != null) {
                    mCurrentPage.close();
                }
                //应用程序已获得内存中未使用内存
                long freeMemory = ((int) Runtime.getRuntime().freeMemory())/1024/1024;
                Log.e("tag","freeMemory="+freeMemory);
               if (freeMemory>=2){
                   try {
                       mCurrentPage = mPdfRenderer.openPage(i);
                       Bitmap bitmap = Bitmap.createBitmap(mCurrentPage.getWidth(), mCurrentPage.getHeight(), Bitmap.Config.ARGB_8888);
                       mCurrentPage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
                       // We are ready to show the Bitmap to user.
                       views.add(bitmap);
                   } catch (Exception e) {
//                    ToastUtils.showLong("please upload normal small file");
                       e.printStackTrace();
                   }
               }else {
                   ToastUtils.showLong("上传的文档过大");
                   Log.e("tag","上传的文档过大");
                   break;
               }
            }
            if (views.size() > 0) {
                views.add(0, views.get(views.size() - 1));
                views.add(views.size() - 1, views.get(1));
            }
            Log.e("tag", "pdfPages=" + views.size());
            if (views.size()>0){
                setAdapter(new PdfPagerAdapter(views));
                startLoop();
            }
        }
    }

    private void init(Context context) {
        LayoutInflater.from(getContext()).inflate(R.layout.view_viewpager_loop_run_layout, this);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.addOnPageChangeListener(this);
        viewPager.setOnTouchListener(this);
        runHandler = new RunHandler();
    }

    public void setAdapter(PagerAdapter pagerAdapter) {
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(1);
    }

    public void startLoop() {
        if (loopThread != null) {
            loopThread.interrupt();
        }
        loopThread = new Thread() {
            @Override
            public void run() {
                while (true) {
                    try {
                        if (!viewPagerTouchTag) {
                            Thread.sleep(3000);
                            sendLoopMessage();
                        } else {
                            Thread.sleep(5000);
                            viewPagerTouchTag = false;
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }


        };
        loopThread.start();
    }

    private void sendLoopMessage() {
        runHandler.sendEmptyMessage(1);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position == 0) {
            borderTag = true;
            borderJumpIndex = viewPager.getAdapter().getCount() - 2;
        } else if (position == viewPager.getAdapter().getCount() - 1) {
            borderTag = true;
            borderJumpIndex = 1;
        } else if (position < viewPager.getAdapter().getCount() - 1 && position > 0) {
            borderTag = false;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (borderTag) {
            viewPager.setCurrentItem(borderJumpIndex, false);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        viewPagerTouchTag = true;
        return false;
    }

    class RunHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            if (viewPagerTouchTag) {
                return;
            }
            int currentItem = viewPager.getCurrentItem();
            if (currentItem < viewPager.getAdapter().getCount() - 1) {
                currentItem++;
            } else {
                currentItem = 0;
            }
            viewPager.setCurrentItem(currentItem);
        }
    }
}
