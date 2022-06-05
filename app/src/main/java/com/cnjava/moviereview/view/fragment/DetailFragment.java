package com.cnjava.moviereview.view.fragment;

import static com.cnjava.moviereview.util.NumberUtils.convertDateType3;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.cnjava.moviereview.R;
import com.cnjava.moviereview.databinding.FragmentDetailBinding;
import com.cnjava.moviereview.model.Actor;
import com.cnjava.moviereview.model.Collection;
import com.cnjava.moviereview.model.Movie;
import com.cnjava.moviereview.model.MovieDetail;
import com.cnjava.moviereview.model.Review;
import com.cnjava.moviereview.model.Video;
import com.cnjava.moviereview.util.Constants;
import com.cnjava.moviereview.util.NumberUtils;
import com.cnjava.moviereview.util.ViewUtils;
import com.cnjava.moviereview.view.adapter.CastAdapter;
import com.cnjava.moviereview.view.adapter.CollectionAdapter;
import com.cnjava.moviereview.view.adapter.GenresAdapter;
import com.cnjava.moviereview.view.adapter.MovieAdapter;
import com.cnjava.moviereview.view.adapter.PopularAdapter;
import com.cnjava.moviereview.view.adapter.ReviewAdapter;
import com.cnjava.moviereview.view.adapter.VideoAdapter;
import com.cnjava.moviereview.viewmodel.CommonViewModel;

import java.util.ArrayList;
import java.util.List;

public class DetailFragment extends BaseFragment<FragmentDetailBinding, CommonViewModel> implements PopularAdapter.MovieCallBack, VideoAdapter.VideoCallBack {

    public static final String TAG = DetailFragment.class.getName();

    private Object mData;
    private MovieDetail movieDetail;

    @Override
    protected Class<CommonViewModel> getClassVM() {
        return CommonViewModel.class;
    }

