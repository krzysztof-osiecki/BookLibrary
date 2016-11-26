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
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import cieslik.karolina.booklibrary.R;
import cieslik.karolina.booklibrary.utils.DeviceUtils;
import cieslik.karolina.booklibrary.utils.StringLiterals;

import static cieslik.karolina.booklibrary.ui.MainWindow.mDB;

/**
 * Created by Karolina on 24.11.2016.
 */

public class BookListFragment extends Fragment implements View.OnClickListener
{
    public static final String TAG = "BookListFragment";

    private AlbumsAdapter mAdapter;
    private List<Book> mAlbumList;
    private RecyclerView mRecyclerView;

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

        mAlbumList = new ArrayList<>();
        mAlbumList = mDB.getAllBooks();
        mAdapter = new AlbumsAdapter(getActivity(), mAlbumList);
        mAdapter.setOnClickListener(this);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(2, DeviceUtils.dpToPx(getActivity(), 10), true));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);

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
                fragmentTransaction.add(R.id.content_main, fragment);
                fragmentTransaction.addToBackStack(AddBookFragment.TAG);
                fragmentTransaction.commit();
            }
        });

        try
        {
            Glide.with(this).load(R.drawable.cover).into((ImageView) view.findViewById(R.id.backdrop));
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        inflater.inflate(R.menu.main_options_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onClick(View v)
    {
        int position = mRecyclerView.getChildAdapterPosition(v);
        Book item = mAdapter.getItem(position);

        FragmentManager fragmentManager = getActivity().getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        BookDetailsFragment detailsFragment = BookDetailsFragment.newInstance(item);
        transaction.replace(R.id.content_main, detailsFragment);
        transaction.addToBackStack(BookDetailsFragment.TAG);
        transaction.commit();
    }
}
