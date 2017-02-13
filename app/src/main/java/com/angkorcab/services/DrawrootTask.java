package com.angkorcab.services;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.angkorcab.taxi.R;
import com.angkorcab.model.DriverDetails;
import com.angkorcab.model.UserData;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DrawrootTask extends AsyncTask<String, String, String> {
    private Context context;
    public static boolean flagCompleted = false;
    private GoogleMap googleMap;
    private double source_lat = 0.0;
    private double source_long = 0.0;
    private double dest_lat = 0.0;
    private double dest_long = 0.0;
    UserData userdata;
    String tag = "DrawRootTask";
    private ProgressDialog progressDialog;
    public static double dist, time;
    private Polyline line;
    String distanceText = "";
    String durationText = "";
    private ImageButton bookNowButton;

    public DrawrootTask(Context context, LatLng source, LatLng destination,
                        GoogleMap googleMap, ImageButton bookNowButton) {
        source_lat = source.latitude;
        source_long = source.longitude;
        dest_lat = destination.latitude;
        dest_long = destination.longitude;
        this.googleMap = googleMap;
        this.context = context;
        this.bookNowButton=bookNowButton;
        userdata = UserData.getinstance(context);
    }

    protected void onPreExecute() {
        // // TODO Auto-generated method stub
        super.onPreExecute();
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(context.getResources().getString(
                R.string.drawing_route));
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected String doInBackground(String... params) {
        String json = "";
        // constructor
        StringBuilder urlString = new StringBuilder();
        urlString.append("https://maps.googleapis.com/maps/api/directions/json");
        HashMap<String, String> keyValue = new HashMap<String, String>();
        urlString.append("?origin=");// from
        urlString.append(Double.toString(source_lat));
        urlString.append(",");
        urlString.append(Double.toString(source_long));
        urlString.append("&destination=");// to
        urlString.append(Double.toString(dest_lat));
        urlString.append(",");
        urlString.append(Double.toString(dest_long));
        urlString.append("&sensor=false&mode=driving&alternatives=true");
        // defaultHttpClient
        String url = urlString.toString();
        FetchUrl fetchurl = new FetchUrl();
        json = fetchurl.fetchUrl(url, keyValue);
        Log.e("Buffer Error", json);
        return json;
    }

    @Override
    protected void onPostExecute(String result) {
        // TODO Auto-generated method stub
        super.onPostExecute(result);
        try {
            progressDialog.dismiss();
            bookNowButton.setVisibility(View.VISIBLE);
            final JSONObject json = new JSONObject(result);
            JSONArray routeArray = json.getJSONArray("routes");
            JSONObject routes = routeArray.getJSONObject(0);
            JSONObject overviewPolylines = routes
                    .getJSONObject("overview_polyline");
            String encodedString = overviewPolylines.getString("points");
            List<LatLng> list = decodePoly(encodedString);
            for (int z = 0; z < list.size() - 1; z++) {
                LatLng src = list.get(z);
                LatLng dest = list.get(z + 1);
                line = googleMap.addPolyline(new PolylineOptions()
                        .add(new LatLng(src.latitude, src.longitude),
                                new LatLng(dest.latitude, dest.longitude))
                        // .width(8).color(Color.BLUE).geodesic(true));
                        .width(3)
                        .color(context.getResources().getColor(R.color.colorDrawRouteLine)).geodesic(true));
                Log.i("draw root", "" + "" + line.toString());
            }
            JSONArray legs = routes.getJSONArray("legs");
            JSONObject steps = legs.getJSONObject(0);
            JSONObject duration = steps.getJSONObject("duration");
            JSONObject distance = steps.getJSONObject("distance");
            distanceText = distance.getString("text");
            durationText = duration.getString("text");
            Log.i("draw root", "" + distance.toString());
            dist = Double.parseDouble(distance.getString("text").replaceAll(
                    "[^\\.0123456789]", ""));
            time = Double.parseDouble(duration.getString("text").replaceAll(
                    "[^\\.0123456789]", ""));
            userdata.setDistance(distanceText);
            userdata.setTime(durationText);
            DriverDetails.setTravelTime(durationText);
            Log.d(tag, "distace is " + distanceText + " time is " + durationText);
            flagCompleted = true;
        } catch (JSONException e) {
            Log.d("draw root", "" + e);
        }
    }

    private List<LatLng> decodePoly(String encoded) {
        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;
        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;
            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;
            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }
        return poly;
    }
}
