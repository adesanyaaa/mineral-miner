package com.rivergillis.androidgames.framework.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Environment;
import android.preference.PreferenceManager;

import com.rivergillis.androidgames.framework.FileIO;

/**
 * Implementing the FileIO class, this class will allow us to easily get
 * input and output streams from filenames
 * @author jgillis
 *
 */
public class AndroidFileIO implements FileIO {
    Context context;				// instance is needed for many methods
    AssetManager assets;			// pulled from the context
    String externalStoragePath;		// stored for iostreams

    public AndroidFileIO(Context context) {
    	// Get references to the context and assets, as well as getting the external storage path string
        this.context = context;
        this.assets = context.getAssets();
        this.externalStoragePath = Environment.getExternalStorageDirectory()
                .getAbsolutePath() + File.separator;
    }

    // Returns an istream for reading the asset from the assetmanager
    public InputStream readAsset(String fileName) throws IOException {
        return assets.open(fileName);
    }

    // Returns a FileInputStream for reading the file
    public InputStream readFile(String fileName) throws IOException {
        return new FileInputStream(externalStoragePath + fileName);
    }

    // Returns a FileOutputStream for writing to the file
    public OutputStream writeFile(String fileName) throws IOException {
        return new FileOutputStream(externalStoragePath + fileName);
    }
    
    // Returns the SharedPreferences from the context
    public SharedPreferences getPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }
}
