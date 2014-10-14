package py.fpuna.tesis.qoetest.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import py.fpuna.tesis.qoetest.R;

public class StreamingTestIntroActivity extends Activity {

    private Button startStreamingTestBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_streaming_test_intro);

        startStreamingTestBtn = (Button) findViewById(R.id.startStreamingTestBtn);
        startStreamingTestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(),
                        StreamingTestActivity.class);
                intent.putExtras(getIntent().getExtras());
                startActivity(intent);
            }
        });
    }
}
