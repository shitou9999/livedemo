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
        private String commentid;
        private String default_avatar;
        private int status;
        private int is_teacher;
        private int direction;
        private int userid;
        private String ip;
        private String content;
        private String username;
        private String parent_comment_content;
        private String teacher_id;
        private String source_show;
        private int reply;
        private int siteid;
        private long creat_at;
        private int parent_is_teacher;

        public String getCommentid() {
            return commentid;
        }

        public void setCommentid(String commentid) {
            this.commentid = commentid;
        }

        public String getDefault_avatar() {
            return default_avatar;
        }

        public void setDefault_avatar(String default_avatar) {
            this.default_avatar = default_avatar;
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

        public int getDirection() {
            return direction;
        }

        public void setDirection(int direction) {
            this.direction = direction;
        }

        public int getUserid() {
            return userid;
        }

        public void setUserid(int userid) {
            this.userid = userid;
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

        public long getCreat_at() {
            return creat_at;
        }

        public void setCreat_at(long creat_at) {
            this.creat_at = creat_at;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
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

        public int getSiteid() {
            return siteid;
        }

        public void setSiteid(int siteid) {
            this.siteid = siteid;
        }

        public String getTeacher_id() {
            return teacher_id;
        }

        public void setTeacher_id(String teacher_id) {
            this.teacher_id = teacher_id;
        }

        public String getSource_show() {
            return source_show;
        }

        public void setSource_show(String source_show) {
            this.source_show = source_show;
        }

        public int getParent_is_teacher() {
            return parent_is_teacher;
        }

        public void setParent_is_teacher(int parent_is_teacher) {
            this.parent_is_teacher = parent_is_teacher;
        }

        @Override public String toString() {
            return "CommentInfoEntity{" +
                    "commentid='" + commentid + '\'' +
                    ", default_avatar='" + default_avatar + '\'' +
                    ", status=" + status +
                    ", is_teacher=" + is_teacher +
                    ", direction=" + direction +
                    ", userid=" + userid +
                    ", ip='" + ip + '\'' +
                    ", content='" + content + '\'' +
                    ", username='" + username + '\'' +
                    ", parent_comment_content='" + parent_comment_content + '\'' +
                    ", teacher_id='" + teacher_id + '\'' +
                    ", source_show='" + source_show + '\'' +
                    ", reply=" + reply +
                    ", siteid=" + siteid +
                    ", creat_at=" + creat_at +
                    ", parent_is_teacher=" + parent_is_teacher +
                    '}';
        }
    }

    @Override public String toString() {
        return "SubCommentItem{" +
                "comment_data_id='" + comment_data_id + '\'' +
                ", comment_info=" + comment_info +
                '}';
    }
}
