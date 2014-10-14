package py.fpuna.tesis.qoetest.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by LF on 14/10/2014.
 */
public class DeviceStatusUtils {
    private BufferedReader reader;

    public long getCPULoad(){
        long carga = 0;
        try {
            // Llamada al comando top desde un proceso
            Process p = Runtime.getRuntime().exec("/system/bin/top  -m 1 -d 1 -n 1");
            p.waitFor();
            reader = new BufferedReader(new InputStreamReader(
                    p.getInputStream()));
            // Parseo del resultado de top
            String line = reader.readLine();
            while ((line = reader.readLine()).isEmpty()) {
            }
            String[] porcentajes = line.split(", ");
            for (String item : porcentajes) {
                String[] porcentaje = item.split(" ");
                carga += Long.valueOf(porcentaje[1].substring(0,
                        porcentaje[1].length() - 1));
            }
            reader.close();

            p.destroy();  // Se destruye el proceso creado
        }catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return carga;
    }
}