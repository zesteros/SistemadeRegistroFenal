package zesters.sistemaderegistrofenal;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Vibrator;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;
import android.widget.Toast;

import java.util.Locale;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private NavigationDrawerFragment mNavigationDrawerFragment;
    private static final String MAIN_TAG = "main";
    private static final String ADD_CUSTOMER_TAG = "customer";
    private static final String ADD_BOOK_TAG = "book";
    private static final String SEARCH_CUSTOMER_TAG = "searchCustomer";
    private static final String SEARCH_BOOK_TAG = "searchBook";
    private static final String DELETE_CUSTOMER_TAG = "deleteCustomer";
    private static final String LIST_BOOK_TAG = "listBook";
    private static final String LIST_EDITORIAL_TAG = "listEditorial";
    private static final String CUSTOMIZE_REGISTER_TAG = "modificaRegistro";
    private static final String MODIFICAR_CLIENTE = "modificaCliente";
    private static final String MODIFICAR_LIBRO = "modificaLibro";

    protected AlertDialog.Builder dialog;
    protected CharSequence mTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onNavigationDrawerItemSelected(int position) {
        android.app.FragmentManager fragmentManager = getFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        switch (position) {
            case 0:
                MainFragment mainFragment = new MainFragment();
                fragmentTransaction.add(R.id.container, mainFragment, MAIN_TAG).commit();
                restoreActionBar(getString(R.string.title_main_menu));
                break;
            case 2:
                AgregaClienteFragment agregaClienteFragment = new AgregaClienteFragment();
                fragmentTransaction.replace(R.id.container, agregaClienteFragment, ADD_CUSTOMER_TAG).commit();
                restoreActionBar(getString(R.string.title_activity_anade_cliente));
                break;
            case 3:
                AgregaLibroFragment agregaLibroFragment = new AgregaLibroFragment();
                fragmentTransaction.replace(R.id.container, agregaLibroFragment, ADD_BOOK_TAG).commit();
                restoreActionBar(getString(R.string.title_activity_anade_libro));
                break;
            case 5:
                BuscaClienteFragment buscaClienteFragment = new BuscaClienteFragment();
                fragmentTransaction.replace(R.id.container, buscaClienteFragment, SEARCH_CUSTOMER_TAG).commit();
                restoreActionBar(getString(R.string.title_activity_busca_cliente));
                break;
            case 6:
                BuscaLibroFragment buscaLibroFragment = new BuscaLibroFragment();
                fragmentTransaction.replace(R.id.container, buscaLibroFragment, SEARCH_BOOK_TAG).commit();
                restoreActionBar(getString(R.string.title_activity_busca_isbn));
                break;
            case 8:
                EliminaClienteFragment eliminaClienteFragment = new EliminaClienteFragment();
                fragmentTransaction.replace(R.id.container, eliminaClienteFragment, DELETE_CUSTOMER_TAG).commit();
                restoreActionBar(getString(R.string.title_activity_elimina_cliente));
                break;
            case 10:
                ListaLibroFragment listaLibroFragment = new ListaLibroFragment();
                fragmentTransaction.replace(R.id.container, listaLibroFragment, LIST_BOOK_TAG).commit();
                restoreActionBar(getString(R.string.title_activity_lista_libros));
                break;
            case 11:
                ListaEditorialFragment listaEditorialFragment = new ListaEditorialFragment();
                fragmentTransaction.replace(R.id.container, listaEditorialFragment, LIST_EDITORIAL_TAG).commit();
                restoreActionBar(getString(R.string.title_activity_lista_editorial));
                break;
            case 13:
                Bundle argModifica = new Bundle();
                argModifica.putBoolean(MODIFICAR_CLIENTE, true);
                ModificaRegistroFragment modificaRegistroFragment = new ModificaRegistroFragment();
                modificaRegistroFragment.setArguments(argModifica);
                fragmentTransaction.replace(R.id.container, modificaRegistroFragment, CUSTOMIZE_REGISTER_TAG).commit();
                restoreActionBar(getString(R.string.title_activity_modifica_cliente));
                break;
            case 14:
                argModifica = new Bundle();
                argModifica.putBoolean(MODIFICAR_LIBRO, true);
                modificaRegistroFragment = new ModificaRegistroFragment();
                modificaRegistroFragment.setArguments(argModifica);
                fragmentTransaction.replace(R.id.container, modificaRegistroFragment, CUSTOMIZE_REGISTER_TAG).commit();
                restoreActionBar(getString(R.string.title_activity_modifica_libro));

                break;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            getMenuInflater().inflate(R.menu.main, menu);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        dialog = new android.app.AlertDialog.Builder(this);

        Vibrator vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);

        Locale english = new Locale("en");
        Locale spanish = new Locale("es");

        Configuration config = new Configuration();

        int botonOprimidoMenu = item.getItemId();

        switch (botonOprimidoMenu) {
            case R.id.menu_info:
                dialog.setTitle(getResources().getString(R.string.datos_title));
                dialog.setMessage(getResources().getString(R.string.datos_developer1) + "\n" +
                        getResources().getString(R.string.datos_developer2).toUpperCase());
                dialog.setNeutralButton(R.string.aceptar_alert, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.create();
                dialog.show();
                break;

            case R.id.salir:
                Toast.makeText(this, getResources().getString(R.string.despedida), Toast.LENGTH_SHORT).show();
                vibrator.vibrate(50);
                finish();
                break;

            case R.id.english:
                setLocale("en");
                break;
            case R.id.spanish:
                setLocale("es");
                break;
        }

        return super.onOptionsItemSelected(item);
    }
    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(this, MainActivity.class);
        startActivity(refresh);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (this.mNavigationDrawerFragment
                .isDrawerOpen()) {
            this.mNavigationDrawerFragment
                    .mDrawerLayout
                    .closeDrawer(this
                            .mNavigationDrawerFragment
                            .mFragmentContainerView);
        } else if (!this
                .mNavigationDrawerFragment
                .isDrawerOpen()) {
            if (getFragmentManager()
                    .findFragmentByTag(MAIN_TAG) == null) {
                MainFragment mainFragment = new MainFragment();
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, mainFragment, MAIN_TAG)
                        .commit();
                setmTitle(getString(R.string.title_main_menu));
            } else {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }

    public void restoreActionBar(String mTitle) {
        this.setmTitle(mTitle);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    protected void setmTitle(CharSequence mTitle) {
        this.mTitle = mTitle;
    }

    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }
}
