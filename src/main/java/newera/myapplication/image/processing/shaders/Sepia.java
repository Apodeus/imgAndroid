package newera.myapplication.image.processing.shaders;
import android.graphics.Bitmap;
import android.renderscript.Allocation;

import newera.myapplication.MainActivity;
import newera.myapplication.R;
import newera.myapplication.ScriptC_sepia;
import newera.myapplication.image.Image;


/**
 * Created by romain on 09/02/17.
 */

public class Sepia extends Shader{

    public Sepia(MainActivity activity) {
        super(activity);
    }

    @Override
    public void ApplyFilter(Image image)
    {
        if(image != null && !image.isEmpty()) {
            ScriptC_sepia rsSepia = new ScriptC_sepia(renderScript);

            for (Bitmap[] arrBitmap : image.getBitmaps())
                for (Bitmap bitmap : arrBitmap) {

                    Allocation in = Allocation.createFromBitmap(renderScript, bitmap);
                    Allocation out = Allocation.createTyped(renderScript, in.getType());

                    rsSepia.forEach_sepia(in, out);
                    out.copyTo(bitmap);
                }
        }
        refreshImage();
    }

    public String getName()
    {
        return activity.getResources().getString(R.string.shaderSepiaName);
    }

    @Override
    public int getNameId() {
        return 0;
    }

    @Override
    public Bitmap getIcone() {
        return null;
    }


}