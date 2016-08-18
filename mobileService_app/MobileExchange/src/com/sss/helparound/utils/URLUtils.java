package com.sss.helparound.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import com.sss.helparound.R;

/**
 * URL utils
 */
public class URLUtils {

    /**
     * Get an URL
     * 
     * @param {Context} ctx
     * @param {Integer} urlId
     * @param {Object...} formatArgs
     * @return {String} url
     * @throws NotFoundException
     */
    public static final String getURL(Context ctx, Integer urlId, Object... formatArgs) throws NotFoundException {
        String serverURL = ctx.getResources().getString(R.string.server_url);

        // create new args
        Object[] args = new Object[formatArgs.length + 1];
        args[0] = serverURL;
        // append formatArgs
        for (int i = 0, length = formatArgs.length; i < length; i++) {
            args[i + 1] = formatArgs[i];
        }

        return ctx.getResources().getString(urlId, args);
    }
    
	public static final String encode(String raw) {
		String encoded = "";
		if (raw != null){
			try {
				encoded = URLEncoder.encode(raw, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			}
    	}
		return encoded;
	}

}
