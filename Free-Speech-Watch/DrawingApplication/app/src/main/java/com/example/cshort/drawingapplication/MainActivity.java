package com.example.cshort.drawingapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.gmail.yuyang226.flickrj.sample.android.FlickrHelper;
import com.gmail.yuyang226.flickrj.sample.android.FlickrjActivity;
import com.googlecode.flickrjandroid.Flickr;
import com.googlecode.flickrjandroid.FlickrException;
import com.googlecode.flickrjandroid.REST;
import com.googlecode.flickrjandroid.photos.Photo;
import com.googlecode.flickrjandroid.photos.PhotoList;
import com.googlecode.flickrjandroid.photos.PhotosInterface;
import com.googlecode.flickrjandroid.photos.SearchParameters;
import com.qualcomm.toq.smartwatch.api.v1.deckofcards.DeckOfCardsEventListener;
import com.qualcomm.toq.smartwatch.api.v1.deckofcards.card.Card;
import com.qualcomm.toq.smartwatch.api.v1.deckofcards.card.ListCard;
import com.qualcomm.toq.smartwatch.api.v1.deckofcards.card.NotificationTextCard;
import com.qualcomm.toq.smartwatch.api.v1.deckofcards.card.SimpleTextCard;
import com.qualcomm.toq.smartwatch.api.v1.deckofcards.remote.DeckOfCardsManager;
import com.qualcomm.toq.smartwatch.api.v1.deckofcards.remote.RemoteDeckOfCards;
import com.qualcomm.toq.smartwatch.api.v1.deckofcards.remote.RemoteDeckOfCardsException;
import com.qualcomm.toq.smartwatch.api.v1.deckofcards.remote.RemoteResourceStore;
import com.qualcomm.toq.smartwatch.api.v1.deckofcards.remote.RemoteToqNotification;
import com.qualcomm.toq.smartwatch.api.v1.deckofcards.resource.CardImage;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Random;
import java.util.UUID;

import javax.xml.parsers.ParserConfigurationException;


public class MainActivity extends Activity {

    private DeckOfCardsManager deckOfCardsManager;
    private DeckOfCardsEventListener deckOfCardsEventListener;
    private RemoteDeckOfCards remoteDeckOfCards;
    private RemoteResourceStore remoteResourceStore;
    private SimpleTextCard simpleTextCard;
    private ListCard listCard;
    private CardImage[] cardImages;
    private String notificationCardSent;
    private final int WIDTH = 250;
    private final int HEIGHT = 288;
    protected File fileUri;
    private Location destination;
    private DrawingView drawView;
    private ImageButton currPaint, drawBtn, eraseBtn, newBtn, saveBtn;
    private float smallBrush, mediumBrush, largeBrush;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the reference to the deck of cards manager
        deckOfCardsManager = DeckOfCardsManager.getInstance(getApplicationContext());
        deckOfCardsEventListener = new mDeckOfCardsEventListener();

