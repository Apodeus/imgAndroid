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
            for (Bitmap[] b1 : image.getBitmaps())
                for (Bitmap b : b1) {
                    Allocation in = Allocation.createFromBitmap(renderScript, b);
                    Allocation out = Allocation.createTyped(renderScript, in.getType());
                    ScriptC_lightness rsLightness = new ScriptC_lightness(renderScript);
                    rsLightness.forEach_ChangeLightness(in, out);
                    out.copyTo(b);
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
