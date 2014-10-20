package py.fpuna.tesis.qoetest.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by User on 15/10/2014.
 */
public class DeviceStatus implements Parcelable{
    private int nivelBaterial;
    private String tipoAccesoInternet;
    private Double usoCpu;
    private Double usoRam;
    private String intensidadSenhal;


    public DeviceStatus() {
    }

    public int getNivelBaterial() {
        return nivelBaterial;
    }

    public void setNivelBaterial(int nivelBaterial) {
        this.nivelBaterial = nivelBaterial;
    }

    public String getTipoAccesoInternet() {
        return tipoAccesoInternet;
    }

    public void setTipoAccesoInternet(String tipoAccesoInternet) {
        this.tipoAccesoInternet = tipoAccesoInternet;
    }

    public Double getUsoCpu() {
        return usoCpu;
    }

    public void setUsoCpu(Double usoCpu) {
        this.usoCpu = usoCpu;
    }

    public Double getUsoRam() {
        return usoRam;
    }

    public void setUsoRam(Double usoRam) {
        this.usoRam = usoRam;
    }

    public String getIntensidadSenhal() {
        return intensidadSenhal;
    }

    public void setIntensidadSenhal(String intensidadSenhal) {
        this.intensidadSenhal = intensidadSenhal;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(this.nivelBaterial);
        parcel.writeString(this.tipoAccesoInternet);
        parcel.writeDouble(this.usoCpu);
        parcel.writeDouble(this.usoRam);
        parcel.writeString(this.intensidadSenhal);
    }

    public DeviceStatus (Parcel in){
        this.nivelBaterial = in.readInt();
        this.tipoAccesoInternet = in.readString();
        this.usoCpu = in.readDouble();
        this.usoRam = in.readDouble();
        this.intensidadSenhal = in.readString();

    }

    public static final Creator<DeviceStatus> CREATOR = new Creator<DeviceStatus>() {
        @Override
        public DeviceStatus createFromParcel(Parcel in) {
            return (new DeviceStatus(in));
        }

        @Override
        public DeviceStatus[] newArray(int size) {
            return (new DeviceStatus[size]);
        }
    };
}
