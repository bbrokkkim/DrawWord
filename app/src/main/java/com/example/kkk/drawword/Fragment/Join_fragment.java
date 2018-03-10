package com.example.kkk.drawword.Fragment;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.kkk.drawword.Activity.MainActivity;
import com.example.kkk.drawword.Okhttp.OkhttpUser;
import com.example.kkk.drawword.R;


import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import butterknife.BindView;
import butterknife.ButterKnife;

import static java.util.Collections.rotate;

/**
 * Created by KKK on 2018-02-10.
 */

//가입 프레그먼트
public class Join_fragment extends Fragment implements View.OnClickListener{
    @BindView(R.id.id)
    EditText user_id;
    @BindView(R.id.password1) EditText user_pwd1;
    @BindView(R.id.password2) EditText user_pwd2;
    @BindView(R.id.name) EditText user_name;
    @BindView(R.id.joinbutton)
    Button submit;
    @BindView(R.id.back_photo) Button back_button;
    @BindView(R.id.choice_sex)
    Spinner sex_spinner;
    @BindView(R.id.join_photo)
    ImageView photo;
    @BindView(R.id.join_photo_select) Button photo_select;
    String id, pwd1,pwd2 ,name,sex;
    int a = 1;
    int REQ_CODE_SELECT_IMAGE = 1;
    int exifDegree = 0;
    Uri uri;
    String real_uri;
    Bitmap image_bitmap;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.join_fragment,container,false);

        ButterKnife.bind(this,view);
        photo.setImageResource(R.drawable.defoult);
        submit.setOnClickListener(this);
        photo_select.setOnClickListener(this);
        back_button.setOnClickListener(this);
        MainActivity.where = true;
        return view;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //회원가입 완료
            case R.id.joinbutton:
                id = user_id.getText().toString();
                pwd1 = user_pwd1.getText().toString();
                pwd2 = user_pwd2.getText().toString();
                name = user_name.getText().toString();
                sex = (String) sex_spinner.getSelectedItem();
                JoinMent(id,pwd1,pwd2,name,sex);
                break;
            //이미지 선택
            case R.id.join_photo_select:

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
                intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,REQ_CODE_SELECT_IMAGE);

                //마쉬멜로우 이상 권한 얻기
                if(PermissionStatus(Manifest.permission.READ_EXTERNAL_STORAGE)){
                    PermissionGet();
                }
                else {
                    PermissionGet();
                }
                back_button.bringToFront();
                break;
            //이미지 초기화
            case R.id.back_photo :
                Toast.makeText(getActivity(), "이미지가 초기화 되었습니다.", Toast.LENGTH_SHORT).show();
                uri = null;
                photo.setImageResource(R.drawable.defoult);
