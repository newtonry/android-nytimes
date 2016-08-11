package com.fadetoproductions.rvkn.nytimessearch.fragments;

import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;

import com.fadetoproductions.rvkn.nytimessearch.R;

/**
 * Created by rnewton on 8/9/16.
 */
public class SettingsFragment extends DialogFragment {


//    @BindView(R.id.etBeginDate) EditText etBeginDate;
    EditText etBeginDate;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
//        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));





        return inflater.inflate(R.layout.settings_fragment, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
//        ButterKnife.bind(view);

        etBeginDate = (EditText) view.findViewById(R.id.etBeginDate);

        etBeginDate.setText("SOME TEXT");
        etBeginDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });


    }


    @Override
    public void onResume() {
        // Store access variables for window and blank point
        Window window = getDialog().getWindow();
        Point size = new Point();
        // Store dimensions of the screen in `size`
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        // Set the width of the dialog proportional to 75% of the screen width
        window.setLayout((int) (size.x * 0.8), (int) (size.y * 0.8));
        window.setGravity(Gravity.CENTER);
        // Call super onResume after sizing
        super.onResume();
    }
}
