package ch.zli.m335.baumbro_android.adapters;

import ch.zli.m335.baumbro_android.R;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

import ch.zli.m335.baumbro_android.activities.GalleryActivity;
import ch.zli.m335.baumbro_android.activities.ImageActivity;
import ch.zli.m335.baumbro_android.activities.MainActivity;
import ch.zli.m335.baumbro_android.activities.MapActivity;
import ch.zli.m335.baumbro_android.activities.TreeActivity;
import ch.zli.m335.baumbro_android.database.AppDatabase;
import ch.zli.m335.baumbro_android.database.Tree;
import ch.zli.m335.baumbro_android.database.TreeDao;

// https://medium.com/androiddevelopers/getting-to-know-recyclerview-ea14f8514e6
public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

    private List<String> imagePaths;

    public GalleryAdapter(List<String> imagePaths) {
        this.imagePaths = imagePaths != null ? imagePaths : Collections.emptyList();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the item layout for each tree item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_item, parent, false);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.width = (int)  (parent.getMeasuredWidth() * 0.5);
        layoutParams.height = (int)  (parent.getMeasuredWidth() * 0.65);
        int width = parent.getMeasuredWidth();
        int height = parent.getHeight();
        view.setLayoutParams(layoutParams);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (imagePaths != null && position < imagePaths.size()) {
            String path = imagePaths.get(position);
            holder.treeImage.setImageURI(Uri.parse(path));
            holder.treeImage.setTag(path);
            holder.itemView.setOnClickListener(holder);
        } else {
            Log.w("GalleryAdapter", "Invalid position or empty image list");
        }
    }

    public void setPaths(List<String> imagePaths) {
        this.imagePaths = imagePaths;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return imagePaths.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView treeImage;


        public ViewHolder(View itemView) {
            super(itemView);
            treeImage = itemView.findViewById(R.id.tree_image);
        }

        @Override
        public void onClick(View v) {
            Context context = v.getContext();

            treeImage = itemView.findViewById(R.id.tree_image);

            String path = treeImage.getTag().toString();

            GalleryActivity activity = (GalleryActivity) context;
            Intent treeAct = new Intent(activity, ImageActivity.class);
            treeAct.putExtra("imagePath", path);

            activity.startActivity(treeAct);
        }
    }
}
