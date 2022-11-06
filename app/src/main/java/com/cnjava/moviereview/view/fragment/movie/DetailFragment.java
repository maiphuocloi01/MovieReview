package com.cnjava.moviereview.view.fragment.movie;

import static com.cnjava.moviereview.util.NumberUtils.convertDateType3;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.cnjava.moviereview.MyApplication;
import com.cnjava.moviereview.R;
import com.cnjava.moviereview.Storage;
import com.cnjava.moviereview.databinding.FragmentDetailBinding;
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
import com.cnjava.moviereview.view.fragment.AddReviewFragment;
import com.cnjava.moviereview.view.fragment.BaseFragment;
import com.cnjava.moviereview.view.fragment.EditReviewFragment;
import com.cnjava.moviereview.view.fragment.LoginFragment;
import com.cnjava.moviereview.view.fragment.PersonalFragment;
import com.cnjava.moviereview.view.fragment.ProfileFragment;
import com.cnjava.moviereview.view.fragment.RegisterFragment;
import com.cnjava.moviereview.view.fragment.ReviewFragment;
import com.cnjava.moviereview.view.fragment.VideoFragment;
import com.cnjava.moviereview.view.fragment.reviewdetail.ReviewDetailFragment;
import com.cnjava.moviereview.viewmodel.CommonViewModel;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DetailFragment extends BaseFragment<FragmentDetailBinding, CommonViewModel> implements PopularAdapter.MovieCallBack, VideoAdapter.VideoCallBack, ReviewAdapter.ReviewCallBack {

    public static final String TAG = DetailFragment.class.getName();
    private final Storage storage = MyApplication.getInstance().getStorage();
    private Object mData;
    private int movieId;
    private boolean isSelect = false;
    private MovieDetailViewModel movieDetailViewModel;

    @Override
    protected Class<CommonViewModel> getClassVM() {
        return CommonViewModel.class;
    }

    public static DetailFragment newInstance() {
        return new DetailFragment();
    }

    @Override
    protected void initViews() {
        movieId = (int) mData;
        //setScreenLoading(true);
        movieDetailViewModel = new ViewModelProvider(this).get(MovieDetailViewModel.class);
        movieDetailViewModel.getLiveDataIsLoading().observe(this, this::setScreenLoading);
        storage.fragmentTag = TAG;
        movieDetailViewModel.getMovieDetail(movieId);
        movieDetailViewModel.movieDetailLD().observe(this, _movieDetail -> {
            initMovieDetail(_movieDetail);
            movieDetailViewModel.getSocial(movieId);
            movieDetailViewModel.socialLD().observe(this, _social -> {
                if (_social != null)
                    initBindingButtonFeature(_movieDetail, _social);
            });
            if (_movieDetail.collection != null) {
                movieDetailViewModel.getCollection(_movieDetail.collection.id);
                movieDetailViewModel.collectionLD().observe(this, _collection -> {
                    initCollectionMovie(_collection);
                });
            }
        });
        if (CommonUtils.getInstance().getPref(Constants.ACCESS_TOKEN) != null) {
            movieDetailViewModel.getMyFavorite(CommonUtils.getInstance().getPref(Constants.ACCESS_TOKEN));
            movieDetailViewModel.myFavoriteLD().observe(this, _favorites -> {
                initFavorite(_favorites, String.valueOf(movieId));
            });
        }
        movieDetailViewModel.getCast(movieId);
        movieDetailViewModel.actorLD().observe(this, _actor -> {
            initActorList(_actor);
        });
        movieDetailViewModel.getRecommendation(movieId);
        movieDetailViewModel.recommendationLD().observe(this, _recommendation -> {
            initRecommendMovie(_recommendation);
        });
        movieDetailViewModel.getVideo(movieId);
        movieDetailViewModel.videoLD().observe(this, _video -> {
            initVideoTrailer(_video);
        });
        movieDetailViewModel.getReviewByMovieId(String.valueOf(movieId));
        movieDetailViewModel.movieReviewLD().observe(this, _reviews -> {
            storage.reviewList = _reviews;
            initSomeReview(_reviews);
        });

    }

    private void initSomeReview(List<Review> reviews) {
        Review myReview = null;
        User user = storage.myUser;
        if (user != null) {
            Optional<Review> resultReview = reviews.stream().filter(obj -> obj.user.getId().equals(user.getId())).findFirst();
            if (resultReview.isPresent()) {
                myReview = resultReview.get();
            }
        }
        initReview(reviews, myReview, user);
        binding.ivReview.setOnClickListener(view -> {
            binding.ivReview.startAnimation(AnimationUtils.loadAnimation(context, R.anim.abc_choose));
            callBack.showFragment(ReviewFragment.TAG, reviews, true, Constants.ANIM_SLIDE);
        });
    }

    private void initCollectionMovie(Collection collection) {
        ViewUtils.show(binding.tvCollection);
        ViewUtils.show(binding.rvCollection);
        binding.tvCollection.setText(collection.name);
        binding.rvCollection.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        CollectionAdapter adapter = new CollectionAdapter(context, collection, this);
        binding.rvCollection.setAdapter(adapter);
    }

    private void initVideoTrailer(Video video) {
        if (video.results.size() > 0) {
            ViewUtils.show(binding.tvVideo);
            ViewUtils.show(binding.rvVideo);
            binding.rvVideo.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            VideoAdapter adapter = new VideoAdapter(context, video, this);
            binding.rvVideo.setAdapter(adapter);
        } else {
            ViewUtils.gone(binding.tvVideo);
        }
    }

    private void initRecommendMovie(Movie recommendation) {
        if (recommendation.results != null) {
            if (recommendation.results.size() == 0) {
                binding.tvNoRecommend.setText(R.string.no_recommend);
                binding.tvNoRecommend.setVisibility(View.VISIBLE);

            } else {
                MovieAdapter adapter = new MovieAdapter(context, recommendation, this);
                binding.rvRecommend.setAdapter(adapter);
            }
        }
    }

    private void initActorList(Actor actor) {
        if (actor.cast.size() > 0) {
            binding.rvCast.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            CastAdapter castAdapter = new CastAdapter(context, actor);
            binding.rvCast.setAdapter(castAdapter);
        } else {
            ViewUtils.gone(binding.tvCast);
        }
    }

    @Override
    protected FragmentDetailBinding initViewBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return FragmentDetailBinding.inflate(inflater, container, false);
    }

    private void initBindingButtonFeature(MovieDetail detail, Social social) {

        binding.ivBack.setOnClickListener(view -> {
            binding.ivBack.startAnimation(AnimationUtils.loadAnimation(context, androidx.appcompat.R.anim.abc_fade_in));
            callBack.backToPrev();
        });

        binding.btAddReview.setOnClickListener(view -> {
            if (CommonUtils.getInstance().getPref(Constants.ACCESS_TOKEN) != null) {
                callBack.showFragment(AddReviewFragment.TAG, detail, true, Constants.ANIM_FADE);
            } else {
                showLoginDialog();
            }
        });

        binding.layoutFavorite.setOnClickListener(view -> {
            if (CommonUtils.getInstance().getPref(Constants.ACCESS_TOKEN) != null) {
                view.startAnimation(AnimationUtils.loadAnimation(context, androidx.appcompat.R.anim.abc_fade_in));
                if (!isSelect) {
                    binding.ivFavorite.setColorFilter(ContextCompat.getColor(context, R.color.primary));
                    binding.tvFavorite.setTextColor(ContextCompat.getColor(context, R.color.primary));
                    Favorite.MovieFavorite movieFavorite = new Favorite.MovieFavorite(detail.posterPath,
                            String.valueOf(detail.id),
                            detail.title, detail.overview,
                            detail.releaseDate,
                            detail.voteAverage
                    );
                    viewModel.addFavorite(movieFavorite, CommonUtils.getInstance().getPref(Constants.ACCESS_TOKEN));
                    isSelect = true;
                } else {
                    binding.ivFavorite.setColorFilter(ContextCompat.getColor(context, R.color.light_white));
                    binding.tvFavorite.setTextColor(ContextCompat.getColor(context, R.color.light_white));
                    viewModel.deleteFavorite(String.valueOf(movieId), CommonUtils.getInstance().getPref(Constants.ACCESS_TOKEN));
                    isSelect = false;
                }
            } else {
                showLoginDialog();
            }

        });

        binding.ivMore.setOnClickListener(view -> popupMenuMore(detail, social));
    }

    private void initMovieDetail(MovieDetail detail) {
        storage.movieDetail = detail;
        List<String> listGenres = new ArrayList<>();
        for (MovieDetail.Genres item : detail.genres) {
            listGenres.add(item.name);
        }
        binding.tvName.setText(Objects.requireNonNull(detail.title));
        binding.tvRuntime.setText(detail.runtime + " min");
        binding.tvRating.setText(new DecimalFormat("##.##").format(detail.voteAverage));
        if (detail.overview != null) {
            binding.tvOverview.setText(detail.overview);
        } else {
            binding.tvTitleOverview.setVisibility(View.GONE);
        }
        binding.tvReleaseDate.setText(convertDateType3(detail.releaseDate));
        binding.tvRateCount.setText(String.valueOf(detail.voteCount));
        binding.tvPopularity.setText(String.valueOf(detail.popularity));
        Glide.with(context)
                .load(Constants.IMAGE_URL + detail.backdropPath)
                .transition(DrawableTransitionOptions.withCrossFade())
                .placeholder(R.drawable.ic_image)
                .into(binding.ivCover);
        Glide.with(context)
                .load(Constants.IMAGE_URL + detail.posterPath)
                .transition(DrawableTransitionOptions.withCrossFade())
                .placeholder(R.drawable.ic_movie)
                .into(binding.ivPoster);
        if (detail.budget == 0) {
            binding.tvBudget.setText(R.string.undefined);
        } else {
            binding.tvBudget.setText(NumberUtils.convertCurrency(detail.budget));
        }
        if (detail.revenue == 0) {
            binding.tvRevenue.setText(R.string.undefined);
        } else {
            binding.tvRevenue.setText(NumberUtils.convertCurrency(detail.revenue));
        }
        binding.rvGenres.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        GenresAdapter adapterGenres = new GenresAdapter(context, listGenres);
        binding.rvGenres.setAdapter(adapterGenres);
    }

    private void popupMenuMore(MovieDetail detail, Social social) {
        PopupMenu popup = new PopupMenu(context, binding.ivMore);
        popup.getMenuInflater().inflate(R.menu.poupup_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(item -> {
            if (item.getTitle().toString().equals("Homepage")) {
                if (!detail.homepage.equals("")) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(detail.homepage));
                    startActivity(browserIntent);
                } else {
                    Toast.makeText(context, "No information", Toast.LENGTH_SHORT).show();
                }
            } else if (item.getTitle().toString().equals("Facebook")) {
                if (social.facebookId != null) {
                    String fbId = social.facebookId;
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/" + fbId));
                    startActivity(browserIntent);
                } else {
                    Toast.makeText(context, "No information", Toast.LENGTH_SHORT).show();
                }
            } else if (item.getTitle().toString().equals("Twitter")) {
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
            binding.ivFavorite.setColorFilter(ContextCompat.getColor(context, R.color.primary));
            binding.tvFavorite.setTextColor(ContextCompat.getColor(context, R.color.primary));
            isSelect = true;
        }
    }

    private void initReview(List<Review> listReview, Review myReview, User user) {
        if (listReview != null && listReview.size() > 0) {
            if (myReview != null) {
                initYourReview(myReview, user);
            } else {
                ViewUtils.show(binding.btAddReview);
                ViewUtils.gone(binding.layoutYourReview);
                ViewUtils.gone(binding.tvTitleYourReview);
            }

            List<Review> threeFirstReview;
            if (user != null) {
                threeFirstReview = listReview.stream().filter(obj -> !obj.user.getId().equals(user.getId())).limit(3).collect(Collectors.toList());
            } else {
                threeFirstReview = listReview.stream().limit(3).collect(Collectors.toList());
            }
            Log.d(TAG, "threeFirstReview: " + threeFirstReview.size());
            ReviewAdapter reviewAdapter = new ReviewAdapter(context, threeFirstReview, this);
            binding.rvReview.setAdapter(reviewAdapter);

        } else {
            binding.rvReview.setVisibility(View.GONE);
            binding.tvNoReviews.setVisibility(View.VISIBLE);
            binding.ivReview.setVisibility(View.GONE);
            binding.tvTitleYourReview.setVisibility(View.GONE);
            binding.layoutYourReview.setVisibility(View.GONE);
        }
    }


    public void initYourReview(Review review, User user) {
        ViewUtils.gone(binding.btAddReview);
        ViewUtils.show(binding.layoutYourReview);
        ViewUtils.show(binding.tvTitleYourReview);
        Glide.with(context)
                .load(review.user.getAvatar())
                .transition(DrawableTransitionOptions.withCrossFade())
                .placeholder(R.drawable.img_default_avt)
                .into(binding.ivAvt);
        binding.tvNameYourReview.setText(review.user.getName());
        binding.tvDate.setText(NumberUtils.convertDateType7(review.createdAt));
        binding.tvContent.setText(review.content);
        binding.tvRate.setText(String.valueOf((int) review.rating));
        if (!review.isDislike) {
            for (String yourLike : review.like) {
                if (yourLike.equals(user.getId())) {
                    review.isLike = true;
                    binding.ivLike.setImageResource(R.drawable.ic_choose_like);
                    break;
                }
            }
        }
        if (!review.isLike) {
            for (String yourDislike : review.dislike) {
                if (yourDislike.equals(user.getId())) {
                    review.isDislike = true;
                    binding.ivDislike.setImageResource(R.drawable.ic_choose_dislike);
                    break;
                }
            }
        }
        if (review.like != null) {
            Log.d("TAG", "tvLike: ");
            binding.tvLike.setText(String.valueOf(review.like.size()));
        }
        if (review.dislike != null) {
            Log.d("TAG", "tvDislike: ");
            binding.tvDislike.setText(String.valueOf(review.dislike.size()));
        }
        if (review.user.getId().equals(user.getId())) {
            binding.ivMoreAction.setOnClickListener(view -> {
                PopupMenu popup = new PopupMenu(context, binding.ivMoreAction);
                popup.getMenuInflater().inflate(R.menu.review_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(item -> {
                    if (item.getTitle().toString().equals("Edit")) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("review", review);
                        bundle.putString("tag", DetailFragment.TAG);
                        callBack.showFragment(EditReviewFragment.TAG, bundle, true, Constants.ANIM_SLIDE);
                    } else if (item.getTitle().toString().equals("Delete")) {
                        viewModel.deleteReview(review.id, CommonUtils.getInstance().getPref(Constants.ACCESS_TOKEN));
                        ViewUtils.show(binding.btAddReview);
                        ViewUtils.gone(binding.layoutYourReview);
                        ViewUtils.gone(binding.tvTitleYourReview);
                    }
                    return true;
                });
                popup.show();
            });
        }

        binding.layoutLike.setOnClickListener(view -> {
            viewModel.likeReview(review.id, CommonUtils.getInstance().getPref(Constants.ACCESS_TOKEN));
            likeButtonEvent(review, user);
        });

        binding.layoutDislike.setOnClickListener(view -> {
            viewModel.dislikeReview(review.id, CommonUtils.getInstance().getPref(Constants.ACCESS_TOKEN));
            disLikeButtonEvent(review, user);
        });

        binding.ivAvt.setOnClickListener(view -> {
            //callBack.gotoUserReview(review.user);
        });

        binding.tvName.setOnClickListener(view -> {
            //callBack.gotoUserReview(review.user);
        });
        if (review.content.length() > 150) {
            binding.tvShowMore.setOnClickListener(view -> {
                //callBack.gotoReviewDetail(review);
                callBack.showFragment(ReviewDetailFragment.TAG, review, true, Constants.ANIM_SLIDE);
            });
        } else {
            binding.tvShowMore.setVisibility(View.GONE);
        }
        binding.tvContent.setOnClickListener(view -> {
            //callBack.gotoReviewDetail(review);
            callBack.showFragment(ReviewDetailFragment.TAG, review, true, Constants.ANIM_SLIDE);
        });
        binding.tvDate.setOnClickListener(view -> {
            //callBack.gotoReviewDetail(review);
            callBack.showFragment(ReviewDetailFragment.TAG, review, true, Constants.ANIM_SLIDE);
        });

    }

    private void likeButtonEvent(Review review, User user) {
        if (user != null) {
            if (review.isLike) {
                //unlike
                binding.ivLike.setImageResource(R.drawable.ic_like);
                review.isLike = false;
                if (review.like != null) {
                    binding.tvLike.setText(String.valueOf(review.like.size() - 1));
                    review.like.remove(user.getId());
                }
            } else {
                //like
                binding.ivLike.setImageResource(R.drawable.ic_choose_like);
                review.isLike = true;
                if (review.like != null) {
                    binding.tvLike.setText(String.valueOf(review.like.size() + 1));
                    review.like.add(user.getId());
                }
                if (review.isDislike) {
                    //undislike
                    viewModel.dislikeReview(review.id, CommonUtils.getInstance().getPref(Constants.ACCESS_TOKEN));
                    review.isDislike = false;
                    binding.ivDislike.setImageResource(R.drawable.ic_dislike);
                    if (review.dislike != null) {
                        binding.tvDislike.setText(String.valueOf(review.dislike.size() - 1));
                        review.dislike.remove(user.getId());
                    }
                }
            }
        }
    }

    private void disLikeButtonEvent(Review review, User user) {
        if (review.isDislike) {
            //undislike
            review.isDislike = false;
            binding.ivDislike.setImageResource(R.drawable.ic_dislike);
            if (review.dislike != null) {
                binding.tvDislike.setText(String.valueOf(review.dislike.size() - 1));
                review.dislike.remove(user.getId());
            }
        } else {
            //dislike
            review.isDislike = true;
            binding.ivDislike.setImageResource(R.drawable.ic_choose_dislike);
            if (review.dislike != null) {
                binding.tvDislike.setText(String.valueOf(review.dislike.size() + 1));
                review.dislike.add(user.getId());
            }
            if (review.isLike) {
                //unlike
                viewModel.likeReview(review.id, CommonUtils.getInstance().getPref(Constants.ACCESS_TOKEN));
                review.isLike = false;
                binding.ivLike.setImageResource(R.drawable.ic_like);
                if (review.like != null) {
                    binding.tvLike.setText(String.valueOf(review.like.size() - 1));
                    review.like.remove(user.getId());
                }
            }
        }
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
        callBack.showFragment(DetailFragment.TAG, id, true, Constants.ANIM_SLIDE);
    }

    @Override
    public void gotoVideoYoutube(String key) {
        //showVideoDialog(key);
        callBack.showFragment(VideoFragment.TAG, key, true, Constants.ANIM_FADE);
        /*String urlYoutube = "https://www.youtube.com/watch?v=";
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlYoutube + key));
        startActivity(browserIntent);*/
    }

    @Override
    public void gotoReviewDetail(Review review) {
        callBack.showFragment(ReviewDetailFragment.TAG, review, true, Constants.ANIM_SLIDE);
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
            ViewUtils.show(binding.progressCircular);
            ViewUtils.gone(binding.layoutMovieDetail);
        } else {
            ViewUtils.gone(binding.progressCircular);
            ViewUtils.show(binding.layoutMovieDetail);
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
            callBack.showFragment(PersonalFragment.TAG, userId, true, Constants.ANIM_SLIDE);
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach: ");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }
}
