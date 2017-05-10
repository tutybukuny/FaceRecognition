package ptit.ngocthien.facerecornigtionopencv.HandleCamera;

import android.content.Context;
import android.util.Log;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;

import ptit.ngocthien.facerecornigtionopencv.R;

/**
 * Created by tutyb on 4/1/2017.
 */

public class ActionForFace {
    public static int idImage = 0;
    public static Mat smile = null;
    public static Mat smileImg = null;

    public static Mat writeRect(MatOfRect faces, Mat mRgba, Context context) {
        for (Rect rect : faces.toArray()) {
            Core.rectangle(mRgba, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height),
                    new Scalar(0, 255, 0));
        }
//        Log.e("Frame", facesArray.length + "");

        return mRgba;
    }

    public static Mat writeSmile(MatOfRect faces, Mat mRgba, Context context) {
        Rect[] facesArray = faces.toArray();

        for (Rect rect : facesArray) {

            if (smile != null) {
                //to do good

                Mat img = smileImg.clone();

                Imgproc.resize(img, img, new Size(rect.width, rect.height));
                //to do good
                Mat dst1 = new Mat();
                Imgproc.resize(smile, dst1, new Size(rect.width, rect.height));
                double alpha;
                // start at row 0/col 0
                for (int y = 0; y < dst1.rows(); ++y) {
                    for (int x = 0; x < dst1.cols(); ++x) {
                        double info[] = dst1.get(y, x);
                        alpha = info[2];
                        // and now combine the background and foreground pixel, using the opacity,but only if opacity > 0.
                        if (alpha > 0) //rude but this is what I need
                        {
                            double infof[] = img.get(y, x);
                            mRgba.put(y + rect.y, x + rect.x, infof);
                        }
                    }
                }
            } else {
                writeRect(faces, mRgba, context);
            }
        }

        return mRgba;
    }

    public static void setidImage(int id, Context context) {
        idImage = id;
        try {
            if (idImage == -1) {
                smile = null;
                smileImg = null;
            } else if (idImage != 0) {
                smile = GetInsertImage.prepareImage(idImage, context);
                smileImg = new Mat(smile.rows(), smile.cols(), CvType.CV_8UC4);
                Imgproc.cvtColor(smile, smileImg, Imgproc.COLOR_RGB2BGR);
                Imgproc.cvtColor(smileImg, smileImg, Imgproc.COLOR_BGR2BGRA);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
