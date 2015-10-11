package cshort012.gmail.com.newsapplication;

import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.http.GET;

/**
 * Created by Short on 5/25/2015.
 * Creates a service from the New York Times Most Popular API.
 */
public class ApiClientService {
    private static NewYorkTimesService newYorkTimesService;
    //private static final String API_URL = "http://api.nytimes.com/svc/mostpopular/v2/mostviewed/all-sections/1.json?api-key=98301a99f20298a6a0d917da47ce5cab:18:72981922";
    private static final String API_URL = "http://api.nytimes.com/svc/mostpopular/v2/mostviewed";

    public static NewYorkTimesService getNewYorkTimesService() {
        // Consume New York Times Top Stories API
        if (newYorkTimesService == null) {
            RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(API_URL)
                .build();
            // Create the New York Times Service
            newYorkTimesService = restAdapter.create(NewYorkTimesService.class);
        }
        return newYorkTimesService;
    }

    public interface NewYorkTimesService {

        @GET("/all-sections/1.json?api-key=98301a99f20298a6a0d917da47ce5cab:18:72981922")
        //void getStories(@Query("limit") int limit, @Query("offset") int offset, Callback<List<StoryList>> callback);
        void getStoryList(Callback<List<StoryList>> callback);
    }

}
