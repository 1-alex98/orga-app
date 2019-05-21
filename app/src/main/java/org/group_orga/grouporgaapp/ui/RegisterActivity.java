package org.group_orga.grouporgaapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.group_orga.grouporgaapp.R;
import org.group_orga.grouporgaapp.service.UserService;

public class RegisterActivity extends AppCompatActivity {
    public static final String EMAIL_PREFILL_EXTRA = "email_prefill_extra";
    public static final String PASSWORD_PREFILL_EXTRA = "password_prefill_extra";
    public static final int REGISTERED_WITH_SUCCESS = 1;
    private EditText emailRepeatEditText;
    private EditText emailEditText;
    private EditText passwordRepeatEditText;
    private EditText passwordEditText;
    private EditText usernameEditText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        passwordEditText = findViewById(R.id.passwordEditText);
        passwordRepeatEditText = findViewById(R.id.passwordRepeatEditText);
        emailEditText = findViewById(R.id.emailEditText);
        emailRepeatEditText = findViewById(R.id.emailRepeatEditText);
        usernameEditText = findViewById(R.id.usernameEditText);
        Intent intent = getIntent();
        if (intent.hasExtra(EMAIL_PREFILL_EXTRA) && intent.hasExtra(PASSWORD_PREFILL_EXTRA)) {
            emailEditText.setText(intent.getStringExtra(EMAIL_PREFILL_EXTRA));
            passwordEditText.setText(intent.getStringExtra(PASSWORD_PREFILL_EXTRA));
        }
    }

    public void register(View view) {
        if (!passwordEditText.getText().toString().equals(passwordRepeatEditText.getText().toString())) {
            Toast.makeText(this, getString(R.string.passwords_dont_match), Toast.LENGTH_LONG).show();
        }
        if (!emailEditText.getText().toString().equals(emailRepeatEditText.getText().toString())) {
            Toast.makeText(this, getString(R.string.emails_dont_match), Toast.LENGTH_LONG).show();
        }
        UserService.getInstance()
                .register(emailEditText.getText().toString(), passwordEditText.getText().toString(), usernameEditText.getText().toString());
        getIntent().putExtra(LoginScreenActivity.EMAIL_REGISTERED_EXTRA, emailEditText.getText().toString());
        getIntent().putExtra(LoginScreenActivity.PASSWORD_REGISTERED_EXTRA, passwordEditText.getText().toString());
        setResult(REGISTERED_WITH_SUCCESS, getIntent());
        finish();
    }


}
