package infinnov.udacity.capstoneproject;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

/**
 * Created by Priyank on 01-Nov-16.
 */

public class ScoreProvider extends ContentProvider {
    static final String PROVIDER_NAME = "infinnov.udacity.capstoneproject";
    static final String URL = "content://" + PROVIDER_NAME + "/scores";
    static final Uri CONTENT_URI = Uri.parse(URL);

    static final String _ID = "_id";
    static final String SCORE = "score";

    //private static HashMap<String, String> SCORES_PROJECTION_MAP;

    //static final int SCORES = 1;
    static final int SCORES_ID = 2;

    static final UriMatcher uriMatcher;
    static final String DATABASE_NAME = "ScoreDB";
    static final String SCORES_TABLE_NAME = "scores";
    static final int DATABASE_VERSION = 1;
    static final String CREATE_DB_TABLE =
            " CREATE TABLE " + SCORES_TABLE_NAME +
                    " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    " score INTEGER NOT NULL);";
    static final String INSERT_RECORD_INTO_TABLE =
            " INSERT INTO " + SCORES_TABLE_NAME + " (" + SCORE + ") " +
                    " VALUES (0);";

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        //uriMatcher.addURI(PROVIDER_NAME, "scores", SCORES);
        uriMatcher.addURI(PROVIDER_NAME, "scores/#", SCORES_ID);
    }

    /**
     * Database specific constant declarations
     */
    private SQLiteDatabase db;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
        return (db == null) ? false : true;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long rowID = db.insert(SCORES_TABLE_NAME, "", values);
        if (rowID > 0) {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }
        throw new SQLException("Failed to add a record into " + uri);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(SCORES_TABLE_NAME);

        switch (uriMatcher.match(uri)) {
            case SCORES_ID:
                qb.appendWhere(_ID + "=" + uri.getPathSegments().get(1));
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;

        switch (uriMatcher.match(uri)) {
            case SCORES_ID:
                String id = uri.getPathSegments().get(1);
                count = db.delete(SCORES_TABLE_NAME, _ID + " = " + id +
                        (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int count = 0;

        switch (uriMatcher.match(uri)) {
            case SCORES_ID:
                count = db.update(SCORES_TABLE_NAME, values, _ID + " = " + uri.getPathSegments().get(1) +
                        (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case SCORES_ID:
                return "vnd.android.cursor.item/vnd.example.scores";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_DB_TABLE);
            db.execSQL(INSERT_RECORD_INTO_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + SCORES_TABLE_NAME);
            onCreate(db);
        }
    }
}
