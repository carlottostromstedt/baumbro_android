package ch.zli.m335.baumbro_android.adapters;
import ch.zli.m335.baumbro_android.R;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

import ch.zli.m335.baumbro_android.activities.MainActivity;
import ch.zli.m335.baumbro_android.activities.MapActivity;
import ch.zli.m335.baumbro_android.activities.TreeActivity;
import ch.zli.m335.baumbro_android.database.AppDatabase;
import ch.zli.m335.baumbro_android.database.Tree;
import ch.zli.m335.baumbro_android.database.TreeDao;

// https://medium.com/androiddevelopers/getting-to-know-recyclerview-ea14f8514e6
public class TreeAdapter extends RecyclerView.Adapter<TreeAdapter.ViewHolder> {

    private List<Tree> trees;

    public TreeAdapter(List<Tree> trees) {
        this.trees = trees;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the item layout for each tree item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tree_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Tree tree = trees.get(position);
        holder.treeName.setText(tree.getBaumnamedeu());
        holder.treeNumber.setText(tree.getBaumnummer());
        holder.treeNameLatin.setText(tree.getBaumnamelat());
        holder.treeHeight.setText(tree.getBaumtyptext());
        holder.itemView.setOnClickListener(holder);
    }

    public void setTrees(List<Tree> trees) {
        this.trees = trees;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return trees.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView treeName, treeNumber, treeNameLatin, treeHeight;

        private AppDatabase db;

        public ViewHolder(View itemView) {
            super(itemView);
            treeName = itemView.findViewById(R.id.tree_name);
            treeNumber = itemView.findViewById(R.id.tree_number);
            treeNameLatin = itemView.findViewById(R.id.tree_name_latin);
            treeHeight= itemView.findViewById(R.id.tree_height);
        }

        @Override
        public void onClick(View v) {
            Context context = v.getContext();

            treeNumber = itemView.findViewById(R.id.tree_number);

            db = AppDatabase.getInstance(v.getContext());
            TreeDao treeDao = db.treeDao();
            Tree clickedTree = treeDao.findByTreeNumber((String) treeNumber.getText());

            Log.d("TreeAdapter", clickedTree.getBaumnamedeu());

            MapActivity activity = (MapActivity) context;
            Intent treeAct = new Intent(activity, TreeActivity.class);
            treeAct.putExtra("treeNumber", treeNumber.getText());

            activity.startActivity(treeAct);
        }
    }
}
