package com.cnjava.moviereview.view.fragment.detailmovie;

import static com.cnjava.moviereview.util.NumberUtils.convertDateType3;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.cnjava.moviereview.MyApplication;
import com.cnjava.moviereview.R;
import com.cnjava.moviereview.Storage;
import com.cnjava.moviereview.databinding.FragmentDetailMovieBinding;
import com.cnjava.moviereview.model.Actor;
import com.cnjava.moviereview.model.Collection;
import com.cnjava.moviereview.model.Favorite;
import com.cnjava.moviereview.model.Movie;
import com.cnjava.moviereview.model.MovieDetail;
import com.cnjava.moviereview.model.Review;
import com.cnjava.moviereview.model.Social;
import com.cnjava.moviereview.model.User;
import com.cnjava.moviereview.model.Video;
import com.cnjava.moviereview.util.CommonUtils;
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
import com.cnjava.moviereview.view.fragment.BaseFragment;
import com.cnjava.moviereview.view.fragment.addreview.AddReviewFragment;
import com.cnjava.moviereview.view.fragment.cast.CastFragment;
import com.cnjava.moviereview.view.fragment.editreview.EditReviewFragment;
import com.cnjava.moviereview.view.fragment.login.LoginFragment;
import com.cnjava.moviereview.view.fragment.personal.PersonalFragment;
import com.cnjava.moviereview.view.fragment.register.RegisterFragment;
import com.cnjava.moviereview.view.fragment.review.ReviewFragment;
import com.cnjava.moviereview.view.fragment.reviewdetail.ReviewDetailFragment;
import com.cnjava.moviereview.view.fragment.trailer.VideoFragment;
import com.google.android.material.appbar.AppBarLayout;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class DetailMovieFragment extends BaseFragment<FragmentDetailMovieBinding, DetailViewModel> implements PopularAdapter.MovieCallBack, VideoAdapter.VideoCallBack, ReviewAdapter.ReviewCallBack, CastAdapter.CastCallBack {

    public static final String TAG = DetailMovieFragment.class.getName();
    private final Storage storage = MyApplication.getInstance().getStorage();
    private Object mData;
    private int movieId;
    private boolean isSelect = false;
    private static final String FACEBOOK = "Facebook";
    private static final String TWITTER = "Twitter";
    private static final String HOMEPAGE = "Homepage";
    private VideoAdapter videoAdapter;
    private MovieAdapter movieAdapter;
    private CastAdapter castAdapter;
    private ReviewAdapter reviewAdapter;


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    protected Class<DetailViewModel> getClassVM() {
        return DetailViewModel.class;
    }

    @Override
    protected FragmentDetailMovieBinding initViewBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return FragmentDetailMovieBinding.inflate(inflater, container, false);
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    protected void initViews() {
        movieId = (int) mData;
        ((AppCompatActivity) requireActivity()).setSupportActionBar(binding.toolbar1);
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        binding.collapsingToolbarLayout1.setTitleEnabled(false);
        viewModel.getLiveDataIsLoading().observe(this, this::setScreenLoading);
        viewModel.getMovieDetail(movieId);
        viewModel.movieDetailLD().observe(this, _movieDetail -> {
            initMovieDetail(_movieDetail);
            viewModel.getSocial(movieId);
            viewModel.socialLD().observe(this, _social -> {
                if (_social != null)
                    initBindingButtonFeature(_movieDetail, _social);
            });
            if (_movieDetail.collection != null) {
                viewModel.getCollection(_movieDetail.collection.id);
                viewModel.collectionLD().observe(this, this::initCollectionMovie);
            }
            binding.viewDetailMovie.textRatingReview.setOnClickListener(view -> callBack.replaceFragment(ReviewFragment.TAG, _movieDetail, true, Constants.ANIM_SLIDE));
        });
        if (CommonUtils.getInstance().getPref(Constants.ACCESS_TOKEN) != null) {
            viewModel.getYourProfile(CommonUtils.getInstance().getPref(Constants.ACCESS_TOKEN));
            viewModel.yourProfileLD().observe(this, _user -> {

            });

            viewModel.getMyFavorite(CommonUtils.getInstance().getPref(Constants.ACCESS_TOKEN));
            viewModel.myFavoriteLD().observe(this, _favorites -> initFavorite(_favorites, String.valueOf(movieId)));
        }
        viewModel.getCast(movieId);
        viewModel.actorLD().observe(this, this::initActorList);
        viewModel.getRecommendation(movieId);
        viewModel.recommendationLD().observe(this, this::initRecommendMovie);
        viewModel.getVideo(movieId);
        viewModel.videoLD().observe(this, this::initVideoTrailer);
        viewModel.getReviewByMovieId(String.valueOf(movieId));
        viewModel.movieReviewLD().observe(this, _reviews -> {
            storage.reviewList = _reviews;
            if (CommonUtils.getInstance().getPref(Constants.ACCESS_TOKEN) != null) {
                viewModel.getYourProfile(CommonUtils.getInstance().getPref(Constants.ACCESS_TOKEN));
                viewModel.yourProfileLD().observe(this, _user -> {
                    initSomeReview(_reviews, _user);
                });
            } else {
                initSomeReview(_reviews, null);
            }
        });

        binding.toolbar1.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.backToPrev();
            }
        });

    }

    private void initSomeReview(List<Review> reviews, User user) {
        Review myReview = null;
        if (user != null) {
            Optional<Review> resultReview = reviews.stream().filter(obj -> obj.user.getId().equals(user.getId())).findFirst();
            if (resultReview.isPresent()) {
                myReview = resultReview.get();
            }
        }
        initReview(reviews, myReview, user);
    }

    private void initCollectionMovie(Collection collection) {
        ViewUtils.show(binding.viewDetailMovie.tvCollectionDv);
        ViewUtils.show(binding.viewDetailMovie.rvCollectionDv);
        binding.viewDetailMovie.tvCollectionDv.setText(collection.name);
        binding.viewDetailMovie.rvCollectionDv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        CollectionAdapter adapter = new CollectionAdapter(context, collection, this);
        binding.viewDetailMovie.rvCollectionDv.setAdapter(adapter);
    }

    private void initVideoTrailer(Video video) {
        if (video.results.size() > 0) {
            ViewUtils.show(binding.viewDetailMovie.tvVideoDv);
            ViewUtils.show(binding.viewDetailMovie.rvVideoDv);
            binding.viewDetailMovie.rvVideoDv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            videoAdapter = new VideoAdapter(context, video, this);
            binding.viewDetailMovie.rvVideoDv.setAdapter(videoAdapter);
        } else {
            ViewUtils.gone(binding.viewDetailMovie.tvVideoDv);
        }
    }

    private void initRecommendMovie(Movie recommendation) {
        if (recommendation.results != null) {
            if (recommendation.results.size() == 0) {
                binding.viewDetailMovie.tvNoRecommend.setText(R.string.no_recommend);
                binding.viewDetailMovie.tvNoRecommend.setVisibility(View.VISIBLE);

            } else {
                movieAdapter = new MovieAdapter(context, recommendation, this);
                binding.viewDetailMovie.rvRecommendDv.setAdapter(movieAdapter);
            }
        }
    }

    private void initActorList(Actor actor) {
        if (actor.cast.size() > 0) {
            binding.viewDetailMovie.rvCastDv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            castAdapter = new CastAdapter(context, actor, this);
            binding.viewDetailMovie.rvCastDv.setAdapter(castAdapter);
        } else {
            ViewUtils.gone(binding.viewDetailMovie.tvCast);
        }
    }


    private void initBindingButtonFeature(MovieDetail detail, Social social) {

        /*binding.ivBackDv.setOnClickListener(view -> {
            binding.ivBackDv.startAnimation(AnimationUtils.loadAnimation(context, androidx.appcompat.R.anim.abc_fade_in));
            callBack.backToPrev();
        });*/

        binding.viewDetailMovie.btAddReviewDv.setOnClickListener(view -> {
            if (CommonUtils.getInstance().getPref(Constants.ACCESS_TOKEN) != null) {
                callBack.replaceFragment(AddReviewFragment.TAG, detail, true, Constants.ANIM_FADE);
            } else {
                showLoginDialog();
            }
        });

        binding.viewDetailHeaderMovie.layoutFavorite.setOnClickListener(view -> {
            if (CommonUtils.getInstance().getPref(Constants.ACCESS_TOKEN) != null) {
                if (!isSelect) {
                    binding.viewDetailHeaderMovie.ivFavorite.setColorFilter(ContextCompat.getColor(context, R.color.primary));
                    binding.viewDetailHeaderMovie.tvFavorite.setTextColor(ContextCompat.getColor(context, R.color.primary));
                    Favorite.MovieFavorite movieFavorite = new Favorite.MovieFavorite(detail.posterPath,
                            String.valueOf(detail.id),
                            detail.title, detail.overview,
                            detail.releaseDate,
                            detail.voteAverage
                    );
                    viewModel.addFavorite(movieFavorite, CommonUtils.getInstance().getPref(Constants.ACCESS_TOKEN));
                    isSelect = true;
                } else {
                    binding.viewDetailHeaderMovie.ivFavorite.setColorFilter(ContextCompat.getColor(context, R.color.light_white));
                    binding.viewDetailHeaderMovie.tvFavorite.setTextColor(ContextCompat.getColor(context, R.color.light_white));
                    viewModel.deleteFavorite(String.valueOf(movieId), CommonUtils.getInstance().getPref(Constants.ACCESS_TOKEN));
                    isSelect = false;
                }
            } else {
                showLoginDialog();
            }

        });

        binding.viewDetailHeaderMovie.layoutMore.setOnClickListener(view -> popupMenuMore(detail, social));
    }

    private void initMovieDetail(MovieDetail detail) {
        storage.movieDetail = detail;

        binding.appBarLayout1.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    binding.toolbar1.setTitle(detail.title);
                    isShow = true;
                } else if (isShow) {
                    binding.toolbar1.setTitle(" ");//careful there should a space between double quote otherwise it wont work
                    isShow = false;
                }
            }
        });

        List<String> listGenres = new ArrayList<>();
        for (MovieDetail.Genres item : detail.genres) {
            listGenres.add(item.name);
        }
        binding.viewDetailMovie.tvStatus.setText(detail.status);
        binding.viewDetailHeaderMovie.tvName.setText(Objects.requireNonNull(detail.title));
        binding.viewDetailHeaderMovie.tvRuntime.setText(detail.runtime + " min");
        binding.viewDetailHeaderMovie.tvRating.setText(new DecimalFormat("##.##").format(detail.voteAverage));
        if (detail.overview != null) {
            binding.viewDetailMovie.tvOverview.setText(detail.overview);
        } else {
            binding.viewDetailMovie.tvTitleOverview.setVisibility(View.GONE);
        }
        binding.viewDetailMovie.tvReleaseDate.setText(convertDateType3(detail.releaseDate));
        binding.viewDetailMovie.tvRateCount.setText(String.valueOf(detail.voteCount));
        binding.viewDetailMovie.tvPopularity.setText(String.valueOf(detail.popularity));
        Glide.with(context)
                .load(Constants.IMAGE_URL + detail.backdropPath)
                .into(binding.viewDetailHeaderMovie.ivCover);
        Glide.with(context)
                .load(Constants.IMAGE_URL + detail.posterPath)
                .placeholder(R.drawable.progress_animation)
                .error(R.drawable.ic_movie2)
                .into(binding.viewDetailHeaderMovie.ivPoster);
        if (detail.budget == 0) {
            binding.viewDetailMovie.tvBudget.setText(R.string.undefined);
        } else {
            binding.viewDetailMovie.tvBudget.setText(NumberUtils.convertCurrency(detail.budget));
        }
        if (detail.revenue == 0) {
            binding.viewDetailMovie.tvRevenue.setText(R.string.undefined);
        } else {
            binding.viewDetailMovie.tvRevenue.setText(NumberUtils.convertCurrency(detail.revenue));
        }
        binding.viewDetailHeaderMovie.rvGenres.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        GenresAdapter adapterGenres = new GenresAdapter(context, listGenres);
        binding.viewDetailHeaderMovie.rvGenres.setAdapter(adapterGenres);
    }

    private void popupMenuMore(MovieDetail detail, Social social) {
        PopupMenu popup = new PopupMenu(context, binding.viewDetailHeaderMovie.layoutMore);
        popup.getMenuInflater().inflate(R.menu.poupup_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(item -> {
            if (item.getTitle().toString().equals(HOMEPAGE)) {
                if (!detail.homepage.equals("")) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(detail.homepage));
                    startActivity(browserIntent);
                } else {
                    Toast.makeText(context, "No information", Toast.LENGTH_SHORT).show();
                }
            } else if (item.getTitle().toString().equals(FACEBOOK)) {
                if (social.facebookId != null) {
                    String fbId = social.facebookId;
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/" + fbId));
                    startActivity(browserIntent);
                } else {
                    Toast.makeText(context, "No information", Toast.LENGTH_SHORT).show();
                }
            } else if (item.getTitle().toString().equals(TWITTER)) {
                if (social.twitterId != null) {
                    String twId = social.twitterId;
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/" + twId));
                    startActivity(browserIntent);
                } else {
                    Toast.makeText(context, "No information", Toast.LENGTH_SHORT).show();
                }
            }
            return true;
        });
        popup.show();
    }

    @Override
    public void apiSuccess(String key, Object data) {

    }

    private void initFavorite(List<Favorite> favoriteList, String movieId) {
        Optional<Favorite> result = favoriteList.stream().filter(obj -> obj.movie.id.equals(movieId)).findFirst();
        if (result.isPresent()) {
            binding.viewDetailHeaderMovie.ivFavorite.setColorFilter(ContextCompat.getColor(context, R.color.primary));
            binding.viewDetailHeaderMovie.tvFavorite.setTextColor(ContextCompat.getColor(context, R.color.primary));
            isSelect = true;
        }
    }

    private void initReview(List<Review> listReview, Review myReview, User user) {
        if (listReview != null && listReview.size() > 0) {
            if (myReview != null) {
                initYourReview(myReview, user);
            } else {
                ViewUtils.show(binding.viewDetailMovie.btAddReviewDv);
                ViewUtils.gone(binding.viewDetailMovie.layoutYourReviewDv);
                ViewUtils.gone(binding.viewDetailMovie.tvTitleYourReviewDv);
            }

            List<Review> threeFirstReview;
            if (user != null) {
                threeFirstReview = listReview.stream().filter(obj -> !obj.user.getId().equals(user.getId())).limit(3).collect(Collectors.toList());
            } else {
                threeFirstReview = listReview.stream().limit(3).collect(Collectors.toList());
            }
            reviewAdapter = new ReviewAdapter(context, threeFirstReview, this);
            binding.viewDetailMovie.rvReview.setAdapter(reviewAdapter);

        } else {
            binding.viewDetailMovie.rvReview.setVisibility(View.GONE);
            binding.viewDetailMovie.tvNoReviews.setVisibility(View.VISIBLE);
            binding.viewDetailMovie.textRatingReview.setVisibility(View.GONE);
            binding.viewDetailMovie.tvTitleYourReviewDv.setVisibility(View.GONE);
            binding.viewDetailMovie.layoutYourReviewDv.setVisibility(View.GONE);
        }
    }

    public void initYourReview(Review review, User user) {
        ViewUtils.gone(binding.viewDetailMovie.btAddReviewDv);
        ViewUtils.show(binding.viewDetailMovie.layoutYourReviewDv);
        ViewUtils.show(binding.viewDetailMovie.tvTitleYourReviewDv);
        Glide.with(context)
                .load(review.user.getAvatar())
                .placeholder(R.drawable.progress_animation)
                .error(R.drawable.img_default_avt)
                .into(binding.viewDetailMovie.ivAvt);
        binding.viewDetailMovie.tvNameYourReview.setText(review.user.getName());
        binding.viewDetailMovie.tvDate.setText(NumberUtils.convertDateType7(review.createdAt));
        binding.viewDetailMovie.tvContent.setText(review.content);
        binding.viewDetailMovie.tvRate.setText(String.valueOf((int) review.rating));
        if (!review.isDislike) {
            for (String yourLike : review.like) {
                if (yourLike.equals(user.getId())) {
                    review.isLike = true;
                    binding.viewDetailMovie.ivLike.setImageResource(R.drawable.ic_choose_like);
                    break;
                }
            }
        }
        if (!review.isLike) {
            for (String yourDislike : review.dislike) {
                if (yourDislike.equals(user.getId())) {
                    review.isDislike = true;
                    binding.viewDetailMovie.ivDislike.setImageResource(R.drawable.ic_choose_dislike);
                    break;
                }
            }
        }
        if (review.like != null) {
            binding.viewDetailMovie.tvLike.setText(String.valueOf(review.like.size()));
        }
        if (review.dislike != null) {
            binding.viewDetailMovie.tvDislike.setText(String.valueOf(review.dislike.size()));
        }
        if (review.user.getId().equals(user.getId())) {
            binding.viewDetailMovie.ivMoreAction.setOnClickListener(view -> {
                PopupMenu popup = new PopupMenu(context, binding.viewDetailMovie.ivMoreAction);
                popup.getMenuInflater().inflate(R.menu.review_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(item -> {
                    if (item.getTitle().toString().equals("Edit")) {
                        callBack.replaceFragment(EditReviewFragment.TAG, review, true, Constants.ANIM_SLIDE);
                    } else if (item.getTitle().toString().equals("Delete")) {
                        viewModel.deleteReview(review.id, CommonUtils.getInstance().getPref(Constants.ACCESS_TOKEN));
                        ViewUtils.show(binding.viewDetailMovie.btAddReviewDv);
                        ViewUtils.gone(binding.viewDetailMovie.layoutYourReviewDv);
                        ViewUtils.gone(binding.viewDetailMovie.tvTitleYourReviewDv);
                    }
                    return true;
                });
                popup.show();
            });
        }

        binding.viewDetailMovie.layoutLike.setOnClickListener(view -> {
            viewModel.likeReview(review.id, CommonUtils.getInstance().getPref(Constants.ACCESS_TOKEN));
            likeButtonEvent(review, user);
        });

        binding.viewDetailMovie.layoutDislike.setOnClickListener(view -> {
            viewModel.dislikeReview(review.id, CommonUtils.getInstance().getPref(Constants.ACCESS_TOKEN));
            disLikeButtonEvent(review, user);
        });

        binding.viewDetailMovie.ivAvt.setOnClickListener(view -> {
            //callBack.gotoUserReview(review.user);
        });

        binding.viewDetailHeaderMovie.tvName.setOnClickListener(view -> {
            //callBack.gotoUserReview(review.user);
        });
        if (review.content.length() > 150) {
            binding.viewDetailMovie.tvShowMore.setOnClickListener(view -> {
                callBack.replaceFragment(ReviewDetailFragment.TAG, review, true, Constants.ANIM_SLIDE);
            });
        } else {
            binding.viewDetailMovie.tvShowMore.setVisibility(View.GONE);
        }
        binding.viewDetailMovie.tvContent.setOnClickListener(view -> {
            callBack.replaceFragment(ReviewDetailFragment.TAG, review, true, Constants.ANIM_SLIDE);
        });
        binding.viewDetailMovie.tvDate.setOnClickListener(view -> {
            callBack.replaceFragment(ReviewDetailFragment.TAG, review, true, Constants.ANIM_SLIDE);
        });

    }

    private void likeButtonEvent(Review review, User user) {
        if (user != null) {
            if (review.isLike) {
                binding.viewDetailMovie.ivLike.setImageResource(R.drawable.ic_like);
                review.isLike = false;
                if (review.like != null) {
                    binding.viewDetailMovie.tvLike.setText(String.valueOf(review.like.size() - 1));
                    review.like.remove(user.getId());
                }
            } else {
                binding.viewDetailMovie.ivLike.setImageResource(R.drawable.ic_choose_like);
                review.isLike = true;
                if (review.like != null) {
                    binding.viewDetailMovie.tvLike.setText(String.valueOf(review.like.size() + 1));
                    review.like.add(user.getId());
                }
                if (review.isDislike) {
                    viewModel.dislikeReview(review.id, CommonUtils.getInstance().getPref(Constants.ACCESS_TOKEN));
                    review.isDislike = false;
                    binding.viewDetailMovie.ivDislike.setImageResource(R.drawable.ic_dislike);
                    if (review.dislike != null) {
                        binding.viewDetailMovie.tvDislike.setText(String.valueOf(review.dislike.size() - 1));
                        review.dislike.remove(user.getId());
                    }
                }
            }
        }
    }

    private void disLikeButtonEvent(Review review, User user) {
        if (review.isDislike) {
            review.isDislike = false;
            binding.viewDetailMovie.ivDislike.setImageResource(R.drawable.ic_dislike);
            if (review.dislike != null) {
                binding.viewDetailMovie.tvDislike.setText(String.valueOf(review.dislike.size() - 1));
                review.dislike.remove(user.getId());
            }
        } else {
            review.isDislike = true;
            binding.viewDetailMovie.ivDislike.setImageResource(R.drawable.ic_choose_dislike);
            if (review.dislike != null) {
                binding.viewDetailMovie.tvDislike.setText(String.valueOf(review.dislike.size() + 1));
                review.dislike.add(user.getId());
            }
            if (review.isLike) {
                viewModel.likeReview(review.id, CommonUtils.getInstance().getPref(Constants.ACCESS_TOKEN));
                review.isLike = false;
                binding.viewDetailMovie.ivLike.setImageResource(R.drawable.ic_like);
                if (review.like != null) {
                    binding.viewDetailMovie.tvLike.setText(String.valueOf(review.like.size() - 1));
                    review.like.remove(user.getId());
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            callBack.backToPrev();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void apiError(String key, int code, Object data) {

    }

    @Override
    public void setData(Object data) {
        this.mData = data;
    }

    @Override
    public void gotoMovieDetail(int id) {
        callBack.replaceFragment(DetailMovieFragment.TAG, id, true, Constants.ANIM_SLIDE);
    }

    @Override
    public void gotoVideoYoutube(String key) {
        callBack.replaceFragment(VideoFragment.TAG, key, true, Constants.ANIM_FADE);
        /*String urlYoutube = "https://www.youtube.com/watch?v=";
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlYoutube + key));
        startActivity(browserIntent);*/
    }

    @Override
    public void gotoReviewDetail(Review review) {
        callBack.replaceFragment(ReviewDetailFragment.TAG, review, true, Constants.ANIM_SLIDE);
    }

    @Override
    public void likeReview(String id) {
        if (CommonUtils.getInstance().getPref(Constants.ACCESS_TOKEN) != null) {
            viewModel.likeReview(id, CommonUtils.getInstance().getPref(Constants.ACCESS_TOKEN));
        } else {
            showLoginDialog();
        }
    }

    @Override
    public void dislikeReview(String id) {
        if (CommonUtils.getInstance().getPref(Constants.ACCESS_TOKEN) != null) {
            viewModel.dislikeReview(id, CommonUtils.getInstance().getPref(Constants.ACCESS_TOKEN));
        } else {
            showLoginDialog();
        }
    }

    private void setScreenLoading(boolean isLoading) {
        if (isLoading) {
            ViewUtils.show(binding.progressCircularDv);
            ViewUtils.gone(binding.layoutMovieDetailDv);
        } else {
            ViewUtils.gone(binding.progressCircularDv);
            ViewUtils.show(binding.layoutMovieDetailDv);
        }
    }

    private void showLoginDialog() {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_login_dialog);
        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);
        dialog.setCancelable(true);
        TextView btnCancel = dialog.findViewById(R.id.bt_signup);
        Button btnConfirm = dialog.findViewById(R.id.bt_signin);

        btnCancel.setOnClickListener(view -> {
            callBack.replaceFragment(RegisterFragment.TAG, null, true, Constants.ANIM_SCALE);
            dialog.dismiss();
        });

        btnConfirm.setOnClickListener(view -> {
            callBack.replaceFragment(LoginFragment.TAG, null, true, Constants.ANIM_SCALE);
            dialog.dismiss();
        });
        dialog.show();

    }

    @Override
    public void deleteReview(String id) {

    }

    @Override
    public void updateReview(Review id) {

    }

    @Override
    public void gotoUserReview(String userId) {
        if (userId != null) {
            callBack.replaceFragment(PersonalFragment.TAG, userId, true, Constants.ANIM_SLIDE);
        }
    }

    @Override
    public void gotoCastDetail(String id) {
        callBack.replaceFragment(CastFragment.TAG, id, true, Constants.ANIM_FADE);
    }
}

