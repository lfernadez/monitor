package py.fpuna.tesis.qoetest.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import py.fpuna.tesis.qoetest.R;

public class EnviarTestActivity extends Activity {
    private Button enviarTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enviar_test);

        Bundle bundle = getIntent().getExtras();

        enviarTest = (Button) findViewById(R.id.startWebTestBtn);
        enviarTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    public class EnviarResultados extends AsyncTask<Void, Void,
            Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            return null;
        }
    }
}
