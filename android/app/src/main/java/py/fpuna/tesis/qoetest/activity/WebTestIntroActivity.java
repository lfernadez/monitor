package py.fpuna.tesis.qoetest.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import py.fpuna.tesis.qoetest.R;
import py.fpuna.tesis.qoetest.model.PerfilUsuario;
import py.fpuna.tesis.qoetest.utils.PreferenceUtils;

public class WebTestIntroActivity extends ActionBarActivity {

    public static final String EXTRA_PERFIL_USUARIO = "EXTRA_PERFIL_USUARIO";
    private Button startWebTestBtn;
    private PerfilUsuario perfilUsuario;
    private PreferenceUtils preferenceUtils;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_test_intro);
        preferenceUtils = new PreferenceUtils(this);

        startWebTestBtn = (Button) findViewById(R.id.startWebTestBtn);
        startWebTestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(),
                        WebTestUnoActivity.class);
                Bundle extras = getIntent().getExtras();
                if (extras == null) {
                    extras = new Bundle();
                    perfilUsuario = preferenceUtils.getPerfilUsuario();
                    extras.putParcelable(EXTRA_PERFIL_USUARIO, perfilUsuario);
                }
                intent.putExtras(extras);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
