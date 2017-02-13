package com.angkorcab.fragments;

import android.Manifest;
import android.Manifest.permission;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.angkorcab.taxi.MainActivity;
import com.angkorcab.taxi.R;
import com.angkorcab.model.ApiClient;
import com.angkorcab.model.ApiInterface;
import com.angkorcab.model.MyPojo;
import com.angkorcab.model.UserData;
import com.angkorcab.utils.SharedPreferencesUtility;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateProfilePictureFragment extends BaseFragment implements View.OnClickListener {
    private static final int TAKE_REQUEST = 1888;
    private static final int SELECT_REQUEST = 1;
    private ImageView pic_imageview;
    private Context context;
    private UserData userdata;
    private ImageButton updateToServer, takePicture, selectPicture;
    private final int MY_PERMISSIONS_REQUEST_LOCATION = 5;
    String base64String;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_update_profile_picture, null);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.item_update_profilepic);
        context = getActivity();
        try {
            pic_imageview = (ImageView) root.findViewById(R.id.update_profile_preview);
            updateToServer = (ImageButton) root.findViewById(R.id.update_to_Server);
            takePicture = (ImageButton) root.findViewById(R.id.take_profile_picture);
            selectPicture = (ImageButton) root.findViewById(R.id.select_profile_pic);
            updateToServer.setOnClickListener(this);
            takePicture.setOnClickListener(this);
            selectPicture.setOnClickListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return root;
    }

    @Override
    protected void backPressed() {
        try {
            replaceFragment(context, new ProfileFragment());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.update_to_Server:
                if (pic_imageview.getDrawable() == null) {
                    Toast.makeText(context, R.string.image_not_selected_msg,
                            Toast.LENGTH_SHORT);
                } else {
                    Drawable d = pic_imageview.getDrawable();
                    Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
                    byte[] ba;
                    do {
                        ByteArrayOutputStream bao = new ByteArrayOutputStream();
                        Log.e("BEFORE REDUCING",
                                bitmap.getHeight() + " " + bitmap.getWidth()
                                        + " " + bitmap.getRowBytes()
                                        * bitmap.getHeight());
                        Log.e("After REDUCING",
                                bitmap.getHeight() + " " + bitmap.getWidth()
                                        + " " + bitmap.getRowBytes()
                                        * bitmap.getHeight());
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bao);
                        ba = bao.toByteArray();
                        if ((ba.length / 1024) >= 650) {
                            bitmap = Bitmap.createScaledBitmap(bitmap,
                                    (int) (bitmap.getWidth() * 0.95),
                                    (int) (bitmap.getHeight() * 0.95), true);
                        }
                        Log.e("BYTE LENGTH", "" + ba.length / 1024);
                    } while ((ba.length / 1024) >= 650);
                    base64String = Base64.encodeToString(ba, Base64.DEFAULT);
                    UpdateProfilePicture(base64String);
                }
                break;
            case R.id.take_profile_picture:
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{permission.CAMERA},
                            MY_PERMISSIONS_REQUEST_LOCATION);
                } else {
                    startActivityForResult(new Intent(
                                    android.provider.MediaStore.ACTION_IMAGE_CAPTURE),
                            TAKE_REQUEST);
                }
                break;
            case R.id.select_profile_pic:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, SELECT_REQUEST);
                break;
        }
    }

    public void UpdateProfilePicture(final String base64String) {
        ApiInterface api = ApiClient.getClient().create(ApiInterface.class);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("updatepic", base64String);
        hashMap.put("email", SharedPreferencesUtility.loadUserEmail(context));
        Call<MyPojo> calle = api.updateProfilePic(hashMap);
        calle.enqueue(new Callback<MyPojo>() {
            @Override
            public void onResponse(Call<MyPojo> call, Response<MyPojo> response) {
                try {
                    String result = response.body().getSuccess();
                    if (result.equalsIgnoreCase("1")) {
                        //DriverDetails.setProfilePic(bitmapstring);
                        SharedPreferencesUtility.saveProfilePic(context, base64String);
                        ProfileFragment fragment = new ProfileFragment();
                        ((MainActivity) context).getSupportFragmentManager().beginTransaction().replace(R.id.containerView, fragment).commit();
                    } else {
                        Toast.makeText(context, R.string.profile_updated_fail_msg,
                                Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<MyPojo> call, Throwable t) {
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Now user should be able to use camera
                startActivityForResult(new Intent(
                                android.provider.MediaStore.ACTION_IMAGE_CAPTURE),
                        TAKE_REQUEST);
            } else {
                // Your app will not have this permission. Turn off all functions
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent
            data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            Bitmap pic = null;
            updateToServer.setVisibility(View.VISIBLE);
            if (requestCode == TAKE_REQUEST) {
                pic = (Bitmap) data.getExtras().get("data");
                pic_imageview.setImageBitmap(pic);
            } else if (requestCode == SELECT_REQUEST)
                try {
                    InputStream stream = getActivity().getContentResolver().openInputStream(
                            data.getData());
                    pic = BitmapFactory.decodeStream(stream);
                    stream.close();
                    pic_imageview.setImageBitmap(pic);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(context, R.string.updating_status, Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(context, R.string.updating_status, Toast.LENGTH_LONG).show();
                }
        }
    }
}