    @Override
    protected void initViews() {

        ViewUtils.show(binding.progressCircular);
        ViewUtils.gone(binding.layoutMovieDetail);
        int id = (int) mData;
        viewModel.getMovieDetail(id);
        viewModel.getCast(id);
        viewModel.getRecommendation(id);
        viewModel.getVideo(id);

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.ivBack.startAnimation(AnimationUtils.loadAnimation(context, androidx.appcompat.R.anim.abc_fade_in));
                callBack.backToPrev();
            }
        });

        binding.btAddReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.btAddReview.startAnimation(AnimationUtils.loadAnimation(context, androidx.appcompat.R.anim.abc_popup_exit));
                callBack.showFragment(AddReviewFragment.TAG, null, true, Constants.ANIM_FADE);
            }
        });

        binding.tvReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.tvReview.startAnimation(AnimationUtils.loadAnimation(context, androidx.appcompat.R.anim.abc_fade_in));
                callBack.showFragment(ReviewFragment.TAG, movieDetail, true, Constants.ANIM_SLIDE);
            }
        });

        List<Review> reviewList = new ArrayList<>();

        reviewList.add(new Review(1, 1, "Mai Phước Lợi", getResources().getString(R.string.content), 7.5, "May 4, 2022", 123, "Hello", "https://image.tmdb.org/t/p/w1280/fBEucxECxGLKVHBznO0qHtCGiMO.jpg"));
        reviewList.add(new Review(1, 1, "Mai Phước Lợi", "Great", 7.5, "May 4, 2022", 123, "Hello", "https://image.tmdb.org/t/p/w1280/fBEucxECxGLKVHBznO0qHtCGiMO.jpg"));

        ReviewAdapter reviewAdapter = new ReviewAdapter(context, reviewList);
        binding.rvReview.setAdapter(reviewAdapter);


    }

    @Override
    protected FragmentDetailBinding initViewBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return FragmentDetailBinding.inflate(inflater, container, false);
    }

    @Override
    public void apiSuccess(String key, Object data) {
        if (key.equals(Constants.KEY_GET_MOVIE_DETAIL)) {
            movieDetail = (MovieDetail) data;
            if (movieDetail.collection != null) {
                viewModel.getCollection(movieDetail.collection.id);
            }
            List<String> listGenres = new ArrayList<>();
            if (movieDetail.title != null) {
                binding.tvName.setText(movieDetail.title);
            }

            binding.tvRuntime.setText(String.format(movieDetail.runtime + " min"));

            binding.tvRating.setText(String.valueOf(movieDetail.voteAverage));
            if (movieDetail.overview != null) {
                binding.tvOverview.setText(movieDetail.overview);
            }
            binding.tvReleaseDate.setText(convertDateType3(movieDetail.releaseDate));
            binding.tvRateCount.setText(String.valueOf(movieDetail.voteCount));
            binding.tvPopularity.setText(String.valueOf(movieDetail.popularity));
            Glide.with(context)
                    .load(String.format(Constants.IMAGE_URL + movieDetail.backdropPath))
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .placeholder(R.drawable.ic_image)
                    .into(binding.ivCover);
            Glide.with(context)
                    .load(String.format(Constants.IMAGE_URL + movieDetail.posterPath))
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .placeholder(R.drawable.ic_movie)
                    .into(binding.ivPoster);
            if (movieDetail.budget == 0) {
                binding.tvBudget.setText(R.string.undefined);
            } else {
                binding.tvBudget.setText(NumberUtils.convertCurrency(movieDetail.budget));
            }
            if (movieDetail.revenue == 0) {
                binding.tvRevenue.setText(R.string.undefined);
            } else {
                binding.tvRevenue.setText(NumberUtils.convertCurrency(movieDetail.revenue));
            }

            for (MovieDetail.Genres item : movieDetail.genres) {
                listGenres.add(item.name);
            }

            binding.ivMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popup = new PopupMenu(context, binding.ivMore);
                    //Inflating the Popup using xml file
                    popup.getMenuInflater().inflate(R.menu.poupup_menu, popup.getMenu());

                    //registering popup with OnMenuItemClickListener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            Toast.makeText(context, "You Clicked : " + item.getItemId(), Toast.LENGTH_SHORT).show();
                            if (item.getTitle().toString().equals("Homepage")) {
                                if (!movieDetail.homepage.equals("")) {
                                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(movieDetail.homepage));
                                    startActivity(browserIntent);
                                }
                            } else if (item.getTitle().toString().equals("Facebook")) {
                                try {
                                    context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
                                    String url = "";
                                    String FACEBOOK_URL = "https://www.facebook.com/aseanfootball";
                                    String FACEBOOK_PAGE_ID = "aseanfootball";
                                    int versionCode = context.getPackageManager().getPackageInfo("com.facebook.katana", 0).versionCode;
                                    if (versionCode >= 3002850) { //newer versions of fb app
                                        url = "fb://facewebmodal/f?href=" + FACEBOOK_URL;
                                    } else { //older versions of fb app
                                        url = "fb://page/" + FACEBOOK_PAGE_ID;
                                    }
                                    Intent facebookIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                    startActivity(facebookIntent);
                                } catch (Exception e) {
                                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/aseanfootball"));
                                    startActivity(browserIntent);
                                }
                            }
                            return true;
                        }
                    });

                    popup.show();
                }
            });

            binding.rvGenres.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            GenresAdapter adapterGenres = new GenresAdapter(context, listGenres);
            binding.rvGenres.setAdapter(adapterGenres);

        } else if (key.equals(Constants.KEY_GET_CAST)) {
            Actor actor = (Actor) data;
            binding.rvCast.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            CastAdapter castAdapter = new CastAdapter(context, actor);
            binding.rvCast.setAdapter(castAdapter);

        } else if (key.equals(Constants.KEY_GET_RECOMMENDATION)) {
            ViewUtils.gone(binding.progressCircular);
            ViewUtils.show(binding.layoutMovieDetail);
            Movie movie = (Movie) data;
            if (movie.results != null) {
                if (movie.results.size() == 0) {
                    binding.tvNoRecommend.setText(String.format("We don't have enough data to suggest any movies. You can help by rating movies you've seen."));
                    binding.tvNoRecommend.setVisibility(View.VISIBLE);

                } else {
                    MovieAdapter adapter = new MovieAdapter(context, movie, this);
                    binding.rvRecommend.setAdapter(adapter);
                }
            }
        } else if (key.equals(Constants.KEY_GET_COLLECTION)) {
            Collection collection = (Collection) data;
            ViewUtils.show(binding.tvCollection);
            ViewUtils.show(binding.rvCollection);
            binding.tvCollection.setText(collection.name);
            binding.rvCollection.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            CollectionAdapter adapter = new CollectionAdapter(context, collection, this);
            binding.rvCollection.setAdapter(adapter);
        } else if (key.equals(Constants.KEY_GET_VIDEO)) {
            Video video = (Video) data;
            Log.d(TAG, "KEY_GET_VIDEO: ");
            if (video.results != null) {
                ViewUtils.show(binding.tvVideo);
                ViewUtils.show(binding.rvVideo);
                binding.rvVideo.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                VideoAdapter adapter = new VideoAdapter(context, video, this);
                binding.rvVideo.setAdapter(adapter);
            }
        }
    }

    @Override
    public void apiError(String key, int code, Object data) {
        if (code == 999) {
            Log.d(TAG, "apiError: " + data.toString());
            Toast.makeText(context, "Unable connect to server", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void setData(Object data) {
        this.mData = data;
    }

    @Override
    public void gotoMovieDetail(int id) {
        callBack.showFragment(DetailFragment.TAG, id, true, Constants.ANIM_SLIDE);
    }

    @Override
    public void gotoVideoYoutube(String key) {
        String urlYoutube = "https://www.youtube.com/watch?v=";
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlYoutube + key));
        startActivity(browserIntent);
    }
}
