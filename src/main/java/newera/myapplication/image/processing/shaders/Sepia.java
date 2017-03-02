package newera.myapplication.image.processing.shaders;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.renderscript.Allocation;

import newera.myapplication.MainActivity;
import newera.myapplication.R;
import newera.myapplication.ScriptC_sepia;
import newera.myapplication.image.Image;


/**
 * Created by romain on 09/02/17.
 */

public class Sepia extends Shader{

    public Sepia(Context activity) {
        super(activity);
        this.drawableIconId = R.drawable.ic_photo_filter_sepia_24dp;
        this.clickableName = R.string.shaderSepiaName;
    }

    @Override
    public void ApplyFilter(Image image)
    {
        if(image != null && !image.isEmpty()) {
            ScriptC_sepia rsSepia = new ScriptC_sepia(renderScript);

            for (Bitmap[] arrBitmap : image.getBitmaps())
                for (Bitmap bitmap : arrBitmap) {
                    Allocation in = Allocation.createFromBitmap(renderScript, bitmap);
                    Allocation out = Allocation.createTyped(renderScript, in.getType());

                    rsSepia.forEach_sepia(in, out);
                    out.copyTo(bitmap);
                }
        }
        refreshImage();
    }

}