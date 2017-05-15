package ptit.ngocthien.facerecornigtionopencv.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ptit.ngocthien.facerecornigtionopencv.Adapter.ImageAdapter;
import ptit.ngocthien.facerecornigtionopencv.Helper.ImageSaver;
import ptit.ngocthien.facerecornigtionopencv.Listener.RecyclerListener;
import ptit.ngocthien.facerecornigtionopencv.Model.ImageObject;
import ptit.ngocthien.facerecornigtionopencv.R;

public class ImageSelectionActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView rv_images;
    List<ImageObject> list;
    ImageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_selection);

        init();
    }

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Choose image");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rv_images = (RecyclerView) findViewById(R.id.rv_images);
        list = getAllImages();
        adapter = new ImageAdapter(list, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 3, 1, false);
        rv_images.setLayoutManager(layoutManager);
        rv_images.setAdapter(adapter);
        setListener();
    }

    private void setListener() {
        rv_images.addOnItemTouchListener(new RecyclerListener(this, rv_images,
                new RecyclerListener.ClickListener() {

                    @Override
                    public void onClick(View view, int position) {
                        ImageObject imageObject = list.get(position);
                        Intent intent = new Intent(ImageSelectionActivity.this, ImageDisplayingActivity.class);
                        intent.putExtra("imageUri", imageObject.getPath());
                        startActivity(intent);
                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }
                }));
    }

    public static List<ImageObject> getAllImages() {
        List<ImageObject> list = new ArrayList<>();
        ImageSaver.createFolderIfNotExists();
        File files[] = new File(ImageSaver.dirPath).listFiles();

        for (File file : files) {
            list.add(new ImageObject(file.getAbsolutePath()));
        }

        return list;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
