package com.avivamiriammandel.bakeme.model;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class RecipeGroup extends ExpandableGroup<IndividualRecipe> {
    public RecipeGroup(String title, List items) {
        super(title, items);
    }
}
