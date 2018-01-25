package com.ync.lee.capstone;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;



public class MainActivity extends AppCompatActivity {
    String Select="";
    String position="";
    Menu_List today;
    String Menu="";
    double pressedX=0,distance=0;
    private AdView mAdView;
    int eatingtime;
    String[] foodtime={"아침","점심","저녁"};
    CustomAdapter Adapter;
    ArrayList<String> arraylist = new ArrayList<String>();
    TextView Time;
    ListView list;
    TextView Today;

    ArrayList<String> Datas= new ArrayList<String>();

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Data");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        eatingtime=GetTiem_for_int();

        findViewById(R.id.search).setOnClickListener(onClickListener);



        myRef.addListenerForSingleValueEvent(valueEventListener);



        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        SharedPreferences pref = getSharedPreferences("Check_Select", MODE_PRIVATE);
        position = pref.getString("Check_Select", "");

        Intent intent = getIntent();

        today =(Menu_List)intent.getSerializableExtra("today");


        list = (ListView)findViewById(R.id.list_item);

        list.setOnTouchListener(onTouchListener);
        arraylist=getmenu();

        Adapter = new CustomAdapter(arraylist,getApplicationContext());

        //Adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, arraylist);
        list.setAdapter(Adapter);


        
        Today = (TextView) findViewById(R.id.today);
        Today.setText(this.today.getData());

        Time = (TextView) findViewById(R.id.time);
        Time.setText(GetTiem());




    }
    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                Datas.add(postSnapshot.getKey());
            }

            Collections.sort(Datas, new Menu_List());


        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };



    View.OnClickListener onClickListener =new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            show();
        }
    };

    View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            switch(event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    // 손가락을 touch 했을 떄 x 좌표값 저장
                    pressedX = event.getX();
                    break;
                case MotionEvent.ACTION_UP:
                    // 손가락을 떼었을 때 저장해놓은 x좌표와의 거리 비교
                    distance = pressedX - event.getX();
                    if (distance < -300) {
                        // 손가락을 왼쪽으로 움직였으면 오른쪽 화면이 나타나야 한다.
                        eatingtime--;
                        if(eatingtime<=-1) eatingtime=0;
                        Time.setText(foodtime[eatingtime]);
                        arraylist=getmenu(eatingtime);
                       // Adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, arraylist);
                        Adapter = new CustomAdapter(arraylist,MainActivity.this);
                        list.setAdapter(Adapter);
                    } else if (distance > 300){
                        // 손가락을 오른쪽으로 움직였으면 왼쪽 화면이 나타나야 한다.
                        eatingtime++;
                        if(eatingtime>=3) eatingtime=2;
                        Time.setText(foodtime[eatingtime]);
                        arraylist=getmenu(eatingtime);
                        Adapter = new CustomAdapter(arraylist,MainActivity.this);

//                        Adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, arraylist);
                        list.setAdapter(Adapter);
                    }
                    Log.d("Touch",Double.toString(distance)+"----"+Integer.toString(eatingtime));
                    break;
            }


            return false;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);


        return super.onCreateOptionsMenu(menu);
    }

    //액션버튼을 클릭했을때의 동작
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        //or switch문을 이용하면 될듯 하다.

        if (id == R.id.action_setting) {
            Toast.makeText(this, "설정", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this,CheckActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    public ArrayList<String> getmenu(){

        ArrayList<String> art= new ArrayList<>();
        switch (GetTiem()){

            case "아침":
                art.addAll(today.getMorning());
                art.addAll(today.getMorning_sub());
                break;
            case "점심":
                art.addAll(getLunchMenu(position));
                break;
            case "저녁":
                art.addAll(today.getDinner());
                break;
        }

        return art;
    }


    public ArrayList<String> getmenu(int i){

        ArrayList<String> art= new ArrayList<>();
        switch (i){
            case 0:
                art.addAll(today.getMorning());
                art.addAll(today.getMorning_sub());
                break;
            case 1:
                art.addAll(getLunchMenu(position));
                break;
            case 2:
                art.addAll(today.getDinner());
                break;
        }

        return art;
    }


    public String GetTiem(){

        Calendar oCalendar = Calendar.getInstance( );

        int time =  oCalendar.get(Calendar.HOUR_OF_DAY);
        String realtime="";

        if(9<=time&&time<13){
            //점심
            realtime="점심";
        }else if(13<=time&&time<19){
            //저녁
            realtime="저녁";
        }else {
            realtime = "아침";
        }

            return  realtime;
    }

    public int GetTiem_for_int(){

        Calendar oCalendar = Calendar.getInstance( );

        int time =  oCalendar.get(Calendar.HOUR_OF_DAY);
        int realtime=0;

       if(9<=time&&time<13){
            //점심
            realtime=1;
        }else if(13<=time&&time<19){
            //저녁
            realtime=2;
        }else{
            //내일 아침
            realtime=0;
        }

        return  realtime;
    }


    public ArrayList<String> getLunchMenu(String position){

        switch (position){
            case "직원":
                return today.getStaff();
            default:
                return today.getStudent();
        }
    }


    void show()
    {



        MaterialBetterSpinner materialBetterSpinner = new MaterialBetterSpinner (this);

        materialBetterSpinner.setText("선택해주세요");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_dropdown_item_1line, Datas);

        materialBetterSpinner.setAdapter(adapter);

        materialBetterSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(CheckActivity.this, SPINNER_DATA[position], Toast.LENGTH_SHORT).show();
                Select=Datas.get(position);
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("다른 날짜 학식 매뉴");
        builder.setMessage("날짜를 선택해주");
        builder.setView(materialBetterSpinner);
        builder.setPositiveButton("선택",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                today = dataSnapshot.child(Select).getValue(Menu_List.class);

                                Today.setText(today.getData());
                                arraylist=getmenu();
                                Adapter = new CustomAdapter(arraylist,MainActivity.this);

                                //Adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, arraylist);
                                list.setAdapter(Adapter);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                });
        builder.setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.show();
    }



}
