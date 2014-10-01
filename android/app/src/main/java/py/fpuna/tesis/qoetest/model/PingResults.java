package py.fpuna.tesis.qoetest.model;

/**
 * Created by User on 01/10/2014.
 */
public class PingResults {
    private int packetsSended;
    private int packetsReceived;
    private long time;
    private double packetloss;
    private double rttMin;
    private double rttAvg;
    private double rttMax;
    private double rttStdDev;


    public int getPacketsSended() {
        return packetsSended;
    }

    public void setPacketsSended(int packetsSended) {
        this.packetsSended = packetsSended;
    }

    public int getPacketsReceived() {
        return packetsReceived;
    }

    public void setPacketsReceived(int packetsReceived) {
        this.packetsReceived = packetsReceived;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public double getRttMin() {
        return rttMin;
    }

    public void setRttMin(double rttMin) {
        this.rttMin = rttMin;
    }

    public double getRttAvg() {
        return rttAvg;
    }

    public void setRttAvg(double rttAvg) {
        this.rttAvg = rttAvg;
    }

    public double getRttMax() {
        return rttMax;
    }

    public void setRttMax(double rttMax) {
        this.rttMax = rttMax;
    }

    public double getRttStdDev() {
        return rttStdDev;
    }

    public void setRttStdDev(double rttStdDev) {
        this.rttStdDev = rttStdDev;
    }

    public double getPacketloss() {
        return packetloss;
    }

    public void setPacketloss(double packetloss) {
        this.packetloss = packetloss;
    }
}
