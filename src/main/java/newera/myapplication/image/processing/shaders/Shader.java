package newera.myapplication.image.processing.shaders;


import android.renderscript.RenderScript;
import newera.myapplication.MainActivity;
import newera.myapplication.R;
import newera.myapplication.image.Image;
import newera.myapplication.ui.Clickable;
import newera.myapplication.ui.view.CImageView;
import newera.myapplication.ui.view.ShaderDialogBox;

import java.util.Map;

/**
 * Created by Emile Barjou-Suire on 09/02/2017.
 */

public abstract class Shader implements Clickable{

    protected RenderScript renderScript;
    protected MainActivity activity;
    protected Map<String, Object> params;

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

    protected void refreshImage(){
        ((CImageView)activity.findViewById(R.id.cImageView)).invalidate();
    }

    public String getName(){
        return null;
    }

    public int onClick() {
        ShaderDialogBox dial = new ShaderDialogBox();
        dial.setOnClick(this);
        dial.setImage(((CImageView)activity.findViewById(R.id.cImageView)).getImage());
        dial.setName(this.getName());
        dial.show(this.activity.getFragmentManager(), "ShaderDialog");
        return 0;
    }

    public void setParameters(Map<String, Object> params)
    {
        this.params = params;
    }
}
