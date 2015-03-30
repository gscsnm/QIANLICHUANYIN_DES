package com.yichang.qianlichuanyin.view;
import com.yichang.qianlichuanyin.main.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
/**
 * 自定义的横向按钮群，可以通过参数控制有几个按钮
 * @author Administrator
 *
 */
public class ImageTextButton extends LinearLayout{
    private ImageView imageView;
    private TextView textView;
    private CharSequence text;
    private Drawable drawable;
    private float textSize;
    public ImageTextButton(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }
    /**
     * @param context
     * @param attrs
     * 按钮的功能
     */
    public ImageTextButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        TypedArray btnArr = context.obtainStyledAttributes(attrs, R.styleable.ImageTextButton);
        text = btnArr.getText(R.styleable.ImageTextButton_text);
        if(text==null){
            text="";
        }
        Drawable drawTemp = btnArr.getDrawable(R.styleable.ImageTextButton_src);
        if (drawTemp != null) {
            drawable=drawTemp;
        } else {
        	
            throw new RuntimeException("图像资源为空");  
        }
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 
        textSize = btnArr.getDimension(R.styleable.ImageTextButton_textSize,12);
        //String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.image_text_button, this);
        Log.v("test", "drawable"+drawable);
        imageView = (ImageView) findViewById(R.id.icon_part);
        if(imageView==null){
            Log.v("test", "iamgeView is null");

        }
        imageView.setImageDrawable(drawable);
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 
        textView = (TextView) findViewById(R.id.text_part);
        textView.setTextSize((float) textSize);
        textView.setText(text);
        if(text.equals("")||text==null){
            textView.setVisibility(View.GONE);
        }
        btnArr.recycle();
    }
    
    
    /**
     * @param resId to set
     */
    public void setImageResource(int resId) {
        imageView.setImageResource(resId);
    }
    
    
    /**
     * @param text to set
     */
    public void setTextViewText(String text) {
        textView.setText(text);
    }
}
