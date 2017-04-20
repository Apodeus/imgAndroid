package newera.EliJ.ui.view.inputs;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.design.widget.Snackbar;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.RenderScript;
import android.view.MotionEvent;
import newera.EliJ.R;
import newera.EliJ.ScriptC_fuse_bitmap;
import newera.EliJ.image.processing.EItems;
import newera.EliJ.image.processing.shaders.Shader;
import newera.EliJ.ui.Clickable;
import newera.EliJ.ui.view.CCanvas;
import newera.EliJ.ui.view.CImageView;
import newera.EliJ.ui.view.CanvasTool;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by echo on 21/02/2017.
 */
public class InputManager {

    private CImageView view;

    private GenericBox currentBox;

    private ECategory currentCategory;
    private Clickable currentClickable;

    /**
     * Create an InputManager to handle communication between image processing and
     * user inputs.
     * @param view View used for processing
     */
    public InputManager(CImageView view)
    {
        this.view = view;
    }

    /**
     * Generate a box for user interaction.
     * @param clk CircleMenu button's reference
     * @param type Action enum
     * @param label Label/Hint for user
     */
    public void createBox(Clickable clk, EItems type, String label)
    {
        currentClickable = clk;
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
                InputDataType data = new InputDataType(EInputType.HARD_DATA, "borders", "", new int[] {-50, 50, 0});
                l.add(seekBar);
                l.add(data);
                currentBox = new GenericBox(this, label, l);
                break;

            case F_KEEP_HUE:
                currentCategory = ECategory.FILTER;
                InputDataType seekBarTolerance = new InputDataType(EInputType.INTEGER_SEEKBAR, "valueTolerance", "Tolerance", new int[] {0, 180, 0});
                InputDataType seekBarHue = new InputDataType(EInputType.COLOR_PICKER, "valueHue", "Hue", new int[] {0, 360, 0});
                l.add(seekBarTolerance);
                l.add(seekBarHue);
                currentBox = new GenericBox(this, label, l);
                break;

            case F_CONTRAST:
                currentCategory = ECategory.FILTER;
                InputDataType seekBarContrast = new InputDataType(EInputType.INTEGER_SEEKBAR, "value", "Contrast", new int[] {-128, 128, 0});
                l.add(seekBarContrast);
                currentBox = new GenericBox(this, label, l);
                break;

            case F_CARTOON:
            case F_HISTOGRAM_EQ:
            case F_CONVOLUTION:
            case F_SEPIA:
            case F_INVERT_COLOR:
            case F_GRAYSCALE:
                currentCategory = ECategory.FILTER;
                currentBox = new GenericBox(this, label, l);
                break;

            case S_QUALITY_SAVE:
                currentCategory = ECategory.SYSTEM;
                InputDataType seekBarQuality = new InputDataType(EInputType.INTEGER_SEEKBAR, "value", "Quality", new int[] {50, 100, 75});
                l.add(seekBarQuality);
                currentBox = new GenericBox(this, label, l);
                break;

            case T_BRUSH:
                currentCategory = ECategory.TOOL;
                InputDataType tool = new InputDataType(EInputType.DRAW, "canvas", "", new int[] {0});
                l.add(tool);
                currentBox = new GenericBox(this, label, l);
                break;

            case NONE:
                break;
        }

        view.invalidate();
    }

    /**
     * Bridge between CImageView and GenericBox.
     */
    public boolean handleTouch(MotionEvent event) {
        return currentBox != null && currentBox.handleTouch(event);

    }

    /**
     * Draw the box on the given canvas.
     * @param canvas eh.
     */
    public void draw(Canvas canvas)
    {
        if (currentBox != null)
            currentBox.drawBox(canvas);
    }

    /*public void onPreviewFilter(int value, Map<String, Object> params)
    {
        view.onPreviewFilter(value);
    }*/

    /**
     * @return View for GenericBox's needs.
     */
    public CImageView getView() {
        return view;
    }

    void onConfirm(Map<String, Object> params)
    {
        String message = "";
        switch (currentCategory) {
            case FILTER:
                view.onApplyFilter((Shader) currentClickable, params);
                message = view.getResources().getString(R.string.messageFilterApplied);
                break;
            case TOOL:
                //azerty
                RenderScript rs = RenderScript.create(view.getContext());
                ScriptC_fuse_bitmap script = new ScriptC_fuse_bitmap(rs);
                CCanvas cca = view.getcCanvas();
                for(int i = 0; i < cca.getWidth(); i++)
                    for(int j = 0; j < cca.getHeight(); j++)
                    {
                        if (view.getcCanvas().getCanvasTool(i, j).active)
                        {
                            Allocation ori = Allocation.createFromBitmap(rs, view.getImage().getBitmap(i, j));
                            Allocation pnt = Allocation.createFromBitmap(rs, view.getcCanvas().getCanvasTool(i, j).getBitmap());
                            Allocation out = Allocation.createTyped(rs, ori.getType());
                            script.set_src(ori);
                            script.forEach_Fuse(pnt, out);
                            Bitmap res = Bitmap.createBitmap(view.getImage().getBitmap(i, j).getWidth(), view.getImage().getBitmap(i, j).getHeight(), Bitmap.Config.ARGB_8888);
                            out.copyTo(res);
                            view.getImage().addBitmap(res, i, j);
                        }

                    }

                view.getcCanvas().reset();
                break;
            case SYSTEM:
                view.onApplySystem(params);
                message = view.getResources().getString(R.string.messageSaveSuccess); //temp
                break;
        }

        currentBox = null;
        Snackbar snackbar = Snackbar
                .make(view, message, Snackbar.LENGTH_SHORT);

        snackbar.show();
    }

    void onCancel()
    {
        view.onCancelFilter();
        if (currentCategory == ECategory.TOOL)
            view.getcCanvas().reset();

        currentBox = null;
        Snackbar snackbar = Snackbar
                .make(view, "Filter canceled", Snackbar.LENGTH_SHORT);

        snackbar.show();
    }

    public ECategory getCategory() {
        return currentCategory;
    }
}


