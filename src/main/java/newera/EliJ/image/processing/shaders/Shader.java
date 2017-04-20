package newera.EliJ.image.processing.shaders;


import android.content.Context;

import android.support.v8.renderscript.RenderScript;
import newera.EliJ.image.Image;
import newera.EliJ.image.processing.EItems;
import newera.EliJ.ui.view.ActionClickable;
import newera.EliJ.ui.view.CImageView;
import newera.EliJ.ui.view.inputs.InputManager;

import java.util.Map;

/**
 * Created by Emile Barjou-Suire on 09/02/2017.
 */

public abstract class Shader extends ActionClickable {

    RenderScript renderScript;
    Map<String, Object> params;
    EItems item;

    Shader(Context context)
    {
        super(context);
        renderScript = RenderScript.create(context);
    }

    /**
     * Core code for shaders. Called by the view when the shader options are applied by the user.
     * @param image the Image object to be processed
     */
    public void ApplyFilter(Image image)
    {
    }


    /**
     * Called when the button on CircleMenu is pressed, nicely asks InputManager to generate inputs
     * for the user.
     * @param manager InputManager to which ask to generate inputs
     * @param view CImageView of the processed Image.
     * @return 0.
     */
    public int onClick(InputManager manager, CImageView view) {
        manager.createBox(this, item, view.getResources().getString(clickableName));
        view.setCurrentAction(item);
        return 0;
    }

    /**
     * Setter called before applying filter.
     * @param params Bundle of settings.
     */
    public void setParameters(Map<String, Object> params)
    {
        this.params = params;
    }

}
