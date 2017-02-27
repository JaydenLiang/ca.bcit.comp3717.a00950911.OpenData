package ca.bcit.comp3717.a00950911.opendata;

import android.app.ListActivity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SimpleCursorAdapter;

import ca.bcit.comp3717.a00950911.opendata.DBUtils.DBHelper;
import ca.bcit.comp3717.a00950911.opendata.DBUtils.OpenDataContentProvider;

public class MainActivity extends ListActivity implements AdapterView.OnItemClickListener, LoaderCallbacks {
    SimpleCursorAdapter simpleCursorAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //update category list view
        simpleCursorAdapter = new SimpleCursorAdapter(this,
                R.layout.layout_category_list, null, new String[]{
                DBHelper.getCategoryNameColumnName()
        }, new int[]{
                R.id.textView_layout_cat_list_name
        }, 0);
        setListAdapter(simpleCursorAdapter);
        getListView().setOnItemClickListener(this);
        getLoaderManager().initLoader(AppConstants.LOADER_CATEGORY_LIST_VIEW_LOADER_ID, null, this);
    }

    @Override
    protected void onDestroy() {
        DBHelper.getInstance(getApplicationContext()).closeDatabase();
        super.onDestroy();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Long categoryId = DBHelper.getInstance(getApplicationContext())
                .getCategoryIdAtCursor((Cursor) simpleCursorAdapter.getItem(position), position);
        Intent intent = new Intent(getApplicationContext(), DatasetListViewActivity.class);
        intent.putExtra(DBHelper.getTagCategoryId(), categoryId);
        startActivity(intent);
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        if (id != AppConstants.LOADER_CATEGORY_LIST_VIEW_LOADER_ID) {
            return null;
        }
        return new CursorLoader(MainActivity.this,
                OpenDataContentProvider.getCategoryQueryURI(),
                new String[]{DBHelper.getCategoryNameColumnName()}, null, null,
                null);
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {
        simpleCursorAdapter.swapCursor((Cursor) data);
    }

    @Override
    public void onLoaderReset(Loader loader) {
        simpleCursorAdapter.swapCursor(null);
    }
}