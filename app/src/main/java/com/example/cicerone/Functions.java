package com.example.cicerone;

import android.widget.TextView;

import java.util.Date;

public abstract class Functions {

    public static boolean checkData(int anno,int mese,int giorno){
        boolean res = true;
        Date date = new Date();
        int g=date.getDate();
        int m=date.getMonth()+1;
        int a=date.getYear()+1900;

        if(anno<a)
            res=false;
        else if(anno==a){
            if(mese<m)
                res=false;
            else
            if(mese==m&&giorno<g)
                res=false;
        }
        return res;
    }
}
