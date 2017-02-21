package newera.myapplication.ui.view.inputs;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import newera.myapplication.MainActivity;
import newera.myapplication.ui.view.CImageView;
import newera.myapplication.R;

/**
 * Created by echo on 21/02/2017.
 */
public class IntegerSeekBar {

    private View view;
    private int currentValue = 50;
    private int initialValue = 50;
    private int minValue = 0;
    private int maxValue = 100;
    private int lenght;
    private int viewWidth;
    private int viewHeight;
    private Paint paint;

    private String label;

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
    private final static int TEXT_SIZE = 20;

    private final static float BAR_WIDTH_COVERAGE = 0.75f;
    private final static int BAR_THICKNESS = 25;

    private final static float PAINT_ALPHA = 0.8f;

    public IntegerSeekBar(View v)
    {
        this.view = v;
    }

    public void initialize(Canvas canvas, String label)
    {
        this.viewWidth = canvas.getWidth();
        this.viewHeight = canvas.getHeight();

        this.lenght = maxValue - minValue;

        this.label = label;

        this.boxBackgroundColor = view.getResources().getColor(R.color.colorPrimaryMild);
        this.boxBorderColor = view.getResources().getColor(R.color.colorAccent);
        this.textColor = view.getResources().getColor(R.color.colorLight);
        this.barBackgroundColor = view.getResources().getColor(R.color.colorPrimary);
        this.barForegroundColor = view.getResources().getColor(R.color.colorAccent);

        this.boxBackground = new Rect((int) (viewWidth * (1 - BOX_WIDTH_COVERAGE)) / 2, (int) (viewHeight * (1 - BOX_BOTTOM_OFFSET_Y - BOX_HEIGHT_COVERAGE)), (int) (viewWidth - viewWidth * (1 - BOX_WIDTH_COVERAGE) / 2), (int) (viewHeight * (1 - BOX_BOTTOM_OFFSET_Y)));
        this.barBackground = new Rect((int) (viewWidth * (1 - BAR_WIDTH_COVERAGE)/2), (int) (boxBackground.top + 0.1f * BOX_HEIGHT_COVERAGE * viewHeight), (int) (viewWidth - viewWidth * (1 - BAR_WIDTH_COVERAGE) / 2), (int) (boxBackground.top + 0.1f * BOX_HEIGHT_COVERAGE * viewHeight + BAR_THICKNESS));
        this.barTik = viewWidth * BAR_WIDTH_COVERAGE / lenght;
        this.barForeground = new Rect(barBackground.left, barBackground.top , barBackground.left + (int) (initialValue * barTik), barBackground.bottom);

        this.paint = new Paint();

        this.init = true;
    }

    public void drawBox(Canvas canvas)
    {
        if (!init)
            initialize(canvas, "Luminosité : +0");

        barForeground.right = barBackground.left + (int) (currentValue * barTik);
        paint.setColor(boxBackgroundColor);
        paint.setAlpha((int) (PAINT_ALPHA * 255));
        canvas.drawRect(boxBackground, paint);

        paint.setColor(barBackgroundColor);
        paint.setAlpha((int) (PAINT_ALPHA * 255));
        canvas.drawRect(barBackground, paint);

        paint.setColor(barForegroundColor);
        paint.setAlpha((int) (PAINT_ALPHA * 255));
        canvas.drawRect(barForeground, paint);

        paint.setColor(textColor);
        paint.setTextSize(TEXT_SIZE);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(label, viewWidth / 2, boxBackground.top + (boxBackground.bottom - boxBackground.top) / 2, paint);
    }

    public int getValue()
    {
        return currentValue;
    }

    public void reset()
    {
        this.init = false;
    }
}
