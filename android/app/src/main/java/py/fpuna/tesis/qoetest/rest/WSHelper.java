package py.fpuna.tesis.qoetest.rest;

import android.util.Log;

import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by User on 10/10/2014.
 */
public class WSHelper {

    private static final String TAG = "WebServices";
    private static final String URL = "/IndufarVentasWeb/rest/";
    private static final String HOST = "mail.indufar.com.py";
    private static final int PORT = 80;

    private Gson gson = new Gson();

    /**
     * Realiza una invocacion HTTP y retorna la respuesta obtenida.
     *
     * @param request
     *            Peticion HTTP
     * @return Respuesta obtenida o <code>null</code> si no se obtuvo ninguna
     *         respuesta
     */
    private HttpResponse invoke(HttpRequest request) {
        request.addHeader("Content-Type", "application/json");
        HttpHost host = new HttpHost(HOST, PORT);
        HttpClient client = new DefaultHttpClient();
        HttpResponse response = null;
        try {
            response = client.execute(host, request);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.getMessage());
        }
        return response;
    }

    /**
     * @param response
     * @return
     */
    private String proccess(HttpResponse response) {
        if (response == null || response.getStatusLine().getStatusCode() != 200) {
            return null;
        } else {
            String responseText = null;
            HttpEntity entity = null;
            entity = response.getEntity();
            try {
                byte[] data = EntityUtils.toByteArray(entity);
                responseText = new String(data);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (entity != null) {
                    try {
                        entity.consumeContent();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return responseText;
        }
    }

    /**
     * Realiza una invocacion GET y retorna una cadena que contiene el cuerpo de
     * la respuesta obtenida.
     *
     * @param url
     *            URL a invocar
     * @return Texto de la respuesta o <code>null</code> si no se obtuvo ningua
     *         respuesta
     */
    private String get(String url) {
        Log.d(TAG, "GET Method: " + url);
        HttpGet request = new HttpGet(url);
        HttpResponse response = invoke(request);
        return proccess(response);
    }

    /**
     * Realiza una invocacion DELETE y retorna una cadena que contiene el cuerpo
     * de la respuesta obtenida.
     *
     * @param url
     *            URL a invocar
     * @return Texto de la respuesta o <code>null</code> si no se obtuvo ningua
     *         respuesta
     */
    private String delete(String url) {
        url = URL + url;
        Log.d(TAG, "DELETE Method: " + url);
        HttpDelete request = new HttpDelete(url);
        HttpResponse response = invoke(request);
        return proccess(response);
    }

    /**
     * Realiza una invocacion PUT y retorna una cadena que contiene el cuerpo de
     * la respuesta obtenida.
     *
     * @param url
     *            URL a invocar
     * @return Texto de la respuesta o <code>null</code> si no se obtuvo ningua
     *         respuesta
     */
    private String put(String url) {
        url = URL + url;
        Log.d(TAG, "PUT Method: " + url);
        HttpPut request = new HttpPut(url);
        HttpResponse response = invoke(request);
        return proccess(response);
    }

    /**
     * Realiza una invocacion POST y retorna una cadena que contiene el cuerpo
     * de la respuesta obtenida.
     *
     * @param url
     *            URL a invocar
     * @param datos
     *            Datos a enviar
     * @return Texto de la respuesta o <code>null</code> si no se obtuvo ningua
     *         respuesta
     */
    private String post(String url, String datos) {
        url = URL + url;
        Log.d(TAG, "POST Method: " + url);
        HttpPost request = new HttpPost(url);
        try {
            request.setEntity(new StringEntity(datos));
        } catch (UnsupportedEncodingException e1) {
            return null;
        }
        HttpResponse response = invoke(request);
        return proccess(response);
    }
}
