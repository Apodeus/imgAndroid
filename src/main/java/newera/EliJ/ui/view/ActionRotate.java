package newera.EliJ.ui.view;

import android.content.Context;

import newera.EliJ.R;
import newera.EliJ.ui.view.inputs.InputManager;

/**
 * Created by Romain on 17/02/2017.
 */

public class ActionRotate extends ActionClickable {

    private int angle;

    public ActionRotate(Context context, int alpha)
    {
        super(context);
        this.angle = alpha;
        this.clickableName = R.string.systemRotateLeftName;
        this.drawableIconId = R.drawable.ic_rotate_left_black_24dp;

        if(this.angle > 0)//right
        {
            this.clickableName = R.string.systemRotateRightName;
            this.drawableIconId = R.drawable.ic_rotate_right_black_24dp;
        }
        else if(this.angle < 0)//left
        {
            this.clickableName = R.string.systemRotateLeftName;
            this.drawableIconId = R.drawable.ic_rotate_left_black_24dp;
        }
    }

    @Override
    public int onClick(InputManager manager, CImageView view) {
        if(view.getImage() != null && !view.getImage().isEmpty()) {
            view.getImage().setAngle(this.angle);
            view.invalidate();
        }
        return 0;
    }
}
