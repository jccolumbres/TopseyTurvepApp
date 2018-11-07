package info.adavis.topsy.turvey.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import info.adavis.topsy.turvey.models.Recipe;
import info.adavis.topsy.turvey.models.RecipeStep;

public class MainDatasource {
    private static final String TAG = MainDatasource.class.getSimpleName();

    private static SQLiteDatabase database;
    private static SQLiteOpenHelper dbHelper;

    public MainDatasource(Context context){
        dbHelper = new AnotherSQLiteOpenHelper(context);
    }

    public void open(){
        this.database = dbHelper.getWritableDatabase();
        Log.d(TAG,"DB is opened");
    }

    public void close(){
        dbHelper.close();
        Log.d(TAG,"DB is closed");
    }

    public void createRecipe(Recipe recipe){
        ContentValues values = new ContentValues();
        values.put(RecipeContract.RecipeEntry.COLUMN_NAME,recipe.getName());
        values.put(RecipeContract.RecipeEntry.COLUMN_DESCRIPTION,recipe.getDescription());
        values.put(RecipeContract.RecipeEntry.COLUMN_IMAGE_RESOURCE_ID,recipe.getImageResourceId());

        long rowId = database.insert(RecipeContract.RecipeEntry.TABLE_NAME,null,values);

        Log.d(TAG,"Recipe ID #" + rowId);
        List<RecipeStep> steps = recipe.getSteps();
        if (steps != null && steps.size() != 0 ){
            for (RecipeStep step : steps) {
                createRecipeSteps(step,rowId);
            }
        }

    }

    public void createRecipeSteps(RecipeStep step, long rowId){
        ContentValues values = new ContentValues();
        values.put(RecipeContract.RecipeStepEntry.COLUMN_RECIPE_ID,rowId);
        values.put(RecipeContract.RecipeStepEntry.COLUMN_INSTRUCTION,step.getInstruction());
        values.put(RecipeContract.RecipeStepEntry.COLUMN_STEP_NUMBER,step.getStepNumber());

        long rowStepId = database.insert(RecipeContract.RecipeStepEntry.TABLE_NAME,null,values);


        Log.d(TAG,"Recipe step ID #" + rowStepId);

    }

    public List<Recipe> getListOfRecipes(){
        List<Recipe> recipes = new ArrayList<>();

        String selectAllQuery = "SELECT * FROM recipe";
        Cursor cursor = database.rawQuery(selectAllQuery,null);
        try {
            while (cursor.moveToNext()){
                Recipe recipe = new Recipe(
                        cursor.getString(cursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_NAME)),
                        cursor.getString(cursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_DESCRIPTION)),
                        cursor.getInt(cursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_IMAGE_RESOURCE_ID)));
                recipe.setId(cursor.getLong(cursor.getColumnIndex(RecipeContract.RecipeEntry._ID)));

                recipes.add(recipe);
            }
        }finally {
            if (cursor!=null && !cursor.isClosed()){
                cursor.close();
            }
        }
        return recipes;
    }
    public void updateRecipe(Recipe recipe){
        ContentValues values = new ContentValues();
        values.put(RecipeContract.RecipeEntry.COLUMN_NAME,recipe.getName());
        values.put(RecipeContract.RecipeEntry.COLUMN_DESCRIPTION,recipe.getDescription());
        values.put(RecipeContract.RecipeEntry.COLUMN_IMAGE_RESOURCE_ID,recipe.getImageResourceId());

        String selection = RecipeContract.RecipeEntry._ID +  " = ?";
        String[] selectionArgs = { String.valueOf(recipe.getId())};

        int count = database.update(RecipeContract.RecipeEntry.TABLE_NAME,values,selection,selectionArgs);
        Log.d(TAG,"Number of records updated: " + count);
    }


}
