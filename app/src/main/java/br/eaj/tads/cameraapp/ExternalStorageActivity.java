package br.eaj.tads.cameraapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class ExternalStorageActivity extends AppCompatActivity {

    String FILENAME = "fotoExterna.jpg";
    private String pictureImagePath = "";
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_external_storage);

        imageView = (ImageView) findViewById(R.id.imageViewExterno);
    }

    //Metodo para criar um tipo de arquivo no armazenamento externo
    private File createImageFile() throws IOException {
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "Camera");
        pictureImagePath = storageDir.getAbsolutePath()+"/"+FILENAME;
        File image = new File(pictureImagePath);
        return image;
    }

    //Metodo de inicializar a camera, porem com algumas mudanças devido ao APK no AndroidManifests existe um objeto
    //Esse objeto é devido ao Android Nougart, pois o arquivo uris não pode mais ser exposto.
    //E assim utiliza um provedor de arquivos para o Android
    public void takePhotoAndSaveExternally(View v) throws IOException {
        if (checkStorage() == false){
            return;
        }else {
            File file = createImageFile();
            Uri outputFileUri = FileProvider.getUriForFile(ExternalStorageActivity.this, BuildConfig.APPLICATION_ID + ".provider", file);
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            startActivityForResult(cameraIntent,1);
        }
    }
    //Metodo responsavel por ler a imagem que foi tirada e ser decodificada (BitmapFactory.decodeFile) e assim setada no imageView
    public void readPhotoExternally(View v) throws IOException {
        if (checkStorage() == false){
            Toast.makeText(this, "O armazenamento está ocupado", Toast.LENGTH_SHORT).show();
            return;
        }else {
            File imgFile = createImageFile();

            if(imgFile.exists()){
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                ImageView myImage = (ImageView) findViewById(R.id.imageViewExterno);
                myImage.setImageBitmap(myBitmap);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Metodo para retornar se a foto foi salva corretamente, ele dê esse TOAST
        if (requestCode == 1) {
            if(resultCode == RESULT_OK){
                Toast.makeText(this, "Imagem salva!", Toast.LENGTH_SHORT).show();
            }
        }

    }

    //Metodo responsavel por verificar a disponibilidade do android em relaçao a gravar e a ler arquivo no armazenamento externo
    public boolean checkStorage(){
        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
            return false;
        }
        return true;
    }


    private static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    private static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
    }

}
