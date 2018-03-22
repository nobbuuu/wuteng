package own.stromsong.myapplication.mvp.model;

/**
 * Created by Administrator on 2018/3/21 0021.
 */

public class UpdateAppBean {

    /**
     * APK : {"id":"c81629fcc9ac4f248996fa5e20cc02f3","isNewRecord":false,"createDate":"2018-01-03 17:42:29","updateDate":"2018-01-12 17:43:54","name":"悟腾广告","version":"1.0","appFile":"/2018-01/APP/dff1ea64-8c87-44f0-90b3-6b2ed0b91d3d.xlsx","descs":"<p>\r\n\t发123<\/p>","agentIds":"21b88a788d864011b425e858d5c42cee,6f423109fe974cec80b9a04f39a6b0bb,e3dcff2999ca45a98847d08d9cf8bfc7,d140815ae5b441b9a7243db52f7ba273","agentNum":0}
     */

    private APKBean APK;

    public APKBean getAPK() {
        return APK;
    }

    public void setAPK(APKBean APK) {
        this.APK = APK;
    }

    public static class APKBean {
        /**
         * id : c81629fcc9ac4f248996fa5e20cc02f3
         * isNewRecord : false
         * createDate : 2018-01-03 17:42:29
         * updateDate : 2018-01-12 17:43:54
         * name : 悟腾广告
         * version : 1.0
         * appFile : /2018-01/APP/dff1ea64-8c87-44f0-90b3-6b2ed0b91d3d.xlsx
         * descs : <p>
         发123</p>
         * agentIds : 21b88a788d864011b425e858d5c42cee,6f423109fe974cec80b9a04f39a6b0bb,e3dcff2999ca45a98847d08d9cf8bfc7,d140815ae5b441b9a7243db52f7ba273
         * agentNum : 0
         */

        private String id;
        private boolean isNewRecord;
        private String createDate;
        private String updateDate;
        private String name;
        private String version;
        private String appFile;
        private String descs;
        private String agentIds;
        private int agentNum;

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

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getAppFile() {
            return appFile;
        }

        public void setAppFile(String appFile) {
            this.appFile = appFile;
        }

        public String getDescs() {
            return descs;
        }

        public void setDescs(String descs) {
            this.descs = descs;
        }

        public String getAgentIds() {
            return agentIds;
        }

        public void setAgentIds(String agentIds) {
            this.agentIds = agentIds;
        }

        public int getAgentNum() {
            return agentNum;
        }

        public void setAgentNum(int agentNum) {
            this.agentNum = agentNum;
        }
    }
}
