package com.kre8tives.bareboneneww.Database;

/**
 * Created by user on 5/6/2017.
 */

public class Constants {
    //COLUMNS
    static final String ROW_ID="id";
    static final String NAME = "name";
    static final String NEWID = "newid";
    static final String Quantity="quantity";
    static final String Price="price";
    static final String NewPrice="newprice";
    static final String POSITION = "position";
    //DB PROPERTIES
    static final String DB_NAME="db_Database";
    static final String TB_NAME="db_Table";
    static final int DB_VERSION='3';

    static final String CREATE_TB="CREATE TABLE db_Table(id INTEGER PRIMARY KEY AUTOINCREMENT," + "newid TEXT NOT NULL," + "name TEXT NOT NULL," + "quantity TEXT NOT NULL," + "price TEXT NOT NULL," + "newprice TEXT NOT NULL);";
}