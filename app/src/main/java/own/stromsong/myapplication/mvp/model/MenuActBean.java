package own.stromsong.myapplication.mvp.model;

import java.util.List;

/**
 * Created by Administrator on 2018/3/20 0020.
 */

public class MenuActBean {

    private List<ShowBean> show;

    public List<ShowBean> getShow() {
        return show;
    }

    public void setShow(List<ShowBean> show) {
        this.show = show;
    }

    public static class ShowBean {
        /**
         * shows : {"id":"275cac05943e42a284cbcc970b79a27f","isNewRecord":false,"createDate":"2018-02-05 11:08:45","updateDate":"2018-02-05 14:51:10","name":"节目三安1","resolutionId":"6c17f3daa42a41e1838bc5b4186dac53","wide":500,"hige":300,"playTime":25,"resolution":{"isNewRecord":true,"screenType":1,"wide":1000,"high":600}}
         * material :
         */

        private ShowsBean shows;
        private String material;

        public ShowsBean getShows() {
            return shows;
        }

        public void setShows(ShowsBean shows) {
            this.shows = shows;
        }

        public String getMaterial() {
            return material;
        }

        public void setMaterial(String material) {
            this.material = material;
        }

        public static class ShowsBean {
            /**
             * id : 275cac05943e42a284cbcc970b79a27f
             * isNewRecord : false
             * createDate : 2018-02-05 11:08:45
             * updateDate : 2018-02-05 14:51:10
             * name : 节目三安1
             * resolutionId : 6c17f3daa42a41e1838bc5b4186dac53
             * wide : 500
             * hige : 300
             * playTime : 25
             * resolution : {"isNewRecord":true,"screenType":1,"wide":1000,"high":600}
             */

            private String id;
            private boolean isNewRecord;
            private String createDate;
            private String updateDate;
            private String name;
            private String resolutionId;
            private int wide;
            private int hige;
            private int playTime;
            private ResolutionBean resolution;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public boolean isIsNewRecord() {
                return isNewRecord;
            }

            public void setIsNewRecord(boolean isNewRecord) {
                this.isNewRecord = isNewRecord;
            }

            public String getCreateDate() {
                return createDate;
            }

            public void setCreateDate(String createDate) {
                this.createDate = createDate;
            }

            public String getUpdateDate() {
                return updateDate;
            }

            public void setUpdateDate(String updateDate) {
                this.updateDate = updateDate;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getResolutionId() {
                return resolutionId;
            }

            public void setResolutionId(String resolutionId) {
                this.resolutionId = resolutionId;
            }

            public int getWide() {
                return wide;
            }

            public void setWide(int wide) {
                this.wide = wide;
            }

            public int getHige() {
                return hige;
            }

            public void setHige(int hige) {
                this.hige = hige;
            }

            public int getPlayTime() {
                return playTime;
            }

            public void setPlayTime(int playTime) {
                this.playTime = playTime;
            }

            public ResolutionBean getResolution() {
                return resolution;
            }

            public void setResolution(ResolutionBean resolution) {
                this.resolution = resolution;
            }

            public static class ResolutionBean {
                /**
                 * isNewRecord : true
                 * screenType : 1
                 * wide : 1000
                 * high : 600
                 */

                private boolean isNewRecord;
                private int screenType;
                private int wide;
                private int high;

                public boolean isIsNewRecord() {
                    return isNewRecord;
                }

                public void setIsNewRecord(boolean isNewRecord) {
                    this.isNewRecord = isNewRecord;
                }

                public int getScreenType() {
                    return screenType;
                }

                public void setScreenType(int screenType) {
                    this.screenType = screenType;
                }

                public int getWide() {
                    return wide;
                }

                public void setWide(int wide) {
                    this.wide = wide;
                }

                public int getHigh() {
                    return high;
                }

                public void setHigh(int high) {
                    this.high = high;
                }
            }
        }
    }
}
