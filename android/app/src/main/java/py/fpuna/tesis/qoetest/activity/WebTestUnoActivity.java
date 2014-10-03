package py.fpuna.tesis.qoetest.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.drive.internal.i;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import py.fpuna.tesis.qoetest.R;
import py.fpuna.tesis.qoetest.model.PerfilUsuario;
import py.fpuna.tesis.qoetest.utils.Constants;

/**
 *
 */
public class WebTestUnoActivity extends Activity {

    public static final String EXTRA_TCARGA_UNO = "EXTRA_TCARGA_UNO";
    private Button siguienteBtn;
    private Button atrasBtn;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_web_test_uno);

        webView = (WebView) this.findViewById(R.id.webView);
        webView.clearCache(true);
        final WebClientTest webClient = new WebClientTest();
        webView.setWebViewClient(webClient);
        webView.loadUrl(Constants.ABC_URL);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

        /* Boton siguiente */
        siguienteBtn = (Button) findViewById(R.id.rightButton);
        siguienteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(),
                        WebTestDosActivity.class);
                intent.putExtras(getIntent().getExtras());
                intent.putExtra(EXTRA_TCARGA_UNO,
                        webClient.getLoadTime());
                startActivity(intent);
            }
        });

        /* Boton Atras */
        atrasBtn = (Button) findViewById(R.id.leftButton);
        atrasBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     *
     */
    private class WebClientTest extends WebViewClient {
        private long loadTime; // Tiempo de carga de la pagina web


        /**
         *
         * @return
         */
        public long getLoadTime(){
            return this.loadTime;
        }


        /**
         *
         * @param view
         * @param url
         * @param favicon
         */
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);

            // Save start time
            this.loadTime = System.currentTimeMillis();
            setProgressBarIndeterminateVisibility(true);

            // Show a toast
            Toast.makeText(getApplicationContext(),
                    "Se ha comenzado a cargar la página...",
                    Toast.LENGTH_SHORT).show();
        }

        /**
         *
         * @param view
         * @param url
         */
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            // Calculate load time
            this.loadTime = System.currentTimeMillis() - this.loadTime;
            setProgressBarIndeterminateVisibility(false);

            // Convert milliseconds to date format
            String time = new SimpleDateFormat("mm:ss:SSS", Locale.getDefault())
                    .format(new Date(this.loadTime));

            // Show a toast
            Toast.makeText(getApplicationContext(),
                    "La carga de la página ha finalizado en " + time,
                    Toast.LENGTH_SHORT)
                    .show();

        }
    }
}
