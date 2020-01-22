package id.co.derahh.moviecatalogue.API;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {

    private static final String BASE_URL = "https://api.themoviedb.org/3/";

    private static APIClient mInstance;
    private Retrofit retrofit;

    private APIClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static synchronized APIClient getInstance() {
        if (mInstance == null) {
            mInstance = new APIClient();
        }
        return mInstance;
    }

    public APIService getAPI() {
        return retrofit.create(APIService.class);
    }
}
