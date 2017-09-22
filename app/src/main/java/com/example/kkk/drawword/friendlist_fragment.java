package com.example.kkk.drawword;

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

public class friendlist_fragment extends Fragment implements View.OnClickListener{
    @BindView(R.id.friend_list) ListView listView;
    @BindView(R.id.my_photo) ImageView myphoto;
    @BindView(R.id.my_name) TextView myname;
    @BindView(R.id.my_ment) TextView myment;
    @BindView(R.id.add_friend) Button add_friend;
    Friend_Adapter friend_adapter;
    ArrayList<Friend_Data> item = new ArrayList<Friend_Data>();
    String iden,id,ment,photo_uri,token,friend_list_json;
    String friend_iden,friend_id,friend_photo_uri,friend_ment;
    String default_photo_url = "http://13.124.229.116/user_photo/";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.friendlist_fragment, container,false);
        ButterKnife.bind(this,view);


        iden = getArguments().getString("iden");
        id = getArguments().getString("id");
        photo_uri = getArguments().getString("uri");
        friend_list_json = getArguments().getString("friend_list_json");
        //user_info
        myname.setText(id);
        myment.setText("아직 준비 모함");
        String photo_backUri = "http://13.124.229.116/user_photo/";
        Picasso.with(getActivity()).load(photo_backUri+photo_uri).into(myphoto);
        Toast.makeText(getActivity(), "test", Toast.LENGTH_SHORT).show();
        Log.d("photo_uri",photo_backUri+photo_uri);
        Log.d("photo_uri",id+ iden);
        Log.d("fr_list",friend_list_json);

        GetJson(friend_list_json);

        add_friend.setOnClickListener(this);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Friend_Data fr_data = (Friend_Data) listView.getItemAtPosition(position);
                String friend_iden = fr_data.getIden();
                OkhttpFriend okhttpFriend = new OkhttpFriend(getActivity());
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
                GetJson(del_json);
                return false;
            }
        });

        friend_adapter = new Friend_Adapter(inflater,getActivity(),item);
        listView.setAdapter(friend_adapter);

        return view;
    }

    void GetJson(String json_list){
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
                    friend_photo_uri = default_photo_url + friend_photo_uri;
                    item.add(new Friend_Data(friend_id, friend_ment, friend_photo_uri, friend_iden));

                }

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
               /* OkhttpFriend okhttpFriend = new OkhttpFriend();
                okhttpFriend.execute("2");*/
                final LayoutInflater inflater = getActivity().getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.add_friend_dialog, null);
                final EditText friend_name= (EditText)dialogView.findViewById(R.id.friend_name);
                final AlertDialog.Builder buider= new AlertDialog.Builder(getActivity());
                buider.setTitle("친구추가"); //Dialog 제목
                buider.setIcon(android.R.drawable.ic_menu_add); //제목옆의 아이콘 이미지(원하는 이미지 설정)
                buider.setView(dialogView);
                buider.setPositiveButton("네", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String friend = friend_name.getText().toString();
                        String friend_output = null;
                        OkhttpFriend okhttpFriend = new OkhttpFriend(getActivity());
                        try {
                            friend_output = okhttpFriend.execute("2",iden,id,friend).get();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                        Log.d("output_json",friend_output);
                        if (friend_output.equals("already") || friend_output.equals("cantfind")){
                            Toast.makeText(getActivity(),"good", Toast.LENGTH_SHORT).show();
                        }
                        
                        else if (friend_output.equals("already") || !friend_output.equals("cantfind")) {
                            item.clear();
                            friend_adapter.notifyDataSetChanged();
                            GetJson(friend_output);
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

}
