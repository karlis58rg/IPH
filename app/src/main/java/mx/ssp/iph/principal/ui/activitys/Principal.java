package mx.ssp.iph.principal.ui.activitys;

import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Toast;

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

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import mx.ssp.iph.R;
import mx.ssp.iph.SqLite.DataHelper;
import mx.ssp.iph.administrativo.ui.fragmets.NoReferencia_Administrativo;
import mx.ssp.iph.principal.ui.fragments.MenuPrincipal;
import mx.ssp.iph.principal.ui.fragments.PrincipalAdministrativo;
import mx.ssp.iph.principal.ui.fragments.PrincipalBuscar;
import mx.ssp.iph.principal.ui.fragments.PrincipalDelictivo;
import mx.ssp.iph.principal.ui.fragments.PrincipalEmergencias;
import mx.ssp.iph.utilidades.ui.DialogFragmentSalir;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import mx.ssp.iph.principal.ui.fragments.ViewModelSumarActivity;
import mx.ssp.iph.principal.ui.fragments.ViewModelSumarActivity;

public class Principal extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private DrawerLayout drawer;
    private Fragment PrincipalDelictivo,PrincipalAdministrativo,PrincipalEmergencias,PrincipalBuscar;


    String respuestaJson;
    private int idEntidadFederativa;
    private String idMunicipio;
    private String municipio;
    int idAutoridadAdmin; String autoridadAdmin;
    int idCargo; String cargo;
    int idConocimiento; String conocimiento;
    int idFiscaliaAutoridad; String fiscaliaAutoridad;
    int idInstitucion;String institucion;
    int idLugarTraslado; String lugarTraslado; String descripcion;
    int idNacionalida; String nacionalida; String desNacionalidad;
    int idSexo;String sexo;
    String idUnidad; String unidad; String idMarca,marca,idColor,color,modeloVehiculo;
    int idSubMarca; int modelo; String descripcionU; int idInstitucionU;

/*
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

 */

    //Intercambia los Fragmentos
    private void addFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.nav_host_fragment, fragment)
                //.addToBackStack(null) //Se quita la pila de fragments. Botón atrás
                .commit();

    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            DialogFragmentSalir dialog = new DialogFragmentSalir();
            dialog.show( this.getSupportFragmentManager(),"Salir");
            //super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        /**********************************************************/
        /*        */
        ListAutoridadAdmin();
        ListCargo();
        ListConocimientoInfraccion();
        ListFiscaliaAutoridad();
        ListInstitucion();
        ListLugarTraslado();
        ListMunicipios();
        ListNacionalidad();
        ListSexo();
        ListUnidad();
        ListMarca();
        ListSubMarca();
        ListColores();
        ListModelo();





        /**********************************************************/

        //Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("titulo toolbar");

        PrincipalDelictivo = new PrincipalDelictivo();
        PrincipalAdministrativo= new PrincipalAdministrativo();
        PrincipalEmergencias = new PrincipalEmergencias();
        PrincipalBuscar = new PrincipalBuscar();

