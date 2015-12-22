/*
package union.union_vr1.SyncAdapter;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

*/
/**
 * Created by Usuario on 21/12/2015.
 *//*

public class SyncAdapter extends AbstractThreadedSyncAdapter {

    private ContentResolver mContentResolver;
    private String mToken;
    private AccountManager mAccountManager;

    private static String TAG = "Steve.SyncAdapter";

    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContentResolver = context.getContentResolver();
        mAccountManager = AccountManager.get(context);
        Log.d(TAG, "Sync Adapter Constructor...");

    }

    */
/**
     * Llamado cuando se sincroniza. Acá se debe hacer el llamado al servidor y la
     * actualizacion de datos locales.
     * @param account
     * @param extras
     * @param authority
     * @param provider
     * @param syncResult
     *//*

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {

        try {

            Log.d(TAG, "OnPerformSync...");
            // No importa que el thread se bloquee ya que es asincrónico.
            mToken = mAccountManager.blockingGetAuthToken(account,
                    AccountAuthenticator.ACCOUNT_TYPE, true);

            //LÓGICO DE SINCRONIZACIÓN
            //CONSUMO EL MÉTODO PARA EXPORTAR LOS COMPROBANTES AL FLEX.
            //ACTUALIZO MI TABLA SQLITE
            //ENJOY FUCK OFF

*/
/*
            Request request = Request.createRequest(Request.GET);
            ArrayList<String> results = request.perform(mToken);*//*


            */
/*if (results.size() == 1){
                updateData(results);
            }*//*

            // Manejo de errores
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
*/
