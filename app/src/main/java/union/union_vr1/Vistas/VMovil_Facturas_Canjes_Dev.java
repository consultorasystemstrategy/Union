package union.union_vr1.Vistas;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import union.union_vr1.Objects.HistorialVentaDetalles;
import union.union_vr1.R;
import union.union_vr1.Sqlite.CursorAdapter_Facturas_Canjes_Dev;
import union.union_vr1.Sqlite.DBAdapter_Temp_Canjes_Devoluciones;
import union.union_vr1.Sqlite.DbAdapter_Canjes_Devoluciones;
import union.union_vr1.Sqlite.DbAdapter_Histo_Venta_Detalle;
import union.union_vr1.Sqlite.DbAdapter_Stock_Agente;
import union.union_vr1.Sqlite.DbAdapter_Temp_Session;

import static union.union_vr1.R.layout.*;

public class VMovil_Facturas_Canjes_Dev extends Activity {
    private DBAdapter_Temp_Canjes_Devoluciones dbAdapter_tem_canjes_devoluciones;
    private View layoutNoEncontrado;
    private DbAdapter_Histo_Venta_Detalle dbAdapter_histo_venta_detalle;
    private int idProducto;
    private DbAdapter_Canjes_Devoluciones dbHelperCanjes_Dev;
    private DbAdapter_Stock_Agente dbHelperStockAgente;
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
    private DbAdapter_Temp_Session dbAdapter_temp_session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(princ_facturas_canjes_dev);
        dbAdapter_temp_session = new DbAdapter_Temp_Session(ctx);
        dbAdapter_temp_session.open();
        dbAdapter_tem_canjes_devoluciones = new DBAdapter_Temp_Canjes_Devoluciones(this);
        dbAdapter_tem_canjes_devoluciones.open();
        //--------------------------------------------
        dbAdapter_histo_venta_detalle = new DbAdapter_Histo_Venta_Detalle(this);
        dbAdapter_histo_venta_detalle.open();

