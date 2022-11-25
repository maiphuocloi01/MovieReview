package com.cnjava.moviereview.view.fragment.photoview;

import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cnjava.moviereview.databinding.FragmentPhotoViewBinding;
import com.cnjava.moviereview.util.ViewUtils;
import com.cnjava.moviereview.view.fragment.BaseFragment;
import com.cnjava.moviereview.viewmodel.CommonViewModel;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PhotoViewFragment extends BaseFragment<FragmentPhotoViewBinding, CommonViewModel> {

    public static final String TAG = PhotoViewFragment.class.getName();
    private static final String IMAGES_FOLDER_NAME = "Viewie";
    private Object mData;
    private String title;
    private String image;

    @Override
    public void apiSuccess(String key, Object data) {

    }

    @Override
    public void apiError(String key, int code, Object data) {

    }

    @Override
    protected Class<CommonViewModel> getClassVM() {
        return CommonViewModel.class;
    }

    @Override
    protected void initViews() {

        Bundle bundle = (Bundle) mData;
        title = bundle.getString("title");
        image = bundle.getString("image");
        binding.layoutFragmentPhotoView.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                requireActivity().runOnUiThread(() -> {
                    ViewUtils.gone(binding.progressCircularDv);
                    ViewUtils.show(binding.layoutPhotoFragment);
                });
            }
        }, 300);


        /*Picasso.get().load(image)
                .placeholder(R.drawable.progress_animation)
                .error(R.drawable.ic_movie2)
                .into(binding.photoView);*/
        /*Glide.with(context)
                .load(image)
                .placeholder(R.drawable.progress_animation)
                .into(binding.photoView);
        binding.ivBack.setOnClickListener(view -> callBack.backToPrev());
        binding.tvShowsText.setText(title);
        binding.photoView.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.photoView.zoomTo(1f, true);
            }
        }, 400);*/

    }

    private Bitmap mLoad(String string) {
        URL url = mStringToURL(string);
        HttpURLConnection connection;
        try {
            assert url != null;
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            return BitmapFactory.decodeStream(bufferedInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Function to convert string to URL
    private URL mStringToURL(String string) {
        try {
            return new URL(string);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void mSaveMediaToStorage(Bitmap bitmap, String name) {
        String filename = name + ".jpg";
        OutputStream fos = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, filename);
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/" + IMAGES_FOLDER_NAME);
            Uri imageUri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            if (imageUri != null) {
                try {
                    fos = context.getContentResolver().openOutputStream(imageUri);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }

        } else {
            String imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString() + File.separator + IMAGES_FOLDER_NAME;
            File file = new File(imagesDir);
            if (!file.exists()) {
                file.mkdir();
            }
            File image = new File(imagesDir, filename);
            try {
                fos = new FileOutputStream(image);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);

    }

    /*private void saveImage(Bitmap bitmap, @NonNull String name) throws IOException {
        boolean saved;
        OutputStream fos;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContentResolver resolver = context.getContentResolver();
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, name);
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, "DCIM/" + IMAGES_FOLDER_NAME);
            Uri imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            fos = resolver.openOutputStream(imageUri);
        } else {
            String imagesDir = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DCIM).toString() + File.separator + IMAGES_FOLDER_NAME;

            File file = new File(imagesDir);

            if (!file.exists()) {
                file.mkdir();
            }

            File image = new File(imagesDir, name + ".png");
            fos = new FileOutputStream(image);

        }

        saved = bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        fos.flush();
        fos.close();
    }*/


    @Override
    public void onResume() {
        super.onResume();
        binding.ivBack.setClickable(true);
        binding.ivBack.setOnClickListener(view -> callBack.backToPrev());
        binding.tvShowsText.setText(title);
        final Bitmap[] mImage = {null};
        ExecutorService myExecutor = Executors.newSingleThreadExecutor();
        Handler myHandler = binding.photoView.getHandler();
        myExecutor.execute(() -> {
            if (image != null && !image.equals("")) {
                mImage[0] = mLoad(image);
                if (mImage[0] != null) {
                    requireActivity().runOnUiThread(() -> binding.photoView.setImageBitmap(mImage[0]));
                }
            }
        });
        binding.ivDownload.setOnClickListener(view -> myHandler.post(() -> {
            if (mImage[0] != null) {
                mSaveMediaToStorage(mImage[0], title);
                Toast.makeText(context, "Save image success!", Toast.LENGTH_SHORT).show();
            }
        }));
    }

    @Override
    protected FragmentPhotoViewBinding initViewBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return FragmentPhotoViewBinding.inflate(inflater, container, false);
    }

    @Override
    public void setData(Object data) {
        this.mData = data;
    }
}
