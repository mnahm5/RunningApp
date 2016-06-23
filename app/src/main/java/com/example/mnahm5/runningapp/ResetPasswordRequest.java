package com.example.mnahm5.runningapp;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ResetPasswordRequest extends StringRequest {
    private static final String RESET_PASSWORD_URL = "http://mnahm5.x10host.com/resetPassword.php";
    private Map<String, String> params;

    public ResetPasswordRequest(String username, String oldPassword, String newPassword, Response.Listener<String> listener) {
        super(Method.POST, RESET_PASSWORD_URL, listener, null);
        params = new HashMap<>();
        params.put("username", username);
        params.put("oldPassword", oldPassword);
        params.put("newPassword", newPassword);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
