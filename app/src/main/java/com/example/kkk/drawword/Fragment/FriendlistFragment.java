package com.example.kkk.drawword.Fragment;

import android.app.AlertDialog;
import android.support.v4.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kkk.drawword.Activity.GameActivity;
import com.example.kkk.drawword.Activity.MainActivity;
import com.example.kkk.drawword.Adapter.FriendAdapter;
import com.example.kkk.drawword.Data.FriendData;
import com.example.kkk.drawword.Okhttp.OkhttpFriend;
import com.example.kkk.drawword.Okhttp.Tcp_chat;
import com.example.kkk.drawword.R;
import com.example.kkk.drawword.view.CircleTransform;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by KKK on 2017-08-17.
 */

public class FriendlistFragment extends Fragment implements View.OnClickListener{
    @BindView(R.id.friend_list) ListView listView;
    @BindView(R.id.my_photo) ImageView myphoto;
    @BindView(R.id.my_name) TextView myname;
    @BindView(R.id.my_ment) TextView myment;
    @BindView(R.id.add_friend) ImageButton add_friend;
    FriendAdapter friend_adapter;
    ArrayList<FriendData> item = new ArrayList<>();
    int json_length = 0;
    int json_row = 1;
    public static ArrayList<FriendData> item_static = new ArrayList<>();
    //버튼 중복으로 누르는거 방지
    boolean enter_in_gameroom = true;
    public static int a  = 12;
    public static int b;
    String iden,id,ment,photo_uri,token,friend_list_json,check_json,tokeb;
    String friend_iden,friend_id,friend_photo_uri,friend_ment,rotate;
    int rotate_int;
    String default_photo_url;
    CircleTransform circleTransform;
    public FriendlistFragment(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.friendlist_fragment, container,false);
        ButterKnife.bind(this,view);

        iden = GameActivity.iden;
        id = GameActivity.id;
        photo_uri = GameActivity.uri;
        token = GameActivity.token;
        friend_list_json = GameActivity.friend_list_json;
        rotate_int = GameActivity.rotate;
        default_photo_url = MainActivity.server_url + "user_photo/";
        try {
            check_json = new OkhttpFriend().execute("4",iden).get();
            GetJson_add(check_json,true);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
/*        iden = "1";
        id = "2";
        photo_uri="1";*/
//        friend_list_json = "";
//        check_json = "";
//        rotate_int = 1;
//        Toast.makeText(getActivity(), String.valueOf(rotate_int), Toast.LENGTH_SHORT).show();
        //user_info
        myname.setText(id);
        myment.setText("아직 준비 모함");
        if (photo_uri.equals("null")){
            photo_uri = "default/default.jpg";
        }
        String photo_backUri = MainActivity.server_url+"user_photo/";
        circleTransform = new CircleTransform();
        myphoto.setScaleType(ImageView.ScaleType.FIT_XY);
        Picasso.with(getActivity()).load(photo_backUri+photo_uri).rotate(rotate_int*-1).transform(circleTransform).into(myphoto);
        Log.d("photo_uri",photo_backUri+photo_uri);
        Log.d("photo_uri",id+ iden);
        Log.d("fr_list",friend_list_json);

//        GetJson(friend_list_json,true);
        
        add_friend.setOnClickListener(this);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                FriendData fr_data = (FriendData) listView.getItemAtPosition(position);
                String friend_iden = fr_data.getIden();
                String friend_name = fr_data.getUser_name();
                Dialog("친구삭제",friend_name+ "님을 삭제하겠습니까?",friend_iden);
                return false;
            }
        });

        friend_adapter = new FriendAdapter(android.R.layout.activity_list_item,getActivity(),item);
        listView.setAdapter(friend_adapter);
