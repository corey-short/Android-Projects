package cshort012.gmail.com.newsapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Short on 5/25/2015.
 */
public class NewYorkTimesStoryAdapter extends ArrayAdapter<Story> {
    private LayoutInflater layoutInflater;

    public NewYorkTimesStoryAdapter(Context context, int textViewResourceID, List<Story> objects) {
        super(context, textViewResourceID, objects);
        System.out.println("adapter construct");
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View childView, ViewGroup parent) {
        View view = childView;
        System.out.println("view, " + childView);
        System.out.println("position, " + position);
        System.out.println("parent, " + parent);
        Holder holder;

        if (view == null) {
            // View does not exist so create it and create the holder
            view = layoutInflater.inflate(R.layout.grid_item, parent, false);

            holder = new Holder();
            holder.storyImage = (ImageView) view.findViewById(R.id.storyMultimedia);

            System.out.println("holder image, " + holder.storyImage);
            holder.storyTitle = (TextView) view.findViewById(R.id.storyTitle);

            //holder.storyUrl = (TextView) view.findViewById(R.id.storyUrl);

            view.setTag(holder);
        } else {
            // Our view exists so get the existing holder
            holder = (Holder) view.getTag();
        }

        // Populate the holder
        Story story = getItem(position);

        System.out.println("Story " + story);



        // Populate the item contents
        holder.storyTitle.setText(story.getTitle());

        // Load image on a background thread w/ LRU memory cache of 15%
        Picasso.with(getContext())
                .load(story.getMultimedia())
                .placeholder(R.color.white)
                .into(holder.storyImage);

        return view;
    }


    // Holder class used to recycle and repopulate our view positions
    private static final class Holder {
        public ImageView storyImage;
        public TextView storyTitle;
        public TextView storyUrl;
        public TextView storyAbstract;
    }
}
