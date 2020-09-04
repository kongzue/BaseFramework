package com.kongzue.baseframework.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.kongzue.baseframework.interfaces.OnPictureSavedCallBack;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Bitmap 缓存器
 * 此类无须定义目录，可用于缓存 Bitmap
 * 注意：你可能获得 null 的结果；
 *
 * @author: Kongzue
 * @github: https://github.com/kongzue/
 * @homepage: http://kongzue.com/
 * @mail: myzcxhh@live.cn
 * @createTime: 2020/7/29 18:31
 */
public class PictureCache {
    
    public static String PICTURE_CACHE_FOLDER = "picCache";
    private boolean isEmpty = true;
    private File picFile;
    private Bitmap bitmapCache;
    private OnPictureSavedCallBack saveCallBack;
    
    private static List<PictureCache> pictureCacheSaveList;
    
    private PictureCache() {
    }
    
    /**
     * 读取图像
     *
     * @param context 上下文
     * @param path    路径
     * @param key     key
     */
    private PictureCache(Context context, String path, String key) {
        picFile = new File(getFolder(getFolder(context.getCacheDir(), PICTURE_CACHE_FOLDER), path), key);
        if (picFile.exists()) {
            isEmpty = false;
        }
    }
    
    /**
     * 存储图像
     *
     * @param context 上下文
     * @param path    路径
     * @param key     key
     * @param bitmap  图像
     */
    private PictureCache(Context context, String path, String key, Bitmap bitmap) {
        picFile = new File(getFolder(getFolder(context.getCacheDir(), PICTURE_CACHE_FOLDER), path), key);
        bitmapCache = Bitmap.createBitmap(bitmap);
        save();
    }
    
    /**
     * 存储图像(异步)
     *
     * @param context 上下文
     * @param path    路径
     * @param key     key
     * @param bitmap  图像
     */
    private PictureCache(Context context, String path, String key, Bitmap bitmap, OnPictureSavedCallBack callBack) {
        picFile = new File(getFolder(getFolder(context.getCacheDir(), PICTURE_CACHE_FOLDER), path), key);
        bitmapCache = Bitmap.createBitmap(bitmap);
    }
    
    private void save() {
        try {
            FileOutputStream out = new FileOutputStream(picFile);
            bitmapCache.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            isEmpty = false;
            
            if (saveCallBack != null) saveCallBack.onSaved(this);
        } catch (Exception e) {
            isEmpty = true;
        }
    }
    
    public static PictureCache read(Context context, String path, String key) {
        return new PictureCache(context, path, key);
    }
    
    public static PictureCache save(Context context, String path, String key, Bitmap bitmap) {
        return new PictureCache(context, path, key, bitmap);
    }
    
    public static void clean(Context context, String path) {
        File pathFolder = new File(getFolder(context.getCacheDir(), PICTURE_CACHE_FOLDER), path);
        deleteFolder(pathFolder);
    }
    
    private static void deleteFolder(File pathFolder) {
        if (pathFolder.exists()) {
            if (pathFolder.isDirectory()) {
                File[] files = pathFolder.listFiles();
                for (File f : files) {
                    deleteFolder(f);
                }
            } else {
                pathFolder.delete();
            }
            pathFolder.delete();
        }
    }
    
    public static void saveInBackground(Context context, String path, String key, Bitmap bitmap, OnPictureSavedCallBack callBack) {
        if (pictureCacheSaveList == null) pictureCacheSaveList = new ArrayList<>();
        pictureCacheSaveList.add(new PictureCache(context, path, key, bitmap, callBack));
        tryInBackgroundSave();
    }
    
    private static Thread saveThread;
    
    private static void tryInBackgroundSave() {
        synchronized (PictureCache.class) {
            if (saveThread == null) {
                saveThread = new Thread() {
                    @Override
                    public void run() {
                        if (pictureCacheSaveList != null) {
                            while (!pictureCacheSaveList.isEmpty()) {
                                PictureCache pc = pictureCacheSaveList.get(0);
                                pc.save();
                                pictureCacheSaveList.remove(pc);
                            }
                        }
                    }
                };
            }
            saveThread.start();
        }
    }
    
    public Bitmap getBitmap() {
        if (!isEmpty) {
            try {
                if (bitmapCache != null && !bitmapCache.isRecycled()) {
                    return bitmapCache;
                }
                bitmapCache = BitmapFactory.decodeFile(picFile.getAbsolutePath());
                if (bitmapCache != null) {
                    return bitmapCache;
                } else {
                    return tryToReadBigPic(inSampleSizeCache);
                }
            } catch (Exception e) {
                return tryToReadBigPic(inSampleSizeCache);
            }
        }
        return null;
    }
    
    public Bitmap getBitmap(int width, int height) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(picFile.getAbsolutePath(), options);
        int outWidth = options.outWidth;
        int outHeight = options.outHeight;
        int sampleSize = 1;
        while (outHeight / sampleSize > height || outWidth / sampleSize > width) {
            sampleSize *= 2;
        }
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(picFile.getAbsolutePath(), options);
    }
    
    private static int inSampleSizeCache = 1;
    
    /**
     * 尝试读取压缩图像
     *
     * @param inSampleSize 压缩倍率
     * @return 位图
     */
    private Bitmap tryToReadBigPic(int inSampleSize) {
        try {
            int maxMemory = (int) (Runtime.getRuntime().maxMemory());
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inTempStorage = new byte[maxMemory / (inSampleSize + 1)];
            opts.inPurgeable = true;
            opts.inSampleSize = inSampleSize;
            opts.inInputShareable = true;
            opts.inPreferredConfig = Bitmap.Config.ARGB_8888;
            bitmapCache = BitmapFactory.decodeFile(picFile.getAbsolutePath(), opts);
            inSampleSizeCache = inSampleSize;
        } catch (Exception e) {
            if (inSampleSize > 4) {
                //已经为原图的1/4,不再尝试，内存太小无能为力
                isEmpty = true;
                return null;
            }
            return tryToReadBigPic(inSampleSize++);
        }
        return null;
    }
    
    public File getFile() {
        return picFile;
    }
    
    public String getFilePath() {
        return picFile.getAbsolutePath();
    }
    
    public boolean copyTo(String path) {
        return copyTo(new File(path));
    }
    
    public boolean copyTo(File file) {
        try {
            File oldFile = picFile;
            if (!oldFile.exists()) {
                return false;
            }
            FileInputStream fileInputStream = new FileInputStream(picFile);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int byteRead;
            while (-1 != (byteRead = fileInputStream.read(buffer))) {
                fileOutputStream.write(buffer, 0, byteRead);
            }
            fileInputStream.close();
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    
    private static File getFolder(File dir, String folderName) {
        File cache = new File(dir, folderName);
        if (!cache.exists()) {
            cache.mkdirs();
        }
        return cache;
    }
    
    public boolean isEmpty() {
        return isEmpty;
    }
    
    public OnPictureSavedCallBack getSaveCallBack() {
        return saveCallBack;
    }
    
    public PictureCache setSaveCallBack(OnPictureSavedCallBack saveCallBack) {
        this.saveCallBack = saveCallBack;
        return this;
    }
}
