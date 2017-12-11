package com.android.jianchen.rentme.Task;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by emerald on 6/7/2017.
 */
public class APIStringRequester extends Request<String> {
    private Response.Listener<String> listener;
    private Map<String, String> params;

    public APIStringRequester(String url, Map<String, String> params, Response.Listener<String> responseListener, Response.ErrorListener errorListener) {
        super(Method.GET, url, errorListener);
        this.listener = responseListener;
        this.params = params;
    }

    public APIStringRequester(int method, String url, Map<String, String> params, Response.Listener<String> reponseListener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.listener = reponseListener;
        this.params = params;
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));

            return Response.success(jsonString, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
        return params;
    };

    @Override
    protected void deliverResponse(String response) {
        listener.onResponse(response);
    }
}
