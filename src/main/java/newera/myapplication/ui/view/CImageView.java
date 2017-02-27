package newera.myapplication.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import newera.myapplication.R;
import newera.myapplication.image.Image;
import newera.myapplication.image.processing.EItems;
import newera.myapplication.image.processing.shaders.ChangeHue;
import newera.myapplication.image.processing.shaders.Contrast;
import newera.myapplication.image.processing.shaders.KeepHue;
import newera.myapplication.image.processing.shaders.Lightness;
import newera.myapplication.image.processing.shaders.Shader;
import newera.myapplication.ui.view.inputs.EInputType;
import newera.myapplication.ui.view.inputs.InputManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Emile Barjou-Suire on 09/02/2017.
 */

public class CImageView extends View {
    private final static float MOVE_SAFEZONE = 0.5f;
    private final static float LERP_FACTOR = 3f;
    private EItems currentInputItem;

    public InputManager getManager() {
        return inputManager;
    }

    private enum TouchMethod {DRAG, ZOOM, TOOL}

    private Image image;
    private Point contentCoords;
    private float contentScale;
    private TouchHandler touchHandler;
    private Rect src;
    private Rect dst;
    private InputManager inputManager;

    public CImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        image = null;
        this.contentCoords = new Point(0, 0);
        this.touchHandler = new TouchHandler();
        this.contentScale = 1f;
        this.src = new Rect();
        this.dst = new Rect();

