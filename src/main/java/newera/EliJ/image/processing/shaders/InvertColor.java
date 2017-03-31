package newera.EliJ.image.processing.shaders;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v8.renderscript.Allocation;

import newera.EliJ.R;
import newera.EliJ.ScriptC_invert;
import newera.EliJ.image.Image;
import newera.EliJ.image.processing.EItems;

/**
 * Created by Romain on 18/02/2017.
 */

public class InvertColor extends Shader {

    public InvertColor(Context context) {
        super(context);
        this.drawableIconId = R.drawable.ic_invert_color_24dp;
        this.clickableName = R.string.shaderInvertColorName;
        this.item = EItems.F_INVERT_COLOR;
    }

    @Override
    public void ApplyFilter(Image image)
    {
        if(image != null && !image.isEmpty()) {
            ScriptC_invert rsInvert = new ScriptC_invert(renderScript);

            for (Bitmap[] arrBitmap : image.getBitmaps())
                for (Bitmap bitmap : arrBitmap) {
                    Allocation in = Allocation.createFromBitmap(renderScript, bitmap);
                    Allocation out = Allocation.createTyped(renderScript, in.getType());
                    rsInvert.forEach_invert(in, out);
                    out.copyTo(bitmap);
                }
        }
    }

}
