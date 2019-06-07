package org.group_orga.grouporgaapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import org.group_orga.grouporgaapp.R;
import org.group_orga.grouporgaapp.service.UserService;
import org.group_orga.grouporgaapp.util.UIUtil;

public class LoginScreenActivity extends AppCompatActivity {
    public static final String EMAIL_REGISTERED_EXTRA = "email_registered_extra";
    public static final String PASSWORD_REGISTERED_EXTRA = "password_registered_extra";
    private static final int REGISTER_REQUEST_CODE = 1;
    private EditText emailEditText;
    private EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        passwordEditText = findViewById(R.id.passwordEditText);
        emailEditText = findViewById(R.id.emailEditText);
    }

    public void register(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        intent.putExtra(RegisterActivity.EMAIL_PREFILL_EXTRA, emailEditText.getText().toString());
        intent.putExtra(RegisterActivity.PASSWORD_PREFILL_EXTRA, passwordEditText.getText().toString());
        startActivityForResult(intent, REGISTER_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REGISTER_REQUEST_CODE && resultCode == RegisterActivity.REGISTERED_WITH_SUCCESS) {
            String email = data.getStringExtra(EMAIL_REGISTERED_EXTRA);
            emailEditText.setText(email);
            String password = data.getStringExtra(PASSWORD_REGISTERED_EXTRA);
            passwordEditText.setText(password);
        }
    }

    public void login(View view) {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        UserService.getInstance().login(email, password)
                .exceptionally(UIUtil.defaultAPIErrorHandler(this))
                .thenAccept(tokenResponse -> {
                    if (tokenResponse != null) {
                        runOnUiThread(() -> {
                            Intent intent = new Intent(this, GroupsActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        });
                    }
                });
    }
}
