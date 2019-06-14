package barber.ahmad.com.trims;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import barber.ahmad.com.trims.Barber.Home;
import ru.dimorinny.floatingtextbutton.FloatingTextButton;

public class SignupActivity extends AppCompatActivity {

    EditText edtName, edtEmail,edtPassword, edtconfPassword;
    TextView tvlogin;
   FloatingTextButton btnSignup;

   RadioGroup rguser;
   RadioButton rbuser;

   String type = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        edtName=findViewById(R.id.edt_name);
        edtEmail=findViewById(R.id.edt_Email);
        edtPassword=findViewById(R.id.edt_Pasword);
        edtconfPassword=findViewById(R.id.edt_confrmPasword);

        rguser=(RadioGroup) findViewById(R.id.rg_type);


            btnSignup=findViewById(R.id.signup_button);
            tvlogin=(TextView)findViewById(R.id.tv_login);
            tvlogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent loginActivity= new Intent(SignupActivity.this,LoginActivity.class);
                    startActivity(loginActivity); }
            });
                    btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                final String email = edtEmail.getText().toString();
                final String name = edtName.getText().toString();
                final String confPassword = edtconfPassword.getText().toString();
                final String password = edtPassword.getText().toString();

                /* validations */
                if (name.isEmpty())
                {
                    edtName.requestFocus();
                    Toast.makeText(SignupActivity.this, "Enter Your Name", Toast.LENGTH_SHORT).show();

                }

                else if (email.isEmpty())
                {
                    edtEmail.requestFocus();
                    Toast.makeText(SignupActivity.this, "Enter Your Email", Toast.LENGTH_SHORT).show();

                }
                else if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"))
                {
                    edtEmail.requestFocus();
                    Toast.makeText(SignupActivity.this, "Enter Valid Email", Toast.LENGTH_SHORT).show();

                }
                else if (password.isEmpty())
                {
                    edtPassword.requestFocus();
                    Toast.makeText(SignupActivity.this, "Enter Password", Toast.LENGTH_SHORT).show();

                }
                else if (password.length() < 8)
                {
                    edtPassword.requestFocus();
                    Toast.makeText(SignupActivity.this, "Passwprd length Should be 8 Characters", Toast.LENGTH_SHORT).show();

                }
                else if (password.length() > 12)
                {
                    edtPassword.requestFocus();
                    Toast.makeText(SignupActivity.this, "Passwprd length Should be 8 Characters", Toast.LENGTH_SHORT).show();

                }
                else if(confPassword.length() != password.length())
                {
                    edtconfPassword.requestFocus();
                    Toast.makeText(SignupActivity.this, "Password Not Matched", Toast.LENGTH_SHORT).show();

                }
                else if(!confPassword.equals(password))
                {
                    edtconfPassword.requestFocus();
                    Toast.makeText(SignupActivity.this, "Password Not Matched2", Toast.LENGTH_SHORT).show();

                }
                else

                {
                    type=getUserType();
                    final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this);
                    progressDialog.setMessage("Trims");
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                    //Send data to the server for sign up the user
                    StringRequest request = new StringRequest(Request.Method.POST, "http://trimsapp.000webhostapp.com/api/User/signup.php", new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            progressDialog.dismiss();
                            Log.e("Signup","OnSucess:"+response);
                            try {
                                JSONObject jObject = new JSONObject(response);
                                boolean status = jObject.getBoolean("status");
                                String message = jObject.getString("message");
                                if (status)
                                {
                                    Toast.makeText(SignupActivity.this, message, Toast.LENGTH_SHORT).show();
                                    Intent intent=null;
                                    if(type.equals("barber"))
                                    {
                                        jObject.put("type","barber");
                                        intent=new Intent(SignupActivity.this, Home.class);
                                    }
                                    else
                                    {
                                        intent=new Intent(SignupActivity.this, MainActivity.class);
                                    }
                                    // Save the Sign up user detail and direct login the next activity depend on the user type
                                    SessionHelper.createLoginSession(SignupActivity.this,jObject.toString());
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(SignupActivity.this, message, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(SignupActivity.this, "Parsing Error", Toast.LENGTH_SHORT).show();
                            }

                       
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            error.printStackTrace();
                            Toast.makeText(SignupActivity.this, "Volley Error", Toast.LENGTH_SHORT).show();
                        }
                    }) 


                    {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("name",name);
                            params.put("password", password);
                            params.put("email", email);
                            params.put("type",type);
                            params.put("profilepic","NULL");
                            params.put("status","active");

                            //profilepic


                            return params;
                        }
                    };
                    RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                    queue.add(request);
                }
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
