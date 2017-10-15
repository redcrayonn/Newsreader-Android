package olaf.demol.nl.newsreader548385.net.Models.Results;

import olaf.demol.nl.newsreader548385.net.Models.Results.LoginResult;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by olaf on 15-10-17.
 */


public interface UserResult {

    @POST("users/login")
    @FormUrlEncoded
    Call<LoginResult> login(@Field("username") String username,
                            @Field("password") String password);
}