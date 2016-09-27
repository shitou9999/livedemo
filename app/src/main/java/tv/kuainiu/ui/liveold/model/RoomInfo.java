package tv.kuainiu.ui.liveold.model;

/**
 * @author nanck on 2016/7/7.
 */
public class RoomInfo {
    /**
     * connections : 3
     * room_info : {"id":"753C612EB38A8D5A","name":"踩踩踩踩踩","desc":"踩踩踩踩踩","status":"10","publisherPass":"1234567","playPass":"7654321","assistantPass":"123456","checkUrl":"http://www.a.b/check","templateType":"3","authType":"0","barrage":"0","publishUrl":"rtmp://1.1.1.1/src/753C612EB38A8D5A","openLowDelayMode":"1","delayTime":"0","showUserCount":"1","openHostMode":"0"}
     */

    private int connections;
    /**
     * id : 753C612EB38A8D5A
     * name : 踩踩踩踩踩
     * desc : 踩踩踩踩踩
     * status : 10
     * publisherPass : 1234567
     * playPass : 7654321
     * assistantPass : 123456
     * checkUrl : http://www.a.b/check
     * templateType : 3
     * authType : 0
     * barrage : 0
     * publishUrl : rtmp://1.1.1.1/src/753C612EB38A8D5A
     * openLowDelayMode : 1
     * delayTime : 0
     * showUserCount : 1
     * openHostMode : 0
     */

    private RoomInfoEntity room_info;

    public int getConnections() {
        return connections;
    }

    public void setConnections(int connections) {
        this.connections = connections;
    }

    public RoomInfoEntity getRoom_info() {
        return room_info;
    }

    public void setRoom_info(RoomInfoEntity room_info) {
        this.room_info = room_info;
    }

    public static class RoomInfoEntity {
        private String id;
        private String name;
        private String desc;
        private String status;
        private String publisherPass;
        private String playPass;
        private String assistantPass;
        private String checkUrl;
        private String templateType;
        private String authType;
        private String barrage;
        private String publishUrl;
        private String openLowDelayMode;
        private String delayTime;
        private String showUserCount;
        private String openHostMode;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getPublisherPass() {
            return publisherPass;
        }

        public void setPublisherPass(String publisherPass) {
            this.publisherPass = publisherPass;
        }

        public String getPlayPass() {
            return playPass;
        }

        public void setPlayPass(String playPass) {
            this.playPass = playPass;
        }

        public String getAssistantPass() {
            return assistantPass;
        }

        public void setAssistantPass(String assistantPass) {
            this.assistantPass = assistantPass;
        }

        public String getCheckUrl() {
            return checkUrl;
        }

        public void setCheckUrl(String checkUrl) {
            this.checkUrl = checkUrl;
        }

        public String getTemplateType() {
            return templateType;
        }

        public void setTemplateType(String templateType) {
            this.templateType = templateType;
        }

        public String getAuthType() {
            return authType;
        }

        public void setAuthType(String authType) {
            this.authType = authType;
        }

        public String getBarrage() {
            return barrage;
        }

        public void setBarrage(String barrage) {
            this.barrage = barrage;
        }

        public String getPublishUrl() {
            return publishUrl;
        }

        public void setPublishUrl(String publishUrl) {
            this.publishUrl = publishUrl;
        }

        public String getOpenLowDelayMode() {
            return openLowDelayMode;
        }

        public void setOpenLowDelayMode(String openLowDelayMode) {
            this.openLowDelayMode = openLowDelayMode;
        }

        public String getDelayTime() {
            return delayTime;
        }

        public void setDelayTime(String delayTime) {
            this.delayTime = delayTime;
        }

        public String getShowUserCount() {
            return showUserCount;
        }

        public void setShowUserCount(String showUserCount) {
            this.showUserCount = showUserCount;
        }

        public String getOpenHostMode() {
            return openHostMode;
        }

        public void setOpenHostMode(String openHostMode) {
            this.openHostMode = openHostMode;
        }
    }
}