/*
        //Bottom Navigation
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setItemIconTintList(null);
 */
        addFragment(new MenuPrincipal());

        // Menu lateral
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_inicio, R.id.nav_quees, R.id.nav_como_funciona, R.id.nav_protocolo,R.id.nav_terminos,R.id.nav_blank)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    /**************** SPINNER **************************************/
    private void ListAutoridadAdmin() {
        DataHelper dataHelper = new DataHelper(getApplicationContext());
        ArrayList<String> list = dataHelper.getAllAutoridadAdmin();
        if (list.size() > 0) {
            System.out.println("YA EXISTE INFORMACIÓN DE AUTORIDAD ADMIN");
        }else{
            getAutoridadAdmin();
        }
    }
    private void ListCargo() {
        DataHelper dataHelper = new DataHelper(getApplicationContext());
        ArrayList<String> list = dataHelper.getAllCargos();
        if (list.size() > 0) {
            System.out.println("YA EXISTE INFORMACIÓN DE CARGOS");
        }else{
            getCargo();
        }
    }
    private void ListConocimientoInfraccion() {
        DataHelper dataHelper = new DataHelper(getApplicationContext());
        ArrayList<String> list = dataHelper.getAllConocimientoInfraccion();
        if (list.size() > 0) {
            System.out.println("YA EXISTE INFORMACIÓN DE CONOCIMIENTO INFRACCION");
        }else{
            getConocimientoInfraccion();

        }
    }
    private void ListFiscaliaAutoridad() {
        DataHelper dataHelper = new DataHelper(getApplicationContext());
        ArrayList<String> list = dataHelper.getAllFiscaliaAutoridad();
        if (list.size() > 0) {
            System.out.println("YA EXISTE INFORMACIÓN DE FISCALIA AUTORIDAD");
        }else{
            getFiscaliaAutoridad();
        }
    }
    private void ListInstitucion() {
        DataHelper dataHelper = new DataHelper(getApplicationContext());
        ArrayList<String> list = dataHelper.getAllInstitucion();
        if (list.size() > 0) {
            System.out.println("YA EXISTE INFORMACIÓN DE INSTITUCIONES");
        }else{
            getInstitucion();
        }
    }

    private void ListLugarTraslado() {
        DataHelper dataHelper = new DataHelper(getApplicationContext());
        ArrayList<String> list = dataHelper.getAllLugarTraslado();
        if (list.size() > 0) {
            System.out.println("YA EXISTE INFORMACIÓN DE LUGAR TRASLADO");
        }else{
            getLugarTraslado();
        }
    }
    private void ListMunicipios() {
        DataHelper dataHelper = new DataHelper(getApplicationContext());
        ArrayList<String> list = dataHelper.getAllMunicipios();
        if (list.size() > 0) {
            System.out.println("YA EXISTE INFORMACIÓN DE MUNICIPIOS");
        }else{
            getMunicipios();
        }
    }
    private void ListNacionalidad() {
        DataHelper dataHelper = new DataHelper(getApplicationContext());
        ArrayList<String> list = dataHelper.getAllNacionalidad();
        if (list.size() > 0) {
            System.out.println("YA EXISTE INFORMACIÓN DE NACIONALIDAD");
        }else{
            getNacionalidad();
        }
    }
    private void ListSexo() {
        DataHelper dataHelper = new DataHelper(getApplicationContext());
        ArrayList<String> list = dataHelper.getAllSexo();
        if (list.size() > 0) {
            System.out.println("YA EXISTE INFORMACIÓN DE SEXO");
        }else{
            getSexo();
        }
    }
    private void ListUnidad() {
        DataHelper dataHelper = new DataHelper(getApplicationContext());
        ArrayList<String> list = dataHelper.getAllUnidad();
        if (list.size() > 0) {
            System.out.println("YA EXISTE INFORMACIÓN DE UNIDAD");
        }else{
            getUnidad();
        }
    }

    private void ListMarca() {
        DataHelper dataHelper = new DataHelper(getApplicationContext());
        ArrayList<String> list = dataHelper.getAllMarcaVehiculos();
        if (list.size() > 0) {
            System.out.println("YA EXISTE INFORMACIÓN DE MARCA VEHICULOS");
        }else{
            getMarcaV();
        }
    }

    private void ListColores() {
        DataHelper dataHelper = new DataHelper(getApplicationContext());
        ArrayList<String> list = dataHelper.getAllColores();
        if (list.size() > 0) {
            System.out.println("YA EXISTE INFORMACIÓN DE COLORES");
        }else{
            getColor();
        }
    }

    private void ListModelo() {
        DataHelper dataHelper = new DataHelper(getApplicationContext());
        ArrayList<String> list = dataHelper.getAllModelos();
        if (list.size() > 0) {
            System.out.println("YA EXISTE INFORMACIÓN DE MODELOS");
        }else{
            getAnio();
        }
    }


    /********************************** CARGA DE TABLAS POR WS ***************************************/
    public void getAutoridadAdmin() {
        DataHelper dataHelper = new DataHelper(getApplicationContext());

        final OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url("http://189.254.7.167/WebServiceIPH/api/CatAutoridadAdmin")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Looper.prepare();
                Toast.makeText(getApplicationContext(), "ERROR AL OBTENER LA INFORMACIÓN, POR FAVOR VERIFIQUE SU CONEXIÓN A INTERNET", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String myResponse = response.body().string();
                    Principal.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                respuestaJson = "null";
                                if (myResponse.equals(respuestaJson)) {
                                    Toast.makeText(getApplicationContext(), "NO SE CUENTA CON INFORMACIÓN", Toast.LENGTH_SHORT).show();
                                } else {
                                    JSONArray ja = null;
                                    try {
                                        ja = new JSONArray("" + myResponse + "");
                                        for (int i = 0; i < ja.length(); i++) {
                                            try {
                                                idAutoridadAdmin = (ja.getJSONObject(i).getInt("IdAutoridadAdmin"));
                                                autoridadAdmin = (ja.getJSONObject(i).getString("AutoridadAdmin"));
                                                dataHelper.insertCatAutoridadAdmin(idAutoridadAdmin,autoridadAdmin);
                                                System.out.println(ja);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }

        });
    }
    public void getCargo() {
        DataHelper dataHelper = new DataHelper(getApplicationContext());

        final OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url("http://189.254.7.167/WebServiceIPH/api/CatCargo")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Looper.prepare();
                Toast.makeText(getApplicationContext(), "ERROR AL OBTENER LA INFORMACIÓN, POR FAVOR VERIFIQUE SU CONEXIÓN A INTERNET", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String myResponse = response.body().string();
                    Principal.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                respuestaJson = "null";
                                if (myResponse.equals(respuestaJson)) {
                                    Toast.makeText(getApplicationContext(), "NO SE CUENTA CON INFORMACIÓN", Toast.LENGTH_SHORT).show();
                                } else {
                                    JSONArray ja = null;
                                    try {
                                        ja = new JSONArray("" + myResponse + "");
                                        for (int i = 0; i < ja.length(); i++) {
                                            try {
                                                idCargo = (ja.getJSONObject(i).getInt("IdCargo"));
                                                cargo = (ja.getJSONObject(i).getString("Cargo"));
                                                dataHelper.insertCatCargo(idCargo,cargo);
                                                System.out.println(ja);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }

        });
    }
    public void getConocimientoInfraccion() {
        DataHelper dataHelper = new DataHelper(getApplicationContext());

        final OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url("http://189.254.7.167/WebServiceIPH/api/CatConocimientoInfraccion")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Looper.prepare();
                Toast.makeText(getApplicationContext(), "ERROR AL OBTENER LA INFORMACIÓN, POR FAVOR VERIFIQUE SU CONEXIÓN A INTERNET", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String myResponse = response.body().string();
                    Principal.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                respuestaJson = "null";
                                if (myResponse.equals(respuestaJson)) {
                                    Toast.makeText(getApplicationContext(), "NO SE CUENTA CON INFORMACIÓN", Toast.LENGTH_SHORT).show();
                                } else {
                                    JSONArray ja = null;
                                    try {
                                        ja = new JSONArray("" + myResponse + "");
                                        for (int i = 0; i < ja.length(); i++) {
                                            try {
                                                idConocimiento = (ja.getJSONObject(i).getInt("IdConocimiento"));
                                                conocimiento = (ja.getJSONObject(i).getString("Conocimiento"));
                                                dataHelper.insertCatConocimientoInfraccion(idConocimiento,conocimiento);
                                                System.out.println(ja);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }

        });
    }
    public void getFiscaliaAutoridad() {
        DataHelper dataHelper = new DataHelper(getApplicationContext());

        final OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url("http://189.254.7.167/WebServiceIPH/api/CatFiscaliaAutoridad")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Looper.prepare();
                Toast.makeText(getApplicationContext(), "ERROR AL OBTENER LA INFORMACIÓN, POR FAVOR VERIFIQUE SU CONEXIÓN A INTERNET", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String myResponse = response.body().string();
                    Principal.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                respuestaJson = "null";
                                if (myResponse.equals(respuestaJson)) {
                                    Toast.makeText(getApplicationContext(), "NO SE CUENTA CON INFORMACIÓN", Toast.LENGTH_SHORT).show();
                                } else {
                                    JSONArray ja = null;
                                    try {
                                        ja = new JSONArray("" + myResponse + "");
                                        for (int i = 0; i < ja.length(); i++) {
                                            try {
                                                idFiscaliaAutoridad = (ja.getJSONObject(i).getInt("IdFiscaliaAutoridad"));
                                                fiscaliaAutoridad = (ja.getJSONObject(i).getString("FiscaliaAutoridad"));
                                                dataHelper.insertCatFiscaliaAutoridad(idFiscaliaAutoridad,fiscaliaAutoridad);
                                                System.out.println(ja);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }

        });
    }
    public void getInstitucion() {
        DataHelper dataHelper = new DataHelper(getApplicationContext());

        final OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url("http://189.254.7.167/WebServiceIPH/api/CatInstitucion")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Looper.prepare();
                Toast.makeText(getApplicationContext(), "ERROR AL OBTENER LA INFORMACIÓN, POR FAVOR VERIFIQUE SU CONEXIÓN A INTERNET", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String myResponse = response.body().string();
                    Principal.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                respuestaJson = "null";
                                if (myResponse.equals(respuestaJson)) {
                                    Toast.makeText(getApplicationContext(), "NO SE CUENTA CON INFORMACIÓN", Toast.LENGTH_SHORT).show();
                                } else {
                                    JSONArray ja = null;
                                    try {
                                        ja = new JSONArray("" + myResponse + "");
                                        for (int i = 0; i < ja.length(); i++) {
                                            try {
                                                idInstitucion = (ja.getJSONObject(i).getInt("IdInstitucion"));
                                                institucion = (ja.getJSONObject(i).getString("Institucion"));
                                                dataHelper.insertCatInstitucion(idInstitucion,institucion);
                                                System.out.println(ja);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }

        });
    }
    public void getLugarTraslado() {
        DataHelper dataHelper = new DataHelper(getApplicationContext());

        final OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url("http://189.254.7.167/WebServiceIPH/api/CatLugarTraslado")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Looper.prepare();
                Toast.makeText(getApplicationContext(), "ERROR AL OBTENER LA INFORMACIÓN, POR FAVOR VERIFIQUE SU CONEXIÓN A INTERNET", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String myResponse = response.body().string();
                    Principal.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                respuestaJson = "null";
                                if (myResponse.equals(respuestaJson)) {
                                    Toast.makeText(getApplicationContext(), "NO SE CUENTA CON INFORMACIÓN", Toast.LENGTH_SHORT).show();
                                } else {
                                    JSONArray ja = null;
                                    try {
                                        ja = new JSONArray("" + myResponse + "");
                                        for (int i = 0; i < ja.length(); i++) {
                                            try {
                                                idLugarTraslado = (ja.getJSONObject(i).getInt("IdLugarTraslado"));
                                                lugarTraslado = (ja.getJSONObject(i).getString("LugarTraslado"));
                                                descripcion = (ja.getJSONObject(i).getString("Descripcion"));
                                                dataHelper.insertCatLugarTraslado(idLugarTraslado,lugarTraslado,descripcion);
                                                System.out.println(ja);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }

        });
    }
    public void getMunicipios() {
        DataHelper dataHelper = new DataHelper(getApplicationContext());

        final OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url("http://189.254.7.167/WebServiceIPH/api/CatMunicipios")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Looper.prepare();
                Toast.makeText(getApplicationContext(), "ERROR AL OBTENER LA INFORMACIÓN, POR FAVOR VERIFIQUE SU CONEXIÓN A INTERNET", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String myResponse = response.body().string();
                    Principal.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                respuestaJson = "null";
                                if (myResponse.equals(respuestaJson)) {
                                    Toast.makeText(getApplicationContext(), "NO SE CUENTA CON INFORMACIÓN", Toast.LENGTH_SHORT).show();
                                } else {
                                    JSONArray ja = null;
                                    try {
                                        ja = new JSONArray("" + myResponse + "");
                                        for (int i = 0; i < ja.length(); i++) {
                                            try {
                                                idEntidadFederativa = (ja.getJSONObject(i).getInt("IdEntidadFederativa"));
                                                idMunicipio = (ja.getJSONObject(i).getString("IdMunicipio"));
                                                municipio = (ja.getJSONObject(i).getString("Municipio"));
                                                dataHelper.insertCatMunicipios(idEntidadFederativa,idMunicipio,municipio);
                                                System.out.println(ja);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }

        });
    }
    public void getNacionalidad() {
        DataHelper dataHelper = new DataHelper(getApplicationContext());

        final OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url("http://189.254.7.167/WebServiceIPH/api/CatNacionalidad")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Looper.prepare();
                Toast.makeText(getApplicationContext(), "ERROR AL OBTENER LA INFORMACIÓN, POR FAVOR VERIFIQUE SU CONEXIÓN A INTERNET", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String myResponse = response.body().string();
                    Principal.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                respuestaJson = "null";
                                if (myResponse.equals(respuestaJson)) {
                                    Toast.makeText(getApplicationContext(), "NO SE CUENTA CON INFORMACIÓN", Toast.LENGTH_SHORT).show();
                                } else {
                                    JSONArray ja = null;
                                    try {
                                        ja = new JSONArray("" + myResponse + "");
                                        for (int i = 0; i < ja.length(); i++) {
                                            try {
                                                idNacionalida = (ja.getJSONObject(i).getInt("IdNacionalida"));
                                                nacionalida = (ja.getJSONObject(i).getString("Nacionalida"));
                                                desNacionalidad = (ja.getJSONObject(i).getString("DesNacionalidad"));
                                                dataHelper.insertCatNacionalidad(idNacionalida,nacionalida,desNacionalidad);
                                                System.out.println(ja);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }

        });
    }
    public void getSexo() {
        DataHelper dataHelper = new DataHelper(getApplicationContext());

        final OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url("http://189.254.7.167/WebServiceIPH/api/CatSexo")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Looper.prepare();
                Toast.makeText(getApplicationContext(), "ERROR AL OBTENER LA INFORMACIÓN, POR FAVOR VERIFIQUE SU CONEXIÓN A INTERNET", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String myResponse = response.body().string();
                    Principal.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                respuestaJson = "null";
                                if (myResponse.equals(respuestaJson)) {
                                    Toast.makeText(getApplicationContext(), "NO SE CUENTA CON INFORMACIÓN", Toast.LENGTH_SHORT).show();
                                } else {
                                    JSONArray ja = null;
                                    try {
                                        ja = new JSONArray("" + myResponse + "");
                                        for (int i = 0; i < ja.length(); i++) {
                                            try {
                                                idSexo = (ja.getJSONObject(i).getInt("IdSexo"));
                                                sexo = (ja.getJSONObject(i).getString("Sexo"));
                                                dataHelper.insertCatSexo(idSexo,sexo);
                                                System.out.println(ja);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }

        });
    }
    public void getUnidad() {
        DataHelper dataHelper = new DataHelper(getApplicationContext());

        final OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url("http://189.254.7.167/WebServiceIPH/api/CatUnidad")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Looper.prepare();
                Toast.makeText(getApplicationContext(), "ERROR AL OBTENER LA INFORMACIÓN, POR FAVOR VERIFIQUE SU CONEXIÓN A INTERNET", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String myResponse = response.body().string();
                   Principal.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                respuestaJson = "null";
                                if (myResponse.equals(respuestaJson)) {
                                    Toast.makeText(getApplicationContext(), "NO SE CUENTA CON INFORMACIÓN", Toast.LENGTH_SHORT).show();
                                } else {
                                    JSONArray ja = null;
                                    try {
                                        ja = new JSONArray("" + myResponse + "");
                                        for (int i = 0; i < ja.length(); i++) {
                                            try {
                                                idUnidad = (ja.getJSONObject(i).getString("IdUnidad"));
                                                unidad = (ja.getJSONObject(i).getString("Unidad"));
                                                idMarca = (ja.getJSONObject(i).getString("IdMarca"));
                                                idSubMarca = (ja.getJSONObject(i).getInt("IdSubMarca"));
                                                modelo = (ja.getJSONObject(i).getInt("Modelo"));
                                                descripcionU = (ja.getJSONObject(i).getString("Descripcion"));
                                                idInstitucionU = (ja.getJSONObject(i).getInt("IdInstitucion"));
                                                dataHelper.insertCatUnidad(idUnidad,unidad,idMarca,idSubMarca,modelo,descripcionU,idInstitucionU);
                                                System.out.println(ja);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }

        });
    }
    public void getMarcaV() {
        DataHelper dataHelper = new DataHelper(getApplicationContext());

        final OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url("http://189.254.7.167/WebServiceIPH/api/CatMarcaVehiculos")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Looper.prepare();
                Toast.makeText(getApplicationContext(), "ERROR AL OBTENER LA INFORMACIÓN, POR FAVOR VERIFIQUE SU CONEXIÓN A INTERNET", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String myResponse = response.body().string();
                    Principal.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                respuestaJson = "null";
                                if (myResponse.equals(respuestaJson)) {
                                    Toast.makeText(getApplicationContext(), "NO SE CUENTA CON INFORMACIÓN", Toast.LENGTH_SHORT).show();
                                } else {
                                    JSONArray ja = null;
                                    try {
                                        ja = new JSONArray("" + myResponse + "");
                                        for (int i = 0; i < ja.length(); i++) {
                                            try {
                                                idMarca = (ja.getJSONObject(i).getString("IdMarca"));
                                                marca = (ja.getJSONObject(i).getString("Marca"));
                                                dataHelper.insertCatVehiculos(idMarca,marca);
                                                System.out.println(ja);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }

        });
    }
    public void getColor() {
        DataHelper dataHelper = new DataHelper(getApplicationContext());

        final OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url("http://189.254.7.167/WebServiceIPH/api/CatColor")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Looper.prepare();
                Toast.makeText(getApplicationContext(), "ERROR AL OBTENER LA INFORMACIÓN, POR FAVOR VERIFIQUE SU CONEXIÓN A INTERNET", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String myResponse = response.body().string();
                    Principal.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                respuestaJson = "null";
                                if (myResponse.equals(respuestaJson)) {
                                    Toast.makeText(getApplicationContext(), "NO SE CUENTA CON INFORMACIÓN", Toast.LENGTH_SHORT).show();
                                } else {
                                    JSONArray ja = null;
                                    try {
                                        ja = new JSONArray("" + myResponse + "");
                                        for (int i = 0; i < ja.length(); i++) {
                                            try {
                                                idColor = (ja.getJSONObject(i).getString("IdColor"));
                                                color = (ja.getJSONObject(i).getString("Color"));
                                                dataHelper.insertCatColor(idColor,color);
                                                System.out.println(ja);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }

        });
    }
    public void getAnio() {
        DataHelper dataHelper = new DataHelper(getApplicationContext());

        final OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url("http://189.254.7.167/WebServiceIPH/api/CatAnio")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Looper.prepare();
                Toast.makeText(getApplicationContext(), "ERROR AL OBTENER LA INFORMACIÓN, POR FAVOR VERIFIQUE SU CONEXIÓN A INTERNET", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String myResponse = response.body().string();
                    Principal.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                respuestaJson = "null";
                                if (myResponse.equals(respuestaJson)) {
                                    Toast.makeText(getApplicationContext(), "NO SE CUENTA CON INFORMACIÓN", Toast.LENGTH_SHORT).show();
                                } else {
                                    JSONArray ja = null;
                                    try {
                                        ja = new JSONArray("" + myResponse + "");
                                        for (int i = 0; i < ja.length(); i++) {
                                            try {
                                                modeloVehiculo = (ja.getJSONObject(i).getString("MODELO"));
                                                dataHelper.insertCatModelo(modeloVehiculo);
                                                System.out.println(ja);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }

        });
    }
    /******************************************************** SUB MARCAS VEHÍCULOS, 1588 REGISTROS ********************************************************/
    private void ListSubMarca() {
        DataHelper dataHelper = new DataHelper(getApplicationContext());
        ArrayList<String> list = dataHelper.getAllSubMarcaVehiculos();
        if (list.size() > 0) {
            System.out.println("YA EXISTE INFORMACIÓN DE SUBMARCAVEHICULOS");
        }else{
            //getSubmarcaV();
            dataHelper.insertCatSubMarcaVehiculos("ABRT",1,"500 MICROURBANO");
            dataHelper.insertCatSubMarcaVehiculos("ACUR",2,"NSX");
            dataHelper.insertCatSubMarcaVehiculos("ACUR",3,"RDX");
            dataHelper.insertCatSubMarcaVehiculos("ACUR",4,"MDX");
            dataHelper.insertCatSubMarcaVehiculos("ACUR",5,"CL");
            dataHelper.insertCatSubMarcaVehiculos("ACUR",6,"RL");
            dataHelper.insertCatSubMarcaVehiculos("ACUR",7,"RLX");
            dataHelper.insertCatSubMarcaVehiculos("ACUR",8,"TL");
            dataHelper.insertCatSubMarcaVehiculos("ACUR",9,"TLX");
            dataHelper.insertCatSubMarcaVehiculos("ALFA",10,"GIULIETTA");
            dataHelper.insertCatSubMarcaVehiculos("ALFA",11,"GT");
            dataHelper.insertCatSubMarcaVehiculos("ALFA",12,"GIULIA");
            dataHelper.insertCatSubMarcaVehiculos("ALFA",13,"BRERA");
            dataHelper.insertCatSubMarcaVehiculos("ALFA",14,"75");
            dataHelper.insertCatSubMarcaVehiculos("ALFA",15,"4C");
            dataHelper.insertCatSubMarcaVehiculos("ALFA",16,"166");
            dataHelper.insertCatSubMarcaVehiculos("ALFA",17,"159");
            dataHelper.insertCatSubMarcaVehiculos("ALFA",18,"156");
            dataHelper.insertCatSubMarcaVehiculos("ALFA",19,"147");
            dataHelper.insertCatSubMarcaVehiculos("ALFA",20,"GTV");
            dataHelper.insertCatSubMarcaVehiculos("ALFA",21,"MITO");
            dataHelper.insertCatSubMarcaVehiculos("ALFA",22,"SPIDER");
            dataHelper.insertCatSubMarcaVehiculos("ALFA",23,"STELVIO");
            dataHelper.insertCatSubMarcaVehiculos("ASTO",24,"RAPIDE");
            dataHelper.insertCatSubMarcaVehiculos("ASTO",25,"VANQUISH");
            dataHelper.insertCatSubMarcaVehiculos("ASTO",26,"DB11");
            dataHelper.insertCatSubMarcaVehiculos("ASTO",27,"VANTAGE GT12");
            dataHelper.insertCatSubMarcaVehiculos("ASTO",28,"VANTAGE GT8");
            dataHelper.insertCatSubMarcaVehiculos("ASTO",29,"VANTAGE");
            dataHelper.insertCatSubMarcaVehiculos("ASTO",30,"VANQUISH VOLANTE");
            dataHelper.insertCatSubMarcaVehiculos("ASTO",31,"DB9");
            dataHelper.insertCatSubMarcaVehiculos("ASTO",32,"DB9 GT");
            dataHelper.insertCatSubMarcaVehiculos("ASTO",33,"PAST MODELS");
            dataHelper.insertCatSubMarcaVehiculos("ASTO",34,"RAPIDE S");
            dataHelper.insertCatSubMarcaVehiculos("ASTO",35,"V12 VANTAGE S");
            dataHelper.insertCatSubMarcaVehiculos("ASTO",36,"V12 VANTAGE S ROADSTER");
            dataHelper.insertCatSubMarcaVehiculos("ASTO",37,"V8 VANTAGE S");
            dataHelper.insertCatSubMarcaVehiculos("ASTO",38,"V8 VANTAGE S ROADSTER");
            dataHelper.insertCatSubMarcaVehiculos("AUDI",39,"A1");
            dataHelper.insertCatSubMarcaVehiculos("AUDI",40,"A3");
            dataHelper.insertCatSubMarcaVehiculos("AUDI",41,"A4");
            dataHelper.insertCatSubMarcaVehiculos("AUDI",42,"A5");
            dataHelper.insertCatSubMarcaVehiculos("AUDI",43,"A6");
            dataHelper.insertCatSubMarcaVehiculos("AUDI",44,"A7");
            dataHelper.insertCatSubMarcaVehiculos("AUDI",45,"A8");
            dataHelper.insertCatSubMarcaVehiculos("AUDI",46,"Q2");
            dataHelper.insertCatSubMarcaVehiculos("AUDI",47,"Q3");
            dataHelper.insertCatSubMarcaVehiculos("AUDI",48,"Q5");
            dataHelper.insertCatSubMarcaVehiculos("AUDI",49,"Q7");
            dataHelper.insertCatSubMarcaVehiculos("AUDI",50,"Q8");
            dataHelper.insertCatSubMarcaVehiculos("AUDI",51,"S3");
            dataHelper.insertCatSubMarcaVehiculos("AUDI",52,"TT");
            dataHelper.insertCatSubMarcaVehiculos("AUDI",53,"R8");
            dataHelper.insertCatSubMarcaVehiculos("AUDI",54,"A4 SEDAN");
            dataHelper.insertCatSubMarcaVehiculos("AUDI",55,"A5 COUPE");
            dataHelper.insertCatSubMarcaVehiculos("AUDI",56,"A3 SEDAN");
            dataHelper.insertCatSubMarcaVehiculos("AUDI",57,"A3 E-TRON CONCEPT");
            dataHelper.insertCatSubMarcaVehiculos("AUDI",58,"A3 CABRIOLET");
            dataHelper.insertCatSubMarcaVehiculos("AUDI",59,"A1 SPORTBACK");
            dataHelper.insertCatSubMarcaVehiculos("AUDI",60,"A1 E-TRON");
            dataHelper.insertCatSubMarcaVehiculos("AUDI",61,"90");
            dataHelper.insertCatSubMarcaVehiculos("AUDI",62,"A5 SPORTBACK");
            dataHelper.insertCatSubMarcaVehiculos("AUDI",63,"A6 SEDAN");
            dataHelper.insertCatSubMarcaVehiculos("AUDI",64,"A8 L");
            dataHelper.insertCatSubMarcaVehiculos("AUDI",65,"A8 L W12");
            dataHelper.insertCatSubMarcaVehiculos("AUDI",66,"A7 SPORTBACK");
            dataHelper.insertCatSubMarcaVehiculos("AUDI",67,"A8 SEDAN");
            dataHelper.insertCatSubMarcaVehiculos("AUDI",68,"ALLROAD");
            dataHelper.insertCatSubMarcaVehiculos("AUDI",69,"CABRIOLET");
            dataHelper.insertCatSubMarcaVehiculos("AUDI",70,"E-TRON SPYDER");
            dataHelper.insertCatSubMarcaVehiculos("AUDI",71,"R8 COUPE");
            dataHelper.insertCatSubMarcaVehiculos("AUDI",72,"RS Q3 PERFORMANCE");
            dataHelper.insertCatSubMarcaVehiculos("AUDI",73,"RS4");
            dataHelper.insertCatSubMarcaVehiculos("AUDI",74,"RS5");
            dataHelper.insertCatSubMarcaVehiculos("AUDI",75,"RS6");
            dataHelper.insertCatSubMarcaVehiculos("AUDI",76,"RS7 PERFORMANCE");
            dataHelper.insertCatSubMarcaVehiculos("AUDI",77,"S3 SEDAN");
            dataHelper.insertCatSubMarcaVehiculos("AUDI",78,"S4");
            dataHelper.insertCatSubMarcaVehiculos("AUDI",79,"S5 COUPE");
            dataHelper.insertCatSubMarcaVehiculos("AUDI",80,"S5 SPORTBACK");
            dataHelper.insertCatSubMarcaVehiculos("AUDI",81,"S6 SEDAN");
            dataHelper.insertCatSubMarcaVehiculos("AUDI",82,"S7 SPORTBACK");
            dataHelper.insertCatSubMarcaVehiculos("AUDI",83,"S8 SEDAN");
            dataHelper.insertCatSubMarcaVehiculos("AUDI",84,"SQ5");
            dataHelper.insertCatSubMarcaVehiculos("AUDI",85,"TT COUPE");
            dataHelper.insertCatSubMarcaVehiculos("AUDI",86,"TTS COUPE");
            dataHelper.insertCatSubMarcaVehiculos("AVIO",87,"CESSNA");
            dataHelper.insertCatSubMarcaVehiculos("AVIO",88,"KING AIR");
            dataHelper.insertCatSubMarcaVehiculos("AVIO",89,"OTROS MODELOS");
            dataHelper.insertCatSubMarcaVehiculos("AVIO",90,"PIPER");
            dataHelper.insertCatSubMarcaVehiculos("BENT",91,"CONTINENTAL FLYING SPUR");
            dataHelper.insertCatSubMarcaVehiculos("BENT",92,"CONTINENTAL GT");
            dataHelper.insertCatSubMarcaVehiculos("BENT",93,"CONTINENTAL GTC");
            dataHelper.insertCatSubMarcaVehiculos("BENT",94,"MULSANNE");
            dataHelper.insertCatSubMarcaVehiculos("BENT",95,"BENTAYGA DIESEL");
            dataHelper.insertCatSubMarcaVehiculos("BENT",96,"BENTAYGA");
            dataHelper.insertCatSubMarcaVehiculos("BENT",97,"ARNAGE");
            dataHelper.insertCatSubMarcaVehiculos("BENT",98,"CONTINENTAL GT CONVERTIBLE");
            dataHelper.insertCatSubMarcaVehiculos("BENT",99,"CONTINENTAL GT SPEED");
            dataHelper.insertCatSubMarcaVehiculos("BENT",100,"CONTINENTAL GT SPEED CONVERTIBLE");
            dataHelper.insertCatSubMarcaVehiculos("BENT",101,"CONTINENTAL GT V8");
            dataHelper.insertCatSubMarcaVehiculos("BENT",102,"CONTINENTAL GT V8 S");
            dataHelper.insertCatSubMarcaVehiculos("BENT",103,"CONTINENTAL GT V8 CONVERTIBLE");
            dataHelper.insertCatSubMarcaVehiculos("BENT",104,"MULSANNE SPEED");
            dataHelper.insertCatSubMarcaVehiculos("BENT",105,"S8 SEDAN");
            dataHelper.insertCatSubMarcaVehiculos("BENT",106,"FLYING SPUR W12 S");
            dataHelper.insertCatSubMarcaVehiculos("BENT",107,"FLYING SPUR");
            dataHelper.insertCatSubMarcaVehiculos("BENT",108,"FLYING SPUR V8 S");
            dataHelper.insertCatSubMarcaVehiculos("BENT",109,"FLYING SPUR V8");
            dataHelper.insertCatSubMarcaVehiculos("BENT",110,"CONTINENTAL GT3-R");
            dataHelper.insertCatSubMarcaVehiculos("BENT",111,"CONTINENTAL GT V8 S CONVERTIBLE");
            dataHelper.insertCatSubMarcaVehiculos("BMW",112,"I3 HATCHBACK");
            dataHelper.insertCatSubMarcaVehiculos("BMW",113,"SERIE 1");
            dataHelper.insertCatSubMarcaVehiculos("BMW",114,"SERIE 2");
            dataHelper.insertCatSubMarcaVehiculos("BMW",115,"SERIE 3");
            dataHelper.insertCatSubMarcaVehiculos("BMW",116,"SERIE 4");
            dataHelper.insertCatSubMarcaVehiculos("BMW",117,"SERIE 5");
            dataHelper.insertCatSubMarcaVehiculos("BMW",118,"SERIE 6");
            dataHelper.insertCatSubMarcaVehiculos("BMW",119,"SERIE 7");
            dataHelper.insertCatSubMarcaVehiculos("BMW",120,"I8");
            dataHelper.insertCatSubMarcaVehiculos("BMW",121,"X2 CROSSOVER");
            dataHelper.insertCatSubMarcaVehiculos("BMW",122,"M6 GRAN COUPE");
            dataHelper.insertCatSubMarcaVehiculos("BMW",123,"SERIE 2 ACTIVE TOURER");
            dataHelper.insertCatSubMarcaVehiculos("BMW",124,"SERIE 2 COUPE");
            dataHelper.insertCatSubMarcaVehiculos("BMW",125,"SERIE 2 GRAN TOURER");
            dataHelper.insertCatSubMarcaVehiculos("BMW",126,"SERIE 4 CONVERTIBLE");
            dataHelper.insertCatSubMarcaVehiculos("BMW",127,"SERIE 6 COUPE");
            dataHelper.insertCatSubMarcaVehiculos("BMW",128,"SERIE 4 GRAN COUPE");
            dataHelper.insertCatSubMarcaVehiculos("BMW",129,"SERIE 4 COUPE");
            dataHelper.insertCatSubMarcaVehiculos("BMW",130,"Z8");
            dataHelper.insertCatSubMarcaVehiculos("BMW",131,"Z4 ROADSTER");
            dataHelper.insertCatSubMarcaVehiculos("BMW",132,"Z4");
            dataHelper.insertCatSubMarcaVehiculos("BMW",133,"Z3");
            dataHelper.insertCatSubMarcaVehiculos("BMW",134,"X6 M");
            dataHelper.insertCatSubMarcaVehiculos("BMW",135,"X6");
            dataHelper.insertCatSubMarcaVehiculos("BMW",136,"X5 M");
            dataHelper.insertCatSubMarcaVehiculos("BMW",137,"X5");
            dataHelper.insertCatSubMarcaVehiculos("BMW",138,"X4");
            dataHelper.insertCatSubMarcaVehiculos("BMW",139,"X3");
            dataHelper.insertCatSubMarcaVehiculos("BMW",140,"X1");
            dataHelper.insertCatSubMarcaVehiculos("BMW",141,"SERIE 8");
            dataHelper.insertCatSubMarcaVehiculos("BMW",142,"SERIE 6 GRAN COUPE");
            dataHelper.insertCatSubMarcaVehiculos("BMW",143,"M5 SEDAN");
            dataHelper.insertCatSubMarcaVehiculos("BMW",144,"M6 COUPE");
            dataHelper.insertCatSubMarcaVehiculos("BMW",145,"M4 COUPE");
            dataHelper.insertCatSubMarcaVehiculos("BMW",146,"M4 CONVERTIBLE");
            dataHelper.insertCatSubMarcaVehiculos("BMW",147,"M3 SEDAN");
            dataHelper.insertCatSubMarcaVehiculos("BMW",148,"M2 COUPE");
            dataHelper.insertCatSubMarcaVehiculos("BMW",149,"2800");
            dataHelper.insertCatSubMarcaVehiculos("BMW",150,"I 3");
            dataHelper.insertCatSubMarcaVehiculos("BMW",151,"I 8");
            dataHelper.insertCatSubMarcaVehiculos("BMW",152,"M");
            dataHelper.insertCatSubMarcaVehiculos("BUIC",153,"REGAL");
            dataHelper.insertCatSubMarcaVehiculos("BUIC",154,"ENVISION");
            dataHelper.insertCatSubMarcaVehiculos("BUIC",155,"ENCORE");
            dataHelper.insertCatSubMarcaVehiculos("BUIC",156,"ENCLAVE");
            dataHelper.insertCatSubMarcaVehiculos("BUIC",157,"REGAL GS");
            dataHelper.insertCatSubMarcaVehiculos("BUIC",158,"RIVIERA");
            dataHelper.insertCatSubMarcaVehiculos("BUIC",159,"VERANO");
            dataHelper.insertCatSubMarcaVehiculos("CADI",160,"ATS");
            dataHelper.insertCatSubMarcaVehiculos("CADI",161,"CTS");
            dataHelper.insertCatSubMarcaVehiculos("CADI",162,"ESCALADE");
            dataHelper.insertCatSubMarcaVehiculos("CADI",163,"XT4");
            dataHelper.insertCatSubMarcaVehiculos("CADI",164,"CTS V");
            dataHelper.insertCatSubMarcaVehiculos("CADI",165,"DE VILLE CONCOURS");
            dataHelper.insertCatSubMarcaVehiculos("CADI",166,"DE VILLE");
            dataHelper.insertCatSubMarcaVehiculos("CADI",167,"CATERA");
            dataHelper.insertCatSubMarcaVehiculos("CADI",168,"BLS");
            dataHelper.insertCatSubMarcaVehiculos("CADI",169,"ALLANTE");
            dataHelper.insertCatSubMarcaVehiculos("CADI",170,"ATS COUPE");
            dataHelper.insertCatSubMarcaVehiculos("CADI",171,"ATS SEDAN");
            dataHelper.insertCatSubMarcaVehiculos("CADI",172,"ATS V");
            dataHelper.insertCatSubMarcaVehiculos("CADI",173,"XT5");
            dataHelper.insertCatSubMarcaVehiculos("CADI",174,"XLR");
            dataHelper.insertCatSubMarcaVehiculos("CADI",175,"STS");
            dataHelper.insertCatSubMarcaVehiculos("CADI",176,"SRX");
            dataHelper.insertCatSubMarcaVehiculos("CADI",177,"ESCALADE ESV");
            dataHelper.insertCatSubMarcaVehiculos("CADI",178,"SEVILLE");
            dataHelper.insertCatSubMarcaVehiculos("CADI",179,"ESCALADE EXT");
            dataHelper.insertCatSubMarcaVehiculos("CADI",180,"ELDORADO");
            dataHelper.insertCatSubMarcaVehiculos("CASA",181,"MOTORHOME");
            dataHelper.insertCatSubMarcaVehiculos("CASA",182,"DUTCHMEN");
            dataHelper.insertCatSubMarcaVehiculos("CASA",183,"CHEVROLET");
            dataHelper.insertCatSubMarcaVehiculos("CASA",184,"COACHMEN");
            dataHelper.insertCatSubMarcaVehiculos("CASA",185,"OTROS MODELOS");
            dataHelper.insertCatSubMarcaVehiculos("CASA",186,"PROWLER");
            dataHelper.insertCatSubMarcaVehiculos("CASA",187,"WEEKENDER");
            dataHelper.insertCatSubMarcaVehiculos("CATE",188,"TRACTOR DE CADENAS");
            dataHelper.insertCatSubMarcaVehiculos("CATE",189,"RETROEXCAVADORA");
            dataHelper.insertCatSubMarcaVehiculos("CATE",190,"MINIEXCAVADORA");
            dataHelper.insertCatSubMarcaVehiculos("CATE",191,"416D");
            dataHelper.insertCatSubMarcaVehiculos("CATE",192,"CARGADOR DE RUEDAS");
            dataHelper.insertCatSubMarcaVehiculos("CATE",193,"MANIPULADOR TELESCOPICO");
            dataHelper.insertCatSubMarcaVehiculos("CATE",194,"MINICARGADOR");
            dataHelper.insertCatSubMarcaVehiculos("CHAN",195,"STAR LIGHT 4500");
            dataHelper.insertCatSubMarcaVehiculos("CHAN",196,"Q20");
            dataHelper.insertCatSubMarcaVehiculos("CHAN",197,"STAR 3");
            dataHelper.insertCatSubMarcaVehiculos("CHAN",198,"MINIVAN");
            dataHelper.insertCatSubMarcaVehiculos("CHAN",199,"MINITRUCK");
            dataHelper.insertCatSubMarcaVehiculos("CHEV",200,"AVEO");
            dataHelper.insertCatSubMarcaVehiculos("CHEV",201,"BEAT");
            dataHelper.insertCatSubMarcaVehiculos("CHEV",202,"CAVALIER");
            dataHelper.insertCatSubMarcaVehiculos("CHEV",203,"SPARK");
            dataHelper.insertCatSubMarcaVehiculos("CHEV",204,"CAMARO");
            dataHelper.insertCatSubMarcaVehiculos("CHEV",205,"EQUINOX");
            dataHelper.insertCatSubMarcaVehiculos("CHEV",206,"CORVETTE");
            dataHelper.insertCatSubMarcaVehiculos("CHEV",207,"TRAX CROSSOVER");
            dataHelper.insertCatSubMarcaVehiculos("CHEV",208,"TRAVERSE");
            dataHelper.insertCatSubMarcaVehiculos("CHEV",209,"BLAZER");
            dataHelper.insertCatSubMarcaVehiculos("CHEV",210,"SILVERADO");
            dataHelper.insertCatSubMarcaVehiculos("CHEV",211,"TAHOE");
            dataHelper.insertCatSubMarcaVehiculos("CHEV",212,"SUBURBAN");
            dataHelper.insertCatSubMarcaVehiculos("CHEV",213,"TORNADO");
            dataHelper.insertCatSubMarcaVehiculos("CHEV",214,"COLORADO");
            dataHelper.insertCatSubMarcaVehiculos("CHEV",215,"VOLT");
            dataHelper.insertCatSubMarcaVehiculos("CHEV",216,"BOLT EV");
            dataHelper.insertCatSubMarcaVehiculos("CHEV",217,"ASTRO");
            dataHelper.insertCatSubMarcaVehiculos("CHEV",218,"ASTRA");
            dataHelper.insertCatSubMarcaVehiculos("CHEV",219,"APACHE");
            dataHelper.insertCatSubMarcaVehiculos("CHEV",220,"400 SS");
            dataHelper.insertCatSubMarcaVehiculos("CHEV",221,"AVALANCHE");
            dataHelper.insertCatSubMarcaVehiculos("CHEV",222,"BEL AIR");
            dataHelper.insertCatSubMarcaVehiculos("CHEV",223,"BRAVADA");
            dataHelper.insertCatSubMarcaVehiculos("CHEV",224,"C 3500");
            dataHelper.insertCatSubMarcaVehiculos("CHEV",225,"CAPRICE");
            dataHelper.insertCatSubMarcaVehiculos("CHEV",226,"CARAVAN");
            dataHelper.insertCatSubMarcaVehiculos("CHEV",227,"CAPTIVA SPORT");
            dataHelper.insertCatSubMarcaVehiculos("CHEV",228,"CELEBRITY");
            dataHelper.insertCatSubMarcaVehiculos("CHEV",229,"CENTURY");
            dataHelper.insertCatSubMarcaVehiculos("CHEV",230,"CITATION");
            dataHelper.insertCatSubMarcaVehiculos("CHEV",231,"CORSA");
            dataHelper.insertCatSubMarcaVehiculos("CHEV",232,"CORSICA");
            dataHelper.insertCatSubMarcaVehiculos("CHEV",233,"CRUZE");
            dataHelper.insertCatSubMarcaVehiculos("CHEV",234,"CUTLASS");
            dataHelper.insertCatSubMarcaVehiculos("CHEV",235,"CHEVELLE");
            dataHelper.insertCatSubMarcaVehiculos("CHEV",236,"CHEVY");
            dataHelper.insertCatSubMarcaVehiculos("CHEV",237,"CHEVY C2");
            dataHelper.insertCatSubMarcaVehiculos("CHEV",238,"CHEVY NOVA");
            dataHelper.insertCatSubMarcaVehiculos("CHEV",239,"CHEVY VAN");
            dataHelper.insertCatSubMarcaVehiculos("CHEV",240,"CHEYENNE");
            dataHelper.insertCatSubMarcaVehiculos("CHEV",241,"EXPRESS");
            dataHelper.insertCatSubMarcaVehiculos("CHEV",242,"EXPRESS VAN");
            dataHelper.insertCatSubMarcaVehiculos("CHEV",243,"HHR");
            dataHelper.insertCatSubMarcaVehiculos("CHEV",244,"IMPALA");
            dataHelper.insertCatSubMarcaVehiculos("CHEV",245,"KODIAK");
            dataHelper.insertCatSubMarcaVehiculos("CHEV",246,"LUMINA");
            dataHelper.insertCatSubMarcaVehiculos("CHEV",247,"LUV");
            dataHelper.insertCatSubMarcaVehiculos("CHEV",248,"MALIBU");
            dataHelper.insertCatSubMarcaVehiculos("CHEV",249,"MATIZ");
            dataHelper.insertCatSubMarcaVehiculos("CHEV",250,"MAXICAB");
            dataHelper.insertCatSubMarcaVehiculos("CHEV",251,"MERIVA");
            dataHelper.insertCatSubMarcaVehiculos("CHEV",252,"MICROBUSES");
            dataHelper.insertCatSubMarcaVehiculos("CHEV",253,"MONTECARLO");
            dataHelper.insertCatSubMarcaVehiculos("CHEV",254,"MONZA");
            dataHelper.insertCatSubMarcaVehiculos("CHEV",255,"OLDSMOBILE");
            dataHelper.insertCatSubMarcaVehiculos("CHEV",256,"OLDSMOBILE EIGHTY");
            dataHelper.insertCatSubMarcaVehiculos("CHEV",257,"OLDSMOBILE SILHOUETTE");
            dataHelper.insertCatSubMarcaVehiculos("CHEV",258,"OPTRA");
            dataHelper.insertCatSubMarcaVehiculos("CHEV",259,"P-30");
            dataHelper.insertCatSubMarcaVehiculos("CHEV",260,"PICKUP");
            dataHelper.insertCatSubMarcaVehiculos("CHEV",261,"PIPA");
            dataHelper.insertCatSubMarcaVehiculos("CHEV",262,"S10");
            dataHelper.insertCatSubMarcaVehiculos("CHEV",263,"SILVERADO 1500");
            dataHelper.insertCatSubMarcaVehiculos("CHEV",264,"SILVERADO 2500");
            dataHelper.insertCatSubMarcaVehiculos("CHEV",265,"SILVERADO 3500");
            dataHelper.insertCatSubMarcaVehiculos("CHEV",266,"SILVERADO 3500 HD");
            dataHelper.insertCatSubMarcaVehiculos("CHEV",267,"SONIC");
            dataHelper.insertCatSubMarcaVehiculos("CHEV",268,"SONORA");
            dataHelper.insertCatSubMarcaVehiculos("CHEV",269,"SPARK CLASSIC");
            dataHelper.insertCatSubMarcaVehiculos("CHEV",270,"SIN ESPECIFICAR");
            dataHelper.insertCatSubMarcaVehiculos("CHEV",271,"TIGRA");
            dataHelper.insertCatSubMarcaVehiculos("CHEV",272,"TRACKER");
            dataHelper.insertCatSubMarcaVehiculos("CHEV",273,"TRAILBLAZER");
            dataHelper.insertCatSubMarcaVehiculos("CHEV",274,"TRAX");
            dataHelper.insertCatSubMarcaVehiculos("CHEV",275,"UPLANDER");
            dataHelper.insertCatSubMarcaVehiculos("CHEV",276,"VECTRA");
            dataHelper.insertCatSubMarcaVehiculos("CHEV",277,"VENTURE");
            dataHelper.insertCatSubMarcaVehiculos("CHEV",278,"ZAFIRA");
            dataHelper.insertCatSubMarcaVehiculos("CHRY",279,"PACÍFICA");
            dataHelper.insertCatSubMarcaVehiculos("CHRY",280,"300C");
            dataHelper.insertCatSubMarcaVehiculos("CHRY",281,"VOYAGER");
            dataHelper.insertCatSubMarcaVehiculos("CHRY",282,"VOLARE");
            dataHelper.insertCatSubMarcaVehiculos("CHRY",283,"VALIANT");
            dataHelper.insertCatSubMarcaVehiculos("CHRY",284,"TOWN & COUNTRY");
            dataHelper.insertCatSubMarcaVehiculos("CHRY",285,"SPIRIT");
            dataHelper.insertCatSubMarcaVehiculos("CHRY",286,"SHADOW");
            dataHelper.insertCatSubMarcaVehiculos("CHRY",287,"SEBRING");
            dataHelper.insertCatSubMarcaVehiculos("CHRY",288,"PT CRUISER");
            dataHelper.insertCatSubMarcaVehiculos("CHRY",289,"PLYMOUTH");
            dataHelper.insertCatSubMarcaVehiculos("CHRY",290,"PHANTOM");
            dataHelper.insertCatSubMarcaVehiculos("CHRY",291,"PACIFICA");
            dataHelper.insertCatSubMarcaVehiculos("CHRY",292,"NEW YORKER");
            dataHelper.insertCatSubMarcaVehiculos("CHRY",293,"MAGNUM");
            dataHelper.insertCatSubMarcaVehiculos("CHRY",294,"LEBARON");
            dataHelper.insertCatSubMarcaVehiculos("CHRY",295,"INTREPID");
            dataHelper.insertCatSubMarcaVehiculos("CHRY",296,"DART K");
            dataHelper.insertCatSubMarcaVehiculos("CHRY",297,"GRAND VOYAGER");
            dataHelper.insertCatSubMarcaVehiculos("CHRY",298,"GRAND CARAVAN");
            dataHelper.insertCatSubMarcaVehiculos("CHRY",299,"DAKOTA");
            dataHelper.insertCatSubMarcaVehiculos("CHRY",300,"CROSSFIRE");
            dataHelper.insertCatSubMarcaVehiculos("CHRY",301,"CORDOBA");
            dataHelper.insertCatSubMarcaVehiculos("CHRY",302,"CONCORDE");
            dataHelper.insertCatSubMarcaVehiculos("CHRY",303,"CIRRUS");
            dataHelper.insertCatSubMarcaVehiculos("CHRY",304,"CARAVAN SE");
            dataHelper.insertCatSubMarcaVehiculos("CHRY",305,"CARAVAN");
            dataHelper.insertCatSubMarcaVehiculos("CHRY",306,"BARRACUDA");
            dataHelper.insertCatSubMarcaVehiculos("CHRY",307,"300");
            dataHelper.insertCatSubMarcaVehiculos("CHRY",308,"300M");
            dataHelper.insertCatSubMarcaVehiculos("CHRY",309,"ASPEN");
            dataHelper.insertCatSubMarcaVehiculos("DATS",310,"SEDAN");
            dataHelper.insertCatSubMarcaVehiculos("DATS",311,"REDI-GO");
            dataHelper.insertCatSubMarcaVehiculos("DATS",312,"GO");
            dataHelper.insertCatSubMarcaVehiculos("DATS",313,"GO+");
            dataHelper.insertCatSubMarcaVehiculos("DATS",314,"MI-DO");
            dataHelper.insertCatSubMarcaVehiculos("DATS",315,"ON-DO");
            dataHelper.insertCatSubMarcaVehiculos("DFSK",361,"MINIVAN");
            dataHelper.insertCatSubMarcaVehiculos("DFSK",362,"S1000");
            dataHelper.insertCatSubMarcaVehiculos("DINA",316,"TORTON");
            dataHelper.insertCatSubMarcaVehiculos("DINA",317,"CHASIS CABINA");
            dataHelper.insertCatSubMarcaVehiculos("DINA",318,"AUTOBUS");
            dataHelper.insertCatSubMarcaVehiculos("DINA",319,"9400");
            dataHelper.insertCatSubMarcaVehiculos("DINA",320,"TRACTO");
            dataHelper.insertCatSubMarcaVehiculos("DINA",321,"VOLTEO");
            dataHelper.insertCatSubMarcaVehiculos("DODG",322,"ATTITUDE");
            dataHelper.insertCatSubMarcaVehiculos("DODG",323,"NEON");
            dataHelper.insertCatSubMarcaVehiculos("DODG",324,"DURANGO");
            dataHelper.insertCatSubMarcaVehiculos("DODG",325,"CHALLENGER");
            dataHelper.insertCatSubMarcaVehiculos("DODG",326,"CHARGER");
            dataHelper.insertCatSubMarcaVehiculos("DODG",327,"GRAND CARAVAN");
            dataHelper.insertCatSubMarcaVehiculos("DODG",328,"JOURNEY");
            dataHelper.insertCatSubMarcaVehiculos("DODG",329,"CORONET");
            dataHelper.insertCatSubMarcaVehiculos("DODG",330,"CARAVAN");
            dataHelper.insertCatSubMarcaVehiculos("DODG",331,"D 250");
            dataHelper.insertCatSubMarcaVehiculos("DODG",332,"D 150");
            dataHelper.insertCatSubMarcaVehiculos("DODG",333,"ATOS");
            dataHelper.insertCatSubMarcaVehiculos("DODG",334,"AVENGER");
            dataHelper.insertCatSubMarcaVehiculos("DODG",335,"CALIBER");
            dataHelper.insertCatSubMarcaVehiculos("DODG",336,"DAKOTA");
            dataHelper.insertCatSubMarcaVehiculos("DODG",337,"D 350");
            dataHelper.insertCatSubMarcaVehiculos("DODG",338,"DART");
            dataHelper.insertCatSubMarcaVehiculos("DODG",339,"H 100");
            dataHelper.insertCatSubMarcaVehiculos("DODG",340,"INTREPID");
            dataHelper.insertCatSubMarcaVehiculos("DODG",341,"MICRO BUS");
            dataHelper.insertCatSubMarcaVehiculos("DODG",342,"NITRO");
            dataHelper.insertCatSubMarcaVehiculos("DODG",343,"PICK UP");
            dataHelper.insertCatSubMarcaVehiculos("DODG",344,"RABON");
            dataHelper.insertCatSubMarcaVehiculos("DODG",345,"RAM");
            dataHelper.insertCatSubMarcaVehiculos("DODG",346,"RAM 1500");
            dataHelper.insertCatSubMarcaVehiculos("DODG",347,"RAM 2500");
            dataHelper.insertCatSubMarcaVehiculos("DODG",348,"RAM 3500");
            dataHelper.insertCatSubMarcaVehiculos("DODG",349,"RAM 4000");
            dataHelper.insertCatSubMarcaVehiculos("DODG",350,"RAM CHARGER");
            dataHelper.insertCatSubMarcaVehiculos("DODG",351,"RAM LARAMIE");
            dataHelper.insertCatSubMarcaVehiculos("DODG",352,"RAM SRT 10");
            dataHelper.insertCatSubMarcaVehiculos("DODG",353,"RAM SLT");
            dataHelper.insertCatSubMarcaVehiculos("DODG",354,"VISION");
            dataHelper.insertCatSubMarcaVehiculos("DODG",355,"VIPER");
            dataHelper.insertCatSubMarcaVehiculos("DODG",356,"VERNA");
            dataHelper.insertCatSubMarcaVehiculos("DODG",357,"VAN 1000");
            dataHelper.insertCatSubMarcaVehiculos("DODG",358,"STRATUS");
            dataHelper.insertCatSubMarcaVehiculos("DODG",359,"RAM WAGON");
            dataHelper.insertCatSubMarcaVehiculos("DODG",360,"RAM VAN");
            dataHelper.insertCatSubMarcaVehiculos("EMBA",363,"1990 RIVA FERRARI 32");
            dataHelper.insertCatSubMarcaVehiculos("EMBA",364,"ABEKING & RASMUSSEN YACHTS");
            dataHelper.insertCatSubMarcaVehiculos("EMBA",365,"ADVANTAGE BOATS BAJA BAYLINER");
            dataHelper.insertCatSubMarcaVehiculos("EMBA",366,"ASTON MARTIN AM37");
            dataHelper.insertCatSubMarcaVehiculos("EMBA",367,"ASTONDOA");
            dataHelper.insertCatSubMarcaVehiculos("EMBA",368,"ASTONDOA 66 GLX");
            dataHelper.insertCatSubMarcaVehiculos("EMBA",369,"BAYLINER ELEMENT");
            dataHelper.insertCatSubMarcaVehiculos("EMBA",370,"BENETTI");
            dataHelper.insertCatSubMarcaVehiculos("EMBA",371,"BENNINGTON BRYANT CARAVELLE");
            dataHelper.insertCatSubMarcaVehiculos("EMBA",372,"BLACK MARLIN LUXURY");
            dataHelper.insertCatSubMarcaVehiculos("EMBA",373,"BUGATTI NINIETTE 66");
            dataHelper.insertCatSubMarcaVehiculos("EMBA",374,"BURGESS");
            dataHelper.insertCatSubMarcaVehiculos("EMBA",375,"CATAMARÁN LAGOON 39");
            dataHelper.insertCatSubMarcaVehiculos("EMBA",376,"CHAPARRAL CHRIS-CRAFT COBALT");
            dataHelper.insertCatSubMarcaVehiculos("EMBA",377,"CHAPARRAL H2O 18 SKI & FISH");
            dataHelper.insertCatSubMarcaVehiculos("EMBA",378,"CHRIS-CRAFT CARINA 21");
            dataHelper.insertCatSubMarcaVehiculos("EMBA",379,"CIGARRETTE RACING TIRRANNA 59 AMG EDITION");
            dataHelper.insertCatSubMarcaVehiculos("EMBA",380,"COBALT 200");
            dataHelper.insertCatSubMarcaVehiculos("EMBA",381,"CODECASA");
            dataHelper.insertCatSubMarcaVehiculos("EMBA",382,"CORBETA MALIBU");
            dataHelper.insertCatSubMarcaVehiculos("EMBA",383,"CREST CRESTLINER CROWNLINE");
            dataHelper.insertCatSubMarcaVehiculos("EMBA",384,"CRUISERS CYPRESS CAY ELIMINATOR");
            dataHelper.insertCatSubMarcaVehiculos("EMBA",385,"DE ANTONIO D34");
            dataHelper.insertCatSubMarcaVehiculos("EMBA",386,"ESSEX BOATS FORMULA FOUR WINNS");
            dataHelper.insertCatSubMarcaVehiculos("EMBA",387,"FEADSHIP");
            dataHelper.insertCatSubMarcaVehiculos("EMBA",388,"FERRETTI GROUP");
            dataHelper.insertCatSubMarcaVehiculos("EMBA",389,"FORMULA 270 BOWRIDER");
            dataHelper.insertCatSubMarcaVehiculos("EMBA",390,"FRASER");
            dataHelper.insertCatSubMarcaVehiculos("EMBA",391,"GLASTRON GODFREY HALLETT BOATS");
            dataHelper.insertCatSubMarcaVehiculos("EMBA",392,"HARRIS FLOTEBOTE LARSON LOWE");
            dataHelper.insertCatSubMarcaVehiculos("EMBA",393,"KAWASAKI SX-R");
            dataHelper.insertCatSubMarcaVehiculos("EMBA",394,"KAWASAKI ULTRA 300X");
            dataHelper.insertCatSubMarcaVehiculos("EMBA",395,"LEXUS LY 650");
            dataHelper.insertCatSubMarcaVehiculos("EMBA",396,"LÜRSEN");
            dataHelper.insertCatSubMarcaVehiculos("EMBA",397,"MANSORY BLACK MARLIN 550");
            dataHelper.insertCatSubMarcaVehiculos("EMBA",398,"MARINE ARROW 460 GRANTURISMO");
            dataHelper.insertCatSubMarcaVehiculos("EMBA",399,"MONTEREY NORDIC PLAYCRAFT");
            dataHelper.insertCatSubMarcaVehiculos("EMBA",400,"MTI LAMBORGHINI BOAT");
            dataHelper.insertCatSubMarcaVehiculos("EMBA",401,"NARKE ELECTROJET");
            dataHelper.insertCatSubMarcaVehiculos("EMBA",402,"OCEANCO");
            dataHelper.insertCatSubMarcaVehiculos("EMBA",403,"PERFORMANCE BOATS");
            dataHelper.insertCatSubMarcaVehiculos("EMBA",404,"PERSHING");
            dataHelper.insertCatSubMarcaVehiculos("EMBA",405,"PORSCHE DESIGN GROUP FEARLESS 28");
            dataHelper.insertCatSubMarcaVehiculos("EMBA",406,"PREMIER PRINCECRAFT REGAL");
            dataHelper.insertCatSubMarcaVehiculos("EMBA",407,"QUADROFOIL Q2S ELECTRIC");
            dataHelper.insertCatSubMarcaVehiculos("EMBA",408,"QUER 36 XL");
            dataHelper.insertCatSubMarcaVehiculos("EMBA",409,"QUER 43 XL");
            dataHelper.insertCatSubMarcaVehiculos("EMBA",410,"RINKER SEA RAY SHOCKWAVE");
            dataHelper.insertCatSubMarcaVehiculos("EMBA",411,"RODMAN 1250");
            dataHelper.insertCatSubMarcaVehiculos("EMBA",412,"SACS STRIDER 13 GRAN COUPÉ LANCIA DI LANCIA");
            dataHelper.insertCatSubMarcaVehiculos("EMBA",413,"SEA RAY 205 SPORT");
            dataHelper.insertCatSubMarcaVehiculos("EMBA",414,"SEA-DOO GTX LIMITED 300");
            dataHelper.insertCatSubMarcaVehiculos("EMBA",415,"SEA-DOO RXP-X 260");
            dataHelper.insertCatSubMarcaVehiculos("EMBA",416,"SEA-DOO RXP-X 300.");
            dataHelper.insertCatSubMarcaVehiculos("EMBA",417,"SEA-DOO SPARK TRIXX");
            dataHelper.insertCatSubMarcaVehiculos("EMBA",418,"SMOKER CRAFT STARCRAFT STINGRAY");
            dataHelper.insertCatSubMarcaVehiculos("EMBA",419,"STRAND CRAFT V8");
            dataHelper.insertCatSubMarcaVehiculos("EMBA",420,"SUN TRACKER SUNSATION SYLVAN");
            dataHelper.insertCatSubMarcaVehiculos("EMBA",421,"SUNSEEKER");
            dataHelper.insertCatSubMarcaVehiculos("EMBA",422,"TAHOE");
            dataHelper.insertCatSubMarcaVehiculos("EMBA",423,"VELERO BAVARIA CRUISER 46");
            dataHelper.insertCatSubMarcaVehiculos("EMBA",424,"VELERO BENETEAU OCEANIS 38");
            dataHelper.insertCatSubMarcaVehiculos("EMBA",425,"VELERO DUFOUR 560 GRAN LARGE");
            dataHelper.insertCatSubMarcaVehiculos("EMBA",426,"VELERO ELAN 350");
            dataHelper.insertCatSubMarcaVehiculos("EMBA",427,"VELERO HANSE 508");
            dataHelper.insertCatSubMarcaVehiculos("EMBA",428,"VELERO JEANNEAU SUN ODYSSEY 50 DS");
            dataHelper.insertCatSubMarcaVehiculos("EMBA",429,"YAMAHA EXR");
            dataHelper.insertCatSubMarcaVehiculos("EMBA",430,"YAMAHA FX CRUISER SHO");
            dataHelper.insertCatSubMarcaVehiculos("EMBA",431,"YAMAHA SUPERJET");
            dataHelper.insertCatSubMarcaVehiculos("EMBA",432,"YAMAHA WAVERUNNER EX SPORT");
            dataHelper.insertCatSubMarcaVehiculos("FAW",433,"GF1500");
            dataHelper.insertCatSubMarcaVehiculos("FAW",434,"GF5000/BUS");
            dataHelper.insertCatSubMarcaVehiculos("FAW",435,"MAMUT V80");
            dataHelper.insertCatSubMarcaVehiculos("FAW",436,"OLEY");
            dataHelper.insertCatSubMarcaVehiculos("FAW",437,"GF 60 MINIVAN");
            dataHelper.insertCatSubMarcaVehiculos("FAW",438,"GF 5000 MAXIDIESEL");
            dataHelper.insertCatSubMarcaVehiculos("FAW",439,"GF 1500 MAXITRUCK");
            dataHelper.insertCatSubMarcaVehiculos("FAW",440,"BESTURN X80");
            dataHelper.insertCatSubMarcaVehiculos("FAW",441,"GF7000");
            dataHelper.insertCatSubMarcaVehiculos("FAW",442,"GF8 ECOVAN");
            dataHelper.insertCatSubMarcaVehiculos("FAW",443,"GF900 MINITRUCK");
            dataHelper.insertCatSubMarcaVehiculos("FAW",444,"MAMUT T80");
            dataHelper.insertCatSubMarcaVehiculos("FAW",445,"GF250");
            dataHelper.insertCatSubMarcaVehiculos("FAW",446,"GF6000");
            dataHelper.insertCatSubMarcaVehiculos("FERR",447,"458 ITALIA");
            dataHelper.insertCatSubMarcaVehiculos("FERR",448,"599 GTB FIORANO");
            dataHelper.insertCatSubMarcaVehiculos("FERR",449,"CALIFORNIA");
            dataHelper.insertCatSubMarcaVehiculos("FERR",450,"FF");
            dataHelper.insertCatSubMarcaVehiculos("FERR",451,"LAFERRARI");
            dataHelper.insertCatSubMarcaVehiculos("FERR",452,"488 GTB");
            dataHelper.insertCatSubMarcaVehiculos("FERR",453,"F12 BERLINETTA");
            dataHelper.insertCatSubMarcaVehiculos("FERR",454,"550M MARANELLO");
            dataHelper.insertCatSubMarcaVehiculos("FERR",455,"550");
            dataHelper.insertCatSubMarcaVehiculos("FERR",456,"CALIFORNIA T");
            dataHelper.insertCatSubMarcaVehiculos("FERR",457,"612 SCAGLIETTI");
            dataHelper.insertCatSubMarcaVehiculos("FERR",458,"599");
            dataHelper.insertCatSubMarcaVehiculos("FERR",459,"575M MARANELLO");
            dataHelper.insertCatSubMarcaVehiculos("FERR",460,"308");
            dataHelper.insertCatSubMarcaVehiculos("FERR",461,"360");
            dataHelper.insertCatSubMarcaVehiculos("FERR",462,"456M");
            dataHelper.insertCatSubMarcaVehiculos("FERR",463,"488 SPIDER");
            dataHelper.insertCatSubMarcaVehiculos("FERR",464,"ENZO");
            dataHelper.insertCatSubMarcaVehiculos("FERR",465,"F12 TDF");
            dataHelper.insertCatSubMarcaVehiculos("FERR",466,"F430");
            dataHelper.insertCatSubMarcaVehiculos("FERR",467,"F355");
            dataHelper.insertCatSubMarcaVehiculos("FERR",468,"LA FERRARI APERTA");
            dataHelper.insertCatSubMarcaVehiculos("FERR",469,"LA FERRARI");
            dataHelper.insertCatSubMarcaVehiculos("FERR",470,"GTC4 LUSSO T");
            dataHelper.insertCatSubMarcaVehiculos("FERR",471,"GTC4 LUSSO");
            dataHelper.insertCatSubMarcaVehiculos("FIAT",472,"PALIO ADVENTURE");
            dataHelper.insertCatSubMarcaVehiculos("FIAT",473,"UNO");
            dataHelper.insertCatSubMarcaVehiculos("FIAT",474,"500 ABARTH");
            dataHelper.insertCatSubMarcaVehiculos("FIAT",475,"DUCATO");
            dataHelper.insertCatSubMarcaVehiculos("FIAT",476,"500L");
            dataHelper.insertCatSubMarcaVehiculos("FIAT",477,"MOBI");
            dataHelper.insertCatSubMarcaVehiculos("FIAT",478,"STRADA");
            dataHelper.insertCatSubMarcaVehiculos("FIAT",479,"PUNTO");
            dataHelper.insertCatSubMarcaVehiculos("FIAT",480,"STILO");
            dataHelper.insertCatSubMarcaVehiculos("FIAT",481,"ABARTH");
            dataHelper.insertCatSubMarcaVehiculos("FIAT",482,"500 X");
            dataHelper.insertCatSubMarcaVehiculos("FIAT",483,"500 L");
            dataHelper.insertCatSubMarcaVehiculos("FIAT",484,"500");
            dataHelper.insertCatSubMarcaVehiculos("FIAT",485,"PALIO");
            dataHelper.insertCatSubMarcaVehiculos("FIAT",486,"PALIO SPORTING");
            dataHelper.insertCatSubMarcaVehiculos("FIAT",487,"PANDA");
            dataHelper.insertCatSubMarcaVehiculos("FIAT",488,"DUCATO CARGO VAN");
            dataHelper.insertCatSubMarcaVehiculos("FORD",489,"FIGO");
            dataHelper.insertCatSubMarcaVehiculos("FORD",490,"FIESTA");
            dataHelper.insertCatSubMarcaVehiculos("FORD",491,"FUSION");
            dataHelper.insertCatSubMarcaVehiculos("FORD",492,"ECOSPORT");
            dataHelper.insertCatSubMarcaVehiculos("FORD",493,"ESCAPE");
            dataHelper.insertCatSubMarcaVehiculos("FORD",494,"EDGE");
            dataHelper.insertCatSubMarcaVehiculos("FORD",495,"EXPLORER");
            dataHelper.insertCatSubMarcaVehiculos("FORD",496,"EXPEDITIO");
            dataHelper.insertCatSubMarcaVehiculos("FORD",497,"F-SERIES");
            dataHelper.insertCatSubMarcaVehiculos("FORD",498,"LOBO");
            dataHelper.insertCatSubMarcaVehiculos("FORD",499,"MUSTANG");
            dataHelper.insertCatSubMarcaVehiculos("FORD",500,"RANGER");
            dataHelper.insertCatSubMarcaVehiculos("FORD",501,"TRANSIT");
            dataHelper.insertCatSubMarcaVehiculos("FORD",502,"F-600");
            dataHelper.insertCatSubMarcaVehiculos("FORD",503,"WINDSTAR");
            dataHelper.insertCatSubMarcaVehiculos("FORD",504,"CONTOUR");
            dataHelper.insertCatSubMarcaVehiculos("FORD",505,"CLUB WAGON");
            dataHelper.insertCatSubMarcaVehiculos("FORD",506,"ECONOLINE");
            dataHelper.insertCatSubMarcaVehiculos("FORD",507,"CROWN VICTORIA");
            dataHelper.insertCatSubMarcaVehiculos("FORD",508,"COURIER");
            dataHelper.insertCatSubMarcaVehiculos("FORD",509,"COUGAR");
            dataHelper.insertCatSubMarcaVehiculos("FORD",510,"AEROSTAR");
            dataHelper.insertCatSubMarcaVehiculos("FORD",511,"AEROVAN");
            dataHelper.insertCatSubMarcaVehiculos("FORD",512,"BRONCO");
            dataHelper.insertCatSubMarcaVehiculos("FORD",513,"CAMION");
            dataHelper.insertCatSubMarcaVehiculos("FORD",514,"ESCORT");
            dataHelper.insertCatSubMarcaVehiculos("FORD",515,"EXCURSION");
            dataHelper.insertCatSubMarcaVehiculos("FORD",516,"EXPEDITION");
            dataHelper.insertCatSubMarcaVehiculos("FORD",517,"EXPLORER SPORT");
            dataHelper.insertCatSubMarcaVehiculos("FORD",518,"EXPLORER SPORT TRAC");
            dataHelper.insertCatSubMarcaVehiculos("FORD",519,"F-450");
            dataHelper.insertCatSubMarcaVehiculos("FORD",520,"F-350");
            dataHelper.insertCatSubMarcaVehiculos("FORD",521,"F-250");
            dataHelper.insertCatSubMarcaVehiculos("FORD",522,"F-150");
            dataHelper.insertCatSubMarcaVehiculos("FORD",523,"F-100");
            dataHelper.insertCatSubMarcaVehiculos("FORD",524,"FIVE HUNDRED");
            dataHelper.insertCatSubMarcaVehiculos("FORD",525,"FOCUS");
            dataHelper.insertCatSubMarcaVehiculos("FORD",526,"FOCUS RS");
            dataHelper.insertCatSubMarcaVehiculos("FORD",527,"FOCUS ST");
            dataHelper.insertCatSubMarcaVehiculos("FORD",528,"FREESTAR");
            dataHelper.insertCatSubMarcaVehiculos("FORD",529,"GALAXIE");
            dataHelper.insertCatSubMarcaVehiculos("FORD",530,"GHIA");
            dataHelper.insertCatSubMarcaVehiculos("FORD",531,"FIGO HATCHBACK");
            dataHelper.insertCatSubMarcaVehiculos("FORD",532,"FIGO SEDAN");
            dataHelper.insertCatSubMarcaVehiculos("FORD",533,"FIESTA ST");
            dataHelper.insertCatSubMarcaVehiculos("FORD",534,"FIESTA IKON");
            dataHelper.insertCatSubMarcaVehiculos("FORD",535,"FALCON");
            dataHelper.insertCatSubMarcaVehiculos("FORD",536,"FAIRMONT");
            dataHelper.insertCatSubMarcaVehiculos("FORD",537,"F-550");
            dataHelper.insertCatSubMarcaVehiculos("FORD",538,"VOLTEO");
            dataHelper.insertCatSubMarcaVehiculos("FORD",539,"TRANSIT GASOLINA");
            dataHelper.insertCatSubMarcaVehiculos("FORD",540,"TRANSIT CUSTOM");
            dataHelper.insertCatSubMarcaVehiculos("FORD",541,"TOPAZ");
            dataHelper.insertCatSubMarcaVehiculos("FORD",542,"THUNDERBIRD");
            dataHelper.insertCatSubMarcaVehiculos("FORD",543,"TAURUS");
            dataHelper.insertCatSubMarcaVehiculos("FORD",544,"SUPER DUTY CHASIS");
            dataHelper.insertCatSubMarcaVehiculos("FORD",545,"SABLE");
            dataHelper.insertCatSubMarcaVehiculos("FORD",546,"PROBE");
            dataHelper.insertCatSubMarcaVehiculos("FORD",547,"PICK UP");
            dataHelper.insertCatSubMarcaVehiculos("FORD",548,"MYSTIQUE");
            dataHelper.insertCatSubMarcaVehiculos("FORD",549,"MONDEO");
            dataHelper.insertCatSubMarcaVehiculos("FORD",550,"MICROBUS");
            dataHelper.insertCatSubMarcaVehiculos("FORD",551,"MAVERICK");
            dataHelper.insertCatSubMarcaVehiculos("FORD",552,"LTD");
            dataHelper.insertCatSubMarcaVehiculos("FORD",553,"KA");
            dataHelper.insertCatSubMarcaVehiculos("FORD",554,"IKON");
            dataHelper.insertCatSubMarcaVehiculos("FORD",555,"GT");
            dataHelper.insertCatSubMarcaVehiculos("FORD",556,"GRAND MARQUIS");
            dataHelper.insertCatSubMarcaVehiculos("FREI",557,"M2");
            dataHelper.insertCatSubMarcaVehiculos("FREI",558,"FL360");
            dataHelper.insertCatSubMarcaVehiculos("FREI",559,"COLUMBIA");
            dataHelper.insertCatSubMarcaVehiculos("FREI",560,"CASCADIA");
            dataHelper.insertCatSubMarcaVehiculos("FREI",561,"SE114");
            dataHelper.insertCatSubMarcaVehiculos("FREI",562,"TRACTO CAMION");
            dataHelper.insertCatSubMarcaVehiculos("GMC",563,"YUKON");
            dataHelper.insertCatSubMarcaVehiculos("GMC",564,"SIERRA");
            dataHelper.insertCatSubMarcaVehiculos("GMC",565,"ACADIA");
            dataHelper.insertCatSubMarcaVehiculos("GMC",566,"TERRAIN");
            dataHelper.insertCatSubMarcaVehiculos("GMC",567,"SIN ESPECIFICAR");
            dataHelper.insertCatSubMarcaVehiculos("GMC",568,"SAFFARI");
            dataHelper.insertCatSubMarcaVehiculos("GMC",569,"PICK UP");
            dataHelper.insertCatSubMarcaVehiculos("GMC",570,"SAVANA");
            dataHelper.insertCatSubMarcaVehiculos("GMC",571,"SIERRA ALL TERRAIN");
            dataHelper.insertCatSubMarcaVehiculos("GMC",572,"SIERRA DENALI");
            dataHelper.insertCatSubMarcaVehiculos("GMC",573,"ACADIA DENALI");
            dataHelper.insertCatSubMarcaVehiculos("GMC",574,"CANYON");
            dataHelper.insertCatSubMarcaVehiculos("GMC",575,"JIMMY");
            dataHelper.insertCatSubMarcaVehiculos("GMC",576,"YUKON DENALI");
            dataHelper.insertCatSubMarcaVehiculos("GMC",577,"TERRAIN DENALI");
            dataHelper.insertCatSubMarcaVehiculos("GMC",578,"SIERRA REGULAR");
            dataHelper.insertCatSubMarcaVehiculos("GOKA",579,"SHIFTER");
            dataHelper.insertCatSubMarcaVehiculos("GOKA",580,"OTROS MODELOS");
            dataHelper.insertCatSubMarcaVehiculos("HOND",581,"FIT");
            dataHelper.insertCatSubMarcaVehiculos("HOND",582,"CITY");
            dataHelper.insertCatSubMarcaVehiculos("HOND",583,"CIVIC");
            dataHelper.insertCatSubMarcaVehiculos("HOND",584,"ACCORD");
            dataHelper.insertCatSubMarcaVehiculos("HOND",585,"HR-V");
            dataHelper.insertCatSubMarcaVehiculos("HOND",586,"CR-V");
            dataHelper.insertCatSubMarcaVehiculos("HOND",587,"PILOT");
            dataHelper.insertCatSubMarcaVehiculos("HOND",588,"ODYSSEY");
            dataHelper.insertCatSubMarcaVehiculos("HOND",589,"BR-V");
            dataHelper.insertCatSubMarcaVehiculos("HOND",590,"CIVIC COUPE");
            dataHelper.insertCatSubMarcaVehiculos("HOND",591,"MOTOCICLETA");
            dataHelper.insertCatSubMarcaVehiculos("HOND",592,"ELEMENT");
            dataHelper.insertCatSubMarcaVehiculos("HOND",593,"PASSPORT");
            dataHelper.insertCatSubMarcaVehiculos("HOND",594,"RIDGELINE");
            dataHelper.insertCatSubMarcaVehiculos("HUMM",595,"H2");
            dataHelper.insertCatSubMarcaVehiculos("HUMM",596,"H3");
            dataHelper.insertCatSubMarcaVehiculos("HUMM",597,"H2 SUT");
            dataHelper.insertCatSubMarcaVehiculos("HUMM",598,"H1");
            dataHelper.insertCatSubMarcaVehiculos("HUMM",599,"HX");
            dataHelper.insertCatSubMarcaVehiculos("HYUN",600,"GRAND I10");
            dataHelper.insertCatSubMarcaVehiculos("HYUN",601,"ELANTRA");
            dataHelper.insertCatSubMarcaVehiculos("HYUN",602,"ACCENT");
            dataHelper.insertCatSubMarcaVehiculos("HYUN",603,"TUCSON");
            dataHelper.insertCatSubMarcaVehiculos("HYUN",604,"SANTA FE");
            dataHelper.insertCatSubMarcaVehiculos("HYUN",605,"CRETA");
            dataHelper.insertCatSubMarcaVehiculos("HYUN",606,"IONIQ");
            dataHelper.insertCatSubMarcaVehiculos("HYUN",607,"STAREX");
            dataHelper.insertCatSubMarcaVehiculos("HYUN",608,"ALL NEW TUCSON");
            dataHelper.insertCatSubMarcaVehiculos("HYUN",609,"GRAND I10 SEDAN");
            dataHelper.insertCatSubMarcaVehiculos("HYUN",610,"GRAND I10 HATCHBACK");
            dataHelper.insertCatSubMarcaVehiculos("HYUN",611,"SONATA");
            dataHelper.insertCatSubMarcaVehiculos("INFI",612,"Q50");
            dataHelper.insertCatSubMarcaVehiculos("INFI",613,"Q60");
            dataHelper.insertCatSubMarcaVehiculos("INFI",614,"Q70");
            dataHelper.insertCatSubMarcaVehiculos("INFI",615,"QX60");
            dataHelper.insertCatSubMarcaVehiculos("INFI",616,"QX70");
            dataHelper.insertCatSubMarcaVehiculos("INFI",617,"QX80");
            dataHelper.insertCatSubMarcaVehiculos("INFI",618,"QX30");
            dataHelper.insertCatSubMarcaVehiculos("INFI",619,"Q 45");
            dataHelper.insertCatSubMarcaVehiculos("INFI",620,"J30");
            dataHelper.insertCatSubMarcaVehiculos("INFI",621,"Q");
            dataHelper.insertCatSubMarcaVehiculos("INFI",622,"I35");
            dataHelper.insertCatSubMarcaVehiculos("INFI",623,"I30");
            dataHelper.insertCatSubMarcaVehiculos("INTE",624,"TRANSTAR");
            dataHelper.insertCatSubMarcaVehiculos("INTE",625,"TRAVELER");
            dataHelper.insertCatSubMarcaVehiculos("INTE",626,"WORKSTAR");
            dataHelper.insertCatSubMarcaVehiculos("INTE",627,"3000 RE");
            dataHelper.insertCatSubMarcaVehiculos("INTE",628,"3100 FE/SCD");
            dataHelper.insertCatSubMarcaVehiculos("INTE",629,"4700 FE");
            dataHelper.insertCatSubMarcaVehiculos("INTE",630,"3300 CE");
            dataHelper.insertCatSubMarcaVehiculos("INTE",631,"CITYSTAR");
            dataHelper.insertCatSubMarcaVehiculos("INTE",632,"4700 SCD");
            dataHelper.insertCatSubMarcaVehiculos("INTE",633,"DURASTAR");
            dataHelper.insertCatSubMarcaVehiculos("INTE",634,"F 9200");
            dataHelper.insertCatSubMarcaVehiculos("INTE",635,"PROSTAR");
            dataHelper.insertCatSubMarcaVehiculos("INTE",636,"FAMSA");
            dataHelper.insertCatSubMarcaVehiculos("ISUZ",637,"FORWARD 1100");
            dataHelper.insertCatSubMarcaVehiculos("ISUZ",638,"FORWARD 800");
            dataHelper.insertCatSubMarcaVehiculos("ISUZ",639,"ELF 600 BUS");
            dataHelper.insertCatSubMarcaVehiculos("ISUZ",640,"ELF 600");
            dataHelper.insertCatSubMarcaVehiculos("ISUZ",641,"ELF 400");
            dataHelper.insertCatSubMarcaVehiculos("ISUZ",642,"ELF 500");
            dataHelper.insertCatSubMarcaVehiculos("ISUZ",643,"ELF 300");
            dataHelper.insertCatSubMarcaVehiculos("ISUZ",644,"ELF 200");
            dataHelper.insertCatSubMarcaVehiculos("ISUZ",645,"ELF 100");
            dataHelper.insertCatSubMarcaVehiculos("ISUZ",646,"TROOPER");
            dataHelper.insertCatSubMarcaVehiculos("ISUZ",647,"RODEO");
            dataHelper.insertCatSubMarcaVehiculos("ISUZ",648,"PICKUP");
            dataHelper.insertCatSubMarcaVehiculos("JAC",649,"SEI 2");
            dataHelper.insertCatSubMarcaVehiculos("JAC",650,"SEI 3");
            dataHelper.insertCatSubMarcaVehiculos("JAC",651,"SEI 4");
            dataHelper.insertCatSubMarcaVehiculos("JAC",652,"SEI 7");
            dataHelper.insertCatSubMarcaVehiculos("JAC",653,"J4");
            dataHelper.insertCatSubMarcaVehiculos("JAC",654,"T6");
            dataHelper.insertCatSubMarcaVehiculos("JAC",655,"T8");
            dataHelper.insertCatSubMarcaVehiculos("JAC",656,"ESEI 2");
            dataHelper.insertCatSubMarcaVehiculos("JAC",657,"ESEI 3");
            dataHelper.insertCatSubMarcaVehiculos("JAC",658,"ESEI 4");
            dataHelper.insertCatSubMarcaVehiculos("JAC",659,"ESEI 7");
            dataHelper.insertCatSubMarcaVehiculos("JAC",660,"EJ4");
            dataHelper.insertCatSubMarcaVehiculos("JAC",661,"ET6");
            dataHelper.insertCatSubMarcaVehiculos("JAC",662,"ET8");
            dataHelper.insertCatSubMarcaVehiculos("JAGU",663,"XE");
            dataHelper.insertCatSubMarcaVehiculos("JAGU",664,"XF");
            dataHelper.insertCatSubMarcaVehiculos("JAGU",665,"F-TYPE");
            dataHelper.insertCatSubMarcaVehiculos("JAGU",666,"XJ");
            dataHelper.insertCatSubMarcaVehiculos("JAGU",667,"XK");
            dataHelper.insertCatSubMarcaVehiculos("JAGU",668,"F-PACE");
            dataHelper.insertCatSubMarcaVehiculos("JAGU",669,"XKR");
            dataHelper.insertCatSubMarcaVehiculos("JAGU",670,"X-TYPE");
            dataHelper.insertCatSubMarcaVehiculos("JAGU",671,"S-TYPE");
            dataHelper.insertCatSubMarcaVehiculos("JAGU",672,"I-PACE");
            dataHelper.insertCatSubMarcaVehiculos("JAGU",673,"XJR");
            dataHelper.insertCatSubMarcaVehiculos("JAGU",674,"XK8");
            dataHelper.insertCatSubMarcaVehiculos("JEEP",675,"RENEGADE");
            dataHelper.insertCatSubMarcaVehiculos("JEEP",676,"COMPASS");
            dataHelper.insertCatSubMarcaVehiculos("JEEP",677,"WRANGLER");
            dataHelper.insertCatSubMarcaVehiculos("JEEP",678,"CHEROKEE");
            dataHelper.insertCatSubMarcaVehiculos("JEEP",679,"GRAND CHEROKEE");
            dataHelper.insertCatSubMarcaVehiculos("JEEP",680,"JEEP GLADIATOR (SJ)");
            dataHelper.insertCatSubMarcaVehiculos("JEEP",681,"PATRIOT");
            dataHelper.insertCatSubMarcaVehiculos("JEEP",682,"RENEGADO");
            dataHelper.insertCatSubMarcaVehiculos("JEEP",683,"LIBERTY");
            dataHelper.insertCatSubMarcaVehiculos("JEEP",684,"GRAND WAGONEER");
            dataHelper.insertCatSubMarcaVehiculos("JEEP",685,"COMMANDER");
            dataHelper.insertCatSubMarcaVehiculos("JEEP",686,"COMANCHE US");
            dataHelper.insertCatSubMarcaVehiculos("JEEP",687,"CJ7");
            dataHelper.insertCatSubMarcaVehiculos("JEEP",688,"WAGONEER");
            dataHelper.insertCatSubMarcaVehiculos("JEEP",689,"WRANGLER UNLIMTED");
            dataHelper.insertCatSubMarcaVehiculos("KENW",690,"KW45/55");
            dataHelper.insertCatSubMarcaVehiculos("KENW",691,"T300");
            dataHelper.insertCatSubMarcaVehiculos("KENW",692,"T460");
            dataHelper.insertCatSubMarcaVehiculos("KENW",693,"T370");
            dataHelper.insertCatSubMarcaVehiculos("KENW",694,"T660");
            dataHelper.insertCatSubMarcaVehiculos("KENW",695,"T600 B");
            dataHelper.insertCatSubMarcaVehiculos("KENW",696,"T680");
            dataHelper.insertCatSubMarcaVehiculos("KENW",697,"T800");
            dataHelper.insertCatSubMarcaVehiculos("KENW",698,"TRACTO");
            dataHelper.insertCatSubMarcaVehiculos("KENW",699,"T880 VOC");
            dataHelper.insertCatSubMarcaVehiculos("KIA",700,"FORTE");
            dataHelper.insertCatSubMarcaVehiculos("KIA",701,"SPORTAGE");
            dataHelper.insertCatSubMarcaVehiculos("KIA",702,"SORENTO");
            dataHelper.insertCatSubMarcaVehiculos("KIA",703,"OPTIMA");
            dataHelper.insertCatSubMarcaVehiculos("KIA",704,"RIO");
            dataHelper.insertCatSubMarcaVehiculos("KIA",705,"SOUL");
            dataHelper.insertCatSubMarcaVehiculos("KIA",706,"NIRO");
            dataHelper.insertCatSubMarcaVehiculos("KIA",707,"SEDONA");
            dataHelper.insertCatSubMarcaVehiculos("KIA",708,"STINGER");
            dataHelper.insertCatSubMarcaVehiculos("KIA",709,"SELTOS");
            dataHelper.insertCatSubMarcaVehiculos("KIA",710,"RIO HATCHBACK");
            dataHelper.insertCatSubMarcaVehiculos("KIA",711,"RIO SEDAN");
            dataHelper.insertCatSubMarcaVehiculos("KIA",712,"FORTE HATCHBACK");
            dataHelper.insertCatSubMarcaVehiculos("LAMB",713,"AD PERSONAM");
            dataHelper.insertCatSubMarcaVehiculos("LAMB",714,"AVENTADOR");
            dataHelper.insertCatSubMarcaVehiculos("LAMB",715,"GALLARDO");
            dataHelper.insertCatSubMarcaVehiculos("LAMB",716,"CONCEPT");
            dataHelper.insertCatSubMarcaVehiculos("LAMB",717,"MURCIELAGO");
            dataHelper.insertCatSubMarcaVehiculos("LAMB",718,"HURACAN");
            dataHelper.insertCatSubMarcaVehiculos("LAMB",719,"ONE OFF");
            dataHelper.insertCatSubMarcaVehiculos("LAND",720,"LR4 DISCOVERY");
            dataHelper.insertCatSubMarcaVehiculos("LAND",721,"DISCOVERY");
            dataHelper.insertCatSubMarcaVehiculos("LAND",722,"DEFENDER");
            dataHelper.insertCatSubMarcaVehiculos("LAND",723,"RANGE ROVER");
            dataHelper.insertCatSubMarcaVehiculos("LAND",724,"RANGE ROVER SPORT");
            dataHelper.insertCatSubMarcaVehiculos("LAND",725,"RANGE ROVER EVOQUE");
            dataHelper.insertCatSubMarcaVehiculos("LAND",726,"75");
            dataHelper.insertCatSubMarcaVehiculos("LAND",727,"DEFENDER 90");
            dataHelper.insertCatSubMarcaVehiculos("LAND",728,"DISCOVERY SPORT");
            dataHelper.insertCatSubMarcaVehiculos("LAND",729,"LR2");
            dataHelper.insertCatSubMarcaVehiculos("LAND",730,"LR3");
            dataHelper.insertCatSubMarcaVehiculos("LAND",731,"FREELANDER");
            dataHelper.insertCatSubMarcaVehiculos("LAND",732,"L3");
            dataHelper.insertCatSubMarcaVehiculos("LAND",733,"RANGE ROVER EVOQUE CABRIOLET");
            dataHelper.insertCatSubMarcaVehiculos("LEXU",734,"LFA");
            dataHelper.insertCatSubMarcaVehiculos("LEXU",735,"IS 300H");
            dataHelper.insertCatSubMarcaVehiculos("LEXU",736,"LS");
            dataHelper.insertCatSubMarcaVehiculos("LEXU",737,"LS 600H");
            dataHelper.insertCatSubMarcaVehiculos("LEXU",738,"LS 600HL");
            dataHelper.insertCatSubMarcaVehiculos("LEXU",739,"RC F");
            dataHelper.insertCatSubMarcaVehiculos("LEXU",740,"RX");
            dataHelper.insertCatSubMarcaVehiculos("LEXU",741,"RX 450H");
            dataHelper.insertCatSubMarcaVehiculos("LEXU",742,"RC 300H");
            dataHelper.insertCatSubMarcaVehiculos("LEXU",743,"RC");
            dataHelper.insertCatSubMarcaVehiculos("LEXU",744,"NX");
            dataHelper.insertCatSubMarcaVehiculos("LEXU",745,"NS 300H");
            dataHelper.insertCatSubMarcaVehiculos("LEXU",746,"GS");
            dataHelper.insertCatSubMarcaVehiculos("LEXU",747,"F SPORT");
            dataHelper.insertCatSubMarcaVehiculos("LEXU",748,"IS");
            dataHelper.insertCatSubMarcaVehiculos("LEXU",749,"GS F");
            dataHelper.insertCatSubMarcaVehiculos("LEXU",750,"GS 450H");
            dataHelper.insertCatSubMarcaVehiculos("LEXU",751,"GS 300H");
            dataHelper.insertCatSubMarcaVehiculos("LEXU",752,"F");
            dataHelper.insertCatSubMarcaVehiculos("LEXU",753,"ES");
            dataHelper.insertCatSubMarcaVehiculos("LEXU",754,"CT 200H");
            dataHelper.insertCatSubMarcaVehiculos("LEXU",755,"CT");
            dataHelper.insertCatSubMarcaVehiculos("LINC",756,"MKC");
            dataHelper.insertCatSubMarcaVehiculos("LINC",757,"MKZ");
            dataHelper.insertCatSubMarcaVehiculos("LINC",758,"NAUTILUS");
            dataHelper.insertCatSubMarcaVehiculos("LINC",759,"CONTINENTAL");
            dataHelper.insertCatSubMarcaVehiculos("LINC",760,"NAVIGATOR");
            dataHelper.insertCatSubMarcaVehiculos("LINC",761,"AVIATOR");
            dataHelper.insertCatSubMarcaVehiculos("LINC",762,"BLACKWOOD");
            dataHelper.insertCatSubMarcaVehiculos("LINC",763,"CONTINENTAL");
            dataHelper.insertCatSubMarcaVehiculos("LINC",764,"LS");
            dataHelper.insertCatSubMarcaVehiculos("LINC",765,"MKC");
            dataHelper.insertCatSubMarcaVehiculos("LINC",766,"MKX");
            dataHelper.insertCatSubMarcaVehiculos("LINC",767,"MKZ");
            dataHelper.insertCatSubMarcaVehiculos("LINC",768,"NAVIGATOR");
            dataHelper.insertCatSubMarcaVehiculos("LINC",769,"MARK LT");
            dataHelper.insertCatSubMarcaVehiculos("LINC",770,"MARK VIII");
            dataHelper.insertCatSubMarcaVehiculos("LINC",771,"ZEPHYR MKZ");
            dataHelper.insertCatSubMarcaVehiculos("LINC",772,"TOWN CAR");
            dataHelper.insertCatSubMarcaVehiculos("LINC",773,"ZEPHYR");
            dataHelper.insertCatSubMarcaVehiculos("LOTU",774,"ELISE");
            dataHelper.insertCatSubMarcaVehiculos("LOTU",775,"EXIGE");
            dataHelper.insertCatSubMarcaVehiculos("LOTU",776,"EVORA");
            dataHelper.insertCatSubMarcaVehiculos("LOTU",777,"EVORA GT4");
            dataHelper.insertCatSubMarcaVehiculos("LOTU",778,"EVORA 400 HETHEL");
            dataHelper.insertCatSubMarcaVehiculos("LOTU",779,"EVORA SPORT 410");
            dataHelper.insertCatSubMarcaVehiculos("LOTU",780,"EXIGE 350 SPECIAL EDITION");
            dataHelper.insertCatSubMarcaVehiculos("LOTU",781,"EXIGE SPORT 350");
            dataHelper.insertCatSubMarcaVehiculos("LOTU",782,"EXIGE SPORT 380");
            dataHelper.insertCatSubMarcaVehiculos("LOTU",783,"EXIGE V6 CUP R");
            dataHelper.insertCatSubMarcaVehiculos("LOTU",784,"ELISE RACE 250");
            dataHelper.insertCatSubMarcaVehiculos("LOTU",785,"ELISE CUP 250");
            dataHelper.insertCatSubMarcaVehiculos("LOTU",786,"EVORA 400");
            dataHelper.insertCatSubMarcaVehiculos("LOTU",787,"ELISE SPORT 220");
            dataHelper.insertCatSubMarcaVehiculos("LOTU",788,"ELISE SPORT");
            dataHelper.insertCatSubMarcaVehiculos("LOTU",789,"ELISE 250 SPECIAL EDITION");
            dataHelper.insertCatSubMarcaVehiculos("LOTU",790,"3-ELEVEN ROAD & RACE");
            dataHelper.insertCatSubMarcaVehiculos("LOTU",791,"3-ELEVEN");
            dataHelper.insertCatSubMarcaVehiculos("LOTU",792,"SIN ESPECIFICAR");
            dataHelper.insertCatSubMarcaVehiculos("MAQU",793,"CLARK");
            dataHelper.insertCatSubMarcaVehiculos("MAQU",794,"EXCAVADORA");
            dataHelper.insertCatSubMarcaVehiculos("MAQU",795,"GRUA");
            dataHelper.insertCatSubMarcaVehiculos("MAQU",796,"MONTACARGAS");
            dataHelper.insertCatSubMarcaVehiculos("MAQU",797,"TRACTOR");
            dataHelper.insertCatSubMarcaVehiculos("MAQU",798,"MOTOCONFORMADORA");
            dataHelper.insertCatSubMarcaVehiculos("MAQU",799,"RETROEXCAVADORA");
            dataHelper.insertCatSubMarcaVehiculos("MASE",800,"QUATTROPORTE");
            dataHelper.insertCatSubMarcaVehiculos("MASE",801,"GHIBLI");
            dataHelper.insertCatSubMarcaVehiculos("MASE",802,"GRANCABRIO");
            dataHelper.insertCatSubMarcaVehiculos("MASE",803,"GHIBLI S Q4");
            dataHelper.insertCatSubMarcaVehiculos("MASE",804,"GRANCABRIO MC");
            dataHelper.insertCatSubMarcaVehiculos("MASE",805,"GRANCABRIO SPORT");
            dataHelper.insertCatSubMarcaVehiculos("MASE",806,"GRANTURISMO");
            dataHelper.insertCatSubMarcaVehiculos("MASE",807,"GRANTURISMO MC STRADALE");
            dataHelper.insertCatSubMarcaVehiculos("MASE",808,"COUPE");
            dataHelper.insertCatSubMarcaVehiculos("MASE",809,"4200");
            dataHelper.insertCatSubMarcaVehiculos("MASE",810,"3500 GT");
            dataHelper.insertCatSubMarcaVehiculos("MASE",811,"SPYDER");
            dataHelper.insertCatSubMarcaVehiculos("MASE",812,"QUATTROPORTE S Q4");
            dataHelper.insertCatSubMarcaVehiculos("MASE",813,"QUATTROPORTE GTS");
            dataHelper.insertCatSubMarcaVehiculos("MASE",814,"LEVANTE S");
            dataHelper.insertCatSubMarcaVehiculos("MASE",815,"GRANTURISMO SOPORT");
            dataHelper.insertCatSubMarcaVehiculos("MASE",816,"LEVANTE");
            dataHelper.insertCatSubMarcaVehiculos("MASS",817,"COSECHADORAS");
            dataHelper.insertCatSubMarcaVehiculos("MASS",818,"EQUIPO CAÑERO");
            dataHelper.insertCatSubMarcaVehiculos("MASS",819,"EQUIPO FORRAJERO");
            dataHelper.insertCatSubMarcaVehiculos("MASS",820,"IMPLEMENTOS");
            dataHelper.insertCatSubMarcaVehiculos("MASS",821,"PULVERIZADORAS");
            dataHelper.insertCatSubMarcaVehiculos("MASS",822,"TRACTORES");
            dataHelper.insertCatSubMarcaVehiculos("MAZD",823,"MAZDA 2");
            dataHelper.insertCatSubMarcaVehiculos("MAZD",824,"MAZDA 3");
            dataHelper.insertCatSubMarcaVehiculos("MAZD",825,"MAZDA 6");
            dataHelper.insertCatSubMarcaVehiculos("MAZD",826,"CX-3");
            dataHelper.insertCatSubMarcaVehiculos("MAZD",827,"CX-5");
            dataHelper.insertCatSubMarcaVehiculos("MAZD",828,"CX-9");
            dataHelper.insertCatSubMarcaVehiculos("MAZD",829,"CX-30");
            dataHelper.insertCatSubMarcaVehiculos("MAZD",830,"MX-5");
            dataHelper.insertCatSubMarcaVehiculos("MAZD",831,"MX-5 RF");
            dataHelper.insertCatSubMarcaVehiculos("MAZD",832,"PICKUP");
            dataHelper.insertCatSubMarcaVehiculos("MAZD",833,"SPEED 6");
            dataHelper.insertCatSubMarcaVehiculos("MAZD",834,"RX8");
            dataHelper.insertCatSubMarcaVehiculos("MCLR",835,"675LT");
            dataHelper.insertCatSubMarcaVehiculos("MCLR",836,"650S");
            dataHelper.insertCatSubMarcaVehiculos("MCLR",837,"570S");
            dataHelper.insertCatSubMarcaVehiculos("MERB",838,"300 D");
            dataHelper.insertCatSubMarcaVehiculos("MERB",839,"AMG GT");
            dataHelper.insertCatSubMarcaVehiculos("MERB",840,"AMG GT COUPE");
            dataHelper.insertCatSubMarcaVehiculos("MERB",841,"AUTOBUS");
            dataHelper.insertCatSubMarcaVehiculos("MERB",842,"CLA");
            dataHelper.insertCatSubMarcaVehiculos("MERB",843,"CLASE A");
            dataHelper.insertCatSubMarcaVehiculos("MERB",844,"CLASE A HATCHBACK");
            dataHelper.insertCatSubMarcaVehiculos("MERB",845,"CLASE B");
            dataHelper.insertCatSubMarcaVehiculos("MERB",846,"CLASE B SPORTS TOURER");
            dataHelper.insertCatSubMarcaVehiculos("MERB",847,"CLASE C");
            dataHelper.insertCatSubMarcaVehiculos("MERB",848,"CLASE C CONVERTIBLE");
            dataHelper.insertCatSubMarcaVehiculos("MERB",849,"CLASE C COUPE");
            dataHelper.insertCatSubMarcaVehiculos("MERB",850,"CLASE C SEDAN");
            dataHelper.insertCatSubMarcaVehiculos("MERB",851,"CLASE CL");
            dataHelper.insertCatSubMarcaVehiculos("MERB",852,"CLASE CLA");
            dataHelper.insertCatSubMarcaVehiculos("MERB",853,"CLASE CLA COUPE");
            dataHelper.insertCatSubMarcaVehiculos("MERB",854,"CLASE CLK");
            dataHelper.insertCatSubMarcaVehiculos("MERB",855,"CLASE CLS");
            dataHelper.insertCatSubMarcaVehiculos("MERB",856,"CLASE CLS COUPE");
            dataHelper.insertCatSubMarcaVehiculos("MERB",857,"CLASE E");
            dataHelper.insertCatSubMarcaVehiculos("MERB",858,"CLASE E CONVERTIBLE");
            dataHelper.insertCatSubMarcaVehiculos("MERB",859,"CLASE E COUPE");
            dataHelper.insertCatSubMarcaVehiculos("MERB",860,"CLASE E SEDAN");
            dataHelper.insertCatSubMarcaVehiculos("MERB",861,"CLASE G");
            dataHelper.insertCatSubMarcaVehiculos("MERB",862,"CLASE G TODO TERRENO");
            dataHelper.insertCatSubMarcaVehiculos("MERB",863,"CLASE GL");
            dataHelper.insertCatSubMarcaVehiculos("MERB",864,"CLASE GLA");
            dataHelper.insertCatSubMarcaVehiculos("MERB",865,"CLASE GLA TODO TERRENO");
            dataHelper.insertCatSubMarcaVehiculos("MERB",866,"CLASE GLC");
            dataHelper.insertCatSubMarcaVehiculos("MERB",867,"CLASE GLC COUPE");
            dataHelper.insertCatSubMarcaVehiculos("MERB",868,"CLASE GLC SUV");
            dataHelper.insertCatSubMarcaVehiculos("MERB",869,"CLASE GLE");
            dataHelper.insertCatSubMarcaVehiculos("MERB",870,"CLASE GLE COUPE");
            dataHelper.insertCatSubMarcaVehiculos("MERB",871,"CLASE GLE SUV");
            dataHelper.insertCatSubMarcaVehiculos("MERB",872,"CLASE GLK");
            dataHelper.insertCatSubMarcaVehiculos("MERB",873,"CLASE GLS");
            dataHelper.insertCatSubMarcaVehiculos("MERB",874,"CLASE GLS SUV");
            dataHelper.insertCatSubMarcaVehiculos("MERB",875,"CLASE GLS TODO TERRENO");
            dataHelper.insertCatSubMarcaVehiculos("MERB",876,"CLASE M");
            dataHelper.insertCatSubMarcaVehiculos("MERB",877,"CLASE R");
            dataHelper.insertCatSubMarcaVehiculos("MERB",878,"CLASE S");
            dataHelper.insertCatSubMarcaVehiculos("MERB",879,"CLASE S COUPE");
            dataHelper.insertCatSubMarcaVehiculos("MERB",880,"CLASE S SEDAN");
            dataHelper.insertCatSubMarcaVehiculos("MERB",881,"CLASE SEL");
            dataHelper.insertCatSubMarcaVehiculos("MERB",882,"CLASE SL");
            dataHelper.insertCatSubMarcaVehiculos("MERB",883,"CLASE SL ROADSTER");
            dataHelper.insertCatSubMarcaVehiculos("MERB",884,"CLASE SLC");
            dataHelper.insertCatSubMarcaVehiculos("MERB",885,"CLASE SLC ROADSTER");
            dataHelper.insertCatSubMarcaVehiculos("MERB",886,"CLASE SLK");
            dataHelper.insertCatSubMarcaVehiculos("MERB",887,"CLASE SLR");
            dataHelper.insertCatSubMarcaVehiculos("MERB",888,"MARCO POLO");
            dataHelper.insertCatSubMarcaVehiculos("MERB",889,"MAYBACH");
            dataHelper.insertCatSubMarcaVehiculos("MERB",890,"SPRINTER");
            dataHelper.insertCatSubMarcaVehiculos("MERB",891,"TRACTO");
            dataHelper.insertCatSubMarcaVehiculos("MERB",892,"V");
            dataHelper.insertCatSubMarcaVehiculos("MERB",893,"V AVANTGARDE");
            dataHelper.insertCatSubMarcaVehiculos("MERB",894,"V230");
            dataHelper.insertCatSubMarcaVehiculos("MERB",895,"VIANO");
            dataHelper.insertCatSubMarcaVehiculos("MERB",896,"VITO");
            dataHelper.insertCatSubMarcaVehiculos("MERC",897,"MYSTIQUE");
            dataHelper.insertCatSubMarcaVehiculos("MERC",898,"MOUNTAINEER");
            dataHelper.insertCatSubMarcaVehiculos("MERC",899,"VILLAGER");
            dataHelper.insertCatSubMarcaVehiculos("MERC",900,"MONTEGO");
            dataHelper.insertCatSubMarcaVehiculos("MERC",901,"MILAN");
            dataHelper.insertCatSubMarcaVehiculos("MERC",902,"MARINER");
            dataHelper.insertCatSubMarcaVehiculos("MERC",903,"COUGAR");
            dataHelper.insertCatSubMarcaVehiculos("MG",904,"MG 5");
            dataHelper.insertCatSubMarcaVehiculos("MG",905,"MG HS");
            dataHelper.insertCatSubMarcaVehiculos("MG",906,"MG ZS");
            dataHelper.insertCatSubMarcaVehiculos("MG",907,"MG 3");
            dataHelper.insertCatSubMarcaVehiculos("MG",908,"MG 350");
            dataHelper.insertCatSubMarcaVehiculos("MG",909,"MG GS");
            dataHelper.insertCatSubMarcaVehiculos("MG",910,"ZR");
            dataHelper.insertCatSubMarcaVehiculos("MG",911,"ZT");
            dataHelper.insertCatSubMarcaVehiculos("MG",912,"MG GT");
            dataHelper.insertCatSubMarcaVehiculos("MG",913,"TF");
            dataHelper.insertCatSubMarcaVehiculos("MINI",914,"3-PUERTAS");
            dataHelper.insertCatSubMarcaVehiculos("MINI",915,"5-PUERTAS");
            dataHelper.insertCatSubMarcaVehiculos("MINI",916,"CABRIO");
            dataHelper.insertCatSubMarcaVehiculos("MINI",917,"CLUBMAN");
            dataHelper.insertCatSubMarcaVehiculos("MINI",918,"CONVERTIBLE");
            dataHelper.insertCatSubMarcaVehiculos("MINI",919,"COOPER");
            dataHelper.insertCatSubMarcaVehiculos("MINI",920,"COUNTRYMAN");
            dataHelper.insertCatSubMarcaVehiculos("MINI",921,"COUPE");
            dataHelper.insertCatSubMarcaVehiculos("MINI",922,"HARDTOP");
            dataHelper.insertCatSubMarcaVehiculos("MINI",923,"JOHN COOPER WORKS");
            dataHelper.insertCatSubMarcaVehiculos("MINI",924,"PACEMAN");
            dataHelper.insertCatSubMarcaVehiculos("MINI",925,"ROADSTER");
            dataHelper.insertCatSubMarcaVehiculos("MITS",926,"3000GT");
            dataHelper.insertCatSubMarcaVehiculos("MITS",927,"ECLIPSE");
            dataHelper.insertCatSubMarcaVehiculos("MITS",928,"ECLIPSE CROSS");
            dataHelper.insertCatSubMarcaVehiculos("MITS",929,"ENDEAVOR");
            dataHelper.insertCatSubMarcaVehiculos("MITS",930,"GALANT");
            dataHelper.insertCatSubMarcaVehiculos("MITS",931,"GRANDIS");
            dataHelper.insertCatSubMarcaVehiculos("MITS",932,"L200");
            dataHelper.insertCatSubMarcaVehiculos("MITS",933,"LANCER");
            dataHelper.insertCatSubMarcaVehiculos("MITS",934,"MIRAGE");
            dataHelper.insertCatSubMarcaVehiculos("MITS",935,"MIRAGE/MIRAGE G4");
            dataHelper.insertCatSubMarcaVehiculos("MITS",936,"MONTERO");
            dataHelper.insertCatSubMarcaVehiculos("MITS",937,"MONTERO LIMITED");
            dataHelper.insertCatSubMarcaVehiculos("MITS",938,"MONTERO SPORT");
            dataHelper.insertCatSubMarcaVehiculos("MITS",939,"OUTLANDER");
            dataHelper.insertCatSubMarcaVehiculos("MITS",940,"SPACE STAR");
            dataHelper.insertCatSubMarcaVehiculos("MOTO",1035,"AZEL");
            dataHelper.insertCatSubMarcaVehiculos("MOTO",1036,"APRILIA");
            dataHelper.insertCatSubMarcaVehiculos("MOTO",1037,"BARRUCHI");
            dataHelper.insertCatSubMarcaVehiculos("MOTO",1038,"BAJAJ");
            dataHelper.insertCatSubMarcaVehiculos("MOTO",1039,"BETA");
            dataHelper.insertCatSubMarcaVehiculos("MOTO",1040,"BENELLI");
            dataHelper.insertCatSubMarcaVehiculos("MOTO",1041,"ALPINA RENANIA");
            dataHelper.insertCatSubMarcaVehiculos("MOTO",1042,"AIYUMO");
            dataHelper.insertCatSubMarcaVehiculos("MOTO",1043,"ADLY");
            dataHelper.insertCatSubMarcaVehiculos("MOTO",1044,"ACUATICA");
            dataHelper.insertCatSubMarcaVehiculos("MOTO",1045,"BMW");
            dataHelper.insertCatSubMarcaVehiculos("MOTO",1046,"BRP");
            dataHelper.insertCatSubMarcaVehiculos("MOTO",1047,"BOMBARDIER");
            dataHelper.insertCatSubMarcaVehiculos("MOTO",1048,"BLATA");
            dataHelper.insertCatSubMarcaVehiculos("MOTO",1049,"BIMOTA");
            dataHelper.insertCatSubMarcaVehiculos("MOTO",1050,"COOLTRA");
            dataHelper.insertCatSubMarcaVehiculos("MOTO",1051,"CPI");
            dataHelper.insertCatSubMarcaVehiculos("MOTO",1052,"DAELMI");
            dataHelper.insertCatSubMarcaVehiculos("MOTO",1053,"CUATRIMOTO");
            dataHelper.insertCatSubMarcaVehiculos("MOTO",1054,"CSR");
            dataHelper.insertCatSubMarcaVehiculos("MOTO",1055,"CLIPIC");
            dataHelper.insertCatSubMarcaVehiculos("MOTO",1056,"CAGIVA");
            dataHelper.insertCatSubMarcaVehiculos("MOTO",1057,"BULTACO");
            dataHelper.insertCatSubMarcaVehiculos("MOTO",1058,"BUELL");
            dataHelper.insertCatSubMarcaVehiculos("MOTO",1059,"HAMMEL");
            dataHelper.insertCatSubMarcaVehiculos("MOTO",1060,"HARLEY DAVIDSON");
            dataHelper.insertCatSubMarcaVehiculos("MOTO",1061,"HARTFORD");
            dataHelper.insertCatSubMarcaVehiculos("MOTO",1062,"HM");
            dataHelper.insertCatSubMarcaVehiculos("MOTO",1063,"HONDA");
            dataHelper.insertCatSubMarcaVehiculos("MOTO",1064,"HUATIAN");
            dataHelper.insertCatSubMarcaVehiculos("MOTO",1065,"HUSABERG");
            dataHelper.insertCatSubMarcaVehiculos("MOTO",1066,"HUSQVARNA");
            dataHelper.insertCatSubMarcaVehiculos("MOTO",1067,"GILERA");
            dataHelper.insertCatSubMarcaVehiculos("MOTO",1068,"GOES");
            dataHelper.insertCatSubMarcaVehiculos("MOTO",1069,"GENERIC");
            dataHelper.insertCatSubMarcaVehiculos("MOTO",1070,"GAS GAS");
            dataHelper.insertCatSubMarcaVehiculos("MOTO",1071,"DUCATI");
            dataHelper.insertCatSubMarcaVehiculos("MOTO",1072,"DINAMO");
            dataHelper.insertCatSubMarcaVehiculos("MOTO",1073,"DH HAOTIAN");
            dataHelper.insertCatSubMarcaVehiculos("MOTO",1074,"DERBI");
            dataHelper.insertCatSubMarcaVehiculos("MOTO",1075,"LINHAI");
            dataHelper.insertCatSubMarcaVehiculos("MOTO",1076,"LML");
            dataHelper.insertCatSubMarcaVehiculos("MOTO",1077,"LUCKY");
            dataHelper.insertCatSubMarcaVehiculos("MOTO",1078,"MACBOR");
            dataHelper.insertCatSubMarcaVehiculos("MOTO",1079,"MALAGUTI");
            dataHelper.insertCatSubMarcaVehiculos("MOTO",1080,"MAXMOTO");
            dataHelper.insertCatSubMarcaVehiculos("MOTO",1081,"MBK");
            dataHelper.insertCatSubMarcaVehiculos("MOTO",1082,"MEKO");
            dataHelper.insertCatSubMarcaVehiculos("MOTO",1083,"LIFAN");
            dataHelper.insertCatSubMarcaVehiculos("MOTO",1084,"LEONART");
            dataHelper.insertCatSubMarcaVehiculos("MOTO",1085,"KYMCO");
            dataHelper.insertCatSubMarcaVehiculos("MOTO",1086,"KURAZAI");
            dataHelper.insertCatSubMarcaVehiculos("MOTO",1087,"KTM");
            dataHelper.insertCatSubMarcaVehiculos("MOTO",1088,"KENROD");
            dataHelper.insertCatSubMarcaVehiculos("MOTO",1089,"KEEWAY");
            dataHelper.insertCatSubMarcaVehiculos("MOTO",1090,"KAWASAKI");
            dataHelper.insertCatSubMarcaVehiculos("MOTO",1091,"JINCHENG");
            dataHelper.insertCatSubMarcaVehiculos("MOTO",1092,"ITALJET");
            dataHelper.insertCatSubMarcaVehiculos("MOTO",1093,"ITALIKA");
            dataHelper.insertCatSubMarcaVehiculos("MOTO",1094,"ISLO");
            dataHelper.insertCatSubMarcaVehiculos("MOTO",1095,"I-MOTO");
            dataHelper.insertCatSubMarcaVehiculos("MOTO",1096,"HYOSUNG");
            dataHelper.insertCatSubMarcaVehiculos("MOTO",1097,"SIN ESPECIFICAR");
            dataHelper.insertCatSubMarcaVehiculos("MOTO",1098,"MOTO GUZZI");
            dataHelper.insertCatSubMarcaVehiculos("MOTO",1099,"MOTOCICLETA");
            dataHelper.insertCatSubMarcaVehiculos("MOTO",1100,"MOTORHISPANIA");
            dataHelper.insertCatSubMarcaVehiculos("MOTO",1101,"MOTONETA");
            dataHelper.insertCatSubMarcaVehiculos("MOTO",1102,"ORANGE COUNTY CHOPPER");
            dataHelper.insertCatSubMarcaVehiculos("MOTO",1103,"MZ");
            dataHelper.insertCatSubMarcaVehiculos("MOTO",1104,"MX");
            dataHelper.insertCatSubMarcaVehiculos("MOTO",1105,"MV AGUSTA");
            dataHelper.insertCatSubMarcaVehiculos("MOTO",1106,"RIEJU");
            dataHelper.insertCatSubMarcaVehiculos("MOTO",1107,"SACHS");
            dataHelper.insertCatSubMarcaVehiculos("MOTO",1108,"SCORPA");
            dataHelper.insertCatSubMarcaVehiculos("MOTO",1109,"SHERCO");
            dataHelper.insertCatSubMarcaVehiculos("MOTO",1110,"POLINI");
            dataHelper.insertCatSubMarcaVehiculos("MOTO",1111,"POLARIS");
            dataHelper.insertCatSubMarcaVehiculos("MOTO",1112,"PIAGGIO");
            dataHelper.insertCatSubMarcaVehiculos("MOTO",1113,"PEUGEOT");
            dataHelper.insertCatSubMarcaVehiculos("MOTO",1114,"SUMCO");
            dataHelper.insertCatSubMarcaVehiculos("MOTO",1115,"SUZUKI");
            dataHelper.insertCatSubMarcaVehiculos("MOTO",1116,"SYM");
            dataHelper.insertCatSubMarcaVehiculos("MOTO",1117,"TANK");
            dataHelper.insertCatSubMarcaVehiculos("MOTO",1118,"TGB");
            dataHelper.insertCatSubMarcaVehiculos("MOTO",1119,"TM");
            dataHelper.insertCatSubMarcaVehiculos("MOTO",1120,"TRIUMPH");
            dataHelper.insertCatSubMarcaVehiculos("MOTO",1121,"VENTO");
            dataHelper.insertCatSubMarcaVehiculos("MOTO",1122,"VESPA");
            dataHelper.insertCatSubMarcaVehiculos("MOTO",1123,"VICTORY");
            dataHelper.insertCatSubMarcaVehiculos("MOTO",1124,"VOR");
            dataHelper.insertCatSubMarcaVehiculos("MOTO",1125,"VOXAN");
            dataHelper.insertCatSubMarcaVehiculos("MOTO",1126,"VR");
            dataHelper.insertCatSubMarcaVehiculos("MOTO",1127,"YAMAHA");
            dataHelper.insertCatSubMarcaVehiculos("MOTO",1128,"YING YANG");
            dataHelper.insertCatSubMarcaVehiculos("MOTO",1129,"CARABELA");
            dataHelper.insertCatSubMarcaVehiculos("MTNT",941,"CARABELA");
            dataHelper.insertCatSubMarcaVehiculos("MTNT",942,"YING YANG");
            dataHelper.insertCatSubMarcaVehiculos("MTNT",943,"YAMAHA");
            dataHelper.insertCatSubMarcaVehiculos("MTNT",944,"VR");
            dataHelper.insertCatSubMarcaVehiculos("MTNT",945,"VOXAN");
            dataHelper.insertCatSubMarcaVehiculos("MTNT",946,"VOR");
            dataHelper.insertCatSubMarcaVehiculos("MTNT",947,"VICTORY");
            dataHelper.insertCatSubMarcaVehiculos("MTNT",948,"VESPA");
            dataHelper.insertCatSubMarcaVehiculos("MTNT",949,"VENTO");
            dataHelper.insertCatSubMarcaVehiculos("MTNT",950,"TRIUMPH");
            dataHelper.insertCatSubMarcaVehiculos("MTNT",951,"TM");
            dataHelper.insertCatSubMarcaVehiculos("MTNT",952,"TGB");
            dataHelper.insertCatSubMarcaVehiculos("MTNT",953,"TANK");
            dataHelper.insertCatSubMarcaVehiculos("MTNT",954,"SYM");
            dataHelper.insertCatSubMarcaVehiculos("MTNT",955,"SUZUKI");
            dataHelper.insertCatSubMarcaVehiculos("MTNT",956,"PIAGGIO");
            dataHelper.insertCatSubMarcaVehiculos("MTNT",957,"POLARIS");
            dataHelper.insertCatSubMarcaVehiculos("MTNT",958,"POLINI");
            dataHelper.insertCatSubMarcaVehiculos("MTNT",959,"RIEJU");
            dataHelper.insertCatSubMarcaVehiculos("MTNT",960,"SUMCO");
            dataHelper.insertCatSubMarcaVehiculos("MTNT",961,"SHERCO");
            dataHelper.insertCatSubMarcaVehiculos("MTNT",962,"SCORPA");
            dataHelper.insertCatSubMarcaVehiculos("MTNT",963,"SACHS");
            dataHelper.insertCatSubMarcaVehiculos("MTNT",964,"MX");
            dataHelper.insertCatSubMarcaVehiculos("MTNT",965,"MZ");
            dataHelper.insertCatSubMarcaVehiculos("MTNT",966,"ORANGE COUNTY CHOPPER");
            dataHelper.insertCatSubMarcaVehiculos("MTNT",967,"PEUGEOT");
            dataHelper.insertCatSubMarcaVehiculos("MTNT",968,"MOTORHISPANIA");
            dataHelper.insertCatSubMarcaVehiculos("MTNT",969,"MV AGUSTA");
            dataHelper.insertCatSubMarcaVehiculos("MTNT",970,"MOTONETA");
            dataHelper.insertCatSubMarcaVehiculos("MTNT",971,"MOTOCICLETA");
            dataHelper.insertCatSubMarcaVehiculos("MTNT",972,"SIN ESPECIFICAR");
            dataHelper.insertCatSubMarcaVehiculos("MTNT",973,"I-MOTO");
            dataHelper.insertCatSubMarcaVehiculos("MTNT",974,"ISLO");
            dataHelper.insertCatSubMarcaVehiculos("MTNT",975,"ITALIKA");
            dataHelper.insertCatSubMarcaVehiculos("MTNT",976,"ITALJET");
            dataHelper.insertCatSubMarcaVehiculos("MTNT",977,"JINCHENG");
            dataHelper.insertCatSubMarcaVehiculos("MTNT",978,"KAWASAKI");
            dataHelper.insertCatSubMarcaVehiculos("MTNT",979,"KEEWAY");
            dataHelper.insertCatSubMarcaVehiculos("MTNT",980,"KENROD");
            dataHelper.insertCatSubMarcaVehiculos("MTNT",981,"KTM");
            dataHelper.insertCatSubMarcaVehiculos("MTNT",982,"KURAZAI");
            dataHelper.insertCatSubMarcaVehiculos("MTNT",983,"KYMCO");
            dataHelper.insertCatSubMarcaVehiculos("MTNT",984,"LEONART");
            dataHelper.insertCatSubMarcaVehiculos("MTNT",985,"LIFAN");
            dataHelper.insertCatSubMarcaVehiculos("MTNT",986,"LINHAI");
            dataHelper.insertCatSubMarcaVehiculos("MTNT",987,"MOTO GUZZI");
            dataHelper.insertCatSubMarcaVehiculos("MTNT",988,"MEKO");
            dataHelper.insertCatSubMarcaVehiculos("MTNT",989,"MBK");
            dataHelper.insertCatSubMarcaVehiculos("MTNT",990,"MAXMOTO");
            dataHelper.insertCatSubMarcaVehiculos("MTNT",991,"MALAGUTI");
            dataHelper.insertCatSubMarcaVehiculos("MTNT",992,"MACBOR");
            dataHelper.insertCatSubMarcaVehiculos("MTNT",993,"LUCKY");
            dataHelper.insertCatSubMarcaVehiculos("MTNT",994,"LML");
            dataHelper.insertCatSubMarcaVehiculos("MTNT",995,"DH HAOTIAN");
            dataHelper.insertCatSubMarcaVehiculos("MTNT",996,"DINAMO");
            dataHelper.insertCatSubMarcaVehiculos("MTNT",997,"DUCATI");
            dataHelper.insertCatSubMarcaVehiculos("MTNT",998,"GAS GAS");
            dataHelper.insertCatSubMarcaVehiculos("MTNT",999,"GENERIC");
            dataHelper.insertCatSubMarcaVehiculos("MTNT",1000,"GILERA");
            dataHelper.insertCatSubMarcaVehiculos("MTNT",1001,"HAMMEL");
            dataHelper.insertCatSubMarcaVehiculos("MTNT",1002,"GOES");
            dataHelper.insertCatSubMarcaVehiculos("MTNT",1003,"HYOSUNG");
            dataHelper.insertCatSubMarcaVehiculos("MTNT",1004,"HUSQVARNA");
            dataHelper.insertCatSubMarcaVehiculos("MTNT",1005,"HUSABERG");
            dataHelper.insertCatSubMarcaVehiculos("MTNT",1006,"HUATIAN");
            dataHelper.insertCatSubMarcaVehiculos("MTNT",1007,"HONDA");
            dataHelper.insertCatSubMarcaVehiculos("MTNT",1008,"HM");
            dataHelper.insertCatSubMarcaVehiculos("MTNT",1009,"HARTFORD");
            dataHelper.insertCatSubMarcaVehiculos("MTNT",1010,"HARLEY DAVIDSON");
            dataHelper.insertCatSubMarcaVehiculos("MTNT",1011,"BULTACO");
            dataHelper.insertCatSubMarcaVehiculos("MTNT",1012,"CAGIVA");
            dataHelper.insertCatSubMarcaVehiculos("MTNT",1013,"CLIPIC");
            dataHelper.insertCatSubMarcaVehiculos("MTNT",1014,"COOLTRA");
            dataHelper.insertCatSubMarcaVehiculos("MTNT",1015,"CUATRIMOTO");
            dataHelper.insertCatSubMarcaVehiculos("MTNT",1016,"DAELMI");
            dataHelper.insertCatSubMarcaVehiculos("MTNT",1017,"DERBI");
            dataHelper.insertCatSubMarcaVehiculos("MTNT",1018,"CSR");
            dataHelper.insertCatSubMarcaVehiculos("MTNT",1019,"CPI");
            dataHelper.insertCatSubMarcaVehiculos("MTNT",1020,"BLATA");
            dataHelper.insertCatSubMarcaVehiculos("MTNT",1021,"BMW");
            dataHelper.insertCatSubMarcaVehiculos("MTNT",1022,"BRP");
            dataHelper.insertCatSubMarcaVehiculos("MTNT",1023,"BUELL");
            dataHelper.insertCatSubMarcaVehiculos("MTNT",1024,"BOMBARDIER");
            dataHelper.insertCatSubMarcaVehiculos("MTNT",1025,"ADLY");
            dataHelper.insertCatSubMarcaVehiculos("MTNT",1026,"AIYUMO");
            dataHelper.insertCatSubMarcaVehiculos("MTNT",1027,"ALPINA RENANIA");
            dataHelper.insertCatSubMarcaVehiculos("MTNT",1028,"APRILIA");
            dataHelper.insertCatSubMarcaVehiculos("MTNT",1029,"BETA");
            dataHelper.insertCatSubMarcaVehiculos("MTNT",1030,"BIMOTA");
            dataHelper.insertCatSubMarcaVehiculos("MTNT",1031,"BARRUCHI");
            dataHelper.insertCatSubMarcaVehiculos("MTNT",1032,"BENELLI");
            dataHelper.insertCatSubMarcaVehiculos("MTNT",1033,"AZEL");
            dataHelper.insertCatSubMarcaVehiculos("MTNT",1034,"BAJAJ");
            dataHelper.insertCatSubMarcaVehiculos("NISS",1130,"200 SX");
            dataHelper.insertCatSubMarcaVehiculos("NISS",1131,"240 SX");
            dataHelper.insertCatSubMarcaVehiculos("NISS",1132,"300 ZX");
            dataHelper.insertCatSubMarcaVehiculos("NISS",1133,"350Z");
            dataHelper.insertCatSubMarcaVehiculos("NISS",1134,"370Z");
            dataHelper.insertCatSubMarcaVehiculos("NISS",1135,"ALMERA");
            dataHelper.insertCatSubMarcaVehiculos("NISS",1136,"ALTIMA");
            dataHelper.insertCatSubMarcaVehiculos("NISS",1137,"APRIO");
            dataHelper.insertCatSubMarcaVehiculos("NISS",1138,"ARMADA");
            dataHelper.insertCatSubMarcaVehiculos("NISS",1139,"CABSTAR");
            dataHelper.insertCatSubMarcaVehiculos("NISS",1140,"DATSUN");
            dataHelper.insertCatSubMarcaVehiculos("NISS",1141,"ESTAQUITAS");
            dataHelper.insertCatSubMarcaVehiculos("NISS",1142,"FRONTIER");
            dataHelper.insertCatSubMarcaVehiculos("NISS",1143,"FRONTIER NP300");
            dataHelper.insertCatSubMarcaVehiculos("NISS",1144,"FRONTIER PRO-4X");
            dataHelper.insertCatSubMarcaVehiculos("NISS",1145,"GT-R");
            dataHelper.insertCatSubMarcaVehiculos("NISS",1146,"HIKARY");
            dataHelper.insertCatSubMarcaVehiculos("NISS",1147,"ICHI VAN");
            dataHelper.insertCatSubMarcaVehiculos("NISS",1148,"INFINITY");
            dataHelper.insertCatSubMarcaVehiculos("NISS",1149,"JUKE");
            dataHelper.insertCatSubMarcaVehiculos("NISS",1150,"KICKS");
            dataHelper.insertCatSubMarcaVehiculos("NISS",1151,"KING CAB");
            dataHelper.insertCatSubMarcaVehiculos("NISS",1152,"LEAF");
            dataHelper.insertCatSubMarcaVehiculos("NISS",1153,"LUCINO");
            dataHelper.insertCatSubMarcaVehiculos("NISS",1154,"MARCH");
            dataHelper.insertCatSubMarcaVehiculos("NISS",1155,"MAXIMA");
            dataHelper.insertCatSubMarcaVehiculos("NISS",1156,"MICRA");
            dataHelper.insertCatSubMarcaVehiculos("NISS",1157,"MURANO");
            dataHelper.insertCatSubMarcaVehiculos("NISS",1158,"NOTE");
            dataHelper.insertCatSubMarcaVehiculos("NISS",1159,"NP300");
            dataHelper.insertCatSubMarcaVehiculos("NISS",1160,"NT450");
            dataHelper.insertCatSubMarcaVehiculos("NISS",1161,"NV350");
            dataHelper.insertCatSubMarcaVehiculos("NISS",1162,"NV350 URVAN");
            dataHelper.insertCatSubMarcaVehiculos("NISS",1163,"PATHFINDER");
            dataHelper.insertCatSubMarcaVehiculos("NISS",1164,"PICK-UP");
            dataHelper.insertCatSubMarcaVehiculos("NISS",1165,"PICK-UP DOBLE CABINA");
            dataHelper.insertCatSubMarcaVehiculos("NISS",1166,"PIPA");
            dataHelper.insertCatSubMarcaVehiculos("NISS",1167,"PLATINA");
            dataHelper.insertCatSubMarcaVehiculos("NISS",1168,"QUEST");
            dataHelper.insertCatSubMarcaVehiculos("NISS",1169,"ROGUE");
            dataHelper.insertCatSubMarcaVehiculos("NISS",1170,"SAKURA");
            dataHelper.insertCatSubMarcaVehiculos("NISS",1171,"SAMURAI");
            dataHelper.insertCatSubMarcaVehiculos("NISS",1172,"SENTRA");
            dataHelper.insertCatSubMarcaVehiculos("NISS",1173,"TIIDA");
            dataHelper.insertCatSubMarcaVehiculos("NISS",1174,"TITAN");
            dataHelper.insertCatSubMarcaVehiculos("NISS",1175,"TSUBAME");
            dataHelper.insertCatSubMarcaVehiculos("NISS",1176,"TSURU");
            dataHelper.insertCatSubMarcaVehiculos("NISS",1177,"TSURU GUAYIN");
            dataHelper.insertCatSubMarcaVehiculos("NISS",1178,"TSURU II");
            dataHelper.insertCatSubMarcaVehiculos("NISS",1179,"URVAN");
            dataHelper.insertCatSubMarcaVehiculos("NISS",1180,"VERSA");
            dataHelper.insertCatSubMarcaVehiculos("NISS",1181,"X TERRA");
            dataHelper.insertCatSubMarcaVehiculos("NISS",1182,"X-TRAIL");
            dataHelper.insertCatSubMarcaVehiculos("OPEL",1183,"ADAM");
            dataHelper.insertCatSubMarcaVehiculos("OPEL",1184,"AMPERA");
            dataHelper.insertCatSubMarcaVehiculos("OPEL",1185,"ASTRA");
            dataHelper.insertCatSubMarcaVehiculos("OPEL",1186,"CABRIO");
            dataHelper.insertCatSubMarcaVehiculos("OPEL",1187,"COMBO CARGO");
            dataHelper.insertCatSubMarcaVehiculos("OPEL",1188,"COMBO TOUR");
            dataHelper.insertCatSubMarcaVehiculos("OPEL",1189,"CORSA");
            dataHelper.insertCatSubMarcaVehiculos("OPEL",1190,"CORSA VAN");
            dataHelper.insertCatSubMarcaVehiculos("OPEL",1191,"GTC");
            dataHelper.insertCatSubMarcaVehiculos("OPEL",1192,"INSIGNIA");
            dataHelper.insertCatSubMarcaVehiculos("OPEL",1193,"KARL");
            dataHelper.insertCatSubMarcaVehiculos("OPEL",1194,"MERIVA");
            dataHelper.insertCatSubMarcaVehiculos("OPEL",1195,"MOKKA X");
            dataHelper.insertCatSubMarcaVehiculos("OPEL",1196,"MOVANO");
            dataHelper.insertCatSubMarcaVehiculos("OPEL",1197,"MOVANO COMBI");
            dataHelper.insertCatSubMarcaVehiculos("OPEL",1198,"OPC");
            dataHelper.insertCatSubMarcaVehiculos("OPEL",1199,"VIVARO");
            dataHelper.insertCatSubMarcaVehiculos("OPEL",1200,"VIVARO COMBI");
            dataHelper.insertCatSubMarcaVehiculos("OPEL",1201,"ZAFIRA");
            dataHelper.insertCatSubMarcaVehiculos("PEUG",1202,"2008");
            dataHelper.insertCatSubMarcaVehiculos("PEUG",1203,"2008 CROSSOVER");
            dataHelper.insertCatSubMarcaVehiculos("PEUG",1204,"206");
            dataHelper.insertCatSubMarcaVehiculos("PEUG",1205,"206 SW");
            dataHelper.insertCatSubMarcaVehiculos("PEUG",1206,"207");
            dataHelper.insertCatSubMarcaVehiculos("PEUG",1207,"208");
            dataHelper.insertCatSubMarcaVehiculos("PEUG",1208,"208 GT");
            dataHelper.insertCatSubMarcaVehiculos("PEUG",1209,"3008");
            dataHelper.insertCatSubMarcaVehiculos("PEUG",1210,"3008 CROSSOVER");
            dataHelper.insertCatSubMarcaVehiculos("PEUG",1211,"301");
            dataHelper.insertCatSubMarcaVehiculos("PEUG",1212,"301 SEDAN");
            dataHelper.insertCatSubMarcaVehiculos("PEUG",1213,"306");
            dataHelper.insertCatSubMarcaVehiculos("PEUG",1214,"307");
            dataHelper.insertCatSubMarcaVehiculos("PEUG",1215,"307 SW");
            dataHelper.insertCatSubMarcaVehiculos("PEUG",1216,"308");
            dataHelper.insertCatSubMarcaVehiculos("PEUG",1217,"308 GT");
            dataHelper.insertCatSubMarcaVehiculos("PEUG",1218,"308 HATCHBACK");
            dataHelper.insertCatSubMarcaVehiculos("PEUG",1219,"405");
            dataHelper.insertCatSubMarcaVehiculos("PEUG",1220,"406");
            dataHelper.insertCatSubMarcaVehiculos("PEUG",1221,"407");
            dataHelper.insertCatSubMarcaVehiculos("PEUG",1222,"5008");
            dataHelper.insertCatSubMarcaVehiculos("PEUG",1223,"508 SEDAN");
            dataHelper.insertCatSubMarcaVehiculos("PEUG",1224,"607");
            dataHelper.insertCatSubMarcaVehiculos("PEUG",1225,"GRAND RAID");
            dataHelper.insertCatSubMarcaVehiculos("PEUG",1226,"HATCHBACK");
            dataHelper.insertCatSubMarcaVehiculos("PEUG",1227,"MANAGER");
            dataHelper.insertCatSubMarcaVehiculos("PEUG",1228,"PARTNER");
            dataHelper.insertCatSubMarcaVehiculos("PEUG",1229,"PARTNER FURGONETA");
            dataHelper.insertCatSubMarcaVehiculos("PEUG",1230,"PARTNER MAXI");
            dataHelper.insertCatSubMarcaVehiculos("PEUG",1231,"PARTNER VU");
            dataHelper.insertCatSubMarcaVehiculos("PEUG",1232,"RCZ T");
            dataHelper.insertCatSubMarcaVehiculos("PEUG",1233,"RIFTER");
            dataHelper.insertCatSubMarcaVehiculos("PEUG",1234,"TEPEE");
            dataHelper.insertCatSubMarcaVehiculos("PIAG",1235,"PORTER");
            dataHelper.insertCatSubMarcaVehiculos("PONT",1236,"AZTEK");
            dataHelper.insertCatSubMarcaVehiculos("PONT",1237,"BONNEVILLE");
            dataHelper.insertCatSubMarcaVehiculos("PONT",1238,"FIERO");
            dataHelper.insertCatSubMarcaVehiculos("PONT",1239,"FIREBIRD");
            dataHelper.insertCatSubMarcaVehiculos("PONT",1240,"FIREBIRD TRANS AM");
            dataHelper.insertCatSubMarcaVehiculos("PONT",1241,"G3");
            dataHelper.insertCatSubMarcaVehiculos("PONT",1242,"G4");
            dataHelper.insertCatSubMarcaVehiculos("PONT",1243,"G5");
            dataHelper.insertCatSubMarcaVehiculos("PONT",1244,"G6");
            dataHelper.insertCatSubMarcaVehiculos("PONT",1245,"GRAND AM");
            dataHelper.insertCatSubMarcaVehiculos("PONT",1246,"GRAND PRIX");
            dataHelper.insertCatSubMarcaVehiculos("PONT",1247,"MATIZ");
            dataHelper.insertCatSubMarcaVehiculos("PONT",1248,"MATIZ G2");
            dataHelper.insertCatSubMarcaVehiculos("PONT",1249,"MONTANA SV6");
            dataHelper.insertCatSubMarcaVehiculos("PONT",1250,"SOLSTICE");
            dataHelper.insertCatSubMarcaVehiculos("PONT",1251,"SUNFIRE");
            dataHelper.insertCatSubMarcaVehiculos("PONT",1252,"TORRENT");
            dataHelper.insertCatSubMarcaVehiculos("PONT",1253,"TRANS SPORT");
            dataHelper.insertCatSubMarcaVehiculos("PORS",1254,"718");
            dataHelper.insertCatSubMarcaVehiculos("PORS",1255,"718 BOXSTER");
            dataHelper.insertCatSubMarcaVehiculos("PORS",1256,"718 BOXSTER S");
            dataHelper.insertCatSubMarcaVehiculos("PORS",1257,"718 CAYMAN");
            dataHelper.insertCatSubMarcaVehiculos("PORS",1258,"718 CAYMAN S");
            dataHelper.insertCatSubMarcaVehiculos("PORS",1259,"911");
            dataHelper.insertCatSubMarcaVehiculos("PORS",1260,"911  TARGA 4 GTS");
            dataHelper.insertCatSubMarcaVehiculos("PORS",1261,"911 CARRERA");
            dataHelper.insertCatSubMarcaVehiculos("PORS",1262,"911 CARRERA 4");
            dataHelper.insertCatSubMarcaVehiculos("PORS",1263,"911 CARRERA 4 BLACK EDITION");
            dataHelper.insertCatSubMarcaVehiculos("PORS",1264,"911 CARRERA 4 CABRIOLET");
            dataHelper.insertCatSubMarcaVehiculos("PORS",1265,"911 CARRERA 4 CABRIOLET BLACK EDITION");
            dataHelper.insertCatSubMarcaVehiculos("PORS",1266,"911 CARRERA 4 GTS");
            dataHelper.insertCatSubMarcaVehiculos("PORS",1267,"911 CARRERA 4 GTS CABRIOLET");
            dataHelper.insertCatSubMarcaVehiculos("PORS",1268,"911 CARRERA 4S");
            dataHelper.insertCatSubMarcaVehiculos("PORS",1269,"911 CARRERA 4S CABRIOLET");
            dataHelper.insertCatSubMarcaVehiculos("PORS",1270,"911 CARRERA BLACK EDITION");
            dataHelper.insertCatSubMarcaVehiculos("PORS",1271,"911 CARRERA CABRIOLET");
            dataHelper.insertCatSubMarcaVehiculos("PORS",1272,"911 CARRERA CABRIOLET BLACK EDITION");
            dataHelper.insertCatSubMarcaVehiculos("PORS",1273,"911 CARRERA GTS");
            dataHelper.insertCatSubMarcaVehiculos("PORS",1274,"911 CARRERA S");
            dataHelper.insertCatSubMarcaVehiculos("PORS",1275,"911 CARRERA S CABRIOLET");
            dataHelper.insertCatSubMarcaVehiculos("PORS",1276,"911 CARRETA GTS CABRIOLET");
            dataHelper.insertCatSubMarcaVehiculos("PORS",1277,"911 GT3");
            dataHelper.insertCatSubMarcaVehiculos("PORS",1278,"911 GT3 RS");
            dataHelper.insertCatSubMarcaVehiculos("PORS",1279,"911 R");
            dataHelper.insertCatSubMarcaVehiculos("PORS",1280,"911 TARGA");
            dataHelper.insertCatSubMarcaVehiculos("PORS",1281,"911 TARGA 4");
            dataHelper.insertCatSubMarcaVehiculos("PORS",1282,"911 TARGA 4S");
            dataHelper.insertCatSubMarcaVehiculos("PORS",1283,"911 TURBO");
            dataHelper.insertCatSubMarcaVehiculos("PORS",1284,"911 TURBO CABRIOLET");
            dataHelper.insertCatSubMarcaVehiculos("PORS",1285,"911 TURBO S");
            dataHelper.insertCatSubMarcaVehiculos("PORS",1286,"911 TURBO S CABRIOLET");
            dataHelper.insertCatSubMarcaVehiculos("PORS",1287,"914");
            dataHelper.insertCatSubMarcaVehiculos("PORS",1288,"928");
            dataHelper.insertCatSubMarcaVehiculos("PORS",1289,"BOXSTER");
            dataHelper.insertCatSubMarcaVehiculos("PORS",1290,"CARRERA GT");
            dataHelper.insertCatSubMarcaVehiculos("PORS",1291,"CAYENNE");
            dataHelper.insertCatSubMarcaVehiculos("PORS",1292,"CAYENNE DIESEL");
            dataHelper.insertCatSubMarcaVehiculos("PORS",1293,"CAYENNE DIESEL PLATINUM EDITION");
            dataHelper.insertCatSubMarcaVehiculos("PORS",1294,"CAYENNE GTS");
            dataHelper.insertCatSubMarcaVehiculos("PORS",1295,"CAYENNE PLATINUM EDITION");
            dataHelper.insertCatSubMarcaVehiculos("PORS",1296,"CAYENNE S");
            dataHelper.insertCatSubMarcaVehiculos("PORS",1297,"CAYENNE S E-HYBRID");
            dataHelper.insertCatSubMarcaVehiculos("PORS",1298,"CAYENNE S E-HYBRID PLATINUM EDITION");
            dataHelper.insertCatSubMarcaVehiculos("PORS",1299,"CAYENNE TURBO");
            dataHelper.insertCatSubMarcaVehiculos("PORS",1300,"CAYENNE TURBO S");
            dataHelper.insertCatSubMarcaVehiculos("PORS",1301,"CAYMAN");
            dataHelper.insertCatSubMarcaVehiculos("PORS",1302,"MACAN");
            dataHelper.insertCatSubMarcaVehiculos("PORS",1303,"MACAN CON PAQUETE PERFORMANCE");
            dataHelper.insertCatSubMarcaVehiculos("PORS",1304,"MACAN GTS");
            dataHelper.insertCatSubMarcaVehiculos("PORS",1305,"MACAN S");
            dataHelper.insertCatSubMarcaVehiculos("PORS",1306,"MACAN S DIESEL");
            dataHelper.insertCatSubMarcaVehiculos("PORS",1307,"MACAN TURBO");
            dataHelper.insertCatSubMarcaVehiculos("PORS",1308,"PANAMERA");
            dataHelper.insertCatSubMarcaVehiculos("PORS",1309,"PANAMERA 4");
            dataHelper.insertCatSubMarcaVehiculos("PORS",1310,"PANAMERA 4 EDITION");
            dataHelper.insertCatSubMarcaVehiculos("PORS",1311,"PANAMERA 4 E-HYBRID");
            dataHelper.insertCatSubMarcaVehiculos("PORS",1312,"PANAMERA 4S");
            dataHelper.insertCatSubMarcaVehiculos("PORS",1313,"PANAMERA 4S DIESEL");
            dataHelper.insertCatSubMarcaVehiculos("PORS",1314,"PANAMERA 4S EXECUTIVE");
            dataHelper.insertCatSubMarcaVehiculos("PORS",1315,"PANAMERA DIESEL");
            dataHelper.insertCatSubMarcaVehiculos("PORS",1316,"PANAMERA DIESEL EDITION");
            dataHelper.insertCatSubMarcaVehiculos("PORS",1317,"PANAMERA EDITION");
            dataHelper.insertCatSubMarcaVehiculos("PORS",1318,"PANAMERA EXCLUSIVE SERIES");
            dataHelper.insertCatSubMarcaVehiculos("PORS",1319,"PANAMERA GTS");
            dataHelper.insertCatSubMarcaVehiculos("PORS",1320,"PANAMERA S");
            dataHelper.insertCatSubMarcaVehiculos("PORS",1321,"PANAMERA S E-HYBRID");
            dataHelper.insertCatSubMarcaVehiculos("PORS",1322,"PANAMERA TURBO");
            dataHelper.insertCatSubMarcaVehiculos("PORS",1323,"PANAMERA TURBO EXECUTIVE");
            dataHelper.insertCatSubMarcaVehiculos("PORS",1324,"PANAMERA TURBO S");
            dataHelper.insertCatSubMarcaVehiculos("PORS",1325,"PANAMERA TURBO S EXECUTIVE");
            dataHelper.insertCatSubMarcaVehiculos("RAM",1326,"1500");
            dataHelper.insertCatSubMarcaVehiculos("RAM",1327,"2500");
            dataHelper.insertCatSubMarcaVehiculos("RAM",1328,"4000");
            dataHelper.insertCatSubMarcaVehiculos("RAM",1329,"700");
            dataHelper.insertCatSubMarcaVehiculos("RAM",1330,"PROMASTER");
            dataHelper.insertCatSubMarcaVehiculos("RAM",1331,"PROMASTER RAPID");
            dataHelper.insertCatSubMarcaVehiculos("REMO",1332,"CAJA SECA");
            dataHelper.insertCatSubMarcaVehiculos("REMO",1333,"GONDOLA");
            dataHelper.insertCatSubMarcaVehiculos("REMO",1334,"JAULA GANADERA");
            dataHelper.insertCatSubMarcaVehiculos("REMO",1335,"MADRINA");
            dataHelper.insertCatSubMarcaVehiculos("REMO",1336,"PIPA");
            dataHelper.insertCatSubMarcaVehiculos("REMO",1337,"PLATAFORMA");
            dataHelper.insertCatSubMarcaVehiculos("REMO",1338,"PLATAFORMA DE CAJA BAJA");
            dataHelper.insertCatSubMarcaVehiculos("REMO",1339,"TOLVA");
            dataHelper.insertCatSubMarcaVehiculos("RENA",1340,"ALLIANCE");
            dataHelper.insertCatSubMarcaVehiculos("RENA",1341,"CAPTUR");
            dataHelper.insertCatSubMarcaVehiculos("RENA",1342,"CLIO");
            dataHelper.insertCatSubMarcaVehiculos("RENA",1343,"CLIO R.S.");
            dataHelper.insertCatSubMarcaVehiculos("RENA",1344,"DUSTER");
            dataHelper.insertCatSubMarcaVehiculos("RENA",1345,"ENCORE");
            dataHelper.insertCatSubMarcaVehiculos("RENA",1346,"FLUENCE");
            dataHelper.insertCatSubMarcaVehiculos("RENA",1347,"KANGOO");
            dataHelper.insertCatSubMarcaVehiculos("RENA",1348,"KANGOO EXPRESS");
            dataHelper.insertCatSubMarcaVehiculos("RENA",1349,"KOLEOS");
            dataHelper.insertCatSubMarcaVehiculos("RENA",1350,"KWID");
            dataHelper.insertCatSubMarcaVehiculos("RENA",1351,"LAGUNA");
            dataHelper.insertCatSubMarcaVehiculos("RENA",1352,"LAGUNA II");
            dataHelper.insertCatSubMarcaVehiculos("RENA",1353,"LOGAN");
            dataHelper.insertCatSubMarcaVehiculos("RENA",1354,"MEGANE");
            dataHelper.insertCatSubMarcaVehiculos("RENA",1355,"MEGANE II");
            dataHelper.insertCatSubMarcaVehiculos("RENA",1356,"OROCH");
            dataHelper.insertCatSubMarcaVehiculos("RENA",1357,"R 5 MIRAGE LS");
            dataHelper.insertCatSubMarcaVehiculos("RENA",1358,"R12");
            dataHelper.insertCatSubMarcaVehiculos("RENA",1359,"R18");
            dataHelper.insertCatSubMarcaVehiculos("RENA",1360,"R18 GTX");
            dataHelper.insertCatSubMarcaVehiculos("RENA",1361,"R5 TX MIRAGE");
            dataHelper.insertCatSubMarcaVehiculos("RENA",1362,"SANDERO");
            dataHelper.insertCatSubMarcaVehiculos("RENA",1363,"SANDERO R.S.");
            dataHelper.insertCatSubMarcaVehiculos("RENA",1364,"SANDERO RS");
            dataHelper.insertCatSubMarcaVehiculos("RENA",1365,"SCALA");
            dataHelper.insertCatSubMarcaVehiculos("RENA",1366,"SCENIC");
            dataHelper.insertCatSubMarcaVehiculos("RENA",1367,"SCENIC II");
            dataHelper.insertCatSubMarcaVehiculos("RENA",1368,"STEPWAY");
            dataHelper.insertCatSubMarcaVehiculos("RENA",1369,"TRAFIC");
            dataHelper.insertCatSubMarcaVehiculos("RENA",1370,"TWIZY");
            dataHelper.insertCatSubMarcaVehiculos("ROLL",1371,"DAWN");
            dataHelper.insertCatSubMarcaVehiculos("ROLL",1372,"GHOST");
            dataHelper.insertCatSubMarcaVehiculos("ROLL",1373,"PHANTOM");
            dataHelper.insertCatSubMarcaVehiculos("ROLL",1374,"PHANTOM COUPE");
            dataHelper.insertCatSubMarcaVehiculos("ROLL",1375,"PHANTOM DROPHEAD COUPE");
            dataHelper.insertCatSubMarcaVehiculos("ROLL",1376,"SILVER SHADOW");
            dataHelper.insertCatSubMarcaVehiculos("ROLL",1377,"WRAITH");
            dataHelper.insertCatSubMarcaVehiculos("SAAB",1378,"9-1");
            dataHelper.insertCatSubMarcaVehiculos("SAAB",1379,"9-2");
            dataHelper.insertCatSubMarcaVehiculos("SAAB",1380,"9-3");
            dataHelper.insertCatSubMarcaVehiculos("SAAB",1381,"9-4");
            dataHelper.insertCatSubMarcaVehiculos("SAAB",1382,"9-5");
            dataHelper.insertCatSubMarcaVehiculos("SAAB",1383,"9-6");
            dataHelper.insertCatSubMarcaVehiculos("SAAB",1384,"9-7");
            dataHelper.insertCatSubMarcaVehiculos("SALE",1385,"302");
            dataHelper.insertCatSubMarcaVehiculos("SALE",1386,"570");
            dataHelper.insertCatSubMarcaVehiculos("SALE",1387,"620");
            dataHelper.insertCatSubMarcaVehiculos("SALE",1388,"FOCUS");
            dataHelper.insertCatSubMarcaVehiculos("SALE",1389,"GTX");
            dataHelper.insertCatSubMarcaVehiculos("SALE",1390,"MD");
            dataHelper.insertCatSubMarcaVehiculos("SALE",1391,"SS");
            dataHelper.insertCatSubMarcaVehiculos("SATU",1392,"ASTRA");
            dataHelper.insertCatSubMarcaVehiculos("SATU",1393,"AURA");
            dataHelper.insertCatSubMarcaVehiculos("SATU",1394,"CURVE");
            dataHelper.insertCatSubMarcaVehiculos("SATU",1395,"CV1");
            dataHelper.insertCatSubMarcaVehiculos("SATU",1396,"FLEXTREME");
            dataHelper.insertCatSubMarcaVehiculos("SATU",1397,"GM EV1");
            dataHelper.insertCatSubMarcaVehiculos("SATU",1398,"ION");
            dataHelper.insertCatSubMarcaVehiculos("SATU",1399,"LS");
            dataHelper.insertCatSubMarcaVehiculos("SATU",1400,"LW");
            dataHelper.insertCatSubMarcaVehiculos("SATU",1401,"OUTLOOK");
            dataHelper.insertCatSubMarcaVehiculos("SATU",1402,"S");
            dataHelper.insertCatSubMarcaVehiculos("SATU",1403,"S SW");
            dataHelper.insertCatSubMarcaVehiculos("SATU",1404,"SC");
            dataHelper.insertCatSubMarcaVehiculos("SATU",1405,"SEDAN");
            dataHelper.insertCatSubMarcaVehiculos("SATU",1406,"SKY");
            dataHelper.insertCatSubMarcaVehiculos("SATU",1407,"SL");
            dataHelper.insertCatSubMarcaVehiculos("SATU",1408,"SL2");
            dataHelper.insertCatSubMarcaVehiculos("SATU",1409,"SW");
            dataHelper.insertCatSubMarcaVehiculos("SATU",1410,"VUE");
            dataHelper.insertCatSubMarcaVehiculos("SEAT",1411,"IBIZA");
            dataHelper.insertCatSubMarcaVehiculos("SEAT",1412,"TOLEDO");
            dataHelper.insertCatSubMarcaVehiculos("SEAT",1413,"LEÓN");
            dataHelper.insertCatSubMarcaVehiculos("SEAT",1414,"ATECA");
            dataHelper.insertCatSubMarcaVehiculos("SEAT",1415,"ARONA");
            dataHelper.insertCatSubMarcaVehiculos("SEAT",1416,"TARRACO");
            dataHelper.insertCatSubMarcaVehiculos("SEAT",1417,"LEON SC");
            dataHelper.insertCatSubMarcaVehiculos("SEAT",1418,"LEON ST");
            dataHelper.insertCatSubMarcaVehiculos("SEAT",1419,"ALHAMBRA");
            dataHelper.insertCatSubMarcaVehiculos("SEAT",1420,"ALTEA");
            dataHelper.insertCatSubMarcaVehiculos("SEAT",1421,"CORDOBA");
            dataHelper.insertCatSubMarcaVehiculos("SEAT",1422,"EXEO");
            dataHelper.insertCatSubMarcaVehiculos("SEAT",1423,"LEON 5D");
            dataHelper.insertCatSubMarcaVehiculos("SEAT",1424,"LEON CUPRA");
            dataHelper.insertCatSubMarcaVehiculos("SEAT",1425,"LEON");
            dataHelper.insertCatSubMarcaVehiculos("SEAT",1426,"IBIZA SC");
            dataHelper.insertCatSubMarcaVehiculos("SEAT",1427,"IBIZA 5D");
            dataHelper.insertCatSubMarcaVehiculos("SMAR",1428,"BRABUS");
            dataHelper.insertCatSubMarcaVehiculos("SMAR",1429,"CITY");
            dataHelper.insertCatSubMarcaVehiculos("SMAR",1430,"FORFOUR");
            dataHelper.insertCatSubMarcaVehiculos("SMAR",1431,"FORTWO");
            dataHelper.insertCatSubMarcaVehiculos("SMAR",1432,"FORTWO CABRIOLET");
            dataHelper.insertCatSubMarcaVehiculos("SMAR",1433,"FORTWO COUPE");
            dataHelper.insertCatSubMarcaVehiculos("SMAR",1434,"FORTWO PASSION");
            dataHelper.insertCatSubMarcaVehiculos("SMAR",1435,"FORTWO REVIEWS");
            dataHelper.insertCatSubMarcaVehiculos("SMAR",1436,"ROADSTER");
            dataHelper.insertCatSubMarcaVehiculos("SMAR",1437,"SMART FORFOUR");
            dataHelper.insertCatSubMarcaVehiculos("SMAR",1438,"SMART FORTWO");
            dataHelper.insertCatSubMarcaVehiculos("SRT",1439,"300");
            dataHelper.insertCatSubMarcaVehiculos("SRT",1440,"CHALLENGER");
            dataHelper.insertCatSubMarcaVehiculos("SRT",1441,"CHARGER");
            dataHelper.insertCatSubMarcaVehiculos("SRT",1442,"GRAND CHEROKEE");
            dataHelper.insertCatSubMarcaVehiculos("SRT",1443,"VIPER");
            dataHelper.insertCatSubMarcaVehiculos("SUBA",1444,"FORESTER");
            dataHelper.insertCatSubMarcaVehiculos("SUBA",1445,"IMPREZA");
            dataHelper.insertCatSubMarcaVehiculos("SUBA",1446,"IMPREZA SEDAN");
            dataHelper.insertCatSubMarcaVehiculos("SUBA",1447,"LEGACY");
            dataHelper.insertCatSubMarcaVehiculos("SUBA",1448,"OUTBACK");
            dataHelper.insertCatSubMarcaVehiculos("SUBA",1449,"SUBARU BRZ");
            dataHelper.insertCatSubMarcaVehiculos("SUBA",1450,"SUBARU XV");
            dataHelper.insertCatSubMarcaVehiculos("SUBA",1451,"TRIBECA B9");
            dataHelper.insertCatSubMarcaVehiculos("SUBA",1452,"WRX");
            dataHelper.insertCatSubMarcaVehiculos("SUBA",1453,"WRX STI");
            dataHelper.insertCatSubMarcaVehiculos("SUBA",1454,"WRX/WRX STI");
            dataHelper.insertCatSubMarcaVehiculos("SUBA",1455,"XV");
            dataHelper.insertCatSubMarcaVehiculos("SUZU",1456,"AERIO");
            dataHelper.insertCatSubMarcaVehiculos("SUZU",1457,"CIAZ");
            dataHelper.insertCatSubMarcaVehiculos("SUZU",1458,"ERTIGA");
            dataHelper.insertCatSubMarcaVehiculos("SUZU",1459,"GRAND VITARA");
            dataHelper.insertCatSubMarcaVehiculos("SUZU",1460,"GRAND VITARA 4X4");
            dataHelper.insertCatSubMarcaVehiculos("SUZU",1461,"GRAND VITARA XL-7");
            dataHelper.insertCatSubMarcaVehiculos("SUZU",1462,"IGNIS");
            dataHelper.insertCatSubMarcaVehiculos("SUZU",1463,"KIZASHI");
            dataHelper.insertCatSubMarcaVehiculos("SUZU",1464,"NUEVA VITARA");
            dataHelper.insertCatSubMarcaVehiculos("SUZU",1465,"SCROSS");
            dataHelper.insertCatSubMarcaVehiculos("SUZU",1466,"S-CROSS");
            dataHelper.insertCatSubMarcaVehiculos("SUZU",1467,"SWIFT");
            dataHelper.insertCatSubMarcaVehiculos("SUZU",1468,"SWIFT SPORT");
            dataHelper.insertCatSubMarcaVehiculos("SUZU",1469,"SX4 X OVER");
            dataHelper.insertCatSubMarcaVehiculos("SUZU",1470,"VITARA");
            dataHelper.insertCatSubMarcaVehiculos("TESL",1471,"MODEL 3");
            dataHelper.insertCatSubMarcaVehiculos("TESL",1472,"MODEL S");
            dataHelper.insertCatSubMarcaVehiculos("TESL",1473,"MODEL X");
            dataHelper.insertCatSubMarcaVehiculos("TESL",1474,"MODEL Y");
            dataHelper.insertCatSubMarcaVehiculos("TOYO",1475,"4 RUNNER");
            dataHelper.insertCatSubMarcaVehiculos("TOYO",1476,"AVANZA");
            dataHelper.insertCatSubMarcaVehiculos("TOYO",1477,"CAMRY");
            dataHelper.insertCatSubMarcaVehiculos("TOYO",1478,"CH-R");
            dataHelper.insertCatSubMarcaVehiculos("TOYO",1479,"COROLLA");
            dataHelper.insertCatSubMarcaVehiculos("TOYO",1480,"FJ CRUISER");
            dataHelper.insertCatSubMarcaVehiculos("TOYO",1481,"HIACE");
            dataHelper.insertCatSubMarcaVehiculos("TOYO",1482,"HIGHLANDER");
            dataHelper.insertCatSubMarcaVehiculos("TOYO",1483,"HILUX");
            dataHelper.insertCatSubMarcaVehiculos("TOYO",1484,"LAND CRUISER");
            dataHelper.insertCatSubMarcaVehiculos("TOYO",1485,"MATRIX");
            dataHelper.insertCatSubMarcaVehiculos("TOYO",1486,"MR2");
            dataHelper.insertCatSubMarcaVehiculos("TOYO",1487,"MR2 SPYDER");
            dataHelper.insertCatSubMarcaVehiculos("TOYO",1488,"PICK-UP");
            dataHelper.insertCatSubMarcaVehiculos("TOYO",1489,"PRIUS");
            dataHelper.insertCatSubMarcaVehiculos("TOYO",1490,"RAV4");
            dataHelper.insertCatSubMarcaVehiculos("TOYO",1491,"RUSH");
            dataHelper.insertCatSubMarcaVehiculos("TOYO",1492,"SEQUOIA");
            dataHelper.insertCatSubMarcaVehiculos("TOYO",1493,"SIENNA");
            dataHelper.insertCatSubMarcaVehiculos("TOYO",1494,"SOLARA");
            dataHelper.insertCatSubMarcaVehiculos("TOYO",1495,"SUPRA");
            dataHelper.insertCatSubMarcaVehiculos("TOYO",1496,"T100");
            dataHelper.insertCatSubMarcaVehiculos("TOYO",1497,"TACOMA");
            dataHelper.insertCatSubMarcaVehiculos("TOYO",1498,"TUNDRA");
            dataHelper.insertCatSubMarcaVehiculos("TOYO",1499,"YARIS");
            dataHelper.insertCatSubMarcaVehiculos("TOYO",1500,"YARIS HATCHBACK");
            dataHelper.insertCatSubMarcaVehiculos("TOYO",1501,"YARIS R");
            dataHelper.insertCatSubMarcaVehiculos("TOYO",1502,"YARIS SEDAN");
            dataHelper.insertCatSubMarcaVehiculos("TRAC",1503,"INTERNATIONAL");
            dataHelper.insertCatSubMarcaVehiculos("TRAC",1504,"KENWORTH");
            dataHelper.insertCatSubMarcaVehiculos("TRAC",1505,"MERCEDES BENZ");
            dataHelper.insertCatSubMarcaVehiculos("TRAC",1506,"VOLVO");
            dataHelper.insertCatSubMarcaVehiculos("TRAC",1507,"GMC");
            dataHelper.insertCatSubMarcaVehiculos("TRAC",1508,"FREIGHTLINER");
            dataHelper.insertCatSubMarcaVehiculos("TRAC",1509,"FORD");
            dataHelper.insertCatSubMarcaVehiculos("TRAC",1510,"FAW");
            dataHelper.insertCatSubMarcaVehiculos("VOLK",1511,"AMAROK");
            dataHelper.insertCatSubMarcaVehiculos("VOLK",1512,"ATLANTIC");
            dataHelper.insertCatSubMarcaVehiculos("VOLK",1513,"BEETLE");
            dataHelper.insertCatSubMarcaVehiculos("VOLK",1514,"BORA");
            dataHelper.insertCatSubMarcaVehiculos("VOLK",1515,"BRASILIA");
            dataHelper.insertCatSubMarcaVehiculos("VOLK",1516,"BUGGY");
            dataHelper.insertCatSubMarcaVehiculos("VOLK",1517,"CABRIO");
            dataHelper.insertCatSubMarcaVehiculos("VOLK",1518,"CABRIOLET");
            dataHelper.insertCatSubMarcaVehiculos("VOLK",1519,"CADDY PASAJEROS");
            dataHelper.insertCatSubMarcaVehiculos("VOLK",1520,"CARAVELLE");
            dataHelper.insertCatSubMarcaVehiculos("VOLK",1521,"CARIBE");
            dataHelper.insertCatSubMarcaVehiculos("VOLK",1522,"CC");
            dataHelper.insertCatSubMarcaVehiculos("VOLK",1523,"COMBI");
            dataHelper.insertCatSubMarcaVehiculos("VOLK",1524,"CORSAR");
            dataHelper.insertCatSubMarcaVehiculos("VOLK",1525,"CRAFTER");
            dataHelper.insertCatSubMarcaVehiculos("VOLK",1526,"CRAFTER CARGO VAN");
            dataHelper.insertCatSubMarcaVehiculos("VOLK",1527,"CRAFTER CHASIS CABINA");
            dataHelper.insertCatSubMarcaVehiculos("VOLK",1528,"CRAFTER PASAJEROS");
            dataHelper.insertCatSubMarcaVehiculos("VOLK",1529,"CROSS GOLF");
            dataHelper.insertCatSubMarcaVehiculos("VOLK",1530,"CROSSFOX");
            dataHelper.insertCatSubMarcaVehiculos("VOLK",1531,"DERBY");
            dataHelper.insertCatSubMarcaVehiculos("VOLK",1532,"EUROVAN");
            dataHelper.insertCatSubMarcaVehiculos("VOLK",1533,"GOL");
            dataHelper.insertCatSubMarcaVehiculos("VOLK",1534,"GOL SEDAN");
            dataHelper.insertCatSubMarcaVehiculos("VOLK",1535,"GOLF");
            dataHelper.insertCatSubMarcaVehiculos("VOLK",1536,"GOLF GTI");
            dataHelper.insertCatSubMarcaVehiculos("VOLK",1537,"GTI");
            dataHelper.insertCatSubMarcaVehiculos("VOLK",1538,"JETTA");
            dataHelper.insertCatSubMarcaVehiculos("VOLK",1539,"JETTA GLI");
            dataHelper.insertCatSubMarcaVehiculos("VOLK",1540,"KARMANN GHIA");
            dataHelper.insertCatSubMarcaVehiculos("VOLK",1541,"LUPO");
            dataHelper.insertCatSubMarcaVehiculos("VOLK",1542,"PANEL");
            dataHelper.insertCatSubMarcaVehiculos("VOLK",1543,"PASSAT");
            dataHelper.insertCatSubMarcaVehiculos("VOLK",1544,"POINTER");
            dataHelper.insertCatSubMarcaVehiculos("VOLK",1545,"POLO");
            dataHelper.insertCatSubMarcaVehiculos("VOLK",1546,"POLO GTI");
            dataHelper.insertCatSubMarcaVehiculos("VOLK",1547,"RABITT");
            dataHelper.insertCatSubMarcaVehiculos("VOLK",1548,"SAFARI");
            dataHelper.insertCatSubMarcaVehiculos("VOLK",1549,"SAVEIRO");
            dataHelper.insertCatSubMarcaVehiculos("VOLK",1550,"SAVEIRO DOBLE CABINA");
            dataHelper.insertCatSubMarcaVehiculos("VOLK",1551,"SEDAN");
            dataHelper.insertCatSubMarcaVehiculos("VOLK",1552,"SEDANETA");
            dataHelper.insertCatSubMarcaVehiculos("VOLK",1553,"SHARAN");
            dataHelper.insertCatSubMarcaVehiculos("VOLK",1554,"SPORTVAN");
            dataHelper.insertCatSubMarcaVehiculos("VOLK",1555,"TIGUAN");
            dataHelper.insertCatSubMarcaVehiculos("VOLK",1556,"TOUAREG");
            dataHelper.insertCatSubMarcaVehiculos("VOLK",1557,"TRANSPORTED CARGO VAN");
            dataHelper.insertCatSubMarcaVehiculos("VOLK",1558,"TRANSPORTED CHASIS CABINA");
            dataHelper.insertCatSubMarcaVehiculos("VOLK",1559,"TRANSPORTED PASAJEROS");
            dataHelper.insertCatSubMarcaVehiculos("VOLK",1560,"TRANSPORTER");
            dataHelper.insertCatSubMarcaVehiculos("VOLK",1561,"UP");
            dataHelper.insertCatSubMarcaVehiculos("VOLK",1562,"VENTO");
            dataHelper.insertCatSubMarcaVehiculos("VOLK",1563,"VIRTUS");
            dataHelper.insertCatSubMarcaVehiculos("VOLK",1564,"VOLKSBUS");
            dataHelper.insertCatSubMarcaVehiculos("VOLK",1565,"WORKER");
            dataHelper.insertCatSubMarcaVehiculos("VOLV",1566,"240");
            dataHelper.insertCatSubMarcaVehiculos("VOLV",1567,"C30");
            dataHelper.insertCatSubMarcaVehiculos("VOLV",1568,"C70");
            dataHelper.insertCatSubMarcaVehiculos("VOLV",1569,"CROSS COUNTRY V40");
            dataHelper.insertCatSubMarcaVehiculos("VOLV",1570,"CROSS COUNTRY V60");
            dataHelper.insertCatSubMarcaVehiculos("VOLV",1571,"CROSSOVER XC60");
            dataHelper.insertCatSubMarcaVehiculos("VOLV",1572,"HATCHBACK V60");
            dataHelper.insertCatSubMarcaVehiculos("VOLV",1573,"P1800");
            dataHelper.insertCatSubMarcaVehiculos("VOLV",1574,"S40");
            dataHelper.insertCatSubMarcaVehiculos("VOLV",1575,"S60");
            dataHelper.insertCatSubMarcaVehiculos("VOLV",1576,"S60 CROSS COUNTRY");
            dataHelper.insertCatSubMarcaVehiculos("VOLV",1577,"S60 SEDAN");
            dataHelper.insertCatSubMarcaVehiculos("VOLV",1578,"S60R");
            dataHelper.insertCatSubMarcaVehiculos("VOLV",1579,"S70");
            dataHelper.insertCatSubMarcaVehiculos("VOLV",1580,"S80");
            dataHelper.insertCatSubMarcaVehiculos("VOLV",1581,"S90");
            dataHelper.insertCatSubMarcaVehiculos("VOLV",1582,"TRUCK");
            dataHelper.insertCatSubMarcaVehiculos("VOLV",1583,"V40");
            dataHelper.insertCatSubMarcaVehiculos("VOLV",1584,"V50");
            dataHelper.insertCatSubMarcaVehiculos("VOLV",1585,"V70");
            dataHelper.insertCatSubMarcaVehiculos("VOLV",1586,"XC60");
            dataHelper.insertCatSubMarcaVehiculos("VOLV",1587,"XC70");
            dataHelper.insertCatSubMarcaVehiculos("VOLV",1588,"XC90");

        }
    }
}