package org.group_orga.grouporgaapp.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.group_orga.grouporgaapp.R;
import org.group_orga.grouporgaapp.api.data.GroupOfUsers;
import org.group_orga.grouporgaapp.service.GroupService;
import org.group_orga.grouporgaapp.util.UIUtil;

public class GroupsActivity extends AppCompatActivity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);

        listView= findViewById(R.id.groupListView);

        ArrayAdapter<GroupOfUsers> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1);
        listView.setAdapter(adapter);

        GroupService.getInstance().getMyGroups()
                .thenAccept(groupOfUsers -> runOnUiThread(() -> adapter.addAll(groupOfUsers)))
                .exceptionally(UIUtil.defaultAPIErrorHandler(this));
    }

}
