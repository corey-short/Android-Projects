package prov2.flip;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;


public class MainActivity extends Activity {

    private static final int REQUEST_ENABLE_BT = 1;
    private static final int RESULT = 1;
    ImageButton connectBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ActionBar actionBar = getActionBar();
        assert actionBar != null;
        actionBar.hide();

        connectBtn = (ImageButton) findViewById(R.id.connect_button);

    }

    public void connectBLE(View view) {

        Intent intent = new Intent(this, DeviceScanActivity.class);
        startActivityForResult(intent, RESULT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == RESULT) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                setContentView(R.layout.activity_about2);
            }
        }
    }
}
