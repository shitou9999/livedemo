package tv.kuainiu.request;


import tv.kuainiu.BaseApi.HttpResult;
import tv.kuainiu.modle.NewsFlagList;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by jack on 2016/7/1.
 */
public interface CatsDataInterface {
    @GET("news/get_cats") Observable<HttpResult<NewsFlagList>> getCats();
}
