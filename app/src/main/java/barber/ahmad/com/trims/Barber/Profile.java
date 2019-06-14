package barber.ahmad.com.trims.Barber;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONException;

import barber.ahmad.com.trims.R;
import barber.ahmad.com.trims.SessionHelper;

public class Profile extends AppCompatActivity {
    TextView name,email,status,id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        id=(TextView) findViewById(R.id.userid);
        name=(TextView) findViewById(R.id.name);
        email=(TextView) findViewById(R.id.email);
        status=(TextView) findViewById(R.id.status);
        // Get the data of the user from saved session
        try {
            id.setText(SessionHelper.getCurrentUser(Profile.this).getString("id"));
            name.setText(SessionHelper.getCurrentUser(Profile.this).getString("name"));
            email.setText(SessionHelper.getCurrentUser(Profile.this).getString("email"));
            status.setText(SessionHelper.getCurrentUser(Profile.this).getString("ustatus"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
