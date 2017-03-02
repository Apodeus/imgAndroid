package newera.EliJ.image.processing.shaders;

import android.content.Context;
import android.graphics.Bitmap;
import android.renderscript.Allocation;

import newera.EliJ.R;
import newera.EliJ.ScriptC_hue;
import newera.EliJ.image.Image;
import newera.EliJ.image.processing.EItems;
import newera.EliJ.ui.view.CImageView;
import newera.EliJ.ui.view.inputs.InputManager;

import java.util.Map;

/**
 * Created by Romain on 19/02/2017.
 */

public class ChangeHue extends Shader {

    public ChangeHue(Context context)
    {
        super(context);
        this.drawableIconId = R.drawable.ic_hue_color_lens_black_24dp;
        this.clickableName = R.string.shaderChangeHueName;
    }


    @Override
    public void ApplyFilter(Image image)
    {
        if(image != null && !image.isEmpty()) {

            ScriptC_hue rsChangeHue = new ScriptC_hue(renderScript);
            rsChangeHue.set_newHue((int) params.get("value"));

            for (Bitmap[] arrBitmap : image.getBitmaps())
                for (Bitmap bitmap : arrBitmap) {
                    Allocation in = Allocation.createFromBitmap(renderScript, bitmap);
                    Allocation out = Allocation.createTyped(renderScript, in.getType());
                    rsChangeHue.forEach_ChangeHue(in, out);
                    out.copyTo(bitmap);
                }
        }
    }

    public void ApplyPreviewFilter(Image image, Object param)
    {
        //!! Overwrite current image even if canceled !!
        params = (Map<String, Object>) param;
        ApplyFilter(image);
    }

    @Override
    public int onClick(InputManager manager, CImageView view) {
        manager.createBox(EItems.F_CHANGE_HUE, view.getResources().getString(R.string.shaderChangeHueName));
        view.setCurrentAction(EItems.F_CHANGE_HUE);
        return 0;
    }

}
