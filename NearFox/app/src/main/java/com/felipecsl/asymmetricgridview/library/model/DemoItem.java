package com.felipecsl.asymmetricgridview.library.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

public class DemoItem implements AsymmetricItem {

    private int columnSpan;
    private int rowSpan;
    private int position;
    private String categoryTitle ;

    public void setImageUri(int imageUri) {
        this.imageUri = imageUri;
    }

    public int getImageUri() {

        return imageUri;
    }

    private int imageUri ;

    public DemoItem() {
        this(1, 1, 0, 0,"Category");
    }

    public DemoItem(int columnSpan, int rowSpan, int position, int imageUri, String categoryTitle) {
        this.columnSpan = columnSpan;
        this.rowSpan = rowSpan;
        this.position = position;
        this.categoryTitle = categoryTitle ;
        this.imageUri = imageUri ;
        this.imageUri = imageUri ;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }

    public String getCategoryTitle() {

        return categoryTitle;
    }

    public DemoItem(Parcel in) {
    readFromParcel(in);
  }

    @Override public int getColumnSpan() {
    return columnSpan;
    }

    @Override public int getRowSpan() {
    return rowSpan;
    }

    public int getPosition() {
    return position;
    }

    @Override public String toString() {
        return String.format("%s: %sx%s, value: %s", position, rowSpan, columnSpan, categoryTitle);
    }

    @Override public int describeContents() {
    return 0;
    }

    private void readFromParcel(Parcel in) {
        columnSpan = in.readInt();
        rowSpan = in.readInt();
        position = in.readInt();
        imageUri = in.readInt();
        categoryTitle = in.readString();
    }

    @Override public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(columnSpan);
        dest.writeInt(rowSpan);
        dest.writeInt(position);
        dest.writeInt(imageUri);
        dest.writeString(categoryTitle);
    }

    /* Parcelable interface implementation */
    public static final Parcelable.Creator<DemoItem> CREATOR = new Parcelable.Creator<DemoItem>() {

        @Override public DemoItem createFromParcel(@NonNull Parcel in) {
          return new DemoItem(in);
        }

        @Override @NonNull public DemoItem[] newArray(int size) {
            return new DemoItem[size];
        }
    };
}