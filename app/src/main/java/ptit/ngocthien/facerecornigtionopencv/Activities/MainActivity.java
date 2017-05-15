package ptit.ngocthien.facerecornigtionopencv.Activities;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ptit.ngocthien.facerecornigtionopencv.HandleCamera.GetInputFrame;
import ptit.ngocthien.facerecornigtionopencv.Helper.ImageSaver;
import ptit.ngocthien.facerecornigtionopencv.Model.ImageObject;
import ptit.ngocthien.facerecornigtionopencv.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static String TAG = MainActivity.class.getSimpleName();

    ImageButton btnSwitchCamera;
    ImageButton btnTakePhoto;
    ImageView iv;
    int cameraIndex = -1;
    GetInputFrame inf;
    Bitmap bitmapPhoto;
    Uri uri;
    JavaCameraView cameraView;

    BaseLoaderCallback callback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case BaseLoaderCallback.SUCCESS: {
                    cameraView.enableView();
                    break;
                }

                default: {
                    super.onManagerConnected(status);
                    break;
                }
            }
        }
    };

    static {
        if (OpenCVLoader.initDebug()) {
            Log.i(TAG, "OpenCV loaded successfully");
        } else {
            Log.i(TAG, "OpenCV not loaded");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                Toast.makeText(MainActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }


        };

        new TedPermission(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n" +
                        "\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.INTERNET, Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION)
                .check();

        iv = (ImageView) findViewById(R.id.iv);
        btnSwitchCamera = (ImageButton) findViewById(R.id.rotateCamera);
        btnTakePhoto = (ImageButton) findViewById(R.id.takePhoto);

        iv.setOnClickListener(this);
        btnSwitchCamera.setOnClickListener(this);
        btnTakePhoto.setOnClickListener(this);

        checkCapturedPhoto();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        cameraView = (JavaCameraView) findViewById(R.id.cameraView);
        cameraView.setCameraIndex(cameraIndex);
        cameraView.setMaxFrameSize(360, 270);
        cameraView.setVisibility(SurfaceView.VISIBLE);
        inf = new GetInputFrame(this);
        cameraView.setCvCameraViewListener(inf);
    }

    private void checkCapturedPhoto() {
        ImageSaver.createFolderIfNotExists();

        List<ImageObject> list = ImageSelectionActivity.getAllImages();

        if (list.size() > 0) {
            iv.setImageURI(Uri.parse(list.get(0).getPath()));
            bitmapPhoto = Bitmap.createBitmap(10, 10, Bitmap.Config.ARGB_8888);
        }
    }

    private void changeCamera() {//change camera to front or back
        cameraIndex = -cameraIndex;
        cameraView.disableView();
        cameraView.setCameraIndex(cameraIndex);
        cameraView.enableView();
        Toast.makeText(getBaseContext(), "Changed Camera!", Toast.LENGTH_LONG).show();
    }

    public void ChangeImage(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.iv_rectangle:
                inf.setidImage(-1);
                break;
            case R.id.iv_nose_glass:
                inf.setidImage(R.drawable.nose_glass);
                break;
            case R.id.iv_iron_man_mask:
                inf.setidImage(R.drawable.iron_man_mask);
                break;
        }
    }

    private void capturePicture() {//take a photo
        Mat mat = GetInputFrame.saveMat;
        bitmapPhoto = Bitmap.createBitmap(mat.width(), mat.height(), Bitmap.Config.ARGB_8888);
//        Log.e("mat size", GetInputFrame.mRgba.width() + "");
        Utils.matToBitmap(mat, bitmapPhoto);
        Toast.makeText(this, "Image has been saved!", Toast.LENGTH_SHORT).show();
        iv.setImageBitmap(bitmapPhoto);
        File file = ImageSaver.store(this, bitmapPhoto);
        uri = Uri.fromFile(file);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (cameraView != null) {
            cameraView.disableView();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cameraView != null) {
            cameraView.disableView();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (OpenCVLoader.initDebug()) {
            Log.i(TAG, "OpenCV loaded successfully");
            callback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        } else {
            Log.i(TAG, "OpenCV not loaded");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_11, this, callback);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv:
                if (bitmapPhoto != null) {
                    Intent intent = new Intent(MainActivity.this, ImageSelectionActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.rotateCamera:
                changeCamera();
                break;
            case R.id.takePhoto:
                capturePicture();
                break;
        }
    }
}
