package home.lernesto.budgetquick;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.Timer;
import java.util.TimerTask;

public class StartActivity extends AppCompatActivity {
//    Constant
    private static final int TIME_LIFE_ACTIVITY = 2500;
    private static final int TIME_ANIMATION= 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_start);

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                startActivity(new Intent(StartActivity.this, MainActivity.class));
                finish();
            }
        };

        Timer timer = new Timer();
        timer.schedule(task, TIME_LIFE_ACTIVITY);
    }

    @Override
    protected void onResume() {
        super.onResume();
        YoYo.with(Techniques.FlipInX).duration(TIME_ANIMATION).playOn(findViewById(R.id.imgv_app_name));
    }
}
