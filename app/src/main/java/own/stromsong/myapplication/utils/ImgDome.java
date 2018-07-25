package own.stromsong.myapplication.utils;

/**
 * Created by Administrator on 2018/5/23 0023.
 */

public class ImgDome {

    private String key; //分组id

    private String url; //展示路径 例如/xxx/index.html

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
    private DomeValu mDomeValu;

    public DomeValu getDomeValu() {
        return mDomeValu;
    }

    public void setDomeValu(DomeValu domeValu) {
        mDomeValu = domeValu;
    }

    public static class DomeValu{
        private int x;
        private int y;
        private int w;

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        public int getW() {
            return w;
        }

        public void setW(int w) {
            this.w = w;
        }

        public int getH() {
            return h;
        }

        public void setH(int h) {
            this.h = h;
        }

        public String getFile() {
            return file;
        }

        public void setFile(String file) {
            this.file = file;
        }

        private int h;
        private String file;

        public DomeValu(){}
        public DomeValu(int x,int y,int w,int h,String file){
            this.x=x;
            this.y=y;
            this.w=w;
            this.h=h;
            this.file=file;
        }

    }

}
