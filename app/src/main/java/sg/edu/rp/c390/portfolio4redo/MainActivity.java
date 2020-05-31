package sg.edu.rp.c390.portfolio4redo;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private int SLEEP_TIMER = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        LogoLauncher logoLauncher = new LogoLauncher();
        logoLauncher.start();

    }

    private class LogoLauncher extends Thread{
        public void run() {
            try{
                sleep(1000 * SLEEP_TIMER);
            }catch(InterruptedException e) {
                e.printStackTrace();
            }

            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(intent);
            MainActivity.this.finish();

        }
    }

}