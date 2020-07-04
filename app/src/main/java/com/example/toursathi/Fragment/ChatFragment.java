package com.example.toursathi.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.toursathi.Adapter.GroupListAdapter;
import com.example.toursathi.Adapter.MessageListAdapter;
import com.example.toursathi.Constant.ApiInterface;
import com.example.toursathi.HomeActivity;
import com.example.toursathi.R;
import com.example.toursathi.ResponseModel.User;
import com.example.toursathi.ResponseModel.singleGroupChatModel;
import com.example.toursathi.ResponseModel.singleGroupModel;
import com.example.toursathi.UserClient;
import com.google.gson.Gson;
import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.PusherEvent;
import com.pusher.client.channel.SubscriptionEventListener;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionState;
import com.pusher.client.connection.ConnectionStateChange;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatFragment extends Fragment {
    View v;
    Button btnsendmsg;
    EditText edit_msg;
    RecyclerView recyclerView;

    ApiInterface apiInterface;
    MessageListAdapter messageListAdapter;

    public singleGroupModel singleGroupModel;
    public User user;

    List<singleGroupChatModel> messagelist = new ArrayList<>();

    public ChatFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.chatfragmentlayout, container, false);
        btnsendmsg = v.findViewById(R.id.btnmsgsend);
        edit_msg = v.findViewById(R.id.edit_chatbox);
        recyclerView = v.findViewById(R.id.recyclerview);

        singleGroupModel = ((UserClient) getContext().getApplicationContext()).getSingleGroupModel();
        user = ((UserClient) getContext().getApplicationContext()).getUser();

        messagelist.clear();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        Call<List<singleGroupChatModel>> callforchat = apiInterface.getGroupChat(singleGroupModel.getGroupcode());

        callforchat.enqueue(new Callback<List<singleGroupChatModel>>() {
            @Override
            public void onResponse(Call<List<singleGroupChatModel>> call, Response<List<singleGroupChatModel>> response) {

                if (response.isSuccessful()) {
                    messagelist = response.body();
                    Toast.makeText(getContext(), "all msg got", Toast.LENGTH_SHORT).show();
                    messageListAdapter = new MessageListAdapter(getContext(), messagelist, user);
                    recyclerView.setAdapter(messageListAdapter);
                } else {
                    Toast.makeText(getContext(), String.valueOf(response.code()), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<List<singleGroupChatModel>> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        //pusher implementation
        PusherOptions options = new PusherOptions();
        options.setCluster("ap2");
        final Pusher pusher = new Pusher("dc0a97d420312dc803d2", options);


        pusher.connect(new ConnectionEventListener() {
            @Override
            public void onConnectionStateChange(ConnectionStateChange change) {
                System.out.println("State changed from " + change.getPreviousState() +
                        " to " + change.getCurrentState());
            }

            @Override
            public void onError(String message, String code, Exception e) {
                System.out.println("There was a problem connecting! " +
                        "\ncode: " + code +
                        "\nmessage: " + message +
                        "\nException: " + e
                );
            }
        }, ConnectionState.ALL);

        Channel channel = pusher.subscribe("channel"+singleGroupModel.getGroupcode());

        channel.bind("sendmsgevent", new SubscriptionEventListener() {
            @Override
            public void onEvent(PusherEvent event) {
                try {
                    JSONObject object = new JSONObject(event.getData());
                    Toast.makeText(getContext(), object.toString(), Toast.LENGTH_SHORT).show();
                    singleGroupChatModel pusherdata = new Gson().fromJson(object.toString(), singleGroupChatModel.class);
                    if (!pusherdata.getMobileno().equals(user.getMobileno()))
                    {
                        messagelist.add(new singleGroupChatModel(pusherdata.getName()
                                , pusherdata.getMobileno(), pusherdata.getMessage(), pusherdata.getTime()));

                        //improvement can be possible here.

                        messageListAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


        btnsendmsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String message = edit_msg.getText().toString();
                Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+1:00"));
                Date currentLocalTime = cal.getTime();
                DateFormat date = new SimpleDateFormat("HH:mm a");
                // you can get seconds by adding  "...:ss" to it
                date.setTimeZone(TimeZone.getTimeZone("GMT+1:00"));
                final String time = date.format(currentLocalTime);
                messagelist.add(new singleGroupChatModel(user.getName(), user.getMobileno(), message, time));
                messageListAdapter.notifyDataSetChanged();

                Call<String> sendingmsgcall = apiInterface.sendmsgcall(singleGroupModel.getGroupcode(), user.getMobileno(), user.getName(), message, time);
                sendingmsgcall.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getContext(), response.body(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), String.valueOf(response.code()), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        return v;
    }
}
