package com.angkorcab.services;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.widget.Toast;

import com.angkorcab.taxi.MainActivity;
import com.angkorcab.taxi.R;
import com.angkorcab.constants.ProjectURls;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class GetDriverLocationService extends AsyncTask<String, String, String> {
    Context context;
    String name;
    HashMap<String, String> values = new HashMap<String, String>();
    private GoogleMap googleMap;
    private ProgressDialog dialog;
    private MarkerOptions marker;

    public GetDriverLocationService(Context context, GoogleMap googleMap) {
        super();
        this.context = context;
        this.googleMap = googleMap;
    }

    @Override
    protected void onPreExecute() {
        dialog = ProgressDialog.show(context, null, "Loading ...", true, false);
    }

    @Override
    protected String doInBackground(String... params) {
        FetchUrl fetchUrl = new FetchUrl();
        name = fetchUrl.fetchUrl(ProjectURls.TRACK_DRIVER_URL + params[0], values);
        return name;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        dialog.dismiss();
        try {
            JSONObject res = new JSONObject(result);
            JSONObject responce = res.getJSONObject("response");
            String resultStr = responce.getString("result");
            if (resultStr.equalsIgnoreCase("1")) {
                double lat = Double.parseDouble(responce.getString("lat"));
                double longt = Double.parseDouble(responce.getString("long"));
                String address = getAddress("" + lat, "" + longt);
                address = address.replaceAll("\n", "");
                Marker marker = googleMap.addMarker(new MarkerOptions()
                        .position(
                                new LatLng(lat, longt))
                        .title(address)
                        .icon(BitmapDescriptorFactory
                                .fromResource(R.drawable.marker))
                );
                marker.showInfoWindow();
                googleMap.animateCamera(CameraUpdateFactory
                        .newLatLngZoom((new LatLng(lat, longt)), 12));
            } else if (resultStr.equalsIgnoreCase("3")) {
                Intent intent = new Intent(context, MainActivity.class);
                context.startActivity(intent);
                Toast.makeText(context, "Unable to get location",
                        Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public String getAddress(String lat, String lon) {
        Geocoder geocoder = new Geocoder(context);
        String ret = "";
        try {
            List<Address> addresses = geocoder.getFromLocation(
                    Double.parseDouble(lat), Double.parseDouble(lon), 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");
                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress
                            .append(returnedAddress.getAddressLine(i)).append(
                            "\n");
                }
                ret = strReturnedAddress.toString();
            } else {
                ret = "No Address returned!";
            }
        } catch (IOException e) {
            e.printStackTrace();
            ret = "Can't get Address!";
        }
        return ret;
    }
}
