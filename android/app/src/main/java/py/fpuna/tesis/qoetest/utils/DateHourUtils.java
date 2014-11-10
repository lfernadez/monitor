package py.fpuna.tesis.qoetest.utils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by User on 01/09/2014.
 */
public class DateHourUtils {

    public enum Format {

        /**
         * Formato para mostrar la fecha en la interfaz grafica:
         * {@code "dd/MM/yyyy"}
         */
        DATE_VIEW,

        /**
         * Formato para guardar la fecha en la base de datos: {@code "yyyyMMdd"}
         */
        DATE_STORE,

        /**
         * Formato para mostrar la hora en la interfaz grafica: {@code HH:mm:ss}
         */
        TIME_VIEW,

        /**
         * Formato para guardar la hora en la base de datos: {@code HHmmss}
         */
        TIME_STORE,

        /**
         * Formato para envio a webservice
         */
        DATE_SERVICE,

        /**
         * Formato para envio a webservice
         */
        DATE_SERVICE_2,

        /**
         * Formato para enviar fecha de pedido a webservice.
         */
        DATE_SERVICE_3;
    }

    private static final SimpleDateFormat dateViewFormat = new SimpleDateFormat("dd/MM/yyyy",
            Locale.US);

    private static final SimpleDateFormat dateServiceFormat = new SimpleDateFormat("yyyy/MM/dd",
            Locale.US);

    private static final SimpleDateFormat dateService2Format = new SimpleDateFormat("yyyy-MM-dd",
            Locale.US);

    private static final SimpleDateFormat dateService3Format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
            Locale.US);

    private static final SimpleDateFormat dateStoreFormat = new
            SimpleDateFormat("yyyy-MM-dd",
            Locale.US);

    private static final SimpleDateFormat timeViewFormat = new SimpleDateFormat("HH:mm:ss",
            Locale.US);

    private static final SimpleDateFormat timeStoreFormat = new
            SimpleDateFormat("HH:mm:ss",
            Locale.US);

    /**
     * Convierte la cadena {@code date} cuyo formato es {@code format} a un
     * objeto {@code Date}.
     *
     * @param date   Cadenta en formato {@code format}
     * @param format Formato de la cadena
     * @return objeto {@code Date}
     */
    public static Date parse(String date, Format format) {
        if (TextUtils.isEmpty(date))
            return null;
        try {
            switch (format) {
                case DATE_VIEW:
                    return dateViewFormat.parse(date);
                case DATE_STORE:
                    return dateStoreFormat.parse(date);
                case TIME_VIEW:
                    return timeViewFormat.parse(date);
                case TIME_STORE:
                    return timeStoreFormat.parse(date);
                case DATE_SERVICE_2:
                    return dateService2Format.parse(date);
                default:
                    return null;
            }
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * Convierte el objeto {@code date} a una cadenta en formato {@code format}.
     *
     * @param date   objeto {@code Date}
     * @param format formato del resultado.
     * @return {@code String} en formato {@code format}
     */
    public static String format(Date date, Format format) {
        if (date == null)
            return null;
        switch (format) {
            case DATE_VIEW:
                return dateViewFormat.format(date);
            case DATE_STORE:
                return dateStoreFormat.format(date);
            case TIME_VIEW:
                return timeViewFormat.format(date);
            case TIME_STORE:
                return timeStoreFormat.format(date);
            case DATE_SERVICE:
                return dateServiceFormat.format(date);
            case DATE_SERVICE_2:
                return dateService2Format.format(date);
            case DATE_SERVICE_3:
                return dateService3Format.format(date);
            default:
                return null;
        }
    }

    /**
     * Convierte el objeto {@code date} a una cadenta en formato {@code format}.
     *
     * @param date   objeto {@code Long}
     * @param format formato del resultado.
     * @return {@code String} en formato {@code format}
     */
    public static String format(Long date, Format format) {
        if (date == null)
            return null;
        Date fecha = new Date();
        fecha.setTime(date);
        switch (format) {
            case DATE_VIEW:
                return dateViewFormat.format(fecha);
            case DATE_STORE:
                return dateStoreFormat.format(fecha);
            case TIME_VIEW:
                return timeViewFormat.format(fecha);
            case TIME_STORE:
                return timeStoreFormat.format(fecha);
            case DATE_SERVICE_3:
                return dateService3Format.format(fecha);
            default:
                return null;
        }
    }

    public static long toSeconds (long miliseconds){
        return miliseconds / 1000;
    }

    public static long nanoToSeconds( long nanoseconds){
        return nanoseconds / 1000000000;
    }
}
