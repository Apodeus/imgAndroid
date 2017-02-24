package newera.myapplication.ui.view.inputs.components;

import android.graphics.Canvas;
import android.view.MotionEvent;
import newera.myapplication.ui.view.inputs.GenericBox;

/**
 * Created by echo on 24/02/2017.
 */
public class ColorPicker implements IGenericBoxComponent {

    private final GenericBox box;

    public ColorPicker(GenericBox box)
    {
        this.box = box;
    }

    @Override
    public int getHeight() {
        return 0;
    }

    @Override
    public void setStartingHeight(int height) {

    }

    @Override
    public Object getValue() {
        return null;
    }

    @Override
    public void initialize(int[] settings) {

    }

    @Override
    public void setIndex(int index) {

    }

    @Override
    public void setLabel(String label) {

    }

    @Override
    public String getLabel() {
        return null;
    }

    @Override
    public void draw(Canvas canvas) {

    }

    @Override
    public void enableEdit(MotionEvent event) {

    }

    @Override
    public void disableEdit() {

    }

    @Override
    public void handleEdit(MotionEvent event) {

    }
}
