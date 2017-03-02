package newera.EliJ.ui.view.inputs;

import android.graphics.Canvas;
import android.support.design.widget.Snackbar;
import android.view.MotionEvent;
import newera.EliJ.image.processing.EItems;
import newera.EliJ.ui.view.CImageView;

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
    private ECategory currentCategory;

    public InputManager(CImageView view)
    {
        this.view = view;
    }

    public void createBox(EItems type, String label)
    {
        List<InputDataType> l = new ArrayList<>();
        switch (type) {
            case F_CHANGE_HUE:
                currentCategory = ECategory.FILTER;
                InputDataType n = new InputDataType(EInputType.COLOR_PICKER, "value", "Hue", new int[] {0, 360, 0});
                l.add(n);
                currentBox = new GenericBox(this, label, l);
                break;

            case F_LIGHTNESS:
                currentCategory = ECategory.FILTER;
                InputDataType seekBar = new InputDataType(EInputType.INTEGER_SEEKBAR, "value", "Lightness", new int[] {-50, 50, 0});
                l.add(seekBar);
                currentBox = new GenericBox(this, label, l);
                break;

            case F_KEEP_HUE:
                currentCategory = ECategory.FILTER;
                InputDataType seekBarHue = new InputDataType(EInputType.COLOR_PICKER, "valueHue", "Hue", new int[] {0, 360, 0});
                InputDataType seekBarTolerance = new InputDataType(EInputType.INTEGER_SEEKBAR, "valueTolerance", "Tolerance", new int[] {0, 180, 0});
                l.add(seekBarHue);
                l.add(seekBarTolerance);
                currentBox = new GenericBox(this, label, l);
                break;

            case F_CONTRAST:
                currentCategory = ECategory.FILTER;
                InputDataType seekBarContrast = new InputDataType(EInputType.INTEGER_SEEKBAR, "value", "Contrast", new int[] {-128, 128, 0});
                l.add(seekBarContrast);
                currentBox = new GenericBox(this, label, l);
                break;

            case S_QUALITY_SAVE:
                currentCategory = ECategory.SYSTEM;
                InputDataType seekBarQuality = new InputDataType(EInputType.INTEGER_SEEKBAR, "value", "Quality", new int[] {50, 100, 75});
                l.add(seekBarQuality);
                currentBox = new GenericBox(this, label, l);

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

    public void onConfirm(Map<String, Object> params)
    {
        this.currentParams = params;
        String message = "";
        switch (currentCategory) {
            case FILTER:
                view.onApplyFilter(params);
                message = "Filter applied";
                break;
            case TOOL:
                // :thinking:
                break;
            case SYSTEM:
                view.onApplySystem(params);

                break;
        }

        currentBox = null;
        Snackbar snackbar = Snackbar
                .make(view, message, Snackbar.LENGTH_SHORT);

        snackbar.show();
    }

    public void onCancel()
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

enum ECategory{
    FILTER,
    TOOL,
    SYSTEM,
}
