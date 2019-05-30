package org.group_orga.grouporgaapp.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.group_orga.grouporgaapp.R;
import org.group_orga.grouporgaapp.api.OrgaAPIAccessor;
import org.group_orga.grouporgaapp.util.UIUtil;

import java8.util.concurrent.CompletableFuture;

public class GroupsActivity extends AppCompatActivity {

    private TextView versionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);

        versionTextView = findViewById(R.id.versionTextView);
        CompletableFuture<String> version = OrgaAPIAccessor.getInstance().getVersion();
        version.thenAccept(s -> runOnUiThread(() -> versionTextView.setText(s)))
                .exceptionally(UIUtil.defaultAPIErrorHandler(this));

    }

}
