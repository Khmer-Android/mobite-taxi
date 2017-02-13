package com.angkorcab.utils;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.angkorcab.taxi.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by phuong on 10/27/2016.
 */
public class GoogleUtil {

    private Context context;



    public GoogleUtil(Context context) {
        this.context = context;
    }



    public String getAddress(String lat, String lon) {

        

        if(lat==null || lon==null)return "Can't get Address!";

        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        String ret = "";
        try {
            List<Address> addresses = geocoder.getFromLocation(Double.parseDouble(lat), Double.parseDouble(lon), 1);

            if (addresses != null && addresses.size() > 0 && !addresses.isEmpty()) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");
                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress
                            .append(returnedAddress.getAddressLine(i)).append(
                            " ");
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
