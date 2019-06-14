package barber.ahmad.com.trims;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;

import barber.ahmad.com.trims.Barber.Home;
import barber.ahmad.com.trims.Barber.Profile;
import barber.ahmad.com.trims.User.Appointments;
import barber.ahmad.com.trims.User.Barbers;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Button btnLogout, btnAppointment, btnProfile;
    TextView tvname;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);





        //3 User Buttons (Logout and Profile and Appointment)
btnProfile=(Button)findViewById(R.id.btn_profile);
btnAppointment=(Button)findViewById(R.id.btn_appointment);
btnLogout=(Button)findViewById(R.id.btn_logout);
        tvname=(TextView) findViewById(R.id.name);
        try {
            tvname.setText(SessionHelper.getCurrentUser(MainActivity.this).getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


btnProfile.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent barbers=new Intent(MainActivity.this, Profile.class);
        startActivity(barbers);
    }
});


btnAppointment.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent barbers=new Intent(MainActivity.this, Appointments.class);
        startActivity(barbers);
    }
});
btnLogout.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        SessionHelper.logout(MainActivity.this);
        Intent barbers=new Intent(MainActivity.this,LoginActivity.class);
        startActivity(barbers);
        finish();
    }
});
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
