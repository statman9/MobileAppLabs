package com.example.josh.lab2;

/**
 * Created by Josh on 9/30/2017.
 */

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class AccountSettings extends Activity {
    private static final String TAG = "Lab1";

    public final static String EXTRA_MESSAGE = "com.example.lab1.MESSAGE";

    // private static int IMG_RESULT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_settings);

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        if (prefs.contains("Name")) {
            RadioButton isMale = (RadioButton) findViewById(R.id.radio_male);
            RadioButton isFemale = (RadioButton) findViewById(R.id.radio_female);
            Boolean gender = isMale.isChecked();
            EditText name = (EditText) findViewById(R.id.name);
            EditText email = (EditText) findViewById(R.id.email);
            EditText schoolClass = (EditText) findViewById(R.id.school_class);
            EditText major = (EditText) findViewById(R.id.major);

            name.setText(prefs.getString("Name", "").toString());
            email.setText(prefs.getString("Email", "").toString());
            schoolClass.setText(prefs.getString("SchoolClass", "").toString());
            major.setText(prefs.getString("Major", "").toString());

            Boolean checkGender = prefs.getBoolean("Gender", true);

            if (checkGender) {
                isMale.setChecked(true);
            } else {
                isFemale.setChecked(true);
            }
        }

        /*String path = "";
        try {
            File f=new File(path, "profile.jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            ImageView img=(ImageView)findViewById(R.id.imageView);
            img.setImageBitmap(b);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /*public void onClick(View v) {
        Intent intent;
        intent = new Intent(Intent.ACTION_PICK,
             android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(intent, IMG_RESULT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {

            if (requestCode == IMG_RESULT && resultCode == RESULT_OK
                    && null != data) {


                Uri URI = data.getData();
                String[] FILE = { MediaStore.Images.Media.DATA };


                Cursor cursor = getContentResolver().query(URI,
                        FILE, null, null, null);

                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(FILE[0]);
                ImageDecode = cursor.getString(columnIndex);
                cursor.close();

                imageViewLoad.setImageBitmap(BitmapFactory
                        .decodeFile(ImageDecode));

            }
        } catch (Exception e) {
            Toast.makeText(this, "Please try again", Toast.LENGTH_LONG)
                    .show();
        }

    }*/


    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_male:
                if (checked)
                    break;
            case R.id.radio_female:
                if (checked)
                    break;
        }
    }

    /** Called when the user clicks the Send button */

    public void saveContent(View view) {

        // Send the input string to the DisplayMessageActivity using an intent

        Log.d(TAG, "saveContent");

        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        SharedPreferences.Editor editor = prefs.edit();

        RadioButton isMale = (RadioButton) findViewById(R.id.radio_male);
        Boolean gender = isMale.isChecked();
        EditText name = (EditText) findViewById(R.id.name);
        EditText email = (EditText) findViewById(R.id.email);
        EditText schoolClass = (EditText) findViewById(R.id.school_class);
        EditText major = (EditText) findViewById(R.id.major);

        editor.putString("Name", name.getText().toString());
        editor.putString("Email", email.getText().toString());
        editor.putString("SchoolClass", schoolClass.getText().toString());
        editor.putString("Major", major.getText().toString());
        editor.putBoolean("Gender", gender);
        editor.apply();
        editor.commit();
    }
}