        dbHelperCanjes_Dev = new DbAdapter_Canjes_Devoluciones(this);
        dbHelperCanjes_Dev.open();
        dbHelperStockAgente = new DbAdapter_Stock_Agente(this);
        dbHelperStockAgente.open();

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
        imprimeStock(idProducto);
        listarFacturas_Productos(idProducto, idAgente, idEstablec, nomProducto);
    }

    private void imprimeStock(int producto) {
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


        layoutNoEncontrado = View.inflate(this, R.layout.info_producto_no_encontrado, null);
        if (cr.moveToFirst()) {
            if (cr.getString(10) == null || cr.getString(10).equals("")) {


                ArrayList<String> noEncontrado = new ArrayList<String>();
                noEncontrado.add("");


                ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, noEncontrado);
                View noE = View.inflate(this, R.layout.info_producto_no_encontrado, null);
                final Button btnRegresar = (Button) noE.findViewById(R.id.btn_regresar);
                btnRegresar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent back = new Intent(getApplicationContext(), VMovil_Operacion_Canjes_Devoluciones.class);
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

                        mostrar_alertdialog_spinners(stock);
                    }
                });
                listaFacturas.addHeaderView(noE);
                listaFacturas.setAdapter(adapter);

            } else {
                LinearLayout linearLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.agregar_otro, null);
                TextView vieFooter = (TextView) linearLayout.findViewById(R.id.textViewNoEncontrado);
                //vieFooter.setText("Agregar Otro");
                listaFacturas.addFooterView(linearLayout);
                vieFooter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (stock == 0) {
                            Toast.makeText(getApplicationContext(), "Tiene Stock 0", Toast.LENGTH_SHORT).show();
                            //mostrar_alertdialog_spinners_dev(nomProducto);
                        } else {
                            mostrar_alertdialog_spinners(stock);
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
                            if (cursor.getString(cursor.getColumnIndexOrThrow("hd_in_cantidad_ope")) != null) {
                                canjes = cursor.getInt(cursor.getColumnIndexOrThrow("hd_in_cantidad_ope"));
                            }

                            int devoluciones = 0;

                            if (cursor.getString(cursor.getColumnIndexOrThrow("hd_in_cantidad_ope_dev")) != null) {
                                devoluciones = cursor.getInt(cursor.getColumnIndexOrThrow("hd_in_cantidad_ope_dev"));
                            }
                            int sumacandev = canjes + devoluciones;
                            int total = cursor.getInt(cursor.getColumnIndexOrThrow("hd_in_cantidad"));
                            if (total - sumacandev == 0) {
                                Toast.makeText(getApplicationContext(), "Ya no puede realizar mas operaciones", Toast.LENGTH_SHORT).show();
                            } else {
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
            View noE = View.inflate(this, R.layout.info_producto_no_encontrado, null);
            final Button btnRegresar = (Button) noE.findViewById(R.id.btn_regresar);
            btnRegresar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent back = new Intent(getApplicationContext(), VMovil_Operacion_Canjes_Devoluciones.class);
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

                    mostrar_alertdialog_spinners(stock);
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
        final String comprobante = cursor.getString(7);
        final int idProducto = cursor.getInt(cursor.getColumnIndexOrThrow(DbAdapter_Histo_Venta_Detalle.HD_id_producto));
        final String nomProducto = cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Histo_Venta_Detalle.HD_nom_producto));
        final String lote = cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Histo_Venta_Detalle.HD_lote));
        final int liqui = cursor.getInt(cursor.getColumnIndexOrThrow(DbAdapter_Histo_Venta_Detalle.HD_id_liquidacion));
        //-----------------------------------------
        String[] datos = comprobante.split("/");
        //-------
        final String importe = datos[3];
        //-----------------------------------------
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Devolucion \n" + nomProducto + "");

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
                            if (posicion == 0) {

                                if (stock > 0) {
                                    actualizar_can_dev(idProducto, nomProducto, lote, liqui, comprobante, tipo_op, categoria_op, cantidad, importe, idDetalle, dev, can);
                                } else {
                                    Toast.makeText(getApplicationContext(), "No Tiene Stock Suficiente", Toast.LENGTH_SHORT).show();
                                }


                            }
                            if (posicion == 1) {
                                actualizar_can_dev(idProducto, nomProducto, lote, liqui, comprobante, tipo_op, categoria_op, cantidad, importe, idDetalle, dev, can);

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
        final String comprobante = cursor.getString(7);
        final int idProducto = cursor.getInt(cursor.getColumnIndexOrThrow(DbAdapter_Histo_Venta_Detalle.HD_id_producto));
        final String nomProducto = cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Histo_Venta_Detalle.HD_nom_producto));
        final String lote = cursor.getString(cursor.getColumnIndexOrThrow(DbAdapter_Histo_Venta_Detalle.HD_lote));
        final int liqui = cursor.getInt(cursor.getColumnIndexOrThrow(DbAdapter_Histo_Venta_Detalle.HD_id_liquidacion));
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
                            if (posicion == 0) {
                                if (stock > 0) {
                                    //
                                    actualizar_can_dev(idProducto, nomProducto, lote, liqui, comprobante, tipo_op, categoria_op, cantidad, importe, idDetalle, dev, can);
                                } else {
                                    Toast.makeText(getApplicationContext(), "No tiene Stock Sufiente", Toast.LENGTH_LONG).show();
                                }

                            }
                            if (posicion == 1) {
                                actualizar_can_dev(idProducto, nomProducto, lote, liqui, comprobante, tipo_op, categoria_op, cantidad, importe, idDetalle, dev, can);
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
    private void actualizar_can_dev(int idProducto, String nomProducto, String lote, int liqui, String comprobante, String tipo_op, String categoria_op, String cantidad, String importe, String idDetalle, int devuelto, int canjeado) {
        double importeTotal = Double.parseDouble(importe) * Integer.parseInt(cantidad);
        int liquidacion = dbAdapter_temp_session.fetchVarible(3);
        liqui = liquidacion;
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
            tipo_op = "2";
            long estado = dbAdapter_tem_canjes_devoluciones.createTempCanjesDevoluciones(comprobante + "", idProducto + "", nomProducto, "", cantidad + "", importe + "", importeTotal + "", lote + "", getDatePhone(), "", idEstablec, "", tipo_op, categoria_op + "", 4 + "", liqui + "", idDetalle);
            int estado2 = dbAdapter_histo_venta_detalle.updateHistoVentaDetalleCanje(idDetalle, Integer.parseInt(cantidad));
            if (estado > 0 && estado2 > 0) {
                dbHelperStockAgente.stockCanjes(Integer.parseInt(cantidad), idProducto + "", liqui + "");
                // dbHelperStockAgente.updateStockAgenteDisponibleCantidad(idProducto, -Integer.parseInt(cantidad), liquidacion);
                confirmar();

            } else {
                Toast.makeText(ctx, "Por favor Intente Nuevamente", Toast.LENGTH_SHORT).show();

            }
        }
        if (tipo_op.equals("Devolucion")) {
            tipo_op = "1";
            long estado = dbAdapter_tem_canjes_devoluciones.createTempCanjesDevoluciones(comprobante + "", idProducto + "", nomProducto, "", cantidad + "", importe + "", importeTotal + "", lote + "", getDatePhone(), "", idEstablec, "", tipo_op, categoria_op + "", 4 + "", liqui + "", idDetalle);
            int estado2 = dbAdapter_histo_venta_detalle.updateHistoVentaDetalleDevolucion(idDetalle, Integer.parseInt(cantidad));
            if (estado > 0 && estado2 > 0) {
                dbHelperStockAgente.stockDevoluciones(Integer.parseInt(cantidad), idProducto + "", liqui + "");
                confirmar();

            } else {
                Toast.makeText(ctx, "Por favor Intente Nuevamente", Toast.LENGTH_SHORT).show();

            }
        }
    }

    //Cuando no se encuentran los registros
   /* private void mostrar_alertdialog_spinners_dev(String producto) {



        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Canjes/Devoluciones \n" + nomProducto + "");
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

    }*/

    private void mostrar_alertdialog_spinners(int stockOperacion) {
        ArrayAdapter<CharSequence> adapterTipoOperacion = ArrayAdapter.createFromResource(this, R.array.tipo_devolucion, android.R.layout.simple_spinner_item);
        if (stockOperacion == 0) {
            adapterTipoOperacion = ArrayAdapter.createFromResource(this, R.array.tipo_devolucion_only, android.R.layout.simple_spinner_item);
        }else{
            adapterTipoOperacion = ArrayAdapter.createFromResource(this, R.array.tipo_devolucion, android.R.layout.simple_spinner_item);
        }
        int liquidacion = dbAdapter_temp_session.fetchVarible(3);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Kelvin" + nomProducto + "");
        final View layout_spinners = View.inflate(this, R.layout.prompts, null);
        final EditText vUnidad = (EditText) layout_spinners.findViewById(R.id.EditUnidadValor);
        final EditText nroCompro = (EditText) layout_spinners.findViewById(R.id.comprob_clave_dev_can);
        final EditText nroLote = (EditText) layout_spinners.findViewById(R.id.lote_can_dev);
        final EditText cantidadText = (EditText) layout_spinners.findViewById(R.id.cantidad_can_dev);
        final Spinner spinnerTipoOp = (Spinner) layout_spinners.findViewById(R.id.can_dev_tipo_op);
        final Spinner spinnerCategoria = (Spinner) layout_spinners.findViewById(R.id.can_dev_categoria);
        final Cursor cursor = dbHelperCanjes_Dev.getUnidadMedida(idProducto + "", liquidacion);
        final String[] precioValor = new String[1];
        if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            valorUnidad = cursor.getString(cursor.getColumnIndexOrThrow("valorUnidad"));
            vUnidad.setText(cursor.getString(cursor.getColumnIndexOrThrow("valorUnidad")));
            precioValor[0] = cursor.getString(cursor.getColumnIndexOrThrow("pr_re_precio_unit"));
        } else {
            cursor.moveToFirst();
            vUnidad.setText(cursor.getString(cursor.getColumnIndexOrThrow("valorUnidad")));
            vUnidad.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (cursor.isLast()) {
                        cursor.moveToFirst();
                    } else {
                        cursor.moveToNext();
                    }
                    precioValor[0] = cursor.getString(cursor.getColumnIndexOrThrow("pr_re_precio_unit"));
                    valorUnidad = cursor.getString(cursor.getColumnIndexOrThrow("valorUnidad"));
                    vUnidad.setText(cursor.getInt(cursor.getColumnIndexOrThrow("valorUnidad")) + "");

                }
            });
        }


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
                if (charSequence.length() != 0) {
                    String nro = charSequence.toString();
                    int charSec = Integer.parseInt(nro);

                    if (charSec == 0) {
                        cantidadText.setText("");
                        Toast.makeText(getApplicationContext(), "La Cantidad no puede ser 0", Toast.LENGTH_SHORT).show();
                    }
                    if (charSec > stock) {
                        if (spinnerTipoOp.getSelectedItem().toString().equals("Devolucion")) {

                        } else {
                            cantidadText.setText("");
                            Toast.makeText(getApplicationContext(), "No puede Pasar el Stock", Toast.LENGTH_SHORT).show();
                        }
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
                        String compro = nroCompro.getText().toString();
                        String lote = nroLote.getText().toString();
                        String cantidad = cantidadText.getText().toString();
                        String tipo_op = spinnerTipoOp.getSelectedItem().toString();
                        String categoria_op = spinnerCategoria.getSelectedItem().toString();
                        if (compro.trim().equals("") || lote.trim().equals("") || cantidad.trim().equals("") || cantidad == "0") {
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
                            Log.e("posicion", ": " + position);
                            if (position == 0) {
                                if (stock > 0) {

                                    registrarNuevo_Comprobante(compro, lote, cantidad + "", tipo_op, categoria_op, precioValor[0]);
                                } else {
                                    if(stock ==0 && spinnerTipoOp.getSelectedItem().toString().equals("Devolucion")){
                                        registrarNuevo_Comprobante(compro, lote, cantidad + "", tipo_op, categoria_op, precioValor[0]);
                                    }else{
                                        Toast.makeText(getApplicationContext(), "Ocurrio un error", Toast.LENGTH_LONG).show();
                                    }

                                }

                            }
                            if (position == 1) {

                                registrarNuevo_Comprobante(compro, lote, cantidad + "", tipo_op, categoria_op, precioValor[0]);
                            }

                        }
                    }
                });
        builder.show();


        //Creamos el adaptador, ARRAY EN XML CANJE O DEVOLUCIÓN

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

    private void registrarNuevo_Comprobante(String compro, String lote, String cantidad, String tipo_op, String catego_op, String precioUnitario) {
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
        double importe = 0.0;
        if (precioUnitario == null || precioUnitario == "") {
            String precio = dbHelperCanjes_Dev.obtenerPrecio(idProducto, idCategoriaEstablec, ctx, idEstablec);
            importe = Double.parseDouble(precio);
        } else {
            importe = Double.parseDouble(precioUnitario);
        }
        //String precio = dbHelperCanjes_Dev.obtenerPrecio(idProducto, idCategoriaEstablec, ctx, idEstablec);
        //  double importe = Double.parseDouble(precioUnitario.toString());
        double importeTotal = importe * cantidad2;
        DecimalFormat df = new DecimalFormat("0.00");
        //Evaluando tipo de Operacion:
        Log.d("LIQUIDACION", "" + liquidacion);
        if (tipo_op.equals("Canje")) {
            //2
            idTipo_Op = 2;
            /*
            DEVOLUCIONES SI NO EXISTE
            AL MOMENTO DE CREAR, EL ESTADO ESTA COMO 3 SI ES CREADO.
            */
            //PARAMETROS PARA CANJES
            //ESTADO 3  INDICA QUE LA DEVOLUCION ES MANUAL
            // ES DECIR QUE NO EXISTE UN COMPROBANTE CON ANTERIORIDAD.


            long estadoInserto = dbAdapter_tem_canjes_devoluciones.createTempCanjesDevoluciones(compro, idProducto + "", nomProducto, "", cantidad2 + "", importe + "", df.format(importeTotal) + "", lote, getDatePhone(), "", idEstablec, "", idTipo_Op + "", idCat_tipo + "", 3 + "", liquidacion + "", "-1");
            //Log.d("MasParametros", "[" + idEstablec + "-" + idProducto + "-" + idTipo_Op + "-" + compro + "-" + nomEstablecimiento + "-" + idCat_tipo + "-" + cantidad2 + "-" + importe * cantidad2 + "-" + lote + "-" + idAgente + importe);
            if (estadoInserto > 0) {

                int estado = dbHelperStockAgente.stockCanjes(cantidad2, idProducto + "", liquidacion + "");
                if (estado > 0) {
                    confirmar();
                }

            } else {
                Toast.makeText(this, "No Inserto", Toast.LENGTH_SHORT).show();
            }
        }
        if (tipo_op.equals("Devolucion")) {

            idTipo_Op = 1;
            long estadoInserto = dbAdapter_tem_canjes_devoluciones.createTempCanjesDevoluciones(compro, idProducto + "", nomProducto, "", cantidad2 + "", importe + "", df.format(importeTotal) + "", lote, getDatePhone(), "", idEstablec, "", idTipo_Op + "", idCat_tipo + "", 3 + "", liquidacion + "", "-1");

            //boolean estado = dbHelperCanjes_Dev.insertar_Dev(idEstablec, idProducto, idTipo_Op, compro, nomEstablecimiento, nomProducto, idCat_tipo, cantidad2, importe * cantidad2, lote, idAgente, liquidacion, valorUnidad);
            if (estadoInserto > 0) {
                int estado = dbHelperStockAgente.stockDevoluciones(cantidad2, idProducto + "", liquidacion + "");
                if (estado > 0) {
                    confirmar();
                }

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
                        Intent back = new Intent(getApplicationContext(), VMovil_Operacion_Canjes_Devoluciones.class);
                        back.putExtra("idEstabX", idEstablec);
                        back.putExtra("idAgente", idAgente);
                        startActivity(back);
                        finish();

                    }

                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    private String getTimePhone() {
        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("hh:mm:ss");
        String formatteTime = df.format(date);
        return formatteTime;
    }

    private String getDatePhone() {
        Calendar cal = new GregorianCalendar();
        Date date = cal.getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formatteDate = df.format(date);
        return formatteDate;
    }

}
