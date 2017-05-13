package ptit.ngocthien.facerecornigtionopencv.Model;

import java.io.Serializable;

/**
 * Created by tutyb_000 on 10/23/2016.
 */

public class ImageObject implements Serializable {
    private String path;

    public ImageObject(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
