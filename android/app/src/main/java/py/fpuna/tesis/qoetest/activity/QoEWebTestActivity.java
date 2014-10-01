package py.fpuna.tesis.qoetest.activity;

import android.app.Activity;
import android.content.Intent;
import android.media.Rating;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import py.fpuna.tesis.qoetest.R;
import py.fpuna.tesis.qoetest.model.PerfilUsuario;

public class QoEWebTestActivity extends Activity {

    public static final String EXTRA_QOE_RATING_UNO = "EXTRA_QOE_RATING_UNO";
    public static final String EXTRA_QOE_RATING_DOS = "EXTRA_QOE_RATING_DOS";

    private RatingBar velocQoERatingBar;
    private RatingBar calidadRatingBar;
    private TextView ratingBarVelocLabel;
    private TextView ratingBarCalidadLabel;
    private Button atrasBtn;
    private Button siguienteBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qoe_web_test);

        velocQoERatingBar = (RatingBar) findViewById(R.id.ratingBarVeloc);
        ratingBarVelocLabel = (TextView) findViewById(R.id.ratingBarVeloc_label);

        velocQoERatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                if (v == 5){
                    ratingBarVelocLabel.setText("Excelente");
                }else if(v == 4){
                    ratingBarVelocLabel.setText("Muy Bueno");
                }else if(v == 3) {
                    ratingBarVelocLabel.setText("Bueno");
                }else if(v == 2){
                    ratingBarVelocLabel.setText("Pobre");
                }else{
                    ratingBarVelocLabel.setText("Malo");
                }
            }
        });

        calidadRatingBar = (RatingBar) findViewById(R.id.ratingBarCalidad);
        ratingBarCalidadLabel = (TextView) findViewById(R.id.ratingBarCalidad_label);
        calidadRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                if (v == 5){
                    ratingBarCalidadLabel.setText("Excelente \n Todos los " +
                            "contenidos se visualizaron perfectamente");
                }else if(v == 4){
                    ratingBarCalidadLabel.setText("Muy Bueno \n Pequeñas " +
                            "obstrucciones en la visualización del contenido");
                }else if(v == 3) {
                    ratingBarCalidadLabel.setText("Bueno \n Retardo en la " +
                            "visualización de ciertos contenidos");
                }else if(v == 2){
                    ratingBarCalidadLabel.setText("Pobre \n Algunos contenidos " +
                            "no se muestran");
                }else{
                    ratingBarCalidadLabel.setText("Malo \n Varios contenidos no" +
                            " se muestra");
                }
            }
        });


        /* Boton atras */
        atrasBtn = (Button) findViewById(R.id.leftButton);
        atrasBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        /* Botono Siguiente */
        siguienteBtn = (Button) findViewById(R.id.rightButton);
        siguienteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(verificar()) {
                    Intent intent = new Intent(getBaseContext(),
                            StreamingTestIntroActivity.class);
                    intent.putExtras(getIntent().getExtras());
                    intent.putExtra(EXTRA_QOE_RATING_UNO,
                            velocQoERatingBar.getRating());
                    intent.putExtra(EXTRA_QOE_RATING_DOS,
                            calidadRatingBar.getRating());
                    startActivity(intent);
                }
            }
        });
    }

    public boolean verificar(){
        if(velocQoERatingBar.getRating() == 0){
            Toast.makeText(getBaseContext(), "Seleccione una de las " +
                    "estrellas de acuerdo a su evaluación ",
                    Toast.LENGTH_LONG).show();
            return false;
        }
        if(calidadRatingBar.getRating() == 0){
            Toast.makeText(getBaseContext(), "Seleccione una de las " +
                    "estrellas de acuerdo a su evaluación ",
                    Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}
