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
    private Bitmap pickColorIconBitmap;
    private Canvas drawColorIconCanvas;
    private boolean isEditBar = false;
    private boolean isEditDrawer = false;
    private boolean drawActive = false;
    private Bitmap viewCache;

    private Drawable drawIconDrawable;
    private Drawable eraserIconDrawable;
    private ToolConfig config;
    private Tool brush;

    private final static float BOX_BOTTOM_OFFSET_Y = 0.05f;
    private final static float BOX_HEIGHT_COVERAGE = 0.08f;
    private final static float BOX_WIDTH_COVERAGE = 0.75f;

    private final static float BRUSH_ICON_FROM_RIGHT = 1f;
    private final static float BRUSH_ICON_FROM_TOP = 0.2f;
    private final static float ERASER_ICON_FROM_RIGHT = 0.8f;
    private final static float ERASER_ICON_FROM_TOP = 0.2f;
    private final static int PICK_ICON_SIZE = 100;

    private int viewWidth;
    private int viewHeight;
    private Paint paint;
    private int genericBoxOffset;
    private Canvas eraserIconCanvas;
    private Bitmap eraserIconBitmap;
    private boolean eraseActive;
    private boolean isEditEraser;

    public DrawInterface(GenericBox genericBox) {
        this.box = genericBox;
    }

    @Override
    public int getHeight() {
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


        drawIconDrawable= box.getInputManager().getView().getResources().getDrawable(R.drawable.ic_brush_black_24dp);
        Bitmap o = Bitmap.createBitmap(PICK_ICON_SIZE, PICK_ICON_SIZE, Bitmap.Config.ARGB_8888);
        drawColorIconCanvas = new Canvas(o);

        eraserIconDrawable = box.getInputManager().getView().getResources().getDrawable(R.drawable.brush_eraser);
        Bitmap p = Bitmap.createBitmap(PICK_ICON_SIZE, PICK_ICON_SIZE, Bitmap.Config.ARGB_8888);
        eraserIconCanvas = new Canvas(p);

        drawIconDrawable.setColorFilter(box.getInputManager().getView().getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);
        drawIconDrawable.setBounds(0, 0, PICK_ICON_SIZE, PICK_ICON_SIZE);
        drawColorIconCanvas.drawColor(box.getInputManager().getView().getResources().getColor(R.color.colorLight));
        drawIconDrawable.draw(drawColorIconCanvas);
        eraserIconDrawable.setColorFilter(box.getInputManager().getView().getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);
        eraserIconDrawable.setBounds(0, 0, PICK_ICON_SIZE, PICK_ICON_SIZE);
        eraserIconCanvas.drawColor(box.getInputManager().getView().getResources().getColor(R.color.colorLight));
        eraserIconDrawable.draw(eraserIconCanvas);

        pickColorIconBitmap = o;
        eraserIconBitmap = p;

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
            drawColorIconCanvas.drawColor(box.getInputManager().getView().getResources().getColor(R.color.colorAccent));
            drawIconDrawable.setColorFilter(box.getInputManager().getView().getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);
        }else{
            drawColorIconCanvas.drawColor(box.getInputManager().getView().getResources().getColor(R.color.colorPrimary));
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


        drawIconDrawable.draw(drawColorIconCanvas);
        eraserIconDrawable.draw(eraserIconCanvas);
        canvas.drawBitmap(pickColorIconBitmap, boxBackground.right - boxBackground.width()* BRUSH_ICON_FROM_RIGHT, boxBackground.top + boxBackground.height()* BRUSH_ICON_FROM_TOP, paint);
        canvas.drawBitmap(eraserIconBitmap, boxBackground.right - boxBackground.width()* ERASER_ICON_FROM_RIGHT, boxBackground.top + boxBackground.height()* ERASER_ICON_FROM_TOP, paint);

    }

    @Override
    public void enableEdit(MotionEvent event) {
        float boxW = boxBackground.right - boxBackground.left;
        float boxH = boxBackground.bottom - boxBackground.top;

        if (event.getX() < (boxBackground.right - boxW* BRUSH_ICON_FROM_RIGHT + PICK_ICON_SIZE) && event.getX() > boxBackground.right - boxW* BRUSH_ICON_FROM_RIGHT)
            if(event.getY() > boxBackground.top + boxH* BRUSH_ICON_FROM_TOP && event.getY() < boxBackground.top + boxH* BRUSH_ICON_FROM_TOP + PICK_ICON_SIZE)
                isEditDrawer = true;

        if (event.getX() < (boxBackground.right - boxW* ERASER_ICON_FROM_RIGHT + PICK_ICON_SIZE) && event.getX() > boxBackground.right - boxW* ERASER_ICON_FROM_RIGHT)
            if(event.getY() > boxBackground.top + boxH* ERASER_ICON_FROM_TOP && event.getY() < boxBackground.top + boxH* ERASER_ICON_FROM_TOP + PICK_ICON_SIZE)
                isEditEraser = true;


    }

    @Override
    public void disableEdit() {
        isEditDrawer = false;
        isEditEraser = false;
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

        if (drawActive && event.getY() < boxBackground.top)
            box.getInputManager().getView().getcCanvas().applyTool(brush, config, (int) event.getRawX(), (int) event.getRawY());

        if (eraseActive && event.getY() < boxBackground.top)
            box.getInputManager().getView().getcCanvas().erase(brush, config, (int) event.getRawX(), (int) event.getRawY());
    }
}