package com.dstewart.telecomproject4;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class NoFileFoundDialogFragment extends DialogFragment {
    public NoFileFoundDialogFragment() {

    }

    public static NoFileFoundDialogFragment newinstance(String title) {
        NoFileFoundDialogFragment frag = new NoFileFoundDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.alert_NoFileFound);
        return builder.create();
    }
}
