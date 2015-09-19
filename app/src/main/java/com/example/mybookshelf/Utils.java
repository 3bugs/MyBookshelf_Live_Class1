package com.example.mybookshelf;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;

/**
 * Created by Promlert on 9/15/2015.
 */
public class Utils {

    private static final String IMAGES_DIR_NAME = "images";

    public static void copyFile(File src, File dst) throws IOException {
        FileInputStream inStream = new FileInputStream(src);
        FileOutputStream outStream = new FileOutputStream(dst);
        FileChannel inChannel = inStream.getChannel();
        FileChannel outChannel = outStream.getChannel();
        inChannel.transferTo(0, inChannel.size(), outChannel);
        inStream.close();
        outStream.close();
    }

    public static void copyFile(InputStream src, OutputStream dst) throws IOException {
        byte[] buffer = new byte[1024];
        int length;
        while ((length = src.read(buffer)) > 0) {
            dst.write(buffer, 0, length);
        }
        src.close();
        dst.close();
    }

    public static void copyFileFromAssetsToImagesDir(Context context, String imageFilename) throws IOException {
        AssetManager assets = context.getAssets();
        InputStream inStream = assets.open(imageFilename);

        File outFile = new File(getImagesDir(context), imageFilename);
        OutputStream outStream = new FileOutputStream(outFile);

        copyFile(inStream, outStream);
    }

    public static Bitmap getImageBitmap(Context context, String imageFilename) {
        File imageFile = new File(getImagesDir(context), imageFilename);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        return BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options);
    }

    public static File getImagesDir(Context context) {
        return createImagesDir(context);
    }

    public static File createImagesDir(Context context) {
        File internalDir = context.getFilesDir();
        File imagesDir = new File(internalDir, IMAGES_DIR_NAME);

        if (!imagesDir.exists()) {
            imagesDir.mkdir();
        }

        return imagesDir;
    }

    public static void saveBitmapToImageFile(Context context, Bitmap bitmap, String filename) {
        FileOutputStream outStream = null;
        try {
            File outFile = new File(getImagesDir(context), filename);
            outStream = new FileOutputStream(outFile);
/*
                outStream = mContext.openFileOutput(
                        IMAGE_DIR_NAME + "/" + filename,
                        Context.MODE_PRIVATE
                );
*/
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (outStream != null) {
                    outStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
