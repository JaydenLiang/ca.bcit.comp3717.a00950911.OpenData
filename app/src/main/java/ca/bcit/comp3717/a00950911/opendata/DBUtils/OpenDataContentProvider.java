package ca.bcit.comp3717.a00950911.opendata.DBUtils;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

import ca.bcit.comp3717.a00950911.opendata.AppConstants;

/**
 * Created by jaydenliang on 2017-02-17.
 */

public class OpenDataContentProvider extends ContentProvider {
    public static final String AUTHORITY = AppConstants.APPLICATION_IDENTIFIER + ".content.provider";
    private static final String CONTENT_TYPE = "opendata";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + CONTENT_TYPE);
    private static final int TYPE_CATEGORY_LIST = 1;
    private static final int TYPE_CATEGORY_SET = 2;
    private static final int TYPE_DATASET_ITEM = 3;
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(AUTHORITY, CONTENT_TYPE + "/category", TYPE_CATEGORY_LIST);
        uriMatcher.addURI(AUTHORITY, CONTENT_TYPE + "/category/set/#", TYPE_CATEGORY_SET);
        uriMatcher.addURI(AUTHORITY, CONTENT_TYPE + "/dataset/item/#", TYPE_DATASET_ITEM);
    }

    /**
     * get URI for a query for category
     *
     * @return
     */
    public static Uri getCategoryQueryURI() {
        return Uri.withAppendedPath(CONTENT_URI, "category");
    }

    /**
     * get URI for a query for a dataset by given dataset id
     * @return
     */
    public static Uri getCategorySetQueryURI(Long datasetId) {
        return Uri.withAppendedPath(CONTENT_URI, "category/set/" + datasetId.toString());
    }

    @Override
    public boolean onCreate() {
        DBHelper.getInstance(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        long resourceId;
        switch (uriMatcher.match(uri)) {
            //query category
            case TYPE_CATEGORY_LIST:
                return DBHelper.getInstance(getContext()).getCategoryCursor();
            //query dataset
            case TYPE_DATASET_ITEM:
                resourceId = ContentUris.parseId(uri);
                if (resourceId <= 0) {
                    throw new IllegalArgumentException("Invalid dataset Id in: " + uri);
                }
                return DBHelper.getInstance(getContext()).getDatasetCursorById(resourceId);
            //query category dataset
            case TYPE_CATEGORY_SET:
                resourceId = ContentUris.parseId(uri);
                //must pass a valid id for query using this query type
                if (resourceId <= 0) {
                    throw new IllegalArgumentException("Invalid category Id in: " + uri);
                }
                return DBHelper.getInstance(getContext()).getDatasetCursorByCategoryId(resourceId);
            default:
                throw new IllegalArgumentException("Invalid uri: " + uri);
        }
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case TYPE_CATEGORY_LIST:
                return "vnd.android.cursor.dir/" + CONTENT_TYPE + "/category";
            case TYPE_CATEGORY_SET:
                return "vnd.android.cursor.dir/" + CONTENT_TYPE + "/category-set";
            case TYPE_DATASET_ITEM:
                return "vnd.android.cursor.item/" + CONTENT_TYPE + "/dataset-item";
            default:
                throw new IllegalArgumentException("Invalid uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }
}
