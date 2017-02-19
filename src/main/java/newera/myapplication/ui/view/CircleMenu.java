package newera.myapplication.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import newera.myapplication.MainActivity;
import newera.myapplication.R;
import newera.myapplication.image.processing.shaders.ChangeHue;
import newera.myapplication.image.processing.shaders.GrayScale;
import newera.myapplication.image.processing.shaders.InvertColor;
import newera.myapplication.image.processing.shaders.Lightness;
import newera.myapplication.image.processing.shaders.Shader;
import newera.myapplication.ui.system.PictureFileManager;

/**
 * Created by Emile Barjou-Suire on 11/02/2017.
 */

public class CircleMenu extends View {
    private MainActivity activity;

    public enum Position {TOP_LEFT, TOP_RIGHT, BOT_LEFT, BOT_RIGHT};

    //Radius of menu minimized and extended, in percent of min(width, height)
    private final static double RADIUS_MIN = 0.10;
    private final static double RADIUS_EXT = 0.50;
    private final static double TOUCH_MARGIN = 1.25;
    private final static double ITEM_CIRCLE_RADIUS = 0.90;
    private final static double ITEM_CIRCLE_MARGIN = 0.05;
    private final static double DISPLAY_MARGIN = 1.03;
    private final static int CLICK_DEAD_ZONE = 5;
    private final static int TEXT_SIZE = 50;
    private final static int TEXT_BORDER_SIZE = 4;
    private final static int PositionArray[][] = {{0,0}, {1,0}, {0,1}, {1,1},};
    private final static int DIM_FACTOR = 128;

    private Paint paint;
    private int width, height, min_wh, cornerX, cornerY;
    private int radius, initialRadius, extRadius;
    private boolean shouldExtand, isExtanded, touchIsExt, transisionLock, touchLock;
    private Position position;

    private List<MenuItem> itemList;
    private int itemRadius, itemCircleRadius;
    private float itemListAngle;
    private float itemAngle;
    private int initialTx, initialTy;
    private float initialItemAngle;

    private boolean movingCircle;
    private int currentPositionX;
    private int currentPositionY;

    private CImageView view;

    public CircleMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.isExtanded = false;
        this.shouldExtand = false;
        this.transisionLock = false;
        this.position = Position.TOP_RIGHT;
        this.paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextSize(TEXT_SIZE);

        this.itemList = new ArrayList<MenuItem>();

        this.itemAngle = 0;

