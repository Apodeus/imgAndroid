package newera.myapplication.image;

import android.graphics.Bitmap;

/**
 * Created by Emile Barjou-Suire on 09/02/2017.
 */

public class Image {
    private Bitmap bitmap;

    Bitmap getBitmap(){
        return bitmap;
    }

    boolean isEmpty(){
        return bitmap == null;
    }
}
