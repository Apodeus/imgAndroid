package newera.myapplication.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import newera.myapplication.R;
import newera.myapplication.ui.system.PictureFileManager;

/**
 * Created by Emile Barjou-Suire on 11/02/2017.
 */

public class CircleMenu extends View {
    public enum Position {TOP_LEFT, TOP_RIGHT, BOT_LEFT, BOT_RIGHT};

    //Radius of menu minimized and extended, in percent of min(width, height)
    private final static double RADIUS_MIN = 0.10;
    private final static double RADIUS_EXT = 0.50;
    private final static double TOUCH_MARGIN = 1.25;
    private final static int CLICK_DEAD_ZONE = 5;
    private final static int PositionArray[][] = {{0,0}, {1,0}, {0,1}, {1,1},};

    private Paint paint;
    private int width, height, min_wh, cornerX, cornerY;
    private int radius, initialRadius, extRadius;
    private boolean shouldExtand, isExtanded, touchIsExt, transisionLock, touchLock;
    private Position position;

    private List<MenuItem> itemList;
    private int itemAngle;
    private int initialTx, initialTy, initialItemAngle;

    public CircleMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.isExtanded = false;
        this.shouldExtand = false;
        this.transisionLock = false;
        this.position = Position.BOT_RIGHT;

        this.paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextSize(40);

        /*
            Following code only for testing purpose
         */
        this.itemList = new ArrayList<MenuItem>();
        for(int i = 0; i <20; ++i){
            itemList.add(new MenuItem("Item n°"+i));

        }
        itemList.get(0).string = "Gallery";
        itemList.get(1).string = "Camera";
        this.itemAngle = 0;
        /*
         */
    }

    @Override
    public void onDraw(Canvas canvas){
        double angle;
        int x, y, x2, y2;;

        transition();

        paint.setColor(getResources().getColor(R.color.colorAccent));
        drawCircle(canvas, cornerX, cornerY, radius);

        if (isExtanded){
            for (int i = 0; i < itemList.size(); ++i){
                angle = (Math.toRadians(i * 15) - Math.toRadians(itemAngle)) % (2 * Math.PI);

                // ===== draw circle item ====
                paint.setColor(getResources().getColor(R.color.colorPrimaryDark));
                x = (int)(cornerX + (radius - initialRadius * 0.75) * Math.cos(angle));
                y = (int)(cornerY + (radius - initialRadius * 0.75) * Math.sin(angle));
                itemList.get(i).setRect(x - (initialRadius/2), y - (initialRadius/2), x + (initialRadius/2), y + (initialRadius/2));
                drawCircle(canvas, x, y, initialRadius/2);

                // ==== Draw item name ====
                Paint border = new Paint();
                border.setAntiAlias(true);
                border.setTextSize(40);
                border.setStyle(Paint.Style.STROKE);
                border.setStrokeWidth(8);

                border.setColor(Color.BLACK);
                paint.setColor(Color.WHITE);

                String text = itemList.get(i).getName();

                x2 = (int)(cornerX + radius * Math.cos(angle));
                y2 = (int)(cornerY + radius * Math.sin(angle));

                canvas.save();

                if((angle >= 0 && angle < Math.PI / 2) || (angle <= 0 && angle > -1*(Math.PI / 2))
                        || (angle < -1*(3*Math.PI /2) && angle >= -2 * Math.PI)|| (angle > 3*Math.PI/2 && angle <= 2*Math.PI)){

                    border.setTextAlign(Paint.Align.LEFT);
                    paint.setTextAlign(Paint.Align.LEFT);
                    canvas.rotate((float) Math.toDegrees(angle), x2, y2);
                } else {

                    border.setTextAlign(Paint.Align.RIGHT);
                    paint.setTextAlign(Paint.Align.RIGHT);
                    canvas.rotate((float) Math.toDegrees(angle) + 180, x2, y2);
                }

                canvas.drawText(text, x2, y2, border);
                canvas.drawText(text, x2, y2, paint);

                canvas.restore();
            }
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

        cornerX = width*PositionArray[position.ordinal()][0];
        cornerY = height*PositionArray[position.ordinal()][1];

        setMeasuredDimension(width, height);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                transisionLock = true;

                initialTx = (int)event.getX();
                initialTy = (int)event.getY();
                initialItemAngle = itemAngle;
                touchIsExt = false;

                if (dist(initialTx, initialTy, cornerX, cornerY) > radius*TOUCH_MARGIN){
                    if (isExtanded){
                        shouldExtand = false;
                    } else {
                        return false;
                    }
                }
            } break;

            case MotionEvent.ACTION_MOVE: {
                double distFromCorner = dist((int)event.getX(), (int)event.getY(), cornerX, cornerY);
                double distFromCornerInit = dist(cornerX, cornerY, initialTx, initialTy);
                if ( distFromCorner < extRadius*TOUCH_MARGIN && distFromCorner > initialRadius*(1.0/TOUCH_MARGIN) ) {
                    if (!isExtanded && Math.abs(distFromCorner-distFromCornerInit) > (initialRadius/2)*TOUCH_MARGIN) {
                        touchIsExt = true;
                    }
                    if (isExtanded && Math.abs(distFromCorner-distFromCornerInit) > (extRadius/2)*TOUCH_MARGIN) {
                        touchIsExt = true;
                    }
                    if (touchIsExt) {
                        radius = (int)dist((int)event.getX(), (int)event.getY(), cornerX, cornerY);
                    }
                }

                if (!touchIsExt){
                    int angle = (int)Math.toDegrees(Math.atan2(cornerX - event.getX(), cornerY - event.getY())) % 360;
                    int initialAngle = (int)Math.toDegrees(Math.atan2(cornerX - initialTx, cornerY - initialTy)) % 360;
                    this.itemAngle = initialItemAngle + (angle - initialAngle);
                }
                /*
                if((!isExtanded || (isExtanded && Math.hypot(event.getX() - width, event.getY() - height) < 2 * extRadius / 3))
                                && (Math.hypot(event.getX() - initialTx, event.getY() - initialTy) > extRadius/3) ){
                    if (Math.hypot(event.getX() - width, event.getY() - height) < extRadius + initialRadius) {
                        if (touchIsExt) {
                            radius = (int) Math.hypot(event.getX() - width, event.getY() - height);
                        }
                    }
                } else if (isExtanded){
                    int angle = (int)Math.toDegrees(Math.atan2(width - event.getX(), height - event.getY())) % 360;
                    int initialAngle = (int)Math.toDegrees(Math.atan2(width - initialTx, height - initialTy)) % 360;
                    this.itemAngle = initialItemAngle + (angle - initialAngle);

                }*/
            } break;

            case MotionEvent.ACTION_UP: {
                transisionLock = false;
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
                    Log.i("DBG", "click");
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
                                    break;
                            }
                            /*
                             */
                            Log.i("DBG", "item");
                        }
                    }
                }
            } break;
        }
        invalidate();
        return true;
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

    private RectF rectFromCircle(int x, int y, int r){
        return new RectF(x - r, y - r, x + r, y + r);
    }

    private void drawCircle(Canvas canvas, int x, int y, int r){
        canvas.drawOval(rectFromCircle(x, y, r), paint);
    }

    private double dist(int x1, int y1, int x2, int y2){
        return Math.hypot(x1 - x2, y1 - y2);
    }

    private class MenuItem{
        private RectF rect;
        private String string;

        public MenuItem(String string){
            this.string = string;
            this.rect = new RectF();
        }

        public void setRect(float left, float top, float right, float bottom){
            rect.set(left, top, right, bottom);
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
    }
}
