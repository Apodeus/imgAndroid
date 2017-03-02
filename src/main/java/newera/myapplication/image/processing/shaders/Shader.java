package newera.myapplication.image.processing.shaders;


import android.content.Context;
import android.renderscript.RenderScript;
import newera.myapplication.MainActivity;
import newera.myapplication.R;
import newera.myapplication.image.Image;
import newera.myapplication.ui.view.ActionClickable;
import newera.myapplication.ui.view.CImageView;
import newera.myapplication.ui.view.inputs.InputManager;
import newera.myapplication.ui.view.ShaderDialogBox;

import java.util.Map;

/**
 * Created by Emile Barjou-Suire on 09/02/2017.
 */

public abstract class Shader extends ActionClickable {

    RenderScript renderScript;
    MainActivity activity;
    Map<String, Object> params;

    Shader(MainActivity activity)
    {
        super(activity);
        renderScript = RenderScript.create(activity);
        this.activity = activity;
    }

    Shader(Context context)
    {
        super(context);
        renderScript = RenderScript.create(context);
        this.activity = null;
    }

    public void ApplyFilter(Image image)
    {
    }

    public void ApplyPreviewFilter(Image image, Object param)
    {

    }


    void refreshImage(){
        activity.findViewById(R.id.cImageView).invalidate();
    }

    public int onClick(InputManager manager, CImageView view) {
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
