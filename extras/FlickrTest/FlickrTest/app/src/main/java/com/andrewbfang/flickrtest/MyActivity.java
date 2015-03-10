package com.andrewbfang.flickrtest;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.gmail.yuyang226.flickrj.sample.android.FlickrHelper;
import com.googlecode.flickrjandroid.Flickr;
import com.googlecode.flickrjandroid.FlickrException;
import com.googlecode.flickrjandroid.REST;
import com.googlecode.flickrjandroid.photos.Photo;
import com.googlecode.flickrjandroid.photos.PhotoList;
import com.googlecode.flickrjandroid.photos.PhotosInterface;
import com.googlecode.flickrjandroid.photos.SearchParameters;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import com.gmail.yuyang226.flickrj.sample.android.FlickrjActivity;

import javax.xml.parsers.ParserConfigurationException;

/**
 * http://taapps-javasamples.blogspot.com/2008/01/javaflickrjflickr-java-apiflickrj.html
 */
public class MyActivity extends Activity {


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);


        showImage();
        Button btnFlickr = (Button) findViewById(R.id.btnUpload);
        btnFlickr.setOnClickListener(mUploadClickListener);

        Button btnRefresh = (Button) findViewById(R.id.btnRefresh);
        btnRefresh.setOnClickListener(mRefreshClickListener);

        Button btnPick = (Button) findViewById(R.id.btnPick);
        btnPick.setOnClickListener(mPickClickListener);


    }

    File fileUri;

    View.OnClickListener mPickClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT); //
            intent.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            startActivityForResult(intent, 102); //**** caught on act result
        }

    };

    View.OnClickListener mUploadClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (fileUri == null) {
                Toast.makeText(getApplicationContext(), "Please pick photo",
                        Toast.LENGTH_SHORT).show();

                ImageView imview = (ImageView) findViewById(R.id.imview);
                Bitmap bitmap = ((BitmapDrawable)imview.getDrawable()).getBitmap();
                Intent intent = new Intent(getApplicationContext(),
                        FlickrjActivity.class);
                intent.putExtra("flickImage", bitmap);
                startActivity(intent);
            } else {


                Intent intent = new Intent(getApplicationContext(),
                        FlickrjActivity.class);
                intent.putExtra("flickImagePath", fileUri.getAbsolutePath());

                startActivity(intent);
            }
        }
    };

    View.OnClickListener mRefreshClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            showImage();
        }
    };

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 102) {

            if (resultCode == Activity.RESULT_OK) {
                Uri tmp_fileUri = data.getData();

                ((ImageView) findViewById(R.id.imview))
                        .setImageURI(tmp_fileUri);

                String selectedImagePath = getPath(tmp_fileUri);
                fileUri = new File(selectedImagePath);
            }

        }
    };
    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        @SuppressWarnings("deprecation")
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    public void onResume() {
        super.onResume();
        showImage();
    }

    private void showImage() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    String svr="www.flickr.com";

                    REST rest=new REST();
                    rest.setHost(svr);

                    //initialize Flickr object with key and rest
                    Flickr flickr=new Flickr(FlickrHelper.API_KEY,rest);

                    //initialize SearchParameter object, this object stores the search keyword
                    SearchParameters searchParams=new SearchParameters();
                    searchParams.setSort(SearchParameters.INTERESTINGNESS_DESC);

                    //Create tag keyword array
                    String[] tags=new String[]{"cs160_fall2014"};
                    searchParams.setTags(tags);

                    //Initialize PhotosInterface object
                    PhotosInterface photosInterface=flickr.getPhotosInterface();
                    //Execute search with entered tags
                    PhotoList photoList=photosInterface.search(searchParams,20,1);

                    //get search result and fetch the photo object and get small square imag's url
                    if(photoList!=null){
                        //Get search result and check the size of photo result
                        Random random = new Random();
                        int seed = random.nextInt(photoList.size());
                        //get photo object
                        Photo photo=(Photo)photoList.get(seed);

                        //Get small square url photo
                        InputStream is = photo.getMediumAsStream();
                        final Bitmap bm = BitmapFactory.decodeStream(is);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ImageView imageView = (ImageView) findViewById(R.id.imview);
                                imageView.setImageBitmap(bm);
                            }
                        });
                    }
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                } catch (FlickrException e) {
                    e.printStackTrace();
                } catch (IOException e ) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        thread.start();
    }

}