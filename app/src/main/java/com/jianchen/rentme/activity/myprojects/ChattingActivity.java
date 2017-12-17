package com.jianchen.rentme.activity.myprojects;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jianchen.rentme.R;
import com.jianchen.rentme.activity.root.AppController;
import com.jianchen.rentme.activity.myprojects.adapter.ChattingListAdapter;
import com.jianchen.rentme.model.rentme.MessageModel;
import com.jianchen.rentme.model.rentme.UserModel;
import com.jianchen.rentme.helper.network.volley.APIStringRequester;
import com.jianchen.rentme.helper.Constants;
import com.jianchen.rentme.helper.utils.Utils;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by emerald on 5/30/2017.
 */
public class ChattingActivity extends AppCompatActivity implements View.OnClickListener, Response.ErrorListener, Response.Listener<String> {
    private String projectname;
    private int projectid;
    private String consumeravatar;
    private int consumerid;
    private String talentavatar;
    private int talentid;

    private UserModel curUser;

    private ListView list;
    private ChattingListAdapter adapter;
    private ArrayList<MessageModel> messages;

    private Firebase firebase;
    private boolean isInitial = false;

    private ImageView btnSend;
    private EditText editMessage;

    private ImageView btnBack;
    private TextView txtTitle;

    protected void onCreate(Bundle savedBundle) {
        super.onCreate(savedBundle);
        setContentView(R.layout.activity_chatting);

        messages = new ArrayList<>();

        projectid = getIntent().getIntExtra(Constants.EXTRA_PROJECT_DETAIL, 0);

        curUser = Utils.retrieveUserInfo(getApplicationContext());

        firebase = new Firebase(Constants.FIREBASE_ROOT);

        prepareChatData();
        /*
        initFirebase();
        initViewVariables();
        */
    }

    private void prepareChatData() {
        String url = Constants.API_ROOT_URL + Constants.API_PROJECT_CHATTING;

        Map<String, String> params = new HashMap<>();
        params.put("projectid", Integer.toString(projectid));

        APIStringRequester requester = new APIStringRequester(Request.Method.POST, url, params, this, this);
        AppController.getInstance().addToRequestQueue(requester, Constants.JSON_REQUEST);
    }

    private void initFirebase() {
        firebase = new Firebase(Constants.FIREBASE_ROOT);
        firebase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (!isInitial)
                    return;
                MessageModel message = dataSnapshot.getValue(MessageModel.class);
                if (message.getProjectId() != projectid) {
                    Intent t = getIntent();
                    Gson gson = new Gson();
                    String json = gson.toJson(message);
                    t.putExtra("message", json);
                    t.putExtra(Constants.EXTRA_PROJECT_DETAIL, message.getProjectId());

                    finish();
                    startActivity(t);
                }
                if (message.getReceiver() == curUser.getId() || message.getSender() == curUser.getId()) {
                    //check if adapter is null
                    adapter.add(message);
                    list.post(new Runnable(){
                        public void run() {
                            list.setSelection(list.getCount() - 1);
                        }
                    });
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        firebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                isInitial = true;
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    private void initViewVariables() {
        String json = getIntent().getStringExtra("message");
        if (json != null) {
            Gson gson = new Gson();
            MessageModel message = gson.fromJson(json, MessageModel.class);
            messages.add(message);
        }

        list = (ListView)findViewById(R.id.list_chatting_messages);
        adapter = new ChattingListAdapter(ChattingActivity.this, messages);
        adapter.setConsumerAvatar(consumeravatar);
        adapter.setTalentAvatar(talentavatar);
        adapter.setConsumerId(consumerid);
        adapter.setTalentId(talentid);
        list.setAdapter(adapter);

        btnSend = (ImageView)findViewById(R.id.btn_chatting_send);
        btnSend.setOnClickListener(this);

        editMessage = (EditText)findViewById(R.id.edit_chatting_message);

        btnBack = (ImageView)findViewById(R.id.btn_project_complete_detail_back);
        btnBack.setOnClickListener(this);

        txtTitle = (TextView)findViewById(R.id.txt_project_complete_detail_page);
        txtTitle.setText(projectname);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_chatting_send:
                if (editMessage.getText().toString().equals(""))
                    return;
                MessageModel message = new MessageModel();
                message.setProjectId(projectid);
                message.setMessage(editMessage.getText().toString());
                message.setSender(curUser.getId());
                if (curUser.getId() == consumerid)
                    message.setReceiver(talentid);
                else if (curUser.getId() == talentid)
                    message.setReceiver(consumerid);

                firebase.push().setValue(message);
                editMessage.setText("");
                break;
            case R.id.btn_project_complete_detail_back:
                finish();
                break;
        }
    }

    protected void onNewIntent (Intent intent) {
        /*
        Gson gson = new Gson();
        String json = intent.getStringExtra("message");
        MessageModel message = gson.fromJson(json, MessageModel.class);
        if (message.getProjectId() != projectid) {
            Intent t = getIntent();
            t.putExtra("message", json);
            t.putExtra(Constants.EXTRA_PROJECT_DETAIL, message.getProjectId());

            finish();
            startActivity(t);
        } else
            adapter.add(message);
        */
    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(String response) {
        try {
            JSONArray array = new JSONArray(response);
            if (array.length() > 0) {
                JSONObject obj = array.getJSONObject(0);
                projectname = obj.getString("name");
                consumeravatar = obj.getString("consumer");
                consumerid = obj.getInt("consumer_id");
                talentavatar = obj.getString("talent");
                talentid = obj.getInt("talent_id");

                initFirebase();
                initViewVariables();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
