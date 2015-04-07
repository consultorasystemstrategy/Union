package union.union_vr1.Vistas;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import union.union_vr1.R;
import union.union_vr1.Sqlite.CursorAdapter_Facturas_Canjes_Dev;
import union.union_vr1.Sqlite.CursorAdapter_No_Encontrado;
import union.union_vr1.Sqlite.DbAdapter_Canjes_Devoluciones;
import union.union_vr1.Sqlite.DbAdapter_Temp_Session;

import static union.union_vr1.R.layout.*;

public class VMovil_Facturas_Canjes_Dev extends Activity {
    View layoutNoEncontrado;
    private int idProducto;
    private DbAdapter_Canjes_Devoluciones dbHelperCanjes_Dev;
    private ListView listaFacturas;
    private int stock;
    private String idEstablec;
    private int idAgente;
    private String nomProducto;
    private int idCategoriaEstablec;
    private String nomEstablecimiento;
    private int cantidadV;
    private int devueltoV;
    private String valorUnidad;
    private Context ctx = this;
    DbAdapter_Temp_Session dbAdapter_temp_session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(princ_facturas_canjes_dev);
        dbAdapter_temp_session = new DbAdapter_Temp_Session(ctx);
        dbAdapter_temp_session.open();


        //--------------------------------------------

        dbHelperCanjes_Dev = new DbAdapter_Canjes_Devoluciones(this);
        dbHelperCanjes_Dev.open();

        listaFacturas = (ListView) findViewById(R.id.facturasbyid);

