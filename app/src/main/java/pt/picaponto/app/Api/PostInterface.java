package pt.picaponto.app.Api;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface PostInterface {

    @FormUrlEncoded
    @POST("services/api/get")
    Call<String> getData(
            @Field("token") String token
    );


    @FormUrlEncoded
    @POST("services/api/sync")
    Call<String> sendData(
            @Field("json") String json
    );

}
