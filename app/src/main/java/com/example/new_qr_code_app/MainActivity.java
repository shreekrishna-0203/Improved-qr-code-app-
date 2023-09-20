package com.example.new_qr_code_app;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qrcode_app_for_internshala.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {
    private Button scan_btn;
    private TextView textView;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scan_btn = findViewById(R.id.scanner);
        textView = findViewById(R.id.text);
        webView = findViewById(R.id.webView);

        webView.setVisibility(View.GONE);

        scan_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new IntentIntegrator(MainActivity.this)
                        .setOrientationLocked(true)
                        .setPrompt("Scan a QR Code")
                        .initiateScan();
            }
        });

        webView.setWebViewClient(new WebViewClient());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null) {
            if (intentResult.getContents() != null) {
                String contents = intentResult.getContents();
                textView.setText(contents);

                if (isURL(contents)) {
                    // It's a URL
                    webView.setVisibility(View.VISIBLE);
                    webView.loadUrl(contents);
                } else if (isUPI(contents)) {
                    // It's a UPI QR Code
                    webView.setVisibility(View.GONE);
                    Toast.makeText(this, "UPI QR Code: " + contents, Toast.LENGTH_SHORT).show();
                    openUPIApp(contents);
                } else if (contents.startsWith("WIFI:")) {
                    // It's a Wi-Fi network QR code
                    webView.setVisibility(View.GONE);
                    Toast.makeText(this, "Wi-Fi Network QR Code", Toast.LENGTH_SHORT).show();
                    openWifiSettings(contents);
                } else if (contents.toLowerCase().startsWith("market://")) {
                    // It's a Play Store link
                    webView.setVisibility(View.GONE);
                    Toast.makeText(this, "Opening Play Store", Toast.LENGTH_SHORT).show();
                    openPlayStore(contents);
                } else if (contents.toLowerCase().startsWith("http:") || contents.toLowerCase().startsWith("https:")) {
                    // It's a web URL
                    webView.setVisibility(View.VISIBLE);
                    webView.loadUrl(contents);
                } else if (contents.toLowerCase().startsWith("youtube.com/")) {
                    // It's a YouTube link
                    webView.setVisibility(View.GONE);
                    Toast.makeText(this, "Opening YouTube", Toast.LENGTH_SHORT).show();
                    openYouTube(contents);
                } else {
                    // It's not recognized as any specific type
                    webView.setVisibility(View.GONE);
                    Toast.makeText(this, "Unknown QR Code Type", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Scan canceled", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean isURL(String contents) {
        return false;
    }

    // Check if a string is a valid UPI ID or UPI QR Code
    private boolean isUPI(String text) {
        return text != null && (text.toLowerCase().startsWith("upi:") || text.toLowerCase().startsWith("upi://"));
    }

    // Open the UPI app with the UPI ID
    private void openUPIApp(String upiId) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("upi://pay?pa=" + upiId));
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "Failed to open UPI app", Toast.LENGTH_SHORT).show();
        }
    }

    // Open Wi-Fi settings
    private void openWifiSettings(String wifiConfig) {
        try {
            Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "Failed to open Wi-Fi settings", Toast.LENGTH_SHORT).show();
        }
    }

    // Open Play Store with a given link
    private void openPlayStore(String playStoreLink) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(playStoreLink));
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "Failed to open Play Store", Toast.LENGTH_SHORT).show();
        }
    }

    // Open YouTube with a given link
    private void openYouTube(String youtubeLink) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(youtubeLink));
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "Failed to open YouTube", Toast.LENGTH_SHORT).show();
        }
    }
    // Open Wi-Fi settings
    private void openWifiSettings() {
        try {
            Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "Failed to open Wi-Fi settings", Toast.LENGTH_SHORT).show();
        }
    }
    private void openWebPage(String url) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "Failed to open web page", Toast.LENGTH_SHORT).show();
        }
    }


}