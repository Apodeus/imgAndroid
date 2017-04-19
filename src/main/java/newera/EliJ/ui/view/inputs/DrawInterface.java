package newera.EliJ.ui.view.inputs;

import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import newera.EliJ.R;
import newera.EliJ.image.processing.tools.Brush;
import newera.EliJ.image.processing.tools.Tool;
import newera.EliJ.ui.view.ToolConfig;
import newera.EliJ.ui.view.inputs.components.IGenericBoxComponent;

/**
 * Created by echo on 14/04/2017.
 */
public class DrawInterface implements IGenericBoxComponent {

    private final GenericBox box;

    private int index;
    private String label;
    private int currentValue = 0;
    private int initialValue = 0;
    private int minValue = -100;
    private int maxValue = 100;
    private int length;
    private int boxBackgroundColor;
    private int boxBorderColor;
    private int textColor;
    private int barBackgroundColor;
    private int barForegroundColor;
    private Rect boxBackground;
    private Rect barBackground;
    private Rect barForeground;
    private String plusSign;
    private Bitmap drawIconBitmap;
    private Canvas drawIconCanvas;
    private boolean isEditBar = false;
    private boolean isEditDrawer = false;
    private boolean isEditColorPicker = false;
    private boolean drawActive = false;
    private Bitmap viewCache;

    private Drawable drawIconDrawable;
    private Drawable eraserIconDrawable;
    private Drawable colorPickerIconDrawable;
    private Bitmap colorPickerIconBitmap;
    private Canvas colorPickerIconCanvas;
    private ToolConfig config;
    private Tool brush;

    private int currentColor;

    private final static float BOX_BOTTOM_OFFSET_Y = 0.05f;
    private final static float BOX_HEIGHT_COVERAGE = 0.08f;
    private final static float BOX_WIDTH_COVERAGE = 0.75f;

    private final static float BOX_HEIGHT_COVERAGE_EXP = 4f;

    private final static float BRUSH_ICON_FROM_RIGHT = 1f;
    private final static float BRUSH_ICON_FROM_TOP = 0.2f;
    private final static float ERASER_ICON_FROM_RIGHT = 0.8f;
    private final static float ERASER_ICON_FROM_TOP = 0.2f;
    private final static float COLOR_ICON_FROM_RIGHT = 0.2f;
    private final static float COLOR_ICON_FROM_TOP = 0.2f;
    private final static int ICON_SIZE = 100;

    private int viewWidth;
    private int viewHeight;
    private Paint paint;
    private int genericBoxOffset;
    private Canvas eraserIconCanvas;
    private Bitmap eraserIconBitmap;
    private boolean eraseActive;
    private boolean isEditEraser;
    private boolean colorPickerActive;

    public DrawInterface(GenericBox genericBox) {
        this.box = genericBox;
    }

    @Override
    public int getHeight() {
        if (colorPickerActive)
            return (int) (boxBackground.height() * BOX_HEIGHT_COVERAGE_EXP);
        return boxBackground.height();
    }

    @Override
    public void setStartingHeight(int height) {
        genericBoxOffset = height;
    }

    @Override
    public Object getValue() {
        return null;
    }

