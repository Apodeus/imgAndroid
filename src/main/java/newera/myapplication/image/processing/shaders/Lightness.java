package newera.myapplication.image.processing.shaders;

import android.graphics.Bitmap;
import android.renderscript.Allocation;

import newera.myapplication.MainActivity;
import newera.myapplication.R;
import newera.myapplication.ScriptC_lightness;
import newera.myapplication.image.Image;

/**
 * Created by Romain on 19/02/2017.
 */

public class Lightness extends Shader {
    private Bitmap icone = null;

    @Override
    public void ApplyFilter(Image image)
    {
        if(image != null && !image.isEmpty()) {
            ScriptC_lightness rsLightness = new ScriptC_lightness(renderScript);

            for (Bitmap[] arrBitmap : image.getBitmaps())
                for (Bitmap bitmap : arrBitmap) {
                    Allocation in = Allocation.createFromBitmap(renderScript, bitmap);
                    Allocation out = Allocation.createTyped(renderScript, in.getType());
                    rsLightness.forEach_ChangeLightness(in, out);
                    out.copyTo(bitmap);
                }
        }
        refreshImage();
    }

    public Lightness(MainActivity activity) {
        super(activity);
    }

    public String getName(){
        return activity.getResources().getString(R.string.shaderLightnessName);
    }

    @Override
    public int getNameId() {
        return 0;
    }

    @Override
    public Bitmap getIcone() {
        return icone;
    }

}
