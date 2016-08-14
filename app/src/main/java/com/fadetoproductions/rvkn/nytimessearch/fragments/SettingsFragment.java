package com.fadetoproductions.rvkn.nytimessearch.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.fadetoproductions.rvkn.nytimessearch.R;
import com.fadetoproductions.rvkn.nytimessearch.activities.SearchActivity;
import com.fadetoproductions.rvkn.nytimessearch.clients.ArticleClient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * Created by rnewton on 8/9/16.
 */
public class SettingsFragment extends DialogFragment {

    public interface SettingsDialogListener {
        void onFinishDialog(Boolean changesMade);
    }

    SearchActivity listener;
    public ArticleClient articleClient;  // TODO should stop using this pattern
    EditText etBeginDate;
    EditText etEndDate;
    Button btnSave;
    Spinner spnrSort;
    TextView tvBeginDate;
    CheckBox cbFashion;
    CheckBox cbArts;
    CheckBox cbSports;
    String targetDate;

    private SimpleDateFormat etDateFormat = new SimpleDateFormat("MMM d, yyyy");

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity){
            this.listener = (SearchActivity) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return inflater.inflate(R.layout.settings_fragment, parent, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        etBeginDate = (EditText) view.findViewById(R.id.etBeginDate);
        etEndDate = (EditText) view.findViewById(R.id.etEndDate);
        btnSave = (Button) view.findViewById(R.id.btnSave);
        spnrSort = (Spinner) view.findViewById(R.id.spnrSort);
        tvBeginDate = (TextView) view.findViewById(R.id.tvBeginDate);
        cbArts = (CheckBox) view.findViewById(R.id.cbArts);
        cbFashion = (CheckBox) view.findViewById(R.id.cbFashion);
        cbSports = (CheckBox) view.findViewById(R.id.cbSports);
        setupListeners();
        populateFields();

    }

    public void populateFields() {
        // Is there a better way to render this than with all these if statements
        List<String> sortArray = Arrays.asList(getResources().getStringArray(R.array.sort_options));
        if (articleClient.getSort() != null) {
            int indexOfOption = sortArray.indexOf(articleClient.getSort());
            spnrSort.setSelection(indexOfOption);
        }
        if (articleClient.beginDate != null) {
            etBeginDate.setText(etDateFormat.format(articleClient.beginDate.getTime()));
        }
        if (articleClient.endDate != null) {
            etEndDate.setText(etDateFormat.format(articleClient.endDate.getTime()));
        }
        if (articleClient.topics.contains("arts")) {  // TODO make this an enum
            cbArts.setChecked(true);
        }
        if (articleClient.topics.contains("fashion")) {  // TODO make this an enum
            cbFashion.setChecked(true);
        }
        if (articleClient.topics.contains("sports")) {  // TODO make this an enum
            cbSports.setChecked(true);
        }
    }


    public void setupListeners() {
        etBeginDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                targetDate = "beginDate";
                openDatePickerDialogue();
            }
        });
        etEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                targetDate = "etEndDate";
                openDatePickerDialogue();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { onSave();}
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

        SimpleDateFormat etDateFormat = new SimpleDateFormat("MMM d, yyyy");
        String dateString = etDateFormat.format(calendar.getTime());

        if (targetDate.equals("beginDate")) { // This seems really dumb
            // TODO don't set the article client here. wait for save
            articleClient.beginDate = calendar.getTime();
            etBeginDate.setText(dateString);
        }
        if (targetDate.equals("endDate")) { // This seems really dumb
            // TODO don't set the article client here. wait for save
            articleClient.endDate = calendar.getTime();
            etEndDate.setText(dateString);
        }
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
        // TODO don't set the article client here. wait for save
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
}
