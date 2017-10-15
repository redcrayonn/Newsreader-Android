package olaf.demol.nl.newsreader548385.Activities;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import olaf.demol.nl.newsreader548385.R;
import olaf.demol.nl.newsreader548385.net.Models.Results.LoginResult;
import olaf.demol.nl.newsreader548385.net.Models.User;
import olaf.demol.nl.newsreader548385.net.Services.UserService;
import olaf.demol.nl.newsreader548385.net.WebClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserLogin extends AppCompatActivity implements View.OnClickListener, Callback<LoginResult> {

    private EditText usernameText;
    private EditText passwordText;
    private Button loginButton;
    private TextView errorText;
    private ProgressBar progressBar;

    private UserService userService;
    private boolean loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_login);

        usernameText = findViewById(R.id.username);
        passwordText = findViewById(R.id.password);
        errorText = findViewById(R.id.errorText);

        loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(this);

        progressBar = findViewById(R.id.progressbar);

        userService = new WebClient().createUserService();
    }

    @Override
    public void onClick(View view) {
        errorText.setVisibility(View.GONE);
        String username = usernameText.getText().toString();
        String password = passwordText.getText().toString();

        if(TextUtils.isEmpty(username)) {
            errorText.setText(R.string.username_error);
            errorText.setVisibility(View.VISIBLE);
            return;
        }
        loginButton.setEnabled(false);

        userService.login(username, password).enqueue(this);
    }

    @Override
    public void onResponse(@NonNull Call<LoginResult> call, @NonNull Response<LoginResult> response) {
        if (response.isSuccessful() && response.body() != null) {

            LoginResult result = response.body();

            if (result != null) {
                Toast.makeText(this, getResources().getText(R.string.login), Toast.LENGTH_SHORT).show();

                User.setAuthtoken(result.getAuthToken());

                setResult(RESULT_OK);
                finish();
            } else {
                Toast.makeText(this, getResources().getText(R.string.error), Toast.LENGTH_SHORT).show();
                resetLoginAttempt();
            }
        } else {
            Toast.makeText(this, getResources().getText(R.string.error), Toast.LENGTH_SHORT).show();
            resetLoginAttempt();
        }
    }

    @Override
    public void onFailure(@NonNull Call<LoginResult> call, @NonNull Throwable t) {
        Resources resources = getResources();
        String message = resources.getString(R.string.login_error, t.getLocalizedMessage());

        Toast.makeText(this, message, Toast.LENGTH_LONG).show();

        resetLoginAttempt();
    }

    private void resetLoginAttempt() {
        loading = false;
        loginButton.setEnabled(true);
        progressBar.setVisibility(View.GONE);
    }
}
