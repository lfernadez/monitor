package py.fpuna.tesis.qoetest.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

import py.fpuna.tesis.qoetest.R;

/**
 * Created by User on 15/09/2014.
 */
public class HeaderTextView extends TextView {

    private final Paint mPaint = new Paint();

    private int mUnderlineHeight;

    public HeaderTextView(Context context) {
        super(context);
    }

    public HeaderTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.HeaderTextView, 0, 0);
        try {

            int underlineHeight = a.getDimensionPixelSize(
                    R.styleable.HeaderTextView_lineHeigth, context.getResources()
                            .getDimensionPixelSize(R.dimen.line_dimen));

            int underlineColor = a.getColor(R.styleable.HeaderTextView_lineColor,
                    context.getResources().getColor(R.color.darkBlue));

            setUnderlineHeight(underlineHeight);
            setUnderlineColor(underlineColor);
            setTextColor(underlineColor);

        } finally {
            a.recycle();
        }
    }

    public HeaderTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setUnderlineHeight(int underlineHeight) {
        if (underlineHeight < 0) {
            underlineHeight = 0;
        }
        if (underlineHeight != mUnderlineHeight) {
            mUnderlineHeight = underlineHeight;
            setPadding(getPaddingLeft(), getPaddingTop(), getPaddingRight(),
                    getPaddingBottom() + mUnderlineHeight);
        }
    }

    public void setUnderlineColor(int underlineColor) {
        if (mPaint.getColor() != underlineColor) {
            mPaint.setColor(underlineColor);
            invalidate();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(0, getHeight() - mUnderlineHeight,
                getWidth(), getHeight(), mPaint);
    }
}
