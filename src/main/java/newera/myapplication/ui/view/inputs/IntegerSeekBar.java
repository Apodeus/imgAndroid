package newera.myapplication.ui.view.inputs;

import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import newera.myapplication.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by echo on 21/02/2017.
 */
public class IntegerSeekBar implements IInputBox{

    private InputManager manager;
    private int currentValue = 0;
    private int initialValue = 0;
    private int minValue = -100;
    private int maxValue = 100;
    private int length;
    private int viewWidth;
    private int viewHeight;
    private Paint paint;
    private boolean isEdit = false;
    private float initialX;

    private String label;
    private String plusSign;
    private Bitmap cursorIconBitmap;
    private Bitmap applyIconBitmap;
    private Bitmap cancelIconBitmap;

    private Rect boxBackground;
    private Rect barBackground;
    private Rect barForeground;

    private int boxBackgroundColor;
    private int boxBorderColor;
    private int textColor;
    private int barBackgroundColor;
    private int barForegroundColor;

    private float barTik;

    private boolean init = false;

    private final static float BOX_BOTTOM_OFFSET_Y = 0.05f;
    private final static float BOX_HEIGHT_COVERAGE = 0.10f;
    private final static float BOX_WIDTH_COVERAGE = 0.85f;
    private final static float BOX_BORDER_THICKNESS = 0.7f;
    private final static int TEXT_SIZE = 40;
    private final static int ICON_SIZE = 125;
    private final static int CURSOR_SIZE = 150;

    private final static float BAR_WIDTH_COVERAGE = 0.75f;
    private final static int BAR_THICKNESS = 25;

    private final static float PAINT_ALPHA = 0.8f;


    public IntegerSeekBar(InputManager v)
    {
        this.manager = v;
    }

