package com.android.jianchen.rentme.helper.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.android.jianchen.rentme.helper.delegator.OnConfirmListener;
import com.android.jianchen.rentme.helper.delegator.OnDialogSelectListener;

/**
 * Created by emerald on 12/5/2017.
 */
public class DialogUtil {
    public static ProgressDialog showProgressDialog(Context context, String message){
        ProgressDialog m_Dialog = new ProgressDialog(context);
        m_Dialog.setMessage(message);
        m_Dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        //m_Dialog.setCancelable(false);
        m_Dialog.setCancelable(true);
        m_Dialog.show();
        return m_Dialog;
    }

    public static AlertDialog showAlertDialog(Context context, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setCancelable(true);
        AlertDialog dialog = builder.show();
        return dialog;
    }

    public static AlertDialog showConfirmDialog(Context context, String message, final OnConfirmListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        listener.onConfirm();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .setCancelable(true);
        AlertDialog dialog = builder.show();
        return dialog;
    }

    public static AlertDialog showSelectDialog(Context context, final CharSequence[] options, final OnDialogSelectListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                listener.onDialogSelect(which);
            }
        });
        /*
        AlertDialog dialog = builder.create();
        ListView listView=dialog.getListView();
        listView.setDivider(new ColorDrawable(Color.GRAY));
        listView.setDividerHeight(1);
        dialog.show();
        */
        AlertDialog dialog = builder.show();
        return dialog;
    }
}
