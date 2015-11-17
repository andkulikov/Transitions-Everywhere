package com.transitionseverywhere.activity;

import android.os.Parcel;
import android.os.Parcelable;

public class TransitionData implements Parcelable {
    private final int id;
    private final int top;
    private final int left;
    private final int width;
    private final int height;

    public TransitionData(int id, int left, int top, int width, int height) {
        this.id = id;
        this.top = top;
        this.left = left;
        this.width = width;
        this.height = height;
    }

    public int getId() {
        return id;
    }

    public int getTop() {
        return top;
    }

    public int getLeft() {
        return left;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TransitionData that = (TransitionData) o;

        if (id != that.id) return false;
        if (top != that.top) return false;
        if (left != that.left) return false;
        if (width != that.width) return false;
        return height == that.height;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + top;
        result = 31 * result + left;
        result = 31 * result + width;
        result = 31 * result + height;
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.top);
        dest.writeInt(this.left);
        dest.writeInt(this.width);
        dest.writeInt(this.height);
    }

    protected TransitionData(Parcel in) {
        this.id = in.readInt();
        this.top = in.readInt();
        this.left = in.readInt();
        this.width = in.readInt();
        this.height = in.readInt();
    }

    public static final Parcelable.Creator<TransitionData> CREATOR = new Parcelable.Creator<TransitionData>() {
        public TransitionData createFromParcel(Parcel source) {
            return new TransitionData(source);
        }

        public TransitionData[] newArray(int size) {
            return new TransitionData[size];
        }
    };
}
