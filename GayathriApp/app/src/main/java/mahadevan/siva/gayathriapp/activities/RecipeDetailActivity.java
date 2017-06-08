package mahadevan.siva.gayathriapp.activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.koushikdutta.ion.Ion;

import mahadevan.siva.gayathriapp.R;
import mahadevan.siva.gayathriapp.database.ApplicationDatabaseHelper;
import mahadevan.siva.gayathriapp.models.Recipe;

public class RecipeDetailActivity extends AppCompatActivity {

    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        database = new ApplicationDatabaseHelper(this).getReadableDatabase();

        Intent intent = getIntent();
        Recipe recipe = (Recipe) intent.getParcelableExtra("recipe");
        ImageView toolbarImage = (ImageView) findViewById(R.id.recipe_detail_toolbar_image);
        Ion.with(toolbarImage).load(recipe.getAvatar());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setTitle(recipe.getName());
        setSupportActionBar(toolbar);

        TextView description = (TextView) findViewById(R.id.recipe_detail_description);
        description.setText(recipe.getDescription());

        TextView ingredientsList = (TextView) findViewById(R.id.recipe_detail_ingredients_list);
        ingredientsList.setText(Html.fromHtml(getIngredientsHtmlString(recipe.getId())));

        TextView directionsList = (TextView) findViewById(R.id.recipe_detail_directions_list);
        directionsList.setText(Html.fromHtml(getDirectionsHtmlString(recipe.getId())));
    }

    private String getIngredientsHtmlString (int recipeId) {
        StringBuilder builder = new StringBuilder();

        Cursor cursor = database.rawQuery(
                "SELECT description FROM recipe_ingredients WHERE recipe_id = ?",
                new String[]{String.valueOf(recipeId)}
        );

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            builder.append(String.format("&#8226; %s<br/>", cursor.getString(cursor.getColumnIndex("description"))));
            cursor.moveToNext();
        }
        cursor.close();
        return builder.toString();
    }

    private String getDirectionsHtmlString (int recipeId) {
        StringBuilder builder = new StringBuilder();

        Cursor cursor = database.rawQuery(
                "SELECT description FROM recipe_directions WHERE recipe_id = ?",
                new String[]{String.valueOf(recipeId)}
        );

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            builder.append(String.format("&#8226; %s<br/>", cursor.getString(cursor.getColumnIndex("description"))));
            cursor.moveToNext();
        }
        cursor.close();
        return builder.toString();
    }
}
