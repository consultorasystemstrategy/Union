package union.union_vr1.FacturacionElectronica;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.crypto.MarshalException;
import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import union.union_vr1.R;
import union.union_vr1.Utils.Utils;

public class SignatureActivity extends Activity {

    private TextView textView;
    private EditText editText;
    private Button button;
    private Context contexto;
    String textoSHA1 = null;
    private final static String BKS  ="union.bks";


    private File documentoFirmado;
    //private Document docSinFirmar;
    public InputStream docSinFirmar;
    public InputStream keystore;
    Map<String, String> map = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature);
        contexto = getApplicationContext();


        textView = (TextView) findViewById(R.id.textView);
        editText = (EditText) findViewById(R.id.editText);
        button = (Button) findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String digestValue = null;
                try {
                    //File filesinFirmar = SimpleXMLAndroid.generarXMLown(File.createTempFile("20138122256-01-F104-00000011", "XML"));
                     /*digestValue = Signature.add(keystore, filesinFirmar, createFile("20138122256-01-F104-00000011.XML"));*/

                    //POR DEFECTO FACTURA
                    String tipo_doccumento = "01";



                    map.put("tipo_documento", tipo_doccumento);
                    map.put("id_documento", "6660666");
                    map.put("user_ruc_dni", "7770777");
                    map.put("user_name", "Steve Campos Vega");
                    map.put("total_operaciones_gravadas", Utils.formatDouble(666.0));
                    map.put("total_importe_venta",Utils.formatDouble(666.0));
                    map.put("total__igv", Utils.formatDouble(333.0));


                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    //textView.setText("DIGESTVALUE: "+digestValue);
                }

                File filesinFirmar = null;
                try {
                    Cursor cursor = null;
                    filesinFirmar = SimpleXMLAndroid.generateCreditNote(File.createTempFile(contexto.getString(R.string.RUC) + "-" + "F" + "-" + "666000666","XML"), map, cursor);
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    try {
                        keystore = getFilefromAssets(BKS);
                        textoSHA1 = Signature.add(keystore, filesinFirmar, createFile(contexto.getString(R.string.RUC) + "-" + "F" + "-" + "666000666" + ".XML"));

                        textView.setText("DIGESTVALUE: "+textoSHA1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_signature, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


        File createFile(String pathFile)
                throws IOException, ParserConfigurationException, SAXException {
            File file = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS), pathFile);
            //return File.createTempFile(pathFile,"xml",contexto.getCacheDir());
            return file;
        }





        InputStream getFilefromAssets(String nameDocument)
                throws IOException
        {
            AssetManager am = contexto.getAssets();
            return am.open(nameDocument);
        }

        public static String readFileAsString(File file) {
            String result = "";
            //byte[] buffer = new byte[(int) new File(filePath).length()];
            FileInputStream fis = null;
            try {
                //f = new BufferedInputStream(new FileInputStream(filePath));
                //f.read(buffer);

                fis = new FileInputStream(file);
                char current;
                while (fis.available() > 0) {
                    current = (char) fis.read();
                    result = result + String.valueOf(current);

                }

            } catch (Exception e) {
                Log.d("TourGuide", e.toString());
            } finally {
                if (fis != null)
                    try {
                        fis.close();
                    } catch (IOException ignored) {
                    }
            }
            //result = new String(buffer);

            return result;
        }


        /* Checks if external storage is available for read and write */
        public boolean isExternalStorageWritable() {
            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                return true;
            }
            return false;
        }

        /* Checks if external storage is available to at least read */
        public boolean isExternalStorageReadable() {
            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state) ||
                    Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
                return true;
            }
            return false;
        }
}
