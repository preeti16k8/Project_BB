package com.kre8tives.bareboneneww.Util;

import java.util.Calendar;

/**
 * Created by Android-2 on 5/8/2017.
 */

public class AgeCalculation {
    private int startYear;
    private int startMonth;
    private int startDay;
    private int endYear;
    private int endMonth;
    private int endDay;
    private int resYear;
    private int resMonth;
    private int resDay;
    public String getCurrentDate()
    {
        Calendar c=Calendar.getInstance();
        endYear=c.get(Calendar.YEAR);
        endMonth=c.get(Calendar.MONTH);
        endMonth++;
        endDay=c.get(Calendar.DAY_OF_MONTH);
        return endDay+":"+endMonth+":"+endYear;
    }
    public void setDateOfBirth(int sYear, int sMonth, int sDay)
    {
        startYear=sYear;
        startMonth=sMonth;
        startMonth++;
        startDay=sDay;

    }
    public void calcualteYear()
    {
        resYear=endYear-startYear;

    }

    public void calcualteMonth()
    {
        if(endMonth>=startMonth)
        {
            resMonth= endMonth-startMonth;
        }
        else
        {
            resMonth=endMonth-startMonth;
            resMonth=12+resMonth;
            resYear--;
        }

    }
    public void calcualteDay()
    {

        if(endDay>=startDay)
        {
            resDay= endDay-startDay;
        }
        else
        {
            resDay=endDay-startDay;
            resDay=30+resDay;
//resMonth--;
        }
    }

    public String getResult()
    {
        //return resDay+":"+resMonth+":"+resYear;
        return ""+resYear;
    }


}
