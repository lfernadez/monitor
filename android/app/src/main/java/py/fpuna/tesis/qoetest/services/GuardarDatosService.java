package py.fpuna.tesis.qoetest.services;

import android.app.IntentService;
import android.content.ContentProviderOperation;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.os.RemoteException;

import java.util.ArrayList;

import py.fpuna.tesis.qoetest.database.DatabaseContract;
import py.fpuna.tesis.qoetest.model.Localizacion;
import py.fpuna.tesis.qoetest.utils.Constants;


/**
 * An {@link android.app.IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 */
public class GuardarDatosService extends IntentService {

    public static final String NAME = "GuardarDatosService";
    /**
     * Indica si el servicicio se esta ejecutando
     */
    private static volatile boolean isServiceRunning;

    public GuardarDatosService() {
        super(NAME);
    }

    /**
     * Indica si el servicio se esta ejecutando
     *
     * @return {@code true} si el servicio se esta ejecutando
     */
    public static boolean isServiceRunning() {
        return isServiceRunning;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        isServiceRunning = true;
        handleIntent(intent);
        isServiceRunning = false;
    }

    private void handleIntent(Intent intent) {
        Localizacion localizacion = intent.getParcelableExtra(Constants.EXTRA_LOCALIZACION);

        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

        ops.add(ContentProviderOperation.newInsert(DatabaseContract.Localizacion.CONTENT_URI)
                .withValue(DatabaseContract.Localizacion.FECHA, localizacion.getFecha())
                .withValue(DatabaseContract.Localizacion.HORA, localizacion.getHora())
                .withValue(DatabaseContract.Localizacion.LATITUD, localizacion.getLatitud())
                .withValue(DatabaseContract.Localizacion.LONGITUD, localizacion.getLongitud())
                .withValue(DatabaseContract.Localizacion.PRECISION, localizacion.getPrecision())
                .withValue(DatabaseContract.Localizacion.IMEI, localizacion.getImei())
                .withValue(DatabaseContract.Localizacion.TIPO_MUESTRA, localizacion.getTipoMuestra())
                .withYieldAllowed(true).build());

        try {
            getContentResolver().applyBatch(DatabaseContract.CONTENT_AUTHORITY, ops);
        } catch (RemoteException e) {
            //bus.post(new GuardarPedidoEvent(Tipo.ERROR, "No se pudo guardar el pedido"));
            return;
        } catch (OperationApplicationException e) {
            //bus.post(new GuardarPedidoEvent(Tipo.ERROR, "No se pudo guardar el pedido"));
            return;
        }
    }
}
