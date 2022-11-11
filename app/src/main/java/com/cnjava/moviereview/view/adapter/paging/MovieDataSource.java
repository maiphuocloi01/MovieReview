package com.cnjava.moviereview.view.adapter.paging;

/*
public class MovieDataSource extends PageKeyedDataSource<Integer, Movie> {

    private static final int FIRST_PAGE = 1;
    private static final String TAG = "ItemDataSource";
    public static final int PAGE_SIZE = 9;
    String search ;
    private Repository repository;


    CompositeDisposable compositelist=new CompositeDisposable();



    public MovieDataSource(String search) {
        this.search=search;
    }

    @Override
    public void loadInitial(@NonNull @NotNull LoadInitialParams<Integer> params, @NonNull @NotNull LoadInitialCallback<Integer, Item> callback) {

        PostsClient.getINSTANCE().getApi().get_users(search,FIRST_PAGE,PAGE_SIZE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model_user -> callback.onResult(model_user.getItems(),null,FIRST_PAGE+1),
                        error -> Log.e("viwModel", error.getMessage())
                );





    }


    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Item> callback) {

        Integer adjacentKey = (params.key > 1) ? params.key - 1 : null;
        PostsClient.getINSTANCE().getApi().get_users(search,params.key,PAGE_SIZE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model_user -> callback.onResult(model_user.getItems(),adjacentKey),
                        error -> Log.e("viwModel", error.getMessage())

                );




    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Item> callback) {
        PostsClient.getINSTANCE().getApi().get_users(search,params.key,PAGE_SIZE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model_user -> callback.onResult(model_user.getItems(),params.key+1),
                        error -> Log.e("viwModel", error.getMessage())

                );




    }
}
*/