    @Override
    public void initialize(int[] settings) {
        this.viewWidth = box.getCanvasWidth();
        this.viewHeight = box.getCanvasHeight();

        this.length = maxValue - minValue;

        this.boxBackgroundColor = box.getInputManager().getView().getResources().getColor(R.color.colorPrimaryMild);
        this.boxBorderColor = box.getInputManager().getView().getResources().getColor(R.color.colorAccent);

        this.boxBackground = new Rect((int) (viewWidth * (1 - BOX_WIDTH_COVERAGE)) / 2, (int) (viewHeight * (1 - BOX_BOTTOM_OFFSET_Y - BOX_HEIGHT_COVERAGE) - genericBoxOffset), (int) (viewWidth - viewWidth * (1 - BOX_WIDTH_COVERAGE) / 2), (int) (viewHeight * (1 - BOX_BOTTOM_OFFSET_Y) - genericBoxOffset));

        this.paint = new Paint();
        this.currentColor = Color.BLACK;


        drawIconDrawable= box.getInputManager().getView().getResources().getDrawable(R.drawable.ic_brush_black_24dp);
        Bitmap o = Bitmap.createBitmap(ICON_SIZE, ICON_SIZE, Bitmap.Config.ARGB_8888);
        drawIconCanvas = new Canvas(o);

        eraserIconDrawable = box.getInputManager().getView().getResources().getDrawable(R.drawable.brush_eraser);
        Bitmap p = Bitmap.createBitmap(ICON_SIZE, ICON_SIZE, Bitmap.Config.ARGB_8888);
        eraserIconCanvas = new Canvas(p);

        colorPickerIconDrawable = box.getInputManager().getView().getResources().getDrawable(R.drawable.ic_hue_color_lens_black_24dp);
        Bitmap q = Bitmap.createBitmap(ICON_SIZE, ICON_SIZE, Bitmap.Config.ARGB_8888);
        colorPickerIconCanvas = new Canvas(q);

        drawIconDrawable.setColorFilter(box.getInputManager().getView().getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);
        drawIconDrawable.setBounds(0, 0, ICON_SIZE, ICON_SIZE);
        drawIconCanvas.drawColor(box.getInputManager().getView().getResources().getColor(R.color.colorLight));
        drawIconDrawable.draw(drawIconCanvas);
        eraserIconDrawable.setColorFilter(box.getInputManager().getView().getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);
        eraserIconDrawable.setBounds(0, 0, ICON_SIZE, ICON_SIZE);
        eraserIconCanvas.drawColor(box.getInputManager().getView().getResources().getColor(R.color.colorLight));
        eraserIconDrawable.draw(eraserIconCanvas);
        colorPickerIconDrawable.setColorFilter(box.getInputManager().getView().getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);
        colorPickerIconDrawable.setBounds(0, 0, ICON_SIZE, ICON_SIZE);
        colorPickerIconCanvas.drawColor(box.getInputManager().getView().getResources().getColor(R.color.colorLight));
        colorPickerIconDrawable.draw(eraserIconCanvas);

        drawIconBitmap = o;
        eraserIconBitmap = p;
        colorPickerIconBitmap = q;

        config = new ToolConfig();
        brush = new Brush();
        ((Brush)brush).initialize(box.getInputManager().getView().getContext());
        box.getInputManager().getView().getcCanvas().initialize(box.getInputManager().getView().getImage());
    }

    @Override
    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public boolean getEditStatus() {
        return drawActive || eraseActive;
    }