    private void initialize(Canvas canvas, String label)
    {
        this.viewWidth = canvas.getWidth();
        this.viewHeight = canvas.getHeight();

        this.length = maxValue - minValue;

        this.label = label;
        this.plusSign = "";

        this.boxBackgroundColor = manager.getView().getResources().getColor(R.color.colorPrimaryMild);
        this.boxBorderColor = manager.getView().getResources().getColor(R.color.colorAccent);
        this.textColor = manager.getView().getResources().getColor(R.color.colorLight);
        this.barBackgroundColor = manager.getView().getResources().getColor(R.color.colorPrimary);
        this.barForegroundColor = manager.getView().getResources().getColor(R.color.colorAccent);

        this.boxBackground = new Rect((int) (viewWidth * (1 - BOX_WIDTH_COVERAGE)) / 2, (int) (viewHeight * (1 - BOX_BOTTOM_OFFSET_Y - BOX_HEIGHT_COVERAGE)), (int) (viewWidth - viewWidth * (1 - BOX_WIDTH_COVERAGE) / 2), (int) (viewHeight * (1 - BOX_BOTTOM_OFFSET_Y)));
        this.barBackground = new Rect((int) (viewWidth * (1 - BAR_WIDTH_COVERAGE)/2), (int) (boxBackground.top + 0.1f * BOX_HEIGHT_COVERAGE * viewHeight), (int) (viewWidth - viewWidth * (1 - BAR_WIDTH_COVERAGE) / 2), (int) (boxBackground.top + 0.1f * BOX_HEIGHT_COVERAGE * viewHeight + BAR_THICKNESS));
        this.barTik = viewWidth * BAR_WIDTH_COVERAGE / length;
        this.barForeground = new Rect(barBackground.left, barBackground.top , barBackground.left + (int) (initialValue * barTik), barBackground.bottom);

        this.paint = new Paint();

        Drawable cursorD = manager.getView().getResources().getDrawable(R.drawable.ic_location_on_black_24dp);
        Drawable applyD = manager.getView().getResources().getDrawable(R.drawable.ic_check_black_24dp);
        Drawable cancelD = manager.getView().getResources().getDrawable(R.drawable.ic_clear_black_24dp);

        cursorIconBitmap = Bitmap.createBitmap(CURSOR_SIZE, CURSOR_SIZE, Bitmap.Config.ARGB_8888);
        applyIconBitmap = Bitmap.createBitmap(ICON_SIZE, ICON_SIZE, Bitmap.Config.ARGB_8888);
        cancelIconBitmap = Bitmap.createBitmap(ICON_SIZE, ICON_SIZE, Bitmap.Config.ARGB_8888);
        Canvas cursorIconCanvas = new Canvas(cursorIconBitmap);
        Canvas applyIconCanvas = new Canvas(applyIconBitmap);
        Canvas cancelIconCanvas = new Canvas(cancelIconBitmap);

        cursorD.setColorFilter(manager.getView().getResources().getColor(R.color.colorLight), PorterDuff.Mode.SRC_ATOP);
        cursorD.setBounds(0,0,CURSOR_SIZE,CURSOR_SIZE);
        cursorD.draw(cursorIconCanvas);

        applyD.setColorFilter(manager.getView().getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
        applyD.setBounds(0,0,ICON_SIZE,ICON_SIZE);
        applyD.draw(applyIconCanvas);

        cancelD.setColorFilter(manager.getView().getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
        cancelD.setBounds(0,0,ICON_SIZE,ICON_SIZE);
        cancelD.draw(cancelIconCanvas);

        this.init = true;
    }

    public void displayPlus(boolean on)
    {
        if (on)
            plusSign = "+";
        else
            plusSign = "";
    }

    public void drawBox(Canvas canvas) {
        if (!init)
            initialize(canvas, label);

        barForeground.right = barBackground.left + (int) ((currentValue + length - maxValue) * barTik);
        paint.setColor(boxBackgroundColor);
        paint.setAlpha((int) (PAINT_ALPHA * 255));
        canvas.drawRect(boxBackground, paint);

        paint.setColor(barBackgroundColor);
        paint.setAlpha((int) (PAINT_ALPHA * 255));
        canvas.drawRect(barBackground, paint);

        paint.setColor(barForegroundColor);
        paint.setAlpha((int) (PAINT_ALPHA * 255));
        canvas.drawRect(barForeground, paint);

        canvas.drawBitmap(cursorIconBitmap, barForeground.right - CURSOR_SIZE / 2, barBackground.top - CURSOR_SIZE, paint);
        canvas.drawBitmap(applyIconBitmap, boxBackground.right - ICON_SIZE, boxBackground.bottom - ICON_SIZE, paint);
        canvas.drawBitmap(cancelIconBitmap, boxBackground.left, boxBackground.bottom - ICON_SIZE, paint);

        paint.setColor(textColor);
        paint.setTextSize(TEXT_SIZE);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(label + " : " + (currentValue > 0 ? plusSign : "") + currentValue, viewWidth / 2, boxBackground.top + (boxBackground.bottom - boxBackground.top) / 2, paint);
    }

    public boolean handleTouch(MotionEvent event)
    {
        if (event.getX() > barForeground.right - CURSOR_SIZE/2 && event.getX() < barForeground.right + CURSOR_SIZE/2) {

            if (event.getY() > barBackground.top - CURSOR_SIZE && event.getY() < barBackground.top) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    isEdit = true;
                }
            }
        }

        if (isEdit && event.getAction() == MotionEvent.ACTION_MOVE)
        {
            currentValue =  Math.max(minValue, Math.min(maxValue, (int) ( minValue +(event.getRawX() - barBackground.left) / barTik)));
        }

        if (event.getAction() == MotionEvent.ACTION_UP)
        {
            isEdit = false;
            manager.onPreviewFilter(currentValue, getParams());
            if (event.getY() > boxBackground.bottom - ICON_SIZE && event.getY() < boxBackground.bottom)
            if (event.getX() > boxBackground.left && event.getX() < boxBackground.left + ICON_SIZE)
            {
                manager.onCancelFilter();
            } else if (event.getX() < boxBackground.right && event.getX() > boxBackground.right - ICON_SIZE)
            {
                manager.onApplyFilter(currentValue, getParams());
            }

        }

        return isEdit;
    }

    public int getValue()
    {
        return currentValue;
    }

    public void reset()
    {
        this.init = false;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public Map<String, Object> getParams() {
        Map<String, Object> result = new HashMap<>();
        result.put("value", currentValue);

        return result;
    }

    public void setBounds(int minValue, int maxValue, int initialValue)
    {
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.initialValue = initialValue;
    }
}
