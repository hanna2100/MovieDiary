package com.example.user.moviediary.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.user.moviediary.MainActivity;
import com.example.user.moviediary.R;
import com.example.user.moviediary.model.UserData;
import com.example.user.moviediary.util.DbOpenHelper;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_CANCELED;

public class FrgUserProfileEdit extends Fragment implements View.OnClickListener {
    private Context mContext;
    private View view;

    private DrawerLayout profileMainLayout;
    private LinearLayout profileDrawerLayout;

    private CircleImageView profileImage;
    private Button btnEditProfileImage, btnEditCancel, btnEditSave, btnAdultCheck;
    private EditText txtName, txtDescription;

    private File tempFile;
    private File copyFile;
    private String profileImgPath;

    private DbOpenHelper dbOpenHelper;
    // 리퀘스트코드
    private static final int PICK_FROM_CAMERA = 2;
    private static final int PICK_FROM_ALBUM = 1;

    //Shared Preference 키값
    private static final String USER = "User", INIT = "init";

    public static FrgUserProfileEdit newInstance() {
        FrgUserProfileEdit fragment = new FrgUserProfileEdit();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user_profile, container, false);
        profileMainLayout = view.findViewById(R.id.profileMainLayout);
        profileDrawerLayout = view.findViewById(R.id.profileDrawerLayout);

        profileImage = view.findViewById(R.id.profileImage);

        btnEditProfileImage = view.findViewById(R.id.btnEditProfileImage);
        btnEditCancel = view.findViewById(R.id.btnEditCancel);
        btnEditSave = view.findViewById(R.id.btnEditSave);
        btnAdultCheck = view.findViewById(R.id.btnAdultCheck);

        txtName = view.findViewById(R.id.txtName);
        txtDescription = view.findViewById(R.id.txtDescription);

        // 초기 세팅
        setupProfile();
        // 이름은 수정하지 못함
        txtName.setEnabled(false);

        //액션바 숨기기
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();

