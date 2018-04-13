package own.stromsong.myapplication.mvp.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/3/19 0019.
 */

public class MenuBean implements Serializable{

    private List<ListResultBean> listResult;
    private List<SubtitlesBean> subtitles;

    public List<ListResultBean> getListResult() {
        return listResult;
    }

    public void setListResult(List<ListResultBean> listResult) {
        this.listResult = listResult;
    }

    public List<SubtitlesBean> getSubtitles() {
        return subtitles;
    }

    public static class SubtitlesBean implements Serializable{
        /**
         * id : 2146ab8c8e99435cb57828ba1360fac5
         * isNewRecord : false
         * createDate : 2018-01-25 17:15:18
         * updateDate : 2018-01-25 17:50:43
         * name : RSS
         * location : 0
         * fontSize : 30
         * msgColor : #ff0000
         * bgColor : #ff0000
         * startDate : 2018-01-25 00:00:00
         * endDate : 2018-01-31 00:00:00
         * equipmentId : 1abad3de4e084064b9f5d6217cfa154f,7e562d9cca834a6e8711b6d50bc3b917
         * rss : 12156165
         * type : 3
         * status : 0
         */

        private String id;
        private boolean isNewRecord;
        private String createDate;
        private String updateDate;
        private String name;
        private int location;
        private String fontSize;
        private String msgColor;
        private String bgColor;
        private String msgContext;
        private long startDate;
        private long endDate;
        private String equipmentId;
        private String rss;
        private int type;
        private String status;
        private String file;

        public String getFile() {
            return file;
        }

        public void setFile(String file) {
            this.file = file;
        }

        public String getMsgContext() {
            return msgContext;
        }

        public void setMsgContext(String msgContext) {
            this.msgContext = msgContext;
        }

        public boolean isNewRecord() {
            return isNewRecord;
        }

        public void setNewRecord(boolean newRecord) {
            isNewRecord = newRecord;
        }

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

        public int getLocation() {
            return location;
        }

        public void setLocation(int location) {
            this.location = location;
        }

        public String getFontSize() {
            return fontSize;
        }

        public void setFontSize(String fontSize) {
            this.fontSize = fontSize;
        }

        public String getMsgColor() {
            return msgColor;
        }

        public void setMsgColor(String msgColor) {
            this.msgColor = msgColor;
        }

        public String getBgColor() {
            return bgColor;
        }

        public void setBgColor(String bgColor) {
            this.bgColor = bgColor;
        }

        public long getStartDate() {
            return startDate;
        }

        public void setStartDate(long startDate) {
            this.startDate = startDate;
        }

        public long getEndDate() {
            return endDate;
        }

        public void setEndDate(long endDate) {
            this.endDate = endDate;
        }

        public String getEquipmentId() {
            return equipmentId;
        }

        public void setEquipmentId(String equipmentId) {
            this.equipmentId = equipmentId;
        }

        public String getRss() {
            return rss;
        }

        public void setRss(String rss) {
            this.rss = rss;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }

    public static class ListResultBean implements Serializable {
        /**
         * showMenu : {"id":"6e9c683e0a054c80af4810ed03a97527","isNewRecord":false,"createDate":"2018-02-05 15:53:12","updateDate":"2018-02-05 15:53:12","showId":"275cac05943e42a284cbcc970b79a27f,7feecaa2e02747aead433f63c062d7d0,"}
         * listshow : [{"shows":{"id":"275cac05943e42a284cbcc970b79a27f","isNewRecord":false,"createDate":"2018-02-05 11:08:45","updateDate":"2018-02-05 14:51:10","name":"节目三安1","resolutionId":"6c17f3daa42a41e1838bc5b4186dac53","wide":500,"hige":300,"playTime":25,"resolution":{"isNewRecord":true,"screenType":1,"wide":1000,"high":600}},"listMaterial":[]},{"shows":{"id":"7feecaa2e02747aead433f63c062d7d0","isNewRecord":false,"createDate":"2018-02-05 14:12:03","updateDate":"2018-02-05 14:12:03","wide":500,"hige":300,"playTime":15},"listMaterial":[{"Material":{"id":"8b3af6141f9d42e18b2151524c073490","isNewRecord":false,"createDate":"2018-01-29 15:49:20","updateDate":"2018-01-29 17:26:14","type":2,"groupId":"173437fc93a249a58a0022b5452709c0","file":"http://127.0.0.1:8080/download/2018-01/material/79b354f4-6820-418d-a508-be1ab48cc145.mp3","name":"BMW","size":"24","groups":{"id":"173437fc93a249a58a0022b5452709c0","isNewRecord":false,"name":"建材"}}}]}]
         */

