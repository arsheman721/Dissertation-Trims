package barber.ahmad.com.trims;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import barber.ahmad.com.trims.model.barbersclass;
import barber.ahmad.com.trims.service.ServiceHandler;
import ru.dimorinny.floatingtextbutton.FloatingTextButton;

public class BarbersDetailActivity extends AppCompatActivity {

    ImageView ivBarberDetail;
    TextView Barbername;
    TextView BarberEmail;

    CheckBox item1,item2,item3,item4,item5,item6,item7,item8;
    ArrayList<String> chkbxResults;

    FloatingTextButton btnBookAppointment;
    int barberid,userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Receiving the Intent
        Bundle barberDetBundle=getIntent().getExtras();

        final String name=barberDetBundle.getString("name");
        String email=barberDetBundle.getString("email");

        //To be Sent For Appointment
        barberid=barberDetBundle.getInt("barberid");
        userid=barberDetBundle.getInt("userid");


        setContentView(R.layout.activity_barbers_detail);


        ivBarberDetail=(ImageView)findViewById(R.id.iv_barb_detail);


        Barbername=(TextView)findViewById(R.id.tvbarberdetail_name);
        Barbername.setText(name);


        BarberEmail=(TextView)findViewById(R.id.tvbarberdetail_email);
        BarberEmail.setText(email);

        btnBookAppointment=(FloatingTextButton)findViewById(R.id.appointment_button);
        item1=(CheckBox)findViewById(R.id.item_1);
        item2=(CheckBox)findViewById(R.id.item_2);
        item3=(CheckBox)findViewById(R.id.item_3);
        item4=(CheckBox)findViewById(R.id.item_4);
        item5=(CheckBox)findViewById(R.id.item_5);
        item6=(CheckBox)findViewById(R.id.item_6);
        item7=(CheckBox)findViewById(R.id.item_7);
        item8=(CheckBox)findViewById(R.id.item_8);

        chkbxResults= new ArrayList<>();


        btnBookAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (item1.isChecked())
                {
                    chkbxResults.add("Beard Trim");
                }
                else if (item2.isChecked())
                {
                    chkbxResults.add("Hair Cut and Wash");
                }
                else if (item3.isChecked())
                {
                    chkbxResults.add("Haircut (any Fade)");
                }
                else if (item4.isChecked())
                {
                    chkbxResults.add("Long Hair");
                }
                else if (item5.isChecked())
                {
                    chkbxResults.add("Shave Up");
                }
                else if (item6.isChecked())
                {
                    chkbxResults.add("Shave");
                }
                else if (item7.isChecked())
                {
                    chkbxResults.add("Hair Color");
                }
                else
                {
                    chkbxResults.add("Face Massage");
                }
                try {
                    sendAppointment();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    // Send Appointment to the Api
    public void sendAppointment() throws JSONException {
        final ProgressDialog progressDialog = new ProgressDialog(BarbersDetailActivity.this);
        progressDialog.setMessage("Trims");
        progressDialog.setCancelable(false);
        progressDialog.show();
        // Post request
        ServiceHandler handler = new ServiceHandler();
        Map<String, Object> params = new ArrayMap<String, Object>();
        try {
            params.put("cid", String.valueOf(SessionHelper.getCurrentUser(BarbersDetailActivity.this).getString("id")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        params.put("bid", String.valueOf(barberid));
        params.put("Array", String.valueOf(chkbxResults));

        handler.objectRequest("http://trimsapp.000webhostapp.com/api/User/addappointment.php?cid="+SessionHelper.getCurrentUser(BarbersDetailActivity.this).getString("id")+"&bid="+barberid+" ", Request.Method.GET,
                null, JSONObject.class, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", response.toString());
                        progressDialog.dismiss();
                        try {
                            if(response.getBoolean("status"))
                            {
                                Toast.makeText(BarbersDetailActivity.this,response.getString("message")+" \n Your Appoiontment ID :"+response.getString("id"),Toast.LENGTH_LONG).show();
                                finish();
                            }
                            else
                            {
                                Toast.makeText(BarbersDetailActivity.this,response.getString("message")+" \n Try Again ",Toast.LENGTH_LONG).show();
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Response", error.getMessage());
                        error.printStackTrace();
                        progressDialog.dismiss();
                    }
                });
    }
}
