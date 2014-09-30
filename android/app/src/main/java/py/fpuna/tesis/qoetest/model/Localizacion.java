package py.fpuna.tesis.qoetest.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.BaseColumns;

import py.fpuna.tesis.qoetest.database.DatabaseContract;

/**
 * Created by User on 01/09/2014.
 */
public class Localizacion implements Parcelable {

    private long idLocalizacion;
    private String fecha;
    private String hora;
    private Double latitud;
    private Double longitud;
    private Double precision;
    private String imei;
    private String tipoMuestra;

    public Localizacion() {
    }

    public Localizacion(long idLocalizacion, String fecha, String hora, Double latitud,
                        Double longitud, Double precision, String imei, String tipoMuestra) {
        this.idLocalizacion = idLocalizacion;
        this.fecha = fecha;
        this.hora = hora;
        this.latitud = latitud;
        this.longitud = longitud;
        this.precision = precision;
        this.imei = imei;
        this.tipoMuestra = tipoMuestra;
    }

    public long getIdLocalizacion() {
        return idLocalizacion;
    }

    public void setIdLocalizacion(long idLocalizacion) {
        this.idLocalizacion = idLocalizacion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public Double getPrecision() {
        return precision;
    }

    public void setPrecision(Double precision) {
        this.precision = precision;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getTipoMuestra() {
        return tipoMuestra;
    }

    public void setTipoMuestra(String tipoMuestra) {
        this.tipoMuestra = tipoMuestra;
    }

    public Localizacion(Parcel in) {
        this.idLocalizacion = in.readLong();
        this.fecha = in.readString();
        this.hora = in.readString();
        this.latitud = in.readDouble();
        this.longitud = in.readDouble();
        this.precision = in.readDouble();
        this.tipoMuestra = in.readString();
        this.imei = in.readString();
    }

    public Localizacion(Cursor cursor) {
        this.setIdLocalizacion(cursor.getLong(cursor.getColumnIndex(BaseColumns._ID)));
        this.setFecha(cursor.getString(cursor.getColumnIndex(DatabaseContract.Localizacion.FECHA)));
        this.setHora(cursor.getString(cursor.getColumnIndex(DatabaseContract.Localizacion.HORA)));
        this.setLatitud(cursor.getDouble(cursor.getColumnIndex(DatabaseContract.Localizacion.LATITUD)));
        this.setLongitud(cursor.getDouble(cursor.getColumnIndex(DatabaseContract.Localizacion.LONGITUD)));
        this.setPrecision(cursor.getDouble(cursor.getColumnIndex(DatabaseContract.Localizacion.PRECISION)));
        this.setTipoMuestra(cursor.getString(cursor.getColumnIndex(DatabaseContract.Localizacion.TIPO_MUESTRA)));
        this.setImei(cursor.getString(cursor.getColumnIndex(DatabaseContract.Localizacion.IMEI)));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(this.idLocalizacion);
        parcel.writeString(this.fecha);
        parcel.writeString(this.hora);
        parcel.writeDouble(this.latitud);
        parcel.writeDouble(this.longitud);
        parcel.writeDouble(this.precision);
        parcel.writeString(this.tipoMuestra);
        parcel.writeString(this.imei);
    }

    public static final Creator<Localizacion> CREATOR = new Creator<Localizacion>() {
        @Override
        public Localizacion createFromParcel(Parcel in) {
            return (new Localizacion(in));
        }

        @Override
        public Localizacion[] newArray(int size) {
            return (new Localizacion[size]);
        }
    };
}
