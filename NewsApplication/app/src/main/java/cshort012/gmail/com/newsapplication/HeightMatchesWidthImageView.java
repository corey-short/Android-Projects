package cshort012.gmail.com.newsapplication;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Short on 5/25/2015.
 */
public class HeightMatchesWidthImageView extends ImageView {
    public HeightMatchesWidthImageView(final Context context) {
        super(context);
    }

    public HeightMatchesWidthImageView(final Context context, final AttributeSet attrs, final int style) {
        super(context, attrs, style);
    }

    @Override
    protected void onMeasure(final int widthSpec, final int heightSpec) {
        int width = getDefaultSize(getSuggestedMinimumWidth(), widthSpec);
        setMeasuredDimension(width, width);
    }

    @Override
    protected void onSizeChanged(final int width, final int height, final int oldWidth, final int oldHeight) {
        super.onSizeChanged(width, width, oldWidth, oldHeight);
    }

}
