package com.cnjava.moviereview.view.fragment.editprofile;

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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.cnjava.moviereview.R;
import com.cnjava.moviereview.databinding.FragmentEditProfileBinding;
import com.cnjava.moviereview.model.User;
import com.cnjava.moviereview.util.CommonUtils;
import com.cnjava.moviereview.util.Constants;
import com.cnjava.moviereview.util.DialogUtils;
import com.cnjava.moviereview.util.NumberUtils;
import com.cnjava.moviereview.view.activity.main.MainViewModel;
import com.cnjava.moviereview.view.fragment.BaseFragment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import dagger.hilt.android.AndroidEntryPoint;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


@AndroidEntryPoint
public class EditProfileFragment extends BaseFragment<FragmentEditProfileBinding, EditProfileViewModel> {

    public static final String TAG = EditProfileFragment.class.getName();
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    ActivityResultLauncher<Intent> someActivityResultLauncher;
    private int defaultYear = 2001;
    private int defaultMonth = 0;
    private int defaultDay = 1;
    private Object mData;
    private String filePath = null;
    private final String[] items = {"Male", "Female"};
    private MainViewModel mainViewModel;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
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
                });
    }

    private User user;

    @Override
    protected Class<EditProfileViewModel> getClassVM() {
        return EditProfileViewModel.class;
    }

    @Override
    protected void initViews() {


        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        if (mainViewModel.yourProfileLD().getValue() == null) {
            mainViewModel.getYourProfile(CommonUtils.getInstance().getPref(Constants.ACCESS_TOKEN));
            mainViewModel.yourProfileLD().observe(this, me -> {
                user = me;
            });
        } else {
            user = mainViewModel.yourProfileLD().getValue();
        }

        //editProfileViewModel = new ViewModelProvider(this).get(EditProfileViewModel.class);

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
                binding.autoCompleteTxt.setText(getResources().getString(R.string.male));
            } else {
                binding.autoCompleteTxt.setText(getResources().getString(R.string.female));
            }
        }
        if (filePath == null) {
            Glide.with(context)
                    .load(user.getAvatar())
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .placeholder(R.drawable.progress_animation)
                    .error(R.drawable.img_default_avt)
                    .into(binding.ivAvatar);
        }

        binding.ivEdit.setOnClickListener(view -> {
            binding.btnConfirm.setVisibility(View.VISIBLE);
            binding.btCancel.setVisibility(View.VISIBLE);
            binding.ivEdit.setVisibility(View.INVISIBLE);
            binding.ivEdit.setEnabled(false);
            binding.etName.setEnabled(true);
            binding.etPhone.setEnabled(true);
            binding.etBirthday.setEnabled(true);
            binding.autoCompleteTxt.setEnabled(true);
            binding.btChangeImg.setVisibility(View.VISIBLE);
            binding.etName.setTextColor(ContextCompat.getColor(context, R.color.light_white));
            binding.etPhone.setTextColor(ContextCompat.getColor(context, R.color.light_white));
            binding.etBirthday.setTextColor(ContextCompat.getColor(context, R.color.light_white));
            binding.autoCompleteTxt.setTextColor(ContextCompat.getColor(context, R.color.light_white));
        });
        binding.btCancel.setOnClickListener(view -> {
            filePath = null;
            binding.btnConfirm.setVisibility(View.GONE);
            binding.btCancel.setVisibility(View.GONE);
            binding.ivEdit.setVisibility(View.VISIBLE);
            binding.ivEdit.setEnabled(false);
            binding.btChangeImg.setVisibility(View.GONE);
            binding.etName.setEnabled(false);
            binding.etPhone.setEnabled(false);
            binding.etBirthday.setEnabled(false);
            binding.autoCompleteTxt.setEnabled(false);
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
                    binding.autoCompleteTxt.setText(getResources().getString(R.string.male));
                } else {
                    binding.autoCompleteTxt.setText(getResources().getString(R.string.female));
                }
            } else {
                binding.autoCompleteTxt.setEnabled(false);
            }
            if (user.getAvatar() != null) {
                Glide.with(context)
                        .load(user.getAvatar())
                        .placeholder(R.drawable.progress_animation)
                        .error(R.drawable.img_default_avt)
                        .into(binding.ivAvatar);
            } else {
                binding.ivAvatar.setImageResource(R.drawable.img_default_avt);
            }
            binding.etName.setTextColor(ContextCompat.getColor(context, R.color.mid_white));
            binding.etPhone.setTextColor(ContextCompat.getColor(context, R.color.mid_white));
            binding.etBirthday.setTextColor(ContextCompat.getColor(context, R.color.mid_white));
            binding.autoCompleteTxt.setTextColor(ContextCompat.getColor(context, R.color.mid_white));
        });

        binding.etBirthday.setCursorVisible(false);
        binding.etBirthday.setShowSoftInputOnFocus(false);
        binding.ivBack.setOnClickListener(view -> callBack.backToPrev());
        binding.btChangeImg.setOnClickListener(view -> takeImageFromAlbumWithIntent());
        ArrayAdapter<String> adapterItems = new ArrayAdapter<>(context, R.layout.item_dropdown, items);
        binding.autoCompleteTxt.setAdapter(adapterItems);
        binding.autoCompleteTxt.setCursorVisible(false);
        binding.autoCompleteTxt.setShowSoftInputOnFocus(false);
        binding.autoCompleteTxt.setDropDownBackgroundResource(R.drawable.bg_light_dark_corner_10);

        binding.btnConfirm.setOnClickListener(view -> {
            String newName = binding.etName.getText().toString();
            String newPhone = binding.etPhone.getText().toString();
            String newBirthday = binding.etBirthday.getText().toString();
            String newGender = "male";
            if (binding.autoCompleteTxt.getText().toString().equals(getResources().getString(R.string.female))) {
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
                viewModel.getLiveDataIsLoading().observe(this, loading -> {
                    if (loading) {
                        DialogUtils.showLoadingDialog(context);
                    } else {
                        DialogUtils.hideLoadingDialog();
                        mainViewModel.getYourProfile(CommonUtils.getInstance().getPref(Constants.ACCESS_TOKEN));
                        callBack.reloadFragmentByTag(TAG);
                    }
                });
                if (filePath != null) {
                    File file = new File(filePath);
                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                    String suffix = file.getName().substring(file.getName().lastIndexOf("."));
                    imageFileName = user.getUsername() + "_" + timeStamp + suffix;
                    RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
                    MultipartBody.Part parts = MultipartBody.Part.createFormData("file", imageFileName, requestBody);
                    RequestBody someData = RequestBody.create(MediaType.parse("text/plain"), "This is a new Image");
                    if (CommonUtils.getInstance().getPref(Constants.ACCESS_TOKEN) != null) {
                        viewModel.uploadImageAccount(CommonUtils.getInstance().getPref(Constants.ACCESS_TOKEN), parts, someData);
                    }
                }
                User newUser = new User(newName, newPhone, newGender, convertDateType6(newBirthday));
                viewModel.updateProfile(newUser, CommonUtils.getInstance().getPref(Constants.ACCESS_TOKEN));

            } else {
                Toast.makeText(context, "No information has changed", Toast.LENGTH_SHORT).show();
            }

        });
        binding.etBirthday.setOnFocusChangeListener((view, b) -> {
            if (b) {
                showDatePickerDialog();
            }
        });
        binding.etBirthday.setOnClickListener(view -> showDatePickerDialog());
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
        tvDate.setText(NumberUtils.convertDateType4(convertDateType1(defaultDay, defaultMonth + 1, defaultYear)));
        picker.init(defaultYear, defaultMonth, defaultDay, (datePicker, year, month, dayOfMonth) -> {
            tvDate.setText(NumberUtils.convertDateType4(convertDateType1(dayOfMonth, month + 1, year)));
            defaultDay = dayOfMonth;
            defaultMonth = month;
            defaultYear = year;
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
            Toast.makeText(context, "You do not have permission to access", Toast.LENGTH_SHORT).show();
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
    }

    @Override
    public void apiError(String key, int code, Object data) {
    }

    public static boolean verifyStoragePermissions(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
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
    public void setData(Object data) {
        this.mData = data;
    }
}
