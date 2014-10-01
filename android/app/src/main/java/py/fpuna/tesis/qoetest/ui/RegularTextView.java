package py.fpuna.tesis.qoetest.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by User on 30/09/2014.
 */
public class RegularTextView extends TextView {
    public RegularTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(),
                "Roboto-Regular.ttf"));
    }
}
