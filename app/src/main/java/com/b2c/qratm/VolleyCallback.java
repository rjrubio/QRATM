package com.b2c.qratm;

import com.android.volley.VolleyError;

import org.json.JSONObject;

public interface VolleyCallback {
    void onResponse(JSONObject result);
    void onErrorResponse(VolleyError error);
}
