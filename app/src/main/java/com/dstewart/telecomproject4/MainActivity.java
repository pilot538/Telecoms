package com.dstewart.telecomproject4;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.CellInfo;
import android.telephony.CellSignalStrength;
import android.telephony.NeighboringCellInfo;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;


public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.dstewart.telecomproject4.MESSAGE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView1 = (TextView) findViewById(R.id.textView1);
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        String IMEINumber = "NO PERMISSIONS";
        String IMSINumber = "NO PERMISSIONS";
        String SIMSerialNumber = "NO PERMISSIONS";
        String networkCountryISO = "NO PERMISSIONS";
        String SIMCountyISO = "NO PERMISSIONS";
        String softwareVersion = "NO PERMISSIONS";
        String voiceMailNumber = "NO PERMISSIONS";
        String networkOperator = "NO PERMISSIONS";
        String networkOperatorName = "NO PERMISSIONS";
        int signalStrength = -1;
        int LAC = -1;
        int cellID = -1;

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
             IMEINumber = tm.getImei();
             IMSINumber = tm.getSubscriberId();
             SIMSerialNumber = tm.getSimSerialNumber();
             networkCountryISO = tm.getNetworkCountryIso();
             networkOperator = tm.getNetworkOperator();
             networkOperatorName = tm.getNetworkOperatorName();
             SIMCountyISO = tm.getSimCountryIso();
             softwareVersion = tm.getDeviceSoftwareVersion();
             voiceMailNumber = tm.getVoiceMailNumber();
             signalStrength = tm.getSignalStrength().getGsmSignalStrength();
        }
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            GsmCellLocation cellLocation = (GsmCellLocation) tm.getCellLocation();
            LAC = cellLocation.getLac();
            cellID = cellLocation.getCid();
        }

        String phoneInfo = "Cellular Information:\n";
        phoneInfo += "\nIMEI: " + IMEINumber;
        phoneInfo += "\nIMSI: " + IMSINumber;
        phoneInfo += "\nLAC: " + LAC;
        phoneInfo += "\nCID: " + cellID;
        phoneInfo += "\nSIM Serial Number: " + SIMSerialNumber;
        phoneInfo += "\nMCC: " + networkCountryISO;
        phoneInfo += "\nMCC+MNC: " + networkOperator;
        phoneInfo += "\nNetwork Operator Name: " + networkOperatorName;
        phoneInfo += "\nSIM Country ISO: " + SIMCountyISO;
        phoneInfo += "\nSoftware Version: " + softwareVersion;
        phoneInfo += "\nVoice Mail Number: " + voiceMailNumber;
        phoneInfo += "\nSignal Strength: " + signalStrength;

        textView1.setText(phoneInfo);

    }

    public void showCellTowerInfo(View view) {
        Intent intent = new Intent(this, CellTowers.class);
        startActivity(intent);
    }

    public void refreshCellInfo(View view) {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
}