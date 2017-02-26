package ca.bcit.comp3717.a00950911.opendata.listAdapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import ca.bcit.comp3717.a00950911.opendata.R;
import ca.bcit.comp3717.a00950911.opendata.dao.Category;

/**
 * A list adapter that accepts a List<Category>
 * Created by jaydenliang on 2017-02-16.
 */

public class CategoryListAdapter extends ArrayAdapter<Category> {
    private List<Category> categoryList;

    public CategoryListAdapter(Context context, List<Category> categoryList) {
        super(context, R.layout.layout_category_list);
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.layout_category_list, parent, false);
        TextView textViewCatName = (TextView) rowView.findViewById(R.id.textView_layout_cat_list_name);
        textViewCatName.setText(this.categoryList.get(position).getCat_name());
        return rowView;
    }

    @Override
    public int getCount() {
        return categoryList.size();
    }

    @Nullable
    @Override
    public Category getItem(int position) {
        return categoryList.get(position);
    }
}
