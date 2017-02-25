package newera.myapplication.image.processing.shaders;

import android.content.Context;
import android.graphics.Bitmap;
import android.renderscript.Allocation;

import newera.myapplication.MainActivity;
import newera.myapplication.R;
import newera.myapplication.ScriptC_keepHue;
import newera.myapplication.image.Image;
import newera.myapplication.image.processing.EItems;
import newera.myapplication.ui.view.CImageView;
import newera.myapplication.ui.view.inputs.InputManager;

/**
 * Created by Romain on 21/02/2017.
 */

public class KeepHue extends Shader{

    private Bitmap icone;

    public KeepHue(MainActivity activity) {
        super(activity);
    }

    public KeepHue(Context context) {
        super(context);
    }


    @Override
    public void ApplyFilter(Image image)
    {
        if(image != null && !image.isEmpty()) {
            ScriptC_keepHue rsKeepHue = new ScriptC_keepHue(renderScript);

            rsKeepHue.set_newHue( ((int) params.get("valueHue") * (1f/360f)) );
            rsKeepHue.set_epsilon( ((int) params.get("valueTolerance") * (1f/360f)) );

            for (Bitmap[] arrBitmap : image.getBitmaps())
                for (Bitmap bitmap : arrBitmap) {
                    Allocation in = Allocation.createFromBitmap(renderScript, bitmap);
                    Allocation out = Allocation.createTyped(renderScript, in.getType());

                    rsKeepHue.forEach_KeepSpecificHue(in, out);
                    out.copyTo(bitmap);
                }
        }
        //refreshImage();
    }

    public String getName(){
        return activity.getResources().getString(R.string.shaderKeepHueName);
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
        manager.createBox(EItems.F_KEEP_HUE, view.getResources().getString(R.string.shaderKeepHueName));
        view.setCurrentAction(EItems.F_KEEP_HUE);
        return 0;
    }

}
