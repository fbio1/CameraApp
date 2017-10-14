package br.eaj.tads.cameraapp;

import com.orm.SugarRecord;

public class ImageRecord extends SugarRecord {

    private byte[] image;

    public ImageRecord() {
    }

    public ImageRecord(byte[] image) {
        this.image = image;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
