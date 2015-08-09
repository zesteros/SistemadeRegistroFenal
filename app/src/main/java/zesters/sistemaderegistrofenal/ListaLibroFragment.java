package zesters.sistemaderegistrofenal;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.os.*;
import android.widget.*;

import java.io.*;
import java.util.Arrays;

public class ListaLibroFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_lista_libro, container, false);
        try {
            ListView listaLibros = (ListView) v.findViewById(android.R.id.list);
            int posInicial;

            File tarjetaSd = Environment.getExternalStorageDirectory();
            File archivo = new File(tarjetaSd.getAbsolutePath(), "libro.dat");


            RandomAccessFile acceso = new RandomAccessFile(archivo, "rw");

            acceso.seek(0);
            if (acceso.length() == 0) {
                Toast.makeText(getActivity(), "No hay registro de libros", Toast.LENGTH_SHORT).show();
                MainFragment mainFragment = new MainFragment();
                getFragmentManager().beginTransaction().replace(R.id.container, mainFragment).commit();
            } else {
                int k = 0;
                int i = 0;
                int r = (int) (acceso.length() / 254);

                String[] isbn = new String[r];
                String[] nombreLibro = new String[r];
                String[] autor = new String[r];
                String[] editorial= new String[r];
                String[] genero= new String[r];
                float [] costo= new float[r];

                while (i < acceso.length()) {
                    posInicial = (int) acceso.getFilePointer();
                    isbn[k] = (getResources().getString(R.string.number_register) + " " +
                            (k + 1) + "\n" + getResources().getString(R.string.text_isbn)+
                            ": " + acceso.readUTF() + " \n");
                    acceso.seek(posInicial);
                    acceso.seek(acceso.getFilePointer() + 50);
                    posInicial = (int) acceso.getFilePointer();
                    nombreLibro[k] = getResources().getString(R.string.text_nombre_libro)+
                            ": " + acceso.readUTF() + " \n";
                    acceso.seek(posInicial);
                    acceso.seek(acceso.getFilePointer() + 50);
                    posInicial = (int) acceso.getFilePointer();
                    autor[k] = getResources().getString(R.string.text_autor)+
                            ": " + acceso.readUTF() + " \n";
                    acceso.seek(posInicial);
                    acceso.seek(acceso.getFilePointer() + 50);
                    posInicial = (int) acceso.getFilePointer();
                    editorial[k] = getResources().getString(R.string.text_editorial) +
                            ": " + acceso.readUTF() + " \n";
                    acceso.seek(posInicial);
                    acceso.seek(acceso.getFilePointer() + 50);
                    posInicial = (int) acceso.getFilePointer();
                    genero[k] = getResources().getString(R.string.text_genero)+
                            ": " + acceso.readUTF() + " \n";
                    acceso.seek(posInicial);
                    acceso.seek(acceso.getFilePointer() + 50);
                    costo[k] = acceso.readFloat();
                    i = (int) acceso.getFilePointer();
                    k++;
                }
                AdaptadorListaLibros adaptador = new AdaptadorListaLibros(getActivity(),
                        isbn, nombreLibro, autor, editorial, genero, costo);
                listaLibros.setAdapter(adaptador);
                acceso.close();
            }

        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return v;
    }
    public class AdaptadorListaLibros extends BaseAdapter{
        Context context;
        String[] isbn;
        String[] nombreLibro;
        String[] autor;
        String[] editorial;
        String[] genero;
        float[] costo;
        LayoutInflater inflater;

        public AdaptadorListaLibros(Context context,
                                    String[] isbn,
                                    String[] nombreLibro,
                                    String[] autor,
                                    String[] editorial,
                                    String[] genero,
                                    float[]costo){
            this.context = context;
            this.isbn = isbn;
            this.nombreLibro = nombreLibro;
            this.autor = autor;
            this.editorial = editorial;
            this.genero = genero;
            this.costo = costo;
        }

        @Override
        public int getCount() {
            return isbn.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView isbnT;
            TextView nombreLibroT;
            TextView autorT;
            TextView editorialT;
            TextView generoT;
            TextView costoT;

            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View itemView = inflater.inflate(R.layout.lista_libro_row, parent, false);

            isbnT = (TextView) itemView.findViewById(R.id.isbn);
            nombreLibroT = (TextView) itemView.findViewById(R.id.nombre_libro);
            autorT = (TextView) itemView.findViewById(R.id.autor);
            editorialT = (TextView) itemView.findViewById(R.id.editorial);
            generoT = (TextView) itemView.findViewById(R.id.genero);
            costoT = (TextView) itemView.findViewById(R.id.costo);

            isbnT.setText(isbn[position]);
            nombreLibroT.setText(nombreLibro[position]);
            autorT.setText(autor[position]);
            editorialT.setText(editorial[position]);
            generoT.setText(genero[position]);
            costoT.setText(getResources().getString(R.string.text_costo)+": "+costo[position]);
            return itemView;
        }
    }


}