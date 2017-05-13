package ptit.ngocthien.facerecornigtionopencv.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Mat;

import java.io.File;
import java.util.List;

import ptit.ngocthien.facerecornigtionopencv.HandleCamera.GetInputFrame;
import ptit.ngocthien.facerecornigtionopencv.R;
import ptit.ngocthien.facerecornigtionopencv.Helper.ImageSaver;

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

        iv = (ImageView) findViewById(R.id.iv);
        btnSwitchCamera = (ImageButton) findViewById(R.id.rotateCamera);
        btnTakePhoto = (ImageButton) findViewById(R.id.takePhoto);

        iv.setOnClickListener(this);
        btnSwitchCamera.setOnClickListener(this);
        btnTakePhoto.setOnClickListener(this);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

//        askPermissions();

        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                ).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
            }
        }).check();

        cameraView = (JavaCameraView) findViewById(R.id.cameraView);
        cameraView.setCameraIndex(cameraIndex);
        cameraView.setMaxFrameSize(360, 270);
        cameraView.setVisibility(SurfaceView.VISIBLE);
        inf = new GetInputFrame(this);
        cameraView.setCvCameraViewListener(inf);
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
            case R.id.iv_smile:
                inf.setidImage(R.drawable.smile);
                break;
            case R.id.iv_face2:
                inf.setidImage(R.drawable.face2);
                break;
            case R.id.iv_face1:
                inf.setidImage(R.drawable.smile1);
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

    private void checkRequest(int[] grantResults, String permissionName) {
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, permissionName + " Permission granted!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, permissionName + " Permission denied!", Toast.LENGTH_LONG).show();
        }
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

//                    intent.putExtra("imageUri", uri.toString());
//                    Log.d("image URI : ", uri.toString());

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
