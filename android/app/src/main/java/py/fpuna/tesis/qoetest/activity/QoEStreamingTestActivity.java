package py.fpuna.tesis.qoetest.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import py.fpuna.tesis.qoetest.R;

public class QoEStreamingTestActivity extends Activity {
    public static final String EXTRA_TIEMPO_CARGA = "extra_tiempo_carga";
    public static final String EXTRA_DURACION_VIDEO = "extra_duracion_video";
    public static final String EXTRA_TIEMPO_BUFFERING =
            "extra_tiempo_buffering";
    public static final String EXTRA_TIEMPO_TOTAL_REP =
            "extra_tiempo_total_rep";
    private RatingBar tiempoCargaInicialRatingBar;
    private RatingBar bufferingRatingBar;
    private TextView tiempoCargaRBLabel;
    private TextView bufferingRBLabel;
    private Button siguienteBtn;
    private Button atrasBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qoe_streaming_test);

        tiempoCargaInicialRatingBar = (RatingBar) findViewById(R.id.ratingBar_tiempo_carga_video);
        tiempoCargaRBLabel = (TextView) findViewById(R.id.ratingBar_tiempo_carga_video_label);

        tiempoCargaInicialRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                if (v == 5) {
                    tiempoCargaRBLabel.setText("Excelente \n Reproducción " +
                            "instantanea");
                } else if (v == 4) {
                    tiempoCargaRBLabel.setText("Muy Bueno \n Casi " +
                            "imperceptible retardo en el incio de " +
                            "reproducción");
                } else if (v == 3) {
                    tiempoCargaRBLabel.setText("Bueno \n  Retardo perceptible" +
                            " en el inicio de reproducción");
                } else if (v == 2) {
                    tiempoCargaRBLabel.setText("Pobre \n Mucho retardo al " +
                            "inicio de la reproducción");
                } else {
                    tiempoCargaRBLabel.setText("Malo \n Retardo inaceptable " +
                            "en" +
                            " el inicio de la reproducción");
                }
            }
        });

        bufferingRatingBar = (RatingBar) findViewById(R.id.ratingBar_buffering_video);
        bufferingRBLabel = (TextView) findViewById(R.id.ratingBar_buffering_label);
        bufferingRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                if (v == 5) {
                    bufferingRBLabel.setText("Excelente \n Reproducción " +
                            "fluida del video, como si fuera que se viera " +
                            "desde el móvil");
                } else if (v == 4) {
                    bufferingRBLabel.setText("Muy Bueno \n Imperceptibles " +
                            "atascos en la reproducción");
                } else if (v == 3) {
                    bufferingRBLabel.setText("Bueno \n Atascos aceptable en " +
                            "la reproducción del video");
                } else if (v == 2) {
                    bufferingRBLabel.setText("Pobre \n Atascos de larga " +
                            "duración o ráfagas de atascos. Reproducción no " +
                            "muy fluida");
                } else {
                    bufferingRBLabel.setText("Malo \n Atascos inaceptables de" +
                            " larga duración o en grandes cantidades");
                }
            }
        });

        /* Boton Atras */
        atrasBtn = (Button) findViewById(R.id.leftButton);
        atrasBtn.setVisibility(View.INVISIBLE);

        /* Boton Siguiente */
        siguienteBtn = (Button) findViewById(R.id.rightButton);
        siguienteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(),
                        MusicStreamingTestIntro.class);
                intent.putExtras(getIntent().getExtras());
                startActivity(intent);
            }
        });
    }


}
