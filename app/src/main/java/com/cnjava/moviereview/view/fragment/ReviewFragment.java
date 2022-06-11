package com.cnjava.moviereview.view.fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.cnjava.moviereview.MyApplication;
import com.cnjava.moviereview.R;
import com.cnjava.moviereview.databinding.FragmentReviewBinding;
import com.cnjava.moviereview.model.MovieDetail;
import com.cnjava.moviereview.model.Review;
import com.cnjava.moviereview.model.User;
import com.cnjava.moviereview.util.CommonUtils;
import com.cnjava.moviereview.util.Constants;
import com.cnjava.moviereview.util.ViewUtils;
import com.cnjava.moviereview.view.adapter.ReviewAdapter;
import com.cnjava.moviereview.viewmodel.CommonViewModel;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ReviewFragment extends BaseFragment<FragmentReviewBinding, CommonViewModel> implements ReviewAdapter.ReviewCallBack {

    public static final String TAG = ReviewFragment.class.getName();
    private final String[] items = {"Newest", "Oldest"};
    private Object mData;
    private List<Review> reviews = new ArrayList<>();
    private List<Review> sortedReview = new ArrayList<>();

    @Override
    protected Class<CommonViewModel> getClassVM() {
        return CommonViewModel.class;
    }

    @Override
    protected void initViews() {

        MovieDetail movieDetail = (MovieDetail) mData;

        if (MyApplication.getInstance().getStorage().reviewList != null) {
            reviews = MyApplication.getInstance().getStorage().reviewList;
            sortedReview = MyApplication.getInstance().getStorage().reviewList;
            initReview(sortedReview);
        } else {
            Log.d(TAG, "getReviewByMovieId: 3");
            viewModel.getReviewByMovieId(String.valueOf(movieDetail.id));
        }

        binding.tvName.setText(movieDetail.title);
        //binding.tvDate.setText(NumberUtils.convertDateType3(movieDetail.releaseDate));
        Glide.with(context)
                .load(String.format(Constants.IMAGE_URL + movieDetail.posterPath))
                .transition(DrawableTransitionOptions.withCrossFade())
                .placeholder(R.drawable.ic_image)
                .into(binding.ivPoster);

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.ivBack.startAnimation(AnimationUtils.loadAnimation(context, androidx.appcompat.R.anim.abc_fade_in));
                callBack.backToPrev();
            }
        });

        ArrayAdapter<String> adapterItems = new ArrayAdapter<String>(context, R.layout.item_dropdown, items);
        binding.autoCompleteTxt.setAdapter(adapterItems);

        binding.autoCompleteTxt.setCursorVisible(false);
        binding.autoCompleteTxt.setShowSoftInputOnFocus(false);
        binding.autoCompleteTxt.setDropDownBackgroundResource(R.drawable.bg_light_dark_corner_10);

        binding.autoCompleteTxt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                sortReviewByDay(item);
                //Toast.makeText(context,"Item: "+item,Toast.LENGTH_SHORT).show();
            }
        });

        binding.rbAll.setChecked(true);

        binding.rbAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.tvSort.setText(binding.rbAll.getText());
                sortedReview = reviews;
                if (binding.autoCompleteTxt.getText().toString().equals("Newest")) {
                    sortReviewByDay("Newest");
                } else if (binding.autoCompleteTxt.getText().toString().equals("Oldest")) {
                    sortReviewByDay("Oldest");
                } else {
                    if (sortedReview.size() > 0) {
                        ViewUtils.show(binding.rvReview);
                        ViewUtils.gone(binding.layoutEmpty);
                        initReview(sortedReview);
                    } else {
                        ViewUtils.gone(binding.rvReview);
                        ViewUtils.show(binding.layoutEmpty);
                    }
                }

            }
        });

        binding.rbPositive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.tvSort.setText(binding.rbPositive.getText());
                sortedReview = reviews.stream()
                        .sorted(Comparator.comparing(review -> review.dislike.size()))
                        .collect(Collectors.toList());

                if (binding.autoCompleteTxt.getText().toString().equals("Newest")) {
                    sortReviewByDay("Newest");
                } else if (binding.autoCompleteTxt.getText().toString().equals("Oldest")) {
                    sortReviewByDay("Oldest");
                } else {

                    if (sortedReview.size() > 0) {
                        ViewUtils.show(binding.rvReview);
                        ViewUtils.gone(binding.layoutEmpty);
                        initReview(sortedReview);
                    } else {
                        ViewUtils.gone(binding.rvReview);
                        ViewUtils.show(binding.layoutEmpty);
                    }
                }

            }
        });

        binding.rbCritical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.tvSort.setText(binding.rbCritical.getText());
                sortedReview = reviews.stream()
                        .sorted(Comparator.comparing(review -> review.like.size()))
                        .collect(Collectors.toList());
                if (binding.autoCompleteTxt.getText().toString().equals("Newest")) {
                    sortReviewByDay("Newest");
                } else if (binding.autoCompleteTxt.getText().toString().equals("Oldest")) {
                    sortReviewByDay("Oldest");
                } else {
                    if (sortedReview.size() > 0) {
                        ViewUtils.show(binding.rvReview);
                        ViewUtils.gone(binding.layoutEmpty);
                        initReview(sortedReview);
                    } else {
                        ViewUtils.gone(binding.rvReview);
                        ViewUtils.show(binding.layoutEmpty);
                    }
                }

            }
        });

        binding.rbTenStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.tvSort.setText(binding.rbTenStar.getText());
                sortedReview = new ArrayList<>();
                for (Review review : reviews) {
                    if (review.rating == 10.0) {
                        sortedReview.add(review);
                    }
                }

                if (binding.autoCompleteTxt.getText().toString().equals("Newest")) {
                    sortReviewByDay("Newest");
                } else if (binding.autoCompleteTxt.getText().toString().equals("Oldest")) {
                    sortReviewByDay("Oldest");
                } else {
                    if (sortedReview.size() > 0) {
                        ViewUtils.show(binding.rvReview);
                        ViewUtils.gone(binding.layoutEmpty);
                        initReview(sortedReview);
                    } else {
                        ViewUtils.gone(binding.rvReview);
                        ViewUtils.show(binding.layoutEmpty);
                    }
                }

            }
        });

        binding.rbNineStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.tvSort.setText(binding.rbNineStar.getText());
                sortedReview = new ArrayList<>();
                for (Review review : reviews) {
                    if (review.rating == 9.0) {
                        sortedReview.add(review);
                    }
                }

                if (binding.autoCompleteTxt.getText().toString().equals("Newest")) {
                    sortReviewByDay("Newest");
                } else if (binding.autoCompleteTxt.getText().toString().equals("Oldest")) {
                    sortReviewByDay("Oldest");
                } else {
                    if (sortedReview.size() > 0) {
                        ViewUtils.show(binding.rvReview);
                        ViewUtils.gone(binding.layoutEmpty);
                        initReview(sortedReview);
                    } else {
                        ViewUtils.gone(binding.rvReview);
                        ViewUtils.show(binding.layoutEmpty);
                    }
                }

            }
        });

        binding.rbEightStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.tvSort.setText(binding.rbEightStar.getText());
                sortedReview = new ArrayList<>();
                for (Review review : reviews) {
                    if (review.rating == 8.0) {
                        sortedReview.add(review);
                    }
                }

                if (binding.autoCompleteTxt.getText().toString().equals("Newest")) {
                    sortReviewByDay("Newest");
                } else if (binding.autoCompleteTxt.getText().toString().equals("Oldest")) {
                    sortReviewByDay("Oldest");
                } else {
                    if (sortedReview.size() > 0) {
                        ViewUtils.show(binding.rvReview);
                        ViewUtils.gone(binding.layoutEmpty);
                        initReview(sortedReview);
                    } else {
                        ViewUtils.gone(binding.rvReview);
                        ViewUtils.show(binding.layoutEmpty);
                    }
                }

            }
        });

        binding.rbSevenStar.setOnClickListener(view -> {
            binding.tvSort.setText(binding.rbSevenStar.getText());
            sortedReview = new ArrayList<>();
            for (Review review : reviews) {
                if (review.rating == 7.0) {
                    sortedReview.add(review);
                }
            }

            if (binding.autoCompleteTxt.getText().toString().equals("Newest")) {
                sortReviewByDay("Newest");
            } else if (binding.autoCompleteTxt.getText().toString().equals("Oldest")) {
                sortReviewByDay("Oldest");
            } else {
                if (sortedReview.size() > 0) {
                    ViewUtils.show(binding.rvReview);
                    ViewUtils.gone(binding.layoutEmpty);
                    initReview(sortedReview);
                } else {
                    ViewUtils.gone(binding.rvReview);
                    ViewUtils.show(binding.layoutEmpty);
                }
            }

        });

        binding.rbSixStar.setOnClickListener(view -> {
            binding.tvSort.setText(binding.rbSixStar.getText());
            sortedReview = new ArrayList<>();
            for (Review review : reviews) {
                if (review.rating == 6.0) {
                    sortedReview.add(review);
                }
            }

            if (binding.autoCompleteTxt.getText().toString().equals("Newest")) {
                sortReviewByDay("Newest");
            } else if (binding.autoCompleteTxt.getText().toString().equals("Oldest")) {
                sortReviewByDay("Oldest");
            } else {
                if (sortedReview.size() > 0) {
                    ViewUtils.show(binding.rvReview);
                    ViewUtils.gone(binding.layoutEmpty);
                    initReview(sortedReview);
                } else {
                    ViewUtils.gone(binding.rvReview);
                    ViewUtils.show(binding.layoutEmpty);
                }
            }

        });

        binding.rbFiveStar.setOnClickListener(view -> {
            binding.tvSort.setText(binding.rbFiveStar.getText());
            sortedReview = new ArrayList<>();
            for (Review review : reviews) {
                if (review.rating == 5.0) {
                    sortedReview.add(review);
                }
            }

            if (binding.autoCompleteTxt.getText().toString().equals("Newest")) {
                sortReviewByDay("Newest");
            } else if (binding.autoCompleteTxt.getText().toString().equals("Oldest")) {
                sortReviewByDay("Oldest");
            } else {
                if (sortedReview.size() > 0) {
                    ViewUtils.show(binding.rvReview);
                    ViewUtils.gone(binding.layoutEmpty);
                    initReview(sortedReview);
                } else {
                    ViewUtils.gone(binding.rvReview);
                    ViewUtils.show(binding.layoutEmpty);
                }
            }

        });

        binding.rbFourStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.tvSort.setText(binding.rbFourStar.getText());
                sortedReview = new ArrayList<>();
                for (Review review : reviews) {
                    if (review.rating == 4.0) {
                        sortedReview.add(review);
                    }
                }

                if (binding.autoCompleteTxt.getText().toString().equals("Newest")) {
                    sortReviewByDay("Newest");
                } else if (binding.autoCompleteTxt.getText().toString().equals("Oldest")) {
                    sortReviewByDay("Oldest");
                } else {
                    if (sortedReview.size() > 0) {
                        ViewUtils.show(binding.rvReview);
                        ViewUtils.gone(binding.layoutEmpty);
                        initReview(sortedReview);
                    } else {
                        ViewUtils.gone(binding.rvReview);
                        ViewUtils.show(binding.layoutEmpty);
                    }
                }

            }
        });

        binding.rbThreeStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.tvSort.setText(binding.rbThreeStar.getText());
                sortedReview = new ArrayList<>();
                for (Review review : reviews) {
                    if (review.rating == 3.0) {
                        sortedReview.add(review);
                    }
                }

                if (binding.autoCompleteTxt.getText().toString().equals("Newest")) {
                    sortReviewByDay("Newest");
                } else if (binding.autoCompleteTxt.getText().toString().equals("Oldest")) {
                    sortReviewByDay("Oldest");
                } else {
                    if (sortedReview.size() > 0) {
                        ViewUtils.show(binding.rvReview);
                        ViewUtils.gone(binding.layoutEmpty);
                        initReview(sortedReview);
                    } else {
                        ViewUtils.gone(binding.rvReview);
                        ViewUtils.show(binding.layoutEmpty);
                    }
                }

            }
        });

        binding.rbTwoStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.tvSort.setText(binding.rbTwoStar.getText());
                sortedReview = new ArrayList<>();
                for (Review review : reviews) {
                    if (review.rating == 2.0) {
                        sortedReview.add(review);
                    }
                }

                if (binding.autoCompleteTxt.getText().toString().equals("Newest")) {
                    sortReviewByDay("Newest");
                } else if (binding.autoCompleteTxt.getText().toString().equals("Oldest")) {
                    sortReviewByDay("Oldest");
                } else {
                    if (sortedReview.size() > 0) {
                        ViewUtils.show(binding.rvReview);
                        ViewUtils.gone(binding.layoutEmpty);
                        initReview(sortedReview);
                    } else {
                        ViewUtils.gone(binding.rvReview);
                        ViewUtils.show(binding.layoutEmpty);
                    }

                }

            }
        });

        binding.rbOneStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.tvSort.setText(binding.rbOneStar.getText());
                sortedReview = new ArrayList<>();
                for (Review review : reviews) {
                    if (review.rating == 1.0) {
                        sortedReview.add(review);
                    }
                }

                if (binding.autoCompleteTxt.getText().toString().equals("Newest")) {
                    sortReviewByDay("Newest");
                } else if (binding.autoCompleteTxt.getText().toString().equals("Oldest")) {
                    sortReviewByDay("Oldest");
                } else {
                    if (sortedReview.size() > 0) {
                        ViewUtils.show(binding.rvReview);
                        ViewUtils.gone(binding.layoutEmpty);
                        initReview(sortedReview);
                    } else {
                        ViewUtils.gone(binding.rvReview);
                        ViewUtils.show(binding.layoutEmpty);
                    }

                }

            }
        });


    }

    private void sortReviewByDay(String item) {
        if (item.equals("Newest")) {
            sortedReview = sortedReview.stream()
                    .sorted(Comparator.comparing(Review::getCreatedAt).reversed())
                    .collect(Collectors.toList());

            if (sortedReview.size() > 0) {
                ViewUtils.show(binding.rvReview);
                ViewUtils.gone(binding.layoutEmpty);
                initReview(sortedReview);
            } else {
                ViewUtils.gone(binding.rvReview);
                ViewUtils.show(binding.layoutEmpty);
            }
        } else if (item.equals("Oldest")) {
            sortedReview = sortedReview.stream()
                    .sorted(Comparator.comparing(Review::getCreatedAt))
                    .collect(Collectors.toList());

            if (sortedReview.size() > 0) {
                ViewUtils.show(binding.rvReview);
                ViewUtils.gone(binding.layoutEmpty);
                initReview(sortedReview);
            } else {
                ViewUtils.gone(binding.rvReview);
                ViewUtils.show(binding.layoutEmpty);
            }
        }
    }

    @Override
    protected FragmentReviewBinding initViewBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return FragmentReviewBinding.inflate(inflater, container, false);
    }

    @Override
    public void apiSuccess(String key, Object data) {
        if (key.equals(Constants.KEY_REVIEW_BY_MOVIE_ID)) {
            Log.d(TAG, "KEY_REVIEW_BY_MOVIE_ID: ");
            reviews = (List<Review>) data;
            MyApplication.getInstance().getStorage().reviewList = reviews;
            initReview(reviews);
        }
    }

    @Override
    public void apiError(String key, int code, Object data) {

    }

    private void initReview(List<Review> listReview) {

        ReviewAdapter reviewAdapter = new ReviewAdapter(context, listReview, this);
        binding.rvReview.setAdapter(reviewAdapter);
    }

    @Override
    public void setData(Object data) {
        this.mData = data;
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

    }

    @Override
    public void updateReview(String id) {

    }

    @Override
    public void gotoUserReview(User userReview) {

    }
}
