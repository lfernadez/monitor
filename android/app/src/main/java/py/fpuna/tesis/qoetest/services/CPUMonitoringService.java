package py.fpuna.tesis.qoetest.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class CPUMonitoringService extends IntentService {
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_GET_USAGE = "py.fpuna.tesis.qoetest" +
            ".services.action.CPU_USAGE";
    private BufferedReader reader;

    private static final String EXTRA_PARAM1 = "py.fpuna.tesis.qoetest.services.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "py.fpuna.tesis.qoetest.services.extra.PARAM2";

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, CPUMonitoringService.class);
        intent.setAction(ACTION_GET_USAGE);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    public CPUMonitoringService() {
        super("CPUMonitoringService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_GET_USAGE.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleAction(param1, param2);
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleAction(String param1, String param2) {
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
    }
}
