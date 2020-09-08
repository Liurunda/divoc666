//package com.java.liurunda;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.widget.ImageView;
//import androidx.room.util.FileUtil;
//
//import java.io.File;
//import java.io.InputStream;
//import java.lang.ref.SoftReference;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.concurrent.CompletableFuture;
//
//public class CacheUtil {
//    private static CacheUtil instance;
//
//    private Context context;
//    private ImageCache imageCache;
//
//    private CacheUtil(Context context) {
//        this.context = context;
//        Map<String, SoftReference<Bitmap>> cacheMap = new HashMap<>();
//        this.imageCache = new ImageCache(cacheMap);
//    }
//
//    public static CacheUtil getInstance(Context context) {
//        if (instance == null) {
//            synchronized (CacheUtil.class) {
//                if (instance == null) {
//                    instance = new CacheUtil(context);
//                }
//            }
//        }
//        return instance;
//    }
//
//    private void putBitmapIntoCache(String fileName, byte[] data) {
//        FileUtil.getInstance(context).writeFileToStorage(fileName, data);
//        imageCache.put(fileName, BitmapFactory.decodeByteArray(data, 0, data.length));
//    }
//
//    private Bitmap getBitmapFromCache(String fileName) {
//        Bitmap bm = null;
//        bm = imageCache.get(fileName);
//        if (bm == null) {
//            Map<String, SoftReference<Bitmap>> cacheMap = imageCache.getCacheMap();
//            SoftReference<Bitmap> softReference = cacheMap.get(fileName);
//            if (softReference != null) {
//                bm = softReference.get();
//                imageCache.put(fileName, bm);
//            } else {
//                byte[] data = FileUtil.getInstance(context).readBytesFromStorage(fileName);
//                if (data != null && data.length > 0) {
//                    bm = BitmapFactory.decodeByteArray(data, 0, data.length);
//                    imageCache.put(fileName, bm);
//                }
//            }
//        }
//        return bm;
//    }
//
//    public void setImageToView(final String path, final ImageView view) {
//        final String fileName = path.substring(path.lastIndexOf(File.separator) + 1);
//        Bitmap bm = getBitmapFromCache(fileName);
//        if (bm != null) {
//            view.setImageBitmap(bm);
//        } else {
//            CompletableFuture.supplyAsync(() -> {
//                try {
//                    URL url = new URL(path);
//                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//
//                    conn.setConnectTimeout(5000);
//                    conn.setRequestMethod("GET");
//
//                    int code = conn.getResponseCode();
//
//                    if (code == 200) {
//                        InputStream in = conn.getInputStream();
//                        return BitmapFactory.decodeStream(in);
//                    } else {
//                        return null;
//                    }
//                } catch (Exception ignored) {
//                    return null;
//                }
//            }).thenAccept(view::setImageBitmap);
//        }
//    }
//}
