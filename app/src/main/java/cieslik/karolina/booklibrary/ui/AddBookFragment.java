package cieslik.karolina.booklibrary.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RatingBar;

import java.util.List;

import cieslik.karolina.booklibrary.R;
import cieslik.karolina.booklibrary.database.Item;
import cieslik.karolina.booklibrary.database.ItemList;
import cieslik.karolina.booklibrary.database.VolumeInfo;
import cieslik.karolina.booklibrary.utils.StringLiterals;

public class AddBookFragment extends Fragment
{
    public static final String TAG = "AddBookFragment";

    EditText mTitle;
    EditText mAuthor;
    EditText mIsbn;
    EditText mPublisher;
    EditText mPublishingYear;
    EditText mNotes;
    RatingBar mRatingBar;
    TextInputLayout mTitleLayout;
    TextInputLayout mAuthorLayout;

    ItemList mItem;
    String mIsbnCode;

    public static AddBookFragment newInstance(ItemList aItem, String aIsbn)
    {
        AddBookFragment fragment = new AddBookFragment();
        fragment.mItem = aItem;
        fragment.mIsbnCode = aIsbn;
        return fragment;
    }

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
        View view = inflater.inflate(R.layout.simple_activity, container, false);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        mTitle = (EditText) view.findViewById(R.id.input_title);
        mAuthor = (EditText) view.findViewById(R.id.input_author);
        mIsbn = (EditText) view.findViewById(R.id.input_isbn);
        mPublisher = (EditText) view.findViewById(R.id.input_publisher);
        mPublishingYear = (EditText) view.findViewById(R.id.input_publishing_year);
        mNotes = (EditText) view.findViewById(R.id.input_notes);
        mRatingBar = (RatingBar) view.findViewById(R.id.rating_bar);
        mTitleLayout = (TextInputLayout) view.findViewById(R.id.input_layout_title);
        mAuthorLayout = (TextInputLayout) view.findViewById(R.id.input_layout_author);
        mTitle.addTextChangedListener(new MyTextWatcher(mTitle));
        mAuthor.addTextChangedListener(new MyTextWatcher(mAuthor));

        if (mItem != null)
        {
            List<Item> items = mItem.getItems();
            VolumeInfo volumeInfo = items.get(0).getVolumeInfo();

            List<String> authors = volumeInfo.getAuthors();
            String authorsString = StringLiterals.EMPTY_STRING;
            for (String author : authors)
            {
                authorsString += author + " ,";
            }
            mAuthor.setText(authorsString);

            mTitle.setText(volumeInfo.getTitle());
            mPublisher.setText(volumeInfo.getPublisher());
            mPublishingYear.setText(volumeInfo.getPublishedDate());
            mIsbn.setText(mIsbnCode);
        }

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        menu.clear();
        inflater.inflate(R.menu.add_book_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.save:
                saveData();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private boolean validateTitle()
    {
        if (mTitle.getText().toString().trim().isEmpty())
        {
            mTitleLayout.setError(getString(R.string.err_title_msg));
            requestFocus(mTitle);
            return false;
        } else
        {
            mTitleLayout.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateAuthor()
    {
        if (mAuthor.getText().toString().trim().isEmpty())
        {
            mAuthorLayout.setError(getString(R.string.err_author_msg));
            requestFocus(mAuthor);
            return false;
        } else
        {
            mAuthorLayout.setErrorEnabled(false);
        }

        return true;
    }

    private void requestFocus(View view)
    {
        if (view.requestFocus())
        {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private class MyTextWatcher implements TextWatcher
    {
        private View view;

        private MyTextWatcher(View view)
        {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
        {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
        {
        }

        public void afterTextChanged(Editable editable)
        {
            switch (view.getId())
            {
                case R.id.input_title:
                    validateTitle();
                    break;
                case R.id.input_author:
                    validateAuthor();
                    break;
            }
        }

    }

    private void saveData()
    {
        if (!validateTitle())
        {
            return;
        }

        if (!validateAuthor())
        {
            return;
        }

        String titleText = mTitle.getText().toString();
        String authorText = mAuthor.getText().toString();
        String isbnText = mIsbn.getText().toString();
        String publisherText = mPublisher.getText().toString();
        String publishingYearText = mPublishingYear.getText().toString();

        String notesText = mNotes.getText().toString();
        int rate = (int) mRatingBar.getRating();

        MainWindow.mDB.insertBook(titleText, authorText, isbnText, publisherText, publishingYearText, notesText,
                StringLiterals.EMPTY_STRING, rate);

        getFragmentManager().popBackStack();
    }
}
