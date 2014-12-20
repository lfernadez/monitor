package py.fpuna.tesis.qoetest.utils;

/**
 * Representa a las constantes utilizadas en los extras
 * Created by User on 01/09/2014.
 */
public class Constants {
    /**
     * Iperf binary directory
     */
    public static final String IPERF_BINARY_DIC = "/data/data/py.fpuna.tesis.qoetest/iperf";
    public static final String IPERF_FILE_NAME = "iperf";

    /**
     * TCPDump binary directory
     */
    public static final String TCPDUMP_BINARY_DIC = "/data/data/py.fpuna" +
            ".tesis.qoetest/tcpdump";

    public static final String TCPDUMP_FILE_NAME = "tcpdump";

    /**
     * Direccion IP del server de transmision
     */
    public static final String IP_TRANSMITTER_SERVER = "192.168.0.19";
    public static final String VIDEO_URL_DOS = "rtsp://" +
            IP_TRANSMITTER_SERVER + "/videoStr.mp4";
    public static final String IMAGE_URL_DOWN_DOS = "http://" +
            IP_TRANSMITTER_SERVER +
            ":8083/porsche_911_carrera_gts_997_coupe2d-4477.jpg";
    public static final String PORT_HTTP = "8084";
    public static final String PORT_WOWZA = "1935";

    /* URLs */
    public static final String NINE_GAG_URL = "http://" +
            IP_TRANSMITTER_SERVER + ":" + PORT_HTTP +
            "/9GAG%20-%20Why%20So%20Serious%20.html";

    public static final String CODING_LOVE_URL = "http://" +
            IP_TRANSMITTER_SERVER + ":" + PORT_HTTP + "/html/coding.html";

    public static final String A_PREF_URL = "http://" +
            IP_TRANSMITTER_SERVER + ":" + PORT_HTTP + "/html/preferente.html";

    public static final String VIDEO_SERVER = "http://" + IP_TRANSMITTER_SERVER +
            ":" + PORT_HTTP + "/videos/";

    public static final String VIDEO_URL = "http://" + IP_TRANSMITTER_SERVER +
            ":" + PORT_HTTP + "/videos/videoStr.mp4";

    public static final String VIDEO_URL_WOWZA = "rtsp://" +
            IP_TRANSMITTER_SERVER + ":" + PORT_WOWZA + "/vod/videoStr.mp4";
    /**
     * Representa el tiempo de actualizacion de los datos recogidos
     */
    public static final Integer TIEMPO_ACTUALIZACION = 1000;
    public static final long MULTIPLO_MB = 1048576L;
    public static final long MULTIPLI_KB_MB = 1024L;
    public static final long INTERVAL_UPDATE_LOCATION_MILIS = 5 * 60 * 1000;
    public static final int FAST_CEILING_IN_SECONDS = 1;

    /** SharedPrefences */
    public static final String SAHRED_PREFERENCES = "py.fpuna.tesis.qoetest" +
            ".SHARED_PREFERENCES";
    public static final String PERFIL_USUARIO_SHARED = "PERFIL_USUARIO";
    public static final String DEVICE_SHARED = "DEVICE_SHARED";

    /** EXTRAS */
    public static final String EXTRA_LOCALIZACION = "EXTRA_LOCALIZACION";
    public static final String EXTRA_DEVICE_INFO = "EXTRA_DEVICE_INFO";
    public static final String EXTRA_PERFIL_USUARIO = "EXTRA_PERFIL_USUARIO";
    public static final String EXTRA_DEVICE_STATUS = "EXTRA_DEVICE_STATUS";
    public static final String EXTRA_PARAM_QOS = "EXTRA_PARAM_QOS";
    public static final String EXTRA_QOE_TEST = "EXTRA_QOE_TEST";
    public static final String EXTRA_TIEMPO_CARGA = "EXTRA_TIEMPO_CARGA";
    public static final String EXTRA_DURACION_VIDEO = "EXTRA_DURACION_VIDEO";
    public static final String EXTRA_TIEMPO_BUFFERING =
            "EXTRA_TIEMPO_BUFFERING";
    public static final String EXTRA_TIEMPO_TOTAL_REP =
            "EXTRA_TIEMPO_TOTAL_REP";
    public static final String EXTRA_CANT_PAUSAS = "EXTRA_CANT_PAUSAS";

    /** Bandwidth Test */
    public static final String IMAGE_URL_DOWN = "http://carlook" +
            ".net/data/db_photos/porsche/911_carrera_gts/997/porsche_911_carrera_gts_997_coupe2d-4477.jpg";
    public static final long IMAGE_LENGTH = 1185228;

    /**
     * Codigos de Test
     */
    public static final int TEST_WEB_UNO = 1;
    public static final int TEST_STREAMING_UNO = 2;
    public static final int TEST_WEB_DOS = 3;
    public static final int TEST_STREAMING_DOS = 4;

    public static final int DELAY_ID = 1;
    public static final int BANDWITDH_ID = 2;
    public static final int PACKET_LOSS_ID = 3;
    public static final int JITTER_ID = 4;

    /*Streaming uno */
    public static final int TIEMPO_CARGA_WEB = 5;
    public static final int CARGA_INICIAL_VIDEO = 6;
    public static final int TIEMPO_BUFFERING = 7;
    public static final int CANT_BUFFERING = 8;

    public static final int CANCELADO_ID = 9;
    public static final int CANCELADO_VIDEO_ID = 10;
    public static final int NAME_VIDEO_ID = 11;

    public static final String OBT_TEL = "telefono";
    public static final String OBT_ENV = "enviado";

}
