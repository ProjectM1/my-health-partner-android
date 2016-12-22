package com.serli.myhealthpartner;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.serli.myhealthpartner.controller.ProfileController;
import com.serli.myhealthpartner.model.ProfileData;

import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * View of Profile Activity.<br/>
 * allow the usr to Inform his profile (gender, Birthday, Height and weight)<br/>
 * *  this information will be used to Calculates calories burned <br/>
 */

public class ProfileActivity extends AppCompatActivity {

    private ProfileController controller;

    private Spinner spinnerGender;
    private String[] gender;
    private EditText editTextHeight;
    private EditText editTextWeight;
    private DatePicker datePickerBirthday;
    ProfileData profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_profile);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        controller = new ProfileController(this);
        profile = new ProfileData();

        gender = getResources().getStringArray(R.array.gender);

        spinnerGender = (Spinner) findViewById(R.id.spinner_gender);
        editTextHeight = (EditText) findViewById(R.id.editText_height);
        editTextWeight = (EditText) findViewById(R.id.editText_weight);
        datePickerBirthday = (DatePicker) findViewById(R.id.datePicker_birthday);


        if (controller.getProfile() != null) {
            profile = controller.getProfile();

            spinnerGender.setSelection(profile.getSex());
            editTextHeight.setText(String.valueOf(profile.getSize()));
            editTextWeight.setText(String.valueOf(profile.getWeight()));

            Date d = profile.getBirthday();
            Calendar calendar_tmp = Calendar.getInstance();
            calendar_tmp.setTime(d);
            datePickerBirthday.updateDate(calendar_tmp.get(Calendar.YEAR), calendar_tmp.get(Calendar.MONTH), calendar_tmp.get(Calendar.DAY_OF_MONTH));
        }


        final Button button_validate = (Button) findViewById(R.id.button_validate);
        button_validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profile.setSex(spinnerGender.getSelectedItemPosition());
                profile.setSize(Integer.parseInt(editTextHeight.getText().toString()));
                profile.setWeight(Integer.parseInt(editTextWeight.getText().toString()));

                int day = datePickerBirthday.getDayOfMonth();
                int month = datePickerBirthday.getMonth();
                int year = datePickerBirthday.getYear();

                Calendar calendar_birthday = Calendar.getInstance();
                calendar_birthday.set(year, month, day);

                profile.setBirthday(calendar_birthday.getTime());

                controller.setProfile(profile);
                finish();
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, gender);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(adapter);
    }
}
