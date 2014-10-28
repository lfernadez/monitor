package py.fpuna.tesis.qoetest.utils;

/**
 * Representa a las constantes utilizadas en los extras
 * Created by User on 01/09/2014.
 */
public class Constants {
    /** Iperf binary directory */
    public static final String IPERF_BINARY_DIC = "/data/data/py.fpuna.tesis.qoetest/iperf";
    public static final String IPERF_FILE_NAME = "iperf";


    /** Direccion IP del server de transmision */
    public static final String IP_TRANSMITTER_SERVER = "192.168.1.101";

    /** Representa el tiempo de actualizacion de los datos recogidos */
    public static final Integer TIEMPO_ACTUALIZACION = 1000;

    public static final long MULTIPLO_MB = 1048576L;
    public static final long MULTIPLI_KB_MB = 1024L;

    /* URLs */
    public static final String GAG_URL = "http://9gag.com";
    public static final String CODING_LOVE_URL = "http://thecodinglove.com";
    public static final String GOOGLE_URL = "http://www.google.com";
    public static final String YOUTUBE_URL = "http://www.youtube.com";
    public static final String ABC_URL = "http://www.abc.com.py";
    public static final String VIDEO_URL = "https://archive" +
            ".org/download/ksnn_compilation_master_the_internet/ksnn_compilation_master_the_internet_512kb.mp4";
    public static final String VIDEO_URL_DOS = "rtsp://" +
            IP_TRANSMITTER_SERVER + "/sample_300kbit.mp4";

    public static final long INTERVAL_UPDATE_LOCATION_MILIS = 5 * 60 * 1000;
    public static final int FAST_CEILING_IN_SECONDS = 1;

    /* SharedPrefences */
    public static final String SAHRED_PREFERENCES = "py.fpuna.tesis.qoetest" +
            ".SHARED_PREFERENCES";
    public static final String PERFIL_USUARIO_SHARED = "PERFIL_USUARIO";
    public static final String DEVICE_SHARED = "DEVICE_SHARED";

    /* EXTRAS */
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

    /* bandwidth Test */
    public static final String IMAGE_URL_DOWN = "http://carlook" +
            ".net/data/db_photos/porsche/911_carrera_gts/997/porsche_911_carrera_gts_997_coupe2d-4477.jpg";
    public static final String IMAGE_URL_DOWN_DOS = "http://" +
            IP_TRANSMITTER_SERVER +
            ":8083/porsche_911_carrera_gts_997_coupe2d-4477.jpg";
    public static final long IMAGE_LENGTH = 1185228;

    public static final int DELAY_ID = 1;
    public static final int BANDWITDH_ID = 2;
    public static final int PACKET_LOSS_ID = 3;
    public static final int JITTER_ID = 4;

}
