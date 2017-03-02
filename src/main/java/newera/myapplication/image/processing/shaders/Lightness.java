package newera.myapplication.image.processing.shaders;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
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
    
    private Bitmap icon;

    public Lightness(MainActivity activity) {
        super(activity);
    }

    public Lightness(Context context){super(context);}

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
    public void initIcon(Context context, int iconSize) {
        Drawable drawable = context.getResources().getDrawable(R.drawable.ic_brightness_5_black_24dp);
        drawable.setColorFilter(context.getResources().getColor(R.color.colorLight), PorterDuff.Mode.SRC_ATOP);
        drawable.setBounds(0, 0, iconSize, iconSize);
        icon = Bitmap.createBitmap(iconSize, iconSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(icon);
        drawable.draw(canvas);
    }

    public String getName(){
        return activity.getResources().getString(R.string.shaderLightnessName);
    }

    @Override
    public int getNameId() {
        return R.string.shaderLightnessName;
    }

    @Override
    public Bitmap getIcon() {
        return icon;
    }

    @Override
    public int onClick(InputManager manager, CImageView view) {
        manager.createBox(EItems.F_LIGHTNESS, view.getResources().getString(R.string.shaderLightnessName));
        view.setCurrentAction(EItems.F_LIGHTNESS);
        return 0;
    }

}
