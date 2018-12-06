package com.dstewart.telecomproject4;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.CellIdentityCdma;
import android.telephony.CellIdentityGsm;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityWcdma;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.CellSignalStrength;
import android.telephony.CellSignalStrengthCdma;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthWcdma;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.FileOutputStream;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

public class CellTowers extends AppCompatActivity {
    String cellTowerInfo = "\n\rMISSING PERMISSIONS";
    JSONArray cellTowersJSON = new JSONArray();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cell_towers);
        TextView textView = (TextView) findViewById(R.id.cellTowerInfo);
        List<CellInfo> cellInfoList = null;
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            cellInfoList = tm.getAllCellInfo();
            cellTowerInfo = "";
        }
        if(cellInfoList!= null) {
            for (final CellInfo info : cellInfoList) {
                //Network Type
                cellTowerInfo += "\n\n\rNetwork Type: " + tm.getNetworkType();
                if(info instanceof CellInfoGsm) {
                    final CellSignalStrengthGsm gsm = ((CellInfoGsm) info).getCellSignalStrength();
                    final CellIdentityGsm identityGsm = ((CellInfoGsm) info).getCellIdentity();
                    JSONObject cellTower = new JSONObject();
                    //Use these to display information
                    cellTowerInfo += "\n\rCID: " + identityGsm.getCid();
                    cellTowerInfo += "\n\rMCC: " + identityGsm.getMccString();
                    cellTowerInfo += "\n\rMNC: " + identityGsm.getMncString();
                    cellTowerInfo += "\n\rLAC: " + identityGsm.getLac();
                    cellTowerInfo += "\n\rSignal Strength: " + gsm.getDbm();
                    try {
                        cellTower.put("networkType", "GSM");
                        cellTower.put("CID", identityGsm.getCid());
                        cellTower.put("MCC", identityGsm.getMccString());
                        cellTower.put("MNC", identityGsm.getMncString());
                        cellTower.put("LAC", identityGsm.getLac());
                        cellTowersJSON.put(cellTower);
                    } catch (JSONException e) {
                        //bad json stuff
                    }

                }
                else if(info instanceof CellInfoCdma) {
                    final CellSignalStrengthCdma cdma = ((CellInfoCdma) info).getCellSignalStrength();
                    final CellIdentityCdma identityCdma = ((CellInfoCdma) info).getCellIdentity();
                    JSONObject cellTower = new JSONObject();
                    //Use to display information
                    cellTowerInfo += "\n\rCID: " + identityCdma.getBasestationId();
                    cellTowerInfo += "\n\rMNC: " + identityCdma.getSystemId();
                    cellTowerInfo += "\n\rLAC: " + identityCdma.getNetworkId();
                    cellTowerInfo += "\n\rSID: " + identityCdma.getSystemId();
                    cellTowerInfo += "\n\rLatitude: " + identityCdma.getLatitude();
                    cellTowerInfo += "\n\rLongitude: " + identityCdma.getLongitude();
                    cellTowerInfo += "\n\rSignal Strength: " + cdma.getDbm();
                    try {
                        cellTower.put("networkType", "CDMA");
                        cellTower.put("CID", identityCdma.getBasestationId());
                        cellTower.put("MNC", identityCdma.getSystemId());
                        cellTower.put("LAC", identityCdma.getNetworkId());
                        cellTower.put("SID",identityCdma.getSystemId());
                        cellTower.put("Latitude", identityCdma.getLatitude());
                        cellTower.put("Longitude", identityCdma.getLongitude());
                        cellTowersJSON.put(cellTower);
                    } catch (JSONException e) {

                    }
                }
                else if(info instanceof CellInfoLte) {
                    final CellSignalStrengthLte lte = ((CellInfoLte) info).getCellSignalStrength();
                    final CellIdentityLte identityLte = ((CellInfoLte) info).getCellIdentity();
                    JSONObject cellTower = new JSONObject();
                    //Use these to display information
                    cellTowerInfo += "\n\rCID: " + identityLte.getCi();
                    cellTowerInfo += "\n\rMCC: " + identityLte.getMccString();
                    cellTowerInfo += "\n\rMNC: " + identityLte.getMncString();
                    cellTowerInfo += "\n\rTracking Area Code: " + identityLte.getTac();
                    cellTowerInfo += "\n\rSignal Strength: " + lte.getDbm();
                    try {
                        cellTower.put("networkType", "LTE");
                        cellTower.put("CID", identityLte.getCi());
                        cellTower.put("MCC", identityLte.getMccString());
                        cellTower.put("MNC", identityLte.getMncString());
                        cellTower.put("trackingAreaCode", identityLte.getTac());
                        cellTowersJSON.put(cellTower);
                    } catch (JSONException e) {

                    }
                }
                else if(info instanceof CellInfoWcdma) {
                    final CellSignalStrengthWcdma wcdma = ((CellInfoWcdma) info).getCellSignalStrength();
                    final CellIdentityWcdma identityWcdma = ((CellInfoWcdma) info).getCellIdentity();
                    JSONObject cellTower = new JSONObject();
                    //Use these to display information
                    cellTowerInfo += "\n\rCID: " + identityWcdma.getCid();
                    cellTowerInfo += "\n\rMCC: " + identityWcdma.getMccString();
                    cellTowerInfo += "\n\rMNC: " + identityWcdma.getMncString();
                    cellTowerInfo += "\n\rLAC: " + identityWcdma.getLac();
                    cellTowerInfo += "\n\rPSC: " + identityWcdma.getPsc();
                    cellTowerInfo += "\n\rSignal Strength: " + wcdma.getDbm();
                    try {
                        cellTower.put("networkType", identityWcdma.getCid());
                        cellTower.put("CID", identityWcdma.getCid());
                        cellTower.put("MCC", identityWcdma.getMccString());
                        cellTower.put("MNC", identityWcdma.getMncString());
                        cellTower.put("LAC", identityWcdma.getLac());
                        cellTower.put("PSC", identityWcdma.getPsc());
                        cellTowersJSON.put(cellTower);
                    } catch (JSONException e) {

                    }
                }
            }
        }

        textView.setText(cellTowerInfo);
    }

    public void refreshCellInfo(View view) {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    public void saveAllTowers(View view) {
        FileOutputStream fOut;
        try {
            fOut = openFileOutput("whitelistedTowers", Context.MODE_PRIVATE);
            String toWrite = cellTowersJSON.toString();
            fOut.write(toWrite.getBytes());
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
