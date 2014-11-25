package py.fpuna.tesis.qoetest.utils;

import java.util.ArrayList;

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

    public static double getPromedio(ArrayList<Long> data){
        int size = data.size();
        long total = 0;
        for(Long dato : data){
            total += dato;
        }
        return Math.ceil(total/size);

    }
}
