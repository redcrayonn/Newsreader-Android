package olaf.demol.nl.newsreader548385.net.Services;

import olaf.demol.nl.newsreader548385.net.Models.Results.LoginResult;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface UserService {
    @POST("users/login")
    @FormUrlEncoded
    Call<LoginResult> login(@Field("username") String username, @Field("password") String password);
}
