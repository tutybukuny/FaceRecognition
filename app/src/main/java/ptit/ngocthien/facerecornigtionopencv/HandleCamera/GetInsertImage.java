package ptit.ngocthien.facerecornigtionopencv.HandleCamera;

import android.content.Context;
import android.util.Log;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;

import ptit.ngocthien.facerecornigtionopencv.R;

/**
 * Created by huutien on 4/10/2017.
 */

public class GetInsertImage {

    public static Mat smile;

    public static Mat prepareImage(int id, Context context) throws IOException {
        switch (id) {
            case R.drawable.smile:
                return getImgage(smile, id, context);
            case R.drawable.face2:
                return getImgage(smile, id, context);
            case R.drawable.smile1:
                return getImgage(smile, id, context);
        }

        return null;
    }

    private static Mat getImgage(Mat smile, int id, Context context) throws IOException {
        if (smile != null) {
            Log.e("input channel img", smile.channels() + "");
            return smile;
        }
        return Utils.loadResource(context, id, Highgui.CV_LOAD_IMAGE_ANYCOLOR);
    }
}
