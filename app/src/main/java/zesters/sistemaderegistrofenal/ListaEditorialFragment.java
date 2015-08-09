package zesters.sistemaderegistrofenal;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.os.Environment;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class ListaEditorialFragment extends Fragment {
    RandomAccessFile acceso;
    int l = 150, canEditoriales, repeticion;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_lista_editorial, container, false);
        ListView listaEditorial = (ListView) v.findViewById(android.R.id.list);
        try {

            File tarjetaSd = Environment.getExternalStorageDirectory();
            File archivo = new File(tarjetaSd.getAbsolutePath(), "libro.dat");

            RandomAccessFile acceso = new RandomAccessFile(archivo, "rw");

            if(acceso.length()==0){
                Toast.makeText(getActivity(), "No hay registro de libros", Toast.LENGTH_SHORT).show();
                MainFragment mainFragment = new MainFragment();
                getFragmentManager().beginTransaction().replace(R.id.container,mainFragment).commit();
            }else {
                while (l < acceso.length()) {
                    acceso.seek(l);
                    canEditoriales++;
                    l += 254;
                }


                String editoriales[] = new String[canEditoriales];

                l = 150;
                int j = 0;
                while (l < acceso.length()) {
                    acceso.seek(l);
                    editoriales[j] = acceso.readUTF();
                    l += 254;
                    j++;
                }

                ArrayAdapter<String> adaptador = new ArrayAdapter<String>(getActivity(),
                        android.R.layout.simple_list_item_1,
                        editoriales);
                listaEditorial.setAdapter(adaptador);
            }
            acceso.close();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return v;
    }


}
