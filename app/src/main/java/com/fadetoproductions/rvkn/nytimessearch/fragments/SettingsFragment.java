package com.fadetoproductions.rvkn.nytimessearch.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.fadetoproductions.rvkn.nytimessearch.R;
import com.fadetoproductions.rvkn.nytimessearch.activities.SearchActivity;
import com.fadetoproductions.rvkn.nytimessearch.clients.ArticleClient;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * Created by rnewton on 8/9/16.
 */
public class SettingsFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Log.d("Date", "Year=" + year + " Month=" + (monthOfYear + 1) + " day=" + dayOfMonth);
        view.setVisibility(View.INVISIBLE);

    }

    public interface SettingsDialogListener {
        void onFinishDialog(Boolean changesMade);
    }


    SearchActivity listener;
    public ArticleClient articleClient;  // TODO should stop using this pattern
    EditText etBeginDate;
    Button btnSave;
    Spinner spnrSort;
    DatePicker datePicker;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity){
            this.listener = (SearchActivity) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        // Defines the xml file for the fragment
//        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return inflater.inflate(R.layout.settings_fragment, parent, false);
    }

    private void setupDatePickers() {
        datePicker.setVisibility(View.INVISIBLE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());


        Log.v("sdafdsa", "Hewrewre");


        datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int year, int month, int dayOfMonth) {
                Log.d("Date", "Year=" + year + " Month=" + (month + 1) + " day=" + dayOfMonth);
                datePicker.setVisibility(View.INVISIBLE);
                Log.v("sdafdsa", "Hewrewre222");
            }
        });

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        etBeginDate = (EditText) view.findViewById(R.id.etBeginDate);
        btnSave = (Button) view.findViewById(R.id.btnSave);
        spnrSort = (Spinner) view.findViewById(R.id.spnrSort);
        datePicker = (DatePicker) view.findViewById(R.id.datePicker);


        setupDatePickers();




//        datePicker.setOndate






        List<String> sortArray = Arrays.asList(getResources().getStringArray(R.array.sort_options));

        if (articleClient.getSort() != null) {
            int indexOfOption = sortArray.indexOf(articleClient.getSort());
             spnrSort.setSelection(indexOfOption);
        }


        etBeginDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker.setVisibility(View.VISIBLE);

            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSave();
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

    public void onSave() {
        Boolean changesMade = true;

        // Get the values
        String sort = spnrSort.getSelectedItem().toString();
        articleClient.setSort(sort);


        listener.onFinishDialog(changesMade);
        dismiss();

    }


}
