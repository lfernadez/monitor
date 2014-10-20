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

    private String fecha;
    private String hora;
    private Double latitud;
    private Double longitud;
    private int idCelda;

    public Localizacion() {
    }

    public Localizacion(String fecha, String hora, Double latitud,
                        Double longitud, int idCelda) {
        this.fecha = fecha;
        this.hora = hora;
        this.latitud = latitud;
        this.longitud = longitud;
        this.idCelda = idCelda;

    }

    public int getIdCelda() {
        return idCelda;
    }

    public void setIdCelda(int idCelda) {
        this.idCelda = idCelda;
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


    public Localizacion(Parcel in) {
        this.fecha = in.readString();
        this.hora = in.readString();
        this.latitud = in.readDouble();
        this.longitud = in.readDouble();
        this.idCelda = in.readInt();
    }

    public Localizacion(Cursor cursor) {
        this.setFecha(cursor.getString(cursor.getColumnIndex(DatabaseContract.Localizacion.FECHA)));
        this.setHora(cursor.getString(cursor.getColumnIndex(DatabaseContract.Localizacion.HORA)));
        this.setLatitud(cursor.getDouble(cursor.getColumnIndex(DatabaseContract.Localizacion.LATITUD)));
        this.setLongitud(cursor.getDouble(cursor.getColumnIndex(DatabaseContract.Localizacion.LONGITUD)));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.fecha);
        parcel.writeString(this.hora);
        parcel.writeDouble(this.latitud);
        parcel.writeDouble(this.longitud);
        parcel.writeInt(this.idCelda);
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
