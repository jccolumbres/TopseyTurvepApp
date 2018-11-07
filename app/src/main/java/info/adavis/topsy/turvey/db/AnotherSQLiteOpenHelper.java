package info.adavis.topsy.turvey.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import info.adavis.topsy.turvey.models.Recipe;

public class AnotherSQLiteOpenHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "sample_db.db";
    private static final int VERSION_NUMBER = 1;
    public AnotherSQLiteOpenHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(RecipeContract.CREATE_RECIPE_ENTRY_TABLE);
        sqLiteDatabase.execSQL(RecipeContract.CREATE_RECIPE_STEP_ENTRY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + RecipeContract.RecipeEntry.TABLE_NAME);
    }
}
