package in.ac.vit.sriharrsha.popularmoviesi;

import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;

/**
 * Created by Sri Harrsha on 02-Feb-16.
 */
public class ServiceGenerator {

    public static final String API_BASE_URL = "https://api.themoviedb.org/3/";

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                            .create()));

    public static <S> S createService(Class<S> serviceClass) {
        Retrofit retrofit = builder.client(httpClient.build()).build();
        return retrofit.create(serviceClass);
    }
}
