package newera.myapplication.image.processing.shaders;

import android.content.Context;
import android.graphics.Bitmap;
import android.renderscript.Allocation;

import newera.myapplication.MainActivity;
import newera.myapplication.R;
import newera.myapplication.ScriptC_lightness;
import newera.myapplication.image.Image;
import newera.myapplication.image.processing.EItems;
import newera.myapplication.ui.view.CImageView;
import newera.myapplication.ui.view.inputs.InputManager;

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
            rsLightness.set_factor(((int) params.get("value") * (1f/100f)));

            for (Bitmap[] arrBitmap : image.getBitmaps())
                for (Bitmap bitmap : arrBitmap) {
                    Allocation in = Allocation.createFromBitmap(renderScript, bitmap);
                    Allocation out = Allocation.createTyped(renderScript, in.getType());
                    rsLightness.forEach_ChangeLightness(in, out);
                    out.copyTo(bitmap);
                }
        }
        //refreshImage();
    }

    public Lightness(MainActivity activity) {
        super(activity);
    }

    public Lightness(Context context){super(context);}

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

    @Override
    public int onClick(InputManager manager, CImageView view) {
        manager.createBox(EItems.F_LIGHTNESS, view.getResources().getString(R.string.shaderLightnessName));
        view.setCurrentAction(EItems.F_LIGHTNESS);
        return 0;
    }

}
