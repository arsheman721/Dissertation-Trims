package barber.ahmad.com.trims.User;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import barber.ahmad.com.trims.BarbersAdapter;
import barber.ahmad.com.trims.BarbersDetailActivity;
import barber.ahmad.com.trims.R;
import barber.ahmad.com.trims.model.barbersclass;

public class Barbers extends AppCompatActivity {

    RecyclerView rvBarbers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barbers);

        rvBarbers=(RecyclerView)findViewById(R.id.rv_barbers);
        // Get barbers and set it to receycler view
      getAllBarbers();
    }
    public void getAllBarbers()
    {
        final ProgressDialog dialog=new ProgressDialog(Barbers.this);
        dialog.setMessage("Trims");
        dialog.setCancelable(false);
        dialog.show();

        StringRequest apirequest = new StringRequest("http://trimsapp.000webhostapp.com/api/User/alluser.php?type=barber", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                Log.i("Check",response);
                try
                {
                    JSONArray jsonArray = new JSONArray(response);
                    final ArrayList<barbersclass> barberlist = new ArrayList<>();
                    for (int i=0; i<jsonArray.length(); i++)
                    {
                        JSONObject jsonObject=jsonArray.getJSONObject(i);

                        barbersclass barbers = new barbersclass();
                        barbers.userid=jsonObject.getInt("bid");
                        barbers.name=jsonObject.getString("name");
                        barbers.email=jsonObject.getString("email");
                        barbers.imagelink=jsonObject.getString("profilepic");

                        Log.i("Checkit",response);
                        barberlist.add(barbers);
                    }

                    LinearLayoutManager manager = new LinearLayoutManager(Barbers.this, LinearLayoutManager.VERTICAL,false);
                    rvBarbers.setLayoutManager(manager);

                    BarbersAdapter adapter=new BarbersAdapter(barberlist, new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            barbersclass selectedBarber = barberlist.get(position);
                            Intent barberdetailintent = new Intent(Barbers.this, BarbersDetailActivity.class);
                            barberdetailintent.putExtra("name",selectedBarber.name);
                            barberdetailintent.putExtra("email",selectedBarber.email);
                            barberdetailintent.putExtra("userid",selectedBarber.userid);
                            barberdetailintent.putExtra("barberid",selectedBarber.userid);

                            startActivity(barberdetailintent);
                            finish();
                        }
                    });
                    rvBarbers.setAdapter(adapter);

                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                    Toast.makeText(Barbers.this, "Data Parsing Error", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                error.printStackTrace();
                Toast.makeText(Barbers.this, "Volley Error", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue queue=Volley.newRequestQueue(getApplicationContext());
        queue.add(apirequest);
    }
}
