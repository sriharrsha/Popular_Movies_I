package in.ac.vit.sriharrsha.popularmoviesi;


import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Sri Harrsha on 02-Feb-16.
 */
public interface MovieDbService {

    @GET("discover/movie?sort_by=popularity.desc&api_key=e04a342e57a66223e87fb82449e7a691")
    Call<Movie> listPopularMovies();

    @GET("discover/movie?sort_by=vote_average.desc&api_key=e04a342e57a66223e87fb82449e7a691")
    Call<Movie> listHighestRatedMovies();
}
