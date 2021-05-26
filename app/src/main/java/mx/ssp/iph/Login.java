package mx.ssp.iph;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import mx.ssp.iph.administrativo.model.ModelLugarIntervencion_Administrativo;
import mx.ssp.iph.administrativo.model.ModeloUsuarios_Administrativo;
import mx.ssp.iph.administrativo.ui.activitys.Iph_Administrativo;
import mx.ssp.iph.administrativo.ui.fragmets.LugarDeIntervencion;
import mx.ssp.iph.principal.ui.activitys.Principal;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Login extends AppCompatActivity {
    Button btnLogin;
    EditText txtUsuario,txtContrasena;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        txtUsuario = findViewById(R.id.txtUsuario);
        txtContrasena = findViewById(R.id.txtContrasena);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtUsuario.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"EL USUARIO ES NECESARIO PARA INGRESAR",Toast.LENGTH_SHORT).show();
                }else if(txtContrasena.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"LA CONTRASEÑA ES NECESARIA PARA INGRESAR",Toast.LENGTH_SHORT).show();
                }else if(txtUsuario.getText().length() < 3){
                    Toast.makeText(getApplicationContext(),"EL USUARIO NO PUEDE SER MENOR A TRES LETRAS",Toast.LENGTH_SHORT).show();
                }else if(txtContrasena.getText().length() < 3){
                    Toast.makeText(getApplicationContext(),"LA CONTRASEÑA NO PUEDE SER MENOR A TRES LETRAS",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getApplicationContext(), "ESTAMOS PROCESANDO SU SOLICITUD, UN MOMENTO POR FAVOR", Toast.LENGTH_SHORT).show();
                    getUsuaio();
                }

            }
        });
    }

    private void getUsuaio() {
        ModeloUsuarios_Administrativo modeloUsuarios = new ModeloUsuarios_Administrativo(
                txtUsuario.getText().toString(),
                txtContrasena.getText().toString());

        final OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url("http://189.254.7.167/WebServiceIPH/api/Usuarios?usuario="+modeloUsuarios.getUsuario()+"&passsword="+modeloUsuarios.getPassword())
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Looper.prepare();
                Toast.makeText(getApplicationContext(),"ERROR AL OBTENER LA INFORMACIÓN, POR FAVOR VERIFIQUE SU CONEXIÓN A INTERNET",Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String myResponse = response.body().string();
                    Login.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String resp = myResponse;
                            if(resp.equals("true")){
                                System.out.println("EL DATO SE ENVIO CORRECTAMENTE");
                                Toast.makeText(getApplicationContext(), "EL DATO SE ENVIO CORRECTAMENTE", Toast.LENGTH_SHORT).show();
                                txtUsuario.setText("");
                                txtContrasena.setText("");
                                Intent intent = new Intent(Login.this,Principal.class);
                                startActivity(intent);

                            }else{
                                Toast.makeText(getApplicationContext(), "LO SENTIMOS, USUARIO O CONTRASEÑA INCORRECTOS", Toast.LENGTH_SHORT).show();
                            }
                            Log.i("HERE", resp);
                        }
                    });
                }
            }

        });
    }
}
