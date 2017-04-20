package newera.EliJ.image.processing.tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;

/**
 * Created by Emile Barjou-Suire on 09/02/2017.
 */

public abstract class Tool{
    protected int standardSize;

    public void initialize(Context context)
    {

    }

    public int getStandardSize() {
        return standardSize;
    }

    public Bitmap getBitmap(Paint paint) {
        return null;
    }
}
