package com.cnjava.moviereview.view.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.cnjava.moviereview.MyApplication;
import com.cnjava.moviereview.R;
import com.cnjava.moviereview.databinding.ItemMyReviewBinding;
import com.cnjava.moviereview.model.Review;
import com.cnjava.moviereview.model.User;
import com.cnjava.moviereview.util.Constants;
import com.cnjava.moviereview.util.NumberUtils;

import java.util.List;

public class MyReviewAdapter extends RecyclerView.Adapter<MyReviewAdapter.MyViewHolder> {

    private Context context;
    private List<Review> listReview;
    private MyReviewCallBack callBack;
    private User user = MyApplication.getInstance().getStorage().myUser;

    public interface MyReviewCallBack{
        void gotoReviewDetail(Review review);
        void likeReview(String id);
        void dislikeReview(String id);
        void deleteReview(String id);
        void updateReview(Review review);
        void gotoMovieDetail(String id);
    }

    public MyReviewAdapter(Context context, List<Review> listReview, MyReviewCallBack callBack) {
        this.context = context;
        this.listReview = listReview;
        this.callBack = callBack;
    }

    public void renewItems(List<Review> reviews) {
        this.listReview = reviews;
        notifyDataSetChanged();
    }

    public void deleteItem(String id) {
        if (listReview != null) {
            for (Review review : listReview) {
                if (review.id.equals(id)) {
                    this.listReview.remove(review);
                    break;
                }
            }
        }
        notifyDataSetChanged();
    }

    public void addItem(Review review) {
        this.listReview.add(review);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMyReviewBinding binding = ItemMyReviewBinding.inflate(LayoutInflater.from(context), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Review review = listReview.get(position);

        holder.binding.tvMovieName.setText(review.movie.title);
        holder.binding.tvOverview.setText(review.movie.overview);
        Glide.with(context)
                .load(Constants.IMAGE_URL + review.movie.backdropPath)
                .transition(DrawableTransitionOptions.withCrossFade())
                .placeholder(R.drawable.ic_image)
                .into(holder.binding.ivPoster);

        if(user != null) {
            if (!review.isDislike) {
                for (String yourLike : review.like) {
                    if (yourLike.equals(user.getId())) {
                        review.isLike = true;
                        holder.binding.ivLike.setImageResource(R.drawable.ic_choose_like);
                        break;
                    }
                }
            }
            if (!review.isLike) {
                for (String yourDislike : review.dislike) {
                    if (yourDislike.equals(user.getId())) {
                        review.isDislike = true;
                        holder.binding.ivDislike.setImageResource(R.drawable.ic_choose_dislike);
                        break;
                    }
                }
            }
        }
        holder.binding.tvName.setText(review.user.getName());
        holder.binding.tvDate.setText(NumberUtils.convertDateType7(review.createdAt));
        holder.binding.tvContent.setText(review.content);
        holder.binding.tvRate.setText(String.valueOf((int) review.rating));
        Glide.with(context)
                .load(review.user.getAvatar())
                .transition(DrawableTransitionOptions.withCrossFade())
                .placeholder(R.drawable.img_default_avt)
                .into(holder.binding.ivAvt);
        if(review.like != null) {
            Log.d("TAG", "tvLike: ");
            holder.binding.tvLike.setText(String.valueOf(review.like.size()));
        }
        if(review.dislike != null) {
            Log.d("TAG", "tvDislike: ");
            holder.binding.tvDislike.setText(String.valueOf(review.dislike.size()));
        }
        Log.d("TAG", "onBindViewHolder: " + review.like.size());
        if(user != null) {
            if (review.user.getId().equals(user.getId())) {
                holder.binding.ivMore.setVisibility(View.VISIBLE);
                holder.binding.ivMore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        PopupMenu popup = new PopupMenu(context, holder.binding.ivMore);
                        popup.getMenuInflater().inflate(R.menu.review_menu, popup.getMenu());
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            public boolean onMenuItemClick(MenuItem item) {
                                //Toast.makeText(context, "You Clicked : " + item.getItemId(), Toast.LENGTH_SHORT).show();
                                if (item.getTitle().toString().equals("Edit")) {
                                    callBack.updateReview(review);
                                } else if (item.getTitle().toString().equals("Delete")) {
                                    callBack.deleteReview(review.id);
                                }
                                return true;
                            }
                        });

                        popup.show();
                    }
                });
            }
        }
        holder.binding.layoutLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBack.likeReview(review.id);
                if (user != null) {
                    if (review.isLike) {
                        //unlike
                        holder.binding.ivLike.setImageResource(R.drawable.ic_like);
                        review.isLike = false;
                        if (review.like != null) {
                            Log.d("TAG", "tvLike: ");
                            holder.binding.tvLike.setText(String.valueOf(review.like.size() - 1));
                            review.like.remove(user.getId());
                        }
                    } else {
                        //like
                        holder.binding.ivLike.setImageResource(R.drawable.ic_choose_like);
                        review.isLike = true;
                        if (review.like != null) {
                            Log.d("TAG", "tvLike: ");
                            holder.binding.tvLike.setText(String.valueOf(review.like.size() + 1));
                            review.like.add(user.getId());
                        }
                        if (review.isDislike) {
                            //undislike
                            callBack.dislikeReview(review.id);
                            review.isDislike = false;
                            holder.binding.ivDislike.setImageResource(R.drawable.ic_dislike);
                            if (review.dislike != null) {
                                Log.d("TAG", "tvDislike: ");
                                holder.binding.tvDislike.setText(String.valueOf(review.dislike.size() - 1));
                                review.dislike.remove(user.getId());
                            }
                        }
                    }
                }

            }
        });

        holder.binding.layoutDislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBack.dislikeReview(review.id);
                if (user != null) {
                    if (review.isDislike) {
                        //undislike
                        review.isDislike = false;
                        holder.binding.ivDislike.setImageResource(R.drawable.ic_dislike);
                        if (review.dislike != null) {
                            Log.d("TAG", "tvDislike: ");
                            holder.binding.tvDislike.setText(String.valueOf(review.dislike.size() - 1));
                            review.dislike.remove(user.getId());
                        }
                    } else {
                        //dislike
                        review.isDislike = true;
                        holder.binding.ivDislike.setImageResource(R.drawable.ic_choose_dislike);
                        if (review.dislike != null) {
                            Log.d("TAG", "tvDislike: ");
                            holder.binding.tvDislike.setText(String.valueOf(review.dislike.size() + 1));
                            review.dislike.add(user.getId());
                        }
                        if (review.isLike) {
                            //unlike
                            callBack.likeReview(review.id);
                            review.isLike = false;
                            holder.binding.ivLike.setImageResource(R.drawable.ic_like);
                            if (review.like != null) {
                                Log.d("TAG", "tvLike: ");
                                holder.binding.tvLike.setText(String.valueOf(review.like.size() - 1));
                                review.like.remove(user.getId());
                            }
                        }
                    }
                }
            }
        });

        holder.binding.layoutMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBack.gotoMovieDetail(review.movie.id);
            }
        });

        if(review.content.length() > 150) {
            holder.binding.tvShowMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callBack.gotoReviewDetail(review);
                }
            });
        } else {
            holder.binding.tvShowMore.setVisibility(View.GONE);
        }
        holder.binding.tvContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBack.gotoReviewDetail(review);
            }
        });
        holder.binding.tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBack.gotoReviewDetail(review);
            }
        });

    }

    @Override
    public int getItemCount() {
        if(listReview != null){
            return listReview.size();
        }
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ItemMyReviewBinding binding;
        public MyViewHolder(ItemMyReviewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
