package br.eaj.tads.cameraapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.orm.SugarContext;

import java.io.ByteArrayOutputStream;

public class DataBaseActivity extends AppCompatActivity {

    ImageView imagemView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_base);

        imagemView = (ImageView) findViewById(R.id.imageViewBD);
        //Inicia as transações no banco de dados
        SugarContext.init(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Finaliza as transações do banco de dados
        SugarContext.terminate();
    }

    //Metodo que abre a camera do celular
    public void takePhotoAndSaveDatabase(View v){
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent,69);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 69) {
            if(resultCode == RESULT_OK){
                if(data!=null){
                    //Faz o mesmo processo que no armazenamento interno
                    Bundle extras = data.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    //Porem aqui nao terá que escrever um arquivo
                    //E sim um objeto de ByteArrayOutputStream (baos) é utilizado para transformação da foto em um array de bits
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);

                    //O banco de dados Sugar so aceita um array de bits e assim converte a imagem tirada em um array de bits
                    byte[] photo = baos.toByteArray();

                    //Objeto da classe Imagem que no caso, nessa classe existe um objeto de array de bits
                    ImageRecord imagemBanco = new ImageRecord(photo);
                    imagemBanco.save();
                    Toast.makeText(this, "Imagem salva!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    //Como no banco foi salva como um array de bits, nao é necessario ler o arquivo criado
    //Apenas cria um bitmap e decodificar aquele array de bits para ser transformado em um imageView que esta sendo setado
    public void readPhotoDatabase(View v){
        //Na linha abaixo, a imagem que vai ser mostrada é sempre a ultima que foi tirada no banco de dados
        ImageRecord imr = ImageRecord.last(ImageRecord.class);
        if(imr != null){
            Bitmap imagem = BitmapFactory.decodeByteArray(imr.getImage(),0, imr.getImage().length);
            imagemView.setImageBitmap(imagem);
        }
    }
}
