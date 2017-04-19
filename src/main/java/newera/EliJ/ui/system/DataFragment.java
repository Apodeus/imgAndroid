package newera.EliJ.ui.system;

import android.app.Fragment;
import android.os.Bundle;
import newera.EliJ.image.Image;
import newera.EliJ.ui.view.CCanvas;

/**
 * Created by echo on 02/03/2017.
 */
public class DataFragment extends Fragment {

    private Image image;
    private CCanvas cCanvas;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // retain this fragment
        setRetainInstance(true);
    }

    /**
     * Setter.
     * @param image Image object to be saved
     */
    public void setImage(Image image)
    {
        this.image = image;
    }

    public void setCCanvas(CCanvas cCanvas)
    {
        this.cCanvas = cCanvas;
    }
    /**
     * @return Image object to be restored
     */
    public Image getImage()
    {
        return this.image;
    }

    public CCanvas getcCanvas()
    {
        return this.cCanvas;
    }
}
