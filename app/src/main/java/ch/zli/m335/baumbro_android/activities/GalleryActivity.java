package ch.zli.m335.baumbro_android.activities;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ch.zli.m335.baumbro_android.R;
import ch.zli.m335.baumbro_android.adapters.GalleryAdapter;
import ch.zli.m335.baumbro_android.adapters.TreeAdapter;
import ch.zli.m335.baumbro_android.database.AppDatabase;
import ch.zli.m335.baumbro_android.database.Tree;
import ch.zli.m335.baumbro_android.database.TreeDao;

public class GalleryActivity extends AppCompatActivity {

    ImageButton imageButton;

    TextView treeNameView, treeNumberView, treeNameLatinView, treeHeightView;

    private RecyclerView recyclerView;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_SELECT_IMAGE = 2;
    String uriString = "";

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 1001;
    private static final int PERMISSION_REQUEST_CODE = 1001;

    Tree tree;
    String treeNumber;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_gallery);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // Permission not granted, request permission from the user
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        }


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            treeNumber = extras.getString("treeNumber");
        }

        db = AppDatabase.getInstance(this);
        TreeDao treeDao = db.treeDao();
        tree = treeDao.findByTreeNumber(treeNumber);

        // https://developers.google.com/admob/android/browser/webview
        treeNameView = findViewById(R.id.tree_name);
        treeNumberView = findViewById(R.id.tree_number);
        treeNameLatinView = findViewById(R.id.tree_name_latin);
        treeHeightView = findViewById(R.id.tree_height);
        imageButton = findViewById(R.id.image_button);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));

        treeNameView.setText(tree.getBaumnamedeu());
        treeNumberView.setText(tree.getBaumnummer());
        treeNameLatinView.setText(tree.getBaumnamelat());
        treeHeightView.setText(tree.getBaumtyptext());

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        List<String> paths = getPaths();
        GalleryAdapter adapter = new GalleryAdapter(getPaths());
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        List<String> imagePaths = getPaths();
        GalleryAdapter adapter = (GalleryAdapter) recyclerView.getAdapter();
        adapter.setPaths(imagePaths);
        adapter.notifyDataSetChanged();
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (ActivityNotFoundException e) {
            // display error state to the user
        }
    }

    private List<String> getPaths(){
        String treeNumber = tree.getBaumnummer();
        String selection = MediaStore.Images.Media.DISPLAY_NAME + " LIKE ?";
        String[] selectionArgs = new String[]{"%" + treeNumber + "%"};

        ContentResolver contentResolver = getContentResolver();
        Uri collection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        Cursor cursor = contentResolver.query(collection, null, selection, selectionArgs, null);

        List<String> imagePaths = new ArrayList<>();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int columnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
                String imagePath = cursor.getString(columnIndex);
                imagePaths.add(imagePath);
            } while (cursor.moveToNext());
            cursor.close();
        }

        return imagePaths;
    }

    private String saveBitmapToGallery(Bitmap bitmap) {
        String filePath = "";
        try {
            // Create file to save bitmap
            String fileName = "Image_" + tree.getBaumnummer() + "_" + System.currentTimeMillis() + ".jpg";
            File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), fileName);

            // Save bitmap to file
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();

            // Update MediaStore so image appears in the gallery
            MediaStore.Images.Media.insertImage(getContentResolver(), file.getAbsolutePath(), fileName, null);

            filePath = file.getAbsolutePath();
            Log.d("MainActivity", "Bitmap saved to gallery: " + filePath);
        } catch (IOException e) {
            Log.e("MainActivity", "Error saving bitmap to gallery", e);
        }

        List<String> imagePaths = getPaths();
        GalleryAdapter adapter = (GalleryAdapter) recyclerView.getAdapter();
        adapter.setPaths(imagePaths);
        adapter.notifyDataSetChanged();
        Toast.makeText(this, "Image saved successfully!", Toast.LENGTH_SHORT).show();
        return filePath;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            if (data != null && data.getExtras() != null) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
//                image_view.setImageBitmap(imageBitmap);
                // Save bitmap to storage
                saveBitmapToGallery(imageBitmap);
            } else {
                Log.e("MainActivity", "onActivityResult: Data or data.getExtras() is null for REQUEST_IMAGE_CAPTURE");
            }
        } else if (requestCode == REQUEST_SELECT_IMAGE && resultCode == RESULT_OK) {
            if (data != null && data.getData() != null) {
                try {
                    Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
//                    image_view.setImageBitmap(imageBitmap);
                    // Save bitmap to storage
                    String file_path = saveBitmapToGallery(imageBitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("MainActivity", "onActivityResult: Data or data.getData() is null for REQUEST_SELECT_IMAGE");
            }
        }
    }
}