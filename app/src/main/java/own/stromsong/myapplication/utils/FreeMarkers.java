package own.stromsong.myapplication.utils; /**
 * .
 */

import android.util.Log;

import freemarker.template.Configuration;
import freemarker.template.Template;
import rx.exceptions.Exceptions;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * 描述：FreeMarkers工具类
 * 时间：2018/5/4 17:37
 * 邮箱：huangjunquan1109@163.com
 * 作者：godly.strong
 */
public class FreeMarkers {

    public static String renderString(String templateString, Map<String, ?> model) {
        try {
            StringWriter result = new StringWriter();
            Template t = new Template( "name", new StringReader( templateString ), new Configuration() );
            t.process( model, result );
            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String renderTemplate(Template template, Object model) {
        try {
            StringWriter result = new StringWriter();
            template.process( model, result );
            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
