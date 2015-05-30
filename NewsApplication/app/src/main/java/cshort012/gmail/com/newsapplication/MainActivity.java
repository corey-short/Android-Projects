package cshort012.gmail.com.newsapplication;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


// TODO: Use Robospice to better handle Android Activity lifecycle
public class MainActivity extends Activity {

    /* If the amount of items left in the ListView is the threshold
     * then start downloading more (if available)
     */
    private static final int THRESHOLD = 5;
    private static final int ITEMS_PER_PAGE = 6;
    private NewYorkTimesStoryAdapter newYorkTimesStoryAdapter;
    private ProgressBar progressBar;
    private boolean isDownloading = false;

    private static class ActivityState {
        private int nextPage = 0;
        private List<Story> storyData = new ArrayList<>();
    }

    // Holds state information for this activity
    private ActivityState activityState = new ActivityState();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        System.out.println("On Create");

        if (getLastNonConfigurationInstance() instanceof ActivityState) {
            activityState = (ActivityState) getLastNonConfigurationInstance();
        }

        setContentView(R.layout.activity_main);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        System.out.println("Set content view and progress bar");

        // Create the ArrayAdapter and bind it to the GridView
        GridView gridView = (GridView) findViewById(R.id.grid);

        System.out.println("gridview, " + gridView);
        gridView.setOnScrollListener(scrollListener);



        newYorkTimesStoryAdapter = new NewYorkTimesStoryAdapter(this, 0, activityState.storyData);

        System.out.println("adapter, " + newYorkTimesStoryAdapter);

        gridView.setAdapter(newYorkTimesStoryAdapter);
        System.out.println("adapter set");
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("adapter item listener created");
                String storyTitle = activityState.storyData.get(position).getTitle();
                System.out.println("Story title: " + storyTitle);

                //Intent viewStory = new Intent(Intent.ACTION_VIEW, stories.get(position));
                //startActivity(viewStory);
            }
        });
        System.out.println("On create finished");
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Display the first story
        if (activityState.nextPage == 0) {
            displayMoreStories(activityState.nextPage);
        }
    }

    @Override
    public Object onRetainNonConfigurationInstance() {
        // Return state so we can later restore it in onCreate()
        return activityState;
    }

    private void displayMoreStories(final int pageNumber) {
        System.out.println("page number " + pageNumber);
        if (!isDownloading) {
            isDownloading = true;

            progressBar.setVisibility(View.VISIBLE);

            ApiClientService.getNewYorkTimesService().getStoryList(new Callback<List<StoryList>>() {
                @Override
                public void success(List<StoryList> storyLists, Response response) {
                    System.out.println("success");
                    System.out.println("num results " + storyLists.get(pageNumber).getNumResults());
                }

                @Override
                public void failure(RetrofitError error) {
                    System.out.println("failure: " + error.getUrl());
                    consumeApiData(null);


                }
            });

            /*ApiClientService.getNewYorkTimesService().getStories(ITEMS_PER_PAGE, pageNumber * ITEMS_PER_PAGE, new Callback<List<StoryList>>() {
                @Override
                public void success(List<StoryList> storyLists, Response response) {
                    consumeApiData(storyLists);
                }
                @Override
                public void failure(RetrofitError error) {
                    consumeApiData(null);
                }
            });*/
        }
    }

    private void consumeApiData(List<StoryList> storyLists) {
        System.out.println("consume Api Data");
        if (storyLists != null) {
            // Add the found stories to our array to render in the UI
            //activityState.storyData.addAll(storyLists)

            // Notify the adapter of the change in state
            newYorkTimesStoryAdapter.notifyDataSetChanged();

            // Keep track of what page to download next
            activityState.nextPage++;
        }
        isDownloading = false;
    }

    // Scroll-handler for the ListView which can auto-load the next page of data
    private AbsListView.OnScrollListener scrollListener = new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView absListView, int scrollState) {
            // Do nothing
        }
        @Override
        public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            // Determine if the ListView is running low on data
            if (totalItemCount > 0 && totalItemCount - (visibleItemCount + firstVisibleItem) <= THRESHOLD) {
                displayMoreStories(activityState.nextPage);
            }
        }
    };

}
