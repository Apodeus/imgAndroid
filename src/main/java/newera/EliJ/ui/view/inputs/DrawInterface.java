package newera.EliJ.ui.view.inputs;

import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.RenderScript;
import android.view.MotionEvent;
import newera.EliJ.R;
import newera.EliJ.ScriptC_color_bars;
import newera.EliJ.image.processing.tools.Brush;
import newera.EliJ.image.processing.tools.Tool;
import newera.EliJ.ui.view.EMethod;
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
    private Bitmap colorBarR;
    private Bitmap colorBarG;
    private Bitmap colorBarB;
    private Bitmap colorBarA;
    private Rect colorBarRRect;
    private Rect colorBarGRect;
    private Rect colorBarBRect;
    private Rect colorBarARect;
    private Bitmap colorCursorIconBitmap;
    private Canvas colorCursorIconCanvas;
    private Drawable colorCursorIconDrawable;
    private Allocation aout;

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
    private final static float COLOR_BAR_THICKNESS = 0.08f;
    private final static int BAR_SEEKER_ICON_SIZE = 60;
    private final static float R_BAR_FROM_TOP = 0.15f;
    private final static float G_BAR_FROM_TOP = 0.35f;
    private final static float B_BAR_FROM_TOP = 0.55f;
    private final static float A_BAR_FROM_TOP = 0.75f;
    private final static float BAR_FROM_LEFT = 0.05f;
    private final static float BAR_FROM_RIGHT = 0.3f;
    private final static int CURSOR_DECAL_TO_UP = 5;


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

    private RenderScript rs;
    private ScriptC_color_bars script;
    private boolean isEditColorR;
    private boolean isEditColorG;
    private boolean isEditColorB;
    private boolean isEditColorA;

    public DrawInterface(GenericBox genericBox) {
        this.box = genericBox;
        rs = RenderScript.create(box.getInputManager().getView().getContext());
        script = new ScriptC_color_bars(rs);
        box.getInputManager().getView().getcCanvas().setMethod(EMethod.DRAW);
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

        int topExp = boxBackground.top - (int)(boxBackground.height() * (BOX_HEIGHT_COVERAGE_EXP - 1));
        int topExpSize = (int)(boxBackground.height() * (BOX_HEIGHT_COVERAGE_EXP - 1));
        colorBarRRect = new Rect(boxBackground.left + (int) (boxBackground.width() * BAR_FROM_LEFT), topExp + (int)(topExpSize * R_BAR_FROM_TOP), boxBackground.right - (int)(boxBackground.width() * BAR_FROM_RIGHT), topExp + (int)(topExpSize * (R_BAR_FROM_TOP + COLOR_BAR_THICKNESS)));
        colorBarGRect = new Rect(boxBackground.left + (int) (boxBackground.width() * BAR_FROM_LEFT), topExp + (int)(topExpSize * G_BAR_FROM_TOP), boxBackground.right - (int)(boxBackground.width() * BAR_FROM_RIGHT), topExp + (int)(topExpSize * (G_BAR_FROM_TOP + COLOR_BAR_THICKNESS)));
        colorBarBRect = new Rect(boxBackground.left + (int) (boxBackground.width() * BAR_FROM_LEFT), topExp + (int)(topExpSize * B_BAR_FROM_TOP), boxBackground.right - (int)(boxBackground.width() * BAR_FROM_RIGHT), topExp + (int)(topExpSize * (B_BAR_FROM_TOP + COLOR_BAR_THICKNESS)));
        colorBarARect = new Rect(boxBackground.left + (int) (boxBackground.width() * BAR_FROM_LEFT), topExp + (int)(topExpSize * A_BAR_FROM_TOP), boxBackground.right - (int)(boxBackground.width() * BAR_FROM_RIGHT), topExp + (int)(topExpSize * (A_BAR_FROM_TOP + COLOR_BAR_THICKNESS)));
        script.set_width(colorBarRRect.width());
        drawIconDrawable= box.getInputManager().getView().getResources().getDrawable(R.drawable.ic_brush_black_24dp);
        Bitmap o = Bitmap.createBitmap(ICON_SIZE, ICON_SIZE, Bitmap.Config.ARGB_8888);
        drawIconCanvas = new Canvas(o);

        eraserIconDrawable = box.getInputManager().getView().getResources().getDrawable(R.drawable.brush_eraser);
        Bitmap p = Bitmap.createBitmap(ICON_SIZE, ICON_SIZE, Bitmap.Config.ARGB_8888);
        eraserIconCanvas = new Canvas(p);

        colorPickerIconDrawable = box.getInputManager().getView().getResources().getDrawable(R.drawable.ic_hue_color_lens_black_24dp);
        Bitmap q = Bitmap.createBitmap(ICON_SIZE, ICON_SIZE, Bitmap.Config.ARGB_8888);
        colorPickerIconCanvas = new Canvas(q);

        colorCursorIconDrawable = box.getInputManager().getView().getResources().getDrawable(R.drawable.ic_pause_circle_filled_black_24dp);
        Bitmap r = Bitmap.createBitmap(BAR_SEEKER_ICON_SIZE, BAR_SEEKER_ICON_SIZE, Bitmap.Config.ARGB_8888);
        colorCursorIconCanvas = new Canvas(r);

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

        colorCursorIconDrawable.setColorFilter(box.getInputManager().getView().getResources().getColor(R.color.colorLight), PorterDuff.Mode.SRC_ATOP);
        colorCursorIconDrawable.setBounds(0, 0, BAR_SEEKER_ICON_SIZE, BAR_SEEKER_ICON_SIZE);
        colorCursorIconDrawable.draw(colorCursorIconCanvas);

        colorBarA = Bitmap.createBitmap(colorBarARect.width(), colorBarARect.height(), Bitmap.Config.ARGB_8888);
        colorBarR = Bitmap.createBitmap(colorBarRRect.width(), colorBarRRect.height(), Bitmap.Config.ARGB_8888);
        colorBarG = Bitmap.createBitmap(colorBarGRect.width(), colorBarGRect.height(), Bitmap.Config.ARGB_8888);
        colorBarB = Bitmap.createBitmap(colorBarBRect.width(), colorBarBRect.height(), Bitmap.Config.ARGB_8888);


        drawIconBitmap = o;
        eraserIconBitmap = p;
        colorPickerIconBitmap = q;
        colorCursorIconBitmap = r;

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
            script.set_red(Color.red(currentColor));
            script.set_blue(Color.blue(currentColor));
            script.set_green(Color.green(currentColor));
            canvas.drawBitmap(colorBarR, null, colorBarRRect, paint);
            canvas.drawBitmap(colorBarG, null, colorBarGRect, paint);
            canvas.drawBitmap(colorBarB, null, colorBarBRect, paint);
            Xfermode xf =  paint.getXfermode();
            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
            //ColorFilter cf = paint.getColorFilter();
            //paint.setColorFilter(new PorterDuffColorFilter(Color.TRANSPARENT, PorterDuff.Mode.DST_IN));
            canvas.drawBitmap(colorBarA, null, colorBarARect, paint);
            //paint.setColorFilter(cf);
            paint.setXfermode(xf);

            canvas.drawBitmap(colorCursorIconBitmap, (int)(colorBarRRect.left - BAR_SEEKER_ICON_SIZE / 2 + Color.red(currentColor)/255f * colorBarRRect.width()), (int)(colorBarRRect.top - CURSOR_DECAL_TO_UP), paint);
            canvas.drawBitmap(colorCursorIconBitmap, (int)(colorBarGRect.left - BAR_SEEKER_ICON_SIZE / 2 + Color.green(currentColor)/255f * colorBarRRect.width()), (int)(colorBarGRect.top - CURSOR_DECAL_TO_UP), paint);
            canvas.drawBitmap(colorCursorIconBitmap, (int)(colorBarBRect.left - BAR_SEEKER_ICON_SIZE / 2 + Color.blue(currentColor)/255f * colorBarRRect.width()), (int)(colorBarBRect.top - CURSOR_DECAL_TO_UP), paint);
            canvas.drawBitmap(colorCursorIconBitmap, (int)(colorBarARect.left - BAR_SEEKER_ICON_SIZE / 2 + Color.alpha(currentColor)/255f * colorBarRRect.width()), (int)(colorBarARect.top - CURSOR_DECAL_TO_UP), paint);
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


        if (event.getX() > (colorBarRRect.left + Color.red(currentColor)/255f * colorBarRRect.width() - BAR_SEEKER_ICON_SIZE/2) && event.getX() < (colorBarRRect.left + Color.red(currentColor)/255f * colorBarRRect.width() + BAR_SEEKER_ICON_SIZE/2))
            if(event.getY() > colorBarRRect.top - BAR_SEEKER_ICON_SIZE/2 && event.getY() < colorBarRRect.top + BAR_SEEKER_ICON_SIZE/2 )
                isEditColorR = true;

        if (event.getX() > (colorBarGRect.left + Color.green(currentColor)/255f * colorBarGRect.width() - BAR_SEEKER_ICON_SIZE/2) && event.getX() < (colorBarGRect.left + Color.green(currentColor)/255f * colorBarGRect.width() + BAR_SEEKER_ICON_SIZE/2))
            if(event.getY() > colorBarGRect.top - BAR_SEEKER_ICON_SIZE/2 && event.getY() < colorBarGRect.top + BAR_SEEKER_ICON_SIZE/2 )
                isEditColorG = true;

        if (event.getX() > (colorBarBRect.left + Color.blue(currentColor)/255f * colorBarBRect.width() - BAR_SEEKER_ICON_SIZE/2) && event.getX() < (colorBarBRect.left + Color.blue(currentColor)/255f * colorBarBRect.width() + BAR_SEEKER_ICON_SIZE/2))
            if(event.getY() > colorBarBRect.top - BAR_SEEKER_ICON_SIZE/2 && event.getY() < colorBarBRect.top + BAR_SEEKER_ICON_SIZE/2 )
                isEditColorB = true;

        if (event.getX() > (colorBarARect.left + Color.alpha(currentColor)/255f * colorBarARect.width() - BAR_SEEKER_ICON_SIZE/2) && event.getX() < (colorBarARect.left + Color.alpha(currentColor)/255f * colorBarARect.width() + BAR_SEEKER_ICON_SIZE/2))
            if(event.getY() > colorBarARect.top - BAR_SEEKER_ICON_SIZE/2 && event.getY() < colorBarARect.top + BAR_SEEKER_ICON_SIZE/2 )
                isEditColorA = true;



    }

    @Override
    public void disableEdit() {
        isEditDrawer = false;
        isEditEraser = false;
        isEditColorPicker = false;
        isEditColorR = false;
        isEditColorG = false;
        isEditColorB = false;
        isEditColorA = false;
    }

    @Override
    public void handleEdit(MotionEvent event) {
        config.setSizeModifier(1/box.getInputManager().getView().getContentScale());
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
            redrawColorBars();
        }

        if (isEditColorR)
        {
            currentColor = Color.argb(Color.alpha(currentColor), (int)Math.min(255f, Math.max(0f, (event.getRawX() - colorBarRRect.left)/colorBarRRect.width()*255)), Color.green(currentColor), Color.blue(currentColor));
            redrawColorBars();
        }

        if (isEditColorG)
        {
            currentColor = Color.argb(Color.alpha(currentColor), Color.red(currentColor), (int)Math.min(255f, Math.max(0f, (event.getRawX() - colorBarRRect.left)/colorBarRRect.width()*255)), Color.blue(currentColor));
            redrawColorBars();
        }

        if (isEditColorB)
        {
            currentColor = Color.argb(Color.alpha(currentColor), Color.red(currentColor), Color.green(currentColor), (int)Math.min(255f, Math.max(0f, (event.getRawX() - colorBarRRect.left)/colorBarRRect.width()*255)));
            redrawColorBars();
        }

        if (isEditColorA)
        {
            currentColor = Color.argb((int)Math.min(255f, Math.max(0f, (event.getRawX() - colorBarRRect.left)/colorBarRRect.width()*255)), Color.red(currentColor), Color.green(currentColor), Color.blue(currentColor));
            redrawColorBars();
        }

        if (drawActive && event.getY() < boxBackground.top)
            box.getInputManager().getView().getcCanvas().applyTool(brush, config, (int) event.getRawX(), (int) event.getRawY());

        if (eraseActive && event.getY() < boxBackground.top)
            box.getInputManager().getView().getcCanvas().erase(brush, config, (int) event.getRawX(), (int) event.getRawY());
    }

    private void redrawColorBars()
    {
        Allocation in = Allocation.createFromBitmap(rs, colorBarR);
        aout = Allocation.createTyped(rs, in.getType());
        script.forEach_FillRed(in, aout);
        aout.copyTo(colorBarR);

        in = Allocation.createFromBitmap(rs, colorBarG);
        aout = Allocation.createTyped(rs, in.getType());
        script.forEach_FillGreen(in, aout);
        aout.copyTo(colorBarG);

        in = Allocation.createFromBitmap(rs, colorBarB);
        aout = Allocation.createTyped(rs, in.getType());
        script.forEach_FillBlue(in, aout);
        aout.copyTo(colorBarB);

        in = Allocation.createFromBitmap(rs, colorBarA);
        aout = Allocation.createTyped(rs, in.getType());
        script.forEach_FillAlpha(in, aout);
        aout.copyTo(colorBarA);

        config.setColor(currentColor);
    }
}
