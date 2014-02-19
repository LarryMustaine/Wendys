package tools;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

public class CustomVideoView extends VideoView {
    
	private int mVideoWidth;
    private int mVideoHeight;
    
	
    public CustomVideoView(Context context) {
        super(context);
    }

    public CustomVideoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomVideoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        
        mVideoWidth = 0;
        mVideoHeight = 0;
    }

    @Override	
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    	
    	if(mVideoWidth <= 0 || mVideoHeight <= 0) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
    	
    	float heightRatio = (float) mVideoHeight / (float) getHeight();
        float widthRatio = (float) mVideoWidth / (float) getWidth();

        int scaledHeight;
        int scaledWidth;

        if (heightRatio > widthRatio) {
            scaledHeight = (int) Math.ceil((float) mVideoHeight
                    / heightRatio);
            scaledWidth = (int) Math.ceil((float) mVideoWidth
                    / heightRatio);
        } else {
            scaledHeight = (int) Math.ceil((float) mVideoHeight
                    / widthRatio);
            scaledWidth = (int) Math.ceil((float) mVideoWidth
                    / widthRatio);
        }
    	
        setMeasuredDimension(scaledWidth, scaledHeight);
    }
}