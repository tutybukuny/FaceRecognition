package ptit.ngocthien.facerecornigtionopencv.HandleCamera;

import android.content.Context;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Size;
import org.opencv.objdetect.CascadeClassifier;

/**
 * Created by tutyb on 4/1/2017.
 */

public class GetInputFrame implements CameraBridgeViewBase.CvCameraViewListener2 {
    public static Mat saveMat;
    public static Mat mRgba;
    private int absoluteFaceSize;
    private CascadeClassifier cascadeClassifier;
    Context context;
    public GetInputFrame(Context context) {
        this.context = context;
        cascadeClassifier = CreateCascadeClassifier.createCascadeClassifier(context);
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        mRgba = new Mat(height, width, CvType.CV_8UC4);
        absoluteFaceSize = (int) (height * 0.2);
    }

    @Override
    public void onCameraViewStopped() {
        mRgba.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        mRgba = inputFrame.rgba();
        MatOfRect faces = new MatOfRect();

        if (cascadeClassifier != null) {
            cascadeClassifier.detectMultiScale(mRgba, faces, 1.1, 2, 2,
                    new Size(absoluteFaceSize, absoluteFaceSize), new Size());
        }

        mRgba = ActionForFace.writeSmile(faces, mRgba, context);
        this.saveMat = mRgba;
        return mRgba;
    }
    public void setidImage(int id){
        ActionForFace.setidImage(id, context);
    }
}
