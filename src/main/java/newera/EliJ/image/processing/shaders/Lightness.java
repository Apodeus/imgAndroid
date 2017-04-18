package newera.EliJ.image.processing.shaders;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v8.renderscript.Allocation;

import newera.EliJ.R;
import newera.EliJ.ScriptC_lightness;
import newera.EliJ.image.Image;
import newera.EliJ.image.processing.EItems;

/**
 * Created by Romain on 19/02/2017.
 */

public class Lightness extends Shader {

    public Lightness(Context context){
        super(context);
        this.drawableIconId = R.drawable.ic_brightness_5_black_24dp;
        this.clickableName = R.string.shaderLightnessName;
        this.item = EItems.F_LIGHTNESS;
    }

    @Override
    public void ApplyFilter(Image image)
    {
        if(image != null && !image.isEmpty()) {
            ScriptC_lightness rsLightness = new ScriptC_lightness(renderScript);
            int bordersup = ((int[]) params.get("borders"))[1];
            rsLightness.set_brightness(((int) params.get("value") + ((float)bordersup)) * (1f/((float)bordersup)) );

            for (Bitmap[] arrBitmap : image.getBitmaps())
                for (Bitmap bitmap : arrBitmap) {
                    Allocation in = Allocation.createFromBitmap(renderScript, bitmap);
                    Allocation out = Allocation.createTyped(renderScript, in.getType());

                    rsLightness.forEach_ChangeLightness(in, out);
                    out.copyTo(bitmap);
                }
        }
    }

}
