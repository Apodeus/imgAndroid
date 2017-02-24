package newera.myapplication.ui.view.inputs;

import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import newera.myapplication.R;
import newera.myapplication.ui.view.inputs.components.*;
import newera.myapplication.ui.view.inputs.components.IntegerSeekBar;

import java.util.ArrayList;
import java.util.List;

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
    private int boxBorderColor;
    private int textColor;

    private List<IGenericBoxComponent> components;
    private List<InputDataType> askedValues;
    private boolean init = false;

    private final static float BOX_BOTTOM_OFFSET_Y = 0.05f;
    private final static float BOX_HEIGHT_COVERAGE = 0.05f;
    private final static float BOX_WIDTH_COVERAGE = 0.85f;
    private final static float BOX_BORDER_THICKNESS = 0.7f;
    private final static int TEXT_SIZE = 40;
    private final static int ICON_SIZE = 125;

    private final static float PAINT_ALPHA = 0.8f;

    public GenericBox(InputManager manager, List<InputDataType> askedValues)
    {
        this.manager = manager;
        this.askedValues = askedValues;
    }

    private void initialize(Canvas canvas, String label)
    {
        this.viewWidth = canvas.getWidth();
        this.viewHeight = canvas.getHeight();

        this.label = label;

        this.boxBackgroundColor = manager.getView().getResources().getColor(R.color.colorPrimaryMild);
        this.boxBorderColor = manager.getView().getResources().getColor(R.color.colorAccent);
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
                default:
                    c = new Label(this);
                    break;
            }

            c.setIndex(i);
            c.setLabel(d.getLabel());
            c.setStartingHeight(currentAdditionalHeight + ICON_SIZE);
            c.initialize(d.getSettings());
            currentAdditionalHeight += c.getHeight();
            components.add(c);
        }

        this.extendedBoxBackground = new Rect(collapsedBoxBackground.left, collapsedBoxBackground.top + currentAdditionalHeight, collapsedBoxBackground.right, collapsedBoxBackground.bottom);
        this.currentBoxBackground = extendedBoxBackground;

        this.init = true;
    }

    public void drawBox(Canvas canvas) {
        if (!init)
            initialize(canvas, "test");

        paint.setColor(boxBackgroundColor);
        paint.setAlpha((int) (PAINT_ALPHA * 255));
        canvas.drawRect(currentBoxBackground, paint);

        canvas.drawBitmap(applyIconBitmap, currentBoxBackground.right - ICON_SIZE, currentBoxBackground.bottom - ICON_SIZE, paint);
        canvas.drawBitmap(cancelIconBitmap, currentBoxBackground.left, currentBoxBackground.bottom - ICON_SIZE, paint);

        paint.setColor(textColor);
        paint.setTextSize(TEXT_SIZE);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(label, viewWidth/2, currentBoxBackground.top + TEXT_SIZE * 1.5f, paint);

        if (isExtended)
        {
            for (IGenericBoxComponent c : components)
                c.draw(canvas);

            canvas.drawBitmap(collapseIconBitmap, (viewWidth - ICON_SIZE) / 2, currentBoxBackground.bottom - ICON_SIZE / 1.3f, paint);
        }else{
            canvas.drawBitmap(extendIconBitmap, (viewWidth - ICON_SIZE) / 2, currentBoxBackground.bottom - ICON_SIZE / 1.3f , paint);
        }
    }

    public boolean handleTouch(MotionEvent event)
    {
        if (event.getY() > currentBoxBackground.top && event.getY() < currentBoxBackground.bottom)
            if (event.getX() > currentBoxBackground.left && event.getX() < currentBoxBackground.right) {
                //Handle action_down
            }

        if (isEdit && event.getAction() == MotionEvent.ACTION_MOVE)
        {
            //Handle edit
        }

        if (event.getAction() == MotionEvent.ACTION_UP)
        {
            isEdit = false;
            //manager.onPreviewFilter(currentValue, getParams());
            if (event.getY() > currentBoxBackground.bottom - ICON_SIZE && event.getY() < currentBoxBackground.bottom)
                if (event.getX() > currentBoxBackground.left && event.getX() < currentBoxBackground.left + ICON_SIZE)
                {
                    manager.onCancelFilter();
                } else if (event.getX() < currentBoxBackground.right && event.getX() > currentBoxBackground.right - ICON_SIZE)
                {
                    //manager.onApplyFilter(currentValue, getParams());
                }

        }

        return isEdit;
    }

    public int getCanvasWidth()
    {
        return this.viewWidth;
    }

    public int getCanvasHeight()
    {
        return this.viewHeight;
    }

    public InputManager getInputManager()
    {
        return this.manager;
    }

}
