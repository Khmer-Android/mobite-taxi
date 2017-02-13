package com.angkorcab.services;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.webkit.WebView;

import com.angkorcab.constants.ProjectURls;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class UpdatePaymentDetails extends AsyncTask<String, String, String> {
    Context context;
    JSONObject js;
    String table_id;
    WebView rides_webview;
    private String response = "";
    String tag = "PaymentDetailsAsyntask";
    String payment_id;

    public UpdatePaymentDetails(Context context, WebView rides_webview, String payment_id) {
        this.context = context;
        this.payment_id=payment_id;
      //  this.js = js;
        this.rides_webview = rides_webview;
       // this.table_id = table_id;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
       // if (js.has("response")) {

               /* transaction_id = js.getJSONObject("response").getString("id");
                transaction_state = js.getJSONObject("response").getString(
                        "state");*/
        try {
            String url = ProjectURls.Update_Payment_Details_URL;
            HashMap<String, String> keyValue = new HashMap<String, String>();
            keyValue.put("payment_id",payment_id);
            keyValue.put("trans_id", "7890");
            keyValue.put("trans_state", "demosuccess");
            FetchUrl fetchUrl = new FetchUrl();
            response = fetchUrl.fetchUrl(url, keyValue);
            Log.d(tag, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //}
        return response;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        try {
            if (new JSONObject(result).has("success")
                    && new JSONObject(result).getString("success").equals("1")) {
                rides_webview.loadUrl(ProjectURls.PAYMENT_SUCCESS_URL);
            } else {
                rides_webview.loadUrl(ProjectURls.PAYMENT_FAIL_URL);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
