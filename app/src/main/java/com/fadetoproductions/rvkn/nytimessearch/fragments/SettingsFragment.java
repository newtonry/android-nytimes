package com.fadetoproductions.rvkn.nytimessearch.fragments;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fadetoproductions.rvkn.nytimessearch.R;

/**
 * Created by rnewton on 8/9/16.
 */
public class SettingsFragment extends DialogFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.settings_fragment, parent, false);
    }
}
