package newera.EliJ.ui.view.inputs;

import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import newera.EliJ.R;
import newera.EliJ.image.processing.tools.Brush;
import newera.EliJ.ui.view.CanvasTool;
import newera.EliJ.ui.view.EMethod;
import newera.EliJ.ui.view.ToolConfig;
import newera.EliJ.ui.view.inputs.components.*;
import newera.EliJ.ui.view.inputs.components.IntegerSeekBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by echo on 24/02/2017.
 */
public class GenericBox {
    private InputManager manager;
    private int viewWidth;
    private int viewHeight;
    private Paint paint;
    private boolean isEdit = false;
    private boolean isExtended = true;

    private String label;

    private Bitmap applyIconBitmap;
    private Bitmap cancelIconBitmap;
    private Bitmap extendIconBitmap;
    private Bitmap collapseIconBitmap;
    private Rect currentBoxBackground;

    private Rect extendedBoxBackground;
    private Rect collapsedBoxBackground;
    private int boxBackgroundColor;

    private int textColor;
    private List<IGenericBoxComponent> components;

    private List<InputDataType> askedValues;
    private boolean init = false;
    private final static float BOX_BOTTOM_OFFSET_Y = 0.05f;

    private final static float BOX_WIDTH_COVERAGE = 0.85f;
    private final static int TEXT_SIZE = 40;
    private final static int ICON_SIZE = 125;
    private final static float PAINT_ALPHA = 0.8f;
    private Bitmap zoneIconBitmap;
    private boolean isEditZone;

    private Brush zoneSelectorBrush;
    private ToolConfig zoneSelectorConfig;

    /**
     * Create a modular input box for user interaction on items.
     * @param manager InputManager parent of the box
     * @param label String to display as a title
     * @param askedValues List of asked settings to be set by user
     */
    GenericBox(InputManager manager, String label, List<InputDataType> askedValues)
    {
        this.manager = manager;
        this.label = label;
        this.askedValues = askedValues;
    }

    /**
     * Draw the box in the given canvas.
     * @param canvas eh.
     */
    void drawBox(Canvas canvas) {
        if (!init)
            initialize(canvas);

        if (components != null & isExtended)
        {
            int tsize = 0;
            for (IGenericBoxComponent c : components)
                tsize += c.getHeight();

            currentBoxBackground.top = collapsedBoxBackground.top - tsize;
        }

        paint.setColor(boxBackgroundColor);
        paint.setAlpha((int) (PAINT_ALPHA * 255));
        canvas.drawRect(currentBoxBackground, paint);

        canvas.drawBitmap(applyIconBitmap, currentBoxBackground.right - ICON_SIZE, currentBoxBackground.bottom - ICON_SIZE, paint);
        if (manager.getCategory() == ECategory.FILTER)
            canvas.drawBitmap(zoneIconBitmap, currentBoxBackground.right - ICON_SIZE * 2 - 15, currentBoxBackground.bottom - ICON_SIZE, paint);

        canvas.drawBitmap(cancelIconBitmap, currentBoxBackground.left, currentBoxBackground.bottom - ICON_SIZE, paint);

        paint.setColor(textColor);
        paint.setTextSize(TEXT_SIZE);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(label, viewWidth/2, currentBoxBackground.bottom - ICON_SIZE / 1.75f, paint);

        Canvas zoneIconCanvas = new Canvas(zoneIconBitmap);
        Drawable zoneD = manager.getView().getResources().getDrawable(R.drawable.ic_filter_center_focus_black_24dp);
        zoneD.setBounds(0,0,ICON_SIZE,ICON_SIZE);

        if (isEditZone)
        {
            zoneIconCanvas.drawColor(manager.getView().getResources().getColor(R.color.colorAccent));
            zoneD.setColorFilter(manager.getView().getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);
        }else{
            zoneIconCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.SRC);
            zoneD.setColorFilter(manager.getView().getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
        }

        zoneD.draw(zoneIconCanvas);

        if (isExtended)
        {
            for (IGenericBoxComponent c : components)
                c.draw(canvas);

            canvas.drawBitmap(collapseIconBitmap, (viewWidth - ICON_SIZE) / 2, currentBoxBackground.bottom - ICON_SIZE / 1.3f, paint);
        }else{
            canvas.drawBitmap(extendIconBitmap, (viewWidth - ICON_SIZE) / 2, currentBoxBackground.bottom - ICON_SIZE / 1.3f , paint);
        }

    }

