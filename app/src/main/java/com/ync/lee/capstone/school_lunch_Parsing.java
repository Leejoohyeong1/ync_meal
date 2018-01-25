package com.ync.lee.capstone;


import android.os.AsyncTask;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by lee on 2017. 10. 31..
 */

public class school_lunch_Parsing extends AsyncTask<ArrayList<Menu_List>,Void,ArrayList<Menu_List>>{

    ArrayList<Menu_List> menu;

    @Override //스레드 작업
    protected ArrayList<Menu_List> doInBackground(ArrayList<Menu_List>... params) {

    try {
        Menu_List list;

        Document html = Jsoup.connect("http://www.ync.ac.kr/kor/CMS/Board/Board.do?mCode=MN217").get();
        Element element = html.select(".stitle a").first();
        String url = element.attr("href");
        url="http://www.ync.ac.kr/kor/CMS/Board/Board.do"+url;

        html = Jsoup.connect( url).get();
        element = html.select(".board-view-filelist").first();

        String File = element.select("a").attr("href");
        File="http://www.ync.ac.kr"+File;
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(File).build();
        Response response = client.newCall(request).execute();

        System.out.println(File);
        InputStream fis =  response.body().byteStream();
        HSSFWorkbook workbook=new HSSFWorkbook(fis);
        int rowindex=0;
        int columnindex=0;
        //시트 선택
        HSSFSheet sheet = workbook.getSheetAt(0);
        //행선택
        HSSFRow row=null;
        menu = new ArrayList<Menu_List>();
        for (int j=1;j<=5;j++){
            list = new Menu_List();
            for(int i =2;i<=28;i++) {
                row = sheet.getRow(i);
                HSSFCell cell = row.getCell(j);
                String value = "";
                value = cell.getStringCellValue() + "";
                list.list_add(i,value);

                if(i==28){
                    row = sheet.getRow(1);
                    cell = row.getCell(j);
                    Calendar oCalendar = Calendar.getInstance( );
                    list.setData(oCalendar.get(Calendar.YEAR)+"년 "+cell.getStringCellValue());
                    menu.add(list);
                }

            }

        }
        return menu;
    }catch (Exception e ){

    }
        return menu;
    }


}
