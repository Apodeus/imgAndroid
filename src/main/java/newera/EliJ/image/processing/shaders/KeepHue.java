package newera.EliJ.image.processing.shaders;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v8.renderscript.Allocation;

import newera.EliJ.R;
import newera.EliJ.ScriptC_keepHue;
import newera.EliJ.image.Image;
import newera.EliJ.image.processing.EItems;

/**
 * Created by Romain on 21/02/2017.
 */

public class KeepHue extends Shader{

    public KeepHue(Context context) {
        super(context);
        this.drawableIconId = R.drawable.ic_keep_hue_colorize_black_24dp;
        this.clickableName = R.string.shaderKeepHueName;
        this.item = EItems.F_KEEP_HUE;
    }

    @Override
    public void ApplyFilter(Image image)
    {
        if(image != null && !image.isEmpty()) {
            ScriptC_keepHue rsKeepHue = new ScriptC_keepHue(renderScript);

            rsKeepHue.set_newHue( ((int) params.get("valueHue") * (1f/360f)) );
            rsKeepHue.set_epsilon( ((int) params.get("valueTolerance") * (1f/360f)) );

            for (Bitmap[] arrBitmap : image.getBitmaps())
                for (Bitmap bitmap : arrBitmap) {
                    Allocation in = Allocation.createFromBitmap(renderScript, bitmap);
                    Allocation out = Allocation.createTyped(renderScript, in.getType());

                    rsKeepHue.forEach_KeepSpecificHue(in, out);
                    out.copyTo(bitmap);
                }
        }
    }

}