        TedPermission.with(mContext)
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        Toast.makeText(mContext, "카메라 권한 요청 허용되었습니다.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                        Toast.makeText(mContext, "카메라 권한 요청 거절되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                })
                .setRationaleMessage("카메라 권한 허용 요청")
                .setDeniedMessage("요청 거절시 카메라를 사용할 수 없습니다.")
                .setPermissions(Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .check();


        btnEditProfileImage.setOnClickListener(this);
        btnEditCancel.setOnClickListener(this);
        btnEditSave.setOnClickListener(this);

        return view;
    }

    private void setupProfile() {
        txtName.setText(UserData.userName);
        txtDescription.setText(UserData.diaryDescription);
        if (UserData.profileImgPath != null)
            profileImage.setImageURI(Uri.parse(UserData.profileImgPath));
        else {
            profileImage.setImageResource(R.drawable.user_default_image);
            profileImage.setColorFilter(MainActivity.mainColor);
        }
        if (UserData.kakaoLogin == 0) {
            btnAdultCheck.setText("성인인증하기");
            btnAdultCheck.setBackgroundColor(Color.LTGRAY);
            btnAdultCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 카카오톡 열어서 성인인증 하게 해,,,?
                }
            });
        } else if (UserData.kakaoLogin == 1) {
            btnAdultCheck.setText("완료");
            btnAdultCheck.setBackgroundColor(Color.YELLOW);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnEditProfileImage:
                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(mContext);
                bottomSheetDialog.setContentView(R.layout.user_profile_image_dialog);
                Button btnPofileImageDelete = bottomSheetDialog.findViewById(R.id.btnPofileImageDelete);
                Button btnPofileImageCameraOn = bottomSheetDialog.findViewById(R.id.btnPofileImageCameraOn);
                Button btnPofileImageLibraryOn = bottomSheetDialog.findViewById(R.id.btnPofileImageLibraryOn);

                btnPofileImageDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        profileImage.setImageResource(R.drawable.user_default_image);
                        bottomSheetDialog.dismiss();
                    }
                });
                btnPofileImageCameraOn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                        try {
                            tempFile = createImageFile();
                        } catch (IOException e) {
                            Toast.makeText(mContext, "이미지 처리 오류! 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                            bottomSheetDialog.dismiss();
                            e.printStackTrace();
                        }
                        if (tempFile != null) {

                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {

                                Uri photoUri = FileProvider.getUriForFile(mContext,
                                        "com.example.user.moviediary.fragment.provider", tempFile);
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                                startActivityForResult(intent, PICK_FROM_CAMERA);

                            } else {

                                Uri photoUri = Uri.fromFile(tempFile);
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                                startActivityForResult(intent, PICK_FROM_CAMERA);

                            }
                        }
                        bottomSheetDialog.dismiss();
                    }
                });
                btnPofileImageLibraryOn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                        startActivityForResult(intent, PICK_FROM_ALBUM);
                        bottomSheetDialog.dismiss();
                    }
                });
                bottomSheetDialog.show();
                break;
            case R.id.btnEditCancel:
                ((MainActivity) mContext).setChangeFragment(FrgUser.newInstance());
                break;
            case R.id.btnEditSave:
                String description = txtDescription.getText().toString().trim();
                String name = txtName.getText().toString().trim();

                if (!description.equals("") && !name.equals("")) {

                    Log.d("User_check", name + ", " + description + ", " + profileImgPath);

                    //디비에 저장
                    dbOpenHelper = new DbOpenHelper(getContext());
                    dbOpenHelper.openUser();
                    dbOpenHelper.updateUserColumn(name, profileImgPath, description, 0);
//                    dbOpenHelper.upgradeUserHelper();
//                    dbOpenHelper.createUserHelper();
//                    dbOpenHelper.insertUserColumn(name, profileImgPath, description, 0);
                    dbOpenHelper.close();


                    //프로필 설정 완료했음을 저장
                    SharedPreferences.Editor editor = mContext.getSharedPreferences(USER, Context.MODE_PRIVATE).edit();
                    editor.putBoolean(INIT, true);
                    editor.apply();

                    ((MainActivity) mContext).setupUserProfile();
                    ((MainActivity) mContext).setChangeFragment(FrgUser.newInstance());
                } else {
                    Toast.makeText(mContext, "입력되지 않은 정보가 있습니다.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != RESULT_CANCELED) {
            if (requestCode == PICK_FROM_CAMERA) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                Bitmap originalBm = BitmapFactory.decodeFile(tempFile.getAbsolutePath(), options);

                ExifInterface exifInterface = null;
                // 속성을 체크해야된다.
                try {
                    exifInterface = new ExifInterface(tempFile.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                int exifOrientation; // 방향 설정값 저장 변수
                int exifDegree; // Degree 설정값 저장 변수
                if (exifInterface != null) {
                    exifOrientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                            ExifInterface.ORIENTATION_NORMAL);
                    exifDegree = exifOrientationToDegree(exifOrientation);
                } else {
                    exifDegree = 0;
                }

                Bitmap bitmapTemp = rotate(originalBm, exifDegree);
                profileImage.setImageBitmap(bitmapTemp);
            } else if (requestCode == PICK_FROM_ALBUM) {

                Uri photoUri = data.getData();

                Cursor cursor = null;

                try {
                    // Uri 스키마를 content:/// 에서 file:/// 로  변경한다
                    String[] proj = {MediaStore.Images.Media.DATA};

                    assert photoUri != null;
                    cursor = mContext.getContentResolver().query(photoUri, proj, null, null, null);

                    assert cursor != null;
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

                    cursor.moveToFirst();

                    tempFile = new File(cursor.getString(column_index));
                    copyFile = createImageFile();

                    Thread copyImage = new Thread(new Runnable() {
                        @Override
                        public void run() {

                            FileInputStream fi = null;
                            FileOutputStream fo = null;
                            byte[] buf = null;

                            try {
                                fi = new FileInputStream(tempFile.getAbsolutePath());
                                fo = new FileOutputStream(copyFile.getAbsolutePath());

                                buf = new byte[1024];

                                int length;
                                while ((length = fi.read(buf)) > 0) {
                                    fo.write(buf, 0, length);
                                    //파일 전부를 옮길때까지 이걸 반복
                                }
                                fo.flush();

                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            BitmapFactory.Options options = new BitmapFactory.Options();
                            final Bitmap originalBm = BitmapFactory.decodeFile(copyFile.getAbsolutePath(), options);

                            ((MainActivity) mContext).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    profileImage.setImageBitmap(originalBm);
                                }
                            });
                        }
                    });

                    copyImage.start();

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }
            }
        }
    }

    // 각도를 조절해서 다시 만든 비트맵
    private Bitmap rotate(Bitmap bitmap, int exifDegree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(exifDegree);
        Bitmap tempBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(),
                matrix, true);
        return tempBitmap;
    }

    private int exifOrientationToDegree(int exifOrientation) {

        switch (exifOrientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return 90;
            case ExifInterface.ORIENTATION_ROTATE_180:
                return 180;
            case ExifInterface.ORIENTATION_ROTATE_270:
                return 270;
        }
        return 0;
    }

    private File createImageFile() throws IOException {

        // 이미지 파일 이름 ( profileImage_{시간}_ )
        String timeStamp = new SimpleDateFormat("HHmmss").format(new Date());
        String imageFileName = "profileImage_" + timeStamp + "_";

        // 이미지가 저장될 폴더 이름
        File storageDir = new File(Environment.getExternalStorageDirectory() + "/ProfileImage/");

        // 프로필 이미지 바꿀때 안에 있는 파일 지워줌. 용량 차지하지 못하게 한다.
        if (storageDir.exists()) {
            File[] fileList = storageDir.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                if (fileList.length == 0) {
                    break;
                }
                fileList[i].delete();
                Toast.makeText(mContext, "파일삭제해씀", Toast.LENGTH_SHORT).show();
            }
            //storageDir.delete();
        } else if (!storageDir.exists()) {
            storageDir.mkdirs();
            Toast.makeText(mContext, "디렉토리만들겨", Toast.LENGTH_SHORT).show();
        }

        // 빈 파일 생성
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        //이미지경로저장
        profileImgPath = image.getAbsolutePath();

        return image;
    }
}
