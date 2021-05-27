package mx.ssp.iph.principal.ui.activitys;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import mx.ssp.iph.R;
import mx.ssp.iph.principal.ui.fragments.PrincipalAdministrativo;
import mx.ssp.iph.principal.ui.fragments.PrincipalBuscar;
import mx.ssp.iph.principal.ui.fragments.PrincipalDelictivo;
import mx.ssp.iph.principal.ui.fragments.PrincipalEmergencias;

public class Principal extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private DrawerLayout drawer;

    //Menu inferior
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener;
    {
        mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.navigation_delictivo:
                        addFragment(new PrincipalDelictivo());
                        return true;
                    case R.id.navigation_administrativo:
                        addFragment(new PrincipalAdministrativo());
                        return true;
                    case R.id.navigation_emergencias:
                        addFragment(new PrincipalEmergencias());
                        return true;
                    case R.id.navigation_buscar:
                        addFragment(new PrincipalBuscar());
                        return true;
                }
                return false;
            }
        };
    }

    //Intercambia los Fragmentos
    private void addFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.nav_host_fragment, fragment)
                //.addToBackStack(null) //Se quita la pila de fragments. Botón atrás
                .commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        //Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("titulo toolbar");


        //Bottom Navigation
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        addFragment(new PrincipalDelictivo());

        // Menu lateral
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_inicio, R.id.nav_quees, R.id.nav_como_funciona, R.id.nav_protocolo)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}