//        Toast.makeText(getActivity(), friend_list_json, Toast.LENGTH_SHORT).show();
        friend_adapter.notifyDataSetChanged();
        GetJson(friend_list_json,true);

        return view;
    }
    void GetJson_add(String json_list, boolean type) {
        Log.d("테스트", "not null");
        if (!json_list.equals("nothing")) {
            try {
                JSONArray json = new JSONArray(json_list);
                Log.d("json_length", String.valueOf(json.length()));
                json_length = json.length();
                for (int i = 0; i < json.length(); i++) {


                    JSONObject jsonObject = json.getJSONObject(i);
                    friend_id = jsonObject.getString("my_id");
                    Log.d("ilength",String.valueOf(i));
                    Dialog_view(friend_id);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    void Dialog_view(final String friend_id){
        final AlertDialog.Builder dialog =
                new AlertDialog.Builder(getActivity());
        dialog.setTitle(friend_id+"씨가 친구를 추가 했습니다.");
        dialog.setMessage("친구를 추가하시겠습니까?");
        dialog.setPositiveButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                OkhttpFriend okhttpFriend = new OkhttpFriend();
                okhttpFriend.execute("5",iden,id,friend_id,"1");
                Log.d("length",String.valueOf(json_length));
                Log.d("row",String.valueOf(json_row));
                if (json_length == json_row){
                    try {
                        friend_list_json = new OkhttpFriend().execute("1",iden,token).get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
//                    switchfragment(1);
//                    Toast.makeText(GameActivity.this, "끝", Toast.LENGTH_SHORT).show();
                    json_row = 1;
                }
                json_row = json_row + 1;
                GetJson(friend_list_json,true);
            }
        });
        dialog.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                OkhttpFriend okhttpFriend = new OkhttpFriend();
                okhttpFriend.execute("5",iden,id,friend_id,"2");
                Log.d("length",String.valueOf(json_length));
                if (json_length == json_row){
                    try {
                        friend_list_json = new OkhttpFriend().execute("1",iden,token).get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
//                    Toast.makeText(GameActivity.this, "끝", Toast.LENGTH_SHORT).show();
                    json_row = 1;
                }
                json_row = json_row + 1;
            }
        });
        dialog.show();
    }

    void GetJson(String json_list, boolean type){
        Log.d("테스트", "not null");
        item.clear();

            try {
                JSONArray json = new JSONArray(json_list);
                Log.d("json_length", String.valueOf(json.length()));
                for (int i = 0; i < json.length(); i++) {


                    JSONObject jsonObject = json.getJSONObject(i);
                    friend_iden = jsonObject.getString("iden");
                    friend_id = jsonObject.getString("id");
                    friend_photo_uri = jsonObject.getString("photo_uri");
                    friend_ment = jsonObject.getString("ment");
                    rotate = jsonObject.getString("rotate");
                    if (friend_ment.equals("null")) {
                        friend_ment = "멘트가 없습니다.";
                    }
                    if (friend_photo_uri.equals("null")) {
                        friend_photo_uri = "default/default.jpg";
                    }
                    if (type == true) {
                        friend_photo_uri = default_photo_url + friend_photo_uri;
                        item.add(new FriendData(friend_id, friend_ment, friend_photo_uri, friend_iden,rotate));
                        Log.d("friend_list",friend_id);
                    }

                }
                item_static = item;
            } catch (JSONException e) {
                e.printStackTrace();
            }


    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_friend:
                enter_in_gameroom = true;
               /* OkhttpFriend okhttpFriend = new OkhttpFriend();
                okhttpFriend.execute("2");*/
                final LayoutInflater inflater = getActivity().getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.add_friend_dialog, null);
                final EditText friend_name= (EditText)dialogView.findViewById(R.id.friend_name);
                final AlertDialog.Builder buider= new AlertDialog.Builder(getActivity());
                friend_name.setHint("친구의 이름을 적어주세요");
                buider.setTitle("친구추가"); //Dialog 제목
                buider.setIcon(android.R.drawable.ic_menu_add); //제목옆의 아이콘 이미지(원하는 이미지 설정)
                buider.setView(dialogView);
                buider.setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (enter_in_gameroom == true) {
                            String friend = friend_name.getText().toString();
                            String friend_output = null;
                            OkhttpFriend okhttpFriend = new OkhttpFriend();
                            try {
                                friend_output = okhttpFriend.execute("2", iden, id, friend).get();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                            Log.d("output_json", friend_output);
                            if (friend_output.equals("already")) {
                                Toast.makeText(getActivity(), "이미 친구사이입니다.", Toast.LENGTH_SHORT).show();
                            } else if (friend_output.equals("cantfind")) {
                                Toast.makeText(getActivity(), "그런 아이디는 없습니다.", Toast.LENGTH_SHORT).show();
                            }
                            //친구 실시간 추가
                            else if (friend_output.equals("already") || !friend_output.equals("cantfind")) {
                                item.clear();
                                friend_adapter.notifyDataSetChanged();
                                GetJson(friend_output, true);
                            }

                            GameActivity.friend_list_json = friend_output;
                            enter_in_gameroom = false;
                        }
                    }
                });
                buider.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                buider.show();
                break;

        }
    }

    public void Dialog(String title, String message, final String friend_iden){
        final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());

        dialog.setTitle(title);
        dialog.setMessage(message);
        dialog.setPositiveButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                OkhttpFriend okhttpFriend = new OkhttpFriend();
                String del_json = null;
                try {
                    del_json = okhttpFriend.execute("3",iden,friend_iden).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                Log.d("testesetset",del_json);
                item.clear();
                friend_adapter.notifyDataSetChanged();
                GetJson(del_json,true);
                GameActivity.friend_list_json = del_json;
            }
        });
        dialog.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }



}