        Bundle bd = getIntent().getExtras();
        idProducto = bd.getInt("idProducto");
        idEstablec = bd.getString("idEstablec");
        idAgente = bd.getInt("idAgente");
        nomProducto = bd.getString("nomProducto");
        valorUnidad = bd.getString("valorUnidad");
        Cursor cr = dbHelperCanjes_Dev.nom_establecimiento(idEstablec);
        cr.moveToFirst();
        nomEstablecimiento = cr.getString(1);
        idCategoriaEstablec = cr.getInt(3);
        imprimeStock(idAgente, idProducto);
        listarFacturas_Productos(idProducto, idAgente, idEstablec, nomProducto);
    }

    private void imprimeStock(int id, int producto) {
        int liquidacion = dbAdapter_temp_session.fetchVarible(3);
        TextView stockView = (TextView) findViewById(R.id.stock_can_dev);

        Cursor cr = dbHelperCanjes_Dev.obtenerStock(producto, liquidacion);
        if (cr.moveToFirst()) {
            stock = cr.getInt(cr.getColumnIndex("disponible"));

            if (stock < 0) {
                stock = 0;
                stockView.setText("Stock Disponible: " + stock);
            } else {

                stockView.setText("Stock Disponible: " + stock);
            }

        } else {
            Log.e("idProducto", idProducto + "-" + stock + cr.getCount() + idAgente + "-" + liquidacion);
            stockView.setText("No Cuenta con Stock de este Producto");
        }


    }


    private void listarFacturas_Productos(int producto, final int idAgente, final String idEstablec, final String nomProducto) {
        Cursor cr = dbHelperCanjes_Dev.listaFacturasByProducto(producto, idAgente, idEstablec);


        layoutNoEncontrado= View.inflate(this, R.layout.info_producto_no_encontrado, null);
        if (cr.moveToFirst()) {
            if (cr.getString(10) == null || cr.getString(10).equals("")) {



                ArrayList<String> noEncontrado = new ArrayList<String>();
                noEncontrado.add("");


                ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, noEncontrado);
                View noE =   View.inflate(this, R.layout.info_producto_no_encontrado, null);
                final Button btnRegresar = (Button) noE.findViewById(R.id.btn_regresar);
                btnRegresar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent back = new Intent(getApplicationContext(), VMovil_Evento_Canjes_Dev.class);
                        back.putExtra("idEstabX", idEstablec);
                        back.putExtra("idAgente", idAgente);
                        startActivity(back);
                        finish();
                    }
                });
                final Button btnAgregar = (Button) noE.findViewById(R.id.btn_agregar);
                btnAgregar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        mostrar_alertdialog_spinners(nomProducto);
                    }
                });
                listaFacturas.addHeaderView(noE);
                listaFacturas.setAdapter(adapter);

            } else {
                LinearLayout linearLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.agregar_otro, null);
                TextView vieFooter = (TextView)  linearLayout.findViewById(R.id.textViewNoEncontrado);
                //vieFooter.setText("Agregar Otro");
                listaFacturas.addFooterView(linearLayout);
                vieFooter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (stock == 0) {
                            Toast.makeText(getApplicationContext(), "Tiene Stock 0", Toast.LENGTH_SHORT).show();
                            mostrar_alertdialog_spinners_dev(nomProducto);
                        } else {
                            mostrar_alertdialog_spinners(nomProducto);
                        }
                    }
                });
                Cursor cr2 = dbHelperCanjes_Dev.listaFacturasByProducto2(producto, idAgente, idEstablec);
                CursorAdapter_Facturas_Canjes_Dev cFac_Can_Dev = new CursorAdapter_Facturas_Canjes_Dev(getApplicationContext(), cr2);
                listaFacturas.setAdapter(cFac_Can_Dev);
                listaFacturas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Cursor cursor = (Cursor) adapterView.getItemAtPosition(i);
                        if (stock == 0) {
                            mostrar_alertdialog_spinners_regitrados_dev(cursor);
                            Toast.makeText(getApplicationContext(), "Tiene Stock 0", Toast.LENGTH_SHORT).show();
                        } else {
                            int canjes = 0;
                            if(cursor.getString(cursor.getColumnIndexOrThrow("hd_in_cantidad_ope"))!=null){
                               canjes = cursor.getInt(cursor.getColumnIndexOrThrow("hd_in_cantidad_ope"));
                            }

                            int devoluciones = 0;

                            if(cursor.getString(cursor.getColumnIndexOrThrow("hd_in_cantidad_ope_dev"))!=null){
                              devoluciones = cursor.getInt(cursor.getColumnIndexOrThrow("hd_in_cantidad_ope_dev"));
                            }
                            int sumacandev  = canjes+devoluciones;
                            int total = cursor.getInt(cursor.getColumnIndexOrThrow("hd_in_cantidad"));
                            if(total-sumacandev==0) {
                                Toast.makeText(getApplicationContext(),"Ya no puede realizar mas operaciones",Toast.LENGTH_SHORT).show();
                            }else{
                                mostrar_alertdialog_spinners_regitrados(cursor);
                            }

                        }
                    }
                });
            }

        } else {
            ArrayList<String> noEncontrado = new ArrayList<String>();
            noEncontrado.add("");


            ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, noEncontrado);
            View noE =   View.inflate(this, R.layout.info_producto_no_encontrado, null);
            final Button btnRegresar = (Button) noE.findViewById(R.id.btn_regresar);
            btnRegresar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent back = new Intent(getApplicationContext(), VMovil_Evento_Canjes_Dev.class);
                    back.putExtra("idEstabX", idEstablec);
                    back.putExtra("idAgente", idAgente);
                    startActivity(back);
                    finish();
                }
            });
            final Button btnAgregar = (Button) noE.findViewById(R.id.btn_agregar);
            btnAgregar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    mostrar_alertdialog_spinners(nomProducto);
                }
            });
            listaFacturas.addHeaderView(noE);
            listaFacturas.setAdapter(adapter);

        }


    }


    //cuando hay registros anteriores

    private void mostrar_alertdialog_spinners_regitrados_dev(Cursor cursor) {

        cantidadV = cursor.getInt(10);
        devueltoV = cursor.getInt(17) + cursor.getInt(25);
        final int dev = cursor.getInt(25);
        final int can = cursor.getInt(17);
        String cantidad = cursor.getString(10);
        final String idDetalle = cursor.getString(1);
        String comprobante = cursor.getString(7);
        //-----------------------------------------
        String[] datos = comprobante.split("/");
        //-------
        final String importe = datos[3];
        //-----------------------------------------
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("" + nomProducto + "");

        final View layout_spinners = View.inflate(this, R.layout.prompts_canjes, null);
        final EditText cantidadText = (EditText) layout_spinners.findViewById(R.id.cantidad_can_dev_registrado);
        final Spinner spinnerTipoOp = (Spinner) layout_spinners.findViewById(R.id.can_dev_tipo_op_registrado);
        final Spinner spinnerCategoria = (Spinner) layout_spinners.findViewById(R.id.can_dev_categoria_registrado);
        if (cantidad.equals("1")) {
            cantidadText.setText(cantidad);
            cantidadText.setEnabled(false);
        }


        cantidadText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (cantidadText.getText().toString().trim() != "") {
                    cantidadText.setError(null);
                    cantidadText.setTextColor(getApplicationContext().getResources().getColor(R.color.Dark1));
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                int r = cantidadV - devueltoV;


                if (charSequence.length() != 0) {
                    String nro = charSequence.toString();
                    int charSec = Integer.parseInt(nro);
                    if (charSec > r) {

                        cantidadText.setText("");
                        Toast.makeText(getApplicationContext(), "Cantidad tiene que ser Menor o Igual: " + r + "", Toast.LENGTH_SHORT).show();
                    }
                    if (charSec == 0) {
                        cantidadText.setText("");
                        Toast.makeText(getApplicationContext(), "La Cantidad no puede ser 0", Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (cantidadText.getText().toString().trim().equals("")) {
                    cantidadText.setError("Es Requerido");
                } else {
                    cantidadText.setError(null);
                    cantidadText.setTextColor(getApplicationContext().getResources().getColor(R.color.Dark1));
                }
            }
        });

        builder.setView(layout_spinners);
        builder.setCancelable(true);
        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        String cantidad = cantidadText.getText().toString();
                        String tipo_op = spinnerTipoOp.getSelectedItem().toString();
                        String categoria_op = spinnerCategoria.getSelectedItem().toString();
                        if (cantidad.trim().equals("")) {
                            cantidadText.setError("Es Requerido");
                            Toast.makeText(getApplicationContext(), "Por favor Ingrese Todos los Campos", Toast.LENGTH_SHORT).show();

                        } else {
                            int posicion = spinnerTipoOp.getSelectedItemPosition();
                            if(posicion==0){

                                if(stock>0){
                                    actualizar_can_dev(tipo_op, categoria_op, cantidad, importe, idDetalle, dev, can);
                                }else{
                                    Toast.makeText(getApplicationContext(),"No Tiene Stock Suficiente",Toast.LENGTH_SHORT).show();
                                }



                            }if(posicion==1){
                                actualizar_can_dev(tipo_op, categoria_op, cantidad, importe, idDetalle, dev, can);

                            }

                        }
                    }
                });
        builder.show();


        //Creamos el adaptador, ARRAY EN XML CANJE O DEVOLUCIÓN
        ArrayAdapter<CharSequence> adapterTipoOperacion = ArrayAdapter.createFromResource(this, R.array.tipo_devolucion, android.R.layout.simple_spinner_item);
        //Añadimos el layout para el TIPO DE OPERACIÓN //CANJE  O DEVOLUCIÓN
        adapterTipoOperacion.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Le indicamos al spinner el adaptador a usar
        spinnerTipoOp.setAdapter(adapterTipoOperacion);

        //Creamos el adaptador, ARRAY EN XML BUENO, MALO, VENCIMIENTO MALO, ETC
        ArrayAdapter<CharSequence> adapterCategoria = ArrayAdapter.createFromResource(this, R.array.cate_devolucion, android.R.layout.simple_spinner_item);
        //Añadimos el layout para el TIPO DE OPERACIÓN //CANJE  O DEVOLUCIÓN
        adapterCategoria.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Le indicamos al spinner el adaptador a usar
        spinnerCategoria.setAdapter(adapterCategoria);


    }

    private void mostrar_alertdialog_spinners_regitrados(Cursor cursor) {

        cantidadV = cursor.getInt(10);
        devueltoV = cursor.getInt(17) + cursor.getInt(25);
        final int dev = cursor.getInt(25);
        final int can = cursor.getInt(17);

        String cantidad = cursor.getString(10);
        final String idDetalle = cursor.getString(1);
        String comprobante = cursor.getString(7);
        //-----------------------------------------
        String[] datos = comprobante.split("/");
        //-------
        final String importe = datos[3];
        //-----------------------------------------
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("" + nomProducto + "");

        final View layout_spinners = View.inflate(this, R.layout.prompts_canjes, null);
        final EditText cantidadText = (EditText) layout_spinners.findViewById(R.id.cantidad_can_dev_registrado);
        final Spinner spinnerTipoOp = (Spinner) layout_spinners.findViewById(R.id.can_dev_tipo_op_registrado);
        final Spinner spinnerCategoria = (Spinner) layout_spinners.findViewById(R.id.can_dev_categoria_registrado);
        if (cantidad.equals("1")) {
            cantidadText.setText(cantidad);
            cantidadText.setEnabled(false);
        }


        cantidadText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (cantidadText.getText().toString().trim() != "") {
                    cantidadText.setError(null);
                    cantidadText.setTextColor(getApplicationContext().getResources().getColor(R.color.Dark1));
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                int r = cantidadV - devueltoV;


                if (charSequence.length() != 0) {
                    String nro = charSequence.toString();
                    int charSec = Integer.parseInt(nro);
                    if (charSec > r) {

                        cantidadText.setText("");
                        Toast.makeText(getApplicationContext(), "Cantidad tiene que ser Menor o Igual: " + r + "", Toast.LENGTH_SHORT).show();
                    }
                    if (charSec == 0) {
                        cantidadText.setText("");
                        Toast.makeText(getApplicationContext(), "La Cantidad no puede ser 0", Toast.LENGTH_SHORT).show();
                    }
                    if (charSec > stock) {
                        cantidadText.setText("");
                        Toast.makeText(getApplicationContext(), "No puede Pasar el Stock", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (cantidadText.getText().toString().trim().equals("")) {
                    cantidadText.setError("Es Requerido");
                } else {
                    cantidadText.setError(null);
                    cantidadText.setTextColor(getApplicationContext().getResources().getColor(R.color.Dark1));
                }
            }
        });

        builder.setView(layout_spinners);
        builder.setCancelable(true);
        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        String cantidad = cantidadText.getText().toString();
                        String tipo_op = spinnerTipoOp.getSelectedItem().toString();
                        String categoria_op = spinnerCategoria.getSelectedItem().toString();
                        if (cantidad.trim().equals("")) {
                            cantidadText.setError("Es Requerido");
                            Toast.makeText(getApplicationContext(), "Por favor Ingrese Todos los Campos", Toast.LENGTH_SHORT).show();

                        } else {
                            int posicion = spinnerTipoOp.getSelectedItemPosition();
                            if(posicion==0){
                                if(stock>0){
                                    actualizar_can_dev(tipo_op, categoria_op, cantidad, importe, idDetalle, dev, can);
                                }else{
                                    Toast.makeText(getApplicationContext(),"No tiene Stock Sufiente",Toast.LENGTH_LONG).show();
                                }

                            }if(posicion==1){
                                actualizar_can_dev(tipo_op, categoria_op, cantidad, importe, idDetalle, dev, can);
                            }



                        }
                    }
                });
        builder.show();



        //Creamos el adaptador, ARRAY EN XML CANJE O DEVOLUCIÓN
        ArrayAdapter<CharSequence> adapterTipoOperacion = ArrayAdapter.createFromResource(this, R.array.tipo_devolucion, android.R.layout.simple_spinner_item);
        //Añadimos el layout para el TIPO DE OPERACIÓN //CANJE  O DEVOLUCIÓN
        adapterTipoOperacion.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Le indicamos al spinner el adaptador a usar
        spinnerTipoOp.setAdapter(adapterTipoOperacion);

            //Creamos el adaptador, ARRAY EN XML BUENO, MALO, VENCIMIENTO MALO, ETC
        ArrayAdapter<CharSequence> adapterCategoria = ArrayAdapter.createFromResource(this, R.array.cate_devolucion, android.R.layout.simple_spinner_item);
        //Añadimos el layout para el TIPO DE OPERACIÓN //CANJE  O DEVOLUCIÓN
        adapterCategoria.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Le indicamos al spinner el adaptador a usar
        spinnerCategoria.setAdapter(adapterCategoria);



    }

    //Actualizar Datos Histo_venta_Detalle
    private void actualizar_can_dev(String tipo_op, String categoria_op, String cantidad, String importe, String idDetalle, int devuelto, int canjeado) {
        int liquidacion = dbAdapter_temp_session.fetchVarible(3);
        if (categoria_op.equals("Bueno")) {
            categoria_op = "1";
        }
        if (categoria_op.equals("Malogrado")) {
            categoria_op = "2";
        }
        if (categoria_op.equals("Reclamo")) {
            categoria_op = "3";
        }
        if (categoria_op.equals("Vencido-Malo")) {
            categoria_op = "4";
        }
        if (tipo_op.equals("Canje")) {
            tipo_op = "1";
            boolean estado = dbHelperCanjes_Dev.update_Canj(tipo_op, categoria_op, cantidad, importe, idDetalle, canjeado, idProducto, ctx, liquidacion);
            if (estado) {
                confirmar();

            } else {
                Toast.makeText(ctx, "Por favor Intente Nuevamente", Toast.LENGTH_SHORT).show();

            }
        }
        if (tipo_op.equals("Devolucion")) {
            tipo_op = "2";
            boolean estado = dbHelperCanjes_Dev.update_dev(tipo_op, categoria_op, cantidad, importe, idDetalle, devuelto, idProducto, ctx, liquidacion);
            if (estado) {
                confirmar();

            } else {
                Toast.makeText(ctx, "Por favor Intente Nuevamente", Toast.LENGTH_SHORT).show();

            }
        }
    }

    //Cuando no se encuentran los registros
    private void mostrar_alertdialog_spinners_dev(String producto) {



        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("" + nomProducto + "");
        final View layout_spinners = View.inflate(this, R.layout.prompts, null);
        final EditText vUnidad = (EditText) layout_spinners.findViewById(R.id.EditUnidadValor);
        final EditText nroCompro = (EditText) layout_spinners.findViewById(R.id.comprob_clave_dev_can);
        final EditText nroLote = (EditText) layout_spinners.findViewById(R.id.lote_can_dev);
        final EditText cantidadText = (EditText) layout_spinners.findViewById(R.id.cantidad_can_dev);
        final Spinner spinnerTipoOp = (Spinner) layout_spinners.findViewById(R.id.can_dev_tipo_op);
        final Spinner spinnerCategoria = (Spinner) layout_spinners.findViewById(R.id.can_dev_categoria);
        vUnidad.setText("HolaMundo");
        Log.e("errorValor Unidad",valorUnidad);
        nroCompro.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (nroCompro.getText().toString().trim() != "") {
                    nroCompro.setError(null);
                    nroCompro.setTextColor(getApplicationContext().getResources().getColor(R.color.Dark1));
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {


            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (nroCompro.getText().toString().trim().equals("")) {
                    nroCompro.setError("Es Requerido");
                } else {
                    nroCompro.setError(null);
                    nroCompro.setTextColor(getApplicationContext().getResources().getColor(R.color.Dark1));
                }
            }
        });

        nroLote.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (nroLote.getText().toString().trim() != "") {
                    nroLote.setError(null);
                    nroLote.setTextColor(getApplicationContext().getResources().getColor(R.color.Dark1));
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (nroLote.getText().toString().trim().equals("")) {
                    nroLote.setError("Es Requerido");
                } else {
                    nroLote.setError(null);
                    nroLote.setTextColor(getApplicationContext().getResources().getColor(R.color.Dark1));
                }
            }
        });


        builder.setView(layout_spinners);
        builder.setCancelable(true);
        builder.setPositiveButton("Guardar",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String compro = nroCompro.getText().toString();
                        String lote = nroLote.getText().toString();
                        String cantidad = cantidadText.getText().toString();
                        String tipo_op = spinnerTipoOp.getSelectedItem().toString();
                        String categoria_op = spinnerCategoria.getSelectedItem().toString();
                        if (compro.trim().equals("") || lote.trim().equals("") || cantidad.trim().equals("")) {
                            if (compro.trim().equals("")) {
                                nroCompro.setError("Es Requerido");
                            }
                            if (lote.trim().equals("")) {
                                nroLote.setError("Es Requerido");
                            }
                            if (cantidad.trim().equals("")) {
                                cantidadText.setError("Es Requerido");
                            }
                            Toast.makeText(getApplicationContext(), "Por favor Ingrese Todos los Campos", Toast.LENGTH_SHORT).show();

                        } else {
                            int position = spinnerTipoOp.getSelectedItemPosition();
                           // Log.e("Posicion",""+position);
                            if(position==0){
                                if(stock>0){
                                    registrarNuevo_Comprobante(compro, lote, cantidad, tipo_op, categoria_op);
                                }else{
                                    Toast.makeText(getApplicationContext(),"No Tiene Stock Suficiente",Toast.LENGTH_LONG).show();
                                }
                            }
                            if(position==1){
                                registrarNuevo_Comprobante(compro, lote, cantidad, tipo_op, categoria_op);
                            }

                        }
                    }
                });
        builder.show();


        //Creamos el adaptador, ARRAY EN XML CANJE O DEVOLUCIÓN
        ArrayAdapter<CharSequence> adapterTipoOperacion = ArrayAdapter.createFromResource(this, R.array.tipo_devolucion, android.R.layout.simple_spinner_item);
        //Añadimos el layout para el TIPO DE OPERACIÓN //CANJE  O DEVOLUCIÓN
        adapterTipoOperacion.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Le indicamos al spinner el adaptador a usar
        spinnerTipoOp.setAdapter(adapterTipoOperacion);

        //Creamos el adaptador, ARRAY EN XML BUENO, MALO, VENCIMIENTO MALO, ETC
        ArrayAdapter<CharSequence> adapterCategoria = ArrayAdapter.createFromResource(this, R.array.cate_devolucion, android.R.layout.simple_spinner_item);
        //Añadimos el layout para el TIPO DE OPERACIÓN //CANJE  O DEVOLUCIÓN
        adapterCategoria.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Le indicamos al spinner el adaptador a usar
        spinnerCategoria.setAdapter(adapterCategoria);

    }

    private void mostrar_alertdialog_spinners(String producto) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("" + nomProducto + "");
        final View layout_spinners = View.inflate(this, R.layout.prompts, null);
        final EditText vUnidad = (EditText) layout_spinners.findViewById(R.id.EditUnidadValor);
        final EditText nroCompro = (EditText) layout_spinners.findViewById(R.id.comprob_clave_dev_can);
        final EditText nroLote = (EditText) layout_spinners.findViewById(R.id.lote_can_dev);
        final EditText cantidadText = (EditText) layout_spinners.findViewById(R.id.cantidad_can_dev);
        final Spinner spinnerTipoOp = (Spinner) layout_spinners.findViewById(R.id.can_dev_tipo_op);
        final Spinner spinnerCategoria = (Spinner) layout_spinners.findViewById(R.id.can_dev_categoria);
        vUnidad.setText(valorUnidad);


        nroCompro.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (nroCompro.getText().toString().trim() != "") {
                    nroCompro.setError(null);
                    nroCompro.setTextColor(getApplicationContext().getResources().getColor(R.color.Dark1));
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {


            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (nroCompro.getText().toString().trim().equals("")) {
                    nroCompro.setError("Es Requerido");
                } else {
                    nroCompro.setError(null);
                    nroCompro.setTextColor(getApplicationContext().getResources().getColor(R.color.Dark1));
                }
            }
        });

        nroLote.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (nroLote.getText().toString().trim() != "") {
                    nroLote.setError(null);
                    nroLote.setTextColor(getApplicationContext().getResources().getColor(R.color.Dark1));
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (nroLote.getText().toString().trim().equals("")) {
                    nroLote.setError("Es Requerido");
                } else {
                    nroLote.setError(null);
                    nroLote.setTextColor(getApplicationContext().getResources().getColor(R.color.Dark1));
                }
            }
        });

        cantidadText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (cantidadText.getText().toString().trim() != "") {
                    cantidadText.setError(null);
                    cantidadText.setTextColor(getApplicationContext().getResources().getColor(R.color.Dark1));
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (cantidadText.getText().toString().trim().equals("")) {
                    cantidadText.setError("Es Requerido");
                } else {
                    cantidadText.setError(null);
                    cantidadText.setTextColor(getApplicationContext().getResources().getColor(R.color.Dark1));
                }
            }
        });

        builder.setView(layout_spinners);
        builder.setCancelable(true);
        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String compro = nroCompro.getText().toString();
                        String lote = nroLote.getText().toString();
                        String cantidad = cantidadText.getText().toString();
                        String tipo_op = spinnerTipoOp.getSelectedItem().toString();
                        String categoria_op = spinnerCategoria.getSelectedItem().toString();
                        if (compro.trim().equals("") || lote.trim().equals("") || cantidad.trim().equals("")) {
                            if (compro.trim().equals("")) {
                                nroCompro.setError("Es Requerido");
                            }
                            if (lote.trim().equals("")) {
                                nroLote.setError("Es Requerido");
                            }
                            if (cantidad.trim().equals("")) {
                                cantidadText.setError("Es Requerido");
                            }
                            Toast.makeText(getApplicationContext(), "Por favor Ingrese Todos los Campos", Toast.LENGTH_SHORT).show();

                        } else {
                            int position = spinnerTipoOp.getSelectedItemPosition();
                            Log.e("posicion",": "+position);
                            if(position==0){
                                if(stock>0){
                                    registrarNuevo_Comprobante(compro, lote, cantidad, tipo_op, categoria_op);
                                }else{
                                    Toast.makeText(getApplicationContext(),"Usted no tiene Stock para realizar el Canje",Toast.LENGTH_LONG).show();
                                }

                            }if(position==1){

                                registrarNuevo_Comprobante(compro, lote, cantidad, tipo_op, categoria_op);
                            }

                        }
                    }
                });
        builder.show();



        //Creamos el adaptador, ARRAY EN XML CANJE O DEVOLUCIÓN
        ArrayAdapter<CharSequence> adapterTipoOperacion = ArrayAdapter.createFromResource(this, R.array.tipo_devolucion, android.R.layout.simple_spinner_item);
        //Añadimos el layout para el TIPO DE OPERACIÓN //CANJE  O DEVOLUCIÓN
        adapterTipoOperacion.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Le indicamos al spinner el adaptador a usar
        spinnerTipoOp.setAdapter(adapterTipoOperacion);

        //Creamos el adaptador, ARRAY EN XML BUENO, MALO, VENCIMIENTO MALO, ETC
        ArrayAdapter<CharSequence> adapterCategoria = ArrayAdapter.createFromResource(this, R.array.cate_devolucion, android.R.layout.simple_spinner_item);
        //Añadimos el layout para el TIPO DE OPERACIÓN //CANJE  O DEVOLUCIÓN
        adapterCategoria.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Le indicamos al spinner el adaptador a usar
        spinnerCategoria.setAdapter(adapterCategoria);


    }

    private void registrarNuevo_Comprobante(String compro, String lote, String cantidad, String tipo_op, String catego_op) {
        int liquidacion = dbAdapter_temp_session.fetchVarible(3);
        int cantidad2 = Integer.parseInt(cantidad);
        int idTipo_Op = 0;
        int idCat_tipo = 0;
        //Evaluando Categoria
        if (catego_op.equals("Bueno")) {
            idCat_tipo = 1;
        }
        if (catego_op.equals("Malogrado")) {
            idCat_tipo = 2;
        }
        if (catego_op.equals("Reclamo")) {
            idCat_tipo = 3;
        }
        if (catego_op.equals("Vencido-Malo")) {
            idCat_tipo = 4;
        }
        //Obteniendo el nombre del establecimiento, categoria.
        Log.d("Parametros", "" + idProducto + "-" + idCategoriaEstablec + "-" + idEstablec);
        String precio = dbHelperCanjes_Dev.obtenerPrecio(idProducto, idCategoriaEstablec, ctx, idEstablec);
        double importe = Double.parseDouble(precio);

        //Evaluando tipo de Operacion:
        if (tipo_op.equals("Canje")) {
            idTipo_Op = 1;
           // Log.d("MasParametros", "[" + idEstablec + "-" + idProducto + "-" + idTipo_Op + "-" + compro + "-" + nomEstablecimiento + "-" + idCat_tipo + "-" + cantidad2 + "-" + importe * cantidad2 + "-" + lote + "-" + idAgente + importe);
            boolean estado = dbHelperCanjes_Dev.insertarCanjes(idEstablec, idProducto, idTipo_Op, compro, nomEstablecimiento, nomProducto, idCat_tipo, cantidad2, importe * cantidad2, lote, idAgente, liquidacion, valorUnidad);
            if (estado) {
                confirmar();
            } else {
                Toast.makeText(this, "No Inserto", Toast.LENGTH_SHORT).show();
            }
        }
        if (tipo_op.equals("Devolucion")) {
            idTipo_Op = 2;
            boolean estado = dbHelperCanjes_Dev.insertar_Dev(idEstablec, idProducto, idTipo_Op, compro, nomEstablecimiento, nomProducto, idCat_tipo, cantidad2, importe * cantidad2, lote, idAgente, liquidacion, valorUnidad);
            if (estado) {
                confirmar();
            } else {
                Toast.makeText(this, "No Inserto", Toast.LENGTH_SHORT).show();

            }
        }


    }

    public void confirmar() {
        //Instancia para poder volver

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ctx);
        alertDialogBuilder.setTitle("Guardado Correctamente");
        AlertDialog.Builder builder = alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Regresar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent back = new Intent(getApplicationContext(), VMovil_Evento_Canjes_Dev.class);
                        back.putExtra("idEstabX", idEstablec);
                        back.putExtra("idAgente", idAgente);
                        startActivity(back);
                        finish();

                    }

                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

}
