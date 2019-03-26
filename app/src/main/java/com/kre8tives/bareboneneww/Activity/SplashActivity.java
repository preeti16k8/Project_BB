package com.kre8tives.bareboneneww.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.kre8tives.bareboneneww.R;
import com.kre8tives.bareboneneww.Util.UserSessionManager;


/* In this activity we will manage sessions is already registered user..or we will register them and will store data on shared preference.
*/

public class SplashActivity extends Activity  {


    boolean isUserLoggedIn;
    UserSessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        session = new UserSessionManager(getApplicationContext());

        this.getWindow().setStatusBarColor(this.getResources().getColor(R.color.black));
        isUserLoggedIn = session.isUserLoggedIn();


        
        if(isUserLoggedIn==true)
        {
            Intent intent = new Intent(SplashActivity.this,StoreActivity.class);
            startActivity(intent);
            SplashActivity.this.finish();
        }
        else {
            setContentView(R.layout.activity_splash);
            Thread timer = new Thread() {
                public void run() {
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                    } finally {
                        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(intent);
                        SplashActivity.this.finish();
                    }
                }
            };
            timer.start();
        }

    }

}
