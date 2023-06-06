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
//import com.example.internet.chatkit.fixtures.DialogsFixtures;
import com.example.internet.activity.MainActivity;
import com.example.internet.model.DialogModel;
import com.example.internet.request.message.GetChatListRequest;
import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.dialogs.DialogsList;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter;

import org.json.JSONArray;

import java.io.IOException;

import butterknife.ButterKnife;

public class ChatFragment extends Fragment {

    MainActivity ctx;

    private DialogsList dialogsList;
    private DialogsListAdapter<DialogModel> dialogsListAdapter;
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
        ctx = (MainActivity) getActivity();
        dialogsList = rootView.findViewById(R.id.dialogList);

        //初始化适配器并添加图像加载
        dialogsListAdapter=new DialogsListAdapter<>((imageView, url, payload) -> Picasso.get().load(url).into(imageView));
        //通过DialogsFixtures给予列表初始数据
//        dialogsListAdapter.addItem(new Dialog("cuijz20", null, "123", 1));
        //item项的点击事件，跳转到聊天界面
        dialogsListAdapter.setOnDialogClickListener(dialog -> {
            Intent intent = new Intent(ctx, ChattingActivity.class);
            intent.putExtra("jwt", ctx.jwt);
            intent.putExtra("username", ctx.getUsername());
            intent.putExtra("target", dialog.getId());
            startActivity(intent);
            ctx.overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
        });

        dialogsList.setAdapter(dialogsListAdapter);
        return rootView;
    }

    Thread mThread;
    @Override
    public void onPause() {
        super.onPause();
        mThread.interrupt();
    }

    @Override
    public void onResume() {
        super.onResume();
        mThread = new Thread(polling);
        mThread.start();
    }

    Runnable polling = new Runnable() {
        @Override
        public void run() {
            while (true) {
                new GetChatListRequest(new okhttp3.Callback() {
                    @Override
                    public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response) throws IOException {
                        if (response.code() != 200) {
                            return;
                        }
                        String res = response.body().string();
                        try {
                            ctx.runOnUiThread(() ->
                            {
                                try {
                                    dialogsListAdapter.clear();
                                    JSONArray jsonArray = new JSONArray(res);
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        String username = jsonArray.getJSONObject(i).getString("username");
                                        String avatar = jsonArray.getJSONObject(i).getString("avatar");
                                        String lastMessage = jsonArray.getJSONObject(i).getString("last_message");
                                        int unreadCount = 0; // TODO:
                                        dialogsListAdapter.addItem(new DialogModel(username, avatar, lastMessage, unreadCount));
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                ;
                            });
                        }catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, ctx.jwt);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };
}
