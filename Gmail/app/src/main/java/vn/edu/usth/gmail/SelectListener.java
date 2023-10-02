package vn.edu.usth.gmail;

public interface SelectListener {
    default void onItemClicked(int position){
    }
    void onLongItemClick(int position);
}
