package com.example.shubhamr.health;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    Button login_button;
    EditText password,adharlogin;
    String Password,AdharLogin;
   String login_url = "http://13.126.36.6/login.php";
    private static final String TAG = "MyActivity";
    //String login_url = "http://10.0.3.2/login.php";
    AlertDialog.Builder builder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.reg_txt);
        login_button=(Button) findViewById(R.id.bn_login);

        password=(EditText)findViewById(R.id.login_password);
        adharlogin = (EditText) findViewById(R.id.aadhaar_login);


        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Register.class);
                startActivity(intent);
            }
        });

        builder = new AlertDialog.Builder(MainActivity.this);

       login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v(TAG, "index");
                AdharLogin = adharlogin.getText().toString();


                Password = password.getText().toString();
                if (adharlogin.equals("")||password.equals(""))
                {
                    builder.setTitle("Something went wrong");
                    displayAlert("Enter a valid username or password");


                }
                else
                {

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, login_url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {

                                    try {
                                        JSONArray jsonArray = new JSONArray(response);
                                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                                        String code = jsonObject.getString("code");
                                        if (code.equals("login failed"))
                                        {
                                            String message=jsonObject.getString("message");

                                            displayAlert(message);

                                            Toast.makeText(getApplicationContext(), "something went wrong",Toast.LENGTH_LONG).show();
                                        }
                                        else
                                        {
                                            Toast.makeText(getApplicationContext(), "login successfull", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(MainActivity.this, LoginSuccess.class);
                                            Bundle bundle = new Bundle();

                                            bundle.putString("pname",jsonObject.getString("pname"));
                                            bundle.putString("aadhaar",jsonObject.getString("aadhaar"));

                                        
                                            intent.putExtras(bundle);
                                            startActivity(intent);
                                        }


                                    } catch (JSONException e) {
                                        e.printStackTrace();

                                    }

                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Toast.makeText(MainActivity.this, "ERROR",Toast.LENGTH_LONG).show();
                            error.printStackTrace();


                        }
                    })
                    {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("aadhaar",AdharLogin);
                            params.put("password",Password);
                            return params;
                        }
                    };
                    MySingleton.getInstance(MainActivity.this).addToRequestque(stringRequest);
                }
            }
        });




    }
    public void displayAlert(String message)
    {
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                adharlogin.setText("");
                password.setText("");
            }
        });

          AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
