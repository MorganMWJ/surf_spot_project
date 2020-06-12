package com.example.morgan.surf_spot_app.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.morgan.surf_spot_app.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SettingsFragment extends Fragment {

    /* Inputs for extra options section */
    private SeekBar radiusInput;
    private CheckBox surfKeywordInput;
    private Spinner placeTypeInput;
    private EditText keyInput;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                      Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){

        /* Handle on inputs */
        this.keyInput = view.findViewById(R.id.edit_key);
        this.surfKeywordInput = view.findViewById(R.id.keyword_checkbox);
        this.placeTypeInput = view.findViewById(R.id.type_dropdown);
        this.radiusInput = view.findViewById(R.id.radius_bar);

        /* By default 'surf' keyword is used */
        this.surfKeywordInput.setChecked(true);

        /* Default search radius is 10000m / 10km */
        this.radiusInput.setProgress(10);

        /* Functionality of radius SeekBar */
        this.radiusInput.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                /* Do Nothing */
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                /* Do Nothing */
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                /* Set radius in MainActivity */
                ((MainActivity)getActivity()).setRadius(seekBar.getProgress());

                /* Alert user to successful change of radius */
                Toast.makeText(getActivity(),
                "Radius set: " + seekBar.getProgress() + "km",
                        Toast.LENGTH_SHORT).show();
            }
        });

        /* Functionality of place type spinner */
        this.placeTypeInput.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                /* Set place type on MainActivity */
                String placeType = placeTypeInput.getSelectedItem().toString();
                ((MainActivity)getActivity()).setPlaceType(placeType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                /* Do Nothing */
            }

        });

        /* Set functionality of surfKeyword checkbox change  */
        this.surfKeywordInput.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                   @Override
                   public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                       ((MainActivity)getActivity()).setSurfKeyword(isChecked);
                   }
               }
        );

        /* Set functionality of reset default API key button */
        Button resetKeyButton = view.findViewById(R.id.reset_default_api_key_button);
        resetKeyButton.setOnClickListener(v -> {
            ((MainActivity)getActivity()).resetApiKey();
            TextView currentKeyTextView = view.findViewById(R.id.current_key);
            String currentApiKey = ((MainActivity)getActivity()).getCurrentApiKey();
            currentKeyTextView.setText(currentApiKey);
            keyInput.getText().clear();
        });


        /* Set functionality of set API key button */
        Button setKeyButton = view.findViewById(R.id.set_api_key_button);
        setKeyButton.setOnClickListener(v -> {
            /* Get text from new key input */
            ((MainActivity)getActivity()).setCurrentApiKey(keyInput.getText().toString());
            TextView currentKeyTextView = view.findViewById(R.id.current_key);
            String currentApiKey = ((MainActivity)getActivity()).getCurrentApiKey();
            currentKeyTextView.setText(currentApiKey);
            keyInput.getText().clear();
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("radius_input", this.radiusInput.getProgress());
        outState.putInt("type_input_spinner_position", this.placeTypeInput.getSelectedItemPosition());
        outState.putBoolean("use_surf_input", this.surfKeywordInput.isChecked());
        outState.putString("key_input", this.keyInput.getText().toString());
    }

}
