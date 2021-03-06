package ru.henridellal.emerald;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

public class MyCache {
	static public final int MODE_READ = 0;
	static public final int MODE_WRITE = 1;
	
	public static boolean write(Context c, String fname, 
			ArrayList<AppData> data) {
		String path = genFilename(c, fname);
//		Log.v("TinyLaunch", "cache write "+path+" "+data.size()+" items");
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(
					path+".temp"));
			for (AppData a: data) {
				a.write(writer);
			}
			writer.close();
			if (!new File(path+".temp").renameTo(new File(path))) {
				Log.e("TinyLaunch", "error renaming");
				throw new IOException();
			}
//			Log.v("TinyLaunch", "wrote cache to "+path);
		} catch (IOException e) {
//			Log.e("TinyLaunch", ""+e);
			new File(path+".tmp").delete();
			return false;
		}
		return true;		
	}
	
	public static void read(Context c, String fname, ArrayList<AppData> data) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(genFilename(c,fname)));
			for(;;) {
				AppData a = new AppData();
				a.read(reader);
				data.add(a);
			}
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}
	}

	static public String genFilename(Context c, String name) {
		File dir = c.getCacheDir();
		return dir.getPath() + "/" + name + ".MyCache"; 
	}
	public static File getCustomIconFile(Context c, String componentName) {
		return new File(c.getCacheDir(), 
				Uri.encode(componentName)+".custom.png");
	}
	public static File getIconFile(Context c, String componentName) {
		return new File(c.getCacheDir(), 
				Uri.encode(componentName)+".icon.png");
	}
	
	public static void deleteIcon(Context c, String componentName) {
		if (componentName.startsWith(" "))
			return;
		if (getIconFile(c, componentName).delete()) {
//			Log.v("TinyLaunch", "successful delete of "+componentName+" icon");
		}
	}
	/* removes icons of deleted apps */
	public static void cleanIcons(Context c, ArrayList<AppData> data) {
		ArrayList<String> components = new ArrayList<String>();
		for (AppData a : data)
			components.add(Uri.encode(a.getComponent())+".icon.png");
		
		File[] dirs = c.getCacheDir().listFiles();
		
		for (File f : dirs) {
			String name = f.getName();
			if (name.endsWith(".icon.png") && !components.contains(f.getName()))
				f.delete();
		}
	}
	/* removes all icons in cache */
	public static void deleteIcons(Context c) {
		File[] dirs = c.getCacheDir().listFiles();
		
		for (File f : dirs) {
			String name = f.getName();
			if (name.endsWith(".icon.png")) {
				f.delete();
			}
		}
	}
}
