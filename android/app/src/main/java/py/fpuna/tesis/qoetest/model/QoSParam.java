package py.fpuna.tesis.qoetest.model;

/**
 * Created by User on 15/10/2014.
 */
public class QoSParam {
    private int codigoParametro;
    private double valor;

    public QoSParam() {
    }

    public QoSParam(int codigoParametro, double valor) {
        this.codigoParametro = codigoParametro;
        this.valor = valor;
    }


    public int getCodigoParametro() {
        return codigoParametro;
    }

    public void setCodigoParametro(int codigoParametro) {
        this.codigoParametro = codigoParametro;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }
}
