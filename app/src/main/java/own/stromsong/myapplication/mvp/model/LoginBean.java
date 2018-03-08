package own.stromsong.myapplication.mvp.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/3/8 0008.
 */

public class LoginBean implements Serializable{

    /**
     * id : 1abad3de4e084064b9f5d6217cfa154f
     * isNewRecord : false
     * createDate : 2018-01-03 15:08:11
     * updateDate : 2018-03-08 14:35:12
     * name : 上映一号
     * equId : N6714583
     * password : 77f41826506f1ddef123de0b676c0b64f8895703712315eb3f2d5d67
     * agentId : d140815ae5b441b9a7243db52f7ba273
     * state : 1
     * resolutionId : ae79e733cd2d4f9a8576ee33f1fbdb82
     * showMenuId : 6e9c683e0a054c80af4810ed03a97527
     * token : KA31LLIV6Y7BNEYBBBWKGLTV6VT9L5
     * agent : {"id":"d140815ae5b441b9a7243db52f7ba273","isNewRecord":false,"name":"海思广告","agentId":"82924258","ip":"","level":2}
     * showMenu : {"id":"6e9c683e0a054c80af4810ed03a97527","isNewRecord":false}
     * resolution : {"id":"ae79e733cd2d4f9a8576ee33f1fbdb82","isNewRecord":false,"screenType":2,"wide":100,"high":300}
     */

    private String id;
    private boolean isNewRecord;
    private String createDate;
    private String updateDate;
    private String name;
    private String equId;
    private String password;
    private String agentId;
    private int state;
    private String resolutionId;
    private String showMenuId;
    private String token;
    private AgentBean agent;
    private ShowMenuBean showMenu;
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

    public String getEquId() {
        return equId;
    }

    public void setEquId(String equId) {
        this.equId = equId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getResolutionId() {
        return resolutionId;
    }

    public void setResolutionId(String resolutionId) {
        this.resolutionId = resolutionId;
    }

    public String getShowMenuId() {
        return showMenuId;
    }

    public void setShowMenuId(String showMenuId) {
        this.showMenuId = showMenuId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public AgentBean getAgent() {
        return agent;
    }

    public void setAgent(AgentBean agent) {
        this.agent = agent;
    }

    public ShowMenuBean getShowMenu() {
        return showMenu;
    }

    public void setShowMenu(ShowMenuBean showMenu) {
        this.showMenu = showMenu;
    }

    public ResolutionBean getResolution() {
        return resolution;
    }

    public void setResolution(ResolutionBean resolution) {
        this.resolution = resolution;
    }

    public static class AgentBean  implements Serializable{
        /**
         * id : d140815ae5b441b9a7243db52f7ba273
         * isNewRecord : false
         * name : 海思广告
         * agentId : 82924258
         * ip :
         * level : 2
         */

        private String id;
        private boolean isNewRecord;
        private String name;
        private String agentId;
        private String ip;
        private int level;

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

        public String getAgentId() {
            return agentId;
        }

        public void setAgentId(String agentId) {
            this.agentId = agentId;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }
    }

    public static class ShowMenuBean implements Serializable {
        /**
         * id : 6e9c683e0a054c80af4810ed03a97527
         * isNewRecord : false
         */

        private String id;
        private boolean isNewRecord;

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
    }

    public static class ResolutionBean implements Serializable {
        /**
         * id : ae79e733cd2d4f9a8576ee33f1fbdb82
         * isNewRecord : false
         * screenType : 2
         * wide : 100
         * high : 300
         */

        private String id;
        private boolean isNewRecord;
        private int screenType;
        private int wide;
        private int high;

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
