package newera.EliJ.ui.view.inputs.components;

import android.graphics.Canvas;
import android.view.MotionEvent;
import newera.EliJ.ui.view.inputs.GenericBox;

/**
 * Created by echo on 24/02/2017.
 */
public class Label implements IGenericBoxComponent {

    private final GenericBox box;

    public Label(GenericBox box)
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
    public boolean getEditStatus() {
        return false;
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
