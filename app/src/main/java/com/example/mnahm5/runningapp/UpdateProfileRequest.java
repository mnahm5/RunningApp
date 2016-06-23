package com.example.mnahm5.runningapp;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class UpdateProfileRequest extends StringRequest{
    private static final String UPDATE_PROFILE_REQUEST = "http://mnahm5.x10host.com/editProfile.php";
    private Map<String, String> params;

    public UpdateProfileRequest(String username, String fullName, String email, int weight, Response.Listener<String> listener) {
        super(Request.Method.POST, UPDATE_PROFILE_REQUEST, listener, null);
        params = new HashMap<>();
        params.put("username", username);
        params.put("fullName", fullName);
        params.put("email", email);
        params.put("weight", weight + "");
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
