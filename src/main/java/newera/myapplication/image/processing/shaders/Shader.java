package newera.myapplication.image.processing.shaders;


import android.renderscript.RenderScript;
import newera.myapplication.MainActivity;
import newera.myapplication.image.Image;
import newera.myapplication.ui.Clickable;

/**
 * Created by Emile Barjou-Suire on 09/02/2017.
 */

public abstract class Shader implements Clickable{

    protected RenderScript renderScript;
    protected MainActivity activity;

    Shader(MainActivity activity)
    {
        renderScript = RenderScript.create(activity);
        this.activity = activity;
    }

    public void ApplyFilter(Image image)
    {
        if (renderScript == null)
            return;
        return;
    }

    public String getName(){
        return null;
    }


}
