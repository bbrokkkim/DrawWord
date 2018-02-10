package com.example.kkk.drawword.Fragment;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
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


import butterknife.BindView;
import butterknife.ButterKnife;

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
    Uri uri;
    String real_uri;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.join_fragment,container,false);

        ButterKnife.bind(this,view);
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
                break;
            //이미지 초기화
            case R.id.back_photo :
                Toast.makeText(getActivity(), "이미지가 초기화 되었습니다.", Toast.LENGTH_SHORT).show();
                uri = null;
                photo.setImageResource(0);

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

            okhttpUser.execute("1", id, pwd1, name, "01012341234", "12", sex, real_uri);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQ_CODE_SELECT_IMAGE) {
            /*if(resultCode == Activity.RESULT_OK) {

                photo.setImageURI(data.getData());
                String imagePath = imageUri.getPath();
                uri = data.getData();

                Bitmap image = BitmapFactory.decodeFile(data.getData());

                ExifInterface exif = new ExifInterface(imagePath);
                int exifOrientation = exif.getAttributeInt(
                        ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                int exifDegree = exifOrientationToDegrees(exifOrientation);
                image = rotate(image, exifDegree);




                String[] projection = {MediaStore.Images.Media.DATA};
                //apk static 에러 때문에 독립되게 fragment 만드니 에러 뜸
 *//*               Cursor cursor = managedQuery(uri, projection, null, null, null);
                startManagingCursor(cursor);
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                real_uri = cursor.getString(columnIndex);*//*

                String[] proj = { MediaStore.Images.Media.DATA };

                CursorLoader cursorLoader = new CursorLoader(getActivity(), uri, proj, null, null, null);
                Cursor cursor1 = cursorLoader.loadInBackground();

                int column_index = cursor1.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor1.moveToFirst();
                real_uri = cursor1.getString(column_index);

            }*/
        }
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