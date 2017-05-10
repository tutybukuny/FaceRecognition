package ptit.ngocthien.facerecornigtionopencv.HandleCamera;

import android.content.Context;
import android.util.Log;

import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import ptit.ngocthien.facerecornigtionopencv.R;

/**
 * Created by tutyb on 4/1/2017.
 */

public class CreateCascadeClassifier {

    public static CascadeClassifier createCascadeClassifier(Context context) {
        try {
            // Copy the resource into a temp file so OpenCV can load it
//            System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
            InputStream is = context.getResources().openRawResource(R.raw.haarcascade_frontalface_alt);
            File cascadeDir = context.getDir("cascade", Context.MODE_PRIVATE);
            File mCascadeFile = new File(cascadeDir, "haarcascade_frontalface_alt.xml");
            FileOutputStream os = new FileOutputStream(mCascadeFile);


            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            is.close();
            os.close();

            // Load the cascade classifier
            return new CascadeClassifier(mCascadeFile.getAbsolutePath());
        } catch (Exception e) {
            Log.e("OpenCVActivity", "Error loading cascade", e);
        }

        return null;
    }
}
