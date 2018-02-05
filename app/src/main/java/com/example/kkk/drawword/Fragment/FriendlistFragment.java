package com.example.kkk.drawword.Fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
    @BindView(R.id.add_friend) Button add_friend;
    FriendAdapter friend_adapter;
    ArrayList<FriendData> item = new ArrayList<>();
    public static ArrayList<FriendData> item_static = new ArrayList<>();
    //버튼 중복으로 누르는거 방지
    boolean enter_in_gameroom = true;
    public static int a  = 12;
    public static int b;
    String iden,id,ment,photo_uri,token,friend_list_json,check_json;
    String friend_iden,friend_id,friend_photo_uri,friend_ment;
    String default_photo_url;
    CircleTransform circleTransform;
    public FriendlistFragment(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.friendlist_fragment, container,false);
        ButterKnife.bind(this,view);

        iden = getArguments().getString("iden");
        id = getArguments().getString("id");
        photo_uri = getArguments().getString("uri");
        friend_list_json = getArguments().getString("friend_list_json");
        check_json = getArguments().getString("check_json");

        default_photo_url = MainActivity.server_url + "user_photo/";
        //user_info
        myname.setText(id);
        myment.setText("아직 준비 모함");
        if (photo_uri.equals("null")){
            photo_uri = "default/default.jpg";
        }
        String photo_backUri = MainActivity.server_url+"user_photo/";
        circleTransform = new CircleTransform();
        myphoto.setScaleType(ImageView.ScaleType.FIT_XY);
        Picasso.with(getActivity()).load(photo_backUri+photo_uri).transform(circleTransform).into(myphoto);
        Log.d("photo_uri",photo_backUri+photo_uri);
        Log.d("photo_uri",id+ iden);
        Log.d("fr_list",friend_list_json);

        GetJson(friend_list_json,true);

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

        return view;
    }

    void GetJson(String json_list, boolean type){
        Log.d("테스트", "not null");


            try {
                JSONArray json = new JSONArray(json_list);
                Log.d("json_length", String.valueOf(json.length()));
                for (int i = 0; i < json.length(); i++) {


                    JSONObject jsonObject = json.getJSONObject(i);
                    friend_iden = jsonObject.getString("iden");
                    friend_id = jsonObject.getString("id");
                    friend_photo_uri = jsonObject.getString("photo_uri");
                    friend_ment = jsonObject.getString("ment");
                    if (friend_ment.equals("null")) {
                        friend_ment = "멘트가 없습니다.";
                    }
                    if (friend_photo_uri.equals("null")) {
                        friend_photo_uri = "default/default.jpg";
                    }
                    if (type == true) {
                        friend_photo_uri = default_photo_url + friend_photo_uri;
                        item.add(new FriendData(friend_id, friend_ment, friend_photo_uri, friend_iden));
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
                            } else if (friend_output.equals("already") || !friend_output.equals("cantfind")) {
                                item.clear();
                                friend_adapter.notifyDataSetChanged();
                                GetJson(friend_output, true);
                            }

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
