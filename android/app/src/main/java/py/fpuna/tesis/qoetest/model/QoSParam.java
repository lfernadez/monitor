package py.fpuna.tesis.qoetest.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by User on 15/10/2014.
 */
public class QoSParam  implements Parcelable{
    private int codigoParametro;
    private double valor;
    private String obtenido;

    public QoSParam() {
    }

    public QoSParam(int codigoParametro, double valor, String obtenido) {
        this.codigoParametro = codigoParametro;
        this.valor = valor;
        this.obtenido = obtenido;
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

    public String getObtenido() {
        return obtenido;
    }

    public void setObtenido(String obtenido) {
        this.obtenido = obtenido;
    }

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeInt(this.codigoParametro);
        parcel.writeDouble(this.valor);
        parcel.writeString(this.obtenido);
    }

    public QoSParam(Parcel in){
        this.codigoParametro = in.readInt();
        this.valor = in.readDouble();
        this.obtenido = in.readString();
    }

    public static final Creator<QoSParam> CREATOR = new Creator<QoSParam>() {
        @Override
        public QoSParam createFromParcel(Parcel in) {
            return (new QoSParam(in));
        }

        @Override
        public QoSParam[] newArray(int size) {
            return (new QoSParam[size]);
        }
    };
}
