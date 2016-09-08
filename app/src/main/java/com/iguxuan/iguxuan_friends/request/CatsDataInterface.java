package com.iguxuan.iguxuan_friends.request;


import com.iguxuan.iguxuan_friends.BaseApi.HttpResult;
import com.iguxuan.iguxuan_friends.modle.NewsFlagList;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by jack on 2016/7/1.
 */
public interface CatsDataInterface {
    @GET("news/get_cats") Observable<HttpResult<NewsFlagList>> getCats();
}
