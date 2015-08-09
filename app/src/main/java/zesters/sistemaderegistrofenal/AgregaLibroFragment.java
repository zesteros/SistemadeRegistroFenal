package zesters.sistemaderegistrofenal;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.os.Environment;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class AgregaLibroFragment extends Fragment implements View.OnClickListener {
    protected EditText isbnText;
    protected EditText nombreLibroText;
    protected EditText autorText;
    protected EditText editorialText;
    protected EditText costoText;
    protected Button anadeLibroBoton;
    protected RadioGroup generoSeleccionado;
    protected String ISBN;
    protected String nombreLibro;
    protected String autor;
    protected String editorial;
    protected String genero = "Otros";
    protected float costo;
    protected int posInicioRegistro;
    protected int posInicial;
    protected int posFinal;
    protected RandomAccessFile acceso;
    protected File tarjetaSd;
    protected File archivo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_agrega_libro, container, false);
        try {
            isbnText = (EditText) view.findViewById(R.id.edit_isbn);
            nombreLibroText = (EditText) view.findViewById(R.id.edit_nombre_libro);
            autorText = (EditText) view.findViewById(R.id.edit_autor);
            editorialText = (EditText) view.findViewById(R.id.edit_editorial);
            costoText = (EditText) view.findViewById(R.id.edit_costo);

            anadeLibroBoton = (Button) view.findViewById(R.id.anade_libro_button);
            generoSeleccionado = (RadioGroup) view.findViewById(R.id.generos);
            generoSeleccionado.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch (checkedId) {
                        case R.id.radio_button_suspenso:
                            genero = getResources().getString(R.string.genero_suspenso);
                            break;
                        case R.id.radio_button_terror:
                            genero = getResources().getString(R.string.genero_terror);
                            break;
                        case R.id.radio_button_educativo:
                            genero = getResources().getString(R.string.genero_educativo);
                            break;
                        case R.id.radio_button_programacion:
                            genero = getResources().getString(R.string.genero_programacion);
                            break;
                        default:
                            Toast.makeText(getActivity(), getResources().getString(R.string.field_empty), Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            });

            anadeLibroBoton.setOnClickListener(this);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), getResources().getString(R.string.field_empty), Toast.LENGTH_SHORT).show();
        }
        return view;
    }

    @Override
    public void onClick(View v) {
        try {
            ISBN = isbnText.getText().toString();
            nombreLibro = nombreLibroText.getText().toString();
            autor = autorText.getText().toString();
            editorial = editorialText.getText().toString();
            costo = Float.parseFloat(costoText.getText().toString());

            tarjetaSd = Environment.getExternalStorageDirectory();
            archivo = new File(tarjetaSd.getAbsolutePath(), "libro.dat");

            acceso = new RandomAccessFile(archivo, "rw");

            posInicioRegistro = (int) acceso.length();

            acceso.seek(posInicioRegistro);

            for (int i = 0; i < 50; i++) {
                acceso.writeByte(0);
            }
            posFinal = (int) acceso.getFilePointer();
            acceso.seek(posInicioRegistro);
            acceso.writeUTF(ISBN);
            acceso.seek(posFinal);

            posInicial = (int) acceso.getFilePointer();
            for (int i = 0; i < 50; i++) {
                acceso.writeByte(0);
            }
            posFinal = (int) acceso.getFilePointer();
            acceso.seek(posInicial);
            acceso.writeUTF(nombreLibro);
            acceso.seek(posFinal);

            posInicial = (int) acceso.getFilePointer();
            for (int i = 0; i < 50; i++) {
                acceso.writeByte(0);
            }
            posFinal = (int) acceso.getFilePointer();
            acceso.seek(posInicial);
            acceso.writeUTF(autor);
            acceso.seek(posFinal);

            posInicial = (int) acceso.getFilePointer();
            for (int i = 0; i < 50; i++) {
                acceso.writeByte(0);
            }
            posFinal = (int) acceso.getFilePointer();
            acceso.seek(posInicial);
            acceso.writeUTF(editorial);
            acceso.seek(posFinal);

            posInicial = (int) acceso.getFilePointer();
            for (int i = 0; i < 50; i++) {
                acceso.writeByte(0);
            }
            posFinal = (int) acceso.getFilePointer();
            acceso.seek(posInicial);
            acceso.writeUTF(genero);
            acceso.seek(posFinal);

            acceso.writeFloat(costo);

            Toast.makeText(getActivity(), getResources().getString(R.string.add_book_success), Toast.LENGTH_SHORT).show();

            MainFragment mainFragment = new MainFragment();
            getFragmentManager().beginTransaction().replace(R.id.container, mainFragment).commit();

            acceso.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), getResources().getString(R.string.field_empty), Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), getResources().getString(R.string.field_empty), Toast.LENGTH_SHORT).show();

        } catch (NumberFormatException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), getResources().getString(R.string.field_empty), Toast.LENGTH_SHORT).show();
        }
    }
}
