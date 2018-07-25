package own.stromsong.myapplication.mvp.view.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import own.stromsong.myapplication.R;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class AndroidPdfViewActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * Key string for saving the state of current page index.
     */
    private static final String STATE_CURRENT_PAGE_INDEX = "current_page_index";

    /**
     * The filename of the PDF.
     */
    private static final String FILENAME = "sample.pdf";


    /**
     * File descriptor of the PDF.
     */
    private ParcelFileDescriptor mFileDescriptor;

    /**
     * Page that is currently shown on the screen.
     */
    private PdfRenderer.Page mCurrentPage;

    /**
     * PDF page index
     */
    private int mPageIndex;

    /**
     * {@link android.graphics.pdf.PdfRenderer} to render the PDF.
     */
    private PdfRenderer mPdfRenderer;
    private ImageView mImageView;
    private Button btnPrevious;
    private Button btnNext;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (null != mCurrentPage) {
            outState.putInt(STATE_CURRENT_PAGE_INDEX, mCurrentPage.getIndex());
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_pdf_view);
        mImageView = (ImageView) findViewById(R.id.iv);
        btnPrevious = (Button) findViewById(R.id.btn_previous);
        btnNext = (Button) findViewById(R.id.btn_next);

        btnPrevious.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        mPageIndex = 0;
        // If there is a savedInstanceState (screen orientations, etc.), we restore the page index.
        if (null != savedInstanceState) {
            mPageIndex = savedInstanceState.getInt(STATE_CURRENT_PAGE_INDEX, 0);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            openRenderer(this);
            showPage(mPageIndex);
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error! " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Sets up a {@link android.graphics.pdf.PdfRenderer} and related resources.
     */
    private void openRenderer(Context context) throws IOException {
        // In this sample, we read a PDF from the assets directory.
        File file = new File(context.getCacheDir(), FILENAME);
        if (!file.exists()) {
            // Since PdfRenderer cannot handle the compressed asset file directly, we copy it into
            // the cache directory.
            InputStream asset = getAssets().open(FILENAME);
            FileOutputStream output = new FileOutputStream(file);
            final byte[] buffer = new byte[1024];
            int size;
            while ((size = asset.read(buffer)) != -1) {
                output.write(buffer, 0, size);
            }
            asset.close();
            output.close();
        }
        mFileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
        // This is the PdfRenderer we use to render the PDF.
        if (mFileDescriptor != null) {
            mPdfRenderer = new PdfRenderer(mFileDescriptor);
        }
    }

    /**
     * Shows the specified page of PDF to the screen.
     *
     * @param index The page index.
     */
    private void showPage(int index) {
        if (mPdfRenderer.getPageCount() <= index) {
            return;
        }
        // Make sure to close the current page before opening another one.
        if (null != mCurrentPage) {
            mCurrentPage.close();
        }
        // Use `openPage` to open a specific page in PDF.
        mCurrentPage = mPdfRenderer.openPage(index);
        // Important: the destination bitmap must be ARGB (not RGB).
        Bitmap bitmap = Bitmap.createBitmap(mCurrentPage.getWidth(), mCurrentPage.getHeight(), Bitmap.Config.ARGB_8888);
        // Here, we render the page onto the Bitmap.
        // To render a portion of the page, use the second and third parameter. Pass nulls to get
        // the default result.
        // Pass either RENDER_MODE_FOR_DISPLAY or RENDER_MODE_FOR_PRINT for the last parameter.
        mCurrentPage.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
        // We are ready to show the Bitmap to user.
        mImageView.setImageBitmap(bitmap);
        updateUi();
    }

    /**
     * Closes the {@link android.graphics.pdf.PdfRenderer} and related resources.
     *
     * @throws java.io.IOException When the PDF file cannot be closed.
     */
    private void closeRenderer() throws IOException {
        if (null != mCurrentPage) {
            mCurrentPage.close();
        }
        mPdfRenderer.close();
        mFileDescriptor.close();
    }

    @Override
    protected void onDestroy() {
       /* try {
            closeRenderer();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        super.onDestroy();
    }

    @Override
    protected void onPause() {
      /*  try {
            closeRenderer();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        super.onPause();
    }

    /**
     * Updates the state of 2 control buttons in response to the current page index.
     */
    private void updateUi() {
        int index = mCurrentPage.getIndex();
        int pageCount = mPdfRenderer.getPageCount();
        btnPrevious.setEnabled(0 != index);
        btnNext.setEnabled(index + 1 < pageCount);
//        setTitle(getString(R.string.app_name_with_index, index + 1, pageCount));
    }

    /**
     * Gets the number of pages in the PDF. This method is marked as public for testing.
     *
     * @return The number of pages.
     */
    public int getPageCount() {
        return mPdfRenderer.getPageCount();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_previous: {
                // Move to the previous page
                showPage(mCurrentPage.getIndex() - 1);
                break;
            }
            case R.id.btn_next: {
                // Move to the next page
                showPage(mCurrentPage.getIndex() + 1);
                break;
            }
        }
    }
}