package mahadevan.siva.gayathriapp.activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import mahadevan.siva.gayathriapp.R;
import mahadevan.siva.gayathriapp.adapters.IngredientListAdapter;
import mahadevan.siva.gayathriapp.database.ApplicationDatabaseHelper;
import mahadevan.siva.gayathriapp.models.Ingredient;

import static mahadevan.siva.gayathriapp.database.ApplicationDatabaseContract.IngredientEntry;

public class IngredientListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient_list);

        recyclerView = (RecyclerView) findViewById(R.id.ingredient_recycler_view);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        Intent intent = getIntent();
        int foodGroupId = intent.getIntExtra("food_group_id", 0);
        List<Ingredient> ingredients = prepareAdapterData(foodGroupId);
        adapter = new IngredientListAdapter(this, ingredients);
        recyclerView.setAdapter(adapter);
    }

    private List<Ingredient> prepareAdapterData (int foodGroupId) {
        List<Ingredient> ingredients = new ArrayList<>();
        ApplicationDatabaseHelper databaseHelper = new ApplicationDatabaseHelper(this);
        SQLiteDatabase database = databaseHelper.getReadableDatabase();

        String[] projection = {
                IngredientEntry.COLUMN_NAME_ROWID,
                IngredientEntry.COLUMN_NAME_NAME,
                IngredientEntry.COLUMN_NAME_BARCODE,
                IngredientEntry.COLUMN_NAME_FOOD_GROUP_ID,
                IngredientEntry.COLUMN_NAME_AVATAR
        };
        String selection = IngredientEntry.COLUMN_NAME_FOOD_GROUP_ID + " = ?";
        String[] selectionArgs = { String.valueOf(foodGroupId) };
        String sortOrder = IngredientEntry.COLUMN_NAME_NAME + " ASC";

        Cursor cursor = database.query(IngredientEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            ingredients.add(new Ingredient(
                    cursor.getInt(cursor.getColumnIndexOrThrow(IngredientEntry.COLUMN_NAME_ROWID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(IngredientEntry.COLUMN_NAME_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(IngredientEntry.COLUMN_NAME_BARCODE)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(IngredientEntry.COLUMN_NAME_FOOD_GROUP_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(IngredientEntry.COLUMN_NAME_AVATAR))
            ));
            cursor.moveToNext();
        }
        cursor.close();
        return ingredients;
    }
}
