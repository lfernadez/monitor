package py.fpuna.tesis.qoetest.model;

import java.util.List;

/**
 * Created by LF on 14/10/2014.
 */
public class Prueba {
    private PerfilUsuario datosUsuario;
    private String fecha;
    private String hora;
    private PhoneInfo telefono;
    private List<QoSParam> pruebaQos;
    private List<PruebaTest> pruebaTests;

    public Prueba() {
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public PhoneInfo getTelefono() {
        return telefono;
    }

    public void setTelefono(PhoneInfo telefono) {
        this.telefono = telefono;
    }

    public List<QoSParam> getPruebaQoS() {
        return pruebaQos;
    }

    public void setPruebaQoS(List<QoSParam> pruebaQoS) {
        this.pruebaQos = pruebaQoS;
    }

    public List<PruebaTest> getPruebaTests() {
        return pruebaTests;
    }

    public void setPruebaTests(List<PruebaTest> pruebaTests) {
        this.pruebaTests = pruebaTests;
    }

    public PerfilUsuario getDatosUsuario() {
        return datosUsuario;
    }

    public void setDatosUsuario(PerfilUsuario datosUsuario) {
        this.datosUsuario = datosUsuario;
    }
}
