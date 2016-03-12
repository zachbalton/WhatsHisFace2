package com.skylarkingstudios.whatshisface;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;
import com.skylarkingstudios.whatshisface.model.MovieList;
import com.skylarkingstudios.whatshisface.model.MovieResults;
import com.skylarkingstudios.whatshisface.model.Movie;

public class FindActorListFragment extends Fragment {

    private RecyclerView mMovieRecyclerView;
    private MovieAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list_actor_find, container, false);

        mMovieRecyclerView = (RecyclerView) v.findViewById(R.id.fragment_list_actor_find_recycler_view);
        mMovieRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return v;
    }

    public void updateUI() {
        MovieList movieList = MovieList.get(getActivity());
        List<Movie> movies = movieList.getMovies();

        if (mAdapter == null) {
            mAdapter = new MovieAdapter(movies);
            mMovieRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
    }


    private class MovieHolder extends RecyclerView.ViewHolder {

        private TextView mTitleTextView;
        private TextView mReleaseDateTextView;
        private Button mRemoveButton;
        private Movie mMovie;

        public MovieHolder(View itemView) {
            super(itemView);

            mTitleTextView = (TextView) itemView.findViewById(R.id.list_item_movie_title);
            mReleaseDateTextView = (TextView) itemView.findViewById(R.id.list_item_movie_release_date);
            mRemoveButton = (Button) itemView.findViewById(R.id.list_item_movie_remove_button);
            mRemoveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MovieList.get(getActivity()).removeMovie(mMovie);
                    updateUI();
                }
            });
        }

        public void bindMovie(Movie movie) {
            mMovie = movie;
            mTitleTextView.setText(mMovie.getTitle());
            mReleaseDateTextView.setText(mMovie.getFormattedReleaseDate());
        }
    }


    private class MovieAdapter extends RecyclerView.Adapter<MovieHolder> {

        private List<Movie> mMovies;

        public MovieAdapter(List<Movie> movies) {
            mMovies = movies;
        }

        @Override
        public MovieHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.list_item_movie, parent, false);
            return new MovieHolder(view);
        }

        @Override
        public void onBindViewHolder(MovieHolder holder, int position) {
            Movie movie = mMovies.get(position);
            holder.bindMovie(movie);
        }

        @Override
        public int getItemCount() {
            return mMovies.size();
        }
    }

}
