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
        this.drawableIconId = R.drawable.ic_reset_replay_black_24dp;

        if(this.angle > 0)
            this.clickableName = R.string.systemRotateLeftName;
        else if(this.angle < 0)
            this.clickableName = R.string.systemRotateRightName;
    }

    @Override
    public int onClick(InputManager manager, CImageView view) {
        view.getImage().setAngle(this.angle);
        view.invalidate();
        return 0;
    }
}
