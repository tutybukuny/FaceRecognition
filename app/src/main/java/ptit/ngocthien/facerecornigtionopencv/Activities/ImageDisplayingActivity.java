package ptit.ngocthien.facerecornigtionopencv.Activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import ptit.ngocthien.facerecornigtionopencv.R;

public class ImageDisplayingActivity extends AppCompatActivity {

    Toolbar toolbar;
    ImageView imageView;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_displaying);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Image");
        imageView = (ImageView) findViewById(R.id.image_view);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        Intent intent = getIntent();
//        byte[] byteArray = intent.getByteArrayExtra("image");
//        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray,0,byteArray.length);
        uri = Uri.parse(intent.getStringExtra("imageUri"));
        imageView.setImageURI(uri);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_view_image, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_share:
                shareImage(uri);
                break;
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void shareImage(Uri uri) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");

        intent.putExtra(Intent.EXTRA_SUBJECT, "");
        intent.putExtra(Intent.EXTRA_TEXT, "");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        try {
            startActivity(Intent.createChooser(intent, "Share Picture"));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "No App Avative", Toast.LENGTH_SHORT).show();
        }
    }
}
