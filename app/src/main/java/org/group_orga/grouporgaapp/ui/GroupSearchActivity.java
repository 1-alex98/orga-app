package org.group_orga.grouporgaapp.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.group_orga.grouporgaapp.R;
import org.group_orga.grouporgaapp.api.data.GroupOfUsers;
import org.group_orga.grouporgaapp.service.GroupService;

import java.util.Collections;
import java.util.List;

import java8.util.Comparators;
import java8.util.concurrent.CompletableFuture;

public class GroupSearchActivity extends AppCompatActivity {

    private ArrayAdapter<GroupOfUsers> groupListViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_search);

        ListView listView = findViewById(R.id.groupListView);

        groupListViewAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1);
        listView.setAdapter(groupListViewAdapter);

    }

    public void searchForGroup(View view) {
        EditText searchText = findViewById(R.id.searchEditText);
        CompletableFuture<List<GroupOfUsers>> allGroupsWithName = GroupService.getInstance().getAllGroupsWithName(searchText.getText().toString());
        allGroupsWithName.thenAccept(groupOfUsers -> runOnUiThread(() -> {
            Collections.sort(groupOfUsers, Comparators.comparing(groupOfUsers1 -> groupOfUsers1.getName()));
            groupListViewAdapter.clear();
            groupListViewAdapter.addAll(groupOfUsers);
        }));
    }
}