package newera.myapplication;

import android.app.FragmentManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import newera.myapplication.image.processing.shaders.ChangeHue;
import newera.myapplication.image.processing.shaders.Contrast;
import newera.myapplication.image.processing.shaders.Convolution;
import newera.myapplication.image.processing.shaders.GrayScale;
import newera.myapplication.image.processing.shaders.HistogramEqualize;
import newera.myapplication.image.processing.shaders.InvertColor;
import newera.myapplication.image.processing.shaders.KeepHue;
import newera.myapplication.image.processing.shaders.Lightness;
import newera.myapplication.image.processing.shaders.Sepia;
import newera.myapplication.ui.system.DataFragment;
import newera.myapplication.ui.system.SystemActionHandler;
import newera.myapplication.ui.view.ActionCamera;
import newera.myapplication.ui.view.ActionGallery;
import newera.myapplication.ui.view.ActionReset;
import newera.myapplication.ui.view.ActionSave;
import newera.myapplication.ui.view.CImageView;
import newera.myapplication.ui.view.CircleMenu;

public class MainActivity extends AppCompatActivity {

    public CImageView civ;
    public DataFragment dataFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //OnCreate
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SystemActionHandler.setActivity(this);
        SystemActionHandler.requestCreateDirectory();

        //PictureFileManager.CreatePictureFileFromCamera();
        //PictureFileManager.LoadPictureFromGallery();
        civ = (CImageView) findViewById(R.id.cImageView);

        CircleMenu sysmenu = (CircleMenu) findViewById(R.id.sysMenu);
        sysmenu.setView((CImageView) findViewById(R.id.cImageView));
        sysmenu.setActivity(this);
        sysmenu.setManager(civ.getManager());
        sysmenu.setPosition(CircleMenu.Position.TOP_LEFT);
        initializeSysMenu(sysmenu);

        CircleMenu menu = (CircleMenu) findViewById(R.id.filterMenu);
        menu.setView((CImageView) findViewById(R.id.cImageView));
        menu.setActivity(this);
        menu.setManager(civ.getManager());
        menu.setPosition(CircleMenu.Position.BOT_RIGHT);
        initializeFilterMenu(menu);

        // find the retained fragment on activity restarts
        FragmentManager fm = getFragmentManager();
        dataFragment = (DataFragment) fm.findFragmentByTag("data");

        // create the fragment and data the first time
        if (dataFragment == null) {
            // add the fragment
            dataFragment = new DataFragment();
            fm.beginTransaction().add(dataFragment, "data").commit();
            // load the data from the web
        }else{
            civ.setImage(dataFragment.getImage());
        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        dataFragment.setImage(civ.getImage());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        SystemActionHandler.handleActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        SystemActionHandler.handleRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void initializeSysMenu(CircleMenu menu){
        menu.menuColor = getResources().getColor(R.color.colorPrimaryDark);
        menu.menuColor = getResources().getColor(R.color.colorPrimary);
        menu.addClickable(new ActionCamera());
        menu.addClickable(new ActionGallery());
        menu.addClickable(new ActionSave());
        menu.addClickable(new ActionReset());
    }
    public void initializeFilterMenu(CircleMenu menu) {
        menu.addClickable(new InvertColor(this));
        menu.addClickable(new GrayScale(this));
        menu.addClickable(new Sepia(this));
        menu.addClickable(new Contrast(this));
        menu.addClickable(new Lightness(this));
        menu.addClickable(new ChangeHue(this));
        menu.addClickable(new KeepHue(this));
        menu.addClickable(new HistogramEqualize(this));
        menu.addClickable(new Convolution(this, Convolution.ConvType.GAUSS));
        menu.addClickable(new Convolution(this, Convolution.ConvType.MOY));
        menu.addClickable(new Convolution(this, Convolution.ConvType.EDGE));
        menu.addClickable(new Convolution(this, Convolution.ConvType.LAPL));
        menu.addClickable(new Convolution(this, Convolution.ConvType.SOBEL));

        /*
        itemList.get(0).string = "Gallery";
        itemList.get(1).string = "Camera";
        itemList.get(2).string = "Save";
        itemList.get(3).string = "Reinitialize";*/
    }
}