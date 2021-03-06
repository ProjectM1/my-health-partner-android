package com.serli.myhealthpartner.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;

import java.util.Date;

/**
 * Created by kahina on 01/02/2017.
 */

public class PodometreDAO {

    private Database database;
    private SQLiteDatabase db;
    private String[] allColumns = { database.PODOMETRE_TSTMP, database.PODOMETRE_DURATION, database.PODOMETRE_STEPS,
                                    database.PODOMETRE_CALORIES, database.PODOMETRE_DISTANCE, database.PODOMETRE_ACTIVITY};

    /**
     *
     * @param context The context where the dao is called
     */

    public PodometreDAO(Context context){
        this.database = new Database(context);

    }

    /**
     * Open The connection with the database
     */

    public void open(){
        db = database.getWritableDatabase();
    }

    /**
     * Close the connection with the database
     */
    public void close(){
        database.close();
    }

    /**
     * Add an entry in the profile table
     * As only one profile is needed in the application, the table will be empty before the insertion
     *
     * @param data the data object containing the podometre information
     */

    public void addEntry(PodometreData data){
        if (data.getSteps() !=0 && data.getTstmp()!=0){
            db.delete(Database.PODOMETRE_TABLE, null,null);

            ContentValues values = new ContentValues();
            values.put(Database.PODOMETRE_TSTMP, data.getTstmp());
            values.put(Database.PODOMETRE_DURATION, data.getDuration());
            values.put(Database.PODOMETRE_STEPS, data.getSteps());
            values.put(Database.PODOMETRE_CALORIES, data.getCalories());
            values.put(Database.PODOMETRE_DISTANCE, data.getDistance());
            values.put(Database.PODOMETRE_ACTIVITY, data.getActivity());


            db.insert(Database.PODOMETRE_TABLE, null, values);

        }
    }

    /**
     * Return object containing the podometre stored
     *
     * @return the podometre
     */
    public PodometreData getPodometre(){
        Cursor c = db.query(Database.PODOMETRE_TABLE, allColumns, null, null, null, null, null);
        c.moveToFirst();
        return cursorToData(c);
    }


    private PodometreData cursorToData (Cursor cursor){
        PodometreData podoData = null;
        if(!cursor.isAfterLast()){
            podoData = new PodometreData();
            podoData.setTstmp(cursor.getInt(0));
            podoData.setDuration(cursor.getLong(1));
            podoData.setSteps(cursor.getInt(2));
            podoData.setCalories(cursor.getInt(3));
            podoData.setDistance(cursor.getInt(4));
            podoData.setActivity(cursor.getInt(5));
        }
        return podoData;
    }

}

