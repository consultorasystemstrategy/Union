/*
package union.union_vr1.SyncAdapter;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import union.union_vr1.Login;

*/
/**
 * Created by Usuario on 18/12/2015.
 *//*

public class AccountAuthenticator extends AbstractAccountAuthenticator {


    public static final String ACCOUNT_TYPE = "union.union_vr1";
    public static final String AUTHTOKEN_TYPE = "normal";

    private Context mContext;
    public AccountAuthenticator(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public Bundle editProperties(AccountAuthenticatorResponse response, String accountType) {
        return null;
    }
    */
/**

              Llamado cuando el usuario quiere loguearse y anadir un nuevo usuario.
              @return bundle con intent para iniciar AuthenticatorActivity.
             *//*

    @Override
    public Bundle addAccount(AccountAuthenticatorResponse response, String accountType, String authTokenType, String[] requiredFeatures, Bundle options) throws NetworkErrorException {
        final Intent intent = new Intent(mContext, Login.class);
        intent.putExtra(Login.ARG_ACCOUNT_TYPE, accountType);
        intent.putExtra(Login.ARG_AUTH_TYPE, authTokenType);
        intent.putExtra(Login.ARG_IS_ADDING_NEW_ACCOUNT, true);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
        final Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);
        return bundle;
    }

    @Override
    public Bundle confirmCredentials(AccountAuthenticatorResponse response, Account account, Bundle options) throws NetworkErrorException {
        return null;
    }

    */
/**
     * Obtiene el token de una cuenta. Si falla, se avisa que se debe llamar a AuthenticatorActivity.
     * @return Si resulta, bundle con informacion de cuenta y token.   Si falla, bundle con
     * informacion de cuenta y activity.
     *//*

    @Override
    public Bundle getAuthToken(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        // Extrae username y pass del account manager
        final AccountManager am = AccountManager.get(mContext);

        // Pide el authtoken
        String authToken = am.peekAuthToken(account, authTokenType);

        // Intento de autenticar al usuario
        if (TextUtils.isEmpty(authToken)){
            final String password = am.getPassword(account);
            if (password != null) {
                // Se autentica en el servidor
                //CREAR UN MÉTODO ASÍNCRONO PARA AUTENTICAR NUEVAMENTE
                //authToken = authenticateInServer(account);
            }
        }
        // Si obtenemos un authToken, lo retornamos
        if (!TextUtils.isEmpty(authToken)) {

            final Bundle result = new Bundle();
            result.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
            result.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type);
            result.putString(AccountManager.KEY_AUTHTOKEN, authToken);

            return result;
        }
        // Si llegamos acá aún no podemos obtener el token.
        // Necesitamos pedirle de nuevo las credenciales
        final Intent intent = new Intent(mContext, Login.class);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
        intent.putExtra(Login.ARG_ACCOUNT_TYPE, account.type);
        intent.putExtra(Login.ARG_AUTH_TYPE, authTokenType);

        final Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);
        return bundle;
    }

    @Override
    public String getAuthTokenLabel(String authTokenType) {
        return null;
    }

    @Override
    public Bundle updateCredentials(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        return null;
    }

    @Override
    public Bundle hasFeatures(AccountAuthenticatorResponse response, Account account, String[] features) throws NetworkErrorException {
        return null;
    }
}
*/
