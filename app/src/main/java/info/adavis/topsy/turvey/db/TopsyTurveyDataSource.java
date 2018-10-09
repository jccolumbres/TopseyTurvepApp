package info.adavis.topsy.turvey.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class TopsyTurveyDataSource {
    private static final String TAG = TopsyTurveyDataSource.class.getSimpleName();

    private SQLiteDatabase database;
    private DatabaseSQLiteOpenHelper dbHelper;

    public TopsyTurveyDataSource(Context context)
    {
        this.dbHelper = new DatabaseSQLiteOpenHelper(context);

    }

    public void open(){
        this.database = dbHelper.getWritableDatabase();
        Log.d(TAG, "DB is opened");
    }

    public void close(){
        dbHelper.close();
        Log.d(TAG, "DB is closed");
    }
}
