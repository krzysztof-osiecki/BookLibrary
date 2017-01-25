package cieslik.karolina.booklibrary.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import cieslik.karolina.booklibrary.R;


public class SplashWindow extends AppCompatActivity
{
    private static final int REQUEST_CODE_READ_CONTACTS = 1;
    private static final int SPLASH_TIME_OUT = 500;

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
            @Override
            public void run()
            {
                if (ContextCompat.checkSelfPermission(SplashWindow.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(SplashWindow.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                            REQUEST_CODE_READ_CONTACTS);

                    // REQUEST_CODE_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                } else
                {
                    Intent i = new Intent(SplashWindow.this, MainWindow.class);
                    startActivity(i);
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
            case REQUEST_CODE_READ_CONTACTS:
            {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED)
                {
                    Intent i = new Intent(SplashWindow.this, MainWindow.class);
                    startActivity(i);
                    // close this activity
                    finish();
                } else
                {
                    Log.i(this.getClass().getName(),
                            "Permission denied, boo! Disable the functionality that depends on this permission.");
                    finish();

                }
                return;
            }
            default:
                Log.i(this.getClass().getName(), "Unsupported request code: " + requestCode);
        }
    }
}
