package ch.zli.m335.baumbro_android.activities;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import ch.zli.m335.baumbro_android.R;
import ch.zli.m335.baumbro_android.database.AppDatabase;
import ch.zli.m335.baumbro_android.database.Tree;
import ch.zli.m335.baumbro_android.database.TreeDao;

public class TreeActivity extends AppCompatActivity {

    private WebView webView;

    ImageButton galleryButton;

    TextView treeNameView, treeNumberView, treeNameLatinView, treeHeightView;


    Tree tree;
    String treeNumber;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tree);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            treeNumber = extras.getString("treeNumber");
        }

        db = AppDatabase.getInstance(this);
        TreeDao treeDao = db.treeDao();
        Tree tree = treeDao.findByTreeNumber(treeNumber);

        // https://developers.google.com/admob/android/browser/webview
        webView = findViewById(R.id.web_view);
        treeNameView = findViewById(R.id.tree_name);
        treeNumberView = findViewById(R.id.tree_number);
        treeNameLatinView = findViewById(R.id.tree_name_latin);
        treeHeightView = findViewById(R.id.tree_height);
        galleryButton = findViewById(R.id.gallery_button);

        treeNameView.setText(tree.getBaumnamedeu());
        treeNumberView.setText(tree.getBaumnummer());
        treeNameLatinView.setText(tree.getBaumnamelat());
        treeHeightView.setText(tree.getBaumtyptext());

        setWebView(tree);
        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryAct = new Intent(TreeActivity.this, GalleryActivity.class);
                galleryAct.putExtra("treeNumber", treeNumberView.getText());

                startActivity(galleryAct);
            }
        });
    }


    public void setWebView(Tree tree){
        // Let the web view accept third-party cookies.
        CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true);
        // Let the web view use JavaScript.
        webView.getSettings().setJavaScriptEnabled(true);
        // Let the web view access local storage.
        webView.getSettings().setDomStorageEnabled(true);
        // Let HTML videos play automatically.
        webView.getSettings().setMediaPlaybackRequiresUserGesture(false);

        webView.loadUrl("https://www.google.ch/search?q=" + tree.getBaumnamedeu());

//        webView.loadUrl("https://de.wikipedia.org/w/index.php?go=Go&search=" + tree.getBaumnamedeu());
    }
}