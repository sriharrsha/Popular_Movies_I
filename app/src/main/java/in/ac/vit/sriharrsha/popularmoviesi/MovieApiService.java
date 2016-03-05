package in.ac.vit.sriharrsha.popularmoviesi;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Sri Harrsha on 02-Feb-16.
 */
public interface MovieApiService {

    @GET("discover/movie?sort_by=popularity.desc")
    Call<MovieApiResponse> listPopularMovies(@Query("api_key") String apiKey);

    @GET("discover/movie?sort_by=vote_average.desc")
    Call<MovieApiResponse> listHighestRatedMovies(@Query("api_key") String apiKey);
}
