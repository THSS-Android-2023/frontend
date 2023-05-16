package com.example.internet.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.internet.R;
import com.example.internet.activity.ChattingActivity;
import com.example.internet.chatkit.fixtures.DialogsFixtures;
import com.example.internet.chatkit.model.Dialog;
import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.dialogs.DialogsList;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter;

import butterknife.ButterKnife;

public class ChatFragment extends Fragment {

    private DialogsList dialogsList;
    private DialogsListAdapter<Dialog> dialogsListAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chat, container, false);
        ButterKnife.bind(this, rootView);
        dialogsList = rootView.findViewById(R.id.dialogList);

        //初始化适配器并添加图像加载
        dialogsListAdapter=new DialogsListAdapter<>((imageView, url, payload) -> Picasso.get().load(url).into(imageView));
        //通过DialogsFixtures给予列表初始数据
        dialogsListAdapter.addItems(DialogsFixtures.getDialogs());
        //item项的点击事件，跳转到聊天界面
        dialogsListAdapter.setOnDialogClickListener(dialog -> {
            startActivity(new Intent(getActivity(), ChattingActivity.class));
        });

        dialogsList.setAdapter(dialogsListAdapter);
        return rootView;
    }
}
