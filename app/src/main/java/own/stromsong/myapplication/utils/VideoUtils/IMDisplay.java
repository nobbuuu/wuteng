/*
 *
 * IMDisplay.java
 * 
 * Created by Wuwang on 2016/9/29
 * Copyright © 2016年 深圳哎吖科技. All rights reserved.
 */
package own.stromsong.myapplication.utils.VideoUtils;

import android.view.SurfaceHolder;
import android.view.View;

/**
 * Description:
 */
public interface IMDisplay extends IMPlayListener {

    View getDisplayView();
    SurfaceHolder getHolder();

}
