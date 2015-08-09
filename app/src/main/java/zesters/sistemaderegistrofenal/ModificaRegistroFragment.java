package zesters.sistemaderegistrofenal;


import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Environment;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class ModificaRegistroFragment extends Fragment {

    protected int noClienteBusqueda;
    protected int noCliente;
    protected int celular;
    protected int valorNuevo;
    protected int posInicial;
    protected int posRegistro;
    protected float costo;
    protected String nombreCliente;
    protected String procedencia;
    protected String correo;
    protected String textoNuevo;
    protected String isbnBusqueda;
    protected String isbn;
    protected String nombreLibro;
    protected String autor;
    protected String editorial;
    protected String genero;
    protected int pocisionRegistro;
    protected boolean encontrado;
    protected Button modificaButton;
    protected TextView subtituloModificar;
    protected TextView muestraDatosModificar;
    protected AlertDialog.Builder valorNuevoDialog;
    protected AlertDialog.Builder ingresaIsbnDialog;
    protected AlertDialog.Builder ingresaNoClienteDialog;
    protected AlertDialog.Builder opcionModificaClienteDialog;
    protected AlertDialog.Builder opcionModificaLibroDialog;
    protected AlertDialog.Builder opcionModificaGeneroDialog;
    protected CharSequence[] opciones;
    protected EditText valorNuevoField;
    protected EditText buscarField;
    protected LinearLayout.LayoutParams parametrosLayout;
    protected Bundle recibeDatos;
    protected String MODIFICAR_CLIENTE = "modificaCliente";
    protected String MODIFICAR_LIBRO = "modificaLibro";
    protected RandomAccessFile acceso;
    protected File tarjetaSd;
    protected File archivo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_modifica_registro, container, false);
        recibeDatos = this.getArguments();

        ingresaNoClienteDialog = new AlertDialog.Builder(getActivity());
        opcionModificaClienteDialog = new AlertDialog.Builder(getActivity());
        opcionModificaLibroDialog = new AlertDialog.Builder(getActivity());
        opcionModificaGeneroDialog = new AlertDialog.Builder(getActivity());
        ingresaIsbnDialog = new AlertDialog.Builder(getActivity());
        valorNuevoDialog = new AlertDialog.Builder(getActivity());

        buscarField = (EditText) v.findViewById(R.id.edit_busca_dato);
        subtituloModificar = (TextView) v.findViewById(R.id.titulo_busca_client1);
        muestraDatosModificar = (TextView) v.findViewById(R.id.datos_a_modificar);
        modificaButton = (Button) v.findViewById(R.id.modificar_registro_button);

        parametrosLayout = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        setPocisionRegistro(0);

        tarjetaSd = Environment.getExternalStorageDirectory();

        if (recibeDatos.getBoolean(MODIFICAR_CLIENTE, false)) {
            archivo = new File(tarjetaSd.getAbsolutePath(), "cliente.dat");

            subtituloModificar.setText(getResources().getString(R.string.text_titulo_modificar_cliente));
            buscarField.setHint(getResources().getString(R.string.text_numero_cliente));
            buscarField.setInputType(InputType.TYPE_CLASS_NUMBER);

            modificaButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {

                        acceso = new RandomAccessFile(archivo, "rw");

                        acceso.seek(0);

                        noClienteBusqueda = Integer.parseInt(buscarField.getText().toString());

                        for (int i = 0; i < acceso.length(); i++) {

                            posRegistro = (int)acceso.getFilePointer();
                            noCliente = acceso.readInt();
                            posInicial = (int) acceso.getFilePointer();
                            nombreCliente = acceso.readUTF();
                            acceso.seek(posInicial + 50);
                            posInicial = (int) acceso.getFilePointer();
                            procedencia = acceso.readUTF();
                            acceso.seek(posInicial + 50);
                            posInicial = (int) acceso.getFilePointer();
                            correo = acceso.readUTF();
                            acceso.seek(posInicial + 50);
                            celular = acceso.readInt();

                            i = (int) acceso.getFilePointer();

                            if (noCliente == noClienteBusqueda) {
                                setPocisionRegistro(posRegistro);
                                encontrado = true;

                                muestraDatosModificar.setText(getResources().getString(R.string.data_found) + "\n\n" +
                                        getResources().getString(R.string.text_numero_cliente) + ": " + noCliente + "\n" +
                                        getResources().getString(R.string.text_nombre_cliente) + ": " + nombreCliente + "\n" +
                                        getResources().getString(R.string.text_procedencia) + ": " + procedencia + "\n" +
                                        getResources().getString(R.string.text_correo) + ": " + correo + "\n" +
                                        getResources().getString(R.string.text_numero_telefonico) + ": " + celular);
                                modificaButton.setText(getResources().getString(R.string.title_activity_modifica_registro));
                                modificaButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        opcionModificaClienteDialog.setTitle
                                                (getResources().getString(R.string.text_modificar_title));
                                        opciones = new CharSequence[]
                                                {getResources().getString(R.string.text_numero_cliente),
                                                        getResources().getString(R.string.text_nombre_cliente),
                                                        getResources().getString(R.string.text_procedencia),
                                                        getResources().getString(R.string.text_correo),
                                                        getResources().getString(R.string.text_numero_telefonico)};
                                        opcionModificaClienteDialog.setItems(opciones,
                                                new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        switch (which) {
                                                            case 0:
                                                                valorNuevoDialog.setTitle(R.string.edit_value);
                                                                valorNuevoField = new EditText(getActivity());
                                                                valorNuevoField.setLayoutParams(parametrosLayout);
                                                                valorNuevoDialog.setView(valorNuevoField);
                                                                valorNuevoField.setHint(getResources().getString(R.string.text_numero_cliente));
                                                                valorNuevoField.setInputType(InputType.TYPE_CLASS_NUMBER);
                                                                valorNuevoDialog.setNegativeButton(getResources().getString(R.string.aceptar_alert),
                                                                        new DialogInterface.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(DialogInterface dialog, int which) {
                                                                                try {
                                                                                    acceso = new RandomAccessFile(archivo, "rw");
                                                                                    valorNuevo = Integer.parseInt(valorNuevoField.getText().
                                                                                            toString());
                                                                                    acceso.seek(getPocisionRegistro());
                                                                                    acceso.writeInt(valorNuevo);
                                                                                    Toast.makeText(getActivity(),
                                                                                            R.string.edit_register_success,
                                                                                            Toast.LENGTH_SHORT).show();

                                                                                    MainFragment mainFragment = new MainFragment();
                                                                                    getFragmentManager().beginTransaction().replace(R.id.container, mainFragment).commit();
                                                                                    acceso.close();
                                                                                } catch (IOException e) {
                                                                                    e.printStackTrace();
                                                                                } catch (NumberFormatException e) {
                                                                                    Toast.makeText(getActivity(), getResources().getString(R.string.field_empty), Toast.LENGTH_SHORT).show();
                                                                                    e.printStackTrace();
                                                                                }
                                                                            }
                                                                        });
                                                                valorNuevoDialog.create();
                                                                valorNuevoDialog.show();
                                                                break;
                                                            case 1:
                                                                valorNuevoDialog.setTitle(R.string.edit_value);
                                                                valorNuevoField = new EditText(getActivity());
                                                                valorNuevoField.setLayoutParams(parametrosLayout);
                                                                valorNuevoDialog.setView(valorNuevoField);
                                                                valorNuevoField.setHint(getResources().getString(R.string.text_nombre_cliente));
                                                                valorNuevoDialog.setNegativeButton(getResources().getString(R.string.aceptar_alert),
                                                                        new DialogInterface.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(DialogInterface dialog, int which) {
                                                                                try {
                                                                                    acceso = new RandomAccessFile(archivo, "rw");

                                                                                    textoNuevo = valorNuevoField.getText().
                                                                                            toString();
                                                                                    acceso.seek(getPocisionRegistro() + 4);
                                                                                    acceso.writeUTF(textoNuevo);
                                                                                    Toast.makeText(getActivity(),
                                                                                            R.string.edit_register_success,
                                                                                            Toast.LENGTH_SHORT).show();
                                                                                    acceso.close();
                                                                                    MainFragment mainFragment = new MainFragment();
                                                                                    getFragmentManager().beginTransaction().replace(R.id.container, mainFragment).commit();
                                                                                } catch (IOException e) {
                                                                                    e.printStackTrace();
                                                                                } catch (NumberFormatException e) {
                                                                                    Toast.makeText(getActivity(), getResources().getString(R.string.field_empty), Toast.LENGTH_SHORT).show();
                                                                                    e.printStackTrace();
                                                                                }
                                                                            }
                                                                        });
                                                                valorNuevoDialog.create();
                                                                valorNuevoDialog.show();
                                                                break;
                                                            case 2:
                                                                valorNuevoDialog.setTitle(R.string.edit_value);
                                                                valorNuevoField = new EditText(getActivity());
                                                                valorNuevoField.setLayoutParams(parametrosLayout);
                                                                valorNuevoDialog.setView(valorNuevoField);
                                                                valorNuevoField.setHint(getResources().getString(R.string.text_procedencia));
                                                                valorNuevoDialog.setNegativeButton(getResources().getString(R.string.aceptar_alert),
                                                                        new DialogInterface.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(DialogInterface dialog, int which) {
                                                                                try {
                                                                                    acceso = new RandomAccessFile(archivo, "rw");

                                                                                    textoNuevo = valorNuevoField.getText().
                                                                                            toString();
                                                                                    acceso.seek(getPocisionRegistro() + 54);
                                                                                    acceso.writeUTF(textoNuevo);
                                                                                    Toast.makeText(getActivity(),
                                                                                            R.string.edit_register_success,
                                                                                            Toast.LENGTH_SHORT).show();
                                                                                    acceso.close();
                                                                                    MainFragment mainFragment = new MainFragment();
                                                                                    getFragmentManager().beginTransaction().replace(R.id.container, mainFragment).commit();
                                                                                } catch (IOException e) {
                                                                                    e.printStackTrace();
                                                                                } catch (NumberFormatException e) {
                                                                                    Toast.makeText(getActivity(), getResources().getString(R.string.field_empty), Toast.LENGTH_SHORT).show();
                                                                                    e.printStackTrace();
                                                                                }
                                                                            }
                                                                        });
                                                                valorNuevoDialog.create();
                                                                valorNuevoDialog.show();
                                                                break;
                                                            case 3:
                                                                valorNuevoDialog.setTitle(R.string.edit_value);
                                                                valorNuevoField = new EditText(getActivity());
                                                                valorNuevoField.setLayoutParams(parametrosLayout);
                                                                valorNuevoDialog.setView(valorNuevoField);
                                                                valorNuevoField.setHint(getResources().getString(R.string.text_correo));
                                                                valorNuevoDialog.setNegativeButton(getResources().getString(R.string.aceptar_alert),
                                                                        new DialogInterface.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(DialogInterface dialog, int which) {
                                                                                try {
                                                                                    acceso = new RandomAccessFile(archivo, "rw");

                                                                                    textoNuevo = valorNuevoField.getText().
                                                                                            toString();
                                                                                    acceso.seek(getPocisionRegistro() + 104);
                                                                                    acceso.writeUTF(textoNuevo);
                                                                                    Toast.makeText(getActivity(),
                                                                                            R.string.edit_register_success,
                                                                                            Toast.LENGTH_SHORT).show();
                                                                                    acceso.close();
                                                                                    MainFragment mainFragment = new MainFragment();
                                                                                    getFragmentManager().beginTransaction().replace(R.id.container, mainFragment).commit();
                                                                                } catch (IOException e) {
                                                                                    e.printStackTrace();
                                                                                } catch (NumberFormatException e) {
                                                                                    Toast.makeText(getActivity(), getResources().getString(R.string.field_empty), Toast.LENGTH_SHORT).show();
                                                                                    e.printStackTrace();
                                                                                }
                                                                            }
                                                                        });
                                                                valorNuevoDialog.create();
                                                                valorNuevoDialog.show();
                                                                break;
                                                            case 4:
                                                                valorNuevoDialog.setTitle(R.string.edit_value);
                                                                valorNuevoField = new EditText(getActivity());
                                                                valorNuevoField.setLayoutParams(parametrosLayout);
                                                                valorNuevoDialog.setView(valorNuevoField);
                                                                valorNuevoField.setHint(getResources().getString(R.string.text_numero_telefonico));
                                                                valorNuevoField.setInputType(InputType.TYPE_CLASS_NUMBER);
                                                                valorNuevoDialog.setNegativeButton(getResources().getString(R.string.aceptar_alert),
                                                                        new DialogInterface.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(DialogInterface dialog, int which) {
                                                                                try {
                                                                                    acceso = new RandomAccessFile(archivo, "rw");

                                                                                    valorNuevo = Integer.parseInt(valorNuevoField.getText().
                                                                                            toString());
                                                                                    acceso.seek(getPocisionRegistro() + 154);
                                                                                    acceso.writeInt(valorNuevo);
                                                                                    Toast.makeText(getActivity(),
                                                                                            R.string.edit_register_success,
                                                                                            Toast.LENGTH_SHORT).show();
                                                                                    acceso.close();
                                                                                    MainFragment mainFragment = new MainFragment();
                                                                                    getFragmentManager().beginTransaction().replace(R.id.container, mainFragment).commit();
                                                                                } catch (IOException e) {
                                                                                    e.printStackTrace();
                                                                                } catch (NumberFormatException e) {
                                                                                    Toast.makeText(getActivity(), getResources().getString(R.string.field_empty), Toast.LENGTH_SHORT).show();
                                                                                    e.printStackTrace();
                                                                                }
                                                                            }
                                                                        });
                                                                valorNuevoDialog.create();
                                                                valorNuevoDialog.show();
                                                                break;
                                                        }
                                                    }
                                                });
                                        opcionModificaClienteDialog.create();
                                        opcionModificaClienteDialog.show();
                                    }
                                });
                            }
                        }
                        if (!encontrado) {
                            muestraDatosModificar.setText(getResources().getString(R.string.customer_dont_found));
                        }
                        encontrado = false;
                        acceso.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            });

        } else if (recibeDatos.getBoolean(MODIFICAR_LIBRO, false)) {

            subtituloModificar.setText(getResources().getString(R.string.text_titulo_modifica_libro));
            buscarField.setHint(getResources().getString(R.string.text_isbn));

            tarjetaSd = Environment.getExternalStorageDirectory();
            archivo = new File(tarjetaSd.getAbsolutePath(), "libro.dat");

            modificaButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        acceso = new RandomAccessFile(archivo, "rw");

                        acceso.seek(0);

                        isbnBusqueda = buscarField.getText().toString();
                        Log.i("", "BUSQUEDA: " + isbnBusqueda);

                        for (int i = 0; i < acceso.length(); i++) {
                            posInicial = (int) acceso.getFilePointer();
                            posRegistro = (int) acceso.getFilePointer();
                            isbn = acceso.readUTF();
                            acceso.seek(posInicial);
                            acceso.seek(acceso.getFilePointer() + 50);
                            posInicial = (int) acceso.getFilePointer();
                            nombreLibro = acceso.readUTF();
                            acceso.seek(posInicial);
                            acceso.seek(acceso.getFilePointer() + 50);
                            posInicial = (int) acceso.getFilePointer();
                            autor = acceso.readUTF();
                            acceso.seek(posInicial);
                            acceso.seek(acceso.getFilePointer() + 50);
                            posInicial = (int) acceso.getFilePointer();
                            editorial = acceso.readUTF();
                            acceso.seek(posInicial);
                            acceso.seek(acceso.getFilePointer() + 50);
                            posInicial = (int) acceso.getFilePointer();
                            genero = acceso.readUTF();
                            acceso.seek(posInicial);
                            acceso.seek(acceso.getFilePointer() + 50);
                            costo = acceso.readFloat();

                            i = (int) acceso.getFilePointer();

                            if (isbn.equals(isbnBusqueda)) {
                                setPocisionRegistro(posRegistro);
                                encontrado = true;

                                muestraDatosModificar.setText(getResources().getString(R.string.data_found) + "\n\n" +
                                        getResources().getString(R.string.text_isbn) + ": " + isbn + "\n" +
                                        getResources().getString(R.string.text_nombre_libro) + ": " + nombreLibro + "\n" +
                                        getResources().getString(R.string.text_autor) + ": " + autor + "\n" +
                                        getResources().getString(R.string.text_editorial) + ": " + editorial + "\n" +
                                        getResources().getString(R.string.text_genero) + ": " + genero + "\n" +
                                        getResources().getString(R.string.text_costo) + ": " + costo);
                                modificaButton.setText(getResources().getString(R.string.title_activity_modifica_registro));
                                modificaButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        opcionModificaLibroDialog.setTitle
                                                (getResources().getString(R.string.text_modificar_title));
                                        opciones = new CharSequence[]
                                                {getResources().getString(R.string.text_isbn),
                                                        getResources().getString(R.string.text_nombre_libro),
                                                        getResources().getString(R.string.text_autor),
                                                        getResources().getString(R.string.text_editorial),
                                                        getResources().getString(R.string.text_genero),
                                                        getResources().getString(R.string.text_costo)};
                                        opcionModificaLibroDialog.setItems(opciones,
                                                new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        switch (which) {
                                                            case 0:
                                                                valorNuevoDialog.setTitle(R.string.edit_value);
                                                                valorNuevoField = new EditText(getActivity());
                                                                valorNuevoField.setLayoutParams(parametrosLayout);
                                                                valorNuevoDialog.setView(valorNuevoField);
                                                                valorNuevoField.setHint(getResources().getString(R.string.text_isbn));
                                                                valorNuevoDialog.setNegativeButton(getResources().getString(R.string.aceptar_alert),
                                                                        new DialogInterface.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(DialogInterface dialog, int which) {
                                                                                try {
                                                                                    acceso = new RandomAccessFile(archivo, "rw");
                                                                                    textoNuevo = valorNuevoField.getText().
                                                                                            toString();
                                                                                    acceso.seek(getPocisionRegistro());
                                                                                    acceso.writeUTF(textoNuevo);
                                                                                    Toast.makeText(getActivity(),
                                                                                            R.string.edit_book_success,
                                                                                            Toast.LENGTH_SHORT).show();
                                                                                    MainFragment mainFragment = new MainFragment();
                                                                                    getFragmentManager().beginTransaction().replace(R.id.container, mainFragment).commit();

                                                                                } catch (IOException e) {
                                                                                    e.printStackTrace();
                                                                                } catch (NumberFormatException e) {
                                                                                    Toast.makeText(getActivity(), getResources().getString(R.string.field_empty), Toast.LENGTH_SHORT).show();
                                                                                    e.printStackTrace();
                                                                                }
                                                                            }
                                                                        });
                                                                valorNuevoDialog.create();
                                                                valorNuevoDialog.show();
                                                                break;
                                                            case 1:
                                                                valorNuevoDialog.setTitle(R.string.edit_value);
                                                                valorNuevoField = new EditText(getActivity());
                                                                valorNuevoField.setLayoutParams(parametrosLayout);
                                                                valorNuevoDialog.setView(valorNuevoField);
                                                                valorNuevoField.setHint(getResources().getString(R.string.text_nombre_libro));
                                                                valorNuevoDialog.setNegativeButton(getResources().getString(R.string.aceptar_alert),
                                                                        new DialogInterface.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(DialogInterface dialog, int which) {
                                                                                try {
                                                                                    acceso = new RandomAccessFile(archivo, "rw");
                                                                                    textoNuevo = valorNuevoField.getText().
                                                                                            toString();
                                                                                    acceso.seek(getPocisionRegistro() + 50);
                                                                                    acceso.writeUTF(textoNuevo);
                                                                                    Toast.makeText(getActivity(),
                                                                                            R.string.edit_book_success,
                                                                                            Toast.LENGTH_SHORT).show();
                                                                                    MainFragment mainFragment = new MainFragment();
                                                                                    getFragmentManager().beginTransaction().replace(R.id.container, mainFragment).commit();

                                                                                } catch (IOException e) {
                                                                                    e.printStackTrace();
                                                                                } catch (NumberFormatException e) {
                                                                                    Toast.makeText(getActivity(), getResources().getString(R.string.field_empty), Toast.LENGTH_SHORT).show();
                                                                                    e.printStackTrace();
                                                                                }
                                                                            }
                                                                        });
                                                                valorNuevoDialog.create();
                                                                valorNuevoDialog.show();
                                                                break;
                                                            case 2:
                                                                valorNuevoDialog.setTitle(R.string.edit_value);
                                                                valorNuevoField = new EditText(getActivity());
                                                                valorNuevoField.setLayoutParams(parametrosLayout);
                                                                valorNuevoDialog.setView(valorNuevoField);
                                                                valorNuevoField.setHint(getResources().getString(R.string.text_autor));
                                                                valorNuevoDialog.setNegativeButton(getResources().getString(R.string.aceptar_alert),
                                                                        new DialogInterface.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(DialogInterface dialog, int which) {
                                                                                try {
                                                                                    acceso = new RandomAccessFile(archivo, "rw");
                                                                                    textoNuevo = valorNuevoField.getText().
                                                                                            toString();
                                                                                    acceso.seek(getPocisionRegistro() + 100);
                                                                                    acceso.writeUTF(textoNuevo);
                                                                                    Toast.makeText(getActivity(),
                                                                                            R.string.edit_book_success,
                                                                                            Toast.LENGTH_SHORT).show();
                                                                                    acceso.close();
                                                                                    MainFragment mainFragment = new MainFragment();
                                                                                    getFragmentManager().beginTransaction().replace(R.id.container, mainFragment).commit();

                                                                                } catch (IOException e) {
                                                                                    e.printStackTrace();
                                                                                } catch (NumberFormatException e) {
                                                                                    Toast.makeText(getActivity(), getResources().getString(R.string.field_empty), Toast.LENGTH_SHORT).show();
                                                                                    e.printStackTrace();
                                                                                }
                                                                            }
                                                                        });
                                                                valorNuevoDialog.create();
                                                                valorNuevoDialog.show();
                                                                break;
                                                            case 3:
                                                                valorNuevoDialog.setTitle(R.string.edit_value);
                                                                valorNuevoField = new EditText(getActivity());
                                                                valorNuevoField.setLayoutParams(parametrosLayout);
                                                                valorNuevoDialog.setView(valorNuevoField);
                                                                valorNuevoField.setHint(getResources().getString(R.string.text_editorial));
                                                                valorNuevoDialog.setNegativeButton(getResources().getString(R.string.aceptar_alert),
                                                                        new DialogInterface.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(DialogInterface dialog, int which) {
                                                                                try {
                                                                                    acceso = new RandomAccessFile(archivo, "rw");
                                                                                    textoNuevo = valorNuevoField.getText().
                                                                                            toString();
                                                                                    acceso.seek(getPocisionRegistro() + 150);
                                                                                    acceso.writeUTF(textoNuevo);
                                                                                    Toast.makeText(getActivity(),
                                                                                            R.string.edit_book_success,
                                                                                            Toast.LENGTH_SHORT).show();
                                                                                    acceso.close();
                                                                                    MainFragment mainFragment = new MainFragment();
                                                                                    getFragmentManager().beginTransaction().replace(R.id.container, mainFragment).commit();

                                                                                } catch (IOException e) {
                                                                                    e.printStackTrace();
                                                                                } catch (NumberFormatException e) {
                                                                                    Toast.makeText(getActivity(), getResources().getString(R.string.field_empty), Toast.LENGTH_SHORT).show();
                                                                                    e.printStackTrace();
                                                                                }
                                                                            }
                                                                        });
                                                                valorNuevoDialog.create();
                                                                valorNuevoDialog.show();
                                                                break;
                                                            case 4:
                                                                opcionModificaGeneroDialog.setTitle(getResources().
                                                                        getString(R.string.selecciona_genero).toUpperCase());
                                                                opciones = new CharSequence[]{
                                                                        getResources().getString(R.string.genero_suspenso),
                                                                        getResources().getString(R.string.genero_programacion),
                                                                        getResources().getString(R.string.genero_educativo),
                                                                        getResources().getString(R.string.genero_terror),
                                                                        getResources().getString(R.string.genero_otros)
                                                                };
                                                                opcionModificaGeneroDialog.setItems(opciones, new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {
                                                                        try {
                                                                            switch (which) {
                                                                                case 0:
                                                                                    acceso = new RandomAccessFile(archivo, "rw");
                                                                                    genero = getResources().getString(R.string.genero_suspenso);
                                                                                    acceso.seek(getPocisionRegistro() + 200);
                                                                                    acceso.writeUTF(genero);
                                                                                    Toast.makeText(getActivity(),
                                                                                            R.string.edit_book_success,
                                                                                            Toast.LENGTH_SHORT).show();
                                                                                    acceso.close();
                                                                                    MainFragment mainFragment = new MainFragment();
                                                                                    getFragmentManager().beginTransaction().replace(R.id.container, mainFragment).commit();

                                                                                    break;
                                                                                case 1:
                                                                                    acceso = new RandomAccessFile(archivo, "rw");
                                                                                    genero = getResources().getString(R.string.genero_programacion);
                                                                                    acceso.seek(getPocisionRegistro() + 200);
                                                                                    acceso.writeUTF(genero);
                                                                                    Toast.makeText(getActivity(),
                                                                                            R.string.edit_book_success,
                                                                                            Toast.LENGTH_SHORT).show();
                                                                                    acceso.close();
                                                                                    MainFragment mainFragment1 = new MainFragment();
                                                                                    getFragmentManager().beginTransaction().replace(R.id.container, mainFragment1).commit();
                                                                                    break;
                                                                                case 2:
                                                                                    acceso = new RandomAccessFile(archivo, "rw");
                                                                                    genero = getResources().getString(R.string.genero_educativo);
                                                                                    acceso.seek(getPocisionRegistro() + 200);
                                                                                    acceso.writeUTF(genero);
                                                                                    Toast.makeText(getActivity(),
                                                                                            R.string.edit_book_success,
                                                                                            Toast.LENGTH_SHORT).show();
                                                                                    acceso.close();
                                                                                    MainFragment mainFragment2 = new MainFragment();
                                                                                    getFragmentManager().beginTransaction().replace(R.id.container, mainFragment2).commit();

                                                                                    break;
                                                                                case 3:
                                                                                    acceso = new RandomAccessFile(archivo, "rw");
                                                                                    genero = getResources().getString(R.string.genero_terror);
                                                                                    acceso.seek(getPocisionRegistro() + 200);
                                                                                    acceso.writeUTF(genero);
                                                                                    Toast.makeText(getActivity(),
                                                                                            R.string.edit_book_success,
                                                                                            Toast.LENGTH_SHORT).show();
                                                                                    acceso.close();
                                                                                    MainFragment mainFragment3 = new MainFragment();
                                                                                    getFragmentManager().beginTransaction().replace(R.id.container, mainFragment3).commit();

                                                                                    break;
                                                                                case 4:
                                                                                    acceso = new RandomAccessFile(archivo, "rw");
                                                                                    genero = getResources().getString(R.string.genero_otros);
                                                                                    acceso.seek(getPocisionRegistro() + 200);
                                                                                    acceso.writeUTF(genero);
                                                                                    Toast.makeText(getActivity(),
                                                                                            R.string.edit_book_success,
                                                                                            Toast.LENGTH_SHORT).show();
                                                                                    acceso.close();
                                                                                    MainFragment mainFragment5 = new MainFragment();
                                                                                    getFragmentManager().beginTransaction().replace(R.id.container, mainFragment5).commit();

                                                                                    break;
                                                                            }
                                                                        } catch (IOException e) {
                                                                            e.printStackTrace();
                                                                        }
                                                                    }
                                                                });
                                                                opcionModificaGeneroDialog.create();
                                                                opcionModificaGeneroDialog.show();
                                                                break;
                                                            case 5:
                                                                valorNuevoDialog.setTitle(R.string.edit_value);
                                                                valorNuevoField = new EditText(getActivity());
                                                                valorNuevoField.setLayoutParams(parametrosLayout);
                                                                valorNuevoDialog.setView(valorNuevoField);
                                                                valorNuevoField.setHint(getResources().getString(R.string.text_costo));
                                                                valorNuevoField.setInputType(InputType.TYPE_CLASS_NUMBER);
                                                                valorNuevoDialog.setNegativeButton(getResources().getString(R.string.aceptar_alert),
                                                                        new DialogInterface.OnClickListener() {
                                                                            @Override
                                                                            public void onClick(DialogInterface dialog, int which) {
                                                                                try {
                                                                                    acceso = new RandomAccessFile(archivo, "rw");
                                                                                    costo = Float.parseFloat(valorNuevoField.getText().
                                                                                            toString());
                                                                                    acceso.seek(getPocisionRegistro() + 250);
                                                                                    acceso.writeFloat(costo);
                                                                                    Toast.makeText(getActivity(),
                                                                                            R.string.edit_book_success,
                                                                                            Toast.LENGTH_SHORT).show();
                                                                                    acceso.close();
                                                                                    MainFragment mainFragment = new MainFragment();
                                                                                    getFragmentManager().beginTransaction().replace(R.id.container, mainFragment).commit();

                                                                                } catch (IOException e) {
                                                                                    e.printStackTrace();
                                                                                } catch (NumberFormatException e) {
                                                                                    Toast.makeText(getActivity(), getResources().getString(R.string.field_empty), Toast.LENGTH_SHORT).show();
                                                                                    e.printStackTrace();
                                                                                }
                                                                            }
                                                                        });
                                                                valorNuevoDialog.create();
                                                                valorNuevoDialog.show();
                                                                break;
                                                        }
                                                    }
                                                });
                                        opcionModificaLibroDialog.create();
                                        opcionModificaLibroDialog.show();
                                    }
                                });

                            }
                        }
                        if (!encontrado) {
                            muestraDatosModificar.setText(getResources().
                                    getString(R.string.titulo_cliente_no_encontrado_alert));
                        }
                        acceso.close();
                        encontrado = false;
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        return v;
    }

    protected int getPocisionRegistro() {
        return pocisionRegistro;
    }

    protected void setPocisionRegistro(int pocisionRegistro) {
        this.pocisionRegistro = pocisionRegistro;
    }
}
