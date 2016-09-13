package tv.kuainiu.region;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;


/**
 * Adapter holding a list of animal names of type String. Note that each item must be unique.
 */
public abstract class RegionListAdapter<VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> {
    private ArrayList<Region> items = new ArrayList<>();

    public RegionListAdapter() {
        setHasStableIds(true);
    }

    public void add(Region object) {
        items.add(object);
        notifyDataSetChanged();
    }

    public void add(int index, Region object) {
        items.add(index, object);
        notifyDataSetChanged();
    }

    public void addAll(Collection<? extends Region> collection) {
        if (collection != null) {
            items.addAll(collection);
            notifyDataSetChanged();
        }
    }

    public void addAll(Region... items) {
        addAll(Arrays.asList(items));
    }

    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    public void remove(String object) {
        items.remove(object);
        notifyDataSetChanged();
    }

    public Region getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).hashCode();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
