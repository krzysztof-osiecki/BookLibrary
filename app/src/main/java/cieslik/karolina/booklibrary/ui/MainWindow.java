package cieslik.karolina.booklibrary.ui;

import android.Manifest;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import cieslik.karolina.booklibrary.R;
import cieslik.karolina.booklibrary.database.Item;
import cieslik.karolina.booklibrary.database.ItemList;
import cieslik.karolina.booklibrary.database.MainDatabase;
import cieslik.karolina.booklibrary.database.VolumeInfo;
import cieslik.karolina.booklibrary.utils.StringLiterals;
import cieslik.karolina.booklibrary.utils.TargetPhoneGallery;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainWindow extends AppCompatActivity {
  private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
  public static MainDatabase mDB;
  public static Map<String, String> COVERS_CACHE = new HashMap<>();
  public static final String API_KEY = "AIzaSyASa1J-rRBBkmjghGXzsoTzCt4T_3-j-10";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    mDB = new MainDatabase(this);
    mDB.open();

    openFragment();
  }

  private void openFragment() {
    FragmentManager fragmentManager = getFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    BookListFragment fragment = new BookListFragment();
    fragmentTransaction.add(R.id.content_main, fragment);
    fragmentTransaction.addToBackStack(BookListFragment.TAG);
    fragmentTransaction.commit();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.barcode_scanner:
        openBarcodeScanner();
        return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onBackPressed() {
    FragmentManager fragmentManager = getFragmentManager();
    int backStackEntryCount = fragmentManager.getBackStackEntryCount();

    if (backStackEntryCount > 1) {
      fragmentManager.popBackStack();
    } else {
      super.onBackPressed();
    }
  }

  private void openBarcodeScanner() {
    if (ContextCompat.checkSelfPermission(MainWindow.this,
        Manifest.permission.CAMERA)
        != PackageManager.PERMISSION_GRANTED) {
      if (ActivityCompat.shouldShowRequestPermissionRationale(MainWindow.this,
          Manifest.permission.CAMERA)) {

        // Show an explanation to the user *asynchronously* -- don't block
        // this thread waiting for the user's response! After the user
        // sees the explanation, try again to request the permission.

      } else {
        ActivityCompat.requestPermissions(MainWindow.this,
            new String[]{Manifest.permission.CAMERA},
            MY_PERMISSIONS_REQUEST_READ_CONTACTS);
      }
    } else {
      new IntentIntegrator(this).initiateScan();
    }
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
    switch (requestCode) {
      case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
        if (grantResults.length > 0
            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

          new IntentIntegrator(this).initiateScan();

        } else {
          //TODO
        }
        return;
      }
    }
  }


  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
    if (result != null) {
      if (result.getContents() == null) {
        Toast.makeText(this, getText(R.string.CODE_NOT_SCANNED), Toast.LENGTH_LONG).show();
      } else {
        Book book = mDB.selectBook(result.getContents());
        if (book == null) {
          BookReaderProgress bookReaderProgress = new BookReaderProgress(result.getContents());
          bookReaderProgress.execute();
        } else {
          Toast.makeText(this, getText(R.string.BOOK_EXISTS), Toast.LENGTH_LONG).show();
        }
      }
    }
  }

  public class BookReaderProgress extends AsyncTask<String, Object, ItemList> {
    String isbnCode;

    BookReaderProgress(String isbnCode) {
      this.isbnCode = isbnCode;
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected ItemList doInBackground(String... isbns) {
      if (isCancelled()) {
        return null;
      }
      Book book = mDB.getBook(isbnCode);

      if (book != null) {
        return null;
      }

      String apiUrlString = "https://www.googleapis.com/books/v1/volumes?q=" + isbnCode +
          "&fields=items(volumeInfo(authors,description,imageLinks,pageCount,publishedDate,publisher,subtitle,title))";
      try {
        HttpURLConnection connection = null;
        try {
          URL url = new URL(apiUrlString);
          connection = (HttpURLConnection) url.openConnection();
          connection.setRequestMethod("GET");
          connection.setReadTimeout(5000);
          connection.setConnectTimeout(5000);
        } catch (MalformedURLException | ProtocolException e) {
          Log.w("BookLibrary", "Request to google api was incorrect", e);
          return null;
        }

        int responseCode = connection.getResponseCode();
        if (responseCode != 200) {
          Log.w(getClass().getName(), "GoogleBooksAPI request failed. Response Code: " + responseCode);
          connection.disconnect();
          return null;
        }

        StringBuilder builder = new StringBuilder();
        BufferedReader responseReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line = responseReader.readLine();
        while (line != null) {
          builder.append(line);
          line = responseReader.readLine();
        }
        String responseString = builder.toString();

        Gson gson = new GsonBuilder().create();
        ItemList item = gson.fromJson(responseString, ItemList.class);
        connection.disconnect();
        return item;
      } catch (SocketTimeoutException e) {
        Log.w(getClass().getName(), "Connection timed out. Returning null", e);
        return null;
      } catch (IOException e) {
        Log.d(getClass().getName(), "IOException when connecting to Google Books API.", e);
        return null;
      }
    }

    @Override
    protected void onPostExecute(ItemList itemList) {
      Book book;

      if (itemList != null && itemList.getItems().size() > 0) {
        List<Item> items = itemList.getItems();
        VolumeInfo volumeInfo = items.get(0).getVolumeInfo();

        List<String> authors = volumeInfo.getAuthors();
        String authorsString = StringLiterals.EMPTY_STRING;
        for (String author : authors) {
          authorsString += author + " ";
        }

        if (volumeInfo.getImageLinks() != null) {
          String thumbnail =
              volumeInfo.getImageLinks().getThumbnail() != null ? volumeInfo.getImageLinks().getThumbnail() :
                  volumeInfo.getImageLinks().getSmallThumbnail();
          if (thumbnail != null) {
            Picasso.with(getApplicationContext()).load(thumbnail)
                .into(new TargetPhoneGallery(getContentResolver(), isbnCode));
          }

        }

        book = new Book(isbnCode, volumeInfo.getTitle(), authorsString, volumeInfo.getPublisher(),
            volumeInfo.getPublishedDate());
      } else {
        book = new Book(isbnCode);
      }

      FragmentManager fragmentManager = getFragmentManager();
      FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
      AddBookFragment fragment = AddBookFragment.newInstance(book);
      fragmentTransaction.add(R.id.main_content, fragment);
      fragmentTransaction.addToBackStack(AddBookFragment.TAG);
      fragmentTransaction.commit();
    }
  }
}
