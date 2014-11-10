package py.fpuna.tesis.qoetest.model;

/**
 * Created by User on 10/11/2014.
 */
public class IperfTCPResults {
    private double bandwidthDown;
    private double bandwidthUp;
    private double fileSize;

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
}
