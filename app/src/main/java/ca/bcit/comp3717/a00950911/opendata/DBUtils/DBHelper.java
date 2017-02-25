package ca.bcit.comp3717.a00950911.opendata.DBUtils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import ca.bcit.comp3717.a00950911.opendata.AppConstants;
import ca.bcit.comp3717.a00950911.opendata.R;
import ca.bcit.comp3717.a00950911.opendata.dao.Category;
import ca.bcit.comp3717.a00950911.opendata.dao.CategoryDao;
import ca.bcit.comp3717.a00950911.opendata.dao.DaoMaster;
import ca.bcit.comp3717.a00950911.opendata.dao.DaoMaster.DevOpenHelper;
import ca.bcit.comp3717.a00950911.opendata.dao.DaoSession;
import ca.bcit.comp3717.a00950911.opendata.dao.Dataset;
import ca.bcit.comp3717.a00950911.opendata.dao.DatasetDao;

/**
 * Created by jaydenliang on 2017-02-16.
 */

public class DBHelper {
    public static final int NOT_FOUND = -1;
    private static final int DATABASE_VERSION = 1;
    private static DBHelper instance;
    private DevOpenHelper devOpenHelper;
    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private Context context;

    public DBHelper(final Context context) {
        instance = this;
        this.context = context;
        devOpenHelper = new DevOpenHelper(context, AppConstants.DATABASE_DATABASE_NAME, null);
        if (openReadableDatabase().categoryCount() == 0)
            importFromResource();
    }

    synchronized public static DBHelper getInstance(final Context context) {
        if (instance == null)
            instance = new DBHelper(context);
        return instance;
    }

    public static final String getTagCategoryId() {
        return CategoryDao.TABLENAME + "_" + CategoryDao.Properties.Id.columnName;
    }

    public static final String getCategoryNameColumnName() {
        return CategoryDao.Properties.Cat_name.columnName;
    }

    public static final String getTagDatasetId() {
        return DatasetDao.TABLENAME + "_" + DatasetDao.Properties.Id.columnName;
    }

    public static final String getDatasetNameColumnName() {
        return DatasetDao.Properties.Set_name.columnName;
    }

    synchronized public DBHelper openWritableDatabase() {
        if (db == null || db.isReadOnly()) {
            db = devOpenHelper.getWritableDatabase();
            daoMaster = new DaoMaster(db);
            daoSession = daoMaster.newSession();
        }
        return this;
    }

    synchronized public DBHelper openReadableDatabase() {
        if (db == null || !db.isReadOnly()) {
            db = devOpenHelper.getReadableDatabase();
            daoMaster = new DaoMaster(db);
            daoSession = daoMaster.newSession();
        }
        return this;
    }

    public void closeDatabase() {

    }

    synchronized public void resetDatabase() {
        if (db != null) {
            daoSession.getCategoryDao().deleteAll();
            daoSession.getDatasetDao().deleteAll();
            importFromResource();
        }
    }

    private DBHelper importFromResource() {
        openWritableDatabase();
        String[] cat;
        Long catId;
        Category category;
        //cat 1
        cat = context.getResources().getStringArray(R.array.nwod_category_economy_and_living);
        category = new Category();
        category.setCat_name("Economy and Living");
        catId = daoSession.getCategoryDao().insertOrReplace(category);
        updateDatasets(cat, catId);
        //cat 2
        cat = context.getResources().getStringArray(R.array.nwod_category_facilities_and_services);
        category = new Category();
        category.setCat_name("Facilities and Services");
        catId = daoSession.getCategoryDao().insertOrReplace(category);
        updateDatasets(cat, catId);
        //cat 3
        cat = context.getResources().getStringArray(R.array.nwod_category_infrastructure);
        category = new Category();
        category.setCat_name("Infrastructure");
        catId = daoSession.getCategoryDao().insertOrReplace(category);
        updateDatasets(cat, catId);
        //cat 4
        cat = context.getResources().getStringArray(R.array.nwod_category_statistics);
        category = new Category();
        category.setCat_name("Statistics");
        catId = daoSession.getCategoryDao().insertOrReplace(category);
        updateDatasets(cat, catId);
        //cat 5
        cat = context.getResources().getStringArray(R.array.nwod_category_other);
        category = new Category();
        category.setCat_name("Others");
        catId = daoSession.getCategoryDao().insertOrReplace(category);
        updateDatasets(cat, catId);
        return this;
    }

    private void updateDatasets(String[] array, Long id) {
        for (String description : array) {
            String name = description.substring(0, description.indexOf("|")).replace("_", " ");
            String desc = description.substring(description.indexOf("|") + 1);
            Dataset dataset = new Dataset();
            dataset.setCat_id(id);
            dataset.setSet_name(name);
            dataset.setInfo_about(desc);
            daoSession.getDatasetDao().insertOrReplace(dataset);
        }
    }

    public Long categoryCount() {
        return daoSession.getCategoryDao().count();
    }

    public Dataset getDatasetById(Long datasetId) {
        return daoSession.getDatasetDao().queryBuilder()
                .where(DatasetDao.Properties.Id.eq(datasetId)).unique();
    }

    public Cursor getCategoryCursor() {
        return daoSession.getCategoryDao().queryBuilder().buildCursor().query();
    }

    public Cursor getDatasetCursorByCategoryId(Long categoryId) {
        return daoSession.getDatasetDao().queryBuilder()
                .where(DatasetDao.Properties.Cat_id.eq(categoryId)).buildCursor().query();
    }

    public Cursor getDatasetCursorById(Long datasetId) {
        return daoSession.getDatasetDao().queryBuilder().where(DatasetDao.Properties.Cat_id.eq(datasetId))
                .buildCursor().query();
    }

    public Long getCategoryIdAtCursor(Cursor cursor, int position) {
        Category category = daoSession.getCategoryDao().readEntity(cursor, 0);
        return category == null ? NOT_FOUND : category.getId();
    }

    public Long getDatasetIdAtCursor(Cursor cursor, int position) {
        Dataset dataset = daoSession.getDatasetDao().readEntity(cursor, 0);
        return dataset == null ? NOT_FOUND : dataset.getId();
    }

    public String getDatasetNameById(Long datasetId) {
        List<Dataset> datasetList = daoSession.getDatasetDao().queryBuilder()
                .where(DatasetDao.Properties.Id.eq(datasetId)).limit(1).list();
        return datasetList == null || datasetList.size() == 0 ? null : datasetList.get(0).getSet_name();
    }

    public String getDatasetInfoAboutById(Long datasetId) {
        List<Dataset> datasetList = daoSession.getDatasetDao().queryBuilder()
                .where(DatasetDao.Properties.Cat_id.eq(datasetId)).limit(1).list();
        return datasetList == null || datasetList.size() == 0 ? null : datasetList.get(0).getInfo_about();
    }

    public String getDatasetNameAtCursor(Cursor cursor, int position) {
        Dataset dataset = daoSession.getDatasetDao().readEntity(cursor, position);
        return dataset == null ? null : dataset.getSet_name();
    }
}
