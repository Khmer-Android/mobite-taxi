package com.angkorcab.model;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

/**
 * Created by phuong on 17-Nov-16.
 */

public class UserMarkerIconRendered extends DefaultClusterRenderer<UserMarker> {
    public UserMarkerIconRendered(Context context, GoogleMap map, ClusterManager<UserMarker> clusterManager) {
        super(context, map, clusterManager);
    }


    @Override
    protected void onBeforeClusterItemRendered(UserMarker item, MarkerOptions markerOptions) {

        markerOptions.icon(item.getIcon());
        markerOptions.title(item.getTitle());
        super.onBeforeClusterItemRendered(item, markerOptions);
    }


}
