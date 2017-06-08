package mahadevan.siva.gayathriapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by siva on 2016-12-14.
 */

public class ApplicationDatabaseHelper extends SQLiteAssetHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "app.db";

    public ApplicationDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

}
