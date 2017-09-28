package com.example.kkk.drawword;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kkk.drawword.Activity.RoomActivity;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by KKK on 2017-08-17.
 */

public class gamelist_fragment extends Fragment implements View.OnClickListener {

    ArrayList<Gamelist_Data> item = new ArrayList<Gamelist_Data>();
    Gamelist_Adapter gamelist_adapter;

    @BindView(R.id.create_room) Button create_room;
    @BindView(R.id.game_list) ListView listView;
    String id,iden;
    public gamelist_fragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.gamelist_fragment,container,false);
        ButterKnife.bind(this,view);

        id = getArguments().getString("id");
        iden = getArguments().getString("iden");
        int test = getArguments().getInt("test");
        Toast.makeText(getActivity(), String.valueOf(test), Toast.LENGTH_SHORT).show();
        
        gamelist_adapter = new Gamelist_Adapter(inflater,item);
        item.add(new Gamelist_Data("1","테스트방입니다","2/4"));
        listView.setAdapter(gamelist_adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(),RoomActivity.class);
                startActivity(intent);
            }
        });

        create_room.setOnClickListener(this);

        Toast.makeText(getActivity(),"oncreateview", Toast.LENGTH_SHORT).show();
        return view;
    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Toast.makeText(getActivity(), "onActivityCreateed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStart() {
        super.onStart();
        Toast.makeText(getActivity(), "onStart", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        Toast.makeText(getActivity(), "onResume", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPause() {
        super.onPause();
        Toast.makeText(getActivity(), "onPause", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStop() {
        super.onStop();
        Toast.makeText(getActivity(), "onStop", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onDetach() {
        super.onDetach();
        Toast.makeText(getActivity(), "onDetach", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Toast.makeText(getActivity(), "onDestroyView", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(getActivity(), "onDestroy", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.create_room :
                final LayoutInflater inflater = getActivity().getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.add_friend_dialog, null);
                final EditText room= (EditText)dialogView.findViewById(R.id.friend_name);
                TextView title = (TextView) dialogView.findViewById(R.id.ment);
                title.setText("방의 이름을 적어주세요");
                final AlertDialog.Builder buider= new AlertDialog.Builder(getActivity());
                buider.setTitle("방 만들기"); //Dialog 제목
                buider.setView(dialogView);
                buider.setPositiveButton("생성", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String room_name = room.getText().toString();
                        OkhttpGame okhttpGame = new OkhttpGame();
                        okhttpGame.execute("1",iden,id);
                    }
                });
                buider.setNegativeButton("취소", new DialogInterface.OnClickListener() {
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
