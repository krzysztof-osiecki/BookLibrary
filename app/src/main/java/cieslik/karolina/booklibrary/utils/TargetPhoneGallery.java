package cieslik.karolina.booklibrary.utils;

import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.provider.MediaStore;
import android.util.Log;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.lang.ref.WeakReference;

import static cieslik.karolina.booklibrary.ui.MainWindow.COVERS_CACHE;

public class TargetPhoneGallery implements Target {
  private final WeakReference<ContentResolver> resolver;
  private final String isbn;

  public TargetPhoneGallery(ContentResolver r, String isbn) {
    this.resolver = new WeakReference<>(r);
    this.isbn = isbn;
  }

  @Override
  public void onPrepareLoad(Drawable arg0) {
  }

  @Override
  public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom arg1) {
    ContentResolver r = resolver.get();
    if (r != null) {
      String url = MediaStore.Images.Media.insertImage(r, bitmap, isbn, "");
      COVERS_CACHE.put(isbn, url);
    }
  }

  @Override
  public void onBitmapFailed(Drawable arg0) {
    Log.i("BookLibrary", "Failed to download book cover for isbn: " + isbn);
  }
}
