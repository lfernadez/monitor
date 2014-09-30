package py.fpuna.tesis.qoetest.database;

import android.content.ContentProvider;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentValues;
import android.content.OperationApplicationException;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import java.util.ArrayList;

/**
 * Created by User on 01/09/2014.
 */
public class Provider extends ContentProvider {

    private static final int LOCALIZACIONES = 100;
    private static final int LOCALIZACIONES_ID = 101;
    private static final UriMatcher sUriMatcher;

    static {
        // @formatter:off
        sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        sUriMatcher.addURI(DatabaseContract.CONTENT_AUTHORITY, "localizacion", LOCALIZACIONES);
        sUriMatcher.addURI(DatabaseContract.CONTENT_AUTHORITY, "localizacion/*", LOCALIZACIONES_ID);
    }

    private Database mOpenHelper;

    @Override
    public boolean onCreate() {
        mOpenHelper = new Database(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        SelectionBuilder builder = buildExpandedSelection(uri, selection, selectionArgs);
        return builder.query(db, projection, sortOrder);
    }

    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case LOCALIZACIONES:
                return DatabaseContract.Localizacion.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        switch (sUriMatcher.match(uri)) {
            case LOCALIZACIONES:
                String localizacionId = insertOrThrow(uri, values, db, Database.Tables.LOCALIZACION);
                return DatabaseContract.Localizacion.buildUri(localizacionId);
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        SelectionBuilder builder = buildSimpleSelection(uri, selection, selectionArgs);
        int result = builder.delete(db);
        notifyChange(uri);
        return result;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        SelectionBuilder builder = buildSimpleSelection(uri, selection, selectionArgs);
        int result = builder.update(db, values);
        notifyChange(uri);
        return result;
    }

    @Override
    public ContentProviderResult[] applyBatch(ArrayList<ContentProviderOperation> operations)
            throws OperationApplicationException {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            final int n = operations.size();
            final ContentProviderResult[] results = new ContentProviderResult[n];
            for (int i = 0; i < n; i++) {
                results[i] = operations.get(i).apply(this, results, i);
            }
            db.setTransactionSuccessful();
            return results;
        } finally {
            db.endTransaction();
        }
    }

    private SelectionBuilder buildSimpleSelection(Uri uri) {
        SelectionBuilder builder = new SelectionBuilder();
        switch (sUriMatcher.match(uri)) {
            case LOCALIZACIONES:
                return builder.table(Database.Tables.LOCALIZACION);

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    private SelectionBuilder buildSimpleSelection(Uri uri, String selection, String[] selectionArgs) {
        return buildSimpleSelection(uri).where(selection, selectionArgs);
    }

    private SelectionBuilder buildExpandedSelection(Uri uri) {
        // final SelectionBuilder builder = new SelectionBuilder();
        switch (sUriMatcher.match(uri)) {
            default:
                return buildSimpleSelection(uri);
        }
    }

    private SelectionBuilder buildExpandedSelection(Uri uri, String selection,
                                                    String[] selectionArgs) {
        return buildExpandedSelection(uri).where(selection, selectionArgs);
    }

    private String insertOrThrow(Uri uri, ContentValues values, SQLiteDatabase db, String table) {
        long id = db.insertOrThrow(table, null, values);
        notifyChange(uri);
        return String.valueOf(id);
    }

    private void notifyChange(Uri uri) {
        getContext().getContentResolver().notifyChange(uri, null, false);
    }
}