        private long startTime;
        private long endTime;
        private ShowMenuBean showMenu;
        private List<ListshowBean> listshow;

        public long getEndTime() {
            return endTime;
        }

        public void setEndTime(long endTime) {
            this.endTime = endTime;
        }

        public long getStartTime() {
            return startTime;
        }

        public void setStartTime(long startTime) {
            this.startTime = startTime;
        }

        public ShowMenuBean getShowMenu() {
            return showMenu;
        }

        public void setShowMenu(ShowMenuBean showMenu) {
            this.showMenu = showMenu;
        }

        public List<ListshowBean> getListshow() {
            return listshow;
        }

        public void setListshow(List<ListshowBean> listshow) {
            this.listshow = listshow;
        }

        public static class ShowMenuBean  implements Serializable{
            /**
             * id : 6e9c683e0a054c80af4810ed03a97527
             * isNewRecord : false
             * createDate : 2018-02-05 15:53:12
             * updateDate : 2018-02-05 15:53:12
             * showId : 275cac05943e42a284cbcc970b79a27f,7feecaa2e02747aead433f63c062d7d0,
             */

            private String id;
            private boolean isNewRecord;
            private String createDate;
            private String updateDate;
            private String showId;

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

            public String getShowId() {
                return showId;
            }

            public void setShowId(String showId) {
                this.showId = showId;
            }
        }

        public static class ListshowBean implements Serializable{
            /**
             * shows : {"id":"275cac05943e42a284cbcc970b79a27f","isNewRecord":false,"createDate":"2018-02-05 11:08:45","updateDate":"2018-02-05 14:51:10","name":"节目三安1","resolutionId":"6c17f3daa42a41e1838bc5b4186dac53","wide":500,"hige":300,"playTime":25,"resolution":{"isNewRecord":true,"screenType":1,"wide":1000,"high":600}}
             * listMaterial : []
             */

            private ShowsBean shows;
            private List<Material> listMaterial;

            public ShowsBean getShows() {
                return shows;
            }

            public void setShows(ShowsBean shows) {
                this.shows = shows;
            }

            public List<Material> getListMaterial() {
                return listMaterial;
            }

            public void setListMaterial(List<Material> listMaterial) {
                this.listMaterial = listMaterial;
            }

            public static class Material implements Serializable{

                /**
                 * showsMaterial : {"id":"3f3e5f49288b45e6a4c041a228d7daeb","isNewRecord":false,"createDate":"2018-02-05 14:12:14","updateDate":"2018-02-05 14:12:14","showsId":"7feecaa2e02747aead433f63c062d7d0","materialId":"8b3af6141f9d42e18b2151524c073490","resolutionId":"6c17f3daa42a41e1838bc5b4186dac53","x":"10","y":"30","wide":500,"hige":300,"showType":1,"playTime":15,"sort":1}
                 * Material : {"id":"8b3af6141f9d42e18b2151524c073490","isNewRecord":false,"createDate":"2018-01-29 15:49:20","updateDate":"2018-01-29 17:26:14","type":2,"groupId":"173437fc93a249a58a0022b5452709c0","file":"http://127.0.0.1:8080/download/2018-01/material/79b354f4-6820-418d-a508-be1ab48cc145.mp3","name":"BMW","size":"24","groups":{"id":"173437fc93a249a58a0022b5452709c0","isNewRecord":false,"name":"建材"}}
                 */

                private ShowsMaterialBean showsMaterial;
                private MaterialBean Material;

                public ShowsMaterialBean getShowsMaterial() {
                    return showsMaterial;
                }

                public void setShowsMaterial(ShowsMaterialBean showsMaterial) {
                    this.showsMaterial = showsMaterial;
                }

                public MaterialBean getMaterial() {
                    return Material;
                }

                public void setMaterial(MaterialBean Material) {
                    this.Material = Material;
                }

