package mahadevan.siva.gayathriapp.activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import mahadevan.siva.gayathriapp.R;
import mahadevan.siva.gayathriapp.adapters.RecipeListAdapter;
import mahadevan.siva.gayathriapp.database.ApplicationDatabaseHelper;
import mahadevan.siva.gayathriapp.models.Ingredient;
import mahadevan.siva.gayathriapp.models.Recipe;

public class RecipeListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        recyclerView = (RecyclerView) findViewById(R.id.recipe_recycler_view);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        Intent intent = getIntent();
        Ingredient ingredient = intent.getParcelableExtra("ingredient");
        List<Recipe> recipes = prepareAdapterData(ingredient.getId());
        adapter = new RecipeListAdapter(this, recipes);
        recyclerView.setAdapter(adapter);
    }

    private List<Recipe> prepareAdapterData (int ingredientId) {
        List<Recipe> recipes = new ArrayList<>();
        ApplicationDatabaseHelper databaseHelper = new ApplicationDatabaseHelper(this);
        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        Cursor cursor = database.rawQuery(
                "SELECT DISTINCT r.rowid, r.* FROM recipes AS r " +
                "JOIN recipe_ingredients AS ri ON ri.recipe_id = r.rowid " +
                "WHERE ri.ingredient_id = ?", new String[]{String.valueOf(ingredientId)});

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            recipes.add(new Recipe(
                    cursor.getInt(cursor.getColumnIndexOrThrow("rowid")),
                    cursor.getString(cursor.getColumnIndexOrThrow("name")),
                    cursor.getString(cursor.getColumnIndexOrThrow("avatar")),
                    cursor.getString(cursor.getColumnIndexOrThrow("description")),
                    cursor.getString(cursor.getColumnIndexOrThrow("prep_time"))
            ));
            cursor.moveToNext();
        }
        cursor.close();
        return recipes;
    }
}
