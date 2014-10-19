package py.fpuna.tesis.qoetest.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

import py.fpuna.tesis.qoetest.R;
import py.fpuna.tesis.qoetest.model.PruebaTest;
import py.fpuna.tesis.qoetest.utils.CalcUtils;
import py.fpuna.tesis.qoetest.utils.VideoTestMessages;

public class QoEStreamingTestActivity extends ActionBarActivity {

    public static final String EXTRA_TIEMPO_CARGA = "extra_tiempo_carga";
    public static final String EXTRA_DURACION_VIDEO = "extra_duracion_video";
    public static final String EXTRA_TIEMPO_BUFFERING =
            "extra_tiempo_buffering";
    public static final String EXTRA_TIEMPO_TOTAL_REP =
            "extra_tiempo_total_rep";

    public static final String EXTRA_LISTA_PRUEBA = "EXTRA_LISTA_TEST";

    private RatingBar tiempoCargaInicialRatingBar;
    private RatingBar bufferingRatingBar;
    private TextView tiempoCargaRBLabel;
    private TextView bufferingRBLabel;
    private Button siguienteBtn;
    private Button atrasBtn;

    private ArrayList<PruebaTest> pruebas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qoe_streaming_test);

        tiempoCargaInicialRatingBar = (RatingBar) findViewById(R.id.ratingBar_tiempo_carga_video);
        tiempoCargaRBLabel = (TextView) findViewById(R.id.ratingBar_tiempo_carga_video_label);

        pruebas = getIntent().getParcelableArrayListExtra(EXTRA_LISTA_PRUEBA);

        tiempoCargaInicialRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                if (v == 5) {
                    tiempoCargaRBLabel.setText(VideoTestMessages.EXCELENT_ITIME_QOE);
                } else if (v == 4) {
                    tiempoCargaRBLabel.setText(VideoTestMessages
                            .VERY_GOOG_ITIME_QOE);
                } else if (v == 3) {
                    tiempoCargaRBLabel.setText(VideoTestMessages.GOOD_ITIME_QOE);
                } else if (v == 2) {
                    tiempoCargaRBLabel.setText(VideoTestMessages.POOR_ITIME_QOE);
                } else {
                    tiempoCargaRBLabel.setText(VideoTestMessages.BAD_ITIME_QOE);
                }
            }
        });

        bufferingRatingBar = (RatingBar) findViewById(R.id.ratingBar_buffering_video);
        bufferingRBLabel = (TextView) findViewById(R.id.ratingBar_buffering_label);
        bufferingRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                if (v == 5) {
                    bufferingRBLabel.setText(VideoTestMessages.EXCELENT_DELAY_QOE);
                } else if (v == 4) {
                    bufferingRBLabel.setText(VideoTestMessages.VERY_GOOG_DELAY_QOE);
                } else if (v == 3) {
                    bufferingRBLabel.setText(VideoTestMessages.GOOD_DELAY_QOE);
                } else if (v == 2) {
                    bufferingRBLabel.setText(VideoTestMessages.POOR_DELAY_QOE);
                } else {
                    bufferingRBLabel.setText(VideoTestMessages.BAD_DELAY_QOE);
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
                        EnviarTestActivity.class);
                PruebaTest pruebaStreaming = new PruebaTest();
                pruebaStreaming.setCodigoTest(2);
                pruebaStreaming.setValorMos(
                        CalcUtils.getPromedio(
                                tiempoCargaInicialRatingBar.getRating(),
                                bufferingRatingBar.getRating()));
                pruebas.add(pruebaStreaming);
                intent.putParcelableArrayListExtra(EXTRA_LISTA_PRUEBA, pruebas);
                intent.putExtras(getIntent().getExtras());
                startActivity(intent);
            }
        });
    }


}
