package com.android.jianchen.rentme.activity.me.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.jianchen.rentme.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by emerald on 12/15/2017.
 */
public class EditProfileDialog extends Dialog implements View.OnClickListener {
    @Bind(R.id.txt_edit_title)
    TextView txtTitle;

    @Bind(R.id.edit_field)
    EditText editField;

    @Bind(R.id.btn_update)
    Button btnUpdate;

    public interface OnUpdateListener {
        void onUpdate(String field, int tag);
    }

    private OnUpdateListener updateListener;
    private int tag;

    public EditProfileDialog(Context context) {
        super(context);

        this.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.dialog_profile_edit);
        this.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationScale;
        this.setCancelable(true);

        ButterKnife.bind(this);

        initDialog();
    }

    private void initDialog() {
        btnUpdate.setOnClickListener(this);
    }

    public void setTitle(String title) {
        txtTitle.setText(title);
    }

    public void setUpdateListener(OnUpdateListener listener) {
        updateListener = listener;
    }

    public void setTag(int t) {
        tag = t;

        if (tag == 99) { // review dialogue about review
            txtTitle.setText("Please input review");
            btnUpdate.setText("Leave Review");
        }

    }

    public void setDefault(String def) {
        editField.setText(def);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_update:
                if (editField.getText().toString().equals("")) {
                    Toast.makeText(getContext(), "Please input field", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (updateListener != null) {
                    updateListener.onUpdate(editField.getText().toString(), tag);
                }
                dismiss();
                break;
        }
    }
}