        init();
        initLocation();
        initDrawing();

    }

    // Initialize remoteResourceStore, get the card images for cardImages[]
    // and create a remoteDeckOfCards.
    public void init() {
        // Creates a resource storage for card icons
        remoteResourceStore = new RemoteResourceStore();

        // Get the card images
        try {
            cardImages = new CardImage[10];

            cardImages[0] = new CardImage("card.image.1", getBitmap("art_goldberg_toq.png"));
            cardImages[1] = new CardImage("card.image.2", getBitmap("jack_weinberg_toq.png"));
            cardImages[2] = new CardImage("card.image.3", getBitmap("jackie_goldberg_toq.png"));
            cardImages[3] = new CardImage("card.image.4", getBitmap("joan_baez_toq.png"));
            cardImages[4] = new CardImage("card.image.5", getBitmap("mario_savio_toq.png"));
            cardImages[5] = new CardImage("card.image.6", getBitmap("michael_rossman_toq.png"));
        }
        catch (Exception e) {
            Toast.makeText(this, "Error occurred retrieving card images", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            remoteDeckOfCards = createDeckOfCards();
        }
        catch (Throwable e) {
            e.printStackTrace();
        }

        // Re-populate the resource store with any card images being used by any of the cards
        for (Iterator<Card> it = remoteDeckOfCards.getListCard().iterator(); it.hasNext(); ) {

            String cardImageId = ((SimpleTextCard) it.next()).getCardImageId();

            if ((cardImageId != null) && !remoteResourceStore.containsId(cardImageId)) {

                if (cardImageId.equals("card.image.1")) {
                    remoteResourceStore.addResource(cardImages[0]);
                } else if (cardImageId.equals("card.image.2")) {
                    remoteResourceStore.addResource(cardImages[1]);
                } else if (cardImageId.equals("card.image.3")) {
                    remoteResourceStore.addResource(cardImages[2]);
                } else if (cardImageId.equals("card.image.4")) {
                    remoteResourceStore.addResource(cardImages[3]);
                } else if (cardImageId.equals("card.image.5")) {
                    remoteResourceStore.addResource(cardImages[4]);
                } else if (cardImageId.equals("card.image.6")) {
                    remoteResourceStore.addResource(cardImages[5]);
                }
            }
        }
    }

    // Read an image from res\drawable and return as a bitmap
    private Bitmap getBitmap(String fileName) throws Exception {

        try {
            InputStream in = getAssets().open(fileName);
            return BitmapFactory.decodeStream(in);
        }
        catch (Exception e) {
            throw new Exception("An error occurred getting the bitmap: " + fileName, e);
        }
    }

    // Create FSM cards with content as pngs of people involved.
    private RemoteDeckOfCards createDeckOfCards() {
        listCard = new ListCard();

        // Art Goldberg card
        simpleTextCard = new SimpleTextCard("Art Goldberg");
        simpleTextCard.setMessageText(new String[]{"Art Goldberg", "Draw Text: Now"});
        simpleTextCard.setCardImage(remoteResourceStore, cardImages[0]);
        simpleTextCard.setReceivingEvents(true);
        listCard.add(simpleTextCard);

        // Jack Weinberg card
        simpleTextCard = new SimpleTextCard("Jack Weinberg");
        simpleTextCard.setMessageText(new String[]{"Jack Weinberg", "Draw Text: FSM"});
        simpleTextCard.setCardImage(remoteResourceStore, cardImages[1]);
        simpleTextCard.setReceivingEvents(true);
        listCard.add(simpleTextCard);

        // Jackie Goldberg card
        simpleTextCard = new SimpleTextCard("Jackie Goldberg");
        simpleTextCard.setMessageText(new String[]{"Jackie Goldberg", "Draw Text: SLATE"});
        simpleTextCard.setCardImage(remoteResourceStore, cardImages[2]);
        simpleTextCard.setReceivingEvents(true);
        listCard.add(simpleTextCard);

        // Joan Baez card
        simpleTextCard = new SimpleTextCard(("Joan Baez"));
        simpleTextCard.setMessageText(new String[]{"Joan Baez", "Draw Image of: A Megaphone"});
        simpleTextCard.setCardImage(remoteResourceStore, cardImages[3]);
        simpleTextCard.setReceivingEvents(true);
        listCard.add(simpleTextCard);

        // Mario Savio card
        simpleTextCard = new SimpleTextCard("Mario Savio");
        simpleTextCard.setMessageText(new String[]{"Mario Savio", "Express your own view of Free Speech in an image"});
        simpleTextCard.setCardImage(remoteResourceStore, cardImages[4]);
        simpleTextCard.setReceivingEvents(true);
        listCard.add(simpleTextCard);

        // Michael Rossman
        simpleTextCard = new SimpleTextCard("Michael Rossman");
        simpleTextCard.setMessageText(new String[]{"Michael Rossman", "Draw Text: Free Speech"});
        simpleTextCard.setCardImage(remoteResourceStore, cardImages[5]);
        simpleTextCard.setReceivingEvents(true);
        listCard.add(simpleTextCard);

        return new RemoteDeckOfCards(this, listCard);
    }

    /**
     * @see android.app.Activity#onStart()
     */
    protected void onStart() {
        super.onStart();

        // Add the listeners
        deckOfCardsManager.addDeckOfCardsEventListener(deckOfCardsEventListener);

        // If not connected, try to connect
        if (!deckOfCardsManager.isConnected()) {
            try {
                deckOfCardsManager.connect();
            }
            catch (RemoteDeckOfCardsException e) {
                e.printStackTrace();
            }
        }
    }

    // Installs Free Speech Watch applet on Toq
    public void install(View view) {
        try {
            deckOfCardsManager.installDeckOfCards(remoteDeckOfCards, remoteResourceStore);
        }
        catch (RemoteDeckOfCardsException e) {
            e.printStackTrace();
            try { // This is mostly for quick debugging purposes
                deckOfCardsManager.uninstallDeckOfCards();
                deckOfCardsManager.installDeckOfCards(remoteDeckOfCards, remoteResourceStore);
            } catch (RemoteDeckOfCardsException e2) {
                e2.printStackTrace();
            }
        }
    }

    // Handle card events triggered by the user interacting with a card in the installed deck of cards
    private class mDeckOfCardsEventListener implements DeckOfCardsEventListener {

        /**
         * Removes Install button from main activity view and brings DrawingView to focus
         * @see com.qualcomm.toq.smartwatch.api.v1.deckofcards.DeckOfCardsEventListener#onCardOpen(java.lang.String)
         */
        public void onCardOpen(final String cardId) {
            runOnUiThread(new Runnable() {
                public void run() {
                    if (cardId.equals(notificationCardSent)) {
                        View v = findViewById(R.id.relative_view);
                        v.setVisibility(View.GONE);
                        v = findViewById(R.id.hide1);
                        v.setVisibility(View.VISIBLE);
                        v = findViewById(R.id.drawing);
                        v.setVisibility(View.VISIBLE);
                        v = findViewById(R.id.hide2);
                        v.setVisibility(View.VISIBLE);
                        v = findViewById(R.id.select);
                        v.setVisibility(View.VISIBLE);
                        v = findViewById(R.id.upload_button);
                        v.setVisibility(View.VISIBLE);
                    }
                }
            });
        }

        /**
         * @see com.qualcomm.toq.smartwatch.api.v1.deckofcards.DeckOfCardsEventListener#onCardVisible(java.lang.String)
         */
        public void onCardVisible(final String cardId) {
            runOnUiThread(new Runnable() {
                public void run() {
                    // Do nothing
                }
            });
        }

        /**
         * @see com.qualcomm.toq.smartwatch.api.v1.deckofcards.DeckOfCardsEventListener#onCardInvisible(java.lang.String)
         */
        public void onCardInvisible(final String cardId) {
            runOnUiThread(new Runnable() {
                public void run() {
                    // Do nothing
                }
            });
        }

        /**
         * @see com.qualcomm.toq.smartwatch.api.v1.deckofcards.DeckOfCardsEventListener#onCardClosed(java.lang.String)
         */
        public void onCardClosed(final String cardId) {
            runOnUiThread(new Runnable() {
                public void run() {
                    // Do nothing
                }
            });
        }

        /**
         * @see com.qualcomm.toq.smartwatch.api.v1.deckofcards.DeckOfCardsEventListener#onMenuOptionSelected(java.lang.String, java.lang.String)
         */
        public void onMenuOptionSelected(final String cardId, final String menuOption) {
            runOnUiThread(new Runnable() {
                public void run() {
                    // Do nothing
                }
            });
        }

        /**
         * @see com.qualcomm.toq.smartwatch.api.v1.deckofcards.DeckOfCardsEventListener#onMenuOptionSelected(java.lang.String, java.lang.String, java.lang.String)
         */
        public void onMenuOptionSelected(final String cardId, final String menuOption, final String quickReplyOption) {
            runOnUiThread(new Runnable() {
                public void run() {
                    // Do nothing
                }
            });
        }

    }

    public void initLocation() {

        // Acquire a reference to the system LocationManager
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Set the destination to trigger Toq notification
        destination = new Location("Destination");
        destination.setLatitude(37.86965);
        destination.setLongitude(-122.25914);

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider
                float distance = location.distanceTo(destination);
                if (distance <= 50.00) {
                    sendNotification();
                }
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
                // Do nothing
            }

            @Override
            public void onProviderEnabled(String s) {
                // Do nothing
            }

            @Override
            public void onProviderDisabled(String s) {
                // Do nothing
            }
        };
        // Register the listener with the LocationManager to receive location updates
        if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            // Get update every 3 minutes
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 180000, 0, locationListener);
        }
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            // Get update every 3 minutes
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 180000, 0, locationListener);
        }
    }

    // Send notificationCard to prompt user to draw a random picture with DrawingView
    private void sendNotification() {
        String[] nameList = {"Art Goldberg", "Jack Weinberg", "Jackie Goldberg",
                          "Joan Baez", "Mario Savio", "Michael Rossman"};

        Random random = new Random();
        int randomCard = random.nextInt(nameList.length);

        // Initialize string reference for the cardId sent as a notification
        notificationCardSent = nameList[randomCard];

        String[] messageText = {notificationCardSent};

        NotificationTextCard notificationCard = new NotificationTextCard(System.currentTimeMillis(),
                "Draw Request", messageText);

        notificationCard.setVibeAlert(true);

        RemoteToqNotification notification = new RemoteToqNotification(this, notificationCard);

        try {
            deckOfCardsManager.sendNotification(notification);
        } catch (RemoteDeckOfCardsException e) {
            Toast.makeText(this, "Error sending notification", Toast.LENGTH_SHORT).show();
        }
    }

    // Selects a photo to add to drawing activity
    public void select(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        startActivityForResult(intent, 102);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 102) {

            if (resultCode == Activity.RESULT_OK) {
                Uri tempFileUrl = data.getData();
                String imagePath = getPath(tempFileUrl);
                fileUri = new File(imagePath);
            }
        }
    };

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};

        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    // Upload a drawing to Flickr with tag: cs160fsm
    public void pushFlickrImage(View view) {
        if (fileUri == null) {
            Toast.makeText(getApplicationContext(), "Please pick a photo", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(getApplicationContext(),
                    FlickrjActivity.class);
            intent.putExtra("flickImagePath", fileUri.getAbsolutePath());
            getFlickrImage();
            startActivity(intent);
        }

    }

    // Gets an image from Flickr to be added to the current remoteDeckOfCards
    private void getFlickrImage() {
        Thread thread = new Thread() {
        @Override
        public void run() {
                try {
                    String svr = "www.flickr.com";

                    REST rest = new REST();
                    rest.setHost(svr);

                    // Initialize Flickr object with key and REST
                    Flickr flickr = new Flickr(FlickrHelper.API_KEY, rest);

                    // Initialize SearchParameter object.
                    // This object stores the search keyword
                    SearchParameters searchParameters = new SearchParameters();
                    searchParameters.setSort(SearchParameters.INTERESTINGNESS_DESC);

                    // Create String[] for Flickr keyword tag
                    String[] tags = new String[]{"cs160fsm"};
                    searchParameters.setTags(tags);

                    // Initialize PhotoInterface object.
                    PhotosInterface photosInterface = flickr.getPhotosInterface();

                    // Execute search with specified tags
                    PhotoList photoList = photosInterface.search(searchParameters, 20, 1);

                    // Get search result and fetch the photo obj and get small square image's url
                    if (photoList != null) {
                        // Get search result and check the size of photo result
                        Random rand = new Random();
                        int seed = rand.nextInt(photoList.size());
                        // Get photo object
                        Photo photo = photoList.get(seed);
                        // Get small square url photo
                        InputStream inputStream = photo.getMediumAsStream();
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        // Initialize Card object to be added to Toq with the correct image dimensions
                        final CardImage cardImage = new CardImage("new card",  Bitmap.createScaledBitmap(bitmap, WIDTH, HEIGHT, false));

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Add a new card we downloaded from flickr to our remoteDeckOfCards
                                simpleTextCard = new SimpleTextCard("flickr.card");
                                simpleTextCard.setHeaderText("Flickr Card");
                                simpleTextCard.setCardImage(remoteResourceStore, cardImage);

                                listCard.add(simpleTextCard);
                                updateDeckOfCards();
                            }
                        });
                    }
                } catch (ParserConfigurationException e) {
                    e.printStackTrace();
                } catch (FlickrException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }

    // Update the deck of cards
    private void updateDeckOfCards() {

        try {
            deckOfCardsManager.updateDeckOfCards(remoteDeckOfCards, remoteResourceStore);
        }
        catch (RemoteDeckOfCardsException e) {
            Toast.makeText(this, "Error updating deck of cards", Toast.LENGTH_SHORT).show();
        }
    }

    public void initDrawing() {

        drawView = (DrawingView)findViewById(R.id.drawing);
        LinearLayout paintLayout = (LinearLayout) findViewById(R.id.paint_colors);

        currPaint = (ImageButton)paintLayout.getChildAt(0);
        currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));

        smallBrush = getResources().getInteger(R.integer.small_size);
        mediumBrush = getResources().getInteger(R.integer.medium_size);
        largeBrush = getResources().getInteger(R.integer.large_size);

        drawBtn = (ImageButton)findViewById(R.id.draw_btn);
        eraseBtn = (ImageButton)findViewById(R.id.erase_btn);
        newBtn = (ImageButton)findViewById(R.id.new_btn);
        saveBtn = (ImageButton)findViewById(R.id.save_btn);
    }

    // Choose brush size
    public void onDraw(View view) {
        final Dialog brushDialog = new Dialog(this);
        brushDialog.setTitle("Brush size:");
        brushDialog.setContentView(R.layout.brush_chooser);

        ImageButton smallBtn = (ImageButton)brushDialog.findViewById(R.id.small_brush);
        smallBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                drawView.setBrushSize(smallBrush);
                drawView.setLastBrushSize(smallBrush);
                drawView.setErase(false);
                brushDialog.dismiss();
            }
        });

        ImageButton mediumBtn = (ImageButton)brushDialog.findViewById(R.id.medium_brush);
        mediumBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                drawView.setBrushSize(mediumBrush);
                drawView.setLastBrushSize(mediumBrush);
                drawView.setErase(false);
                brushDialog.dismiss();
            }
        });

        ImageButton largeBtn = (ImageButton)brushDialog.findViewById(R.id.large_brush);
        largeBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                drawView.setBrushSize(largeBrush);
                drawView.setLastBrushSize(largeBrush);
                drawView.setErase(false);
                brushDialog.dismiss();
            }
        });

        brushDialog.show();
    }

    public void onErase(View view) {
        final Dialog brushDialog = new Dialog(this);
        brushDialog.setTitle("Eraser size:");
        brushDialog.setContentView(R.layout.brush_chooser);

        ImageButton smallBtn = (ImageButton)brushDialog.findViewById(R.id.small_brush);
        smallBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                drawView.setErase(true);
                drawView.setBrushSize(smallBrush);
                brushDialog.dismiss();
            }
        });
        ImageButton mediumBtn = (ImageButton)brushDialog.findViewById(R.id.medium_brush);
        mediumBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                drawView.setErase(true);
                drawView.setBrushSize(mediumBrush);
                brushDialog.dismiss();
            }
        });
        ImageButton largeBtn = (ImageButton)brushDialog.findViewById(R.id.large_brush);
        largeBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                drawView.setErase(true);
                drawView.setBrushSize(largeBrush);
                brushDialog.dismiss();
            }
        });
        brushDialog.show();
    }

    // Prompt user to start a new drawing before calling invalidate in DrawingView
    public void startNew(View view) {

        AlertDialog.Builder newDialog = new AlertDialog.Builder(this);
        newDialog.setTitle("New drawing");
        newDialog.setMessage("Start new drawing (you will lose the current drawing)?");
        newDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){
                drawView.startNew();
                dialog.dismiss();
            }
        });
        newDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){
                dialog.cancel();
            }
        });
        newDialog.show();
    }

    // Alert user if they want to save an image to the gallery then save and destroy it
    public void onSave(View view) {

        AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
        saveDialog.setTitle("Save drawing");
        saveDialog.setMessage("Save drawing to device Gallery?");
        saveDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){
                //save drawing
                drawView.setDrawingCacheEnabled(true);
                // Write image to file
                String imgSaved = MediaStore.Images.Media.insertImage(
                        getContentResolver(), drawView.getDrawingCache(),
                        UUID.randomUUID().toString()+".png", "drawing");

                if(imgSaved!=null){
                    Toast savedToast = Toast.makeText(getApplicationContext(),
                            "Drawing saved to Gallery!", Toast.LENGTH_SHORT);
                    savedToast.show();
                }
                else{
                    Toast unsavedToast = Toast.makeText(getApplicationContext(),
                            "Oops! Image could not be saved.", Toast.LENGTH_SHORT);
                    unsavedToast.show();
                }
                drawView.destroyDrawingCache();
            }
        });
        saveDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){
                dialog.cancel();
            }
        });
        saveDialog.show();
    }

    // Use chosen color
    public void paintClicked(View view) {
        drawView.setErase(false);
        if (view != currPaint) {
            // update color
            ImageButton imgView = (ImageButton) view;
            String color = view.getTag().toString();
            drawView.setColor(color);
            drawView.setBrushSize(drawView.getLastBrushSize());

            imgView.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
            currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint));
            currPaint=(ImageButton)view;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

}
