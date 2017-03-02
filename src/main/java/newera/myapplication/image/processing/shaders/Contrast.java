package newera.myapplication.image.processing.shaders;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.renderscript.Allocation;

import newera.myapplication.MainActivity;
import newera.myapplication.R;
import newera.myapplication.ScriptC_contrast;
import newera.myapplication.ScriptC_grayscale;
import newera.myapplication.image.Image;
import newera.myapplication.image.processing.EItems;
import newera.myapplication.ui.view.CImageView;
import newera.myapplication.ui.view.inputs.InputManager;


/**
 * Created by romain on 09/02/17.
 */

public class Contrast extends Shader{

    public Contrast(Context context) {
        super(context);
        this.drawableIconId = R.drawable.ic_contrast_tonality_black_24dp;
        this.clickableName = R.string.shaderContrastName;
    }

    @Override
    public void ApplyFilter(Image image)
    {
        if(image != null && !image.isEmpty()) {

            ScriptC_contrast rsContrast = new ScriptC_contrast(renderScript);
            rsContrast.set_contrast((int) params.get("value"));

            for (Bitmap[] arrBitmap : image.getBitmaps())
                for (Bitmap bitmap : arrBitmap) {
                    Allocation in = Allocation.createFromBitmap(renderScript, bitmap);
                    Allocation out = Allocation.createTyped(renderScript, in.getType());

                    rsContrast.forEach_Contrast(in, out);
                    out.copyTo(bitmap);
                }
        }
    }

    @Override
    public int onClick(InputManager manager, CImageView view) {
        manager.createBox(EItems.F_CONTRAST, view.getResources().getString(R.string.shaderContrastName));
        view.setCurrentAction(EItems.F_CONTRAST);
        return 0;
    }

}