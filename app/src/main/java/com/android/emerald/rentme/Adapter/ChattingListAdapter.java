package com.android.emerald.rentme.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.emerald.rentme.Models.MessageModel;
import com.android.emerald.rentme.Models.UserModel;
import com.android.emerald.rentme.R;
import com.android.emerald.rentme.Utils.Utils;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by emerald on 6/12/2017.
 */
public class ChattingListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<MessageModel> messages;
    private UserModel curUser;
    private String consumerAvatar;
    private int consumerId;
    private String talentAvatar;
    private int talentId;

    public ChattingListAdapter(Context context, ArrayList<MessageModel> messages) {
        this.context = context;
        this.messages = messages;
        this.curUser = Utils.retrieveUserInfo(context);
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public MessageModel getItem(int position) {
        return messages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MessageModel message = getItem(position);

        if (message.getSender() == curUser.getId()) {
            convertView = LayoutInflater.from(context).inflate(R.layout.row_chat_me, parent, false);
            ImageView me = (ImageView)convertView.findViewById(R.id.img_row_chatting_avatar);
            if (curUser.getAvatar() != null)
                Glide.with(context).load(curUser.getAvatar()).asBitmap().centerCrop().placeholder(R.drawable.profile_empty).into(me);
        } else {
            convertView = LayoutInflater.from(context).inflate(R.layout.row_chat_you, parent, false);
            ImageView you = (ImageView)convertView.findViewById(R.id.img_row_chatting_avatar);
            if (curUser.getId() == talentId && consumerAvatar != null)
                Glide.with(context).load(consumerAvatar).asBitmap().centerCrop().placeholder(R.drawable.profile_empty).into(you);
            else if (curUser.getId() == consumerId && talentAvatar != null)
                Glide.with(context).load(talentAvatar).asBitmap().centerCrop().placeholder(R.drawable.profile_empty).into(you);
        }

        TextView txtMessage = (TextView)convertView.findViewById(R.id.txt_row_chatting_message);
        txtMessage.setText(message.getMessage());

        return convertView;
    }

    public void add(MessageModel message) {
        messages.add(message);
        notifyDataSetChanged();
    }

    public void setConsumerId(int i) {
        this.consumerId = i;
    }

    public void setTalentId(int i) {
        this.talentId = i;
    }

    public void setConsumerAvatar(String a) {
        this.consumerAvatar = a;
        notifyDataSetChanged();
    }

    public void setTalentAvatar(String a) {
        this.talentAvatar = a;
        notifyDataSetChanged();
    }
}