                public static class ShowsMaterialBean implements Serializable {
                    /**
                     * id : 3f3e5f49288b45e6a4c041a228d7daeb
                     * isNewRecord : false
                     * createDate : 2018-02-05 14:12:14
                     * updateDate : 2018-02-05 14:12:14
                     * showsId : 7feecaa2e02747aead433f63c062d7d0
                     * materialId : 8b3af6141f9d42e18b2151524c073490
                     * resolutionId : 6c17f3daa42a41e1838bc5b4186dac53
                     * x : 10
                     * y : 30
                     * wide : 500
                     * hige : 300
                     * showType : 1
                     * playTime : 15
                     * sort : 1
                     */

                    private String id;
                    private boolean isNewRecord;
                    private String createDate;
                    private String updateDate;
                    private String showsId;
                    private String materialId;
                    private String resolutionId;
                    private String x;
                    private String y;
                    private int wide;
                    private int hige;
                    private int showType;
                    private int playTime;
                    private int sort;

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

                    public String getShowsId() {
                        return showsId;
                    }

                    public void setShowsId(String showsId) {
                        this.showsId = showsId;
                    }

                    public String getMaterialId() {
                        return materialId;
                    }

                    public void setMaterialId(String materialId) {
                        this.materialId = materialId;
                    }

                    public String getResolutionId() {
                        return resolutionId;
                    }

                    public void setResolutionId(String resolutionId) {
                        this.resolutionId = resolutionId;
                    }

                    public String getX() {
                        return x;
                    }

                    public void setX(String x) {
                        this.x = x;
                    }

                    public String getY() {
                        return y;
                    }

                    public void setY(String y) {
                        this.y = y;
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

                    public int getShowType() {
                        return showType;
                    }

                    public void setShowType(int showType) {
                        this.showType = showType;
                    }

                    public int getPlayTime() {
                        return playTime;
                    }

                    public void setPlayTime(int playTime) {
                        this.playTime = playTime;
                    }

                    public int getSort() {
                        return sort;
                    }

                    public void setSort(int sort) {
                        this.sort = sort;
                    }
                }

                public static class MaterialBean implements Serializable {
                    /**
                     * id : 8b3af6141f9d42e18b2151524c073490
                     * isNewRecord : false
                     * createDate : 2018-01-29 15:49:20
                     * updateDate : 2018-01-29 17:26:14
                     * type : 2
                     * groupId : 173437fc93a249a58a0022b5452709c0
                     * file : http://127.0.0.1:8080/download/2018-01/material/79b354f4-6820-418d-a508-be1ab48cc145.mp3
                     * name : BMW
                     * size : 24
                     * groups : {"id":"173437fc93a249a58a0022b5452709c0","isNewRecord":false,"name":"建材"}
                     */

                    private String id;
                    private boolean isNewRecord;
                    private String createDate;
                    private String updateDate;
                    private int type;
                    private String groupId;
                    private String file;
                    private String name;
                    private String size;
                    private GroupsBean groups;
                    private String link;

                    public String getLink() {
                        return link;
                    }

                    public void setLink(String link) {
                        this.link = link;
                    }

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

                    public int getType() {
                        return type;
                    }

                    public void setType(int type) {
                        this.type = type;
                    }

                    public String getGroupId() {
                        return groupId;
                    }

                    public void setGroupId(String groupId) {
                        this.groupId = groupId;
                    }

                    public String getFile() {
                        return file;
                    }

                    public void setFile(String file) {
                        this.file = file;
                    }

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }

                    public String getSize() {
                        return size;
                    }

                    public void setSize(String size) {
                        this.size = size;
                    }

                    public GroupsBean getGroups() {
                        return groups;
                    }

                    public void setGroups(GroupsBean groups) {
                        this.groups = groups;
                    }

                    public static class GroupsBean implements Serializable{
                        /**
                         * id : 173437fc93a249a58a0022b5452709c0
                         * isNewRecord : false
                         * name : 建材
                         */

                        private String id;
                        private boolean isNewRecord;
                        private String name;

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

                        public String getName() {
                            return name;
                        }

                        public void setName(String name) {
                            this.name = name;
                        }
                    }
                }
            }

            public static class ShowsBean  implements Serializable{
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

                public static class ResolutionBean  implements Serializable{
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
}
