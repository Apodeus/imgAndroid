package newera.EliJ.ui.view;

import android.content.Context;
import newera.EliJ.R;
import newera.EliJ.image.processing.EItems;
import newera.EliJ.ui.view.inputs.InputManager;

/**
 * Created by echo on 14/04/2017.
 */
public class ActionTools extends ActionClickable {
    /**
     * Create a button for CircleMenu with icon, label and identification.
     *
     * @param context App's context (MainActivity or View)
     */
    public ActionTools(Context context) {
        super(context);
        this.drawableIconId = R.drawable.ic_brush_black_24dp;
        this.clickableName = R.string.brushName;
    }

    @Override
    public int onClick(InputManager manager, CImageView view) {
        manager.createBox(this, EItems.T_BRUSH, view.getResources().getString(R.string.toolsBrush));
        view.setCurrentAction(EItems.T_BRUSH);
        return 0;
    }
}
