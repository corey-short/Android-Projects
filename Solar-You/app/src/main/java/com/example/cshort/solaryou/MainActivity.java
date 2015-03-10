package com.example.cshort.solaryou;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

public class MainActivity extends Activity {

    EditText inputUserAge;
    TextView mercury;
    TextView venus;
    TextView earth;
    TextView mars;
    TextView jupiter;
    TextView saturn;
    TextView uranus;
    TextView neptune;
    protected String ageInput;
    protected int userAge;
    protected double mercuryAge;
    protected double venusAge;
    protected double earthAge;
    protected double marsAge;
    protected double jupiterAge;
    protected double saturnAge;
    protected double uranusAge;
    protected double neptuneAge;
    /* All values in earth years*/
    protected final double MERCURY = 4.152569349704411;
    protected final double VENUS = 1.6256898700373865;
    protected final double EARTH = 1.0; // age
    protected final double MARS = 0.5317049027599859;
    protected final double JUPITER = 11.862;
    protected final double SATURN = 29.456;
    protected final double URANUS = 84.07;
    protected final double NEPTUNE = 164.81;

    /**
     * Creates the TextView objects in order to setText()
     * when calculateAge() is called.
     * @param savedInstanceState
     * @author Corey Short
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getActionBar().hide();

        mercury = (TextView) findViewById(R.id.mercury);
        venus = (TextView) findViewById(R.id.venus);
        earth = (TextView) findViewById(R.id.earth);
        mars = (TextView) findViewById(R.id.mars);
        jupiter = (TextView) findViewById(R.id.jupiter);
        saturn = (TextView) findViewById(R.id.saturn);
        uranus = (TextView) findViewById(R.id.uranus);
        neptune = (TextView) findViewById(R.id.neptune);

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


    /**
     * Handles the onClick events for the Calculate and
     * I'm Feeling Lucky Buttons.
     * @param v - the view from the onClick event
     * @author Corey Short
     */
    public void onClick(View v) {

        inputUserAge = ((EditText) findViewById(R.id.user_age));
        ageInput = inputUserAge.getText().toString();
        if (ageInput.matches("")) {
            Toast.makeText(this, "You did not enter your age", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            userAge = Integer.parseInt(ageInput);
            calculateAge(userAge);
        }
    }

    /**
     * Generates a random numbee for userAge.
     * @param v - the view from the onClick event
     * @author Corey Short
     */
    public void onLucky(View v) {

        Random rand = new Random();
        userAge = rand.nextInt(100);
        EditText updateField = (EditText) findViewById(R.id.user_age);
        updateField.setText(Integer.toString(userAge));
        calculateAge(userAge);

    }

    /**
     * Calculates the age to all other planets from Earth.
     * @param userAge - age entered by the user
     * @author Corey Short
     */
    public void calculateAge(int userAge) {

        mercuryAge = userAge * MERCURY;
        BigDecimal me = new BigDecimal(mercuryAge);
        me = me.setScale(2, RoundingMode.CEILING);
        mercury.setText(me.toString());

        venusAge = userAge * VENUS;
        BigDecimal ve = new BigDecimal(venusAge);
        ve = ve.setScale(2, RoundingMode.CEILING);
        venus.setText(ve.toString());

        earthAge = userAge * EARTH;
        BigDecimal ea = new BigDecimal(earthAge);
        ea = ea.setScale(2, RoundingMode.CEILING);
        earth.setText(ea.toString());

        marsAge = userAge * MARS;
        BigDecimal ma = new BigDecimal(marsAge);
        ma = ma.setScale(2, RoundingMode.CEILING);
        mars.setText(ma.toString());

        jupiterAge = userAge / JUPITER;
        BigDecimal ju = new BigDecimal(jupiterAge);
        ju = ju.setScale(2, RoundingMode.CEILING);
        jupiter.setText(ju.toString());

        saturnAge = userAge / SATURN;
        BigDecimal sa = new BigDecimal(saturnAge);
        sa = sa.setScale(2, RoundingMode.CEILING);
        saturn.setText(sa.toString());

        uranusAge = userAge / URANUS;
        BigDecimal ur = new BigDecimal(uranusAge);
        ur = ur.setScale(2, RoundingMode.CEILING);
        uranus.setText(ur.toString());

        neptuneAge = userAge / NEPTUNE;
        BigDecimal ne = new BigDecimal(neptuneAge);
        ne = ne.setScale(2, RoundingMode.CEILING);
        neptune.setText(ne.toString());

    }

}
