package in.ac.vit.sriharrsha.popularmoviesi;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    PosterImageAdapter posterImageAdapter;
    MovieDbService movieDbService;
    Call<Movie> call;
    Movie data = new Movie();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        movieDbService = ServiceGenerator.createService(MovieDbService.class);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
//        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
//        int defaultAction = sharedPref.getInt(getString(R.string.default_action), 0);
//        menu.findItem(defaultAction).setChecked(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(getString(R.string.default_action), id);
        editor.commit();

        switch (id) {
            case R.id.action_sort_high_rating:
                item.setChecked(true);
                Toast.makeText(getActivity(), "Sorted By Highest Rating", Toast.LENGTH_SHORT).show();
                call = movieDbService.listHighestRatedMovies();
                call.enqueue(new Callback<Movie>() {
                    @Override
                    public void onResponse(Response<Movie> response) {
                        data = response.body();
                        posterImageAdapter.movieData = data.getResults();
                        posterImageAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Log.e("POPULAR MOVIES RETROFIT", "Failed fetching");
                        Log.e("POPULAR MOVIES RETROFIT", t.getMessage());
                        Toast.makeText(getActivity(), "Failed !", Toast.LENGTH_LONG).show();
                    }
                });


                return true;
            case R.id.action_sort_most_popular:
                item.setChecked(true);
                Toast.makeText(getActivity(), "Sorted By Most Popular", Toast.LENGTH_SHORT).show();
                call = movieDbService.listPopularMovies();
                call.enqueue(new Callback<Movie>() {
                    @Override
                    public void onResponse(Response<Movie> response) {
                        data = response.body();
                        posterImageAdapter.movieData = data.getResults();
                        posterImageAdapter.notifyDataSetChanged();
                        Toast.makeText(getActivity(), "Data !", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Log.e("POPULAR MOVIES RETROFIT", "Failed fetching");
                        Log.e("POPULAR MOVIES RETROFIT", t.getMessage());
                        Toast.makeText(getActivity(), "Failed !", Toast.LENGTH_LONG).show();
                    }
                });
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        call = movieDbService.listHighestRatedMovies();
        posterImageAdapter = new PosterImageAdapter(null);
        call.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Response<Movie> response) {
                data = response.body();
                posterImageAdapter.movieData = data.getResults();
                posterImageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("POPULAR MOVIES RETROFIT", "Failed fetching");
                Log.e("POPULAR MOVIES RETROFIT", t.getMessage());
                Toast.makeText(getActivity(), "Failed !", Toast.LENGTH_LONG).show();
            }
        });
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        recyclerView.setAdapter(posterImageAdapter);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        //recyclerView.setOnItemClickListener(this);
        return rootView;
    }

    public static class PosterImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageHolder;
        TextView title;
        CardView cardView;

        public PosterImageViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
            cardView.setOnClickListener(this);
            imageHolder = (ImageView) itemView.findViewById(R.id.movie_poster_image_view);
            title = (TextView) itemView.findViewById(R.id.movie_title_bar);
        }


        @Override
        public void onClick(View v) {

//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent intent=new Intent(getActivity(),DetailedAcitivity.class);
//                intent.putExtra("DATA", (Parcelable) data.getResults().get(position));
//                startActivity(intent);
//            }
        }
    }

    private class PosterImageAdapter extends RecyclerView.Adapter<PosterImageViewHolder> {
        private static final String BASE_URL = "http://image.tmdb.org/t/p/w185/";
        List<Result> movieData = Collections.emptyList();

        public PosterImageAdapter(ArrayList<Result> data) {
            if (data != null)
                this.movieData = data;
        }


        @Override
        public int getItemCount() {
            if (this.movieData == null) {
                return 0;
            }
            return this.movieData.size();
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
                    .load(BASE_URL + this.movieData.get(position).getPosterPath())
                    .fitCenter()
                    .crossFade()
                    .into(holder.imageHolder);
            holder.title.setText(this.movieData.get(position).getTitle());
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }

//            if (movieData.size() > 0) {
//
//            }
//            return convertView;

    }


}
