package newera.EliJ.ui.system;

import android.app.Fragment;
import android.os.Bundle;
import newera.EliJ.image.Image;

/**
 * Created by echo on 02/03/2017.
 */
public class DataFragment extends Fragment {

    private Image image;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // retain this fragment
        setRetainInstance(true);
    }

    public void setImage(Image image)
    {
        this.image = image;
    }

    public Image getImage()
    {
        return this.image;
    }


}
