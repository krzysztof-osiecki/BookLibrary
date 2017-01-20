package cieslik.karolina.booklibrary.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import cieslik.karolina.booklibrary.R;

class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.MyViewHolder> implements Filterable
{
    interface onViewClickListener
    {
        void onOptionsClickListener(View view, int position);
    }

    private Context context;
    private List<Book> bookList;
    private BookFilter bookFilter;
    private View.OnClickListener onClickListener;
    private onViewClickListener onViewClickListener;

    BooksAdapter(Context context, List<Book> bookList)
    {
        this.context = context;
        this.bookList = bookList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_card, parent, false);
        itemView.setOnClickListener(onClickListener);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position)
    {
        final Book book = bookList.get(position);
        holder.title.setText(book.getTitle());
        holder.author.setText(book.getAuthor());
        holder.options.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onViewClickListener.onOptionsClickListener(v, holder.getAdapterPosition());
            }
        });

        try
        {
            Glide.with(holder.thumbnail.getContext()).load(book.getCover()).into(holder.thumbnail);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        String rate = book.getRate() != 0 ? String.valueOf(book.getRate()) : "?";
        holder.rating.setText(rate);
    }

    void setOnClickListener(View.OnClickListener mOnClickListener)
    {
        this.onClickListener = mOnClickListener;
    }

    public void setOnViewClickListener(BooksAdapter.onViewClickListener onViewClickListener)
    {
        this.onViewClickListener = onViewClickListener;
    }

    Book getItem(int aPosition)
    {
        return bookList.get(aPosition);
    }

    @Override
    public int getItemCount()
    {
        return bookList.size();
    }

    @Override
    public Filter getFilter()
    {
        if (bookFilter == null)
            bookFilter = new BookFilter(this, bookList);
        return bookFilter;
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView title, author, rating;
        ImageView thumbnail, options;

        MyViewHolder(View view)
        {
            super(view);
            title = (TextView) view.findViewById(R.id.card_view_title);
            author = (TextView) view.findViewById(R.id.card_view_author);
            thumbnail = (ImageView) view.findViewById(R.id.card_view_thumbnail);
            options = (ImageView) view.findViewById(R.id.card_view_options);
            rating = (TextView) view.findViewById(R.id.card_view_rating);
        }
    }

    private static class BookFilter extends Filter
    {
        private final BooksAdapter adapter;
        private final List<Book> originalList;
        private final List<Book> filteredList;

        private BookFilter(BooksAdapter adapter, List<Book> originalList)
        {
            super();
            this.adapter = adapter;
            this.originalList = new LinkedList<>(originalList);
            this.filteredList = new ArrayList<>();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint)
        {
            filteredList.clear();
            final FilterResults results = new FilterResults();

            if (constraint.length() == 0)
            {
                filteredList.addAll(originalList);
            } else
            {
                final String filterPattern = constraint.toString().toLowerCase().trim();

                for (final Book book : originalList)
                {
                    if (contains(book, filterPattern))
                    {
                        filteredList.add(book);
                    }
                }
            }
            results.values = filteredList;
            results.count = filteredList.size();
            return results;
        }

        private boolean contains(Book book, String filterPattern)
        {
            return book.getTitle().toLowerCase().startsWith(filterPattern) ||
                    book.getAuthor().toLowerCase().startsWith(filterPattern) ||
                    book.getPublisher().toLowerCase().startsWith(filterPattern);
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results)
        {
            adapter.bookList.clear();
            adapter.bookList.addAll((ArrayList<Book>) results.values);
            adapter.notifyDataSetChanged();
        }
    }
}
