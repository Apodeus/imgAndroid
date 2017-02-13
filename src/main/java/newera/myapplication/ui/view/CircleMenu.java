package newera.myapplication.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
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
    private final static int RADIUS_FACTOR = 6;

    private Paint paint;
    private int width, height;
    private int radius, initialRadius, extRadius;
    private boolean isExtanded, touchIsExt, touch;

    private List<MenuItem> itemList;
    private int itemAngle;
    private int initialTx, initialTy, initialItemAngle;

    public CircleMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.paint = new Paint();
        this.isExtanded = false;
        this.touch = false;
        paint.setAntiAlias(true);
        paint.setTextSize(40);


        /*
            Following code only for testing purpose
         */
        this.itemList = new ArrayList<MenuItem>();
        for(int i = 0; i <20; ++i){
            itemList.add(new MenuItem("Item n°"+i));
        }
        this.itemAngle = 0;
        /*
         */
    }

    @Override
    public void onDraw(Canvas canvas){
        double angle;
        int padding, x, y;
        RectF rect = new RectF();

        if (!touch ){
            if (isExtanded && radius < extRadius) {
                radius += (extRadius - radius) / 2 + 10;
                invalidate();
            } else if (isExtanded && radius > extRadius){
                radius -= (radius - extRadius) / 2;
                invalidate();
            } else if (!isExtanded && radius > initialRadius){
                radius -= ((radius-initialRadius)/2 + 10);
                invalidate();
            } else if (!isExtanded && radius < initialRadius){
                radius += (initialRadius-radius)/2;
                invalidate();
            }
        }

        paint.setColor(getResources().getColor(R.color.colorAccent));
        padding = (extRadius + initialRadius) - radius;
        rect.set(padding, padding, padding + radius * 2, padding + radius * 2);
        canvas.drawOval(rect, paint);
        if (isExtanded){
            for (int i = 0; i < itemList.size(); ++i){
                angle = Math.toRadians(i * 15) - Math.toRadians(itemAngle);

                // ===== draw circle item ====
                paint.setColor(getResources().getColor(R.color.colorPrimaryDark));
                x = (int)(width + (radius - initialRadius * 0.75) * Math.cos(angle));
                y = (int)(height + (radius - initialRadius * 0.75) * Math.sin(angle));
                rectFromCircle(itemList.get(i), x, y , initialRadius / 2);
                canvas.drawOval(itemList.get(i).getRect(), paint);

                int x2, y2;

                // ==== Draw item name ====
                paint.setColor(Color.WHITE);
                x2 = (int)(width + (radius + initialRadius * 0.85) * Math.cos(angle));
                y2 = (int)(height + (radius + initialRadius * 0.85) * Math.sin(angle));

                canvas.save();
                canvas.rotate((float)Math.toDegrees(angle) + 180, x2, y2);
                canvas.drawText(itemList.get(i).getName(), x2, y2, paint);
                canvas.restore();
            }
        }
    }

    @Override
    public void onMeasure(int w, int h){
        width = MeasureSpec.getSize(w);
        height = MeasureSpec.getSize(h);

        initialRadius = (width / RADIUS_FACTOR);
        extRadius = width - initialRadius;
        radius = initialRadius;

        setMeasuredDimension(width, height);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                initialTx = (int)event.getX();
                initialTy = (int)event.getY();
                initialItemAngle = itemAngle;
                if (Math.hypot(event.getX() - width, event.getY() - height) > extRadius+initialRadius){
                    isExtanded = false;
                    return false;
                }
                touchIsExt = true;
                touch = true;
            } break;

            case MotionEvent.ACTION_MOVE: {
                if(!isExtanded || (isExtanded && Math.hypot(event.getX() - width, event.getY() - height) < 2 * extRadius / 3)){
                    if (Math.hypot(event.getX() - width, event.getY() - height) < extRadius + initialRadius) {
                        if (touchIsExt)
                            radius = (int) Math.hypot(event.getX() - width, event.getY() - height);
                    }
                } else if (isExtanded){
                    int angle = (int)Math.toDegrees(Math.atan2(width - event.getX(), height - event.getY())) % 360;
                    int initialAngle = (int)Math.toDegrees(Math.atan2(width - initialTx, height - initialTy)) % 360;
                    this.itemAngle = initialItemAngle + (angle - initialAngle);
                }
            } break;

            case MotionEvent.ACTION_UP: {
                if ( ((extRadius)-(initialRadius))/2 > Math.hypot(event.getX()-width, event.getY()-height)){
                    isExtanded = false;
                } else {
                    isExtanded = true;
                }
                /*
                * Handle click
                */
                if (Math.abs(initialTx-event.getX()) < 5 && Math.abs(initialTy-event.getY()) < 5){
                    for (int i = 0; i < itemList.size(); ++i){
                        if (itemList.get(i).contains((int)event.getX(), (int)event.getY())){
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
                            Log.i("DBG", "Click on "+itemList.get(i).getName());
                        }
                    }
                }
                touch = false;
            } break;
        }
        invalidate();
        return true;
    }

    private void rectFromCircle(MenuItem item, int x, int y, int r){
        item.setRect(x - r, y - r, x + r, y + r);
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
