package py.fpuna.tesis.qoetest.model;

/**
 * Created by User on 15/10/2014.
 */
public class DeviceStatus {
    private Double nivelBaterial;
    private String tipoAccesoInternet;
    private Double usoCpu;
    private Double usoRam;
    private String intensidadSenhal;


    public DeviceStatus() {
    }

    public Double getNivelBaterial() {
        return nivelBaterial;
    }

    public void setNivelBaterial(Double nivelBaterial) {
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
}
