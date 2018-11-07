package info.adavis.topsy.turvey.features.recipes;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import java.util.List;

import info.adavis.topsy.turvey.R;
import info.adavis.topsy.turvey.db.MainDatasource;
import info.adavis.topsy.turvey.db.RecipesDataProvider;
import info.adavis.topsy.turvey.db.TopsyTurveyDataSource;
import info.adavis.topsy.turvey.models.Recipe;

public class RecipesActivity extends AppCompatActivity
{
    private static final String TAG = RecipesActivity.class.getSimpleName();

    private RecyclerView recipesRecyclerView;
    private RecipesAdapter adapter;

    private MainDatasource datasource;

    @Override

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recipesRecyclerView = (RecyclerView) findViewById(R.id.recipes_recycler_view);
        datasource = new MainDatasource(this);
        setupRecyclerView();
    }

    @Override
    protected void onResume ()
    {

        super.onResume();
        datasource.open();

        for (Recipe recipe : RecipesDataProvider.recipesList) {
            datasource.createRecipe(recipe);
        }

        //display data to recyclerView via adapter
        adapter.setRecipes(datasource.getListOfRecipes());
        List<Recipe> allRecipes = getRecipes();
        Recipe updatedRecipe = allRecipes.get(0);
        updatedRecipe.setName("Yellow dic");
        updatedRecipe.setDescription("Dib d");

        datasource.updateRecipe(updatedRecipe);
        getRecipes();
    }

    private List<Recipe> getRecipes(){
        List<Recipe> allRecipes = datasource.getListOfRecipes();
        for (Recipe recipe : allRecipes) {
            Log.i(TAG,"The recipe: " + recipe);
        }
        adapter.setRecipes(allRecipes);
        return allRecipes;
    }

    @Override
    protected void onPause ()
    {
        super.onPause();
        datasource.close();
    }

    private void setupRecyclerView ()
    {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recipesRecyclerView.setLayoutManager(layoutManager);

        recipesRecyclerView.setHasFixedSize(true);

        adapter = new RecipesAdapter( this );
        recipesRecyclerView.setAdapter( adapter );
    }

}
