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
import android.widget.AdapterView;
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
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by rnewton on 8/9/16.
 */
public class SettingsFragment extends DialogFragment {

    public interface SettingsDialogListener {
        void onFinishDialog(Boolean changesMade);
    }

    private SimpleDateFormat etDateFormat = new SimpleDateFormat("MMM d, yyyy");
    public ArticleClient articleClient;  // TODO should stop using this pattern
    SearchActivity listener;
    String targetDate;
    Boolean changesMade;
    Date beginDate;
    Date endDate;
    ArrayList<String> topics;

    @BindView(R.id.etBeginDate) EditText etEndDate;
    @BindView(R.id.etEndDate) EditText etBeginDate;
    @BindView(R.id.btnSave) Button btnSave;
    @BindView(R.id.spnrSort) Spinner spnrSort;
    @BindView(R.id.tvBeginDate) TextView tvBeginDate;
    @BindView(R.id.cbArts) CheckBox cbArts;
    @BindView(R.id.cbFashion) CheckBox cbFashion;
    @BindView(R.id.cbSports) CheckBox cbSports;


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
        ButterKnife.bind(this, view);
        changesMade = false;
        setupListeners();
        populateFields();

    }

    public void populateFields() {
        // Is there a better way to render this than with all these if statements
        List<String> sortArray = Arrays.asList(getResources().getStringArray(R.array.sort_options));

        topics = articleClient.topics;
        beginDate = articleClient.beginDate;
        endDate = articleClient.endDate;

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
        if (articleClient.topics.contains(getResources().getString(R.string.topic_arts))) {
            cbArts.setChecked(true);
        }
        if (articleClient.topics.contains(getResources().getString(R.string.topic_fashion_style))) {
            cbFashion.setChecked(true);
        }
        if (articleClient.topics.contains(getResources().getString(R.string.topic_sports))) {
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
                targetDate = "endDate";
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

        spnrSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                changesMade = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
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
        changesMade = true;
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        SimpleDateFormat etDateFormat = new SimpleDateFormat("MMM d, yyyy");
        String dateString = etDateFormat.format(calendar.getTime());

        if (targetDate.equals("beginDate")) { // This seems really dumb
            // TODO don't set the article client here. wait for save
            beginDate = calendar.getTime();
            etBeginDate.setText(dateString);
        }
        if (targetDate.equals("endDate")) { // This seems really dumb
            // TODO don't set the article client here. wait for save
            endDate = calendar.getTime();
            etEndDate.setText(dateString);
        }
    }

    public void onCheckboxClicked() {
        changesMade = true;
        topics.clear();
        if (cbArts.isChecked()) {
            topics.add(getResources().getString(R.string.topic_arts));
        }
        if (cbFashion.isChecked()) {
            topics.add(getResources().getString(R.string.topic_fashion_style));
        }
        if (cbSports.isChecked()) {
            topics.add(getResources().getString(R.string.topic_sports));
        }
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
        if (changesMade) {
            String sort = spnrSort.getSelectedItem().toString();
            articleClient.setSort(sort);
            articleClient.topics = topics;
            articleClient.beginDate = beginDate;
            articleClient.endDate = endDate;
            listener.onFinishDialog(changesMade);
        }
        dismiss();
    }
}
