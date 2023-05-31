package com.example.internet.activity;

import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.internet.R;
import com.example.internet.chatkit.fixtures.MessagesFixtures;
import com.example.internet.chatkit.model.Message;
import com.example.internet.util.NotificationUtil;
import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import java.util.ArrayList;
import java.util.Date;

public class ChattingActivity extends AppCompatActivity implements MessagesListAdapter.OnLoadMoreListener{
    private MessagesList messagesList;
    private MessageInput input;

    private MessagesListAdapter<Message> messagesListAdapter;
    private Date lastLoadedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        NotificationUtil.notify(this, this);
        setContentView(R.layout.activity_chatting);

        messagesList = findViewById(R.id.messagesList);
        input = findViewById(R.id.input);

        /*
         * senderId:自己的id，用于区分自己和对方，控制消息气泡的位置。
         * imageLoader:图像加载器
         *
         * */
        messagesListAdapter = new MessagesListAdapter<>("0", (imageView, url, payload) -> Picasso.get().load(url).into(imageView));
        //滑倒顶部时加载历史记录
        messagesListAdapter.setLoadMoreListener(this);

        //发送输入框中的文本，addToStart的第二个参数是列表滚动到底部
        input.setInputListener(input1 -> {
            messagesListAdapter.addToStart(MessagesFixtures.getTextMessage(input1.toString()), true);
            return true;
        });
        //小加号按钮点击事件，发送一张图片
        input.setAttachmentsListener(() -> {
            //第二个参数表示是否滚动列表到最底部
            messagesListAdapter.addToStart(MessagesFixtures.getImageMessage(), true);
        });

        messagesList.setAdapter(messagesListAdapter);
        //初始化时调用一次加载历史记录
        onLoadMore(0,1);
    }

    //滚动到顶部加载历史记录
    @Override
    public void onLoadMore(int page, int totalItemsCount) {
        new Handler().postDelayed(() -> {
            ArrayList<Message> messages = MessagesFixtures.getMessages(lastLoadedDate);
            lastLoadedDate = messages.get(messages.size() - 1).getCreatedAt();
            messagesListAdapter.addToEnd(messages, false);
        }, 1000);
    }
}