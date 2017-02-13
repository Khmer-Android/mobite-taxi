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

public class CabMoneyFragment extends BaseFragment {
    private Context context;
    WebView cabMoneywebview;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_cab_money, null);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getResources().getString(R.string.item_three));
        context = getActivity();
        try {
            cabMoneywebview = (WebView) root.findViewById(R.id.cabmoney_webview);
            MyWebViewClient.enableWebViewSettings(cabMoneywebview);
            cabMoneywebview.setWebViewClient(new MyWebViewClient(context));
            cabMoneywebview.loadUrl(ProjectURls.CAB_MONEY_URL);
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
