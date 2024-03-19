package ch.zli.m335.baumbro_android.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import ch.zli.m335.baumbro_android.R;

public class ImageActivity extends AppCompatActivity {

    ImageView imageView;

    Button deleteButton;

    String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_image);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        imageView = findViewById(R.id.image_view);
        deleteButton = findViewById(R.id.button_delete);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            imagePath = extras.getString("imagePath");
        }

        imageView.setImageURI(Uri.parse(imagePath));

        deleteButton.setOnClickListener(v -> {
            deleteImage(imagePath);
        });
    }




    private void deleteImage(String imagePath) {
        File file = new File(imagePath);
        if (file.exists()) {
            boolean deleted = file.delete();
            if (deleted) {
                Toast.makeText(this, "Image deleted successfully!", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}