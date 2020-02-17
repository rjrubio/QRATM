package com.b2c.qratm;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class WidrawActivity extends AppCompatActivity implements View.OnClickListener {

    EditText inSubject, inBody;
    TextView txtEmailAddress;
    Button btnSendEmail;
    private RequestQueue requestQueue = null;
    private Context context = null;
    String pin = "";
    String api = "";
    String hash= "";
    ProgressDialog progress = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widraw);
        initViews();
        this.context = this;
        this.requestQueue = Volley.newRequestQueue(this);
        progress = new ProgressDialog(context);
        progress.setTitle("Validating");
        progress.setMessage("Please wait...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
    }

    private void initViews() {
        inSubject = findViewById(R.id.inSubject);
        txtEmailAddress = findViewById(R.id.txtEmailAddress);
        btnSendEmail = findViewById(R.id.btnSendEmail);

        if (getIntent().getStringExtra("email_address") != null) {
            txtEmailAddress.setText("Hash : " + getIntent().getStringExtra("email_address"));
            hash = getIntent().getStringExtra("email_address");
        }

        btnSendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               pin = inSubject.getText().toString();
               progress.show();
               validateAndDoWidraw(api, hash);

            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnScanBarcode:
                startActivity(new Intent(WidrawActivity.this, ScannedBarcodeActivity.class));
                break;
        }
    }

    public void getCloudData(String endPoint , final VolleyCallback callback) {
        Log.d("endpoint",endPoint);

        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, endPoint, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        callback.onResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onErrorResponse(error);
                        Log.d("getCloudDBData", error.toString());
                    }
                }
    );
        // add it to the RequestQueue
        requestQueue.add(getRequest);
    }

    public void validateAndDoWidraw(String endpoint, String hashData) {
        getCloudData(endpoint+"atm/hash/"+hashData, new VolleyCallback() {
            @Override
            public void onResponse(JSONObject result) {
                try {
                    String pinRes = result.getString("pin");
                    int amount = result.getInt("amount");
                    String responseDescription = result.getString("description");
                    if(pinRes == pin){
                        //do widraw
                    }
                    else{
                        //prompt error
                    }

                } catch (JSONException ex) {
                    Log.e("checkConnection", ex.toString());
                }
            }
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("checkConnection", error.toString());
            }
        });
    }
}
