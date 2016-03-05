package in.ac.vit.sriharrsha.popularmoviesi;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static in.ac.vit.sriharrsha.popularmoviesi.R.id.action_sort_most_popular;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    private static final String API_KEY = BuildConfig.theMovieDbApiKey;
    PosterImageAdapter posterImageAdapter;
    MovieApiService movieDbService;
    Call<MovieApiResponse> call;
    MovieApiResponse movieApiResponse = new MovieApiResponse();
    ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        movieDbService = ServiceGenerator.createService(MovieApiService.class);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String defaultAction = sharedPref.getString(getString(R.string.default_action), getString(R.string.mostpopular));
        Log.i("pref", "default action is " + defaultAction + " is selected and called in oncreateoptions");

        MenuItem item = menu.findItem(R.id.action_sort_most_popular);
        Log.i("pref", item.getTitle() + " is selected ");
        if (item.getTitle().toString().equalsIgnoreCase(defaultAction)) {
            item.setCheckable(true);
            item.setChecked(true);
            Log.i("pref", item.getTitle() + " is selected and called in oncreateoptions");
        } else {
            item = menu.findItem(R.id.action_sort_high_rating);
            item.setCheckable(true);
            item.setChecked(true);
            Log.i("pref", item.getTitle() + " is selected and called in oncreateoptions");
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        if (item.getTitle().toString().equals(getString(R.string.mostpopular))) {
            editor.putString(getString(R.string.default_action), getString(R.string.mostpopular));
        } else {
            editor.putString(getString(R.string.default_action), getString(R.string.highrating));
        }
        editor.commit();

        switch (id) {
            case R.id.action_sort_high_rating:
                item.setChecked(true);
                Toast.makeText(getActivity(), "Sorted By Highest Rating", Toast.LENGTH_SHORT).show();
                call = movieDbService.listHighestRatedMovies(API_KEY);
                progressDialog.show();
                call.enqueue(new Callback<MovieApiResponse>() {
                    @Override
                    public void onResponse(Response<MovieApiResponse> response) {
                        movieApiResponse = response.body();
                        posterImageAdapter.movieList = movieApiResponse.getResults();
                        posterImageAdapter.notifyDataSetChanged();
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        progressDialog.dismiss();
                    }
                });


                return true;
            case action_sort_most_popular:
                item.setChecked(true);
                Toast.makeText(getActivity(), "Sorted By Most Popular", Toast.LENGTH_SHORT).show();
                call = movieDbService.listPopularMovies(API_KEY);
                progressDialog.show();
                call.enqueue(new Callback<MovieApiResponse>() {
                    @Override
                    public void onResponse(Response<MovieApiResponse> response) {
                        movieApiResponse = response.body();
                        posterImageAdapter.movieList = movieApiResponse.getResults();
                        posterImageAdapter.notifyDataSetChanged();
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onFailure(Throwable t) {

                        progressDialog.dismiss();
                    }
                });
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.setIndeterminate(true);
        progressDialog.show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String defaultAction = sharedPref.getString(getString(R.string.default_action), getString(R.string.mostpopular));
        if (defaultAction.equals(getString(R.string.mostpopular))) {
            call = movieDbService.listPopularMovies(API_KEY);

        } else {
            call = movieDbService.listHighestRatedMovies(API_KEY);
        }


        posterImageAdapter = new PosterImageAdapter(null);
        call.enqueue(new Callback<MovieApiResponse>() {
            @Override
            public void onResponse(Response<MovieApiResponse> response) {
                movieApiResponse = response.body();
                posterImageAdapter.movieList = movieApiResponse.getResults();
                posterImageAdapter.notifyDataSetChanged();
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Throwable t) {
                if (getActivity() != null)
                progressDialog.dismiss();
            }
        });
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        recyclerView.setAdapter(posterImageAdapter);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        return rootView;
    }

    public class PosterImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.movie_poster_image_view)
        ImageView imageHolder;
        @Bind(R.id.movie_title_bar)
        TextView title;
        @Bind(R.id.card_view)
        CardView cardView;
        Result data;

        public PosterImageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            cardView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(), DetailedAcitivity.class);
            intent.putExtra("DATA", data);
            startActivity(intent);
        }
    }

    private class PosterImageAdapter extends RecyclerView.Adapter<PosterImageViewHolder> {
        private static final String BASE_URL = "http://image.tmdb.org/t/p/w185/";
        List<Result> movieList = Collections.emptyList();

        public PosterImageAdapter(ArrayList<Result> list) {
            if (list != null)
                this.movieList = list;
        }

        @Override
        public int getItemCount() {
            if (this.movieList == null) {
                return 0;
            }
            return this.movieList.size();
        }

        @Override
        public PosterImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_poster_item, parent, false);
            PosterImageViewHolder posterImageViewHolder = new PosterImageViewHolder(v);
            return posterImageViewHolder;
        }

        @Override
        public void onBindViewHolder(PosterImageViewHolder holder, int position) {
            Glide.with(getContext())
                    .load(BASE_URL + this.movieList.get(position).getPosterPath())
                    .fitCenter()
                    .crossFade()
                    .into(holder.imageHolder);
            holder.title.setText(this.movieList.get(position).getTitle());
            holder.data = this.movieList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }
    }


}
