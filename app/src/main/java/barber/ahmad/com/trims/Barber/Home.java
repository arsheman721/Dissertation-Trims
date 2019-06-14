package barber.ahmad.com.trims.Barber;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;

import barber.ahmad.com.trims.User.Barbers;
import barber.ahmad.com.trims.LoginActivity;
import barber.ahmad.com.trims.R;
import barber.ahmad.com.trims.SessionHelper;

public class Home extends AppCompatActivity {
    Button btnLogout, btnAppointment, btnShop,btnProfile;
    TextView tvname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        btnProfile=(Button)findViewById(R.id.btn_profile);
        btnShop=(Button)findViewById(R.id.btn_shop);
        btnAppointment=(Button)findViewById(R.id.btn_appointment);
        btnLogout=(Button)findViewById(R.id.btn_logout);
        tvname=(TextView) findViewById(R.id.name);
        try {
            tvname.setText(SessionHelper.getCurrentUser(Home.this).getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this,Profile.class));
            }
        });


        btnAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent barbers=new Intent(Home.this, checkBooking.class);
                startActivity(barbers);
            }
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SessionHelper.logout(Home.this);
                Intent barbers=new Intent(Home.this, LoginActivity.class);
                startActivity(barbers);
                finish();
            }
        });
    }
}
