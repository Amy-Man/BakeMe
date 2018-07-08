package com.avivamiriammandel.bakeme.model;

import android.os.Parcel;
import android.os.Parcelable;

public class IndividualRecipe implements Parcelable {
    private Integer id;
    private String name;
    private Integer servings;
    private String image;

    protected IndividualRecipe(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        name = in.readString();
        if (in.readByte() == 0) {
            servings = null;
        } else {
            servings = in.readInt();
        }
        image = in.readString();
    }

    public static final Creator<IndividualRecipe> CREATOR = new Creator<IndividualRecipe>() {
        @Override
        public IndividualRecipe createFromParcel(Parcel in) {
            return new IndividualRecipe(in);
        }

        @Override
        public IndividualRecipe[] newArray(int size) {
            return new IndividualRecipe[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        dest.writeString(name);
        if (servings == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(servings);
        }
        dest.writeString(image);
    }
}
