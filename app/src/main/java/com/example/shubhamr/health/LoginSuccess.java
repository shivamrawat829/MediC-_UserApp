package com.example.shubhamr.health;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginSuccess extends AppCompatActivity {



    TextView name,temp,heartbeat,emer,aadhaar;
    Button panic;
    AlertDialog.Builder builder;
    public double latitude = 30.6564;
    public double longitude = 63.4545;
    private String reg_url_store="http://13.126.36.6/panic.php";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_success);
        name = (TextView) findViewById(R.id.name);
        temp = (TextView) findViewById(R.id.temp);
        heartbeat = (TextView) findViewById(R.id.hearbeat);
        emer = (TextView) findViewById(R.id.emer);
        builder=new AlertDialog.Builder(LoginSuccess.this);
        aadhaar =(TextView) findViewById(R.id.aadhaar);


        Bundle bundle=getIntent().getExtras();
        name.setText(bundle.getString("pname"));
        aadhaar.setText(bundle.getString("aadhaar"));
        temp.setText("30");
        heartbeat.setText("40bpm");
        emer = (TextView) findViewById(R.id.emer);


        panic = (Button) findViewById(R.id.panic);

        panic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final String Name = name.getText().toString();
                final String Temp = temp.getText().toString();
                final String Heartbeat = heartbeat.getText().toString();
                final String Emer = emer.getText().toString();
                final String Aadaar = aadhaar.getText().toString();



                        StringRequest stringRequest=new StringRequest(Request.Method.POST, reg_url_store,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        try {
                                            JSONArray jsonArray=new JSONArray(response);
                                            JSONObject jsonObject=jsonArray.getJSONObject(0);
                                            String code=jsonObject.getString("code");
                                            String message=jsonObject.getString("message");
                                            builder.setTitle("server response........");
                                            builder.setMessage(message);

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }


                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        }){
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params=new HashMap<String, String>();

                                params.put("aadhaar", Aadaar);
                                params.put("emergency", Emer);
                                params.put("latitude", String.valueOf(latitude));
                                params.put("longitude", String.valueOf(longitude) );



                                return params;
                            }
                        };
                        MySingleton.getInstance(LoginSuccess.this).addToRequestque(stringRequest);





            }
        });



    }
}
