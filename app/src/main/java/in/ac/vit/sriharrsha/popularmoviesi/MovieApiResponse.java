package in.ac.vit.sriharrsha.popularmoviesi;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MovieApiResponse implements Parcelable {

    public static final Creator<MovieApiResponse> CREATOR = new Creator<MovieApiResponse>() {
        @Override
        public MovieApiResponse createFromParcel(Parcel in) {
            return new MovieApiResponse(in);
        }

        @Override
        public MovieApiResponse[] newArray(int size) {
            return new MovieApiResponse[size];
        }
    };
    private int page;
    private List<Result> results = new ArrayList<Result>();
    private int totalResults;
    private int totalPages;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    protected MovieApiResponse(Parcel in) {
        page = in.readInt();
        results = in.createTypedArrayList(Result.CREATOR);
        totalResults = in.readInt();
        totalPages = in.readInt();
    }


    public MovieApiResponse() {

    }

    /**
     * @return The page
     */
    public int getPage() {
        return page;
    }

    /**
     * @param page The page
     */
    public void setPage(int page) {
        this.page = page;
    }

    /**
     * @return The results
     */
    public List<Result> getResults() {
        return results;
    }

    /**
     * @param results The results
     */
    public void setResults(List<Result> results) {
        this.results = results;
    }

    /**
     * @return The totalResults
     */
    public int getTotalResults() {
        return totalResults;
    }

    /**
     * @param totalResults The total_results
     */
    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    /**
     * @return The totalPages
     */
    public int getTotalPages() {
        return totalPages;
    }

    /**
     * @param totalPages The total_pages
     */
    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(page);
        dest.writeTypedList(results);
        dest.writeInt(totalResults);
        dest.writeInt(totalPages);
    }
}
