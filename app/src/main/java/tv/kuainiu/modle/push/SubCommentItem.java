package tv.kuainiu.modle.push;

/**
 * @author nanck on 2016/4/25.
 */
public class SubCommentItem {

    /**
     * comment_data_id : 410
     * comment_info : {"commentid":"content_54-3049-1","default_avatar":"http://img.iguxuan.com/uploadfile/avatar/default.png","status":1,"is_teacher":0,"direction":0,"userid":30,"ip":"192.168.2.149","content":"我软件可以考虑","creat_at":1461570008,"username":"用户1000030","parent_comment_content":"","reply":0,"siteid":1,"teacher_id":"6","parent_is_teacher":0}
     */

    private String comment_data_id;
    /**
     * commentid : content_54-3049-1
     * default_avatar : http://img.iguxuan.com/uploadfile/avatar/default.png
     * status : 1
     * is_teacher : 0
     * direction : 0
     * userid : 30
     * ip : 192.168.2.149
     * content : 我软件可以考虑
     * creat_at : 1461570008
     * username : 用户1000030
     * parent_comment_content :
     * reply : 0
     * siteid : 1
     * teacher_id : 6
     * parent_is_teacher : 0
     */

    private CommentInfoEntity comment_info;

    public String getComment_data_id() {
        return comment_data_id;
    }

    public void setComment_data_id(String comment_data_id) {
        this.comment_data_id = comment_data_id;
    }

    public CommentInfoEntity getComment_info() {
        return comment_info;
    }

    public void setComment_info(CommentInfoEntity comment_info) {
        this.comment_info = comment_info;
    }

    public static class CommentInfoEntity {

        /**
         * default_avatar : http://kuainiu.oss-cn-shanghai.aliyuncs.com/uploadfile/avatar/default.png
         * source_show : 来自android端
         * model : Coolpad Y82-820
         * status : 1
         * is_teacher : 0
         * app_version : 1
         * dynamics_id : 0
         * type : 1
         * ip : 116.236.199.6
         * content : 看咯
         * cat_id : 3
         * user_name : 大龙猫
         * news_id : 22
         * source : android
         * create_date : 1474958462
         * parent_comment_content :
         * reply : 0
         * user_id : 4
         * teacher_id : 1
         * parent_is_teacher : 0
         * system_version : 4.4.4
         */

        private String default_avatar;
        private String source_show;
        private String model;
        private int status;
        private int is_teacher;
        private String app_version;
        private int dynamics_id;
        private int type;
        private String ip;
        private String content;
        private String cat_id;
        private String user_name;
        private int news_id;
        private String source;
        private int create_date;
        private String parent_comment_content;
        private int reply;
        private int user_id;
        private String teacher_id;
        private int parent_is_teacher;
        private String system_version;

        public String getDefault_avatar() {
            return default_avatar;
        }

        public void setDefault_avatar(String default_avatar) {
            this.default_avatar = default_avatar;
        }

        public String getSource_show() {
            return source_show;
        }

        public void setSource_show(String source_show) {
            this.source_show = source_show;
        }

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getIs_teacher() {
            return is_teacher;
        }

        public void setIs_teacher(int is_teacher) {
            this.is_teacher = is_teacher;
        }

        public String getApp_version() {
            return app_version;
        }

        public void setApp_version(String app_version) {
            this.app_version = app_version;
        }

        public int getDynamics_id() {
            return dynamics_id;
        }

        public void setDynamics_id(int dynamics_id) {
            this.dynamics_id = dynamics_id;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCat_id() {
            return cat_id;
        }

        public void setCat_id(String cat_id) {
            this.cat_id = cat_id;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public int getNews_id() {
            return news_id;
        }

        public void setNews_id(int news_id) {
            this.news_id = news_id;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public int getCreate_date() {
            return create_date;
        }

        public void setCreate_date(int create_date) {
            this.create_date = create_date;
        }

        public String getParent_comment_content() {
            return parent_comment_content;
        }

        public void setParent_comment_content(String parent_comment_content) {
            this.parent_comment_content = parent_comment_content;
        }

        public int getReply() {
            return reply;
        }

        public void setReply(int reply) {
            this.reply = reply;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public String getTeacher_id() {
            return teacher_id;
        }

        public void setTeacher_id(String teacher_id) {
            this.teacher_id = teacher_id;
        }

        public int getParent_is_teacher() {
            return parent_is_teacher;
        }

        public void setParent_is_teacher(int parent_is_teacher) {
            this.parent_is_teacher = parent_is_teacher;
        }

        public String getSystem_version() {
            return system_version;
        }

        public void setSystem_version(String system_version) {
            this.system_version = system_version;
        }
    }

    @Override public String toString() {
        return "SubCommentItem{" +
                "comment_data_id='" + comment_data_id + '\'' +
                ", comment_info=" + comment_info +
                '}';
    }
}
