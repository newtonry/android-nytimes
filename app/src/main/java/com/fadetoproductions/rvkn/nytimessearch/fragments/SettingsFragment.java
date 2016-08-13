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
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.fadetoproductions.rvkn.nytimessearch.R;
import com.fadetoproductions.rvkn.nytimessearch.activities.SearchActivity;
import com.fadetoproductions.rvkn.nytimessearch.clients.ArticleClient;

import java.util.ArrayList;
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
        hideDatePicker();
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
    Button btnDatePickerSave;
    TextView tvBeginDate;
    CheckBox cbFashion;
    CheckBox cbArts;
    CheckBox cbSports;


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
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int year, int month, int dayOfMonth) {
                Log.d("Date", "Year=" + year + " Month=" + (month + 1) + " day=" + dayOfMonth);
                datePicker.setVisibility(View.INVISIBLE);
            }
        });

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        etBeginDate = (EditText) view.findViewById(R.id.etBeginDate);
        btnSave = (Button) view.findViewById(R.id.btnSave);
        spnrSort = (Spinner) view.findViewById(R.id.spnrSort);
        datePicker = (DatePicker) view.findViewById(R.id.datePicker);
        btnDatePickerSave = (Button) view.findViewById(R.id.btnDatePickerSave);
        tvBeginDate = (TextView) view.findViewById(R.id.tvBeginDate);
        cbArts = (CheckBox) view.findViewById(R.id.cbArts);
        cbFashion = (CheckBox) view.findViewById(R.id.cbFashion);
        cbSports = (CheckBox) view.findViewById(R.id.cbSports);
        setupListeners();
        setupDatePickers();

        List<String> sortArray = Arrays.asList(getResources().getStringArray(R.array.sort_options));

        if (articleClient.getSort() != null) {
            int indexOfOption = sortArray.indexOf(articleClient.getSort());
             spnrSort.setSelection(indexOfOption);
        }
    }


    public void setupListeners() {
        etBeginDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePickerDialogue();
//                showDatePicker();
            }
        });

        btnDatePickerSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {hideDatePicker();}
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSave();
            }
        });

        cbArts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { onCheckboxClicked();}
        });
        cbFashion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { onCheckboxClicked();}
        });
        cbSports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { onCheckboxClicked();}
        });
    }

    public void openDatePickerDialogue() {
        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                datePicked(year, month, day);
            }
        };
        DatePickerDialog dpDialogue = new DatePickerDialog(getActivity(), listener, 2015, 1, 1);
        dpDialogue.show();
    }

    public void datePicked(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        todo.dueDate = calendar.getTime();

    }


    public void onCheckboxClicked() {
        ArrayList<String> topics = new ArrayList<>();
        if (cbArts.isChecked()) {
            topics.add("arts");
        }
        if (cbFashion.isChecked()) {
            topics.add("fashion");
        }
        if (cbSports.isChecked()) {
            topics.add("sports");
        }

        articleClient.topics = topics;
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
        window.setLayout((int) (size.x * 0.9), (int) (size.y * 0.9));
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

    private void hideDatePicker() {
        datePicker.setVisibility(View.INVISIBLE);
        btnDatePickerSave.setVisibility(View.INVISIBLE);

        etBeginDate.setVisibility(View.VISIBLE);
        btnSave.setVisibility(View.VISIBLE);
        spnrSort.setVisibility(View.VISIBLE);
        datePicker.setVisibility(View.VISIBLE);
        tvBeginDate.setVisibility(View.VISIBLE);
    }

    private void showDatePicker() {
        etBeginDate.setVisibility(View.INVISIBLE);
        btnSave.setVisibility(View.INVISIBLE);
        spnrSort.setVisibility(View.INVISIBLE);
        tvBeginDate.setVisibility(View.INVISIBLE);

        datePicker.setVisibility(View.VISIBLE);
        btnDatePickerSave.setVisibility(View.VISIBLE);
    }

}
