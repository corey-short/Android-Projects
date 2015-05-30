package cshort012.gmail.com.newsapplication;

import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.http.GET;

/**
 * Created by Short on 5/25/2015.
 * Creates a service from the New York Times Top Stories API.
 */
public class ApiClientService {
    private static NewYorkTimesService newYorkTimesService;
    private static final String API_URL = "http://api.nytimes.com/svc/topstories/v1/home.json?api-key=201f2d9fc5a0f555ac4ad2946c51950d:7:71840120";

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

        @GET("/path")
        //void getStories(@Query("limit") int limit, @Query("offset") int offset, Callback<List<StoryList>> callback);
        void getStoryList(Callback<List<StoryList>> callback);
    }

}