        this.movingCircle = false;
    }

    @Override
    public void onDraw(Canvas canvas){
        double angle;
        int x, y, x2, y2;;

        transition();

        paint.setColor(getResources().getColor(R.color.colorAccent));
        if (!movingCircle)
        {

            canvas.drawARGB((isExtanded?DIM_FACTOR:0), 0, 0, 0);
            drawCircle(canvas, cornerX, cornerY, radius);

            if (isExtanded){
                for (int i = 0; i < itemList.size(); ++i){
                    angle = (Math.toRadians(i * itemListAngle) - Math.toRadians(itemAngle)) % (2 * Math.PI);

                    // ===== draw circle item ====
                    paint.setColor(getResources().getColor(R.color.colorPrimaryDark));
                    x = (int)(cornerX + (itemCircleRadius) * Math.cos(angle));
                    y = (int)(cornerY + (itemCircleRadius) * Math.sin(angle));
                    itemList.get(i).setRect(drawCircle(canvas, x, y, itemRadius));


                    // ==== Draw item name ====
                    Paint border = new Paint();
                    border.setAntiAlias(true);
                    border.setTextSize(TEXT_SIZE);
                    border.setStyle(Paint.Style.STROKE);
                    border.setStrokeWidth(TEXT_BORDER_SIZE);

                    border.setColor(Color.BLACK);
                    paint.setColor(Color.WHITE);
                    String text = itemList.get(i).getName();

                    x2 = (int)(cornerX + radius*DISPLAY_MARGIN * Math.cos(angle));
                    y2 = (int)(cornerY + radius*DISPLAY_MARGIN * Math.sin(angle));

                    canvas.save();

                    canvas.rotate((float) Math.toDegrees(angle) + 180*PositionArray[position.ordinal()][0], x2, y2);
                    if (PositionArray[position.ordinal()][0] == 0){
                        border.setTextAlign(Paint.Align.LEFT);
                        paint.setTextAlign(Paint.Align.LEFT);
                    } else {
                        border.setTextAlign(Paint.Align.RIGHT);
                        paint.setTextAlign(Paint.Align.RIGHT);
                    }

                    //canvas.drawText(text, x, y, paint);

                /*if((angle >= 0 && angle < Math.PI / 2) || (angle <= 0 && angle > -1*(Math.PI / 2))
                        || (angle < -1*(3*Math.PI /2) && angle >= -2 * Math.PI)|| (angle > 3*Math.PI/2 && angle <= 2*Math.PI)){
                    border.setTextAlign(Paint.Align.LEFT);
                    paint.setTextAlign(Paint.Align.LEFT);
                    canvas.rotate((float) Math.toDegrees(angle), x2, y2);
                } else {

                    canvas.rotate((float) Math.toDegrees(angle) + 180, x2, y2);
                }*/
                    canvas.drawText(text, x2, y2, border);
                    canvas.drawText(text, x2, y2, paint);

                    canvas.restore();
                }
            }
        }else{
            drawCircle(canvas, currentPositionX, currentPositionY, initialRadius);
        }
    }

    @Override
    public void onMeasure(int w, int h){
        width = MeasureSpec.getSize(w);
        height = MeasureSpec.getSize(h);
        min_wh = Math.min(width, height);

        initialRadius = (int)((double)min_wh*RADIUS_MIN);
        extRadius = (int)((double)min_wh*RADIUS_EXT);
        radius = initialRadius;

        updateItemDisplay();

        setCircleMenuMeasures();

        setMeasuredDimension(width, height);
    }

    private void setCircleMenuMeasures()
    {
        cornerX = width*PositionArray[position.ordinal()][0];
        cornerY = height*PositionArray[position.ordinal()][1];
        currentPositionX = cornerX;
        currentPositionY = cornerY;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                if (dist((int)event.getX(), (int)event.getY(), cornerX, cornerY) > radius*TOUCH_MARGIN){
                    if (isExtanded){
                        shouldExtand = false;
                    } else {
                        return false;
                    }
                }
                transisionLock = true;

                initialTx = (int)event.getX();
                initialTy = (int)event.getY();
                initialItemAngle = itemAngle;
                touchIsExt = false;


            } break;

            case MotionEvent.ACTION_MOVE: {
                double distFromCorner = dist((int)event.getX(), (int)event.getY(), cornerX, cornerY);
                double distFromCornerInit = dist(cornerX, cornerY, initialTx, initialTy);
                if ( distFromCorner < extRadius*TOUCH_MARGIN && distFromCorner > initialRadius*(1.0/TOUCH_MARGIN) && !movingCircle ) {
                    if (!isExtanded && Math.abs(distFromCorner-distFromCornerInit) > (initialRadius/2)*TOUCH_MARGIN) {
                        touchIsExt = true;
                        isExtanded = false;
                    }
                    if (isExtanded && Math.abs(distFromCorner-distFromCornerInit) > (extRadius/3)*TOUCH_MARGIN) {
                        touchIsExt = true;
                        isExtanded = false;
                    }

                    if (touchIsExt) {
                        radius = (int)dist((int)event.getX(), (int)event.getY(), cornerX, cornerY);
                    }
                }

                if (!isExtanded && Math.abs(distFromCorner-distFromCornerInit) > (initialRadius*5)*TOUCH_MARGIN) {
                    movingCircle = true;
                    currentPositionX = (int) event.getX();
                    currentPositionY = (int) event.getY();
                }

                if (!touchIsExt){
                    float angle = (float)Math.toDegrees(Math.atan2(cornerX - event.getX(), cornerY - event.getY())) % 360;
                    float initialAngle = (float)Math.toDegrees(Math.atan2(cornerX - initialTx, cornerY - initialTy)) % 360;
                    this.itemAngle = initialItemAngle + (angle - initialAngle);
                }
            } break;

            case MotionEvent.ACTION_UP: {
                transisionLock = false;

                if (movingCircle)
                {
                    boolean right = currentPositionX > getWidth() / 2;
                    boolean down = currentPositionY > getHeight() / 2;
                    if (!right && !down)
                        position = Position.TOP_LEFT;
                    if (!right && down)
                        position = Position.BOT_LEFT;
                    if (right && !down)
                        position = Position.TOP_RIGHT;
                    if (right && down)
                        position = Position.BOT_RIGHT;

                    movingCircle = false;
                    setCircleMenuMeasures();
                    invalidate();
                }

                if (touchIsExt){
                    if ((extRadius-initialRadius)/2 > dist((int)event.getX(), (int)event.getY(), cornerX, cornerY)){
                        shouldExtand = false;
                    } else {
                        shouldExtand = true;
                    }
                }

                /*
                * Handle click
                */
                if (!touchIsExt && dist(initialTx, initialTy, (int)event.getX(), (int)event.getY()) < CLICK_DEAD_ZONE){
                    for (int i = 0; i < itemList.size(); ++i){
                        if (itemList.get(i).contains((int)event.getX(), (int)event.getY())){
                            /*
                             * Temporary Switch case to test the menu and impletations of functionality
                             */
                            switch(i){
                                case 0:
                                    PictureFileManager.LoadPictureFromGallery();
                                    break;
                                case 1:
                                    PictureFileManager.CreatePictureFileFromCamera();
                                    break;
                                default:
                                    if (itemList.get(i).isShader()) {
                                        itemList.get(i).getShader().onClick();
                                    }
                                    break;
                            }
                            /*
                             */
                            shouldExtand = false;
                        }
                    }
                }
            } break;
        }
        invalidate();
        return true;
    }

    /*
           Following code only for testing purpose
    */
    public void initialize()
    {
        for(int i = 0; i <20; ++i){
            switch(i) {
                case 3 :
                    Shader s = new GrayScale(this.activity);
                    this.addItem(new MenuItem(s.getName(), s));
                    break;
                case 4 :
                    Shader invert = new InvertColor(this.activity);
                    this.addItem(new MenuItem(invert.getName(), invert));
                    break;

                case 5 :
                    Shader lightness = new Lightness(this.activity);
                    this.addItem(new MenuItem(lightness.getName(), lightness));
                    break;

                case 6 :
                    Shader changeHue = new ChangeHue(this.activity);
                    this.addItem(new MenuItem(changeHue.getName(), changeHue));
                    break;
                default :
                    this.addItem(new MenuItem("Item n°" + i));
                    break;
            }
        }
        itemList.get(0).string = "Gallery";
        itemList.get(1).string = "Camera";
    }

    public void setView(CImageView view)
    {
        this.view = view;
    }

    public void setActivity(MainActivity activity)
    {
        this.activity = activity;
    }

    private void transition(){
        if (!transisionLock ){
            if (shouldExtand && radius < extRadius) {
                radius += (extRadius - radius) / 2 + 10;
                invalidate();
            } else if (shouldExtand && radius > extRadius){
                radius -= (radius - extRadius) / 2;
                invalidate();
            } else if (!shouldExtand && radius > initialRadius){
                radius -= ((radius-initialRadius)/2 + 10);
                invalidate();
            } else if (!shouldExtand && radius < initialRadius){
                radius += (initialRadius-radius)/2;
                invalidate();
            }
            if (Math.abs(radius-initialRadius) <= initialRadius/100){
                radius = initialRadius;
                isExtanded = false;
            } else if(Math.abs(radius-extRadius) <= extRadius/100){
                radius = extRadius;
                isExtanded = true;
            }
        }
    }

    private void updateItemDisplay(){
        int margin = (int)(extRadius*(1-ITEM_CIRCLE_RADIUS));
        itemListAngle = 360f/itemList.size();
        itemRadius = (int)(((extRadius*2*Math.PI)/itemList.size())/2);
        itemCircleRadius = extRadius-itemRadius;
        itemRadius = (int)((((itemCircleRadius*2*Math.PI)/itemList.size())/2)*(1-ITEM_CIRCLE_MARGIN));
        Log.i("DBG", "item : angle=" +itemListAngle+"°, r="+itemRadius);
    }

    private void addItem(MenuItem item){
        itemList.add(item);
        updateItemDisplay();
    }

    private RectF rectFromCircle(int x, int y, int r){
        return new RectF(x - r, y - r, x + r, y + r);
    }

    private RectF drawCircle(Canvas canvas, int x, int y, int r){
        RectF rect = rectFromCircle(x, y, r);
        canvas.drawOval(rect, paint);
        return rect;
    }

    private double dist(int x1, int y1, int x2, int y2){
        return Math.hypot(x1 - x2, y1 - y2);
    }

    private class MenuItem{
        private RectF rect;
        private String string;
        private Shader shader;

        public MenuItem(String string){
            this.string = string;
            this.rect = new RectF();
            this.shader = null;
        }

        public MenuItem(String string, Shader shader)
        {
            this.string = string;
            this.rect = new RectF();
            this.shader = shader;
        }

        public void setRect(float left, float top, float right, float bottom){
            rect.set(left, top, right, bottom);
        }

        public void setRect(RectF rectf){
            rect.set(rectf.left, rectf.top, rectf.right, rectf.bottom);
        }

        /**
         * @return the reference of the rect
         */
        public RectF getRect(){
            return rect;
        }

        public String getName(){
            return string;
        }

        public boolean contains(int x, int y){
            return rect.contains(x, y);
        }

        public boolean isShader()
        {
            return shader != null;
        }

        public Shader getShader()
        {
            return this.shader;
        }
    }
}
