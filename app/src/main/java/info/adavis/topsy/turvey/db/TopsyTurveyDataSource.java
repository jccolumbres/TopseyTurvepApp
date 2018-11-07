package info.adavis.topsy.turvey.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import info.adavis.topsy.turvey.models.Recipe;
import info.adavis.topsy.turvey.models.RecipeStep;

public class TopsyTurveyDataSource {
    private static final String TAG = TopsyTurveyDataSource.class.getSimpleName();

    private static SQLiteDatabase database;
    private static DatabaseSQLiteOpenHelper dbHelper;

    public TopsyTurveyDataSource(Context context){
        dbHelper = new DatabaseSQLiteOpenHelper(context);
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

        Log.d(TAG,"Inserted row id: " + rowId);

        List<RecipeStep> steps = recipe.getSteps();
        if (steps != null && steps.size() > 0){
            for (RecipeStep step : steps) {
                createRecipeSteps(step,rowId);
            }
        }
    }

    public void createRecipeSteps(RecipeStep recipeStep, long recipeId){
        ContentValues values = new ContentValues();
        values.put(RecipeContract.RecipeStepEntry.COLUMN_RECIPE_ID,recipeId);
        values.put(RecipeContract.RecipeStepEntry.COLUMN_INSTRUCTION,recipeStep.getInstruction());
        values.put(RecipeContract.RecipeStepEntry.COLUMN_STEP_NUMBER,recipeStep.getStepNumber());
        long rowId = database.insert(RecipeContract.RecipeStepEntry.TABLE_NAME,null,values);

        Log.d(TAG,"Inserted row step id: " + rowId);
    }

    /*public List<Recipe> getAllRecipes(){

        // This will store your query results later
        List<Recipe> recipes = new ArrayList<>();

        //You sql command to fetch records
        String selectQuery = "SELECT * FROM recipe" ;

        //returns query result
            //cursor objects start at an index right before your first record
            //rawQuery also accepts selectionArgs if the sql commands contains a WHERE clause in this case there's none
        Cursor cursor = database.rawQuery(selectQuery,null);
        try {
            //start going through your query results
            while (cursor.moveToNext()){
                //Get columns from Contract class
                Recipe recipe = new Recipe(
                        cursor.getString(cursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_NAME)),
                        cursor.getString(cursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_DESCRIPTION)),
                        cursor.getInt(cursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_IMAGE_RESOURCE_ID)));
                //don't forget to add the id
                recipe.setId(cursor.getLong(cursor.getColumnIndex(RecipeContract.RecipeEntry._ID)));

                //add the new object the the List data type from earlier
                recipes.add(recipe);
            }
        }finally {
            //close the cursor after use
            if (cursor!=null && !cursor.isClosed())
            {
                cursor.close();
            }
        }
        //return list object for use
        return recipes;
    }
    */

    public List<Recipe> getObjects(){
        List<Recipe> recipes = new ArrayList<>();

        String selectAllQuery = "SELECT * FROM recipe";
        Cursor cursor = database.rawQuery(selectAllQuery,null);
        try{
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
        return  recipes;
    }
}
