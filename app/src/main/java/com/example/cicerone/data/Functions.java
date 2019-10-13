package com.example.cicerone.data;

import java.util.Date;

public abstract class Functions {

    private static int C=48;

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
            if(mese==m&&giorno<=g)
                res=false;
        }
        return res;
    }

    public static Integer[] parseData(String data){
        char[] c = data.toCharArray();
        int giorno=c[0]-C;
        int mese=0;
        int anno=0;
        int i=1;

        if(c[i]!='/') {
            giorno = giorno * 10 + (c[i] - C);
            i++;
        }

        i++;
        mese=c[i]-C;
        i++;

        if(c[i]!='/'){
            mese = mese * 10 + (c[i] - C);
            i++;
        }
        i++;

        for(int j=3;j>=0;j--){
            anno+=(c[i]-C)*Math.pow(10,j);
            i++;
        }


        Integer[] res = new Integer[3];
        res[0]=giorno;
        res[1]=mese;
        res[2]=anno;

        return res;
    }
}
