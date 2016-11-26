package cieslik.karolina.booklibrary.ui;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import cieslik.karolina.booklibrary.R;
import cieslik.karolina.booklibrary.utils.StringLiterals;

/**
 * Created by Karolina on 24.11.2016.
 */

public class BookDetailsFragment extends Fragment
{
    public static final String TAG = "BookDetailsFragment";
    private Book mBook;

    public static BookDetailsFragment newInstance(Book aBook)
    {
        BookDetailsFragment fragment = new BookDetailsFragment();
        fragment.mBook = aBook;
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
        View view = inflater.inflate(R.layout.book_details, container, false);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        final CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(StringLiterals.SPACE);
        AppBarLayout appBarLayout = (AppBarLayout) view.findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        try
        {
            Glide.with(this).load(R.drawable.cover).into((ImageView) view.findViewById(R.id.backdrop));
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        fillView(view);

        return view;
    }

    private void fillView(View aView)
    {
        LinearLayout titleView = (LinearLayout) aView.findViewById(R.id.title_view);
        fillItem(titleView, "Tytuł", mBook.getTitle());

        LinearLayout authorView = (LinearLayout) aView.findViewById(R.id.author_view);
        fillItem(authorView, "Autor", mBook.getAuthor());

        LinearLayout publisherView = (LinearLayout) aView.findViewById(R.id.publisher_view);
        fillItem(publisherView, "Wydawnictwo", mBook.getPublisher());

        LinearLayout publishingDateView = (LinearLayout) aView.findViewById(R.id.publishing_date_view);
        fillItem(publishingDateView, "Data wydania", mBook.getPublishingYear());

    }

    private void fillItem(LinearLayout layout, String aLabel, String aValue)
    {
        TextView authorLabel = (TextView) layout.findViewById(R.id.label);
        authorLabel.setText(aLabel);
        TextView authorValue = (TextView) layout.findViewById(R.id.value);
        authorValue.setText(aValue);
    }
}
