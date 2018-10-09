package info.adavis.topsy.turvey.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class DatabaseSQLiteOpenHelper extends SQLiteOpenHelper {
    //Don't forget to extend to SQLiteHelper module
    //In this SQLite Helper has two variable db name and version number
        //also has two Override methods - OnCreate and OnUpgrae
    //Has a constructor - should be modified to only accept context
    private static final String DATABASE_NAME = "topsy_turvy.db";
    private static final int VERSION_NUMBER = 1;
    public DatabaseSQLiteOpenHelper(@Nullable Context context) {
        //replaced arguments with variable then optimize constructor parameters
        super(context, DATABASE_NAME, null, VERSION_NUMBER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //these lines execute the commands located in your contract class
        db.execSQL(RecipeContract.CREATE_RECIPE_ENTRY_TABLE);
        db.execSQL(RecipeContract.CREATE_RECIPE_STEP_ENTRY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //sql commands that deletes tables and recreates it again
        db.execSQL("DROP TABLE IF EXISTS " + RecipeContract.RecipeEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + RecipeContract.RecipeStepEntry.TABLE_NAME);

        //recreation happens here
        onCreate(db);
    }
}
