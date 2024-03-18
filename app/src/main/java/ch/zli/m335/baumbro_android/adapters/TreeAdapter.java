package ch.zli.m335.baumbro_android.adapters;
import ch.zli.m335.baumbro_android.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.Collections;
import java.util.List;

import ch.zli.m335.baumbro_android.database.Tree;

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
    }

    @Override
    public int getItemCount() {
        return trees.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView treeName, treeNumber, treeNameLatin, treeHeight;

        public ViewHolder(View itemView) {
            super(itemView);
            treeName = itemView.findViewById(R.id.tree_name);
            treeNumber = itemView.findViewById(R.id.tree_number);
            treeNameLatin = itemView.findViewById(R.id.tree_name_latin);
            treeHeight= itemView.findViewById(R.id.tree_height);
        }
    }
}
