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

import cieslik.karolina.booklibrary.R;
import cieslik.karolina.booklibrary.utils.DeviceUtils;
import cieslik.karolina.booklibrary.utils.StringLiterals;

public class AddBookFragment extends Fragment
{
    public static final String TAG = "AddBookFragment";

    EditText title;
    EditText author;
    EditText isbn;
    EditText publisher;
    EditText publishingYear;
    EditText notes;
    RatingBar ratingBar;
    TextInputLayout titleLayout;
    TextInputLayout authorLayout;
    TextInputLayout isbnLayout;

    Book book;

    public static AddBookFragment newInstance(Book book)
    {
        AddBookFragment fragment = new AddBookFragment();
        fragment.book = book;
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

        title = (EditText) view.findViewById(R.id.input_title);
        author = (EditText) view.findViewById(R.id.input_author);
        isbn = (EditText) view.findViewById(R.id.input_isbn);
        publisher = (EditText) view.findViewById(R.id.input_publisher);
        publishingYear = (EditText) view.findViewById(R.id.input_publishing_year);
        notes = (EditText) view.findViewById(R.id.input_notes);
        ratingBar = (RatingBar) view.findViewById(R.id.rating_layout);
        titleLayout = (TextInputLayout) view.findViewById(R.id.input_layout_title);
        authorLayout = (TextInputLayout) view.findViewById(R.id.input_layout_author);
        isbnLayout = (TextInputLayout) view.findViewById(R.id.input_layout_isbn);
        title.addTextChangedListener(new MyTextWatcher(title));
        author.addTextChangedListener(new MyTextWatcher(author));
        isbn.addTextChangedListener(new MyTextWatcher(isbn));

        if (book != null)
        {
            author.setText(book.getAuthor());
            title.setText(book.getTitle());
            publisher.setText(book.getPublisher());
            publishingYear.setText(book.getPublishingYear());
            isbn.setText(book.getIsbn());
            ratingBar.setRating(book.getRate());
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
        if (title.getText().toString().trim().isEmpty())
        {
            titleLayout.setError(getString(R.string.err_title_msg));
            requestFocus(title);
            return false;
        } else
        {
            titleLayout.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateAuthor()
    {
        if (author.getText().toString().trim().isEmpty())
        {
            authorLayout.setError(getString(R.string.err_author_msg));
            requestFocus(author);
            return false;
        } else
        {
            authorLayout.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateIsbn()
    {
        if (isbn.getText().toString().trim().isEmpty())
        {
            isbnLayout.setError(getString(R.string.err_isbn_msg));
            requestFocus(isbn);
            return false;
        } else
        {
            isbnLayout.setErrorEnabled(false);
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
                case R.id.input_isbn:
                    validateIsbn();
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

        if (!validateIsbn())
        {
            return;
        }

        String titleText = title.getText().toString();
        String authorText = author.getText().toString();
        String isbnText = isbn.getText().toString();
        String publisherText = publisher.getText().toString();
        String publishingYearText = publishingYear.getText().toString();
        int rating = (int) ratingBar.getRating();

        String notesText = notes.getText().toString();

        Book book = MainWindow.mDB.selectBook(isbnText);
        if (book == null)
        {
            MainWindow.mDB.insertBook(isbnText, titleText, authorText, publisherText, publishingYearText, notesText,
                    StringLiterals.EMPTY_STRING, rating);
        } else
        {
            MainWindow.mDB.updateBook(isbnText, titleText, authorText, publisherText, publishingYearText, notesText,
                    book.getCover(), rating);
        }

        DeviceUtils.hideInputKeyboard(getView(), getActivity());

        Fragment fragment = getFragmentManager().findFragmentById(R.id.content_main);
        if (fragment instanceof BookListFragment)
        {
            BookListFragment bookListFragment = (BookListFragment) fragment;
            bookListFragment.refreshView();
        }
        getFragmentManager().popBackStack();
    }
}
