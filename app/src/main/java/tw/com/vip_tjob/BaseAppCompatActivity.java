package tw.com.vip_tjob;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.CookieManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.HttpCookie;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by fusun on 2016/6/19.
 */
public class BaseAppCompatActivity extends AppCompatActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    public static RequestQueue mQueue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mQueue = Volley.newRequestQueue(getApplicationContext());
    }

    public void ShowDialModal(String message) {

        //Log.d("the context",context.toString());

        AlertDialog.Builder dialog = new AlertDialog.Builder(this, R.style.AlertTheme);
        dialog.setTitle("訊息提醒");
        dialog.setMessage(message);
        dialog.setPositiveButton("確認",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                }
        );
        dialog.create().show();
    }

    public static void ShowDialModalS(Context context, String message) {

        //Log.d("the context",context.toString());

        AlertDialog.Builder dialog = new AlertDialog.Builder(context, R.style.AlertTheme);
        dialog.setTitle("訊息提醒");
        dialog.setMessage(message);
        dialog.setPositiveButton("確認",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                }
        );
        dialog.create().show();
    }


    public interface LoginInputListener {
        void onLoginInputComplete(String userName, String token);
    }

    public static class LoginDialogFragment extends DialogFragment implements
            DialogInterface.OnClickListener {
        EditText editText_account;
        EditText editText_password;
        Button btn_login;
        ProgressBar progressBar_login;
        View scrollView_form;

        //region Response.ErrorListener
        public Response.ErrorListener mErrorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(getContext(),
                            getString(R.string.error_network_timeout),
                            Toast.LENGTH_LONG).show();
                    return;
                }

                if (error.getMessage() != null) {
                    String text = String.format("通訊錯誤: %1$s", error.getMessage());
                    Log.i("錯誤訊息:", text + error.networkResponse.statusCode);
                    return;
                }
            }
        };
        //endregion

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            //
            String message = getArguments().getString("message");
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View view = inflater.inflate(R.layout.login_comm, null);

            editText_account = (EditText) view.findViewById(R.id.editText_account);
            editText_password = (EditText) view.findViewById(R.id.editText_password);
            btn_login = (Button) view.findViewById(R.id.btn_submit_login);
            progressBar_login = (ProgressBar) view.findViewById(R.id.progressBar_login);
            scrollView_form = view.findViewById(R.id.scrollView_form);

            CookieHandler.setDefault(new java.net.CookieManager());

            //region Click
            btn_login.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    View focus_view = null;

                    //final String account = editText_account.getText().toString();
                    //final String password = editText_password.getText().toString();

                    final String account = "admin";
                    final String password = "4257386-";


                    if (account.isEmpty()) {
                        editText_account.setError(getResources().getString(R.string.warn_request_account));
                        focus_view = focus_view == null ? editText_account : focus_view;
                    }

                    if (password.isEmpty()) {
                        editText_password.setError(getResources().getString(R.string.warn_request_password));
                        focus_view = focus_view == null ? editText_password : focus_view;
                    }
                    Log.d("focus_view is null?", Boolean.toString(focus_view == null));
                    if (focus_view != null) {
                        focus_view.requestFocus();
                        return;
                    }
                    progressBar_login.setVisibility(View.VISIBLE);
                    scrollView_form.setVisibility(View.GONE);

                    String url = getResources().getString(R.string.api_url) + "/Token";
                    //region Start Call Token
                    StringRequest reqeust = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Gson gson = new Gson();
                                    LoginPass loginPass = gson.fromJson(response, LoginPass.class);

                                    Log.i("the token is ", loginPass.access_token);
                                    LoginInputListener listener = (LoginInputListener) getActivity();
                                    listener.onLoginInputComplete(loginPass.userName, loginPass.access_token);
                                    getDialog().dismiss();
                                    //Toast.makeText(getContext(), p.access_token, Toast.LENGTH_SHORT).show();
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                    Log.i("statusCode", Integer.toString(error.networkResponse.statusCode));
                                    Log.i("訊息", error.getMessage());
                                    String response_text = new String(error.networkResponse.data);
                                    Log.i("錯誤內容", response_text);
                                    if (error.networkResponse.statusCode == 400) {
                                        Gson gson = new Gson();
                                        LoginInvalid loginInvalid = gson.fromJson(response_text, LoginInvalid.class);
                                        Toast.makeText(getContext(), loginInvalid.error_description, Toast.LENGTH_SHORT).show();
                                        //ShowDialModalS(getContext(), loginInvalid.error_description);
                                        progressBar_login.setVisibility(View.GONE);
                                        scrollView_form.setVisibility(View.VISIBLE);
                                    }
                                }
                            }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {

                            Map<String, String> params = new HashMap<String, String>();
                            params.put("grant_type", "password");
                            params.put("userName", account);
                            params.put("password", password);

                            Log.d("Post Parm", account + ":" + password);

                            return params;
                        }

                        @Override
                        protected VolleyError parseNetworkError(VolleyError volleyError) {
                            Log.i("錯誤A", volleyError.toString());
                            Log.i("錯誤B", Boolean.toString(volleyError.networkResponse.data == null));
                            String response_text = new String(volleyError.networkResponse.data);
                            Log.i("字串", new String(response_text));
                            Log.i("字串長度", Integer.toString(response_text.length()));
                            //Toast.makeText(getContext(), response_text, Toast.LENGTH_SHORT).show();

                            VolleyError _volleyError;
                            if (volleyError.networkResponse != null && volleyError.networkResponse.data != null) {
                                if (volleyError.networkResponse.statusCode == 400) {

                                    //Log.i("parseNetworkError", response_text);

                                    Gson gson = new Gson();
                                    LoginInvalid loginInvalid = gson.fromJson(response_text, LoginInvalid.class);
                                    _volleyError = new VolleyError(loginInvalid.error_description) {
                                    };
                                } else {
                                    //String response_text = new String(volleyError.networkResponse.data);
                                    _volleyError = new VolleyError(response_text);
                                }
                            } else {
                                _volleyError = volleyError;
                            }
                            return _volleyError;
                        }

                        @Override
                        protected Response<String> parseNetworkResponse(NetworkResponse response) {

                            try {

                                String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                                String mHeader = response.headers.toString();
                                Log.d("headers", mHeader);
                                Map<String, String> responseHeaders = response.headers;
                                String rawCookies = responseHeaders.get("Set-Cookie");
                                Log.d("raw Cookies", rawCookies);
                                String dataString = new String(response.data);
                                Log.d("response.data", dataString);
                                //cookieManager.se
                                List<HttpCookie> parse = HttpCookie.parse(rawCookies);
                                Log.d("Cookie 數", Integer.toString(parse.size()));
                                //Log.d("Cookie getValue", parse.get(0).getValue());
                                //Log.d("Cookie Context C", parse.get(0).getMaxAge());
                                //Log.d("Cookie getName", parse.get(0).getName());

                                //CookieManager cookieManager;
                                //cookieManager = CookieManager.getInstance();
                                //cookieManager.setAcceptCookie(false);
                                //cookieManager.removeAllCookie();

                                //cookieManager.setCookie(getResources().getString(R.string.api_url), rawCookies);

                                return Response.success(dataString, HttpHeaderParser.parseCacheHeaders(response));
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }

                            return super.parseNetworkResponse(response);
                        }

                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            //params.put("Host", getResources().getString(R.string.api_url));
                            //params.put("Content-Type", "application/json");
                            params.put("Accept-Language", "zh-TW");
                            return params;
                        }

                        @Override
                        public String getBodyContentType()
                        {
                            return "application/json";
                        }
                    };
                    //endregion

                    mQueue.add(reqeust);
                }
            });
            //endregion

            return new AlertDialog.Builder(getActivity())
                    .setView(view)
                    .setTitle("Login")
                    //.setMessage(message)
                    //.setPositiveButton("OK", this)
                    .setNegativeButton("Cancel", this).create();
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case AlertDialog.BUTTON_POSITIVE:
                    break;
                case AlertDialog.BUTTON_NEGATIVE:
                    Toast.makeText(getActivity(), "Cancel", Toast.LENGTH_SHORT)
                            .show();
                    break;
                default:
                    break;
            }
        }
    }


    class LoginInvalid {
        String error;
        String error_description;
    }

    class LoginPass {
        public String access_token;
        public String token_type;
        public String userName;
    }
}
