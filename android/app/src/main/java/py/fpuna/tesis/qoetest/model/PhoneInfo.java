package py.fpuna.tesis.qoetest.model;

/**
 * Created by User on 06/10/2014.
 */
public class PhoneInfo {
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
}
