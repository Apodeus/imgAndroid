package newera.EliJ.image.processing.shaders;

import android.graphics.Bitmap;
import android.renderscript.Allocation;

import newera.EliJ.MainActivity;
import newera.EliJ.R;
import newera.EliJ.ScriptC_invert;
import newera.EliJ.image.Image;

/**
 * Created by Romain on 18/02/2017.
 */

public class InvertColor extends Shader {

    public InvertColor(MainActivity activity) {
        super(activity);
        this.drawableIconId = R.drawable.ic_invert_color_24dp;
        this.clickableName = R.string.shaderInvertColorName;
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
        refreshImage();
    }

}
