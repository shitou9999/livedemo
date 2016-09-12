package com.kuainiutv.command.http.core;

/**
 * Created by jack on 2016/5/4.
 */
public class CacheConfig {
    private boolean isCached = false;
    private boolean isCacheFirstPage = true;
    private int pageNumber = 1;
    private int expirationTime = -1;
    private static CacheConfig cacheConfig;

    public static CacheConfig getCacheConfig(int pageNumber) {
        if (cacheConfig == null) {
            cacheConfig = new CacheConfig();
        }
        cacheConfig.setCached(true);
        cacheConfig.setCacheFirstPage(false);
        cacheConfig.setPageNumber(pageNumber);
        cacheConfig.setExpirationTime(-1);
        return cacheConfig;
    }
    public static CacheConfig getCacheConfig() {
        if (cacheConfig == null) {
            cacheConfig = new CacheConfig();
        }
        cacheConfig.setCached(true);
        cacheConfig.setCacheFirstPage(true);
        cacheConfig.setPageNumber(1);
        cacheConfig.setExpirationTime(-1);
        return cacheConfig;
    }

    /**
     * 是否缓存,默认不缓存
     *
     * @return
     */
    public boolean isCached() {
        return isCached;
    }

    public void setCached(boolean cached) {
        isCached = cached;
    }

    /**
     * 是否只缓存第一页，默认只缓存第一页
     *
     * @return
     */
    public boolean isCacheFirstPage() {
        return isCacheFirstPage;
    }

    /**
     * 是否只缓存第一页
     *
     * @param cacheFirstPage
     */
    public void setCacheFirstPage(boolean cacheFirstPage) {
        isCacheFirstPage = cacheFirstPage;
    }

    /**
     * 缓存页数
     *
     * @return
     */
    public int getPageNumber() {
        return pageNumber;
    }

    /**
     * 设置当前的缓存是第几页
     *
     * @param pageNumber
     */
    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    /**
     * 过期时间，-1表示永不过期
     *
     * @return
     */
    public int getExpirationTime() {
        return expirationTime;
    }

    /**
     * 过期时间，-1表示永不过期
     *
     * @param expirationTime，单位分钟
     */
    public void setExpirationTime(int expirationTime) {
        this.expirationTime = expirationTime;
    }

    public CacheConfig() {
    }

    public CacheConfig(boolean isCached, boolean isCacheFirstPage, int pageNumber, int expirationTime) {
        this.isCached = isCached;
        this.isCacheFirstPage = isCacheFirstPage;
        this.pageNumber = pageNumber;
        this.expirationTime = expirationTime;
    }
}
