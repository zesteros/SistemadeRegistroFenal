package zesters.sistemaderegistrofenal;


import android.os.Bundle;
import android.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.support.v7.app.ActionBar;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class AgregaClienteFragment extends Fragment implements View.OnClickListener{

    protected int noCliente;
    protected int celular;
    protected int posInicioRegistro;
    protected int posInicial;
    protected int posFinal;
    protected String nombre;
    protected String ciudad;
    protected String correo;
    protected EditText numeroClienteText;
    protected EditText nombreClienteText;
    protected EditText ciudadProcedenciaText;
    protected EditText correoText, celularText;
    protected RandomAccessFile acceso;
    protected Button aceptarCliente;
    protected File tarjetaSd;
    protected File archivo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_agrega_cliente, container, false);
        try {
            aceptarCliente = (Button) rootView.findViewById(R.id.aceptar_cliente);
            numeroClienteText = (EditText) rootView.findViewById(R.id.numero_cliente);
            nombreClienteText = (EditText) rootView.findViewById(R.id.nombre_cliente);
            ciudadProcedenciaText = (EditText) rootView.findViewById(R.id.ciudad_procedencia);
            correoText = (EditText) rootView.findViewById(R.id.correo);
            celularText = (EditText) rootView.findViewById(R.id.celular);
            aceptarCliente.setOnClickListener(this);

        } catch(NumberFormatException e){
            Log.e("ERROR", e.getMessage(), e);
        }
        return rootView;
    }
    @Override
    public void onClick(View v) {
        try {
            noCliente = Integer.parseInt(numeroClienteText.getText().toString());
            nombre = nombreClienteText.getText().toString();
            ciudad = ciudadProcedenciaText.getText().toString();
            correo = correoText.getText().toString();
            celular = Integer.parseInt(celularText.getText().toString());

            tarjetaSd = Environment.getExternalStorageDirectory();
            archivo = new File(tarjetaSd.getAbsolutePath(),"cliente.dat");
            acceso = new RandomAccessFile(archivo, "rw");
            posInicioRegistro = (int) acceso.length();

            acceso.seek(posInicioRegistro);

            acceso.writeInt(noCliente);

            posInicial = (int) acceso.getFilePointer();
            for (int i = 0; i < 50; i++) {
                acceso.writeByte(0);
            }
            posFinal = (int) acceso.getFilePointer();
            acceso.seek(posInicial);
            acceso.writeUTF(nombre);
            acceso.seek(posFinal);

            posInicial = (int) acceso.getFilePointer();
            for (int i = 0; i < 50; i++) {
                acceso.writeByte(0);
            }
            posFinal = (int) acceso.getFilePointer();
            acceso.seek(posInicial);
            acceso.writeUTF(ciudad);
            acceso.seek(posFinal);

            posInicial = (int) acceso.getFilePointer();
            for (int i = 0; i < 50; i++) {
                acceso.writeByte(0);
            }
            posFinal = (int) acceso.getFilePointer();
            acceso.seek(posInicial);
            acceso.writeUTF(correo);
            acceso.seek(posFinal);

            acceso.writeInt(celular);

            acceso.close();

            Toast.makeText(getActivity().getApplicationContext(),
                    getResources().getString(R.string.add_register_success),
                    Toast.LENGTH_SHORT).show();
            MainFragment mainFragment = new MainFragment();
            getFragmentManager().beginTransaction().replace(R.id.container,mainFragment).commit();

        } catch (IOException e) {
            Log.e("ERROR", e.getMessage(), e);
            Toast.makeText(getActivity().getApplicationContext(),
                    getResources().getString(R.string.field_empty),
                    Toast.LENGTH_SHORT).show();
        } catch(NumberFormatException n){
            Log.e("ERROR", n.getMessage(), n);
            Toast.makeText(getActivity().getApplicationContext(),
                    getResources().getString(R.string.field_empty),
                    Toast.LENGTH_SHORT).show();
        }
    }
}
