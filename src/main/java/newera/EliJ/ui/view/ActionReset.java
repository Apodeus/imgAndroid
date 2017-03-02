package newera.EliJ.ui.view;

import android.content.Context;
import newera.EliJ.R;
import newera.EliJ.ui.view.inputs.InputManager;

/**
 * Created by Romain on 17/02/2017.
 */

public class ActionReset extends ActionClickable {

    public ActionReset(Context context)
    {
        super(context);
        this.drawableIconId = R.drawable.ic_reset_replay_black_24dp;
        this.clickableName = R.string.systemResetName;
    }

    @Override
    public int onClick(InputManager manager, CImageView view) {
        view.reinitialize();
        return 0;
    }
}
