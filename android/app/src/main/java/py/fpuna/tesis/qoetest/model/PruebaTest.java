package py.fpuna.tesis.qoetest.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by LF on 14/10/2014.
 */
public class PruebaTest implements Parcelable{
    private int codigoTest;
    private double valorMos;

    public PruebaTest() {
    }

    public int getCodigoTest() {
        return codigoTest;
    }

    public void setCodigoTest(int codigoTest) {
        this.codigoTest = codigoTest;
    }

    public double getValorMos() {
        return valorMos;
    }

    public void setValorMos(double valorMos) {
        this.valorMos = valorMos;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.codigoTest);
        parcel.writeDouble(this.valorMos);
    }

    public PruebaTest(Parcel in) {
        this.codigoTest = in.readInt();
        this.valorMos = in.readDouble();
    }

    public static final Creator<PruebaTest> CREATOR = new Creator<PruebaTest>() {
        @Override
        public PruebaTest createFromParcel(Parcel in) {
            return (new PruebaTest(in));
        }

        @Override
        public PruebaTest[] newArray(int size) {
            return (new PruebaTest[size]);
        }
    };
}
