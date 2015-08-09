package zesters.sistemaderegistrofenal;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.os.Environment;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;


/**
 * A simple {@link Fragment} subclass.
 */
public class BuscaClienteFragment extends Fragment implements View.OnClickListener{


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_busca_cliente, container, false);
        subtituloBuscaCliente = (TextView) rootView.findViewById(R.id.titulo_busca_cliente);
        buscarBoton = (Button) rootView.findViewById(R.id.boton_buscar);

        busquedaText = (EditText) rootView.findViewById(R.id.edit_busca_cliente);
        muestraResultadoView = (TextView) rootView.findViewById(R.id.datos_cliente);

        buscarBoton.setOnClickListener(this);
        return rootView;
    }
    @Override
    public void onClick(View v) {
        try {
            tarjetaSd = Environment.getExternalStorageDirectory();
            archivo = new File(tarjetaSd.getAbsolutePath(), "cliente.dat");

            acceso = new RandomAccessFile(archivo, "rw");

            acceso.seek(0);
            busqueda = Integer.parseInt(busquedaText.getText().toString());

            for (int i = 0; i < acceso.length(); i++) {

                posRegistro = i;
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

                if (noCliente == busqueda) {
                    encontrado = true;

                    muestraResultadoView.setText(getResources().getString(R.string.data_found) + "\n\n" +
                            getResources().getString(R.string.customer_number) + ": " + noCliente + "\n" +
                            getResources().getString(R.string.customer_name) + ": " + nombreCliente + "\n" +
                            getResources().getString(R.string.customer_city) + ": " + procedencia + "\n" +
                            getResources().getString(R.string.customer_email) + ": " + correo + "\n" +
                            getResources().getString(R.string.customer_phone) + ": " + celular);
                }
            }
            if (!encontrado) {
                muestraResultadoView.setText(R.string.data_dont_found);
            }
            encontrado = false;
            acceso.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.field_empty), Toast.LENGTH_SHORT).show();
        }
    }
    protected int noCliente;
    protected int posInicial;
    protected int celular;
    protected int busqueda;
    protected int posRegistro;
    protected String nombreCliente;
    protected String procedencia;
    protected String correo;
    protected EditText busquedaText;
    protected TextView muestraResultadoView, subtituloBuscaCliente;
    protected boolean encontrado;
    protected Button buscarBoton;
    protected RandomAccessFile acceso;
    protected File tarjetaSd;
    protected File archivo;

}
