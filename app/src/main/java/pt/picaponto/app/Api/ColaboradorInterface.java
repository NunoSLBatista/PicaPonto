package pt.picaponto.app.Api;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ColaboradorInterface {

    String JSONURL = "https://picaponto.pt";
    @FormUrlEncoded
    @POST("services/api/get")
    Call<String> getData(
            @Field("token") String token
    );

    @FormUrlEncoded
    @POST("services/api/horario")
    Call<String> getHorario(
            @Field("token") String token, @Field("codigo") String codigo
    );

    @FormUrlEncoded
    @POST("services/api/sync")
    Call<String> sendData(
            @Field("json") String json
    );

}
