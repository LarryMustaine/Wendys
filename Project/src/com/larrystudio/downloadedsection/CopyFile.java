package com.larrystudio.downloadedsection;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

public class CopyFile{

	private String LocalPath;
	private String SoundName;
	private Context context;
	private File musicDirectory;
	private File soundFile;

	public CopyFile(Context context, String LocalPath, String SoundName) {
		this.context   = context;
		this.LocalPath = LocalPath;
		this.SoundName = SoundName;
	}

	public void CopyFileToDestination(){
		musicDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
		String destName = musicDirectory.getAbsolutePath() + File.separatorChar + SoundName + ".mp3";
		musicDirectory = null;
		musicDirectory = new File(destName);
		musicDirectory.setWritable(true, false);
		
		soundFile = new File(LocalPath);
		
		try {
			copy(soundFile, musicDirectory);
			Toast.makeText(context, "Copied to: " + musicDirectory.getAbsolutePath(), Toast.LENGTH_LONG).show();
		} catch (IOException e) {
			Toast.makeText(context, "Couldn't copy file", Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
	}
	
	public void copy(File src, File dst) throws IOException {
	    InputStream in = new FileInputStream(src);
	    OutputStream out = new FileOutputStream(dst);

	    // Transfer bytes from in to out
	    byte[] buf = new byte[1024];
	    int len;
	    while ((len = in.read(buf)) > 0) {
	        out.write(buf, 0, len);
	    }
	    in.close();
	    out.close();
	}
}