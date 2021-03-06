package py.fpuna.tesis.qoetest.rest;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.List;

import py.fpuna.tesis.qoetest.model.DeviceLocation;
import py.fpuna.tesis.qoetest.model.DeviceStatus;
import py.fpuna.tesis.qoetest.model.NetworkStat;
import py.fpuna.tesis.qoetest.model.PerfilUsuario;
import py.fpuna.tesis.qoetest.model.PhoneInfo;
import py.fpuna.tesis.qoetest.model.Prueba;
import py.fpuna.tesis.qoetest.model.PruebaTest;
import py.fpuna.tesis.qoetest.model.QoSParam;

/**
 * Created by User on 10/10/2014.
 */
public class WSHelper {

    private static final String TAG = "WebServices";
    private static final String URL = "/tesis/servicios/";
    private static final String URL_CONFIG = "/config/servicios/";
    private static final String HOST_PERSISTENCIA = "192.168.1.101";
    private static final String HOST_CONF = "192.168.1.101";
    private static final int PORT = 8081;

    private final Charset UTF8_CHARSET = Charset.forName("UTF-8");

    private Gson gson = new Gson();

    /**
     * Realiza una invocacion HTTP y retorna la respuesta obtenida.
     *
     * @param request Peticion HTTP
     * @return Respuesta obtenida o <code>null</code> si no se obtuvo ninguna
     * respuesta
     */
    private HttpResponse invoke(HttpRequest request) {
        request.addHeader("Content-Type", "application/json");
        HttpHost host = new HttpHost(HOST_PERSISTENCIA, PORT);
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
     * Realiza una invocacion HTTP y retorna la respuesta obtenida.
     *
     * @param request Peticion HTTP
     * @return Respuesta obtenida o <code>null</code> si no se obtuvo ninguna
     * respuesta
     */
    private HttpResponse invokePostConf(HttpRequest request) {
        request.addHeader("Content-Type", "application/json");
        HttpHost host = new HttpHost(HOST_CONF, PORT);
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
     * @param url URL a invocar
     * @return Texto de la respuesta o <code>null</code> si no se obtuvo ningua
     * respuesta
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
     * @param url URL a invocar
     * @return Texto de la respuesta o <code>null</code> si no se obtuvo ningua
     * respuesta
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
     * @param url URL a invocar
     * @return Texto de la respuesta o <code>null</code> si no se obtuvo ningua
     * respuesta
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
     * @param url   URL a invocar
     * @param datos Datos a enviar
     * @return Texto de la respuesta o <code>null</code> si no se obtuvo ningua
     * respuesta
     */
    private String post(String url, String datos) {
        url = URL + url;
        Log.d(TAG, "POST Method: " + url);
        HttpPost request = new HttpPost(url);
        try {
            request.setEntity(new StringEntity(datos,HTTP.UTF_8));
        } catch (UnsupportedEncodingException e1) {
            return null;
        }
        HttpResponse response = invoke(request);
        return proccess(response);
    }

    /**
     * Realiza una invocacion POST y retorna una cadena que contiene el cuerpo
     * de la respuesta obtenida.
     *
     * @param url   URL a invocar
     * @param datos Datos a enviar
     * @return Texto de la respuesta o <code>null</code> si no se obtuvo ningua
     * respuesta
     */
    private String postConf(String url, String datos) {
        url = URL_CONFIG + url;
        Log.d(TAG, "POST Method: " + url);
        HttpPost request = new HttpPost(url);
        try {
            request.setEntity(new StringEntity(datos));
        } catch (UnsupportedEncodingException e1) {
            return null;
        }
        HttpResponse response = invokePostConf(request);
        return proccess(response);
    }

    /**
     *
     * @return
     */
    public NetworkStat obtenerParametros(){
        String jsonParametros = "";
        jsonParametros = postConf("parametros/", "");
        Gson gson = new Gson();
        Type tipo = new TypeToken<NetworkStat>() {
        }.getType();

        NetworkStat networkParam = gson.fromJson(jsonParametros, tipo);
        return networkParam;
    }


    /**
     *
     * @param pu
     * @param info
     * @param deviceStatus
     * @param location
     * @param test
     * @param qoSParams
     * @param fecha
     * @param hora
     * @return
     */
    public String enviarResultados(PerfilUsuario pu, PhoneInfo info,
                                   DeviceStatus deviceStatus,
                                   DeviceLocation location,
                                   List<PruebaTest> test,
                                   List<QoSParam> qoSParams,
                                   String fecha, String hora) {
        String respuesta = "";
        JSONObject datosJSON = new JSONObject();
        try {
            Prueba prueba = new Prueba();
            prueba.setFecha(fecha);
            prueba.setHora(hora);
            prueba.setTelefono(info);
            prueba.setPruebaQoS(qoSParams);
            prueba.setPruebaTests(test);
            prueba.setDatosUsuario(pu);
            /* Datos del telefono
            datosJSON.put("telefono", gson.toJson(info));
            /*Datos del Usuario
            datosJSON.put("datosUsuario", gson.toJson(pu));
            /* Datos del estado del telefono
            // datosJSON.put("estadoTelefono", gson.toJson(deviceStatus));
            /* Resultados de las pruebas
            datosJSON.put("pruebaTests", gson.toJson(test));
            /* Localizacion */
            //datosJSON.put("localizacion", gson.toJson(location));
            /* Valores de los parametros QoS
            datosJSON.put("pruebaQos", gson.toJson(qoSParams));
            /* Fecha
            datosJSON.put("fecha", fecha);
            /* Hora
            datosJSON.put("hora", hora);*/
            String jsonString = new Gson().toJson(prueba);
            respuesta = post("prueba/", jsonString);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return respuesta;
    }
}
