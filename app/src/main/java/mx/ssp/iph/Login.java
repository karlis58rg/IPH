package mx.ssp.iph;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import mx.ssp.iph.administrativo.ui.activitys.Iph_Administrativo;
import mx.ssp.iph.principal.ui.activitys.Principal;

public class Login extends AppCompatActivity {

    Button btnLogin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this,Principal.class);
                startActivity(intent);

            }
        });
    }
}