        this.inputManager = new InputManager(this);
    }

    /**
     * Set the picture to be displayed on the view.
     * @param image the Image object to be displayed.
     */
    public void setImage(Image image)
    {
        if(image != null && !image.isEmpty()) {
            this.image = image;
            //src = new Rect(0, 0, image.getWidth(), image.getHeight());
            //dst = new Rect(getWidth() - image.getWidth() / 2, getHeight() - image.getHeight() / 2, getWidth() + image.getWidth() / 2, getHeight() + image.getHeight() / 2);
            contentCoords.x = getWidth() / 2;
            contentCoords.y = getHeight() / 2;
            invalidate();

        }
    }

    public void reinitialize(){
        this.image.reinitializeBitmap();
        invalidate();
    }

    @Override
    public void onDraw(Canvas canvas){
        canvas.drawColor(getResources().getColor(R.color.colorPrimaryDark));
        if (image != null && !image.isEmpty()){
            image.draw(canvas, contentCoords.x, contentCoords.y, contentScale);
            /*dst.left = contentCoords.x - (int) (image.getWidth() * (contentScale/2));
            dst.top = contentCoords.y - (int) (image.getHeight() * (contentScale/2));
            dst.right = contentCoords.x + (int) (image.getWidth() * (contentScale/2));
            dst.bottom =  contentCoords.y + (int) (image.getHeight() * (contentScale/2));
            canvas.drawBitmap(image.getBitmap(), src, dst, null);*/
            inputManager.draw(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if (!inputManager.handleTouch(event))
        {

            if (event.getPointerCount() <= 1) {
                contentScale = touchHandler.onTouch(event, TouchMethod.DRAG, contentCoords, contentScale);
                contentCoords.x = (int) (Math.min(contentCoords.x, getWidth() * MOVE_SAFEZONE + (int) (image.getWidth() * (contentScale/2))));      // need the scale factor
                contentCoords.y = (int) (Math.min(contentCoords.y, getHeight() * MOVE_SAFEZONE + (int) (image.getHeight() * (contentScale/2))));   // somewhere here

            }else{
                contentScale = touchHandler.onTouch(event, TouchMethod.ZOOM, contentCoords, contentScale);
            }
        }


        invalidate();
        return true;
    }

    public void onApplyFilter(Map<String, Object> params)
    {
        Shader shader = null;
        switch (currentInputItem) {
            case NONE:
                return;
            case F_CHANGE_HUE:
                shader = new ChangeHue(getContext());
                break;
            case F_LIGHTNESS:
                shader = new Lightness(getContext());
                break;
            case F_KEEP_HUE:
                shader = new KeepHue(getContext());
                break;
            case F_CONTRAST:
                shader = new Contrast(getContext());
                break;
        }

        shader.setParameters(params);
        shader.ApplyFilter(image);

    }

    public void onCancelFilter()
    {

    }
    //TODO
    public void onPreviewFilter(int value) {
        /*switch (currentInputType) {
            case NONE:
                return;
            case SHADER:
                switch (currentInputItem) {
                    case NONE:
                        return;
                    case F_CHANGE_HUE:
                        Shader s = new ChangeHue(getContext());
                        s.ApplyPreviewFilter(image, inputManager.getPreviewParams());
                        break;
                }
                break;
            case TOOL:
                break;
            case SYSTEM:
                break;
        }*/
    }

    public Image getImage()
    {
        return this.image;
    }

    public void setCurrentAction(EItems item) {
        this.currentInputItem = item;

    }

    private class TouchHandler{
        private int initialX, initialY;
        private int initialContentX, initialContentY;
        private float initialDist, initialScale;
        private TouchMethod method;
        private int mActivePointerId, pointerIndex;
        private List<Point> touchList;


        TouchHandler(){
            this.touchList = new ArrayList<>();
            this.touchList = new ArrayList<>();
        }

        float onTouch(MotionEvent event, TouchMethod method, Point coord, float scale){
            touchList.clear();
            for(int i = 0; i < event.getPointerCount(); ++i){
                mActivePointerId = event.getPointerId(i);
                pointerIndex = event.findPointerIndex(mActivePointerId);
                touchList.add( new Point((int)event.getX(pointerIndex), (int)event.getY(pointerIndex)) );
            }

            switch(method) {
                case DRAG: {
                    initialDist = -1f;
                    initialScale = scale;
                    switch(event.getAction()) {
                        case MotionEvent.ACTION_DOWN: {
                            initialX = touchList.get(0).x;
                            initialY = touchList.get(0).y;
                            initialContentX = coord.x;
                            initialContentY = coord.y;
                        } break;

                        case MotionEvent.ACTION_MOVE: {
                            coord.x = (int) Math.max(0 - image.getWidth() * contentScale + getWidth() * MOVE_SAFEZONE  + (int) (image.getWidth() * (contentScale/2)), initialContentX + (touchList.get(0).x - initialX)); // need a scale factor somewhere here
                            coord.y = (int) Math.max(0 - image.getHeight() * contentScale + getHeight() * MOVE_SAFEZONE + (int) (image.getHeight() * (contentScale/2)), initialContentY + (touchList.get(0).y - initialY));
                            /*
                            coord.x = initialContentX + (touchList.get(0).x - initialX); // need a scale factor somewhere here
                            coord.y = initialContentY + (touchList.get(0).y - initialY);
                            */
                        } break;
                    }
                } break;

                case ZOOM: {
                    if (initialDist < 0)
                    {
                        initialDist = touchList.get(0).distanceFromPoint(touchList.get(1));
                    }else{
                        float currentDist = touchList.get(0).distanceFromPoint(touchList.get(1));
                        scale = currentDist / initialDist * initialScale;
                        /*
                        coord.x =  (touchList.get(0).x +  touchList.get(1).x)/2 - image.getWidth()/2; // need a scale factor somewhere here
                        coord.y = (touchList.get(0).x +  touchList.get(1).x)/2 - image.getHeight()/2;
                        */
                    }

                } break;

                case TOOL: {
                } break;
            }

            return scale;
        }

    }

    private class Point{
        int x, y;

        Point(){
            this.x = 0;
            this.y = 0;
        }

        Point(int x, int y){
            this.x = x;
            this.y = y;
        }

        float distanceFromPoint(Point b) {
            return (float) Math.sqrt((double)((this.x - b.x)*(this.x - b.x) + (this.y - b.y)*(this.y - b.y)));
        }
    }
}
