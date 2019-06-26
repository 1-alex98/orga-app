package org.group_orga.grouporgaapp.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.group_orga.grouporgaapp.R;
import org.group_orga.grouporgaapp.service.GroupService;
import org.group_orga.grouporgaapp.util.UIUtil;

import java.util.Collections;

import java8.util.Comparators;

public class GroupsActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter<Object> groupListViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);

        listView= findViewById(R.id.groupListView);

        groupListViewAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1);
        listView.setAdapter(groupListViewAdapter);

        refreshMyGroups();
    }

    private void refreshMyGroups() {
        GroupService.getInstance().getMyGroups()
                .thenAccept(groupOfUsers -> runOnUiThread(() -> {
                    Collections.sort(groupOfUsers, Comparators.comparing(groupOfUsers1 -> groupOfUsers1.getName()));
                    groupListViewAdapter.clear();
                    groupListViewAdapter.addAll(groupOfUsers);
                }))
                .exceptionally(UIUtil.defaultAPIErrorHandler(this));
    }

    public void createNewGroup(View buttonPressed) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.new_group_dialog,null);
        dialogBuilder.setView(view);
        dialogBuilder.setNegativeButton(R.string.cancel,(dialog, which) ->dialog.dismiss());
        dialogBuilder.setPositiveButton(R.string.create,(dialog, which) -> createGroupAndReadOutTexts(view,dialog));
        dialogBuilder.create().show();
    }

    private void createGroupAndReadOutTexts(View view, DialogInterface dialog) {
        EditText nameEditText = view.findViewById(R.id.groupNameEditText);
        EditText passwordEditText = view.findViewById(R.id.groupPasswordEditText);
        GroupService.getInstance().createGroup(nameEditText.getText().toString(),passwordEditText.getText().toString())
                .thenAccept(groupOfUsers -> runOnUiThread(() -> {
                    dialog.dismiss();
                    refreshMyGroups();
                }))
                .exceptionally(UIUtil.defaultAPIErrorHandler(this));
    }

    public void startSearchActivity(View view) {
        Intent intent = new Intent(this, GroupSearchActivity.class);
        startActivity(intent);
    }
}
