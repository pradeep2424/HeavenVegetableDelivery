package com.heaven.vegetable.utils;

import android.text.TextPaint;
import android.text.style.URLSpan;

/**
 * Created by Priyanka Bamboriya on 05-03-2018.
 */

public class URLSpanNoUnderline extends URLSpan {
    public URLSpanNoUnderline(String p_Url) {
        super(p_Url);
    }

    public void updateDrawState(TextPaint p_DrawState) {
        super.updateDrawState(p_DrawState);
        p_DrawState.setUnderlineText(false);
    }
}