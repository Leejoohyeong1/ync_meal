package com.ync.lee.capstone;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import static android.content.Intent.FLAG_ACTIVITY_NO_HISTORY;

public class CheckActivity extends AppCompatActivity {

    int count=0;

    MaterialBetterSpinner materialBetterSpinner ;
    String Select="";
    String[] SPINNER_DATA = {"직원","일반학생"};
    private InterstitialAd interstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);




        findViewById(R.id.Position_Select).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Select.equals("")){
                    Toast.makeText(CheckActivity.this, "직위를 선택해주세요", Toast.LENGTH_SHORT).show();
                }else{
                    Log.d("Check_Select",Select);
                    SharedPreferences pref = getSharedPreferences("Check_Select", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("Check_Select", Select);
                    editor.commit();


                    Intent intent=new Intent(CheckActivity.this,LodingActivity.class);
                    intent.addFlags(FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);
                    getAD();
                }
            }
        });

        materialBetterSpinner = (MaterialBetterSpinner)findViewById(R.id.material_spinner1);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(CheckActivity.this, android.R.layout.simple_dropdown_item_1line, SPINNER_DATA);

        materialBetterSpinner.setAdapter(adapter);

        materialBetterSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(CheckActivity.this, SPINNER_DATA[position], Toast.LENGTH_SHORT).show();
                Select=SPINNER_DATA[position];
            }
        });



    }


    private void getAD() {
        final InterstitialAd ad = new InterstitialAd(this);
        ad.setAdUnitId(getString(R.string.adID));

        ad.loadAd(new AdRequest.Builder().addTestDevice("16615C674F7AEE94EA391052B933863A").build());

        ad.setAdListener(new AdListener() {
            @Override public void onAdLoaded() {
                if (ad.isLoaded()) {
                    ad.show();
                }
            }
        });
    }







}
