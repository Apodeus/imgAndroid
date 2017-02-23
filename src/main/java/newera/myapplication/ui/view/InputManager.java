package newera.myapplication.ui.view;

import android.graphics.Canvas;
import android.support.design.widget.Snackbar;
import android.view.MotionEvent;
import newera.myapplication.ui.view.inputs.EInputBox;
import newera.myapplication.ui.view.inputs.IInputBox;
import newera.myapplication.ui.view.inputs.IntegerSeekBar;

import java.util.Map;

/**
 * Created by echo on 21/02/2017.
 */
public class InputManager {

    private CImageView view;

    private IInputBox currentBox;

    private Map<String, Object> currentParams;

    InputManager(CImageView view)
    {
        this.view = view;
    }

    void createBox(EInputBox type, String label, int[] bounds)
    {
        switch (type) {
            case INTEGER:
                currentBox = new IntegerSeekBar(this);
                currentBox.setLabel(label);
                ((IntegerSeekBar) currentBox).displayPlus(bounds[0] < 0);
                ((IntegerSeekBar) currentBox).setBounds(bounds[0], bounds[1], bounds[2]);
                break;

            case STRING_X_Y:
                break;
        }

        view.invalidate();
    }

    boolean handleTouch(MotionEvent event) {
        return currentBox != null && currentBox.handleTouch(event);

    }

    void draw(Canvas canvas)
    {
        if (currentBox != null)
            currentBox.drawBox(canvas);
    }

    int getValue()
    {
        return currentBox.getValue();
    }

    public void onApplyFilter(int value, Map<String, Object> params)
    {
        this.currentParams = params;
        view.onApplyFilter(value);
        currentBox = null;
        Snackbar snackbar = Snackbar
                .make(view, "Filter applied", Snackbar.LENGTH_SHORT);

        snackbar.show();

    }

    public void onCancelFilter()
    {
        view.onCancelFilter();
        currentBox = null;
        Snackbar snackbar = Snackbar
                .make(view, "Filter canceled", Snackbar.LENGTH_SHORT);

        snackbar.show();
    }

    public CImageView getView() {
        return view;
    }

    public Map<String, Object> getParams() {
        return currentParams;
    }
}
