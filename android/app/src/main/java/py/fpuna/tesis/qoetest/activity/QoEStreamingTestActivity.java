package py.fpuna.tesis.qoetest.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import py.fpuna.tesis.qoetest.R;
import py.fpuna.tesis.qoetest.model.PruebaTest;
import py.fpuna.tesis.qoetest.utils.CalcUtils;
import py.fpuna.tesis.qoetest.utils.Constants;
import py.fpuna.tesis.qoetest.utils.VideoTestMessages;

public class QoEStreamingTestActivity extends ActionBarActivity {

    private RatingBar tiempoCargaInicialRatingBar;
    private RatingBar bufferingRatingBar;
    private TextView tiempoCargaRBLabel;
    private TextView bufferingRBLabel;
    private Button siguienteBtn;
    private Button atrasBtn;

    private RatingBar calidadRatingBar;
    private TextView calidadRBLabel;
    private RatingBar globalRatingBar;



    private ArrayList<PruebaTest> pruebas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qoe_streaming_test);



        tiempoCargaInicialRatingBar = (RatingBar) findViewById(R.id.ratingBar_tiempo_carga_video);
        tiempoCargaRBLabel = (TextView) findViewById(R.id.ratingBar_tiempo_carga_video_label);

        pruebas = getIntent().getParcelableArrayListExtra(Constants.EXTRA_QOE_TEST);

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

        calidadRatingBar = (RatingBar) findViewById(R.id.ratingBar_calidad_video);
        calidadRBLabel = (TextView) findViewById(R.id.ratingBar_calidad_label);
        calidadRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                if (v == 5) {
                    calidadRBLabel.setText(VideoTestMessages.EXCELENT_QUALITY_QOE);
                } else if (v == 4) {
                    calidadRBLabel.setText(VideoTestMessages.VERY_GOOD_QUALITY_QOE);
                } else if (v == 3) {
                    calidadRBLabel.setText(VideoTestMessages.GOOD_QUALITY_QOE);
                } else if (v == 2) {
                    calidadRBLabel.setText(VideoTestMessages.POOR_QUALITY_QOE);
                } else {
                    calidadRBLabel.setText(VideoTestMessages.BAD_QUALITY_QOE);
                }
            }
        });


        globalRatingBar = (RatingBar) findViewById(R.id.ratingBar_globla_video);

        /* Boton Atras */
        atrasBtn = (Button) findViewById(R.id.leftButton);
        atrasBtn.setVisibility(View.INVISIBLE);

        /* Boton Siguiente */
        siguienteBtn = (Button) findViewById(R.id.rightButton);
        siguienteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (verificar()) {
                    Intent intent = new Intent(getBaseContext(),
                            EnviarTestActivity.class);
                    PruebaTest pruebaStreaming = new PruebaTest();
                    pruebaStreaming.setCodigoTest(Constants.TEST_STREAMING_UNO);
                    pruebaStreaming.setValorMos(
                            CalcUtils.getPromedio(
                                    tiempoCargaInicialRatingBar.getRating(),
                                    bufferingRatingBar.getRating(),
                                    calidadRatingBar.getRating(),
                                    globalRatingBar.getRating()));
                    pruebas.add(pruebaStreaming);
                    intent.putParcelableArrayListExtra(Constants.EXTRA_QOE_TEST, pruebas);
                    intent.putExtras(getIntent().getExtras());
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    /**
     *
     * @return
     */
    public boolean verificar() {
        if (tiempoCargaInicialRatingBar.getRating() == 0) {
            Toast.makeText(getBaseContext(), "Seleccione una de las " +
                            "estrellas de acuerdo a su evaluación ",
                    Toast.LENGTH_LONG).show();
            return false;
        }
        if (calidadRatingBar.getRating() == 0) {
            Toast.makeText(getBaseContext(), "Seleccione una de las " +
                            "estrellas de acuerdo a su evaluación ",
                    Toast.LENGTH_LONG).show();
            return false;
        }
        if (bufferingRatingBar.getRating() == 0) {
            Toast.makeText(getBaseContext(), "Seleccione una de las " +
                            "estrellas de acuerdo a su evaluación ",
                    Toast.LENGTH_LONG).show();
            return false;
        }
        if (globalRatingBar.getRating() == 0) {
            Toast.makeText(getBaseContext(), "Seleccione una de las " +
                            "estrellas de acuerdo a su evaluación ",
                    Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }


}
