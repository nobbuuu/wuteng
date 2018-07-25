package own.stromsong.myapplication.weight;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import own.stromsong.myapplication.R;
import own.stromsong.myapplication.mvp.model.WeatherBean;

/**
 * Created by Administrator on 2018/6/26.
 */

public class WeatherView extends LinearLayout {
    @BindView(R.id.weather_icon)
    ImageView mWeatherIcon;
    @BindView(R.id.wd_tv)
    TextView mWdTv;
    @BindView(R.id.wstatus_tv)
    TextView mWstatusTv;
    @BindView(R.id.wind_tv)
    TextView mWindTv;

    public WeatherView(Context context) {
        super(context);
        View inflate = inflate(getContext(), R.layout.weather_lay, this);
        ButterKnife.bind(inflate);
    }

    public void setData(WeatherBean.QueryBean.ResultsBean.ChannelBean.ItemBean.ForecastBean forecastBean) {
        String low = forecastBean.getLow();
        String high = forecastBean.getHigh();
        String weatherType = forecastBean.getText();
        if (weatherType.contains("Cloudy")) {
            mWeatherIcon.setImageResource(R.mipmap.cloudy1);
        } else if (weatherType.contains("Thunderstorms")) {
            mWeatherIcon.setImageResource(R.mipmap.tstorm2);
        } else if (weatherType.contains("sunny")) {
            mWeatherIcon.setImageResource(R.mipmap.sunny);
        } else if (weatherType.contains("rain") || weatherType.contains("Showers")) {
            mWeatherIcon.setImageResource(R.mipmap.middle_rain);
        } else {
            mWeatherIcon.setImageResource(R.mipmap.cloudy4);
        }
        mWdTv.setText(low + "℃~" + high + "℃");
        mWstatusTv.setText(weatherType);
    }

    public WeatherView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
}
