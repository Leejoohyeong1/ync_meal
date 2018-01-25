package com.ync.lee.capstone;

/**
 * Created by lee on 2017. 9. 30..
 */


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;

class Menu_List implements Serializable,Comparator<String>{

    String Data;
    ArrayList<String> morning = new ArrayList<String>();
    ArrayList<String> morning_sub = new ArrayList<String>();
    ArrayList<String> staff = new ArrayList<String>();
    ArrayList<String> student = new ArrayList<String>();;
    ArrayList<String> dinner = new ArrayList<String>();;

    public void list_add(int index , String menu){

        if (menu.equals("")||menu.equals("*"))
            return;

        if((2<=index)&&(index<=8)){
            this.morning.add(menu);
        }else if((9<=index)&&(index<=11)){
            this.morning_sub.add(menu);
        }else if((12<=index)&&(index<=18)){
            this.staff.add(menu);
        }else if((19<=index)&&(index<=22)){
            this.student.add(menu);
        }else if((23<=index)&&(index<=28)){
            this.dinner.add(menu);
        }

    }




    public ArrayList<String> getMorning() {
        return morning;
    }

    public void setMorning(ArrayList<String> morning) {
        this.morning = morning;
    }

    public ArrayList<String> getMorning_sub() {
        return morning_sub;
    }

    public void setMorning_sub(ArrayList<String> morning_sub) {
        this.morning_sub = morning_sub;
    }

    public ArrayList<String> getStaff() {
        return staff;
    }

    public void setStaff(ArrayList<String> staff) {
        this.staff = staff;
    }

    public ArrayList<String> getStudent() {
        return student;
    }

    public void setStudent(ArrayList<String> student) {
        this.student = student;
    }

    public ArrayList<String> getDinner() {
        return dinner;
    }

    public void setDinner(ArrayList<String> dinner) {
        this.dinner = dinner;
    }

    public String getData() {
        return Data;
    }

    public void setData(String data) {

        Data = data;
    }

    public String getKeyValue(){

        String data= getData();


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


        return year+"-"+month+"-"+day;
    }


    @Override
    public int compare(String o1, String o2) {
        return o2.compareTo(o1);
    }
}


