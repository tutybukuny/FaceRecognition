package ptit.ngocthien.facerecornigtionopencv.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import ptit.ngocthien.facerecornigtionopencv.Model.ImageObject;
import ptit.ngocthien.facerecornigtionopencv.R;

/**
 * Created by tutyb on 5/13/2017.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {

    private List<ImageObject> imageObjects;
    private Context context;

    public ImageAdapter(List<ImageObject> imageObjects, Context context) {
        this.imageObjects = imageObjects;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_cardview_cell, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ImageObject imageObject = imageObjects.get(position);
        String imagePath = imageObject.getPath();

        holder.imgv.setImageURI(Uri.parse(imagePath));
    }

    @Override
    public int getItemCount() {
        return imageObjects.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgv;

        public ViewHolder(View itemView) {
            super(itemView);
            imgv = (ImageView) itemView.findViewById(R.id.imgv);
        }
    }
}
