package py.fpuna.tesis.qoetest.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import py.fpuna.tesis.qoetest.R;
import py.fpuna.tesis.qoetest.model.QoSParam;
import py.fpuna.tesis.qoetest.utils.Constants;
import py.fpuna.tesis.qoetest.utils.DateHourUtils;

/**
 *
 */
public class WebTestUnoActivity extends ActionBarActivity {

    public static final String EXTRA_TCARGA_UNO = "EXTRA_TCARGA_UNO";
    private Button siguienteBtn;
    private Button atrasBtn;
    private WebView webView;
    private ProgressBar progressBar;
    private boolean cargaFinalizada;
    private Long endTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_test_uno);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        cargaFinalizada = false;

        webView = (WebView) this.findViewById(R.id.webView);
        webView.clearCache(true);
        final WebClientTest webClient = new WebClientTest();
        webView.setWebViewClient(webClient);
        webView.loadUrl(Constants.CODING_LOVE_URL);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                progressBar.setProgress(newProgress);
            }
        });

        /* Boton siguiente */
        siguienteBtn = (Button) findViewById(R.id.rightButton);
        siguienteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(),
                        QoEWebTestActivity.class);

                Bundle extras = getIntent().getExtras();
                ArrayList<QoSParam> parametrosQoS = extras
                        .getParcelableArrayList(Constants.EXTRA_PARAM_QOS);

                /* Cancelado */
                QoSParam canceladoWebParam = new QoSParam();
                canceladoWebParam.setObtenido(Constants.OBT_TEL);
                canceladoWebParam.setCodigoParametro(Constants.CANCELADO_ID);

                /* Carga Inicial Video */
                QoSParam cargaWebParam = new QoSParam();
                cargaWebParam.setObtenido(Constants.OBT_TEL);
                cargaWebParam.setCodigoParametro(Constants.TIEMPO_CARGA_WEB);


                if(!cargaFinalizada){
                    // Se ha cancelado la carga de la página
                    Long loadingTime = endTime - webClient.getLoadTime();
                    cargaWebParam.setValor(DateHourUtils.toSeconds(loadingTime));
                    canceladoWebParam.setValor(1.0);
                }else {
                    // Finalización normal de la carga de la página
                    webView.stopLoading();
                    cargaWebParam.setValor(DateHourUtils.toSeconds(webClient.getLoadTime()));
                    canceladoWebParam.setValor(0.0);
                }

                parametrosQoS.add(cargaWebParam);
                parametrosQoS.add(canceladoWebParam);
                extras.putParcelableArrayList(Constants.EXTRA_PARAM_QOS,
                        parametrosQoS);
                intent.putExtras(extras);

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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.web_test_uno, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_cancel:
                endTime = System.currentTimeMillis();
                cargaFinalizada = false;
                progressBar.setVisibility(View.GONE);
                webView.stopLoading();
                Toast.makeText(getApplicationContext(),
                        "La carga de la página ha sido cancelada",
                        Toast.LENGTH_SHORT)
                        .show();
                return true;
            default:
                return true;
        }
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
         * @return
         */
        public long getLoadTime() {
            return this.loadTime;
        }


        /**
         * @param view
         * @param url
         * @param favicon
         */
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);

            // Save start time
            this.loadTime = System.currentTimeMillis();
            progressBar.setVisibility(View.VISIBLE);

            // Show a toast
            Toast.makeText(getApplicationContext(),
                    "Se ha comenzado a cargar la página...",
                    Toast.LENGTH_SHORT).show();
        }

        /**
         * @param view
         * @param url
         */
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            // Calculate load time
            this.loadTime = System.currentTimeMillis() - this.loadTime;
            progressBar.setVisibility(View.GONE);
            // Convert milliseconds to date format
            String time = new SimpleDateFormat("mm:ss:SSS", Locale.getDefault())
                    .format(new Date(this.loadTime));
            cargaFinalizada = true;
            //MenuItem item = (MenuItem) findViewById(R.id.action_cancel);
            //item.setVisible(false);

            // Show a toast
            Toast.makeText(getApplicationContext(),
                    "La carga de la página ha finalizado en " + time,
                    Toast.LENGTH_SHORT)
                    .show();
        }
    }
}
