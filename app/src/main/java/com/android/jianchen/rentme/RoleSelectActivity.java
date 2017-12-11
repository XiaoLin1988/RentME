package com.android.jianchen.rentme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.android.jianchen.rentme.Utils.Constants;
import com.android.jianchen.rentme.Utils.Utils;

/**
 * Created by emerald on 5/30/2017.
 */
public class RoleSelectActivity extends AppCompatActivity implements View.OnClickListener{
    private LinearLayout btnConsumer;
    private LinearLayout btnTalent;

    protected void onCreate(Bundle savedBundle) {
        super.onCreate(savedBundle);
        setContentView(R.layout.activity_roleselect);

        initViewVariables();
    }

    private void initViewVariables() {
        btnConsumer = (LinearLayout)findViewById(R.id.btn_role_consumer);
        btnConsumer.setOnClickListener(this);

        btnTalent = (LinearLayout)findViewById(R.id.btn_role_talent);
        btnTalent.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(RoleSelectActivity.this, RegisterActivity.class);
        Utils.saveUserInfo(getApplicationContext(), null);
        switch (v.getId()) {
            case R.id.btn_role_consumer:
                intent.putExtra(Constants.EXTRA_USERTYPE, 1);
                startActivity(intent);
                finish();
                break;
            case R.id.btn_role_talent:
                intent.putExtra(Constants.EXTRA_USERTYPE, 2);
                startActivity(intent);
                finish();
                break;
        }
    }
}
