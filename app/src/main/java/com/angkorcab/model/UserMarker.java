package com.angkorcab.model;

import com.angkorcab.taxi.R;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by phuong on 17-Nov-16.
 */

public class UserMarker  implements ClusterItem{

    private final LatLng mPosition;
    private BitmapDescriptor icon;
    private String title;

    public UserMarker(double lat, double lng) {
        mPosition = new LatLng(lat, lng);
    }




    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    public BitmapDescriptor getIcon() {
        return icon;
    }

    public void setIcon(BitmapDescriptor icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
