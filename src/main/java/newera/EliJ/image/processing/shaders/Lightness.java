package newera.EliJ.image.processing.shaders;

import android.content.Context;
import android.graphics.Bitmap;
import android.renderscript.Allocation;

import newera.EliJ.R;
import newera.EliJ.ScriptC_lightness;
import newera.EliJ.image.Image;
import newera.EliJ.image.processing.EItems;
import newera.EliJ.ui.view.CImageView;
import newera.EliJ.ui.view.inputs.InputManager;

/**
 * Created by Romain on 19/02/2017.
 */

public class Lightness extends Shader {

    public Lightness(Context context){
        super(context);
        this.drawableIconId = R.drawable.ic_brightness_5_black_24dp;
        this.clickableName = R.string.shaderLightnessName;
    }

    @Override
    public void ApplyFilter(Image image)
    {
        if(image != null && !image.isEmpty()) {
            ScriptC_lightness rsLightness = new ScriptC_lightness(renderScript);
            rsLightness.set_brightness( ((int) params.get("value") + 50) * (1f/50f) );

            for (Bitmap[] arrBitmap : image.getBitmaps())
                for (Bitmap bitmap : arrBitmap) {
                    Allocation in = Allocation.createFromBitmap(renderScript, bitmap);
                    Allocation out = Allocation.createTyped(renderScript, in.getType());

                    rsLightness.forEach_ChangeLightness(in, out);
                    out.copyTo(bitmap);
                }
        }
    }

    @Override
    public int onClick(InputManager manager, CImageView view) {
        manager.createBox(EItems.F_LIGHTNESS, view.getResources().getString(R.string.shaderLightnessName));
        view.setCurrentAction(EItems.F_LIGHTNESS);
        return 0;
    }

}
