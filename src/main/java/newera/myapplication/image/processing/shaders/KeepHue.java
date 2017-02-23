package newera.myapplication.image.processing.shaders;

import android.graphics.Bitmap;
import android.renderscript.Allocation;

import newera.myapplication.MainActivity;
import newera.myapplication.R;
import newera.myapplication.ScriptC_keepHue;
import newera.myapplication.image.Image;
import newera.myapplication.ui.view.CImageView;
import newera.myapplication.ui.view.InputManager;

/**
 * Created by Romain on 21/02/2017.
 */

public class KeepHue extends Shader{

    private Bitmap icone;

    public KeepHue(MainActivity activity) {
        super(activity);
    }


    @Override
    public void ApplyFilter(Image image)
    {
        if(image != null && !image.isEmpty()) {
            ScriptC_keepHue rsKeepHue = new ScriptC_keepHue(renderScript);
            rsKeepHue.set_epsilon(0.05f);
            rsKeepHue.set_newHue(0f);

            for (Bitmap[] b1 : image.getBitmaps())
                for (Bitmap b : b1) {
                    Allocation in = Allocation.createFromBitmap(renderScript, b);
                    Allocation out = Allocation.createTyped(renderScript, in.getType());

                    rsKeepHue.forEach_KeepSpecificHue(in, out);
                    out.copyTo(b);
                }
        }
        refreshImage();
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

}
