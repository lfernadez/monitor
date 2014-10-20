package py.fpuna.tesis.qoetest.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by User on 06/10/2014.
 */
public class PhoneInfo implements Parcelable {
    private String modelo;
    private String marca;
    private String ram;
    private String soVersion;
    private String procesador;
    private int cpuCores;
    private int cpuFrec;
    private String gama;
    private String memoriaInterna;
    private String pantalla;
    private String imei;

    public PhoneInfo(){}

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getRam() {
        return ram;
    }

    public void setRam(String ram) {
        this.ram = ram;
    }

    public String getSoVersion() {
        return soVersion;
    }

    public void setSoVersion(String soVersion) {
        this.soVersion = soVersion;
    }

    public String getProcesador() {
        return procesador;
    }

    public void setProcesador(String procesador) {
        this.procesador = procesador;
    }

    public int getCpuCores() {
        return cpuCores;
    }

    public void setCpuCores(int cpuCores) {
        this.cpuCores = cpuCores;
    }

    public int getCpuFrec() {
        return cpuFrec;
    }

    public void setCpuFrec(int cpuFrec) {
        this.cpuFrec = cpuFrec;
    }

    public String getGama() {
        return gama;
    }

    public void setGama(String gama) {
        this.gama = gama;
    }

    public String getMemoriaInterna() {
        return memoriaInterna;
    }

    public void setMemoriaInterna(String memoriaInterna) {
        this.memoriaInterna = memoriaInterna;
    }

    public String getPantalla() {
        return pantalla;
    }

    public void setPantalla(String pantalla) {
        this.pantalla = pantalla;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(this.modelo);
        parcel.writeString(this.marca);
        parcel.writeString(this.ram);
        parcel.writeString(this.soVersion);
        parcel.writeString(this.procesador);
        parcel.writeInt(this.cpuCores);
        parcel.writeInt(this.cpuFrec);
        parcel.writeString(this.gama);
        parcel.writeString(this.memoriaInterna);
        parcel.writeString(this.pantalla);
        parcel.writeString(this.imei);
    }

    public PhoneInfo (Parcel in){
        this.modelo = in.readString();
        this.marca = in.readString();
        this.ram = in.readString();
        this.soVersion = in.readString();
        this.procesador = in.readString();
        this.cpuCores = in.readInt();
        this.cpuFrec = in.readInt();
        this.gama = in.readString();
        this.memoriaInterna = in.readString();
        this.pantalla = in.readString();
        this.imei = in.readString();
    }

    public static final Creator<PhoneInfo> CREATOR = new Creator<PhoneInfo>() {
        @Override
        public PhoneInfo createFromParcel(Parcel in) {
            return (new PhoneInfo(in));
        }

        @Override
        public PhoneInfo[] newArray(int size) {
            return (new PhoneInfo[size]);
        }
    };
}
