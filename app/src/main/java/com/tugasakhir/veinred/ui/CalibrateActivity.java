package com.tugasakhir.veinred.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Spinner;
import android.widget.TextView;

import com.flir.thermalsdk.image.DistanceUnit;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.tugasakhir.veinred.R;
import com.tugasakhir.veinred.databinding.ActivityCalibrateBinding;
import com.tugasakhir.veinred.util.CalibrationHandler;
import com.tugasakhir.veinred.util.CameraHandler;

import java.util.Date;
import java.util.Map;

public class CalibrateActivity extends AppCompatActivity {

    private ActivityCalibrateBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCalibrateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setView();

        binding.btnView.setOnClickListener(v -> {
            viewLog();
        });

        binding.btnBack.setOnClickListener(v -> {
            finish();
        });

        binding.btnSave.setOnClickListener(v -> {
            saveAll();
        });

    }

    private void setView() {
        binding.atmosphericTemperatureValue.setText(String.valueOf(CalibrationHandler.kToC(CalibrationHandler.atmosphericTemperature)), TextView.BufferType.EDITABLE);
        binding.reflectiveTemperatureValue.setText(String.valueOf(CalibrationHandler.kToC(CalibrationHandler.reflectiveTemperature)), TextView.BufferType.EDITABLE);
        binding.externalOpticsTemperatureValue.setText(String.valueOf(CalibrationHandler.kToC(CalibrationHandler.externalOpticsTemperature)), TextView.BufferType.EDITABLE);
        binding.distanceUnitValue.setSelection(getIndex(binding.distanceUnitValue, CalibrationHandler.distanceUnit.name()));
        if (CalibrationHandler.palette != null) {
            binding.paletteValue.setSelection(getIndex(binding.paletteValue, CalibrationHandler.palette.name));
        }
        binding.distanceValue.setText(String.valueOf(CalibrationHandler.distance), TextView.BufferType.EDITABLE);
        binding.emissivityValue.setText(String.valueOf(CalibrationHandler.emissivity), TextView.BufferType.EDITABLE);
        binding.externalOpticsTransmissionValue.setText(String.valueOf(CalibrationHandler.externalOpticsTransmission), TextView.BufferType.EDITABLE);
        binding.relativeHumidityValue.setText(String.valueOf(CalibrationHandler.relativeHumidity), TextView.BufferType.EDITABLE);
        binding.transmissionValue.setText(String.valueOf(CalibrationHandler.transmission), TextView.BufferType.EDITABLE);
    }

    private int getIndex(Spinner spinner, String myString) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                return i;
            }
        }
        return 0;
    }

    public void saveAll() {
        CalibrationHandler.setAtmosphericTemperature(Double.parseDouble(binding.atmosphericTemperatureValue.getText().toString()));
        CalibrationHandler.setReflectiveTemperature(Double.parseDouble(binding.reflectiveTemperatureValue.getText().toString()));
        CalibrationHandler.setExternalOpticsTemperature(Double.parseDouble(binding.externalOpticsTemperatureValue.getText().toString()));
        CalibrationHandler.setDistanceUnit(DistanceUnit.valueOf(binding.distanceUnitValue.getItemAtPosition(binding.distanceUnitValue.getSelectedItemPosition()).toString()));
        CalibrationHandler.setPalette(binding.paletteValue.getItemAtPosition(binding.paletteValue.getSelectedItemPosition()).toString());
        CalibrationHandler.setDistance(Double.parseDouble(binding.distanceValue.getText().toString()));
        CalibrationHandler.setEmissivity(Double.parseDouble(binding.emissivityValue.getText().toString()));
        CalibrationHandler.setExternalOpticsTransmission(Double.parseDouble(binding.externalOpticsTransmissionValue.getText().toString()));
        CalibrationHandler.setRelativeHumidity(Double.parseDouble(binding.relativeHumidityValue.getText().toString()));
        CalibrationHandler.setTransmission(Double.parseDouble(binding.transmissionValue.getText().toString()));
    }

    public void viewLog() {
        if (CameraHandler.tempLog != null) {
            StringBuilder msgDialog = new StringBuilder();

            if (CameraHandler.tempLog.size() != 0) {
                for (Map.Entry<Long, String> entry : CameraHandler.tempLog.entrySet()) {
                    Date date = new Date(entry.getKey());
                    msgDialog.append(date.toString()).append(":\n==>").append(entry.getValue()).append("\n");
                }
            } else {
                msgDialog.append("There are no logs recorded.");
            }

            String title = CameraHandler.tempLog.size() + " Readings:";
            MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(CalibrateActivity.this, R.style.MyThemeOverlay_MaterialComponents_MaterialAlertDialog);
            alertDialogBuilder.setTitle(title);
            alertDialogBuilder.setMessage(msgDialog.toString());
            alertDialogBuilder.setNeutralButton("Save", (dialog, which) -> {
                CameraHandler.saveLog(this, false);
            });
            alertDialogBuilder.setNegativeButton("Reset", (dialog, which) -> {
                CameraHandler.resetLog();
                viewLog();
            });
            alertDialogBuilder.setPositiveButton("Close", (dialog, which) -> dialog.dismiss());
            alertDialogBuilder.show();
        }
    }
}