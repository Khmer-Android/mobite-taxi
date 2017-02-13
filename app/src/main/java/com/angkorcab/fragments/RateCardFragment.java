package com.angkorcab.fragments;

import android.app.ProgressDialog;
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

public class RateCardFragment extends BaseFragment {
    private Context context;
    WebView webview;
    ProgressDialog dialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.web_view_layout, null);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.taxi));
        webview = (WebView) root.findViewById(R.id.web_view);
        context = getActivity();
        try {
            setWebViewClient(context, webview);
            webview.loadUrl(ProjectURls.RATE_CARD_PROJECT_URL);
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
