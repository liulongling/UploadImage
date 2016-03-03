package com.lll.app.utils;

import java.io.Closeable;
import java.io.IOException;

public class CloseUtils {
	private CloseUtils(){
		throw new UnsupportedOperationException("cannot be instantiated");
	}
	
	/**
	 * πÿ±’Closeable∂‘œÛ
	 * @param closeable
	 */
	public static void closeQuietly(Closeable  closeable){
		if(closeable!=null){
			try {
				closeable.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
