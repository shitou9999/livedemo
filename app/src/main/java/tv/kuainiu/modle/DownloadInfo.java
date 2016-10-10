package tv.kuainiu.modle;

import com.bokecc.sdk.mobile.download.Downloader;

import java.util.Date;

public class DownloadInfo {

    private int id;
    private String firstImage;
    private String newId;
    private String name;

    private String videoId;
    private String catId;

    private String title;

    private int progress;

    private String progressText;

    private int status;

    private Date createTime;

    private int definition;

    public DownloadInfo(String firstImage,String name,String newId,String videoId, String catId, String title, int progress, String progressText, int status, Date createTime) {
        this.firstImage = firstImage;
        this.name = name;
        this.newId = newId;
        this.videoId = videoId;
        this.catId = catId;
        this.title = title;
        this.progress = progress;
        this.progressText = progressText;
        this.status = status;
        this.createTime = createTime;
        this.definition = -1;
    }

    public DownloadInfo(String firstImage,String name,String newId,String videoId, String catId, String title, int progress, String progressText, int status, Date createTime, int definition) {
        this(firstImage,name,newId,videoId, catId, title, progress, progressText, status, createTime);
        this.definition = definition;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstImage() {
        return firstImage;
    }

    public void setFirstImage(String firstImage) {
        this.firstImage = firstImage;
    }

    public String getNewId() {
        return newId;
    }

    public void setNewId(String newId) {
        this.newId = newId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getDefinition() {
        return definition;
    }

    public void setDefinition(int definition) {
        this.definition = definition;
    }

    public String getProgressText() {
        if (progressText == null) {
            progressText = "0M / 0M";
        }
        return progressText;
    }

    public void setProgressText(String progressText) {
        this.progressText = progressText;
    }

    public String getStatusInfo() {
        String statusInfo = "";
        switch (status) {
            case Downloader.WAIT:
                statusInfo = "等待中";
                break;
            case Downloader.DOWNLOAD:
                statusInfo = "下载中";
                break;
            case Downloader.PAUSE:
                statusInfo = "暂停中";
                break;
            case Downloader.FINISH:
                statusInfo = "已下载";
                break;
            default:
                statusInfo = "下载失败";
                break;
        }

        return statusInfo;
    }

}
