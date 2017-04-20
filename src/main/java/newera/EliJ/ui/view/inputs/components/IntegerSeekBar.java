package newera.EliJ.ui.view.inputs.components;

import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import newera.EliJ.R;
import newera.EliJ.ui.view.inputs.GenericBox;

/**
 * Created by echo on 24/02/2017.
 */
public class IntegerSeekBar implements IGenericBoxComponent {

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
    private Bitmap cursorIconBitmap;
    private boolean isEdit = false;

    private float barTik;

    private final static int TEXT_SIZE = 40;
    private final static int CURSOR_SIZE = 150;
    private final static float BAR_WIDTH_COVERAGE = 0.75f;
    private final static int BAR_THICKNESS = 25;
    private final static float BOX_BOTTOM_OFFSET_Y = 0.05f;
    private final static float BOX_HEIGHT_COVERAGE = 0.08f;
    private final static float BOX_WIDTH_COVERAGE = 0.85f;
    private final static float BOX_BORDER_THICKNESS = 0.7f;
    private final static float PAINT_ALPHA = 0.8f;

    private int viewWidth;
    private int viewHeight;
    private Paint paint;
    private int genericBoxOffset;


    public IntegerSeekBar(GenericBox box)
    {
        this.box = box;
    }

    @Override
    public int getHeight() {
        return boxBackground.bottom - boxBackground.top;
    }

    @Override
    public void setStartingHeight(int height) {
        genericBoxOffset = height;
    }

    @Override
    public Object getValue() {
        return currentValue;
    }

    @Override
    public void initialize(int[] settings) {
        minValue = settings[0];
        maxValue = settings[1];
        currentValue = settings[2];
        this.viewWidth = box.getCanvasWidth();
        this.viewHeight = box.getCanvasHeight();

        this.length = maxValue - minValue;

        this.plusSign = "";
        this.boxBackgroundColor = box.getInputManager().getView().getResources().getColor(R.color.colorPrimaryMild);
        this.boxBorderColor = box.getInputManager().getView().getResources().getColor(R.color.colorAccent);
        this.textColor = box.getInputManager().getView().getResources().getColor(R.color.colorLight);
        this.barBackgroundColor = box.getInputManager().getView().getResources().getColor(R.color.colorPrimary);
        this.barForegroundColor = box.getInputManager().getView().getResources().getColor(R.color.colorAccent);

        this.boxBackground = new Rect((int) (viewWidth * (1 - BOX_WIDTH_COVERAGE)) / 2, (int) (viewHeight * (1 - BOX_BOTTOM_OFFSET_Y - BOX_HEIGHT_COVERAGE) - genericBoxOffset), (int) (viewWidth - viewWidth * (1 - BOX_WIDTH_COVERAGE) / 2), (int) (viewHeight * (1 - BOX_BOTTOM_OFFSET_Y) - genericBoxOffset));
        this.barBackground = new Rect((int) (viewWidth * (1 - BAR_WIDTH_COVERAGE)/2), (int) (boxBackground.top + 0.1f * BOX_HEIGHT_COVERAGE * viewHeight), (int) (viewWidth - viewWidth * (1 - BAR_WIDTH_COVERAGE) / 2), (int) (boxBackground.top + 0.1f * BOX_HEIGHT_COVERAGE * viewHeight + BAR_THICKNESS));
        this.barTik = viewWidth * BAR_WIDTH_COVERAGE / length;
        this.barForeground = new Rect(barBackground.left, barBackground.top , barBackground.left + (int) (initialValue * barTik), barBackground.bottom);

        this.paint = new Paint();

        Drawable cursorD = box.getInputManager().getView().getResources().getDrawable(R.drawable.ic_location_on_black_24dp);
        Matrix m = new Matrix();
        m.postRotate(90);
        Bitmap o = Bitmap.createBitmap(CURSOR_SIZE, CURSOR_SIZE, Bitmap.Config.ARGB_8888);
        Canvas cursorIconCanvas = new Canvas(o);

        cursorD.setColorFilter(box.getInputManager().getView().getResources().getColor(R.color.colorLight), PorterDuff.Mode.SRC_ATOP);
        cursorD.setBounds(0,0,CURSOR_SIZE,CURSOR_SIZE);
        cursorD.draw(cursorIconCanvas);
        cursorIconBitmap = Bitmap.createBitmap(o, 0, 0, CURSOR_SIZE, CURSOR_SIZE, m, true);
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
        return false;
    }

    @Override
    public void draw(Canvas canvas) {
        barForeground.right = barBackground.left + (int) ((currentValue + length - maxValue) * barTik);

        paint.setColor(barBackgroundColor);
        paint.setAlpha((int) (PAINT_ALPHA * 255));
        canvas.drawRect(barBackground, paint);

        paint.setColor(barForegroundColor);
        paint.setAlpha((int) (PAINT_ALPHA * 255));
        canvas.drawRect(barForeground, paint);

        canvas.drawBitmap(cursorIconBitmap, barForeground.right - CURSOR_SIZE / 12.5f, barBackground.top + BAR_THICKNESS / 2 - CURSOR_SIZE/2, paint);

        paint.setColor(textColor);
        paint.setTextSize(TEXT_SIZE);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(label + " : " + (currentValue > 0 ? plusSign : "") + currentValue, viewWidth / 2, boxBackground.top + getHeight()/2 + TEXT_SIZE, paint);

    }

    @Override
    public void enableEdit(MotionEvent event) {
        if (event.getX() > barForeground.right && event.getX() < barForeground.right + CURSOR_SIZE)
            if (event.getY() > barForeground.top + BAR_THICKNESS / 2 - CURSOR_SIZE * 0.75f && event.getY() < barForeground.top + BAR_THICKNESS / 2 + CURSOR_SIZE * 0.75f)
                isEdit = true;
    }

    @Override
    public void disableEdit() {
        isEdit = false;
    }

    @Override
    public void handleEdit(MotionEvent event) {
        if (isEdit)
        {
            currentValue =  Math.max(minValue, Math.min(maxValue, (int) ( minValue +(event.getRawX() - barBackground.left - CURSOR_SIZE/2.5f) / barTik)));
        }
    }
}
