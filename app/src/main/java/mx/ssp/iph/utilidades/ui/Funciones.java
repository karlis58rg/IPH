package mx.ssp.iph.utilidades.ui;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import java.io.IOException;
import java.util.Calendar;

public class Funciones {

    //Calendario Picker
    public void calendar(Integer idCajadeTextoCalendario, Context context, Activity activity){
        Calendar c;
        DatePickerDialog dpd;

        c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);

        dpd = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                EditText CajadeTextoCalendario;
                CajadeTextoCalendario = (EditText) activity.findViewById(idCajadeTextoCalendario);
                CajadeTextoCalendario.setText(year+"/"+(month+1)+"/"+dayOfMonth);
            }
        },year,month,day);
        dpd.show();
    }

    //TimePicker
    public void Time(Integer idCajadeTextoTime, Context context, Activity activity){
        TimePickerDialog tpd;

        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        tpd = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                EditText CajadeTextoTime;
                CajadeTextoTime = (EditText) activity.findViewById(idCajadeTextoTime);
                CajadeTextoTime.setText(selectedHour+":"+selectedMinute);
            }
        }, hour, minute, true);//Yes 24 hour time
        tpd.show();
    }

    //Ping
    public boolean ping(Context context){
        Runtime runtime = Runtime.getRuntime();
        try
        {
            Process  mIpAddrProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int mExitValue = mIpAddrProcess.waitFor();
            if(mExitValue==0){
                return true;
            }else{
                Toast.makeText(context, "POR FAVOR VERIFIQUE SU CONEXIÓN A INTERNET", Toast.LENGTH_LONG).show();
                return false;
            }
        }
        catch (InterruptedException ignore)
        {
            ignore.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return false;
    }

}
