package com.udacity.nanodegree.popularmovies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;
import com.udacity.nanodegree.popularmovies.R;
import com.udacity.nanodegree.popularmovies.activity.MainActivity;
import com.udacity.nanodegree.popularmovies.utils.CommonUtils;
import com.udacity.nanodegree.popularmovies.utils.Logger;
import com.udacity.nanodegree.popularmovies.webservice.entity.MoviesResponse;
import com.udacity.nanodegree.popularmovies.webservice.entity.Result;

import java.util.List;

public class HomeMovieAdapter extends RecyclerView.Adapter<HomeMovieAdapter.ViewHolder> {
    private MoviesResponse moviesResponse;
    private Context mContext;
    int width, height;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView posterImage;

        public ViewHolder(View v) {
            super(v);
            posterImage = (ImageView) v.findViewById(R.id.imgPoster);
        }
    }

    public HomeMovieAdapter(MoviesResponse response, Context context) {
        moviesResponse = response;
        mContext = context;
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        width = metrics.widthPixels;
        height = metrics.heightPixels;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public HomeMovieAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        // create a new view
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gridview_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        final ViewHolder vh = new ViewHolder(view);
        ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    int viewWidth = view.getWidth();

                    vh.posterImage.setLayoutParams(new
                            LinearLayout.LayoutParams(viewWidth, (int) (viewWidth * 1.5f)));
                    view.requestLayout();

                    Logger.i("Brijesh", "Called Resize " + vh.getAdapterPosition());
                }
            });
        }
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final List<Result> resultList = moviesResponse.getResults();
        final Result result = resultList.get(position);
        String posterPath = CommonUtils.getImageURL(result.getPosterPath());
        Picasso.with(mContext).load(posterPath).error(R.drawable.ic_poster_placeholder)
                .placeholder(R.drawable.ic_poster_placeholder).into(holder.posterImage);

        holder.posterImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) mContext).showDetailFragment(result, holder.posterImage);
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if (moviesResponse != null)
            return moviesResponse.getResults().size();
        else
            return 0;
    }
}