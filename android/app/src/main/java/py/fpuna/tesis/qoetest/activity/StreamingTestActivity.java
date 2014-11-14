package py.fpuna.tesis.qoetest.activity;


import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import java.util.ArrayList;

import py.fpuna.tesis.qoetest.R;
import py.fpuna.tesis.qoetest.model.QoSParam;
import py.fpuna.tesis.qoetest.utils.Constants;
import py.fpuna.tesis.qoetest.utils.DateHourUtils;
import py.fpuna.tesis.qoetest.utils.VideoUtils;

public class StreamingTestActivity extends Activity
        implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener, SeekBar.OnSeekBarChangeListener,
        MediaPlayer.OnCompletionListener {

    private SeekBar videoProgressBar;
    private VideoView videoView;
    private TextView posicionActualVideoLabel;
    private TextView totalVideoLabel;
    private VideoUtils videoUtils;
    private Handler mHandler = new Handler();
    private Handler mHandlerAutoHide = new Handler();
    private Button siguienteBtn;
    private Button atrasBtn;
    private long duracionVideo;
    private long inicioCargando;
    private long inicioTotal;
    private long finCargando;
    private long finTotal;
    private long bufferingTime;
    private long startBuffering = 0;
    private long endBuffering = 0;
    private long cantidadPausas = 0;
    private LinearLayout buttonBar;
    private RelativeLayout progressLayout;
    public static final int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View
            .SYSTEM_UI_FLAG_FULLSCREEN;

    private ProgressBar bufferingProgressBar;
    View decorView;

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
            videoProgressBar.setProgress(progress);
            // Running this thread after 100 milliseconds
            mHandler.postDelayed(this, 100);
        }
    };

    private Runnable mAutoHideLayouts = new Runnable() {
        @Override
        public void run() {

            if(progressLayout.getVisibility() == View.VISIBLE){
                progressLayout.setVisibility(View.GONE);
            }

            if(decorView.getSystemUiVisibility() != uiOptions){
                decorView.setSystemUiVisibility(uiOptions);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(uiOptions);
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0){
                    progressLayout.setVisibility(View.VISIBLE);
                    mHandlerAutoHide.postDelayed(mAutoHideLayouts, 5*1000);
                }
            }
        });
        setContentView(R.layout.activity_streaming_test);

        /* VideoView */
        videoView = (VideoView) findViewById(R.id.streamingView);
        videoView.setOnErrorListener(this);
        videoView.setOnPreparedListener(this);
        videoView.setOnCompletionListener(this);
        videoView.setVideoURI(Uri.parse(Constants.VIDEO_URL));
        inicioCargando = System.currentTimeMillis();
        videoView.start();

        /* SeekBar Video */
        videoProgressBar = (SeekBar) findViewById(R.id.videoProgressBar);
        videoProgressBar.setOnSeekBarChangeListener(this);

        /* Video utils */
        videoUtils = new VideoUtils();

        /* Progress Buffering */
        bufferingProgressBar = (ProgressBar) findViewById(R.id
                .bufferingProgressBar);
        bufferingProgressBar.setVisibility(View.VISIBLE);


        /* Posicion Actual Label */
        posicionActualVideoLabel = (TextView) findViewById(R.id
                .duracionActualLabel);
        /* Duracion total Label */
        totalVideoLabel = (TextView) findViewById(R.id.duracionTotalLabel);

        /* Boton Atras */
        atrasBtn = (Button) findViewById(R.id.leftButton);
        atrasBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent atrasIntent = new Intent(getApplicationContext(),
                        StreamingTestIntroActivity.class);
                startActivity(atrasIntent);
            }
        });

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
                Log.d("StreamingTestActivity", "Tiempo de carga inicial: " +
                        tiempoCarga);
                Log.d("StreamingTestActivity", "Duracion video: " +
                        duracionVideoString);
                Log.d("StreamingTestActivity", "Tiempo reproducción total: " +
                        tiempoTotalRep);
                Log.d("StreamingTestActivity", "Tiempo Buffering: " +
                        tiempoBufferingString);

                Log.d("StreamingTestActivity", "Bufferin Time:" +
                        videoUtils.milliSecondsToTimer(bufferingTime));

                Intent intent = new Intent(StreamingTestActivity.this,
                        QoEStreamingTestActivity
                                .class);
                /* Se agregan los extras anteriores */
                Bundle extras = getIntent().getExtras();
                ArrayList<QoSParam> parametrosQoS = extras
                        .getParcelableArrayList(Constants.EXTRA_PARAM_QOS);


                /* Carga Inicial Video */
                QoSParam cargaInicialVideo = new QoSParam();
                cargaInicialVideo.setCodigoParametro(Constants.CARGA_INICIAL_VIDEO);
                cargaInicialVideo.setValor(DateHourUtils.toSeconds(finCargando-inicioCargando));
                parametrosQoS.add(cargaInicialVideo);

                /* Tiempo Buffering */
                QoSParam tiempoBufferingParam = new QoSParam();
                tiempoBufferingParam.setCodigoParametro(Constants.TIEMPO_BUFFERING);
                tiempoBufferingParam.setValor(DateHourUtils.toSeconds(bufferingTime));
                parametrosQoS.add(tiempoBufferingParam);

                /* Cantidad Buffering */
                QoSParam cantBufferingParam = new QoSParam();
                cantBufferingParam.setCodigoParametro(Constants.CANT_BUFFERING);
                cantBufferingParam.setValor(cantidadPausas);
                parametrosQoS.add(cantBufferingParam);

                extras.putParcelableArrayList(Constants.EXTRA_PARAM_QOS,
                        parametrosQoS);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });

        buttonBar = (LinearLayout) findViewById(R.id.btLayout);
        buttonBar.setVisibility(View.GONE);

        progressLayout = (RelativeLayout) findViewById(R.id.progressLayout);
        progressLayout.setVisibility(View.GONE);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        int stopPosition = videoView.getCurrentPosition();
        videoView.pause();
        outState.putInt("position", stopPosition);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        int stopPosition = savedInstanceState.getInt("position");
        videoView.seekTo(stopPosition);
        videoView.start();
        videoProgressBar.setProgress(stopPosition);
        updateProgressBar();
    }

    @Override
    protected void onPause() {
        super.onPause();
        videoView.pause();
        mHandler.removeCallbacks(mUpdateTimeTask);

    }

    @Override
    protected void onResume() {
        super.onResume();
        updateProgressBar();
        videoView.resume();
    }

    /**
     * @param mediaPlayer
     */
    @Override
    public void onPrepared(final MediaPlayer mediaPlayer) {
        finCargando = System.currentTimeMillis();
        inicioTotal = System.currentTimeMillis();
        bufferingProgressBar.setVisibility(View.GONE);
        videoProgressBar.setProgress(0);
        videoProgressBar.setMax(100);
        updateProgressBar();

        mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mediaPlayer, int percent) {
                videoProgressBar.setSecondaryProgress(percent);
            }
        });

        mediaPlayer.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                if (what == MediaPlayer.MEDIA_INFO_BUFFERING_START) {
                    cantidadPausas++;
                    startBuffering = System.currentTimeMillis();
                    bufferingProgressBar.setVisibility(View.VISIBLE);
                    //mp.pause();
                }
                if (what == MediaPlayer.MEDIA_INFO_BUFFERING_END) {
                    bufferingProgressBar.setVisibility(View.GONE);
                    endBuffering = System.currentTimeMillis();
                    //mp.start();
                    if(startBuffering != 0) {
                        bufferingTime += (endBuffering - startBuffering);
                    }
                }
                return false;
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
        return true;
    }

    /**
     * @param mediaPlayer
     */
    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        buttonBar.setVisibility(View.VISIBLE);
        duracionVideo = mediaPlayer.getDuration();
        finTotal = System.currentTimeMillis();
        siguienteBtn.setVisibility(View.VISIBLE);
        mostrarSystemUI();
        progressLayout.setVisibility(View.VISIBLE);
        mHandler.removeCallbacks(mUpdateTimeTask);
        mHandlerAutoHide.removeCallbacks(mAutoHideLayouts);
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

    public void mostrarSystemUI(){
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);

    }
}
