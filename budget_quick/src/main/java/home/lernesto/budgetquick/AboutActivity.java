package home.lernesto.budgetquick;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class AboutActivity extends AppCompatActivity {
//    Visual Variables
    private Toolbar toolbar;
    private TextView version, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

//        To link file XML
        toolbar = findViewById(R.id.appbar_about);
        version = findViewById(R.id.version);
        email   = findViewById(R.id.email);

        initializeToolbar();
        initializeTextViewVersion();

        email.setOnClickListener(v -> sendEmail());
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void initializeToolbar(){
        if (toolbar != null){
            toolbar.setTitle(
                    getString(R.string.about) + " " + getResources().getString(R.string.app_name)
            );
            setSupportActionBar(toolbar);
        }
    }

    private void initializeTextViewVersion(){
        version.setText(BuildConfig.VERSION_NAME);
    }

    @SuppressLint("QueryPermissionsNeeded")
    private void sendEmail(){
        String[] recipients = new String[]{email.getText().toString()};

        Intent extIntent = new Intent(Intent.ACTION_SENDTO);
        extIntent.setData(Uri.parse("mailto:"));
        extIntent.putExtra(Intent.EXTRA_EMAIL, recipients);
        if (extIntent.resolveActivity(getPackageManager()) != null)startActivity(extIntent);
    }
}
