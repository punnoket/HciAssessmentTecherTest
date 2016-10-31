package com.example.maaster.itp939juniorproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;

public class WebRegActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_reg);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        WebView webView = (WebView) findViewById(R.id.web_id);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("https://web.reg.tu.ac.th/registrar/login.asp");
        getSupportActionBar().setTitle("ITPPROJECT");


    }

    public void openRegister(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }

}
