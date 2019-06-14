package barber.ahmad.com.trims.Barber;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

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

import barber.ahmad.com.trims.Adapter.appointmentAdapter;
import barber.ahmad.com.trims.LoginActivity;
import barber.ahmad.com.trims.MainActivity;
import barber.ahmad.com.trims.R;
import barber.ahmad.com.trims.SessionHelper;
import barber.ahmad.com.trims.User.AppointmentDetail;
import barber.ahmad.com.trims.User.Appointments;
import barber.ahmad.com.trims.model.appointmentclass;
import barber.ahmad.com.trims.service.ServiceHandler;
import barber.ahmad.com.trims.utils.ActionListner;

public class checkBooking extends AppCompatActivity implements ActionListner {
    RecyclerView rvBarbers;
    ActionListner listner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_booking);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().hide();
        rvBarbers=(RecyclerView)findViewById(R.id.rv_barbers);

        // Get barbers and set it to receycler view
        listner=this;
        try {
            getAppointments(SessionHelper.getCurrentUser(checkBooking.this).getString("type"),SessionHelper.getCurrentUser(checkBooking.this).getString("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    // Get Appointment Detail from the server by sending User userid and type (Barber)
    public void getAppointments(String type,String id)
    {
        final ProgressDialog dialog=new ProgressDialog(checkBooking.this);
        dialog.setTitle("Fetching Data");
        dialog.setMessage("Getting Appointments details....");
        dialog.setCancelable(false);
        dialog.show();

        StringRequest apirequest = new StringRequest("http://trimsapp.000webhostapp.com/api/User/getallappointment.php?type="+type+"&id="+id+" ", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                Log.i("Check",response);
                try
                {
                    JSONObject jsonObject2=new JSONObject (response);
                    if(jsonObject2.getBoolean("status"))
                    {
                        JSONArray jsonArray = jsonObject2.getJSONArray("data");
                        final ArrayList<appointmentclass> appointmentlist = new ArrayList<>();
                        for (int i=0; i<jsonArray.length(); i++)
                        {
                            JSONObject jsonObject=jsonArray.getJSONObject(i);
                            appointmentclass appointment= new appointmentclass();
                            appointment.appid=jsonObject.getInt("appid");
                            appointment.customerid=jsonObject.getInt("customerid");
                            appointment.barberid=jsonObject.getInt("barberid");
                            appointment.cname=jsonObject.getString("cname");
                            appointment.bname=jsonObject.getString("bname");
                            appointment.status=jsonObject.getString("status");
                            appointment.appdate=jsonObject.getString("appdate");

                            Log.i("Checkit",response);
                            appointmentlist.add(appointment);
                        }

                        LinearLayoutManager manager = new LinearLayoutManager(checkBooking.this, LinearLayoutManager.VERTICAL,false);
                        rvBarbers.setLayoutManager(manager);

                        appointmentAdapter adapter=new appointmentAdapter(appointmentlist, new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                appointmentclass appointment = appointmentlist.get(position);
                                Intent barberdetailintent = new Intent(checkBooking.this, AppointmentDetail.class);
                                barberdetailintent.putExtra("appid",appointment.appid);
                                barberdetailintent.putExtra("customerid",appointment.customerid);
                                barberdetailintent.putExtra("barberid",appointment.barberid);
                                barberdetailintent.putExtra("cname",appointment.cname);
                                barberdetailintent.putExtra("bname",appointment.bname);
                                barberdetailintent.putExtra("status",appointment.status);
                                barberdetailintent.putExtra("appdate",appointment.appdate);

                                startActivity(barberdetailintent);
                            }
                        },listner,getApplicationContext(),"barber");
                        rvBarbers.setAdapter(adapter);
                    }
                    else
                    {
                        Toast.makeText(checkBooking.this,jsonObject2.getString("message"),Toast.LENGTH_LONG).show();
                        findViewById(R.id.tv_notfound).setVisibility(View.VISIBLE);
                    }


                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                    Toast.makeText(checkBooking.this, "Data Parsing Error", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                error.printStackTrace();
                Toast.makeText(checkBooking.this, "Volley Error", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue queue= Volley.newRequestQueue(getApplicationContext());
        queue.add(apirequest);
    }
// if any data change by the user this function get the new updated record and set it in the list
    @Override
    public void updateAppointment(int appid, String status) {
        try {
            setAppointmentStatus(appid,status);
            getAppointments(SessionHelper.getCurrentUser(checkBooking.this).getString("type"),SessionHelper.getCurrentUser(checkBooking.this).getString("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    // Change the status of the Appointment when Accept or Reject
    public void setAppointmentStatus(int appid,String status)
    {
        final ProgressDialog pDialog = new ProgressDialog(checkBooking.this);
        pDialog.setTitle("Update Data");
        pDialog.setMessage("Updating Appointment Status.....");
        pDialog.setCancelable(false);
        pDialog.show();
        ServiceHandler handler = new ServiceHandler();
        handler.objectRequest("http://trimsapp.000webhostapp.com/api/User/updateappointment.php?appid="+appid+"&status="+status+"", Request.Method.GET,
                null, JSONObject.class, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", response.toString());
                        if(pDialog.isShowing())
                            pDialog.dismiss();
                        if(response!=null){

                            try
                            {
                                JSONObject jsonObject=response;
                                boolean status= jsonObject.getBoolean("status");
                                String message=jsonObject.getString("message");

                                if (!status)
                                {
                                    Log.i("mytag",message);
                                    Toast.makeText(checkBooking.this,message,Toast.LENGTH_SHORT).show();

                                }
                                else
                                {
                                    Toast.makeText(checkBooking.this, message,Toast.LENGTH_LONG).show();
                                    try {
                                        getAppointments(SessionHelper.getCurrentUser(checkBooking.this).getString("type"),SessionHelper.getCurrentUser(checkBooking.this).getString("id"));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            catch (JSONException e)
                            {
                                e.printStackTrace();

                            }
                        }
                        else{
                            Toast.makeText(checkBooking.this, "Response :"+response,Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Response", error.getMessage());
                        Toast.makeText(checkBooking.this,"Response:"+ error.getMessage(),Toast.LENGTH_LONG).show();
                        if(pDialog.isShowing())
                            pDialog.dismiss();
                    }
                });
    }
}
