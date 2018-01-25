package com.ync.lee.capstone;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

import static android.content.Intent.FLAG_ACTIVITY_NO_HISTORY;

public class LodingActivity extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Data");
    ArrayList<Menu_List> armItem = new ArrayList<Menu_List>();

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        boolean first = pref.getBoolean("isFirst", false);
        if(first==false){
            Log.d("Is first Time?", "first");
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("isFirst",true);
            editor.commit();
            //앱 최초 실행시 하고 싶은 작업
            Intent intent=new Intent(LodingActivity.this,CheckActivity.class);
            intent.addFlags(FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
        }else{
            myRef.child(GatTodayData()).addListenerForSingleValueEvent(postListener);
        }
    }
    //==================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loding);
    }
    //==================================
    ValueEventListener postListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            // Get Post object and use the values to update the UI

            Menu_List today = new Menu_List();
            try{
                today  = dataSnapshot.getValue(Menu_List.class);
                Log.d("todayM",today.getData());
            }catch (NullPointerException e){
                today=FirebaseInsert();
            }finally {
                Intent intent = new Intent(LodingActivity.this,MainActivity.class);
                intent.putExtra("today",today);
                startActivity(intent);
            }

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {}
    };
    //==================================

    public String GatTodayData_t(){
        String Data="";
        String[] Data_M={"일요일","월요일","화요일","수요일","목요일","금요일","토요일"};
        Calendar oCalendar = Calendar.getInstance( );
        int week = oCalendar.get (Calendar.DAY_OF_WEEK);
        int time = oCalendar.get(Calendar.HOUR_OF_DAY);

        if(0<=time&&time<19){
            //오늘
            Data = oCalendar.get(Calendar.YEAR)+"년 "+(oCalendar.get(Calendar.MONTH) + 1)+"월 "+oCalendar.get(Calendar.DAY_OF_MONTH)+"일 "+Data_M[week-1];

        }else{
            //내일 아침
            oCalendar.add(Calendar.DATE, 1);
            week = oCalendar.get (Calendar.DAY_OF_WEEK);
            Data = oCalendar.get(Calendar.YEAR)+"년 "+(oCalendar.get(Calendar.MONTH) + 1)+"월 "+oCalendar.get(Calendar.DAY_OF_MONTH)+"일 "+Data_M[week-1];
        }

        return Data;
    }
    public String GatTodayData(){

        String Data="";
        String[] Data_M={"일요일","월요일","화요일","수요일","목요일","금요일","토요일"};
        Calendar oCalendar = Calendar.getInstance( );
        int week = oCalendar.get (Calendar.DAY_OF_WEEK);
        int time = oCalendar.get(Calendar.HOUR_OF_DAY);

        if(0<=time&&time<19){
            //오늘
            Data = oCalendar.get(Calendar.YEAR)+"년 "+(oCalendar.get(Calendar.MONTH) + 1)+"월 "+oCalendar.get(Calendar.DAY_OF_MONTH)+"일 "+Data_M[week-1];

        }else{
            //내일 아침
            oCalendar.add(Calendar.DATE, 1);
            week = oCalendar.get (Calendar.DAY_OF_WEEK);
            Data = oCalendar.get(Calendar.YEAR)+"년 "+(oCalendar.get(Calendar.MONTH) + 1)+"월 "+oCalendar.get(Calendar.DAY_OF_MONTH)+"일 "+Data_M[week-1];
        }



        int start=2 ,end=0;

        end =  Data.indexOf("년");

        String year = Data.substring(2,end);
        start =end+1;
        end =  Data.indexOf("월");
        String month = Data.substring(start,end);
        month=month.replaceAll(" ", "");
        if(month.length()==1){
            month="0"+month;
        }

        start =end+1;
        end =  Data.indexOf("일");
        String day = Data.substring(start,end);
        day=day.replaceAll(" ", "");

        if(day.length()==1){
            day="0"+day;
        }
        System.out.println(year+"-"+month+"-"+day);




        return  year+"-"+month+"-"+day;
    }
    //==================================
    public Menu_List FirebaseInsert(){

        school_lunch_Parsing slp = new school_lunch_Parsing();

        try {
            armItem = slp.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        Menu_List rm = new Menu_List();

        for(Menu_List m :armItem){

            myRef.child(m.getKeyValue()).setValue(m);
            if(m.getData().equals(GatTodayData_t())){
                rm =m;
            }
        }
        return rm;
    }
    //==================================
}
