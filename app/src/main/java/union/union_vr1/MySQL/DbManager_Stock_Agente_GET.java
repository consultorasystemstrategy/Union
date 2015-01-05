package union.union_vr1.MySQL;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import union.union_vr1.Conexion.JSONParser;
import union.union_vr1.Sqlite.DbAdapter_Stock_Agente;

/**
 * Created by CCIE on 16/12/2014.
 */
public class DbManager_Stock_Agente_GET{

    private DbAdapter_Stock_Agente manager;
    JSONParser jsonParser = new JSONParser();
    private static String url_agente = "http://192.168.0.158:8083/produnion/lis_stock_agente.php";
    private static final String TAG_SUCCESS = "success";
    JSONArray stock_agente;
    public int i;


    public static final String TAG_stock_agente = "stock_agente";
    public static final String TAG_id_producto = "id_producto";
    public static final String TAG_nombre = "nombre";
    public static final String TAG_codigo = "codigo";
    public static final String TAG_codigo_barras = "codigo_barras";
    public static final String TAG_inicial = "inicial";
    public static final String TAG_final = "final";
    public static final String TAG_disponible = "disponible";
    public static final String TAG_ventas = "ventas";
    public static final String TAG_canjes = "canjes";
    public static final String TAG_devoluciones = "devoluciones";
    public static final String TAG_buenos = "buenos";
    public static final String TAG_malos = "malos";
    public static final String TAG_fisico = "fisico";
    public static final String TAG_id_agente = "id_agente";

    protected String doInBackground(String... args) {

        manager = new DbAdapter_Stock_Agente(new DbManager_Agente_GET());
        manager.open();
        manager.deleteAllStockAgente();

        Bundle bundle = new Intent().getExtras();
        String id_agente_venta = bundle.getString("putIdAgenteVenta");
        //Aqui se lista los parametros a enviar al PHP
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("id_agente_venta", id_agente_venta));
        // getting JSON string from URL
        JSONObject json = jsonParser.makeHttpRequest(url_agente, "GET", params);
        //Impresion en consola de prueba para verificar si se recibió algún dato del PHP
        Log.d("stock agente de venta: ", json.toString());

        try {
            //Aqui se verifica si se envio recibio los datos correctamente
            int success = json.getInt(TAG_SUCCESS);
            if (success == 1) {
                // Aqui se inserta la lista de datos en un JSON array
                stock_agente = json.getJSONArray(TAG_stock_agente);


                //Se recorre el array del agente
                for (i = 0; i < stock_agente.length(); i++) {
                    JSONObject c = stock_agente.getJSONObject(i);

                    //Aqui se hace uso de las variables recibidas para cualquier metodo de base de datos
                    manager.createStockAgente(
                            c.getInt(TAG_id_producto),
                            c.getString(TAG_nombre),
                            c.getString(TAG_codigo),
                            c.getString(TAG_codigo_barras),
                            c.getInt(TAG_inicial),
                            c.getInt(TAG_final),
                            c.getInt(TAG_disponible),
                            c.getInt(TAG_ventas),
                            c.getInt(TAG_canjes),
                            c.getInt(TAG_devoluciones),
                            c.getInt(TAG_buenos),
                            c.getInt(TAG_malos),
                            c.getInt(TAG_fisico),
                            c.getInt(TAG_id_agente)
                    );
                }

            } else {
                Log.d("Fallo Sincronizacion!", json.getString(TAG_SUCCESS));
                return json.getString(TAG_SUCCESS);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
