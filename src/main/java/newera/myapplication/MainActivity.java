package newera.myapplication;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import newera.myapplication.ui.system.SystemActionHandler;
import newera.myapplication.ui.view.CImageView;
import newera.myapplication.ui.view.CircleMenu;

public class MainActivity extends AppCompatActivity {

    public CImageView civ;
    private static final int REQUEST_CREATE_DIRECTORY = 3;

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

        CircleMenu menu = (CircleMenu) findViewById(R.id.circleMenu);
        menu.setView((CImageView) findViewById(R.id.cImageView));
        menu.setActivity(this);
        menu.setManager(civ.getManager());
        menu.initialize();
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
}