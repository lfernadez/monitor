package py.fpuna.tesis.qoetest.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.BaseColumns;

import py.fpuna.tesis.qoetest.database.DatabaseContract;

/**
 * Created by User on 19/09/2014.
 */
public class PerfilUsuario implements Parcelable {
    private long idPerfil;
    private String sexo;
    private Integer edad;
    private String profesion;
    private String frecuenciaUso;

    public PerfilUsuario() {
    }

    public PerfilUsuario(long idPerfil, String sexo, Integer edad,
                         String profesion, String frecuenciaUso) {
        this.idPerfil = idPerfil;
        this.sexo = sexo;
        this.edad = edad;
        this.profesion = profesion;
        this.frecuenciaUso = frecuenciaUso;
    }

    public long getIdPerfil() {
        return idPerfil;
    }

    public void setIdPerfil(long idPerfil) {
        this.idPerfil = idPerfil;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public Integer getEdad() {
        return edad;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    public String getProfesion() {
        return profesion;
    }

    public void setProfesion(String profesion) {
        this.profesion = profesion;
    }

    public String getFrecuenciaUso() {
        return frecuenciaUso;
    }

    public void setFrecuenciaUso(String frecuenciaUso) {
        this.frecuenciaUso = frecuenciaUso;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public PerfilUsuario(Parcel in) {
        this.idPerfil = in.readLong();
        this.sexo = in.readString();
        this.edad = in.readInt();
        this.profesion = in.readString();
        this.frecuenciaUso = in.readString();
    }
    /*
    //TODO Base de datos para Perfiles...
    public Localizacion(Cursor cursor) {
        this.setIdLocalizacion(cursor.getLong(cursor.getColumnIndex(BaseColumns._ID)));
        this.setFecha(cursor.getString(cursor.getColumnIndex(DatabaseContract.Localizacion.FECHA)));
        this.setHora(cursor.getString(cursor.getColumnIndex(DatabaseContract.Localizacion.HORA)));
        this.setLatitud(cursor.getDouble(cursor.getColumnIndex(DatabaseContract.Localizacion.LATITUD)));
        this.setLongitud(cursor.getDouble(cursor.getColumnIndex(DatabaseContract.Localizacion.LONGITUD)));
        this.setPrecision(cursor.getDouble(cursor.getColumnIndex(DatabaseContract.Localizacion.PRECISION)));
        this.setTipoMuestra(cursor.getString(cursor.getColumnIndex(DatabaseContract.Localizacion.TIPO_MUESTRA)));
        this.setImei(cursor.getString(cursor.getColumnIndex(DatabaseContract.Localizacion.IMEI)));
    }*/


    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(this.idPerfil);
        parcel.writeString(this.sexo);
        parcel.writeInt(this.edad);
        parcel.writeString(this.profesion);
        parcel.writeString(this.frecuenciaUso);

    }

    public static final Creator<PerfilUsuario> CREATOR = new Creator<PerfilUsuario>() {
        @Override
        public PerfilUsuario createFromParcel(Parcel in) {
            return (new PerfilUsuario(in));
        }

        @Override
        public PerfilUsuario[] newArray(int size) {
            return (new PerfilUsuario[size]);
        }
    };
}
