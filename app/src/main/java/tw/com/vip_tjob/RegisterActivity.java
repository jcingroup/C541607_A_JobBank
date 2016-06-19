package tw.com.vip_tjob;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends BaseAppCompatActivity {

    private RequestQueue mQueue;
    //private AlertDialog.Builder dialog;
    private Button btn_submit_register;
    private EditText editText_vn_no, editText_last_name, editText_given_name, editText_birthday, editText_email, editText_applicants_password;

    private int mYear, mMonth, mDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_register);

        editText_vn_no = (EditText) findViewById(R.id.vn_no);
        editText_birthday = (EditText) findViewById(R.id.birthday);
        editText_email = (EditText) findViewById(R.id.email);
        editText_last_name = (EditText) findViewById(R.id.last_name);
        editText_given_name = (EditText) findViewById(R.id.given_name);
        editText_applicants_password = (EditText) findViewById(R.id.applicants_password);
        editText_email.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        mQueue = Volley.newRequestQueue(getApplicationContext());

        btn_submit_register = (Button) findViewById(R.id.btn_submit_register);
        btn_submit_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String vn_no = editText_vn_no.getText().toString();
                final String email = editText_email.getText().toString();
                final String last_name = editText_last_name.getText().toString();
                final String given_name = editText_given_name.getText().toString();
                final String applicants_password = editText_applicants_password.getText().toString();

                if(email.isEmpty()){
                    editText_email.setError("欄位需填寫");
                    ShowDialModal("欄位需填寫");
                    return;
                }

                String url = getResources().getString(R.string.api_url) + "/EmployeeLogin/ApplyMember";
                //region 收發資料
                StringRequest reqeust = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Gson gson = new Gson();
                                ResultInfo rlt = gson.fromJson(response, ResultInfo.class);

                                if(!rlt.result){
                                    Toast.makeText(getApplicationContext(), rlt.message, Toast.LENGTH_SHORT).show();
                                }

                                Log.d("Get Response", response);
                            }
                        }, mErrorListener) {

                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("vn_no", vn_no);
                        params.put("email", email);
                        params.put("last_name", last_name);
                        params.put("given_name", given_name);
                        params.put("applicants_password", applicants_password);

                        return params;
                    }

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        //params.put("Authorization", "Bearer " + token);
                        return params;
                    }

                    @Override
                    protected VolleyError parseNetworkError(VolleyError volleyError) {

                        Log.i("執行 parseNetworkError", "有");

                        VolleyError _volleyError;
                        if (volleyError.networkResponse != null && volleyError.networkResponse.data != null) {

                            if (volleyError.networkResponse.statusCode == 400) {
                                String response_text = new String("帳號錯誤");
                                _volleyError = new VolleyError(response_text);
                            } else {
                                String response_text = new String(volleyError.networkResponse.data);
                                Log.i("取得 networkResponse.data", response_text);
                                _volleyError = new VolleyError(response_text);
                            }
                        } else {
                            _volleyError = volleyError;
                        }
                        return _volleyError;
                    }
                };
                mQueue.add(reqeust);
                //endregion
            }
        });

        editText_birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(v);
            }
        });
    }

    public void showDatePickerDialog(View v) {
        final EditText txt = (EditText) v;
        String txt_value = txt.getText().toString();
        // 設定初始日期
        final Calendar c = Calendar.getInstance();

        if (!txt_value.isEmpty()) {
            String[] d = txt_value.split("/");
            mYear = Integer.parseInt(d[0]);
            mMonth = Integer.parseInt(d[1]) - 1;
            mDay = Integer.parseInt(d[2]);
        } else {
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);
        }
        // 跳出日期選擇器
        DatePickerDialog dpd = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        txt.setText(year + "/" + (monthOfYear + 1) + "/" + dayOfMonth);
                        Toast.makeText(getApplicationContext(), year + "-" + (monthOfYear + 1) + "-" + dayOfMonth, Toast.LENGTH_SHORT).show();
                    }
                }, mYear, mMonth, mDay);
        dpd.show();
    }

    //region Response.ErrorListener
    private Response.ErrorListener mErrorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {

            if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                Toast.makeText(getApplicationContext(), getString(R.string.error_network_timeout), Toast.LENGTH_LONG).show();
                return;
            }

            if (error.getMessage() != null) {
                AlertDialog.Builder dialog;
                //Log.i("錯誤訊息:", error.getMessage() + error.networkResponse.statusCode);
                String text = String.format("通訊錯誤: %1$s", error.getMessage());
                dialog = new AlertDialog.Builder(RegisterActivity.this);
                dialog.setTitle("錯誤訊息");
                dialog.setMessage(text);
                dialog.setPositiveButton("確認",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                            }
                        }
                );
                dialog.create().show();

                //Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                return;
            }
            //Toast.makeText(getApplicationContext(), "error statusCode" + error.networkResponse.statusCode, Toast.LENGTH_SHORT).show();
        }
    };
    //endregion
}
