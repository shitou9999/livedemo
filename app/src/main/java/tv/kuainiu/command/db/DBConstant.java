package tv.kuainiu.command.db;


/**
 * <p>
 * version 4  [2.X]<br>
 * 增加直播预约表 @see{@link LiveSubscribeContract}
 * </p>
 * <p>
 * version 3  [2.1.1]<br>
 * 播放记录表新增字段 "stopPosition" -- 记录停止播放时的进度
 * </p>
 * <p>
 * version 2  [2.0.0]<br>
 * 增加下载记录表 @see{@link VDLContract}<br>
 * </p>
 * <p>
 * version 1  [1.0.0]<br>
 * 新建播放记录表 @see{@link #TABLE_PLAYRECORD}
 * </p>
 */
public class DBConstant {

    public static final String DB_NAME = "tv.kuainiu.db";
    public static final int DB_VERSION = 1;

    public static final String TABLE_PLAYRECORD = "NewsItem";
}
