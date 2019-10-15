package com.example.uhfsdkdemo.api;

import com.example.uhfsdkdemo.utils.versionUpdate.resultBack.AdressResult;
import com.example.uhfsdkdemo.utils.versionUpdate.resultBack.AppVersionBack;
import com.example.uhfsdkdemo.utils.versionUpdate.resultBack.ProblemBack;

import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by fengye on 2017/4/7.
 * email 1040441325@qq.com
 */
public interface AppVersionApi {
    @FormUrlEncoded
    @POST("web-com.feng.ssm/appVersion")
    Observable<Integer> getLatestVersion(@Field("id") Integer id);

    @FormUrlEncoded
    @POST("web-com.feng.ssm/apk")
    Observable<String> getApkUrl(@Field("version") Integer id);

    @GET("apps/{url}")
    Observable<ResponseBody> UpdateApk(@Path("url") String url);

    @FormUrlEncoded
    @POST("servlet/GetAppVersionServlet")
    Observable<AppVersionBack> getAppVersion(@Field("filename") String filename);

    @FormUrlEncoded
    @POST("servlet/GetAppVersionServlet")
    Observable<ProblemBack> getAppProblem(@Field("filename") String filename);

    @FormUrlEncoded
    @POST("servlet/DownLoadServlet")
    Observable<ResponseBody> updateApp(@Field("filename") String filename);

    @FormUrlEncoded
    @POST("servlet/GetAdressByMacServlet")
    Observable<AdressResult> getAdress(@Field("mac") long mac);//自己写的后台获取ap地址接口
}
