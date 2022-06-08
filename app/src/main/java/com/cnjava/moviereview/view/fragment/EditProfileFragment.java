package com.cnjava.moviereview.view.fragment;

import static com.cnjava.moviereview.util.NumberUtils.convertDateType1;
import static com.cnjava.moviereview.util.NumberUtils.convertDateType2;
import static com.cnjava.moviereview.util.NumberUtils.convertDateType5;
import static com.cnjava.moviereview.util.NumberUtils.convertDateType6;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.cnjava.moviereview.MyApplication;
import com.cnjava.moviereview.R;
import com.cnjava.moviereview.databinding.FragmentEditProfileBinding;
import com.cnjava.moviereview.model.User;
import com.cnjava.moviereview.util.CommonUtils;
import com.cnjava.moviereview.util.Constants;
import com.cnjava.moviereview.util.DialogUtils;
import com.cnjava.moviereview.util.NumberUtils;
import com.cnjava.moviereview.viewmodel.CommonViewModel;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public class EditProfileFragment extends BaseFragment<FragmentEditProfileBinding, CommonViewModel> {

    public static final String TAG = EditProfileFragment.class.getName();
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    // You can do the assignment inside onAttach or onCreate, i.e, before the activity is displayed
    ActivityResultLauncher<Intent> someActivityResultLauncher;
    private User user = MyApplication.getInstance().getStorage().myUser;
    private int defaultYear = 2001;
    private int defaultMonth = 0;
    private int defaultDay = 1;
    private Object mData;
    private String filePath = null;

    public static boolean verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {

                            ContentResolver resolver = context.getContentResolver();
                            Intent data = result.getData();

                            Bitmap bitmap = null;
                            try {
                                if (data != null) {
                                    filePath = getRealPathFromURI(data.getData());
                                    bitmap = MediaStore.Images.Media.getBitmap(resolver, data.getData());
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            displayImage(bitmap);
                        }
                    }
                });
    }

    @Override
    protected Class<CommonViewModel> getClassVM() {
        return CommonViewModel.class;
    }

    @Override
    protected void initViews() {

        if (user.getName() != null) {
            binding.etName.setText(user.getName());
        }
        if (user.getPhone() != null) {
            binding.etPhone.setText(user.getPhone());
        }
        if (user.getEmail() != null) {
            binding.etEmail.setText(user.getEmail());
        }
        if (user.getBirthday() != null) {
            binding.etBirthday.setText(convertDateType5(user.getBirthday()));
        }

        if (user.getGender() != null) {
            if (user.getGender().equals("male")) {
                binding.rbMale.setChecked(true);
            } else {
                binding.rbFemale.setChecked(true);
            }
        }

        if (user.getAvatar() != null) {
            Glide.with(context)
                    .load(String.format(user.getAvatar()))
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .placeholder(R.drawable.img_default_avt)
                    .into(binding.ivAvatar);
        }

        binding.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.btnConfirm.setVisibility(View.VISIBLE);
                binding.btCancel.setVisibility(View.VISIBLE);
                binding.ivEdit.setVisibility(View.GONE);
                binding.etName.setEnabled(true);
                binding.etPhone.setEnabled(true);
                binding.etBirthday.setEnabled(true);
                binding.rbFemale.setEnabled(true);
                binding.rbMale.setEnabled(true);
                binding.btChangeImg.setVisibility(View.VISIBLE);

            }
        });
        binding.btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filePath = null;
                binding.btnConfirm.setVisibility(View.GONE);
                binding.btCancel.setVisibility(View.GONE);
                binding.ivEdit.setVisibility(View.VISIBLE);
                binding.btChangeImg.setVisibility(View.GONE);
                binding.etName.setEnabled(false);
                binding.etPhone.setEnabled(false);
                binding.etBirthday.setEnabled(false);
                binding.rbFemale.setEnabled(false);
                binding.rbMale.setEnabled(false);
                if (user.getName() != null) {
                    binding.etName.setText(user.getName());
                } else {
                    binding.etName.setText("");
                }
                if (user.getPhone() != null) {
                    binding.etPhone.setText(user.getPhone());
                } else {
                    binding.etPhone.setText("");
                }
                if (user.getBirthday() != null) {
                    binding.etBirthday.setText(convertDateType5(user.getBirthday()));
                } else {
                    binding.etBirthday.setText("");
                }

                if (user.getGender() != null) {
                    if (user.getGender().equals("male")) {
                        binding.rbMale.setChecked(true);
                    } else {
                        binding.rbFemale.setChecked(true);
                    }
                } else {
                    binding.rbMale.setChecked(false);
                    binding.rbFemale.setChecked(false);
                }
                if (user.getAvatar() != null) {
                    Glide.with(context)
                            .load(String.format(user.getAvatar()))
                            .transition(DrawableTransitionOptions.withCrossFade())
                            .placeholder(R.drawable.img_default_avt)
                            .into(binding.ivAvatar);
                } else {
                    binding.ivAvatar.setImageResource(R.drawable.img_default_avt);
                }
            }
        });


        binding.etBirthday.setCursorVisible(false);
        binding.etBirthday.setShowSoftInputOnFocus(false);

        binding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBack.backToPrev();
            }
        });

        binding.btChangeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeImageFromAlbumWithIntent();
            }
        });

        binding.btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newName = binding.etName.getText().toString();
                String newPhone = binding.etPhone.getText().toString();
                String newBirthday = binding.etBirthday.getText().toString();
                String newGender = "male";
                if (binding.rbFemale.isChecked()) {
                    newGender = "female";
                }
                String imageFileName = null;

                if (newName.equals("")) {
                    binding.etName.setError("Please fill info");
                } else if (newPhone.equals("")) {
                    binding.etPhone.setError("Please fill info");
                } else if (newBirthday.equals("")) {
                    binding.etBirthday.setError("Please fill info");
                } else if (!newName.equals(user.getName()) ||
                        !newPhone.equals(user.getPhone()) ||
                        !convertDateType6(newBirthday).equals(user.getBirthday()) ||
                        !newGender.equals(user.getGender()) ||
                        filePath != null
                ) {
                    DialogUtils.showLoadingDialog(context);

                    if (filePath != null) {
                        Log.d(TAG, filePath);
                        File file = new File(filePath);
                        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                        String suffix = file.getName().substring(file.getName().lastIndexOf("."));
                        imageFileName = user.getUsername() + "_" + timeStamp + suffix;
                        Log.d(TAG, file.toString());
                        Log.d(TAG, file.getName());
                        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
                        MultipartBody.Part parts = MultipartBody.Part.createFormData("newimage", imageFileName, requestBody);

                        RequestBody someData = RequestBody.create(MediaType.parse("text/plain"), "This is a new Image");
                        viewModel.uploadImageAccount(parts, someData);

                    }
                    String linkAvatar = "https://nhom01nt118.azurewebsites.net/Assets/Images/Account/" + imageFileName;
                    User newUser;
                    Log.d(TAG, "onClick: " + imageFileName + " / " + newName + " / " + convertDateType6(newBirthday) + " / " + newGender + " / " + newPhone);
                    if(imageFileName == null){
                        newUser = new User(newName, newPhone, newGender, convertDateType6(newBirthday));
                    } else {
                        newUser = new User(newName, linkAvatar, newPhone, newGender, convertDateType6(newBirthday));
                    }
                    viewModel.updateProfile(newUser, CommonUtils.getInstance().getPref(Constants.ACCESS_TOKEN));


                } else {
                    Toast.makeText(context, "Không có thông tin nào thay đổi", Toast.LENGTH_SHORT).show();
                }


            }
        });


        binding.etBirthday.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    showDatePickerDialog();
                }
            }
        });

        binding.etBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });
    }

    @Override
    protected FragmentEditProfileBinding initViewBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        return FragmentEditProfileBinding.inflate(inflater, container, false);
    }

    private void showDatePickerDialog() {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_date_picker);
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

        Button btnCancel = dialog.findViewById(R.id.bt_cancel2);
        Button btnConfirm = dialog.findViewById(R.id.bt_confirm2);
        TextView tvDate = dialog.findViewById(R.id.tv_date);
        DatePicker picker = dialog.findViewById(R.id.datePicker);
        //Calendar calendar = Calendar.getInstance();
        //calendar.setTimeInMillis(System.currentTimeMillis());

        tvDate.setText(NumberUtils.convertDateType4(convertDateType1(defaultDay, defaultMonth + 1, defaultYear)));
        picker.init(defaultYear, defaultMonth, defaultDay, new DatePicker.OnDateChangedListener() {

            @Override
            public void onDateChanged(DatePicker datePicker, int year, int month, int dayOfMonth) {
                tvDate.setText(NumberUtils.convertDateType4(convertDateType1(dayOfMonth, month + 1, year)));
                defaultDay = dayOfMonth;
                defaultMonth = month;
                defaultYear = year;
            }
        });

        btnCancel.setOnClickListener(view -> dialog.dismiss());

        btnConfirm.setOnClickListener(view -> {
            binding.etBirthday.setText(convertDateType2(picker.getDayOfMonth(), picker.getMonth(), picker.getYear()));
            dialog.dismiss();
        });
        dialog.show();
    }

    public void takeImageFromAlbumWithIntent() {
        if (verifyStoragePermissions(getActivity())) {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            someActivityResultLauncher.launch(intent);
        } else {
            Toast.makeText(context, "Bạn chưa có quyền truy cập", Toast.LENGTH_SHORT).show();
        }
    }

    private void displayImage(Bitmap bitmap) {
        binding.ivAvatar.setImageBitmap(bitmap);
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = context.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    @Override
    public void apiSuccess(String key, Object data) {
        if (key.equals(Constants.KEY_UPLOAD_IMAGE)) {
            ResponseBody res = (ResponseBody) data;
            Log.d(TAG, res.toString());
        } else if (key.equals(Constants.KEY_UPDATE_YOUR_PROFILE)) {
            viewModel.getYourProfile(CommonUtils.getInstance().getPref(Constants.ACCESS_TOKEN));
        } if (key.equals(Constants.KEY_GET_YOUR_PROFILE)) {
            DialogUtils.hideLoadingDialog();
            Toast.makeText(context, "Update profile success", Toast.LENGTH_SHORT).show();
            User user = (User) data;
            MyApplication.getInstance().getStorage().myUser = user;
            callBack.backToPrev();
        }
    }

    @Override
    public void apiError(String key, int code, Object data) {
        DialogUtils.hideLoadingDialog();
        if (key.equals(Constants.KEY_UPDATE_YOUR_PROFILE)){
            Log.d(TAG, "KEY_UPDATE_YOUR_PROFILE: ");
        }
        if (key.equals(Constants.KEY_GET_YOUR_PROFILE)){
            Log.d(TAG, "KEY_GET_YOUR_PROFILE: ");
        }
        if (code == 999) {
            Log.d(TAG, "apiError: " + data.toString());
            Toast.makeText(context, "Unable to connect server", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void setData(Object data) {
        this.mData = data;
    }
}
