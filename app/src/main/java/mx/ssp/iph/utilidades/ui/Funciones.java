package mx.ssp.iph.utilidades.ui;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

import java.io.IOException;
import java.util.Calendar;

import mx.ssp.iph.R;
import mx.ssp.iph.delictivo.ui.fragmets.LugarDeIntervencion_Delictivo;

public class Funciones {

    ProgressDialog progressDialog;

    //***************** Calendario Picker **************************//
    public void calendar(Integer idCajadeTextoCalendario, Context context, Activity activity){
        Calendar c;
        DatePickerDialog dpd;

        c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);

        dpd = new DatePickerDialog(context,R.style.DatePickerDialog, new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                EditText CajadeTextoCalendario;
                CajadeTextoCalendario = (EditText) activity.findViewById(idCajadeTextoCalendario);
                CajadeTextoCalendario.setText(year+"/"+(month+1)+"/"+dayOfMonth);
            }
        },year,month,day);
        dpd.show();
    }

    //***************** TimePicker **************************//
    public void Time(Integer idCajadeTextoTime, Context context, Activity activity){
        TimePickerDialog tpd;

        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        tpd = new TimePickerDialog(context, R.style.TimePickerTheme, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                EditText CajadeTextoTime;
                CajadeTextoTime = (EditText) activity.findViewById(idCajadeTextoTime);
                CajadeTextoTime.setText(selectedHour+":"+selectedMinute);
            }
        }, hour, minute, true);//Yes 24 hour time
        tpd.show();
    }

     //***************** Ping **************************//
    public boolean ping(Context context){
            return true;
    }

    //Método que abre un alert dialog perzonalizado
    //***************** INSERTA A LA BD MEDIANTE EL WS **************************//

    public void mensajeAlertDialog(String Mensaje, String TextoBoton, String TipoMensaje, Context context){
        ImageView image = new ImageView(context);

        if (TipoMensaje == "Error"){
            image.setImageResource(R.drawable.ic_information);
        }
        if (TipoMensaje == "Alerta"){
            image.setImageResource(R.drawable.ic_information);
        }
        if (TipoMensaje == "Correcto"){
            image.setImageResource(R.drawable.ic_information);
        }
        if (TipoMensaje == "Informacion"){
            image.setImageResource(R.drawable.ic_information);
        }
        if (TipoMensaje == "Conexion"){
            image.setImageResource(R.drawable.ic_information);
        }
        if (TipoMensaje == "Procesando"){
            image.setImageResource(R.drawable.ic_information);
        }

        AlertDialog.Builder builder =
                new AlertDialog.Builder(context).
                        setMessage(Mensaje).
                        setPositiveButton( TextoBoton, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
        builder.create().show();
    }

    public void Procesando(Activity activity, String Titulo,String Mensaje){
        progressDialog = ProgressDialog.show(activity, Titulo,
                Mensaje, true);
    }

    public void ProcesandoDissmis(Activity activity, String Titulo,String Mensaje){
        progressDialog.dismiss();
    }

    //***************** Cambia el título de acuerdo a la seccion seleccionada **************************//
    public void CambiarTituloSecciones(String titulo,Context context, Activity activity){
                TextView tituloSecciones;
                tituloSecciones = (TextView) activity.findViewById(R.id.lblTituloSecciones);
                tituloSecciones.setText(titulo);
    }

    //***************** Cambia el título de acuerdo a la seccion seleccionada **************************//
    public void CambiarTituloSeccionesDelictivo(String titulo,Context context, Activity activity){
        TextView tituloSecciones;
        tituloSecciones = (TextView) activity.findViewById(R.id.lblTituloSeccionesDelictivo);
        tituloSecciones.setText(titulo);
    }

    //***************** OBTIENE LA POSICIÓN DEL SPINER **************************//
    public int getIndexSpiner(Spinner spinner, String myString){
        Log.i("SPINER", "iNICIA");
        for (int i=0;i<spinner.getCount();i++){
            //Toast.makeText(getContext(),""+spinner.getItemAtPosition(i).toString() + " - " + spinner.getItemIdAtPosition(i),Toast.LENGTH_SHORT).show();
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }
        return 0;
    }

    //***************** AJUSTA EL TAMAÑO DEL LIST VIEW **************************//
    public void  ajustaAlturaListView(ListView listView, int factorAlto){
        int ancho = 1200;
        int alto = 120;
        Log.i("LISTVIEW", "TOTAL DE ITEMS" + listView.getAdapter().getCount());

        alto = (listView.getAdapter().getCount())*factorAlto;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ancho, alto);
        listView.setLayoutParams(params);
    }

}
