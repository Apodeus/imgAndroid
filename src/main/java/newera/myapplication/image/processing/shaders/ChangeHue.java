package newera.myapplication.image.processing.shaders;

import android.content.Context;
import android.graphics.Bitmap;
import android.renderscript.Allocation;

import android.renderscript.Sampler;
import newera.myapplication.MainActivity;
import newera.myapplication.R;
import newera.myapplication.ScriptC_hue;
import newera.myapplication.ScriptC_lightness;
import newera.myapplication.image.Image;
import newera.myapplication.image.processing.EItems;
import newera.myapplication.ui.view.CImageView;
import newera.myapplication.ui.view.InputManager;
import newera.myapplication.ui.view.inputs.EInputBox;
import newera.myapplication.ui.view.inputs.EInputType;

import java.util.Map;

/**
 * Created by Romain on 19/02/2017.
 */

public class ChangeHue extends Shader {
    private Bitmap icone = null;

    @Override
    public void ApplyFilter(Image image)
    {
        if(image != null && !image.isEmpty()) {

            ScriptC_hue rsChangeHue = new ScriptC_hue(renderScript);
            rsChangeHue.set_factor(((int) params.get("value") * (1f/360f)));
            for (Bitmap[] b1 : image.getBitmaps())
                for (Bitmap b : b1) {
                    Allocation in = Allocation.createFromBitmap(renderScript, b);
                    Allocation out = Allocation.createTyped(renderScript, in.getType());
                    rsChangeHue.forEach_ChangeHue(in, out);
                    out.copyTo(b);
                }
        }
        //refreshImage();
    }

    public void ApplyPreviewFilter(Image image, Object param)
    {
        //!! Overwrite current image even if canceled !!
        params = (Map<String, Object>) param;
        ApplyFilter(image);
    }

    public ChangeHue(MainActivity activity) {
        super(activity);
    }

    public ChangeHue(Context context)
    {
        super(context);
    }

    public String getName(){
        return activity.getResources().getString(R.string.shaderChangeHueName);
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
        manager.createBox(EInputBox.INTEGER, "Hue", new int[] {0, 360, 0});
        view.setCurrentAction(EInputType.SHADER, EItems.F_CHANGE_HUE);
        return 0;
    }

}
