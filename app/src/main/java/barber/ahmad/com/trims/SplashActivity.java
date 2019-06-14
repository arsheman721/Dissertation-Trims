package barber.ahmad.com.trims;

import android.content.Intent;
import android.se.omapi.Session;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.json.JSONException;

import barber.ahmad.com.trims.Barber.Home;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

                Thread splassh=new Thread()
                {
                    public void run()
                        {
                            try
                                {
                                    sleep(3000);
                                }
                             catch (Exception ex)
                                {
                                    ex.printStackTrace();
                                }
                            finally
                                {
                                    Intent main=null;
                                    try {
                                        if(SessionHelper.isUserLoggedIn(SplashActivity.this) && SessionHelper.getCurrentUser(SplashActivity.this).getString("type").equals("barber"))
                                        {
                                            main=new Intent(SplashActivity.this, Home.class);
                                        }
                                        else if(SessionHelper.isUserLoggedIn(SplashActivity.this) && SessionHelper.getCurrentUser(SplashActivity.this).getString("type").equals("customer"))
                                        {
                                            main=new Intent(SplashActivity.this, MainActivity.class);
                                        }
                                        else
                                        {
                                            main=new Intent(SplashActivity.this, LoginActivity.class);
                                        }
                                        startActivity(main);
                                        finish();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                        }
                };splassh.start();
    }

}
