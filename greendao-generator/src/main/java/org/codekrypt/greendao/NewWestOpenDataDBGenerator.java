package org.codekrypt.greendao;

import org.greenrobot.greendao.generator.DaoGenerator;
import org.greenrobot.greendao.generator.Entity;
import org.greenrobot.greendao.generator.Schema;

/**
 * Created by jaydenliang on 2017-02-15.
 */

public class NewWestOpenDataDBGenerator {
    public static void main(String[] args) throws Exception {

        //place where db folder will be created inside the project folder
        Schema schema = new Schema(1, "ca.bcit.comp3717.a00950911.opendata.dao");

        //table categories [id, cat_name]
        Entity entityCategories = schema.addEntity("Category");

        entityCategories.addIdProperty(); //It is the primary key for uniquely identifying a row
        entityCategories.addStringProperty("cat_name").notNull();  //Not null is SQL constrain

        //table datasets [id, cat_id, set_name, info_about]
        Entity entityDatasets = schema.addEntity("Dataset");

        entityDatasets.addIdProperty(); //It is the primary key for uniquely identifying a row
        entityDatasets.addLongProperty("cat_id");
        entityDatasets.addStringProperty("set_name").notNull();  //Not null is SQL constraint
        entityDatasets.addStringProperty("info_about").notNull();  //can be null

        //  ./app/src/main/java/   ----   com/codekrypt/greendao/db is the full path
        new DaoGenerator().generateAll(schema, "./app/src/main/java");

    }
}
