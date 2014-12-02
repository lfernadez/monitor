package py.fpuna.tesis.qoetest.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by User on 06/10/2014.
 */
public class NetworkStat implements Parcelable{
    private long kbpsDown;
    private long delay;
    private double packetLoss;
    private double jitter;

    public NetworkStat(){}

    public long getkbpsDown() {
        return kbpsDown;
    }

    public void setKbpsDown(long bpsDown) {
        this.kbpsDown = bpsDown;
    }

    public long getDelay() {
        return delay;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }

    public double getPacketLoss() {
        return packetLoss;
    }

    public void setPacketLoss(long packetLoss) {
        this.packetLoss = packetLoss;
    }

    public double getJitter() {
        return jitter;
    }

    public void setJitter(long jitter) {
        this.jitter = jitter;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeLong(this.kbpsDown);
        parcel.writeLong(this.delay);
        parcel.writeDouble(this.packetLoss);
        parcel.writeDouble(this.jitter);
    }

    public NetworkStat(Parcel in){
        this.kbpsDown = in.readLong();
        this.delay = in.readLong();
        this.packetLoss = in.readDouble();
        this.jitter = in.readDouble();
    }

    public static final Creator<NetworkStat> CREATOR = new Creator<NetworkStat>() {
        @Override
        public NetworkStat createFromParcel(Parcel in) {
            return (new NetworkStat(in));
        }

        @Override
        public NetworkStat[] newArray(int size) {
            return (new NetworkStat[size]);
        }
    };
}
