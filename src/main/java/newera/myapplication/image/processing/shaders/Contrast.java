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

    private Bitmap icon;

    public Contrast(MainActivity activity) {
        super(activity);
    }
    public Contrast(Context context) {
        super(context);
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
    public void initIcon(Context context, int iconSize) {
        Drawable drawable = context.getResources().getDrawable(R.drawable.ic_contrast_tonality_black_24dp);
        drawable.setColorFilter(context.getResources().getColor(R.color.colorLight), PorterDuff.Mode.SRC_ATOP);
        drawable.setBounds(0, 0, iconSize, iconSize);
        icon = Bitmap.createBitmap(iconSize, iconSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(icon);
        drawable.draw(canvas);
    }

    public String getName()
    {
        return activity.getResources().getString(R.string.shaderContrastName);
    }

    @Override
    public int getNameId() {
        return R.string.shaderContrastName;
    }

    @Override
    public Bitmap getIcon() {
        return icon;
    }

    @Override
    public int onClick(InputManager manager, CImageView view) {
        manager.createBox(EItems.F_CONTRAST, view.getResources().getString(R.string.shaderContrastName));
        view.setCurrentAction(EItems.F_CONTRAST);
        return 0;
    }

}