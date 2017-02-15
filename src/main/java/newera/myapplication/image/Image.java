package newera.myapplication.image;

import android.graphics.Bitmap;

/**
 * Created by Emile Barjou-Suire on 09/02/2017.
 */

public class Image {
    private Bitmap bitmap;


    /**
     * @return bitmap's reference
     */
    public Bitmap getBitmap(){
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap){
        if(bitmap == null)
            return;
        this.bitmap = bitmap.copy(bitmap.getConfig(), bitmap.isMutable());
    }

    public boolean isEmpty(){
        return bitmap == null;
    }

    public int getWidth()
    {
        return bitmap.getWidth();
    }

    public int getHeight()
    {
        return bitmap.getHeight();
    }
}
