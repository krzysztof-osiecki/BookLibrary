package cieslik.karolina.booklibrary.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import cieslik.karolina.booklibrary.R;

public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.MyViewHolder>
{
    private Context mContext;
    private List<Book> albumList;
    private View.OnClickListener mOnClickListener;

    public AlbumsAdapter(Context mContext, List<Book> albumList)
    {
        this.mContext = mContext;
        this.albumList = albumList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.album_card, parent, false);
        itemView.setOnClickListener(mOnClickListener);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position)
    {
        Book album = albumList.get(position);
        holder.title.setText(album.getTitle());
        holder.count.setText(album.getAuthor() + " songs");

        if (album.getThumbnail() != 0)
        {
            Glide.with(mContext).load(album.getThumbnail()).into(holder.thumbnail);
        }
    }

    public void setOnClickListener(View.OnClickListener mOnClickListener)
    {
        this.mOnClickListener = mOnClickListener;
    }

    public Book getItem(int aPosition)
    {
        return albumList.get(aPosition);
    }

    @Override
    public int getItemCount()
    {
        return albumList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public TextView title, count;
        public ImageView thumbnail, overflow;

        public MyViewHolder(View view)
        {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            count = (TextView) view.findViewById(R.id.count);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            overflow = (ImageView) view.findViewById(R.id.overflow);
        }
    }
}
