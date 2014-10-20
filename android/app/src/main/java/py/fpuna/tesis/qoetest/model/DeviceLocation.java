package py.fpuna.tesis.qoetest.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by User on 15/10/2014.
 */
public class DeviceLocation implements Parcelable{
    private Double latitud;
    private Double longitud;
    private int idCelda;

    public DeviceLocation() {
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

    public long getIdCelda() {
        return idCelda;
    }

    public void setIdCelda(int idCelda) {
        this.idCelda = idCelda;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public DeviceLocation(Parcel in) {
        this.latitud = in.readDouble();
        this.longitud = in.readDouble();
        this.idCelda = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeDouble(this.latitud);
        parcel.writeDouble(this.longitud);
        parcel.writeInt(this.idCelda);
    }

    public static final Creator<DeviceLocation> CREATOR = new Creator<DeviceLocation>() {
        @Override
        public DeviceLocation createFromParcel(Parcel in) {
            return (new DeviceLocation(in));
        }

        @Override
        public DeviceLocation[] newArray(int size) {
            return (new DeviceLocation[size]);
        }
    };
}

