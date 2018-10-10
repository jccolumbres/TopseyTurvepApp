package info.adavis.topsy.turvey.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

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
}
