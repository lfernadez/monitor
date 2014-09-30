package py.fpuna.tesis.qoetest.database;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by User on 01/09/2014.
 */
public class DatabaseContract {

    private DatabaseContract() {
    }

	/* Columnas de las tablas */

    interface LocalizacionColumns {
        /**
         * Fecha
         */
        String FECHA = "fecha";
        /**
         * Hora
         */
        String HORA = "hora";
        /**
         * Latitud
         */
        String LATITUD = "latitud";
        /**
         * Longitud
         */
        String LONGITUD = "longitud";
        /**
         * Precision
         */
        String PRECISION = "precision";
        /**
         * Tipo de muestra
         */
        String TIPO_MUESTRA = "tipo_muestra";
        /**
         * IMEI
         */
        String IMEI = "imei";
    }

    public static final String CONTENT_AUTHORITY = "demo.example.user.demo.provider";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"
            + CONTENT_AUTHORITY);

    private static final String PATH_LOC = "localizacion";
    private static final String PATH_SQLITE_SEQUENCE = "sqlite_sequence";

    /**
     * Localizaciones registrados
     */
    public static class Localizacion implements LocalizacionColumns, BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_LOC).build();
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd"
                + ".py.fpuna.tesis.qoetest";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item"
                + "/vnd.py.fpuna.tesis.qoetest";

        public static Uri buildUri(String id) {
            return CONTENT_URI.buildUpon().appendPath(id).build();
        }

        public static String getId(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }

    /**
     * Secuencias de las tablas de la base de datos. Son expuestas mediante el
     * content provider para permitir reiniciar la secuencia para tablas cuyos
     * datos son periodicamente eliminados.
     */
    public static class SqliteSequences implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_SQLITE_SEQUENCE).build();
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd"
                + ".py.fpuna.tesis.qoetest.sqlite";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor"
                + ".item/vnd.py.fpuna.tesis.qoetest.sqlite";

        public static Uri buildSqliteSequenceUri(String SqliteSequenceId) {
            return CONTENT_URI.buildUpon().appendPath(SqliteSequenceId).build();
        }

        public static String getSqliteSequenceId(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }
}
