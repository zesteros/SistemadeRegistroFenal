package zesters.sistemaderegistrofenal;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.app.AlertDialog;
import android.content.*;
import android.widget.*;

public class MainFragment extends Fragment implements View.OnClickListener {

    protected TextView titulo1;
    protected TextView titulo2;
    protected Button anadeRegistro;
    protected Button buscarCliente;
    protected Button buscaIsbn;
    protected Button eliminaCliente;
    protected Button listaLibro;
    protected Button libroPorEditorial;
    protected Button modificaRegistro;
    protected int botonOprimido;
    protected AlertDialog.Builder dialog;
    protected AlertDialog.Builder addRegisterDialog;
    protected String MODIFICAR_CLIENTE = "modificaCliente";
    protected String MODIFICAR_LIBRO = "modificaLibro";
    private static final String ADD_CUSTOMER_TAG = "customer";
    private static final String ADD_BOOK_TAG = "book";
    private static final String SEARCH_CUSTOMER_TAG = "searchCustomer";
    private static final String SEARCH_BOOK_TAG = "searchBook";
    private static final String DELETE_CUSTOMER_TAG = "deleteCustomer";
    private static final String LIST_BOOK_TAG = "listBook";
    private static final String LIST_EDITORIAL_TAG = "listEditorial";
    private static final String CUSTOMIZE_REGISTER_TAG = "modificaRegistro";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.title_main_menu));

        View view = inflater.inflate(R.layout.fragment_main, container, false);
        titulo1 = (TextView) view.findViewById(R.id.texto1);
        titulo2 = (TextView) view.findViewById(R.id.texto2);

        anadeRegistro = (Button) view.findViewById(R.id.boton1);
        buscarCliente = (Button) view.findViewById(R.id.boton2);
        buscaIsbn = (Button) view.findViewById(R.id.boton3);
        eliminaCliente = (Button) view.findViewById(R.id.boton4);
        listaLibro = (Button) view.findViewById(R.id.boton5);
        libroPorEditorial = (Button) view.findViewById(R.id.boton6);
        modificaRegistro = (Button) view.findViewById(R.id.boton7);

        anadeRegistro.setOnClickListener(this);
        buscarCliente.setOnClickListener(this);
        buscaIsbn.setOnClickListener(this);
        eliminaCliente.setOnClickListener(this);
        listaLibro.setOnClickListener(this);
        libroPorEditorial.setOnClickListener(this);
        modificaRegistro.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        botonOprimido = v.getId();
        final MainActivity main = new MainActivity();
        final android.app.FragmentManager fragmentManager = getFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        try {
            switch (botonOprimido) {

                case R.id.boton1:
                    addRegisterDialog = new android.app.AlertDialog.Builder(getActivity());
                    addRegisterDialog.setTitle(getResources().getString(R.string.boton1));
                    addRegisterDialog.setMessage(getResources().getString(R.string.add_register));
                    addRegisterDialog.setPositiveButton(getResources().getString(R.string.book), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            AgregaLibroFragment agregaLibro = new AgregaLibroFragment();
                            fragmentTransaction.replace(R.id.container, agregaLibro, ADD_BOOK_TAG).commit();
                            ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.title_activity_anade_libro));
                        }
                    });
                    addRegisterDialog.setNeutralButton(getResources().getString(R.string.customer), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            AgregaClienteFragment agregaClienteFragment = new AgregaClienteFragment();
                            fragmentTransaction.replace(R.id.container, agregaClienteFragment, ADD_CUSTOMER_TAG).commit();
                            ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.title_activity_anade_cliente));
                        }
                    });
                    addRegisterDialog.create();
                    addRegisterDialog.show();
                    break;
                case R.id.boton2:
                    BuscaClienteFragment buscaClienteFragment = new BuscaClienteFragment();
                    fragmentTransaction.replace(R.id.container, buscaClienteFragment, SEARCH_CUSTOMER_TAG).commit();
                    ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.title_activity_busca_cliente));
                    break;
                case R.id.boton3:
                    BuscaLibroFragment buscaLibroFragment = new BuscaLibroFragment();
                    fragmentTransaction.replace(R.id.container, buscaLibroFragment, SEARCH_BOOK_TAG).commit();
                    ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.title_activity_busca_isbn));
                    break;
                case R.id.boton4:
                    EliminaClienteFragment eliminaClienteFragment = new EliminaClienteFragment();
                    fragmentTransaction.replace(R.id.container, eliminaClienteFragment, DELETE_CUSTOMER_TAG).commit();
                    ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.title_activity_elimina_cliente));
                    break;
                case R.id.boton5:
                    ListaLibroFragment listaLibroFragment = new ListaLibroFragment();
                    fragmentTransaction.replace(R.id.container, listaLibroFragment, LIST_BOOK_TAG).commit();
                    ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.title_activity_lista_libros));
                    break;
                case R.id.boton6:
                    ListaEditorialFragment listaEditorialFragment = new ListaEditorialFragment();
                    fragmentTransaction.replace(R.id.container, listaEditorialFragment, LIST_EDITORIAL_TAG).commit();
                    ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.title_activity_lista_editorial));
                    break;
                case R.id.boton7:
                    dialog = new android.app.AlertDialog.Builder(getActivity());

                    dialog.setTitle(getResources().getString(R.string.boton7));
                    dialog.setMessage(getResources().getString(R.string.text_modificar_title));
                    dialog.setNeutralButton(getResources().getString(R.string.customer), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Bundle argModifica = new Bundle();
                            argModifica.putBoolean(MODIFICAR_CLIENTE, true);
                            ModificaRegistroFragment modificaRegistroFragment = new ModificaRegistroFragment();
                            modificaRegistroFragment.setArguments(argModifica);
                            fragmentTransaction.replace(R.id.container, modificaRegistroFragment, CUSTOMIZE_REGISTER_TAG).commit();
                            ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.title_activity_modifica_cliente));
                        }
                    });
                    dialog.setPositiveButton(getResources().getString(R.string.book), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Bundle argModifica = new Bundle();
                            argModifica.putBoolean(MODIFICAR_LIBRO, true);
                            ModificaRegistroFragment modificaRegistroFragment = new ModificaRegistroFragment();
                            modificaRegistroFragment.setArguments(argModifica);
                            fragmentTransaction.replace(R.id.container, modificaRegistroFragment, CUSTOMIZE_REGISTER_TAG).commit();
                            ((MainActivity) getActivity()).setActionBarTitle(getString(R.string.title_activity_modifica_libro));
                        }
                    });
                    dialog.create();
                    dialog.show();
                    break;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }
}
