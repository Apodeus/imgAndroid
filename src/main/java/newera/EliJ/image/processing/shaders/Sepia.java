package newera.EliJ.image.processing.shaders;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v8.renderscript.Allocation;

import newera.EliJ.R;
import newera.EliJ.ScriptC_sepia;
import newera.EliJ.image.Image;
import newera.EliJ.image.processing.EItems;


/**
 * Created by romain on 09/02/17.
 */

public class Sepia extends Shader{

    public Sepia(Context activity) {
        super(activity);
        this.drawableIconId = R.drawable.ic_photo_filter_sepia_24dp;
        this.clickableName = R.string.shaderSepiaName;
        this.item = EItems.F_SEPIA;
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
        //refreshImage();
    }

}