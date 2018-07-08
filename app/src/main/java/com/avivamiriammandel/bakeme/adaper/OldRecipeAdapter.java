package com.avivamiriammandel.bakeme.adaper;

import android.view.ViewGroup;

import com.avivamiriammandel.bakeme.viewholder.IndevidualRecipeViewHolder;
import com.avivamiriammandel.bakeme.viewholder.RecipeListViewHolder;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class OldRecipeAdapter extends ExpandableRecyclerViewAdapter<RecipeListViewHolder, IndevidualRecipeViewHolder> {
    public OldRecipeAdapter(List<? extends ExpandableGroup> groups) {
        super(groups);
    }

    @Override
    public RecipeListViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public IndevidualRecipeViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindChildViewHolder(IndevidualRecipeViewHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {

    }

    @Override
    public void onBindGroupViewHolder(RecipeListViewHolder holder, int flatPosition, ExpandableGroup group) {

    }
}