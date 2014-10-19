package py.fpuna.tesis.qoetest.activity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import py.fpuna.tesis.qoetest.R;
import py.fpuna.tesis.qoetest.utils.VideoUtils;

public class StreamingTestActivity extends ActionBarActivity
        implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener, SeekBar.OnSeekBarChangeListener,
        MediaPlayer.OnCompletionListener {

    String vidAddress = "https://archive.org/download/ksnn_compilation_master_the_internet/ksnn_compilation_master_the_internet_512kb.mp4";
    private MediaPlayer mediaPlayer;
    private SeekBar videoProgressBar;
    private VideoView videoView;
    private TextView posicionActualVideoLabel;
    private TextView totalVideoLabel;
    private VideoUtils videoUtils;
    private Handler mHandler = new Handler();
    private ProgressDialog loadingProgress;
    private Button siguienteBtn;
    private long duracionVideo;
    private long inicioCargando;
    private long inicioTotal;
    private long finCargando;
    private long finTotal;
    /**
     * Background Runnable thread
     */
    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            long totalDuration = videoView.getDuration();
            long currentDuration = videoView.getCurrentPosition();

            // Displaying Total Duration time
            totalVideoLabel.setText("" + videoUtils.milliSecondsToTimer
                    (totalDuration));
            // Displaying time completed playing
            posicionActualVideoLabel.setText("" + videoUtils.milliSecondsToTimer
                    (currentDuration));

            // Updating progress bar
            int progress = (int) (videoUtils.getProgressPercentage(currentDuration,
                    totalDuration));
            //Log.d("Progress", ""+progress);
            videoProgressBar.setProgress(progress);
            Log.d(this.getClass().getName(), "percent played: " + progress);

            // Running this thread after 100 milliseconds
            mHandler.postDelayed(this, 100);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_streaming_test);

        /* VideoView */
        videoView = (VideoView) findViewById(R.id.streamingView);
        videoView.setOnErrorListener(this);
        videoView.setOnPreparedListener(this);
        videoView.setOnCompletionListener(this);
        videoView.setVideoURI(Uri.parse(vidAddress));
        inicioCargando = System.currentTimeMillis();
        videoView.start();

        /* SeekBar Video */
        videoProgressBar = (SeekBar) findViewById(R.id.videoProgressBar);
        videoProgressBar.setOnSeekBarChangeListener(this);

        /* Video utils */
        videoUtils = new VideoUtils();

        /* ProgressBar */
        loadingProgress = new ProgressDialog(this);
        loadingProgress.setMessage("Cargando...");
        loadingProgress.setCanceledOnTouchOutside(false);
        loadingProgress.show();

        /* Posicion Actual Label */
        posicionActualVideoLabel = (TextView) findViewById(R.id
                .duracionActualLabel);
        /* Duracion total Label */
        totalVideoLabel = (TextView) findViewById(R.id.duracionTotalLabel);

        /* Boton Siguiente */
        siguienteBtn = (Button) findViewById(R.id.rightButton);
        siguienteBtn.setVisibility(View.INVISIBLE);
        siguienteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long tiempoBuffering = (finTotal - inicioTotal) - duracionVideo;

                String tiempoCarga = videoUtils.milliSecondsToTimer(finCargando -
                        inicioCargando);
                String duracionVideoString = videoUtils.milliSecondsToTimer
                        (duracionVideo);
                String tiempoTotalRep = videoUtils.milliSecondsToTimer(finTotal -
                        inicioTotal);
                String tiempoBufferingString =
                        videoUtils.milliSecondsToTimer(tiempoBuffering);

                Intent intent = new Intent(StreamingTestActivity.this,
                        QoEStreamingTestActivity
                                .class);
                /* Se agregan los extras anteriores */
                intent.putExtras(getIntent().getExtras());
                /* Nuevos extras */
                intent.putExtra(QoEStreamingTestActivity.EXTRA_TIEMPO_CARGA,
                        tiempoCarga);
                intent.putExtra(QoEStreamingTestActivity
                        .EXTRA_DURACION_VIDEO, duracionVideoString);
                intent.putExtra(QoEStreamingTestActivity
                        .EXTRA_TIEMPO_TOTAL_REP, tiempoTotalRep);
                intent.putExtra(QoEStreamingTestActivity
                        .EXTRA_TIEMPO_BUFFERING, tiempoBufferingString);

                Log.d("StreamingTestActivity", "Tiempo de carga inicial: " +
                        tiempoCarga);
                Log.d("StreamingTestActivity", "Duracion video: " +
                        duracionVideoString);
                Log.d("StreamingTestActivity", "Tiempo reproducción total: " +
                        tiempoTotalRep);
                Log.d("StreamingTestActivity", "Tiempo Buffering: " +
                        tiempoBufferingString);

                startActivity(intent);
            }
        });

    }

    /**
     * @param mediaPlayer
     */
    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        finCargando = System.currentTimeMillis();
        inicioTotal = System.currentTimeMillis();
        Log.d(this.getClass().getName(), "prepared");
        loadingProgress.dismiss();
        videoProgressBar.setProgress(0);
        videoProgressBar.setMax(100);
        updateProgressBar();

        mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mediaPlayer, int percent) {
                Log.d(this.getClass().getName(), "percent: " + percent);
                videoProgressBar.setSecondaryProgress(percent);
            }
        });

    }

    /**
     * Update timer on seekbar
     */
    public void updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }

    /**
     * @param mediaPlayer
     * @param i
     * @param i2
     * @return
     */
    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i2) {
        if (loadingProgress.isShowing()) {
            loadingProgress.dismiss();
        }
        return true;
    }

    /**
     * @param mediaPlayer
     */
    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        duracionVideo = mediaPlayer.getDuration();
        finTotal = System.currentTimeMillis();
        siguienteBtn.setVisibility(View.VISIBLE);
        mHandler.removeCallbacks(mUpdateTimeTask);
    }

    /**
     * @param seekBar
     * @param i
     * @param b
     */
    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

    }

    /**
     * @param seekBar
     */
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    /**
     * @param seekBar
     */
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

}
