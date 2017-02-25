package ca.bcit.comp3717.a00950911.opendata;

import android.app.ListActivity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SimpleCursorAdapter;

import ca.bcit.comp3717.a00950911.opendata.DBUtils.DBHelper;
import ca.bcit.comp3717.a00950911.opendata.DBUtils.OpenDataContentProvider;

public class DatasetListViewActivity extends ListActivity implements AdapterView.OnItemClickListener, LoaderCallbacks {
    private SimpleCursorAdapter simpleCursorAdapter;
    private Long categoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() == null) {
            alertAndClose("Sorry, this Page is Unexpectedly opened, and is going to close now.");
            return;
        }
        categoryId = getIntent().getLongExtra(
                DBHelper.getInstance(getApplicationContext()).getTagCategoryId(),
                DBHelper.NOT_FOUND);
        if (categoryId == DBHelper.NOT_FOUND) {
            alertAndClose("This is an empty category.");
            return;
        }
        //update dataset list view
        simpleCursorAdapter = new SimpleCursorAdapter(this,
                R.layout.layout_category_list, null, new String[]{
                DBHelper.getDatasetNameColumnName()
        }, new int[]{
                R.id.textView_layout_cat_list_name
        }, 0);
        setListAdapter(simpleCursorAdapter);
        getListView().setOnItemClickListener(this);
        getLoaderManager().initLoader(AppConstants.LOADER_DATASET_LIST_VIEW_LOADER_ID, null, this);
    }

    protected void alertAndClose(String msg) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setMessage(msg);
        builder.show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Long datasetId = DBHelper.getInstance(getApplicationContext())
                .getDatasetIdAtCursor((Cursor) simpleCursorAdapter.getItem(position), position);
        Intent intent = new Intent(getApplicationContext(), MetadataActivity.class);
        intent.putExtra(DBHelper.getTagDatasetId(), datasetId);
        startActivity(intent);
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        if (id != AppConstants.LOADER_DATASET_LIST_VIEW_LOADER_ID) {
            return null;
        }
        return new CursorLoader(DatasetListViewActivity.this,
                OpenDataContentProvider.getCategorySetQueryURI(categoryId),
                new String[]{DBHelper.getDatasetNameColumnName()}, null, null,
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
