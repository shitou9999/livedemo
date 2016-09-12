package com.kuainiutv.request;


import com.kuainiutv.BaseApi.HttpResult;
import com.kuainiutv.modle.NewsFlagList;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by jack on 2016/7/1.
 */
public interface CatsDataInterface {
    @GET("news/get_cats") Observable<HttpResult<NewsFlagList>> getCats();
}
