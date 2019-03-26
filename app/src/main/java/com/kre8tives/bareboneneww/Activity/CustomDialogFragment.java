package com.kre8tives.bareboneneww.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;

import com.kre8tives.bareboneneww.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CustomDialogFragment extends DialogFragment {
    Dialog d;
    Button btnok;

    public CustomDialogFragment() {

    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        d=null;

        AlertDialog.Builder ab=new AlertDialog.Builder(getActivity());

        View v=getActivity().getLayoutInflater().inflate(R.layout.fragment_custom_dialog,null);

        ab.setView(v);
        d=ab.create();

        return d;

    }



}
