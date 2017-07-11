package com.example.shubhamr.health;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

public class Register extends AppCompatActivity {

    Button Regbtn;
    EditText Password,Name,ConfirmPassword,Aadhaar,Emercontno,Emercontname,Bdgroup,Age,Contno;
    String password;
    String name;
    String confirmpassword;
    String aadhaar;
    String emergcontno;
    String emercontname;
    String bdgroup,age;

    String contno;
    AlertDialog.Builder builder;
    String reg_url="http://13.126.36.6/register.php";
    //String reg_url="http://192.168.43.35/register.php";
   // String reg_url="http://10.0.3.2/register.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Regbtn =(Button) findViewById(R.id.regbtn);

        Password=(EditText) findViewById(R.id.regpass1);
        Aadhaar = (EditText) findViewById(R.id.aadhaar);
        Age = (EditText) findViewById(R.id.Age);
        Emercontno = (EditText) findViewById(R.id.emegcontact);
        Emercontname = (EditText) findViewById(R.id.emercontname);
        Contno = (EditText) findViewById(R.id.contact);
        Bdgroup = (EditText) findViewById(R.id.bloodgroup);


        Name=(EditText) findViewById(R.id.regname);

        ConfirmPassword=(EditText) findViewById(R.id.regpass2);
        builder=new AlertDialog.Builder(Register.this);

        Regbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name=Name.getText().toString();
                password = Password.getText().toString();
                confirmpassword = ConfirmPassword.getText().toString();
                aadhaar= Aadhaar.getText().toString();
                emergcontno = Emercontname.getText().toString();
                emercontname = Emercontname.getText().toString();
                age = Age.getText().toString();
                bdgroup=Bdgroup.getText().toString();
                contno = Contno.getText().toString();

                if(name.equals("")||password.equals("")||confirmpassword.equals("")||aadhaar.equals("")||
                        emergcontno.equals("")||emercontname.equals("")||bdgroup.equals("")||name.equals("")||contno.equals(""))
                {
                    builder.setTitle("something went wrong");
                    builder.setMessage("Please fill all the fields");
                    displayAlert("input_error");
                }

                else {
                    if(!(password.equals(confirmpassword)))
                    {
                        builder.setTitle("something went wrong");
                        builder.setMessage("password did't match");
                        displayAlert("input_error");
                    }
                    else
                    {
                        StringRequest stringRequest=new StringRequest(Request.Method.POST, reg_url,
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
                                            displayAlert(code);
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
                                params.put("aadhaar", aadhaar);
                                params.put("name",name );
                                params.put("age", age);
                                params.put("patcontact", contno);
                                params.put("emercontactname", emercontname);
                                params.put("emercontact", emergcontno);
                                params.put("bloodgroup", bdgroup);
                                params.put("password", password);

                                return params;
                            }
                        };
                        MySingleton.getInstance(Register.this).addToRequestque(stringRequest);
                    }
                }
            }
        });




    }
    public void displayAlert(final String code)
    {

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int Which) {

                if(code.equals("input_error"))
                {
                    Password.setText("");
                    ConfirmPassword.setText("");
                }

                else if(code.equals("reg_success")){

                    finish();

                }
                else if(code.equals("reg_failed")){

                    Name.setText("");
                    Password.setText("");
                    ConfirmPassword.setText("");



                }

            }
        });

        AlertDialog alertDialog=builder.create();
        alertDialog.show();

    }
}
