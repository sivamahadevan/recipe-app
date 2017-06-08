package mahadevan.siva.gayathriapp.database;

import android.provider.BaseColumns;

/**
 * Created by siva on 2016-12-14.
 */

public final class ApplicationDatabaseContract {

    private ApplicationDatabaseContract () {}

    public static class FoodGroupEntry implements BaseColumns {
        public static final String TABLE_NAME = "food_groups";
        public static final String COLUMN_NAME_ROWID = "rowid";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_AVATAR = "avatar";
    }

    public static class IngredientEntry implements BaseColumns {
        public static final String TABLE_NAME = "ingredients";
        public static final String COLUMN_NAME_ROWID = "rowid";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_BARCODE = "barcode";
        public static final String COLUMN_NAME_FOOD_GROUP_ID = "food_group_id";
        public static final String COLUMN_NAME_AVATAR = "avatar";
    }

    public static class RecipeEntry implements BaseColumns {
        public static final String TABLE_NAME = "recipes";
        public static final String COLUMN_NAME_ROWID = "rowid";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_AVATAR = "avatar";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_PREP_TIME = "prep_time";

    }

}
