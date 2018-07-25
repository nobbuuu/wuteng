package own.stromsong.myapplication.mvp.model;

/**
 * Created by Administrator on 2018/5/23 0023.
 */

public class WeatherValueBean {

    /**
     * value : {"query":{"count":1,"created":"2018-05-23T12:05:23Z","lang":"en-US","results":{"channel":{"units":{"distance":"km","pressure":"mb","speed":"km/h","temperature":"C"},"title":"Yahoo! Weather - Shenzhen, Guangdong, CN","link":"http://us.rd.yahoo.com/dailynews/rss/weather/Country__Country/*https://weather.yahoo.com/country/state/city-2161853/","description":"Yahoo! Weather for Shenzhen, Guangdong, CN","language":"en-us","lastBuildDate":"Wed, 23 May 2018 08:05 PM CST","ttl":"60","location":{"city":"Shenzhen","country":"China","region":" Guangdong"},"wind":{"chill":"88","direction":"200","speed":"11.27"},"atmosphere":{"humidity":"66","pressure":"33762.31","rising":"0","visibility":"25.91"},"astronomy":{"sunrise":"5:40 am","sunset":"7:0 pm"},"image":{"title":"Yahoo! Weather","width":"142","height":"18","link":"http://weather.yahoo.com","url":"http://l.yimg.com/a/i/brand/purplelogo//uh/us/news-wea.gif"},"item":{"title":"Conditions for Shenzhen, Guangdong, CN at 07:00 PM CST","lat":"22.546789","long":"114.112556","link":"http://us.rd.yahoo.com/dailynews/rss/weather/Country__Country/*https://weather.yahoo.com/country/state/city-2161853/","pubDate":"Wed, 23 May 2018 07:00 PM CST","condition":{"code":"32","date":"Wed, 23 May 2018 07:00 PM CST","temp":"31","text":"Sunny"},"forecast":[{"code":"32","date":"23 May 2018","day":"Wed","high":"35","low":"25","text":"Sunny"},{"code":"4","date":"24 May 2018","day":"Thu","high":"32","low":"26","text":"Thunderstorms"},{"code":"4","date":"25 May 2018","day":"Fri","high":"33","low":"26","text":"Thunderstorms"},{"code":"4","date":"26 May 2018","day":"Sat","high":"34","low":"27","text":"Thunderstorms"},{"code":"4","date":"27 May 2018","day":"Sun","high":"34","low":"27","text":"Thunderstorms"},{"code":"4","date":"28 May 2018","day":"Mon","high":"33","low":"27","text":"Thunderstorms"},{"code":"4","date":"29 May 2018","day":"Tue","high":"33","low":"27","text":"Thunderstorms"},{"code":"4","date":"30 May 2018","day":"Wed","high":"32","low":"26","text":"Thunderstorms"},{"code":"4","date":"31 May 2018","day":"Thu","high":"30","low":"26","text":"Thunderstorms"},{"code":"4","date":"01 Jun 2018","day":"Fri","high":"30","low":"26","text":"Thunderstorms"}],"description":"<![CDATA[<img src=\"http://l.yimg.com/a/i/us/we/52/32.gif\"/>\n<BR />\n<b>Current Conditions:</b>\n<BR />Sunny\n<BR />\n<BR />\n<b>Forecast:</b>\n<BR /> Wed - Sunny. High: 35Low: 25\n<BR /> Thu - Thunderstorms. High: 32Low: 26\n<BR /> Fri - Thunderstorms. High: 33Low: 26\n<BR /> Sat - Thunderstorms. High: 34Low: 27\n<BR /> Sun - Thunderstorms. High: 34Low: 27\n<BR />\n<BR />\n<a href=\"http://us.rd.yahoo.com/dailynews/rss/weather/Country__Country/*https://weather.yahoo.com/country/state/city-2161853/\">Full Forecast at Yahoo! Weather</a>\n<BR />\n<BR />\n<BR />\n]]>","guid":{"isPermaLink":"false"}}}}}}
     */

    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