//                photo.set

                break;
        }
    }

    //회원가입 유효성 검사
    void JoinMent(String id,String pwd1, String pwd2, String name,String sex){
        if (name.equals("") || pwd1.equals("") || id.equals("") || sex.equals("남/녀")) {
            Toast.makeText(getActivity(), "정보를 입력해주세요.", Toast.LENGTH_SHORT).show();
        } else if (!pwd1.equals(pwd2)) {
            Toast.makeText(getActivity(), "비밀번호가 다릅니다.", Toast.LENGTH_SHORT).show();
        } else {
            if (uri == null){
                a = 1;
                real_uri = "null";
            }
            Log.d("urlurlurl",String.valueOf(uri));
            Log.d("real urlurlurl",real_uri);
            OkhttpUser okhttpUser = new OkhttpUser(getActivity(), MainActivity.database);
            okhttpUser.execute("1", id, pwd1, name, "01012341234", "12", sex, real_uri,String.valueOf(exifDegree),image_bitmap);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQ_CODE_SELECT_IMAGE) {
            if(resultCode == Activity.RESULT_OK) {
//                Toast.makeText(getActivity(), "test2", Toast.LENGTH_SHORT).show();
                /*photo.setImageURI(data.getData());
                String imagePath = imageUri.getPath();
                uri = data.getData();

                Bitmap image = BitmapFactory.decodeFile(data.getData());

                ExifInterface exif = new ExifInterface(imagePath);
                int exifOrientation = exif.getAttributeInt(
                        ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                int exifDegree = exifOrientationToDegrees(exifOrientation);
                image = rotate(image, exifDegree);
*/
                try {
                    // 비트맵 이미지로 가져온다
                    uri = data.getData();


                    String[] proj = { MediaStore.Images.Media.DATA };

                    CursorLoader cursorLoader = new CursorLoader(getActivity(), uri, proj, null, null, null);
                    Cursor cursor1 = cursorLoader.loadInBackground();

                    int column_index = cursor1.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor1.moveToFirst();
                    real_uri = cursor1.getString(column_index);



                    image_bitmap= MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                    ExifInterface exif = new ExifInterface(real_uri);

                    int orientation = exif.getAttributeInt(
                            ExifInterface.TAG_ORIENTATION,
                            ExifInterface.ORIENTATION_NORMAL);
                    exifDegree = exifOrientationToDegrees(orientation);
                    Toast.makeText(getActivity(), ""+exifDegree, Toast.LENGTH_SHORT).show();
//                    Matrix matrix = new Matrix();
//                    matrix.postRotate(exifDegree);
//                    image_bitmap = Bitmap.createBitmap()
//                    image_bitmap = rotate(image_bitmap., exifDegree);
                    int width = image_bitmap.getWidth();
                    int height = image_bitmap.getHeight();



                    if (exifDegree != 0){
                        Toast.makeText(getActivity(), String.valueOf(exifDegree) + "1", Toast.LENGTH_SHORT).show();
                        Matrix matrix = new Matrix();
                        matrix.postRotate(exifDegree);
                        Toast.makeText(getActivity(), String.valueOf(exifDegree) + "111", Toast.LENGTH_SHORT).show();
                        image_bitmap = Bitmap.createBitmap(image_bitmap, 0, 0, width, height, matrix, true);
                        Toast.makeText(getActivity(), String.valueOf(exifDegree) + "11", Toast.LENGTH_SHORT).show();
//                        image_bitmap = Bitmap.createScaledBitmap(image_bitmap,width/10,height/1,true);
//                        image_bitmap.recycle();
//                        Toast.makeText(getActivity(), ""+ exifDegree + "1", Toast.LENGTH_SHORT).show();
                    }
                    else {

                        Toast.makeText(getActivity(), String.valueOf(exifDegree) + "2", Toast.LENGTH_SHORT).show();
                    }
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 4;
                    Bitmap src = BitmapFactory.decodeFile(real_uri, options);
                    image_bitmap = Bitmap.createScaledBitmap(image_bitmap, width/2, height/2, true);
                    Toast.makeText(getActivity(), String.valueOf(exifDegree) + "111", Toast.LENGTH_SHORT).show();
//                    matrix.postRotate(-90);

//                    Bitmap resizedBitmap = Bitmap.createBitmap(image_bitmap, 0, 0, width, height, matrix, true);

//                    image_bitmap = BitmapFactory.cre



                    // 변환된 이미지 사용
                    photo.setImageBitmap(image_bitmap);
                } catch(Exception e) {
                }





/*
                String[] projection = {MediaStore.Images.Media.DATA};
                //apk static 에러 때문에 독립되게 fragment 만드니 에러 뜸
                Cursor cursor = managedQuery(uri, projection, null, null, null);
                startManagingCursor(cursor);
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                real_uri = cursor.getString(columnIndex);




*/
               /* photo.setImageURI(data.getData());
                uri = data.getData();


                String[] proj = { MediaStore.Images.Media.DATA };

                CursorLoader cursorLoader = new CursorLoader(getActivity(), uri, proj, null, null, null);
                Cursor cursor1 = cursorLoader.loadInBackground();

                int column_index = cursor1.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor1.moveToFirst();
                real_uri = cursor1.getString(column_index);*/
            }
        }
    }





    public static Bitmap rotateBitmap(String src, Bitmap bitmap) {
        try {
            int orientation = getExifOrientation(src);

            if (orientation == 1) {
                return bitmap;
            }

            Matrix matrix = new Matrix();
            switch (orientation) {
                case 2:
                    matrix.setScale(-1, 1);
                    break;
                case 3:
                    matrix.setRotate(180);
                    break;
                case 4:
                    matrix.setRotate(180);
                    matrix.postScale(-1, 1);
                    break;
                case 5:
                    matrix.setRotate(90);
                    matrix.postScale(-1, 1);
                    break;
                case 6:
                    matrix.setRotate(90);
                    break;
                case 7:
                    matrix.setRotate(-90);
                    matrix.postScale(-1, 1);
                    break;
                case 8:
                    matrix.setRotate(-90);
                    break;
                default:
                    return bitmap;
            }

            try {
                Bitmap oriented = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                bitmap.recycle();
                return oriented;
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
                return bitmap;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    private static int getExifOrientation(String src) throws IOException {
        int orientation = 1;

        try {
            /**
             * if your are targeting only api level >= 5
             * ExifInterface exif = new ExifInterface(src);
             * orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
             */
            if (Build.VERSION.SDK_INT >= 5) {
                Class<?> exifClass = Class.forName("android.media.ExifInterface");
                Constructor<?> exifConstructor = exifClass.getConstructor(new Class[] { String.class });
                Object exifInstance = exifConstructor.newInstance(new Object[] { src });
                Method getAttributeInt = exifClass.getMethod("getAttributeInt", new Class[] { String.class, int.class });
                Field tagOrientationField = exifClass.getField("TAG_ORIENTATION");
                String tagOrientation = (String) tagOrientationField.get(null);
                orientation = (Integer) getAttributeInt.invoke(exifInstance, new Object[] { tagOrientation, 1});
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        }
        return orientation;
    }

    public int exifOrientationToDegrees(int exifOrientation){
        if(exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        } return 0;
    }





    Boolean PermissionStatus(String permission){
        int permissionCheck = ContextCompat.checkSelfPermission(getActivity(), permission);

        if(permissionCheck== PackageManager.PERMISSION_DENIED){
            // 권한 없음
            return true;
        }
        else{
            // 권한 있음
            return false;
        }

    }

    void PermissionGet(){
        // Activity에서 실행하는경우
        if (ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {

            // 이 권한을 필요한 이유를 설명해야하는가?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // 다이어로그같은것을 띄워서 사용자에게 해당 권한이 필요한 이유에 대해 설명합니다
                // 해당 설명이 끝난뒤 requestPermissions()함수를 호출하여 권한허가를 요청해야 합니다
            }
            else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                // 필요한 권한과 요청 코드를 넣어서 권한허가요청에 대한 결과를 받아야 합니다

            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1:

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 권한 허가
                    // 해당 권한을 사용해서 작업을 진행할 수 있습니다
                } else {
                    // 권한 거부
                    // 사용자가 해당권한을 거부했을때 해주어야 할 동작을 수행합니다
                }
                return;
        }
    }
}