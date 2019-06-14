package barber.ahmad.com.trims.User;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.w3c.dom.Text;

import barber.ahmad.com.trims.R;

public class AppointmentDetail extends AppCompatActivity {
    TextView appid,customername,barbername,status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_detail);
        appid=(TextView) findViewById(R.id.appid);
        customername=(TextView) findViewById(R.id.tv_customername);
        barbername=(TextView) findViewById(R.id.tv_barber_name);
        status=(TextView) findViewById(R.id.status);
        Log.e("Appid", String.valueOf(getIntent().getIntExtra("appid",0)));
        appid.setText(""+getIntent().getIntExtra("appid",0));
        customername.setText(getIntent().getStringExtra("cname"));
        barbername.setText(getIntent().getStringExtra("bname"));
        status.setText(getIntent().getStringExtra("status"));
    }
}
