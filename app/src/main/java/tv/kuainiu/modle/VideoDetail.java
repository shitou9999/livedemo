package tv.kuainiu.modle;

/**
 * Created by sirius on 2016/9/24.
 */

public class VideoDetail {

    /**
     * id : 21
     * cat_id : 3
     * title : 9-20倾力视频
     * thumb : http://kuainiu.oss-cn-shanghai.aliyuncs.com/uploadfile/thumb/201609/teaacher_cover_104785282636985.j
     * user_id : 1
     * description : 今日倾力视频
     * url :
     * inputtime : 1474360262
     * updatetime : 1474360262
     * video_id : 5A64D9A01D1415539C33DC5901307461
     * voice_url :
     * permission : 1
     * allow_comment : 1
     * support_num : 0
     * comment_num : 0
     * content : 老师9月20日最新视频
     * catname : 创业板
     * teacher_info :
     */

    private String id;
    private String cat_id;
    private String title;
    private String thumb;
    private String user_id;
    private String description;
    private String url;
    private String inputtime;
    private String updatetime;
    private String video_id;
    private String voice_url;
    private String permission;
    private String allow_comment;
    private String support_num;
    private String comment_num;
    private String view_num;
    private String content;
    private String catname;
    private TeacherInfo teacher_info;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getInputtime() {
        return inputtime;
    }

    public void setInputtime(String inputtime) {
        this.inputtime = inputtime;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public String getVideo_id() {
        return video_id;
    }

    public void setVideo_id(String video_id) {
        this.video_id = video_id;
    }

    public String getVoice_url() {
        return voice_url;
    }

    public void setVoice_url(String voice_url) {
        this.voice_url = voice_url;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getAllow_comment() {
        return allow_comment;
    }

    public void setAllow_comment(String allow_comment) {
        this.allow_comment = allow_comment;
    }

    public String getSupport_num() {
        return support_num;
    }

    public void setSupport_num(String support_num) {
        this.support_num = support_num;
    }

    public String getComment_num() {
        return comment_num;
    }

    public void setComment_num(String comment_num) {
        this.comment_num = comment_num;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCatname() {
        return catname;
    }

    public void setCatname(String catname) {
        this.catname = catname;
    }

    public TeacherInfo getTeacher_info() {
        return teacher_info;
    }

    public void setTeacher_info(TeacherInfo teacher_info) {
        this.teacher_info = teacher_info;
    }

    public String getView_num() {
        return view_num;
    }

    public void setView_num(String view_num) {
        this.view_num = view_num;
    }
}
