package in.ac.vit.sriharrsha.popularmoviesi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DetailedAcitivity extends AppCompatActivity {
    private static final String BASE_URL = "http://image.tmdb.org/t/p/w185/";
    @Bind(R.id.thumbnail)
    ImageView thumbnail;
    Result itemData;
    @Bind(R.id.plot_synopsis)
    TextView overview;
    @Bind(R.id.rating_bar)
    RatingBar userRating;
    @Bind(R.id.release_date)
    TextView releaseDate;
    @Bind(R.id.original_title)
    TextView originalTitle;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_movie);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        itemData = getIntent().getParcelableExtra("DATA");
        ButterKnife.bind(this);
        originalTitle.setText(itemData.getOriginalTitle());
        Glide.with(DetailedAcitivity.this).load(BASE_URL + itemData.getBackdropPath()).into(thumbnail);
        overview.setText(itemData.getOverview());
        userRating.setRating((float) (itemData.getVote_average() / 2));
        userRating.setIsIndicator(true);
        if (itemData.getRelease_date() != null) {
            releaseDate.setText("Release date: " + itemData.getRelease_date());
        } else {
            releaseDate.setText("Release date: N/A");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
