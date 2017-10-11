package com.example.josh.lab2;

import android.app.ListActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.view.View;
import java.util.List;

/**
 * Created by Josh on 10/2/2017.
 */

public class DatabaseActivity extends ListActivity {

    private CommentsDataSource datasource;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.database_activity);
        datasource = new CommentsDataSource(this);
        datasource.open();
        List<String> values = datasource.getAllComments();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, values);
        setListAdapter(adapter);
    }

    public void onClick(View v) {
        @SuppressWarnings("unchecked")
        ArrayAdapter<Comment> adapter = (ArrayAdapter<Comment>) getListAdapter();
        Comment comment = null;
        switch (v.getId()) {
            case R.id.add:
                adapter.add(comment);
                break;
            case R.id.delete:
                if (getListAdapter().getCount() > 0) {
                    comment = (Comment) getListAdapter().getItem(0);
                    datasource.deleteComment(comment);
                    adapter.remove(comment);
                }
                break;
            case R.id.deleteall:
                if (getListAdapter().getCount() > 0) {
                    datasource.deleteAllComments();
                    adapter.clear();
                }
                break;
        }
        adapter.notifyDataSetChanged();
    }

}
