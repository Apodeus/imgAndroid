package newera.myapplication.image;

import android.graphics.Bitmap;

/**
 * Created by Emile Barjou-Suire on 09/02/2017.
 */

public class Image {
    private Bitmap bitmap;

    public Bitmap getBitmap(){
        return bitmap;
    }

    public boolean isEmpty(){
        return bitmap == null;
    }
}
