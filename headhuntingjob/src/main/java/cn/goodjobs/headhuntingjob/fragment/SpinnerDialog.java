package cn.goodjobs.headhuntingjob.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

import cn.goodjobs.common.util.StringUtil;

public class SpinnerDialog {
    private int id;
    public int chooseIndex;
    public Dialog dialog;
    public String title;
    public SpinnerChooseListener listener;
    public String[] strs;

    public SpinnerDialog(int id) {
        this.id = id;
    }

    public void showSpinner(Context context) {
        if (dialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            if (!StringUtil.isEmpty(title)) {
                builder.setTitle(title);
            }
            dialog = builder.setSingleChoiceItems(strs,
                    chooseIndex, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog,
                                            int position) {
                            chooseIndex = position;
                            dialog.dismiss();
                            listener.choose(position, id);
                        }
                    }).create();
        }
        dialog.show();
    }

    public interface SpinnerChooseListener {
        public void choose(int position, int id);
    }
}

