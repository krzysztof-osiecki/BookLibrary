package cieslik.karolina.booklibrary.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import cieslik.karolina.booklibrary.R;

/**
 * Created by Karolina on 19.11.2016.
 */

public class SplashWindow extends AppCompatActivity
{
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView splashImageView = (ImageView) findViewById(R.id.splash_image_view);

        try
        {
            Glide.with(this).load(R.drawable.splash).into(splashImageView);
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        new Handler().postDelayed(new Runnable()
        {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run()
            {
                // Here, thisActivity is the current activity
                if (ContextCompat.checkSelfPermission(SplashWindow.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED)
                {

                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(SplashWindow.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE))
                    {

                        // Show an explanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.

                    } else
                    {

                        // No explanation needed, we can request the permission.

                        ActivityCompat.requestPermissions(SplashWindow.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.
                    }
                } else
                {
                    Intent i = new Intent(SplashWindow.this, MainWindow.class);
                    startActivity(i);

                    // close this activity
                    finish();
                }
            }
        }, SPLASH_TIME_OUT);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {
        switch (requestCode)
        {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS:
            {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {

                    Intent i = new Intent(SplashWindow.this, MainWindow.class);
                    startActivity(i);

                    // close this activity
                    finish();

                } else
                {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }
}
