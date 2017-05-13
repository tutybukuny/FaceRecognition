package ptit.ngocthien.facerecornigtionopencv.Helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by ngocthien on 4/17/2017.
 */

public class ImageSaver {

    private static final String TAG = "ImageSaver";
    public static String dirPath = Environment.getExternalStorageDirectory() + File.separator + "FaceRecognition";

    public static File store(Context context, Bitmap bm) {
        SimpleDateFormat sdf = new SimpleDateFormat("HHmmssddMMyyyy");
        String fileName = "IMG" + sdf.format(Calendar.getInstance().getTime()) + ".png";

        File folder = new File(dirPath);
        if (!folder.exists()) {
            folder.mkdir();
        }

        File file = new File(dirPath + "/" + fileName);
        try {
            Log.d("create file: ", file.createNewFile() + "");
            FileOutputStream fOut = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 85, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            Log.e("ImageSaver :", "Khong Luu Anh");
            e.printStackTrace();
        }

        MediaScannerConnection.scanFile(context,
                new String[]{file.getPath()}, new String[]{"image/jpeg"}, null);
        return file;
    }
}
