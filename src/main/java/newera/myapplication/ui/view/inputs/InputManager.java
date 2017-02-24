package newera.myapplication.ui.view.inputs;

import android.graphics.Canvas;
import android.support.design.widget.Snackbar;
import android.view.MotionEvent;
import newera.myapplication.image.processing.EItems;
import newera.myapplication.ui.view.CImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by echo on 21/02/2017.
 */
public class InputManager {

    private CImageView view;

    private GenericBox currentBox;

    private Map<String, Object> currentParams;
    private Map<String, Object> currentPreviewParams;

    public InputManager(CImageView view)
    {
        this.view = view;
    }

    public void createBox(EItems type, String label)
    {
        switch (type) {
            case F_CHANGE_HUE:
                List<InputDataType> l = new ArrayList<>();
                InputDataType n = new InputDataType(EInputType.INTEGER_SEEKBAR, "value", "Hue", new int[] {0, 360, 0});
                l.add(n);
                currentBox = new GenericBox(this, label, l);
                break;

            case NONE:
                break;
        }

        view.invalidate();
    }



    public boolean handleTouch(MotionEvent event) {
        return currentBox != null && currentBox.handleTouch(event);

    }

    public void draw(Canvas canvas)
    {
        if (currentBox != null)
            currentBox.drawBox(canvas);
    }

    public void onApplyFilter(Map<String, Object> params)
    {
        this.currentParams = params;
        view.onApplyFilter(params);
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
