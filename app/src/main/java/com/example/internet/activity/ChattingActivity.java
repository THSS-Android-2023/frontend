package com.example.internet.activity;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.internet.R;
import com.example.internet.model.MessageModel;
import com.example.internet.model.ChatUserModel;
import com.example.internet.request.message.GetMessageRequest;
import com.example.internet.request.message.SendMessageRequest;
import com.example.internet.util.ErrorDialog;
import com.squareup.picasso.Picasso;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import org.json.JSONArray;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ChattingActivity extends BaseActivity implements MessagesListAdapter.OnLoadMoreListener{
    private MessagesList messagesList;
    private MessageInput input;

    private MessagesListAdapter<MessageModel> messagesListAdapter;
    private LinkedList<Integer> messageIdList = new LinkedList<>();
    private Date lastLoadedDate;

    private String jwt;
    private String username;

    private String target;

    ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();
    ReentrantReadWriteLock.ReadLock readLock = lock.readLock();



    Callback sendMessageCallback = new okhttp3.Callback() {
        @Override
        public void onFailure(@NonNull Call call, @NonNull IOException e) {
            new ErrorDialog(ChattingActivity.this, "发送信息失败");
        }
        @Override
        public void onResponse(@NonNull Call call, @NonNull Response response){
            if (response.code() != 200) {
                new ErrorDialog(ChattingActivity.this, "发送信息失败");
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chatting);

        messagesList = findViewById(R.id.messagesList);
        input = findViewById(R.id.input);

        /*
         * senderId:自己的id，用于区分自己和对方，控制消息气泡的位置。
         * imageLoader:图像加载器
         *
         * */
        jwt = getIntent().getStringExtra("jwt");
        username = getIntent().getStringExtra("username");
        target = getIntent().getStringExtra("target");

        messagesListAdapter = new MessagesListAdapter<>(username, (imageView, url, payload) -> Picasso.get().load(url).into(imageView));
        //滑倒顶部时加载历史记录
        messagesListAdapter.setLoadMoreListener(this);

        //发送输入框中的文本，addToStart的第二个参数是列表滚动到底部
        input.setInputListener(input1 -> {
            new SendMessageRequest(sendMessageCallback, target, input1.toString(), jwt);
//            messagesListAdapter.addToStart(new Message("0", new User(username, null, false), input1.toString()), true);
            return true;
        });

        new GetMessageRequest(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                new ErrorDialog(ChattingActivity.this, "获取信息失败");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.code() != 200) {
                    new ErrorDialog(ChattingActivity.this, "获取信息失败");
                    return;
                }
                String res = response.body().string();
                runOnUiThread(() ->{
                    writeLock.lock();
                    try {
                    JSONArray jsonArray = new JSONArray(res);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        String sender = jsonArray.getJSONObject(i).getString("sender");
                        String content = jsonArray.getJSONObject(i).getString("content");
                        String time = jsonArray.getJSONObject(i).getString("time");
                        String avatar = jsonArray.getJSONObject(i).getString("avatar");
                        String format = "yyyy-MM-dd HH:mm";
                        DateFormat dateFormat = new SimpleDateFormat(format);
                        Date date = dateFormat.parse(time);
                        messagesListAdapter.addToStart(new MessageModel("0", new ChatUserModel(sender, avatar, false), content, date), true);
                        messageIdList.addFirst(jsonArray.getJSONObject(i).getInt("id"));
                    }} catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        writeLock.unlock();
                    }
                });
            }
        }, target, jwt);

        messagesList.setAdapter(messagesListAdapter);
        //初始化时调用一次加载历史记录
        onLoadMore(0,1);
    }

    Boolean isLoading = false;

    //滚动到顶部加载历史记录
    @Override
    public void onLoadMore(int page, int totalItemsCount) {

//        new Handler().postDelayed(() -> {
//        }, 1000);
        int size = (int) messageIdList.size();
        if (size == 0 || isLoading) {
            return;
        }
        Log.d("onLoadMore", "onLoadMore: " + page + " " + totalItemsCount);

            isLoading = true;
            new GetMessageRequest(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    new ErrorDialog(ChattingActivity.this, "获取信息失败");
                    isLoading = false;
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (response.code() != 200) {
                        new ErrorDialog(ChattingActivity.this, "获取信息失败");
                        isLoading = false;
                        return;
                    }
                    String res = response.body().string();
                    runOnUiThread(() -> {
                        try {
                            writeLock.lock();
                            JSONArray jsonArray = new JSONArray(res);
                            List<MessageModel> messages = new ArrayList<>();
                            for (int i = jsonArray.length() - 1; i >= 0; i--) {
                                String sender = jsonArray.getJSONObject(i).getString("sender");
                                String content = jsonArray.getJSONObject(i).getString("content");

                                String time = jsonArray.getJSONObject(i).getString("time");
                                String avatar = jsonArray.getJSONObject(i).getString("avatar");
                                int id = jsonArray.getJSONObject(i).getInt("id");
                                String format = "yyyy-MM-dd HH:mm";
                                DateFormat dateFormat = new SimpleDateFormat(format);
                                Date date = dateFormat.parse(time);
//                                Log.d("onLoadMore", "onResponse: " + id + " " + messageIdList.getLast());
                                if (id >= messageIdList.getLast())
                                    continue;

                                messages.add(new MessageModel("0", new ChatUserModel(sender, avatar, false), content, date));
                                messageIdList.addLast(id);
                            }
                            messagesListAdapter.addToEnd(messages, false);
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            writeLock.unlock();
                            isLoading = false;

                        }
                    });

                }
            }, target, jwt, messageIdList.get(messageIdList.size() - 1), "old");
        }


    Thread mThread;
    @Override
    protected void onStart() {
        super.onStart();
        mThread = new Thread(polling);
        // 启动线程，进行轮询
        mThread.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 中断线程，即结束线程。
        mThread.interrupt();
    }

    Runnable polling = new Runnable() {
        @Override
        public void run() {
            // 执行轮询操作的代码
            while (!Thread.interrupted()) {
                if (messageIdList.size() > 0){
                            new GetMessageRequest(new Callback() {
                                @Override
                                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                                    new ErrorDialog(ChattingActivity.this, "获取信息失败");
                                }

                                @Override
                                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                    if (response.code() != 200) {
                                        new ErrorDialog(ChattingActivity.this, "获取信息失败");
                                        return;
                                    }
                                    String res = response.body().string();
                                    runOnUiThread(() -> {
                                        try {

                                            writeLock.lock();
                                            Boolean scrollOrNot = !messagesList.canScrollVertically(1);
                                            JSONArray jsonArray = new JSONArray(res);
                                            for (int i = 0; i < jsonArray.length(); i++) {
                                                String sender = jsonArray.getJSONObject(i).getString("sender");
                                                String content = jsonArray.getJSONObject(i).getString("content");
                                                MessageModel m = new MessageModel("0", new ChatUserModel(sender, null, false), content);
                                                messageIdList.addFirst(jsonArray.getJSONObject(i).getInt("id"));
                                                messagesListAdapter.addToStart(m, true);
                                            }
                                            if (scrollOrNot)
                                                messagesList.scrollToPosition(0); // TODO
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        } finally {
                                            writeLock.unlock();
                                        }
                                    });
                                }
                            }, target, jwt, messageIdList.get(0), "new");
                }
                try {
                    Thread.sleep(1000); // 每隔 1 秒执行一次轮询操作
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }
    };

    // get update
}