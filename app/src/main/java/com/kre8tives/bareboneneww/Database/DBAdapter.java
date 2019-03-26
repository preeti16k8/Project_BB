package com.kre8tives.bareboneneww.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import static com.kre8tives.bareboneneww.Database.Constants.ROW_ID;
import static com.kre8tives.bareboneneww.Database.Constants.TB_NAME;

/**
 * Created by user on 5/6/2017.
 */

public class DBAdapter {
    Context c;
    SQLiteDatabase db;
    DBHelper helper;
    public DBAdapter(Context c) {
        this.c = c;
        helper=new DBHelper(c);
    }
    //OPEN DATABASE
    public DBAdapter openDB()
    {
        try {
            db=helper.getWritableDatabase();
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
        return this;
    }
    //CLOSE DATABASE
    public void closeDB()
    {
        try {
            helper.close();
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
    //INSERT
    public long add(String newid,String name,String quantity,String price,String newprice)
    {
        try
        {
            ContentValues cv=new ContentValues();
            cv.put(Constants.NEWID,newid);
            cv.put(Constants.NAME,name);
            //cv.put(Constants.POSITION, pos);
            cv.put(Constants.Quantity,quantity);
            cv.put(Constants.Price,price);
            cv.put(Constants.NewPrice,newprice);

            return db.insert(TB_NAME, ROW_ID,cv);
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
        return 0;
    }
    //RETRIEVE
    public Cursor getAllPlayers()
    {
        String[] columns={ROW_ID,Constants.NEWID, Constants.NAME, Constants.Quantity, Constants.Price,Constants.NewPrice};
        return db.query(TB_NAME,columns,null,null,null,null,null);
    }


    public void deleteitems()
    {
        SQLiteDatabase db = helper.getWritableDatabase();
        //db.delete(Constants.TB_NAME,columns,null,null,null,null,null);
        db.delete(TB_NAME,null,null);
        db.close();
    }

    public boolean delete(int id)
    {
        try
        {
            int result=db.delete(TB_NAME, ROW_ID+" =?",new String[]{String.valueOf(id)});
            if(result>0)
            {
                return true;
            }
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
        return false;
    }








}