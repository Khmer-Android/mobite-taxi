package com.angkorcab.cluster;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by phuong on 21-Nov-16.
 */

public class Passenger implements ClusterItem {

    public final String name;
    public final int profilePhoto;
    private final LatLng mPosition;

    public Passenger(LatLng position, String name, int pictureResource) {
        this.name = name;
        profilePhoto = pictureResource;
        mPosition = position;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }


}