    @Override
    public void draw(Canvas canvas) {
        //barForeground.right = barBackground.left + (int) ((currentValue + length - maxValue) * barTik);

        if (drawActive)
        {
            drawIconCanvas.drawColor(box.getInputManager().getView().getResources().getColor(R.color.colorAccent));
            drawIconDrawable.setColorFilter(box.getInputManager().getView().getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);
        }else{
            drawIconCanvas.drawColor(box.getInputManager().getView().getResources().getColor(R.color.colorPrimary));
            drawIconDrawable.setColorFilter(box.getInputManager().getView().getResources().getColor(R.color.colorLight), PorterDuff.Mode.SRC_ATOP);
        }

        if (eraseActive)
        {
            eraserIconCanvas.drawColor(box.getInputManager().getView().getResources().getColor(R.color.colorAccent));
            eraserIconDrawable.setColorFilter(box.getInputManager().getView().getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);
        }else{
            eraserIconCanvas.drawColor(box.getInputManager().getView().getResources().getColor(R.color.colorPrimary));
            eraserIconDrawable.setColorFilter(box.getInputManager().getView().getResources().getColor(R.color.colorLight), PorterDuff.Mode.SRC_ATOP);
        }

        if (colorPickerActive)
        {
            colorPickerIconDrawable.setColorFilter(box.getInputManager().getView().getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
        }else{
            colorPickerIconDrawable.setColorFilter(box.getInputManager().getView().getResources().getColor(R.color.colorLight), PorterDuff.Mode.SRC_ATOP);
        }

        colorPickerIconCanvas.drawColor(currentColor);

        drawIconDrawable.draw(drawIconCanvas);
        eraserIconDrawable.draw(eraserIconCanvas);
        colorPickerIconDrawable.draw(colorPickerIconCanvas);
        canvas.drawBitmap(drawIconBitmap, boxBackground.right - boxBackground.width() * BRUSH_ICON_FROM_RIGHT, boxBackground.top + boxBackground.height() * BRUSH_ICON_FROM_TOP, paint);
        canvas.drawBitmap(eraserIconBitmap, boxBackground.right - boxBackground.width() * ERASER_ICON_FROM_RIGHT, boxBackground.top + boxBackground.height() * ERASER_ICON_FROM_TOP, paint);
        canvas.drawBitmap(colorPickerIconBitmap, boxBackground.right - boxBackground.width() * COLOR_ICON_FROM_RIGHT, boxBackground.top + boxBackground.height() * COLOR_ICON_FROM_TOP, paint);
    }

    @Override
    public void enableEdit(MotionEvent event) {
        float boxW = boxBackground.right - boxBackground.left;
        float boxH = boxBackground.bottom - boxBackground.top;

        if (event.getX() < (boxBackground.right - boxW* BRUSH_ICON_FROM_RIGHT + ICON_SIZE) && event.getX() > boxBackground.right - boxW* BRUSH_ICON_FROM_RIGHT)
            if(event.getY() > boxBackground.top + boxH* BRUSH_ICON_FROM_TOP && event.getY() < boxBackground.top + boxH* BRUSH_ICON_FROM_TOP + ICON_SIZE)
                isEditDrawer = true;

        if (event.getX() < (boxBackground.right - boxW* ERASER_ICON_FROM_RIGHT + ICON_SIZE) && event.getX() > boxBackground.right - boxW* ERASER_ICON_FROM_RIGHT)
            if(event.getY() > boxBackground.top + boxH* ERASER_ICON_FROM_TOP && event.getY() < boxBackground.top + boxH* ERASER_ICON_FROM_TOP + ICON_SIZE)
                isEditEraser = true;

        if (event.getX() < (boxBackground.right - boxW* COLOR_ICON_FROM_RIGHT + ICON_SIZE) && event.getX() > boxBackground.right - boxW* COLOR_ICON_FROM_RIGHT)
            if(event.getY() > boxBackground.top + boxH* COLOR_ICON_FROM_TOP && event.getY() < boxBackground.top + boxH* COLOR_ICON_FROM_TOP + ICON_SIZE)
                isEditColorPicker = true;


    }

    @Override
    public void disableEdit() {
        isEditDrawer = false;
        isEditEraser = false;
        isEditColorPicker = false;
    }

    @Override
    public void handleEdit(MotionEvent event) {
        if (isEditDrawer)
        {
            isEditDrawer = false;
            drawActive = !drawActive;
            //box.setPictureEdit(drawActive);

            if (drawActive)
            {
                eraseActive = false;
                viewCache = Bitmap.createBitmap(box.getInputManager().getView().getWidth(), box.getInputManager().getView().getHeight(), Bitmap.Config.ARGB_8888);
                Canvas c = new Canvas(viewCache);
                box.getInputManager().getView().draw(c);
            }
        }

        if (isEditEraser)
        {
            isEditEraser = false;
            eraseActive = !eraseActive;
            //box.setPictureEdit(eraseActive);

            if (eraseActive)
            {
                drawActive = false;
                viewCache = Bitmap.createBitmap(box.getInputManager().getView().getWidth(), box.getInputManager().getView().getHeight(), Bitmap.Config.ARGB_8888);
                Canvas c = new Canvas(viewCache);
                box.getInputManager().getView().draw(c);
            }
        }

        if (isEditColorPicker)
        {
            isEditColorPicker = false;
            colorPickerActive = !colorPickerActive;
        }

        if (drawActive && event.getY() < boxBackground.top)
            box.getInputManager().getView().getcCanvas().applyTool(brush, config, (int) event.getRawX(), (int) event.getRawY());

        if (eraseActive && event.getY() < boxBackground.top)
            box.getInputManager().getView().getcCanvas().erase(brush, config, (int) event.getRawX(), (int) event.getRawY());
    }
}
