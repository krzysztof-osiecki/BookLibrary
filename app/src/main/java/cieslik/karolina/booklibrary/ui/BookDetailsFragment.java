package cieslik.karolina.booklibrary.ui;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
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
            Glide.with(this).load(mBook.getCover()).into((ImageView) view.findViewById(R.id.backdrop));
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        final FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                AddBookFragment fragment = AddBookFragment.newInstance(mBook);
                fragmentTransaction.add(R.id.main_content, fragment);
                fragmentTransaction.addToBackStack(AddBookFragment.TAG);
                fragmentTransaction.commit();
            }
        });

        initCollapsingToolbar(view);
        fillView(view);

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

    private void fillView(View aView)
    {
        LinearLayout titleView = (LinearLayout) aView.findViewById(R.id.title_view);
        fillItem(titleView, getString(R.string.TITLE), mBook.getTitle());

        LinearLayout authorView = (LinearLayout) aView.findViewById(R.id.author_view);
        fillItem(authorView, getString(R.string.AUTHOR), mBook.getAuthor());

        LinearLayout publisherView = (LinearLayout) aView.findViewById(R.id.publisher_view);
        fillItem(publisherView, getString(R.string.PUBLISHER), mBook.getPublisher());

        LinearLayout publishingDateView = (LinearLayout) aView.findViewById(R.id.publishing_date_view);
        fillItem(publishingDateView, getString(R.string.PUBLISHING_DATE), mBook.getPublishingYear());

        LinearLayout notesView = (LinearLayout) aView.findViewById(R.id.notes_view);
        fillItem(notesView, getString(R.string.NOTES), mBook.getNotes());
    }

    private void fillItem(LinearLayout layout, String aLabel, String aValue)
    {
        TextView authorLabel = (TextView) layout.findViewById(R.id.label);
        authorLabel.setText(aLabel);
        TextView authorValue = (TextView) layout.findViewById(R.id.value);
        authorValue.setText(aValue);
    }
}
