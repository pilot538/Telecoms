package com.dstewart.telecomproject4;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.CellInfo;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

public class WhitelistedCellTowers extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whitelisted_cell_towers);
        TextView textView = (TextView) findViewById(R.id.whitelistedCellTowersTextView);
        String allCellTowers = getString(R.string.text_noWhitelistedCellTowers);
        FileInputStream fIn;
        int size;
        byte[] buffer;
        String json;
        JSONObject reader;
        //read whitelistedCellTowers file
        try {
            fIn = openFileInput("whitelistedTowers");
            size = fIn.available();
            buffer = new byte[size];
            fIn.read(buffer);
            fIn.close();
            json = new String(buffer, "UTF-8");
            JSONArray cellTowersJSON = new JSONArray(json);
            allCellTowers = "";
            for(int i = 0; i<cellTowersJSON.length();i++) {
                JSONObject cellTower = cellTowersJSON.getJSONObject(i);
                String networkType = cellTower.getString("networkType");
                if (networkType.compareToIgnoreCase("GSM") == 0) {
                    //network is GSM
                    allCellTowers += "\n\nNetwork Type: " + networkType;
                    allCellTowers += "\nCID: " + cellTower.getString("CID");
                    allCellTowers += "\nMCC: " + cellTower.getString("MCC");
                    allCellTowers += "\nMNC: " + cellTower.getString("MNC");
                    allCellTowers += "\nLAC: " + cellTower.getString("LAC");
                }
            }
        } catch (Exception e) {
            showNoFileFoundDialog();
            return;
        }
        textView.setText(allCellTowers);


    }

    private void showNoFileFoundDialog() {
        FragmentManager fm = getSupportFragmentManager();
        NoFileFoundDialogFragment nff = NoFileFoundDialogFragment.newinstance("No Whitelisted Towers");
        nff.show(fm, "fragment_no_file_found");
    }
}
