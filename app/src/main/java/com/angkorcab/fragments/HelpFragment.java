package com.angkorcab.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.angkorcab.taxi.MainActivity;
import com.angkorcab.taxi.R;
import com.angkorcab.constants.ProjectURls;
import com.angkorcab.helper.MyWebViewClient;
import com.angkorcab.utils.GPSTracker;

public class HelpFragment extends BaseFragment {
    private WebView help_webview;
    private Context context;
    GPSTracker gpsTracker;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.web_view_layout, null);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.item_five));
        context = getActivity();
        try {
            help_webview = (WebView) root.findViewById(R.id.web_view);
            MyWebViewClient.enableWebViewSettings(help_webview);
            help_webview.setWebViewClient(new MyWebViewClient(context));
            help_webview.loadUrl(ProjectURls.HELP_PROJECT_URL);
            gpsTracker=new GPSTracker(context);
            //gpsTracker.stopUsingGPS();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return root;
    }

    @Override
    protected void backPressed() {
        try {
            context.startActivity(new Intent(context, MainActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
