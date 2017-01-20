package cieslik.karolina.booklibrary.ui;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cieslik.karolina.booklibrary.R;
import cieslik.karolina.booklibrary.utils.DeviceUtils;
import cieslik.karolina.booklibrary.utils.StringLiterals;

import static android.app.Activity.RESULT_OK;
import static cieslik.karolina.booklibrary.ui.MainWindow.mDB;

/**
 * Created by Karolina on 24.11.2016.
 */

public class BookListFragment extends Fragment implements View.OnClickListener, BooksAdapter.onViewClickListener
{
    public static final String TAG = "BookListFragment";
    private static final int REQUEST_CODE_TAKE_PICTURE = 1;
    private static final int REQUEST_CODE_SELECT_PICTURE = 100;
    private static final int REQUEST_CODE_READ_CONTACTS = 2;

    private BooksAdapter adapter;
    private List<Book> bookList;
    private RecyclerView recyclerView;
    private EditText searchView;

    private Book selectedBook;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.content_main, container, false);

        bookList = new ArrayList<>();
        bookList = mDB.getAllBooks();
        adapter = new BooksAdapter(getActivity(), bookList);
        adapter.setOnClickListener(this);
        adapter.setOnViewClickListener(this);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, DeviceUtils.dpToPx(getActivity(), 10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        searchView = (EditText) view.findViewById(R.id.search_view);
        searchView.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                adapter.getFilter().filter(searchView.getText().toString());
            }
        });

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        final CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(StringLiterals.SPACE);
        AppBarLayout appBarLayout = (AppBarLayout) view.findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        final FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                AddBookFragment fragment = new AddBookFragment();
                fragmentTransaction.add(R.id.main_content, fragment);
                fragmentTransaction.addToBackStack(AddBookFragment.TAG);
                fragmentTransaction.commit();
            }
        });

        initCollapsingToolbar(view);
        try
        {
            Glide.with(this).load(R.drawable.backdrop).into((ImageView) view.findViewById(R.id.backdrop));
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return view;
    }

    private void initCollapsingToolbar(View view)
    {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) view.findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener()
        {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset)
            {
                if (scrollRange == -1)
                {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0)
                {
                    collapsingToolbar.setTitle(getString(R.string.app_name));
                    isShow = true;
                } else if (isShow)
                {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.main_options_menu, menu);
    }

    @Override
    public void onClick(View v)
    {
        int position = recyclerView.getChildAdapterPosition(v);
        Book item = adapter.getItem(position);

        FragmentManager fragmentManager = getActivity().getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        BookDetailsFragment detailsFragment = BookDetailsFragment.newInstance(item);
        transaction.replace(R.id.content_main, detailsFragment);
        transaction.addToBackStack(BookDetailsFragment.TAG);
        transaction.commit();
    }

    public void refreshView()
    {
        bookList = mDB.getAllBooks();
        adapter = new BooksAdapter(getActivity(), bookList);
        adapter.setOnClickListener(this);
        adapter.setOnViewClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onOptionsClickListener(View view, int position)
    {
        PopupMenu popup = new PopupMenu(getActivity(), view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_book, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener(adapter.getItem(position)));
        popup.show();
    }

    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener
    {

        Book book;

        MyMenuItemClickListener(Book book)
        {
            this.book = book;
        }


        @Override
        public boolean onMenuItemClick(MenuItem menuItem)
        {
            switch (menuItem.getItemId())
            {
                case R.id.action_delete:
                    mDB.deleteBook(book.getIsbn());
                    refreshView();
                    return true;
                case R.id.action_edit:
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    AddBookFragment fragment = AddBookFragment.newInstance(book);
                    fragmentTransaction.add(R.id.main_content, fragment);
                    fragmentTransaction.addToBackStack(AddBookFragment.TAG);
                    fragmentTransaction.commit();
                    return true;
                case R.id.action_add_cover:
                    selectedBook = book;
                    showDialog();
                    return true;
                default:
            }
            return false;
        }

    }

    private void showDialog()
    {
        CharSequence options[] = new CharSequence[]{"galeria", "aparat"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Wybierz opcję");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                switch (which)
                {
                    case 0:
                        openImageChooser();
                        break;
                    case 1:
                        tryOpenCamera();
                        break;
                }
            }
        });
        builder.show();
    }

    private void tryOpenCamera()
    {
        if (ContextCompat.checkSelfPermission(this.getActivity(),
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this.getActivity(),
                    new String[]{Manifest.permission.CAMERA},
                    REQUEST_CODE_READ_CONTACTS);

        } else
        {
            openCamera();
        }
    }

    private void openCamera()
    {
        try
        {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File imageFile = createImageFile();
            if (imageFile != null)
            {
                Uri uriForFile = Uri.fromFile(imageFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, uriForFile);
            }
            startActivityForResult(intent, REQUEST_CODE_TAKE_PICTURE);
        } catch (Throwable t)
        {
            Log.i("Book", t.getMessage());
        }
    }

    /* Choose an image from Gallery */
    void openImageChooser()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_CODE_SELECT_PICTURE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        switch (requestCode)
        {
            case REQUEST_CODE_READ_CONTACTS:
            {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    openCamera();

                } else
                {
                    //TODO pokaz dialog ze nie przyznal
                }
                return;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode == RESULT_OK)
        {
            switch (requestCode)
            {
                case REQUEST_CODE_SELECT_PICTURE:
                    // Get the url from data
                    Uri selectedImageUri = data.getData();
                    if (null != selectedImageUri)
                    {
                        // Get the path from the Uri
                        String path = getPathFromURI(selectedImageUri);
                        Log.i(TAG, "Image Path : " + path);
                        // Set the image in ImageView
                        selectedBook.setCover(selectedImageUri.toString());
                        mDB.updateBook(selectedBook);
                        refreshView();
                    }
                    break;
                case REQUEST_CODE_TAKE_PICTURE:
                    refreshView();
                    break;
            }
        }
    }


    private File createImageFile()
    {
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        try
        {
            File image = File.createTempFile(selectedBook.getIsbn(), ".jpg", storageDir);
            String filePath = image.getAbsolutePath();
            selectedBook.setCover(filePath);
            mDB.updateBook(selectedBook);
            return image;
        } catch (Exception e)
        {
            Log.i("1", e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /* Get the real path from the URI */
    public String getPathFromURI(Uri contentUri)
    {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst())
        {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }
}
