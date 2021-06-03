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
import mx.ssp.iph.principal.ui.fragments.PrincipalAdministrativo;
import mx.ssp.iph.principal.ui.fragments.PrincipalBuscar;
import mx.ssp.iph.principal.ui.fragments.PrincipalDelictivo;
import mx.ssp.iph.principal.ui.fragments.PrincipalEmergencias;
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
    private int idMunicipio;
    private String municipio;
    int idAutoridadAdmin; String autoridadAdmin;
    int idCargo; String cargo;
    int idConocimiento; String conocimiento;
    int idFiscaliaAutoridad; String fiscaliaAutoridad;
    int idInstitucion;String institucion;
    int idLugarTraslado; String lugarTraslado; String descripcion;
    int idNacionalida; String nacionalida; String desNacionalidad;
    int idSexo;String sexo;
    String idUnidad; String unidad; String idMarca,marca,subMarca; int idSubMarca; int modelo; String descripcionU; int idInstitucionU;

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
        /**********************************************************/
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

        /**********************************************************/

        //Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("titulo toolbar");

        PrincipalDelictivo = new PrincipalDelictivo();
        PrincipalAdministrativo= new PrincipalAdministrativo();
        PrincipalEmergencias = new PrincipalEmergencias();
        PrincipalBuscar = new PrincipalBuscar();




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

    private void ListSubMarca() {
        DataHelper dataHelper = new DataHelper(getApplicationContext());
        ArrayList<String> list = dataHelper.getAllSubMarcaVehiculos();
        if (list.size() > 0) {
            System.out.println("YA EXISTE INFORMACIÓN DE SUBMARCAVEHICULOS");
        }else{
            getSubmarcaV();
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
                                                idMunicipio = (ja.getJSONObject(i).getInt("IdMunicipio"));
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
    public void getSubmarcaV() {
        DataHelper dataHelper = new DataHelper(getApplicationContext());

        final OkHttpClient client = new OkHttpClient();
        final Request request = new Request.Builder()
                .url("http://189.254.7.167/WebServiceIPH/api/CatSubMarcaVehiculos")
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
                                                idSubMarca = (ja.getJSONObject(i).getInt("IdSubMarca"));
                                                subMarca = (ja.getJSONObject(i).getString("SubMarca"));
                                                dataHelper.insertCatSubMarcaVehiculos(idMarca,idSubMarca,subMarca);
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
}