package com.cnjava.moviereview.view.fragment;

import static com.cnjava.moviereview.util.NumberUtils.convertDateType3;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.cnjava.moviereview.MyApplication;
import com.cnjava.moviereview.R;
import com.cnjava.moviereview.databinding.FragmentDetailBinding;
import com.cnjava.moviereview.model.Actor;
import com.cnjava.moviereview.model.Collection;
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
import com.cnjava.moviereview.viewmodel.CommonViewModel;
import com.cnjava.moviereview.viewmodel.ShareViewModel;

import java.util.ArrayList;
import java.util.List;

public class DetailFragment extends BaseFragment<FragmentDetailBinding, CommonViewModel> implements PopularAdapter.MovieCallBack, VideoAdapter.VideoCallBack, ReviewAdapter.ReviewCallBack {

    public static final String TAG = DetailFragment.class.getName();

    private Object mData;
    private MovieDetail movieDetail;
    private Social social;
    private List<Review> reviews = MyApplication.getInstance().getStorage().reviewList;
    private ShareViewModel sharedViewModel;
    private ReviewAdapter reviewAdapter;

    @Override
    protected Class<CommonViewModel> getClassVM() {
        return CommonViewModel.class;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach: ");
        sharedViewModel = new ViewModelProvider(this).get(ShareViewModel.class);
        sharedViewModel.getListReview().observe(this, new Observer<List<Review>>() {
            @Override
            public void onChanged(List<Review> list) {
                Log.d(TAG, "onChanged: ");
                initReview(list);
            }
        });
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
        viewModel.getSocial(id);

        if (MyApplication.getInstance().getStorage().myUser == null) {
            //DialogUtils.showLoadDataDialog(context);
            Log.d(TAG, "myUser null");
            if (CommonUtils.getInstance().getPref(Constants.ACCESS_TOKEN) != null) {
                Log.d(TAG, "getYourProfile: ");
                viewModel.getYourProfile(CommonUtils.getInstance().getPref(Constants.ACCESS_TOKEN));
            }
        }
        Log.d(TAG, "initViews: ");
        viewModel.getReviewByMovieId(String.valueOf(id));

        /*if (MyApplication.getInstance().getStorage().reviewList != null) {
            if (MyApplication.getInstance().getStorage().reviewList.size() > 0) {
                if (MyApplication.getInstance().getStorage().reviewList.get(0).movie.id.equals(String.valueOf(id))) {
                    Log.d(TAG, "initReview: 1");
                    viewModel.setListReviewLD(MyApplication.getInstance().getStorage().reviewList);
                    viewModel.getListReviewLD().observe(this, new Observer<List<Review>>() {
                        @Override
                        public void onChanged(List<Review> list) {
                            initReview(list);
                        }
                    });
                } else {
                    Log.d(TAG, "getReviewByMovieId: 1");
                    viewModel.getReviewByMovieId(String.valueOf(id));
                }
            } else {
                Log.d(TAG, "getReviewByMovieId: 2");
                viewModel.getReviewByMovieId(String.valueOf(id));
                //initReview(MyApplication.getInstance().getStorage().reviewList);
            }
        } else {
            Log.d(TAG, "getReviewByMovieId: 3");
            viewModel.getReviewByMovieId(String.valueOf(id));
        }*/


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
                if (CommonUtils.getInstance().getPref(Constants.ACCESS_TOKEN) != null) {
                    callBack.replaceFragment(AddReviewFragment.TAG, movieDetail, true, Constants.ANIM_SCALE);
                } else {
                    showLoginDialog();
                }
            }
        });


        //List<Review> reviewList = new ArrayList<>();

        //reviewList.add(new Review(1, 1, "Mai Phước Lợi", getResources().getString(R.string.content), 7, "May 4, 2022", 123, "Hello", "https://image.tmdb.org/t/p/w1280/fBEucxECxGLKVHBznO0qHtCGiMO.jpg"));


    }

    @Override
    protected FragmentDetailBinding initViewBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return FragmentDetailBinding.inflate(inflater, container, false);
    }

    @Override
    public void apiSuccess(String key, Object data) {
        if (key.equals(Constants.KEY_GET_MOVIE_DETAIL)) {
            movieDetail = (MovieDetail) data;
            MyApplication.getInstance().getStorage().movieDetail = movieDetail;

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
            } else {
                binding.tvTitleOverview.setVisibility(View.GONE);
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
                    popup.getMenuInflater().inflate(R.menu.poupup_menu, popup.getMenu());
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            //Toast.makeText(context, "You Clicked : " + item.getItemId(), Toast.LENGTH_SHORT).show();
                            if (item.getTitle().toString().equals("Homepage")) {
                                if (!movieDetail.homepage.equals("")) {
                                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(movieDetail.homepage));
                                    startActivity(browserIntent);
                                } else {
                                    Toast.makeText(context, "No information", Toast.LENGTH_SHORT).show();
                                }
                            } else if (item.getTitle().toString().equals("Facebook")) {
                                try {
                                    context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
                                    String url = "";
                                    if (social.facebookId != null) {
                                        String fbId = social.facebookId;

                                        String FACEBOOK_URL = "https://www.facebook.com/" + fbId;
                                        String FACEBOOK_PAGE_ID = fbId;
                                        int versionCode = context.getPackageManager().getPackageInfo("com.facebook.katana", 0).versionCode;
                                        if (versionCode >= 3002850) { //newer versions of fb app
                                            url = "fb://facewebmodal/f?href=" + FACEBOOK_URL;
                                        } else { //older versions of fb app
                                            url = "fb://page/" + FACEBOOK_PAGE_ID;
                                        }
                                        Intent facebookIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                        startActivity(facebookIntent);
                                    } else {
                                        Toast.makeText(context, "No information", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (Exception e) {
                                    if (social.facebookId != null) {
                                        String fbId = social.facebookId;
                                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/" + fbId));
                                        startActivity(browserIntent);
                                    } else {
                                        Toast.makeText(context, "No information", Toast.LENGTH_SHORT).show();
                                    }
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
            if (actor.cast.size() > 0) {
                binding.rvCast.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                CastAdapter castAdapter = new CastAdapter(context, actor);
                binding.rvCast.setAdapter(castAdapter);
            } else {
                ViewUtils.gone(binding.tvCast);
            }

        } else if (key.equals(Constants.KEY_GET_RECOMMENDATION)) {
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
            if (video.results.size() > 0) {
                ViewUtils.show(binding.tvVideo);
                ViewUtils.show(binding.rvVideo);
                binding.rvVideo.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                VideoAdapter adapter = new VideoAdapter(context, video, this);
                binding.rvVideo.setAdapter(adapter);
            } else {
                ViewUtils.gone(binding.tvVideo);
            }
        } else if (key.equals(Constants.KEY_GET_SOCIAL)) {
            social = (Social) data;
            Log.d(TAG, "KEY_GET_SOCIAL: ");
        } else if (key.equals(Constants.KEY_REVIEW_BY_MOVIE_ID)) {
            /*ViewUtils.gone(binding.progressCircular);
            ViewUtils.show(binding.layoutMovieDetail);*/
            Log.d(TAG, "KEY_REVIEW_BY_MOVIE_ID: ");
            reviews = (List<Review>) data;
            initReview(reviews);
            //initReview(reviews);
            //viewModel.setListReviewLD(reviews);
            //MyApplication.getInstance().getStorage().reviewList = reviews;

            binding.ivReview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    binding.ivReview.startAnimation(AnimationUtils.loadAnimation(context, R.anim.abc_choose));
                    callBack.showFragment(ReviewFragment.TAG, reviews, true, Constants.ANIM_SLIDE);
                }
            });

        } else if (key.equals(Constants.KEY_LIKE_REVIEW)) {
            Log.d(TAG, "KEY_LIKE_REVIEW: ");
        } else if (key.equals(Constants.KEY_DISLIKE_REVIEW)) {
            Log.d(TAG, "KEY_DISLIKE_REVIEW: ");
        } else if (key.equals(Constants.KEY_GET_YOUR_PROFILE)) {
            User user = (User) data;
            MyApplication.getInstance().getStorage().myUser = user;
        } else if (key.equals(Constants.KEY_DELETE_REVIEW)) {
            Log.d(TAG, "delete review: ");
        } else if (key.equals(Constants.KEY_UPDATE_REVIEW)) {
            Log.d(TAG, "delete review: ");
        }
    }

    private void initReview(List<Review> listReview) {
        if (listReview != null && listReview.size() > 0) {
            User user = MyApplication.getInstance().getStorage().myUser;
            if (user != null) {
                initYourReview(listReview, user);
            }
            if (listReview.size() > 4) {
                List<Review> threeFirstReview = new ArrayList<>();
                if (user != null) {
                    for (int i = 0; i < 4; i++) {
                        if (!listReview.get(i).user.getId().equals(user.getId())) {
                            threeFirstReview.add(listReview.get(i));
                            if (threeFirstReview.size() == 3) {
                                break;
                            }
                        }
                    }
                } else {
                    threeFirstReview = listReview.subList(0, 2);

                }
                reviewAdapter = new ReviewAdapter(context, threeFirstReview, this);
            } else {
                List<Review> newReviews = new ArrayList<>();
                if (user != null) {
                    for (Review review : listReview) {
                        if (!review.user.getId().equals(user.getId())) {
                            newReviews.add(review);
                        }
                    }
                    if(newReviews.size() > 0){
                        reviewAdapter = new ReviewAdapter(context, newReviews, this);
                    } else {
                        Log.d(TAG, "no review: ");
                        binding.rvReview.setVisibility(View.GONE);
                        binding.tvNoReviews.setVisibility(View.VISIBLE);
                        binding.ivReview.setVisibility(View.GONE);
                    }
                } else {
                    reviewAdapter = new ReviewAdapter(context, listReview, this);
                }

            }
            binding.rvReview.setAdapter(reviewAdapter);
        } else {
            Log.d(TAG, "no review: ");
            binding.rvReview.setVisibility(View.GONE);
            binding.tvNoReviews.setVisibility(View.VISIBLE);
            binding.ivReview.setVisibility(View.GONE);
            binding.tvTitleYourReview.setVisibility(View.GONE);
            binding.layoutYourReview.setVisibility(View.GONE);
        }
        ViewUtils.gone(binding.progressCircular);
        ViewUtils.show(binding.layoutMovieDetail);
    }


    public void initYourReview(List<Review> listReview, User user) {
        for (Review review : listReview) {
            if (review.user.getId().equals(user.getId())) {
                ViewUtils.gone(binding.btAddReview);
                ViewUtils.show(binding.layoutYourReview);

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
                    binding.ivMoreAction.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Toast.makeText(context, "You Clicked : ", Toast.LENGTH_SHORT).show();
                            PopupMenu popup = new PopupMenu(context, binding.ivMoreAction);
                            popup.getMenuInflater().inflate(R.menu.review_menu, popup.getMenu());
                            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                public boolean onMenuItemClick(MenuItem item) {
                                    Toast.makeText(context, "You Clicked : " + item.getItemId(), Toast.LENGTH_SHORT).show();
                                    if (item.getTitle().toString().equals("Edit")) {
                                        //callBack.updateReview(review.id);
                                    } else if (item.getTitle().toString().equals("Delete")) {
                                        viewModel.deleteReview(review.id, CommonUtils.getInstance().getPref(Constants.ACCESS_TOKEN));
                                        ViewUtils.show(binding.btAddReview);
                                        ViewUtils.gone(binding.layoutYourReview);
                                        ViewUtils.gone(binding.tvTitleYourReview);
                                    }
                                    return true;
                                }
                            });
                            popup.show();
                        }
                    });
                }

                binding.layoutLike.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //callBack.likeReview(review.id);
                        viewModel.likeReview(review.id, CommonUtils.getInstance().getPref(Constants.ACCESS_TOKEN));
                        if (user != null) {
                            if (review.isLike) {
                                //unlike
                                binding.ivLike.setImageResource(R.drawable.ic_like);
                                review.isLike = false;
                                if (review.like != null) {
                                    Log.d("TAG", "tvLike: ");
                                    binding.tvLike.setText(String.valueOf(review.like.size() - 1));
                                    review.like.remove(user.getId());
                                }
                            } else {
                                //like
                                binding.ivLike.setImageResource(R.drawable.ic_choose_like);
                                review.isLike = true;
                                if (review.like != null) {
                                    Log.d("TAG", "tvLike: ");
                                    binding.tvLike.setText(String.valueOf(review.like.size() + 1));
                                    review.like.add(user.getId());
                                }
                                if (review.isDislike) {
                                    //undislike
                                    //callBack.dislikeReview(review.id);
                                    viewModel.dislikeReview(review.id, CommonUtils.getInstance().getPref(Constants.ACCESS_TOKEN));
                                    review.isDislike = false;
                                    binding.ivDislike.setImageResource(R.drawable.ic_dislike);
                                    if (review.dislike != null) {
                                        Log.d("TAG", "tvDislike: ");
                                        binding.tvDislike.setText(String.valueOf(review.dislike.size() - 1));
                                        review.dislike.remove(user.getId());
                                    }
                                }
                            }
                        }

                    }
                });

                binding.layoutDislike.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //callBack.dislikeReview(review.id);
                        viewModel.dislikeReview(review.id, CommonUtils.getInstance().getPref(Constants.ACCESS_TOKEN));
                        if (user != null) {
                            if (review.isDislike) {
                                //undislike
                                review.isDislike = false;
                                binding.ivDislike.setImageResource(R.drawable.ic_dislike);
                                if (review.dislike != null) {
                                    Log.d("TAG", "tvDislike: ");
                                    binding.tvDislike.setText(String.valueOf(review.dislike.size() - 1));
                                    review.dislike.remove(user.getId());
                                }
                            } else {
                                //dislike
                                review.isDislike = true;
                                binding.ivDislike.setImageResource(R.drawable.ic_choose_dislike);
                                if (review.dislike != null) {
                                    Log.d("TAG", "tvDislike: ");
                                    binding.tvDislike.setText(String.valueOf(review.dislike.size() + 1));
                                    review.dislike.add(user.getId());
                                }
                                if (review.isLike) {
                                    //unlike
                                    //callBack.likeReview(review.id);
                                    viewModel.likeReview(review.id, CommonUtils.getInstance().getPref(Constants.ACCESS_TOKEN));
                                    review.isLike = false;
                                    binding.ivLike.setImageResource(R.drawable.ic_like);
                                    if (review.like != null) {
                                        Log.d("TAG", "tvLike: ");
                                        binding.tvLike.setText(String.valueOf(review.like.size() - 1));
                                        review.like.remove(user.getId());
                                    }
                                }
                            }
                        }
                    }
                });

                binding.ivAvt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //callBack.gotoUserReview(review.user);
                    }
                });

                binding.tvName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //callBack.gotoUserReview(review.user);
                    }
                });
                if (review.content.length() > 150) {
                    binding.tvShowMore.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //callBack.gotoReviewDetail(review);
                            callBack.showFragment(ReviewDetailFragment.TAG, review, true, Constants.ANIM_SLIDE);
                        }
                    });
                } else {
                    binding.tvShowMore.setVisibility(View.GONE);
                }
                binding.tvContent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //callBack.gotoReviewDetail(review);
                        callBack.showFragment(ReviewDetailFragment.TAG, review, true, Constants.ANIM_SLIDE);
                    }
                });
                binding.tvDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //callBack.gotoReviewDetail(review);
                        callBack.showFragment(ReviewDetailFragment.TAG, review, true, Constants.ANIM_SLIDE);
                    }
                });

                break;
            } else {
                ViewUtils.show(binding.btAddReview);
                ViewUtils.gone(binding.layoutYourReview);
                ViewUtils.gone(binding.tvTitleYourReview);
            }
        }

    }

    @Override
    public void apiError(String key, int code, Object data) {
        if (code == 999) {
            if (key.equals(Constants.KEY_REVIEW_BY_MOVIE_ID)) {
                Log.d(TAG, "apiError: " + data.toString());
                ViewUtils.gone(binding.progressCircular);
                ViewUtils.show(binding.layoutMovieDetail);
                List<Review> listReview = new ArrayList<>();
                initReview(listReview);
                Toast.makeText(context, "Unable connect to heroku", Toast.LENGTH_SHORT).show();
            }
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

    private void showLoginDialog() {
        Log.d(TAG, "showAlertDialog: ");
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
        reviewAdapter.deleteItem(id);
        viewModel.deleteReview(id, CommonUtils.getInstance().getPref(Constants.ACCESS_TOKEN));
    }

    @Override
    public void updateReview(String id) {

    }

    @Override
    public void gotoUserReview(User userReview) {

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: " + (int) mData);
        if (reviews != null)
            Log.d(TAG, "review: " + reviews.size());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView: " + (int) mData);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: " + (int) mData);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: " + (int) mData);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: " + (int) mData);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: " + (int) mData);
        MyApplication.getInstance().getStorage().reviewList = null;
    }

    /*@Override
    public void updateReview(List<Review> reviewList) {
        Log.d(TAG, "updateReview: ");
        initReview(reviewList);
    }*/
}
