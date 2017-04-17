package newera.EliJ.image.processing.tools;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * Created by Emile Barjou-Suire on 09/02/2017.
 */

public abstract class Tool{
    private int standardSize;

    public void initialize(Context context)
    {

    }

    public int getStandardSize() {
        return standardSize;
    }

    public Bitmap getBitmap() {
        return null;
    }
}
