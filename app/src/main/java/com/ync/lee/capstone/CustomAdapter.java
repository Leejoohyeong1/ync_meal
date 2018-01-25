package com.ync.lee.capstone;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by lee on 2017. 11. 28..
 */

public class CustomAdapter extends BaseAdapter{





    ArrayList<String> list = new ArrayList<String>();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Thumb");
    Context context;
    String Thumbbad,Thumbgood;




    CustomAdapter(ArrayList<String> list,Context context){
        this.list=list;
        this.context=context;
    }

    @Override
    public int getCount() {
        return this.list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ImageView[] image = new ImageView[2];
        final TextView[] var = new TextView[2];
        if(convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item,null);
        }

        final View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.good:
                        SharedPreferences prefs = context.getSharedPreferences("Thumb", MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();

                        int check =prefs.getInt(list.get(position), 0);
                        if(check<0){
                            Toast.makeText(context, "2개 이상 선택할수 없습니다", Toast.LENGTH_SHORT).show();
                            break;
                        }
                        check++;
                        int good =Integer.parseInt(var[0].getText().toString());

                        if (check>=2){
                            check=0;
                            editor.putInt(list.get(position),0);
                            editor.commit();
                            myRef.child(list.get(position)).child("good").setValue(good-1);
                            //Toast.makeText(context, list.get(position)+"UP 취소", Toast.LENGTH_SHORT).show();
                            image[0].setImageResource(R.mipmap.good);
                        }else if(check>=0){
                            editor.putInt(list.get(position),check);
                            editor.commit();
                            image[0].setImageResource(R.mipmap.b_good);
                            Log.d("onClickListener0" ,var[0].getText().toString());
                            myRef.child(list.get(position)).child("good").setValue(good+ 1);
                            //Toast.makeText(context, list.get(position)+"UP", Toast.LENGTH_SHORT).show();
                        }
                        Log.e("plag",Integer.toString(check));



                        break;
                    case R.id.bad:
                        SharedPreferences pref = context.getSharedPreferences("Thumb", MODE_PRIVATE);
                        SharedPreferences.Editor editor1 = pref.edit();
                        int check1 =pref.getInt(list.get(position), 0);
                        if(check1>0){
                            Toast.makeText(context, "2개 이상 선택할수 없습니다", Toast.LENGTH_SHORT).show();
                            break;
                        }
                        check1--;
                        int bad =Integer.parseInt(var[1].getText().toString());

                        if(check1<=-2){
                            check1=0;
                            editor1.putInt(list.get(position),0);
                            editor1.commit();
                            Log.d("onClickListener1",var[1].getText().toString());
                            myRef.child(list.get(position)).child("bad").setValue( bad-1 );
                            //Toast.makeText(context, list.get(position)+"Down 취소", Toast.LENGTH_SHORT).show();
                            image[1].setImageResource(R.mipmap.bad);
                        }else if(check1<=0){
                            editor1.putInt(list.get(position),check1);
                            editor1.commit();
                            Log.d("onClickListener0" ,var[0].getText().toString());
                            myRef.child(list.get(position)).child("bad").setValue(bad+ 1);
                            //Toast.makeText(context, list.get(position)+"Down", Toast.LENGTH_SHORT).show();
                            image[1].setImageResource(R.mipmap.b_bad);

                        }

                        Log.e("plag",Integer.toString(check1));

                        break;
                }
            }
        };


        TextView menu = convertView.findViewById(R.id.menu);
        menu.setText(list.get(position));

        image[0] = convertView.findViewById(R.id.good);
        image[0] .setOnClickListener(onClickListener);


        image[1]  = convertView.findViewById(R.id.bad);
        image[1] .setOnClickListener(onClickListener);




            final View finalConvertView = convertView;
            myRef.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    CustomItme itme;
                    try{

                        SharedPreferences prefs = context.getSharedPreferences("Thumb", MODE_PRIVATE);
                        int check =prefs.getInt(list.get(position), 0);

                        if(check>=1){
                            image[0].setImageResource(R.mipmap.b_good);
                        }else if (check<=-1){
                            image[1].setImageResource(R.mipmap.b_bad);

                        }

                        itme = dataSnapshot.child(list.get(position)).getValue(CustomItme.class);

                        Thumbgood =Integer.toString(itme.getGood());

                        Thumbbad =Integer.toString(itme.getBad());


                       var[0] = finalConvertView.findViewById(R.id.goodbnt_var);
                        var[1]= finalConvertView.findViewById(R.id.badbnt_var);
                        var[0].setText(Thumbgood);
                        var[1].setText(Thumbbad);
                        if(list.get(position).equals("★복수메뉴★")){
                            var[0].setVisibility(View.INVISIBLE);
                            var[1].setVisibility(View.INVISIBLE);
                            image[0].setVisibility(View.INVISIBLE);
                            image[1].setVisibility(View.INVISIBLE);
                        }
                    }catch (NullPointerException e) {
                        SharedPreferences prefs = context.getSharedPreferences("Thumb", MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putInt(list.get(position), 0);
                        editor.commit();

                        itme = new CustomItme(list.get(position));
                        itme.setMenu(list.get(position));
                        myRef.child(list.get(position)).setValue(itme);
                    }finally {

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d("CustomAdapter","씨발4");
                }
            });







        return convertView;
    }







}
