package py.fpuna.tesis.qoetest.utils;

/**
 * Created by User on 01/10/2014.
 */
public class CalcUtils {
    public static double getPromedio(Float... valores){
        int size = valores.length;
        float total = 0;
        for(Float valor: valores){
            total += valor;
        }
        return Math.ceil(total/size);
    }
}
