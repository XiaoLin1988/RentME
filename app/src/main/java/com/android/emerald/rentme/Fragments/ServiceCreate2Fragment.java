package com.android.emerald.rentme.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.android.emerald.rentme.Dialogs.WebLinkDialog;
import com.android.emerald.rentme.Interface.OnDialogSelectListener;
import com.android.emerald.rentme.Listener.SingleClickListener;
import com.android.emerald.rentme.Models.SkillModel;
import com.android.emerald.rentme.PhotoActivity;
import com.android.emerald.rentme.R;
import com.android.emerald.rentme.Utils.Constants;
import com.android.emerald.rentme.Utils.DialogUtil;
import com.android.emerald.rentme.VideoLinkActivity;
import com.android.emerald.rentme.WebLinkActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by emerald on 12/8/2017.
 */
public class ServiceCreate2Fragment extends Fragment {
    @Bind(R.id.edit_service_title)
    EditText editTitle;
    @Bind(R.id.edit_service_detail)
    EditText editDetail;
    @Bind(R.id.edit_service_balance)
    EditText editBalance;
    @Bind(R.id.btn_service_add)
    ImageButton btnAdd;

    private Context context;
    private SkillModel skill;

    private SingleClickListener clickListener = new SingleClickListener() {
        @Override
        public void onSingleClick(View v) {
            switch (v.getId()) {
                case R.id.btn_service_add:
                    CharSequence[] options = {"Post images", "Post videos", "Post web links"};
                    DialogUtil.showSelectDialog(context, options, new OnDialogSelectListener() {
                        @Override
                        public void onDialogSelect(int position) {
                            if (position == 0) {

                                /*
                                Intent intent = new Intent(context, PhotoActivity.class);
                                startActivityForResult(intent, Constants.REQUEST_POST_PHOTO);
                                */
                            } else if (position == 1) {
                                /*
                                Intent intent = new Intent(context, VideoLinkActivity.class);
                                startActivityForResult(intent, Constants.REQUEST_POST_VIDEO);
                                */
                            } else if (position == 2) {
                                WebLinkDialog dialog = new WebLinkDialog(context);
                                dialog.show();
                                /*
                                Intent intent = new Intent(context, WebLinkActivity.class);
                                startActivityForResult(intent, Constants.REQUEST_POST_WEB);
                                */
                            }
                        }
                    });
                    break;
            }
        }
    };

    public static ServiceCreate2Fragment newInstance(Context ctx, SkillModel s) {
        ServiceCreate2Fragment fragment = new ServiceCreate2Fragment();
        fragment.context = ctx;
        fragment.skill = s;

        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_service_create2, container, false);
        ButterKnife.bind(this, view);

        initViewVariables(view);

        return view;
    }

    private void initViewVariables(View view) {
        btnAdd.setOnClickListener(clickListener);
    }


}
