package mahadevan.siva.gayathriapp.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.List;

import mahadevan.siva.gayathriapp.R;
import mahadevan.siva.gayathriapp.adapters.FoodGroupGridAdapter;
import mahadevan.siva.gayathriapp.database.ApplicationDatabaseContract;
import mahadevan.siva.gayathriapp.database.ApplicationDatabaseHelper;
import mahadevan.siva.gayathriapp.models.FoodGroup;
import mahadevan.siva.gayathriapp.models.Ingredient;

public class FoodGroupListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_group_list);

        database = new ApplicationDatabaseHelper(this).getReadableDatabase();

        recyclerView = (RecyclerView) findViewById(R.id.food_group_recycler_view);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));

        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);

        List<FoodGroup> foodGroups = prepareAdapterData();
        adapter = new FoodGroupGridAdapter(this, foodGroups);
        recyclerView.setAdapter(adapter);

        FloatingActionButton button = (FloatingActionButton) findViewById(R.id.barcode_scanner_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new IntentIntegrator(FoodGroupListActivity.this).initiateScan();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Cancelled barcode scan", Toast.LENGTH_LONG).show();
            } else {
                String barcode = result.getContents();
                Cursor cursor = database.rawQuery("SELECT rowid, * FROM ingredients WHERE barcode = ?", new String[]{barcode});

                cursor.moveToFirst();
                if (cursor.isLast()) {
                    Ingredient ingredient = new Ingredient(
                            cursor.getInt(cursor.getColumnIndex("rowid")),
                            cursor.getString(cursor.getColumnIndex("name")),
                            cursor.getString(cursor.getColumnIndex("barcode")),
                            cursor.getInt(cursor.getColumnIndex("food_group_id")),
                            cursor.getString(cursor.getColumnIndex("avatar"))
                    );
                    Toast.makeText(this, "Ingredient scanned: " + ingredient.getName(), Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(FoodGroupListActivity.this, RecipeListActivity.class);
                    intent.putExtra("ingredient", ingredient);
                    cursor.close();
                    FoodGroupListActivity.this.startActivity(intent);
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    private List<FoodGroup> prepareAdapterData () {
        List<FoodGroup> foodGroups = new ArrayList<>();

        String[] projection = {ApplicationDatabaseContract.FoodGroupEntry.COLUMN_NAME_ROWID, ApplicationDatabaseContract.FoodGroupEntry.COLUMN_NAME_NAME, ApplicationDatabaseContract.FoodGroupEntry.COLUMN_NAME_AVATAR};
        Cursor cursor = database.query(ApplicationDatabaseContract.FoodGroupEntry.TABLE_NAME, projection, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            foodGroups.add(new FoodGroup(
                    cursor.getInt(cursor.getColumnIndexOrThrow(ApplicationDatabaseContract.FoodGroupEntry.COLUMN_NAME_ROWID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(ApplicationDatabaseContract.FoodGroupEntry.COLUMN_NAME_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(ApplicationDatabaseContract.FoodGroupEntry.COLUMN_NAME_AVATAR))
            ));
            cursor.moveToNext();
        }
        cursor.close();
        return foodGroups;
    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }
}
