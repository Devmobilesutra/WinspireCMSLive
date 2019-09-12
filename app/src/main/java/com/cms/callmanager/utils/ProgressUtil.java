package com.cms.callmanager.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;

import com.cms.callmanager.R;
import com.cms.callmanager.activities.CreateTransferOrderActivity;

public class ProgressUtil {

    public static ProgressDialog progressDialog;


    public static void showProgressBar(Context context, View root, int id) {

        if (context == null)
            return;

        View view = (View) root.findViewById(id);

        ProgressBar prgBar = (ProgressBar) view
                .findViewById(R.id.progressBarCircular);
        Animation rotate = AnimationUtils.loadAnimation(context,
                R.anim.anim_rotate);
        view.setVisibility(View.VISIBLE);
        prgBar.setVisibility(View.VISIBLE);
        prgBar.startAnimation(rotate);
    }

    public static void hideProgressBar(View root, int id) {

        if (null != root) {
            View view = (View) root.findViewById(id);
            view.setVisibility(View.GONE);
            ProgressBar prgBar = (ProgressBar) view
                    .findViewById(R.id.progressBarCircular);
            prgBar.clearAnimation();
            prgBar.setVisibility(View.GONE);

        }
    }

    public static void ShowBar(Context context) {
        progressDialog = ProgressDialog.show(context,
                context.getResources().getString(R.string.app_name),
                "Loading", false, false);
        progressDialog.setCancelable(false);
    }

    public static void hideBar() {
        if (progressDialog != null)
            if (progressDialog.isShowing())
                progressDialog.dismiss();
    }
}
