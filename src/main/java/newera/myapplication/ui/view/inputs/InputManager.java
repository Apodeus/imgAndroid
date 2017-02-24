package newera.myapplication.ui.view.inputs;

import android.graphics.Canvas;
import android.support.design.widget.Snackbar;
import android.view.MotionEvent;
import newera.myapplication.ui.view.CImageView;
import newera.myapplication.ui.view.inputs.EInputBox;
import newera.myapplication.ui.view.inputs.IInputBox;
import newera.myapplication.ui.view.inputs.IntegerSeekBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by echo on 21/02/2017.
 */
public class InputManager {

    private CImageView view;

    private IInputBox currentBox;
    private GenericBox box;

    private Map<String, Object> currentParams;
    private Map<String, Object> currentPreviewParams;

    private EInputBox currentFilter;

    public InputManager(CImageView view)
    {
        this.view = view;
    }

    public void createBox(EInputBox type, String label)
    {
        switch (type) {
            case INTEGER:
                List<InputDataType> l = new ArrayList<>();
                InputDataType n = new InputDataType();
                n.setInputType(EInputType.INTEGER_SEEKBAR);
                n.setLabel("value");
                n.setSettings(new int[] {0, 360, 0});
                l.add(n);
                //currentBox = new IntegerSeekBar(this);
                //currentBox.setLabel(label);
                //((IntegerSeekBar) currentBox).displayPlus(bounds[0] < 0);
                //((IntegerSeekBar) currentBox).setBounds(bounds[0], bounds[1], bounds[2]);
                box = new GenericBox(this, label, l);
                break;

            case STRING_X_Y:
                break;
        }

        view.invalidate();
    }



    public boolean handleTouch(MotionEvent event) {
        return box != null && box.handleTouch(event);

    }

    public void draw(Canvas canvas)
    {
        if (currentBox != null)
            currentBox.drawBox(canvas);
        if (box != null)
            box.drawBox(canvas);
    }

    int getValue()
    {
        return currentBox.getValue();
    }

    /*public void onApplyFilter(int value, Map<String, Object> params)
    {
        this.currentParams = params;
        view.onApplyFilter(value);
        currentBox = null;
        Snackbar snackbar = Snackbar
                .make(view, "Filter applied", Snackbar.LENGTH_SHORT);

        snackbar.show();

    }*/

    public void onApplyFilter(Map<String, Object> params)
    {
        this.currentParams = params;
        view.onApplyFilter(params);
        box = null;
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

    public void onPreviewFilter(int value, Map<String, Object> params)
    {
        this.currentPreviewParams = params;
        view.onPreviewFilter(value);
    }

    public CImageView getView() {
        return view;
    }

    public Map<String, Object> getParams() {
        return currentParams;
    }

    public Object getPreviewParams() {
        return currentPreviewParams;
    }
}
