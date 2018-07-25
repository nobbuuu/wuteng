package own.stromsong.myapplication.utils;

import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by Administrator on 2018/5/15 0015.
 */

public class XmlUtils {
    private static DocumentBuilderFactory dbFactory = null;
    private static DocumentBuilder db = null;
    private static Document document = null;

    static {
        try {
            dbFactory = DocumentBuilderFactory.newInstance();
            db = dbFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    public static String getWoeid(String xmlStr) {
        String weoid = "";
        if (StringUtil.NoNullOrEmpty(xmlStr) && xmlStr.contains("woeid")) {
            String aa = xmlStr.substring(xmlStr.indexOf("woeid"), xmlStr.length() - 1);
            String[] a = aa.split("&amp;");
            if (a.length > 1) {
                weoid = a[0].substring(1, a[0].length());
                Log.e("tag", "weoid=" + weoid);
            }
            Log.e("tag", "a=" + a.toString());
        }
        return weoid;
//
//        //将给定 URI 的内容解析为一个 XML 文档,并返回Document对象
//        try {
//            document = db.parse(getStringStream(xmlStr));
//            //按文档顺序返回包含在文档中且具有给定标记名称的所有 Element 的 NodeList
//            NodeList bookList = document.getElementsByTagName("s");
//            if (bookList != null && bookList.getLength() > 0) {
//                //获取第i个book结点
//                org.w3c.dom.Node node = bookList.item(0);
//                //获取第i个book的所有属性
//                NamedNodeMap namedNodeMap = node.getAttributes();
//                //获取已知名为id的属性值
//                String md = namedNodeMap.getNamedItem("d").getTextContent();
////                namedNodeMap.getNamedItem("d").
//                String weoid = namedNodeMap.getNamedItem("d").getAttributes().getNamedItem("woeid").getTextContent();
//                Log.e("tag", "weoid=" + weoid);
//                return weoid;
//            }
//        } catch (SAXException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return "";
    }

    public static InputStream getStringStream(String sInputString) throws UnsupportedEncodingException {
        ByteArrayInputStream tInputStringStream = null;
        if (sInputString != null && !sInputString.trim().equals("")) {
            tInputStringStream = new ByteArrayInputStream(sInputString.getBytes("UTF-8"));
        }
        return tInputStringStream;
    }
}