    private void zoneDraw(MotionEvent event)
    {
            if (event.getY() < currentBoxBackground.top)
            {
                zoneSelectorConfig.setSizeModifier(1/manager.getView().getContentScale());
                isEdit = true;
                manager.getView().getcCanvas().applyTool(zoneSelectorBrush, zoneSelectorConfig, (int) event.getRawX(), (int) event.getRawY());
            }
    }
    /**
     * To be called by View's onTouchEvent. Deal with touchEvent on the box.
     * @param event MotionEvent given to the View as it.
     * @return True if event is destined to the box (you might want to ignore further checks)
     */
    boolean handleTouch(MotionEvent event)
    {
        if (event.getY() > currentBoxBackground.top && event.getY() < collapsedBoxBackground.top && event.getAction() == MotionEvent.ACTION_DOWN)
            if (event.getX() > currentBoxBackground.left) {
            isEdit = true;
                for (IGenericBoxComponent c : components)
                    c.enableEdit(event);
                //Handle action_down
            }

        //Handle edit
        for (IGenericBoxComponent c : components)
            c.handleEdit(event);

        if (isEditZone)
            zoneDraw(event);


        if (event.getAction() == MotionEvent.ACTION_UP)
        {

            for (IGenericBoxComponent c : components)
                c.disableEdit();

            if (!isEdit && event.getY() > currentBoxBackground.bottom - ICON_SIZE && event.getY() < currentBoxBackground.bottom)
                if (event.getX() > currentBoxBackground.left && event.getX() < currentBoxBackground.left + ICON_SIZE)
                {
                    manager.onCancel();
                }
                else if (!isEdit && event.getX() < currentBoxBackground.right && event.getX() > currentBoxBackground.right - ICON_SIZE) {
                    Map<String, Object> params = new HashMap<>();
                    for (int i = 0; i < components.size(); i++) {
                        String label = askedValues.get(i).getDataLabel();
                        Object value = components.get(i).getValue();
                        params.put(label, value);
                    }

                    manager.onConfirm(params);
                }else if (!isEdit && event.getX() < currentBoxBackground.right - ICON_SIZE - 15 && event.getX() > currentBoxBackground.right - ICON_SIZE*2 - 15)
                {
                    isEditZone = !isEditZone;
                    if (!isEditZone)
                        manager.getView().getcCanvas().reset();
                    else
                    {
                        manager.getView().getcCanvas().setMethod(EMethod.SELECTION);
                        manager.getView().getcCanvas().initialize(manager.getView().getImage());
                    }
                } else if (!isEdit){
                    isExtended = !isExtended;

                    if (!isExtended)
                        currentBoxBackground = collapsedBoxBackground;
                    else
                        currentBoxBackground = extendedBoxBackground;
                }

            isEdit = false;
        }

        return isEdit || isPictureEdit();
    }

    /**
     * @return Box's width for module placements and touch detections.
     */
    public int getCanvasWidth()
    {
        return this.viewWidth;
    }

    /**
     * @return Box's height for module placements and touch detections.
     */
    public int getCanvasHeight()
    {
        return this.viewHeight;
    }

    /**
     * @return InputManager for module update on general app's state.
     */
    public InputManager getInputManager()
    {
        return this.manager;
    }

