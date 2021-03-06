package ru.henridellal.emerald;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;

public class RestartPreference extends Preference {
	public RestartPreference(Context c) {
		this(c, null);
	}
	public RestartPreference(Context c, AttributeSet attr) {
		super(c, attr);
	}
	@Override
	public void onClick() {
		super.onClick();
		System.exit(0);
	}
}
