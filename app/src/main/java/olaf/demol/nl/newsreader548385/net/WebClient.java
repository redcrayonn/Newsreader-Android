package olaf.demol.nl.newsreader548385.net;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import olaf.demol.nl.newsreader548385.net.Services.ArticleService;
import olaf.demol.nl.newsreader548385.net.Services.UserService;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by olaf on 15-10-17.
 */

public class WebClient {

    private Retrofit retrofit;

    public WebClient() {
        // Custom format for dates since backend gives non-timezone'd dates which the default Gson parser needs
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();

        // Setup Retrofit
        retrofit = new Retrofit.Builder()
                .baseUrl("http://inhollandbackend.azurewebsites.net/api/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    public ArticleService createArticleService() {
        return retrofit.create(ArticleService.class);
    }

    public UserService createUserService() {
        return retrofit.create(UserService.class);
    }
}
