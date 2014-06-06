package com.nevermissacall.utils;

import android.graphics.Typeface;

public class Fonts {
	public static final Typeface ROBOTO_REGULAR = Typeface.createFromAsset(MyApplication.getContext().getAssets(), "Roboto-Regular.ttf");
	public static final Typeface ROBOTO_BOLD = Typeface.createFromAsset(MyApplication.getContext().getAssets(), "Roboto-Bold.ttf");
	public static final Typeface NERETTA = Typeface.createFromAsset(MyApplication.getContext().getAssets(), "Neretta.ttf");
	public static final Typeface BOOK_ANTIQUA = Typeface.createFromAsset(MyApplication.getContext().getAssets(), "Book-Antiqua.ttf");
	public static boolean isRecordDeleted = false;
	public static String deletedNumber = null;

}
