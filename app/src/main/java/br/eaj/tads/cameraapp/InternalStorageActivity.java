package br.eaj.tads.cameraapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class InternalStorageActivity extends AppCompatActivity {

    String FILENAME = "fotoInterna.jpg";
    ImageView imgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internal_storage);

        imgView = (ImageView) findViewById(R.id.imageViewInterno);
    }

    //Metodo que abre a camera do celular
    public void takePhotoAndSaveInternally(View v){
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent,69);
    }

    //Metodo de retorno do startActivityForResult que vai verificar se deu bom, e se deu bom
    //Se sim, vai pega a data passada pelo bundle e criar um objeto de FileOutputStream
    //E logo em seguida vai fazer um compress da imagem com 85% de qualidade
    //E mostra um toast
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 69) {
            if(resultCode == RESULT_OK){

                if(data!=null){
                    Bundle extras = data.getExtras();
                    //Cria-se um Bitmap que esta pegando pelo bundle a imagem que foi tirada
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    //FileOutputStream serve para escrever um arquivo, que no caso seria uma imagem
                    FileOutputStream outputStream = null;
                    try {
                        //abrir o arquivo escrito passando como parametro o nome e o contexto
                        outputStream = openFileOutput(FILENAME, Context.MODE_PRIVATE);;
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 85, outputStream);
                    try {
                        //metodo que garante o envio do último lote de bytes enviados para gravação
                        outputStream.flush();
                        //metodo que fecha a stram de leitura ou gravação
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(this, "Imagem salva com sucesso!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    //Metodo que vai pegar o arquivo criado lá em cima
    //Logo em seguida vai criar uma instancia de BitmapFactory (objeto que tem o objetivo de ler um arquivo ou bit-arrays)
    //e depois seta esse bitmap criado no imageView
    public void readPhotoInternally(View v){
        FileInputStream fin ;
        try {
            fin = openFileInput(FILENAME);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeStream(fin,null, options);
            imgView.setImageBitmap(bitmap);
            fin.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
