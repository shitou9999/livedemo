package com.iguxuan.iguxuan_friends.modle;

public class DownloadInfo {
    private int _id;
    private String title;
    private String status;
    private String vid;
    private String newsId;
    private String catId;
    private String teacherId;
    private String firstImage;
    private String duration;
    private long filesize;
    private long timeStamp;
    private int bitrate;
    private int percent;


    public DownloadInfo() {
    }

    public DownloadInfo(String vid) {
        this.vid = vid;
    }

    public DownloadInfo(String vid, String duration, long filesize, int bitrate) {
        this.vid = vid;
        this.duration = duration;
        this.filesize = filesize;
        this.bitrate = bitrate;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int getBitrate() {
        return bitrate;
    }

    public void setBitrate(int bitrate) {
        this.bitrate = bitrate;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public long getFilesize() {
        return filesize;
    }

    public void setFilesize(long filesize) {
        this.filesize = filesize;
    }

    public String getFirstImage() {
        return firstImage;
    }

    public void setFirstImage(String firstImage) {
        this.firstImage = firstImage;
    }

    public String getNewsId() {
        return newsId;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override public String toString() {
        return "DownloadInfo{" +
                "_id=" + _id +
                ", title='" + title + '\'' +
                ", status='" + status + '\'' +
                ", vid='" + vid + '\'' +
                ", newsId='" + newsId + '\'' +
                ", catId='" + catId + '\'' +
                ", teacherId='" + teacherId + '\'' +
                ", firstImage='" + firstImage + '\'' +
                ", duration='" + duration + '\'' +
                ", filesize=" + filesize +
                ", timeStamp=" + timeStamp +
                ", bitrate=" + bitrate +
                ", percent=" + percent +
                '}';
    }
}
