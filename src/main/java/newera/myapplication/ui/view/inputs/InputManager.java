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
                InputDataType n = new InputDataType(EInputType.COLOR_PICKER, "value", "Hue", new int[] {0, 360, 0});
                l.add(n);
                currentBox = new GenericBox(this, label, l);
                break;

            case F_LIGHTNESS:
                List<InputDataType> lstLightness = new ArrayList<>();
                InputDataType seekBar = new InputDataType(EInputType.INTEGER_SEEKBAR, "value", "Lightness", new int[] {0, 200, 100});
                lstLightness.add(seekBar);
                currentBox = new GenericBox(this, label, lstLightness);
                break;

            case F_KEEP_HUE:
                List<InputDataType> lstKeepHue = new ArrayList<>();
                InputDataType seekBarHue = new InputDataType(EInputType.COLOR_PICKER, "valueHue", "Hue", new int[] {0, 360, 0});
                InputDataType seekBarTolerance = new InputDataType(EInputType.INTEGER_SEEKBAR, "valueTolerance", "Tolerance", new int[] {0, 180, 0});
                lstKeepHue.add(seekBarHue);
                lstKeepHue.add(seekBarTolerance);
                currentBox = new GenericBox(this, label, lstKeepHue);
                break;

            case F_CONTRAST:
                List<InputDataType> lstContrast = new ArrayList<>();
                InputDataType seekBarContrast = new InputDataType(EInputType.INTEGER_SEEKBAR, "value", "Contrast", new int[] {-128, 128, 0});
                lstContrast.add(seekBarContrast);
                currentBox = new GenericBox(this, label, lstContrast);
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
