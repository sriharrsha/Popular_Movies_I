package in.ac.vit.sriharrsha.popularmoviesi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import butterknife.Bind;

public class DetailedAcitivity extends AppCompatActivity {
    @Bind(R.id.imageView)
    ImageView image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_movie);
        Result result = (Result) getIntent().getExtras().get("DATA");

    }
}