    private void initialize(Canvas canvas)
    {

        this.viewWidth = canvas.getWidth();
        this.viewHeight = canvas.getHeight();

        this.boxBackgroundColor = manager.getView().getResources().getColor(R.color.colorPrimaryMild);
        this.textColor = manager.getView().getResources().getColor(R.color.colorLight);

        this.collapsedBoxBackground = new Rect((int) (viewWidth * (1 - BOX_WIDTH_COVERAGE)) / 2, (int) (viewHeight * (1 - BOX_BOTTOM_OFFSET_Y) - ICON_SIZE), (int) (viewWidth - viewWidth * (1 - BOX_WIDTH_COVERAGE) / 2), (int) (viewHeight * (1 - BOX_BOTTOM_OFFSET_Y)));

        this.paint = new Paint();

        Drawable applyD = manager.getView().getResources().getDrawable(R.drawable.ic_check_black_24dp);
        Drawable cancelD = manager.getView().getResources().getDrawable(R.drawable.ic_clear_black_24dp);
        Drawable extendD = manager.getView().getResources().getDrawable(R.drawable.ic_arrow_drop_up_black_24dp);
        Drawable collapseD = manager.getView().getResources().getDrawable(R.drawable.ic_arrow_drop_down_black_24dp);

        applyIconBitmap = Bitmap.createBitmap(ICON_SIZE, ICON_SIZE, Bitmap.Config.ARGB_8888);
        cancelIconBitmap = Bitmap.createBitmap(ICON_SIZE, ICON_SIZE, Bitmap.Config.ARGB_8888);
        extendIconBitmap = Bitmap.createBitmap(ICON_SIZE, ICON_SIZE, Bitmap.Config.ARGB_8888);
        collapseIconBitmap = Bitmap.createBitmap(ICON_SIZE, ICON_SIZE, Bitmap.Config.ARGB_8888);
        zoneIconBitmap = Bitmap.createBitmap(ICON_SIZE, ICON_SIZE, Bitmap.Config.ARGB_8888);

        Canvas applyIconCanvas = new Canvas(applyIconBitmap);
        Canvas cancelIconCanvas = new Canvas(cancelIconBitmap);
        Canvas extendIconCanvas = new Canvas(extendIconBitmap);
        Canvas collapseIconCanvas = new Canvas(collapseIconBitmap);

        applyD.setColorFilter(manager.getView().getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
        applyD.setBounds(0,0,ICON_SIZE,ICON_SIZE);
        applyD.draw(applyIconCanvas);

        cancelD.setColorFilter(manager.getView().getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
        cancelD.setBounds(0,0,ICON_SIZE,ICON_SIZE);
        cancelD.draw(cancelIconCanvas);

        extendD.setColorFilter(manager.getView().getResources().getColor(R.color.colorLight), PorterDuff.Mode.SRC_ATOP);
        extendD.setBounds(0,0,ICON_SIZE,ICON_SIZE);
        extendD.draw(extendIconCanvas);

        collapseD.setColorFilter(manager.getView().getResources().getColor(R.color.colorLight), PorterDuff.Mode.SRC_ATOP);
        collapseD.setBounds(0,0,ICON_SIZE,ICON_SIZE);
        collapseD.draw(collapseIconCanvas);

        int currentAdditionalHeight = 0;
        this.components = new ArrayList<>();
        for (int i = 0; i < askedValues.size(); i++)
        {
            InputDataType d = askedValues.get(i);
            IGenericBoxComponent c;
            switch (d.getInputType()) {
                case INTEGER_SEEKBAR:
                    c = new IntegerSeekBar(this);
                    break;
                case COLOR_PICKER:
                    c = new ColorPicker(this);
                    break;
                case HARD_DATA:
                    c = new HardData(this);
                    break;
                case DRAW:
                    c = new DrawInterface(this);
                    break;
                default:
                    c = new Label(this);
                    break;
            }

            c.setIndex(i);
            c.setLabel(d.getUserLabel());
            c.setStartingHeight(currentAdditionalHeight + ICON_SIZE);
            c.initialize(d.getSettings());
            currentAdditionalHeight += c.getHeight();
            components.add(c);
        }

        this.extendedBoxBackground = new Rect(collapsedBoxBackground.left, collapsedBoxBackground.top - currentAdditionalHeight, collapsedBoxBackground.right, collapsedBoxBackground.bottom);
        this.currentBoxBackground = extendedBoxBackground;

        zoneSelectorBrush = new Brush();
        zoneSelectorBrush.initialize(manager.getView().getContext());
        zoneSelectorConfig = new ToolConfig();
        zoneSelectorConfig.setColor(Color.argb(255, 0, 0, 200));
        this.init = true;
    }

    public boolean isPictureEdit() {
        boolean result = false;
        for (IGenericBoxComponent c : components)
            result = result || c.getEditStatus();

        return result;
    }

}
