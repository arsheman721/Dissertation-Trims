package barber.ahmad.com.trims;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import barber.ahmad.com.trims.Barber.Home;
import barber.ahmad.com.trims.service.ServiceHandler;
import ru.dimorinny.floatingtextbutton.FloatingTextButton;

public class LoginActivity extends AppCompatActivity {
TextView tvSignup;

EditText edtEmail, edtPassword;

FloatingTextButton btnLogin;

    RadioGroup rguser;
    String logintype="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Get View Reference
        edtEmail=(EditText)findViewById(R.id.edt_Email);
        edtPassword=(EditText)findViewById(R.id.edt_Pasword);
        btnLogin=(FloatingTextButton)findViewById(R.id.login_button);
        rguser=(RadioGroup) findViewById(R.id.rg_type);
        tvSignup=(TextView)findViewById(R.id.tv_Signup);
        // Set Click Listeners
        tvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signupintent=new Intent(LoginActivity.this,SignupActivity.class);
                startActivity(signupintent);
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {

                final String email = edtEmail.getText().toString().trim();
                final String password = edtPassword.getText().toString();

                if(email.isEmpty())
                {
                    Toast.makeText(LoginActivity.this, "Please Enter Email", Toast.LENGTH_LONG).show();
                    edtEmail.requestFocus();
                }

                else if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"))
                {
                    edtEmail.requestFocus();
                }
                else if (password.isEmpty())
                {
                    Toast.makeText(LoginActivity.this, "Please Enter Your Password", Toast.LENGTH_SHORT).show();
                    edtPassword.requestFocus();
                }

                else if(password.length() < 5)
                {

                    edtPassword.requestFocus();
                    Toast.makeText(LoginActivity.this, "Proceed with Login", Toast.LENGTH_SHORT).show();
                }

                else if (password.length() > 12)
                {
                    edtPassword.requestFocus();
                }


                else
                {
                    // Get login type is Customer or Barber
                    logintype=getUserType();
                    loginRequest2(email,password,logintype);

                }
            }
        });
    }
    // Send login Request to Server with email and password and type
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void loginRequest2(final String email, final String password, final String logintype)
    {
        final ProgressDialog pDialog = new ProgressDialog(LoginActivity.this);
        pDialog.setMessage("Trims");
        pDialog.setCancelable(false);
        pDialog.show();
        ServiceHandler handler = new ServiceHandler();
        handler.objectRequest("http://trimsapp.000webhostapp.com/api/User/login.php?email="+email+"&password="+password+"&type="+logintype+" ", Request.Method.GET,
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
                                // If status false show why login is not sucessfull
                                // if status true then check the type of the user and open that activity
                                if (!status)
                                {
                                    // Status false
                                    Log.i("mytag",message);
                                    Toast.makeText(LoginActivity.this,message,Toast.LENGTH_SHORT).show();

                                }
                                else
                                {
                                    // Status true
                                    Toast.makeText(LoginActivity.this, message,Toast.LENGTH_LONG).show();
                                    Intent intent=null;
                                    // check type and open activity Home for barber and Mainactivity for customer
                                    if(logintype.equals("barber"))
                                    {
                                        response.put("type","barber");
                                        intent=new Intent(LoginActivity.this, Home.class);
                                    }
                                    else
                                    {
                                        intent=new Intent(LoginActivity.this, MainActivity.class);
                                    }
                                    // Save the session when user comeback they donot want to login again
                                    SessionHelper.createLoginSession(LoginActivity.this,response);
                                    startActivity(intent);

                                    finish(); // Close current Open Activity


                                }
                            }
                            catch (JSONException e)
                            {
                                e.printStackTrace();

                            }
                        }
                        else{
                            Toast.makeText(LoginActivity.this, "Response :"+response,Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Response", error.getMessage());
                        Toast.makeText(LoginActivity.this,"Response:"+ error.getMessage(),Toast.LENGTH_LONG).show();
                        if(pDialog.isShowing())
                            pDialog.dismiss();
                    }
                });

    }
   //  Get the user type which Option selected by the user in radio button
    public String getUserType()
    {
        String type="";
        int selectedId=rguser.getCheckedRadioButtonId();
        if(((RadioButton)findViewById(selectedId)).getText().toString().equals("I am Customer"))
        {
            type="customer";
        }
       else
        {
            type="barber";
        }
        Log.e("Customer Type",type);
        return type;
    }
}
