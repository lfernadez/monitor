package py.fpuna.tesis.qoetest.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import py.fpuna.tesis.qoetest.R;
import py.fpuna.tesis.qoetest.model.PerfilUsuario;

public class StreamingTestIntroActivity extends Activity {

    public static final String EXTRA_PERFIL_USUARIO = "EXTRA_PERFIL_USUARIO";
    private Button startStreamingTestBtn;
    private PerfilUsuario perfilUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_streaming_test_intro);

        perfilUsuario = getIntent().getParcelableExtra(EXTRA_PERFIL_USUARIO);

        startStreamingTestBtn = (Button) findViewById(R.id.startStreamingTestBtn);
        startStreamingTestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(),
                        StreamingTestActivity.class);

                startActivity(intent);

            }
        });
    }



}
