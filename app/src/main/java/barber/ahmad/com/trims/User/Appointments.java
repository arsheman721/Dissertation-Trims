package barber.ahmad.com.trims.User;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import barber.ahmad.com.trims.Adapter.appointmentAdapter;
import barber.ahmad.com.trims.BarbersAdapter;
import barber.ahmad.com.trims.BarbersDetailActivity;
import barber.ahmad.com.trims.R;
import barber.ahmad.com.trims.SessionHelper;
import barber.ahmad.com.trims.model.appointmentclass;
import barber.ahmad.com.trims.model.barbersclass;

public class Appointments extends AppCompatActivity {
    RecyclerView rvBarbers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointments);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().hide();
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent barbers=new Intent(Appointments.this, Barbers.class);
                startActivity(barbers);
            }
        });
        rvBarbers=(RecyclerView)findViewById(R.id.rv_barbers);
        // Get barbers and set it to receycler view
        try {
            getAppointments(SessionHelper.getCurrentUser(Appointments.this).getString("type"),SessionHelper.getCurrentUser(Appointments.this).getString("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void getAppointments(String type,String id)
    {
        final ProgressDialog dialog=new ProgressDialog(Appointments.this);
        dialog.setMessage("Getting Appointments");
        dialog.setCancelable(false);
        dialog.show();

        StringRequest apirequest = new StringRequest("http://trimsapp.000webhostapp.com/api/User/getallappointment.php?type="+type+"&id="+id+" ", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                Log.i("Check",response);
                try
                {
                    JSONObject  jsonObject2=new JSONObject (response);
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

                        LinearLayoutManager manager = new LinearLayoutManager(Appointments.this, LinearLayoutManager.VERTICAL,false);
                        rvBarbers.setLayoutManager(manager);

                        appointmentAdapter adapter=new appointmentAdapter(appointmentlist, new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                appointmentclass appointment = appointmentlist.get(position);
                                Intent barberdetailintent = new Intent(Appointments.this, AppointmentDetail.class);
                                barberdetailintent.putExtra("appid",appointment.appid);
                                barberdetailintent.putExtra("customerid",appointment.customerid);
                                barberdetailintent.putExtra("barberid",appointment.barberid);
                                barberdetailintent.putExtra("cname",appointment.cname);
                                barberdetailintent.putExtra("bname",appointment.bname);
                                barberdetailintent.putExtra("status",appointment.status);
                                barberdetailintent.putExtra("appdate",appointment.appdate);

                                startActivity(barberdetailintent);
                            }
                        },getApplicationContext(),"user");
                        rvBarbers.setAdapter(adapter);
                    }
                    else
                    {
                        Toast.makeText(Appointments.this,jsonObject2.getString("message"),Toast.LENGTH_LONG).show();
                        findViewById(R.id.tv_notfound).setVisibility(View.VISIBLE);
                    }


                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                    Toast.makeText(Appointments.this, "Data Parsing Error", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                error.printStackTrace();
                Toast.makeText(Appointments.this, "Volley Error", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue queue= Volley.newRequestQueue(getApplicationContext());
        queue.add(apirequest);
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            getAppointments(SessionHelper.getCurrentUser(Appointments.this).getString("type"),SessionHelper.getCurrentUser(Appointments.this).getString("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
