package py.fpuna.tesis.qoetest.model;

/**
 * Created by User on 10/11/2014.
 */
public class IperfUDPResults {
    private double bandwidthDown;
    private double bandwidthUp;
    private double fileSize;
    private double jitter;
    private double packetLoss;

    public double getBandwidthDown() {
        return bandwidthDown;
    }

    public void setBandwidthDown(double bandwidthDown) {
        this.bandwidthDown = bandwidthDown;
    }

    public double getBandwidthUp() {
        return bandwidthUp;
    }

    public void setBandwidthUp(double bandwidthUp) {
        this.bandwidthUp = bandwidthUp;
    }

    public double getFileSize() {
        return fileSize;
    }

    public void setFileSize(double fileSize) {
        this.fileSize = fileSize;
    }

    public double getJitter() {
        return jitter;
    }

    public void setJitter(double jitter) {
        this.jitter = jitter;
    }

    public double getPacketLoss() {
        return packetLoss;
    }

    public void setPacketLoss(double packetLoss) {
        this.packetLoss = packetLoss;
    }
}
