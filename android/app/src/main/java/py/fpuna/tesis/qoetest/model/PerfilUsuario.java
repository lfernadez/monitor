package py.fpuna.tesis.qoetest.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by User on 19/09/2014.
 */
public class PerfilUsuario implements Parcelable {
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
    private String sexo;
    private Integer edad;
    private String profesion;
    private String frecuenciaUso;
    private String aplicacionesFrecuentes;
    private String emociones;
    private String respuestaCosto;
    private String respuestaCostoMas;

    public PerfilUsuario() {
    }

    public PerfilUsuario(String sexo, Integer edad,
                         String profesion, String frecuenciaUso, String appFrecuentes,
                         String respuestaCosto, String respuestaCostoMas) {
        this.sexo = sexo;
        this.edad = edad;
        this.profesion = profesion;
        this.frecuenciaUso = frecuenciaUso;
        this.aplicacionesFrecuentes = appFrecuentes;
        this.respuestaCosto = respuestaCosto;
        this.respuestaCostoMas = respuestaCostoMas;
    }

    public PerfilUsuario(Parcel in) {
        this.sexo = in.readString();
        this.edad = in.readInt();
        this.profesion = in.readString();
        this.frecuenciaUso = in.readString();
        this.aplicacionesFrecuentes = in.readString();
        this.emociones = in.readString();
        this.respuestaCosto = in.readString();
        this.respuestaCostoMas = in.readString();
    }

    public String getAplicacionesFrecuentes() {
        return aplicacionesFrecuentes;
    }

    public void setAplicacionesFrecuentes(String aplicacionesFrecuentes) {
        this.aplicacionesFrecuentes = aplicacionesFrecuentes;
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

    public String getEmociones() {
        return emociones;
    }

    public void setEmociones(String emociones) {
        this.emociones = emociones;
    }

    public String getRespuestaCosto() {
        return respuestaCosto;
    }

    public void setRespuestaCosto(String respuestaCosto) {
        this.respuestaCosto = respuestaCosto;
    }

    public String getRespuestaCostoMas() {
        return respuestaCostoMas;
    }

    public void setRespuestaCostoMas(String respuestaCostoMas) {
        this.respuestaCostoMas = respuestaCostoMas;
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
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.sexo);
        parcel.writeInt(this.edad);
        parcel.writeString(this.profesion);
        parcel.writeString(this.frecuenciaUso);
        parcel.writeString(this.aplicacionesFrecuentes);
        parcel.writeString(this.emociones);
        parcel.writeString(this.respuestaCosto);
        parcel.writeString(this.respuestaCostoMas);

    }
}
