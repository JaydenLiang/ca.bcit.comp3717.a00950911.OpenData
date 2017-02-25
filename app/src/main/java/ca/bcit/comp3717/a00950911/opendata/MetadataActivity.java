package ca.bcit.comp3717.a00950911.opendata;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import ca.bcit.comp3717.a00950911.opendata.DBUtils.DBHelper;
import ca.bcit.comp3717.a00950911.opendata.dao.Dataset;

public class MetadataActivity extends AppCompatActivity {
    private Long datasetId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metadata);
        if (getIntent() == null) {
            alertAndClose("Sorry, this Page is Unexpectedly opened, and is going to close now.");
            return;
        }
        datasetId = getIntent().getLongExtra(
                DBHelper.getInstance(getApplicationContext()).getTagDatasetId(),
                DBHelper.NOT_FOUND);
        if (datasetId == DBHelper.NOT_FOUND) {
            alertAndClose("This is an empty dataset.");
            return;
        }
        displayMetadata();
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

    private void displayMetadata() {
        Dataset dataset = DBHelper.getInstance(getApplicationContext()).getDatasetById(datasetId);
        if (dataset != null) {
            ((TextView) findViewById(R.id.textView_display_ds_meta_label_cat_name)).setText(dataset.getSet_name());
            ((TextView) findViewById(R.id.textView_display_ds_meta_content_about)).setText(dataset.getInfo_about());
        }
    }
}
