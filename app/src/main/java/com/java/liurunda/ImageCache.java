//package com.java.liurunda;
//
//import android.graphics.Bitmap;
//import android.util.LruCache;
//
//import java.lang.ref.SoftReference;
//import java.util.Map;
//
//public class ImageCache extends LruCache<String, Bitmap> {
//    private Map<String, SoftReference<Bitmap>> cacheMap;
//
//    public ImageCache(Map<String, SoftReference<Bitmap>> cacheMap) {
//        super((int) (Runtime.getRuntime().maxMemory() / 8));
//        this.cacheMap = cacheMap;
//    }
//
//    @Override
//    protected int sizeOf(String key, Bitmap value) {
//        return value.getRowBytes() * value.getHeight();
//    }
//
//    @Override
//    protected void entryRemoved(boolean evicted, String key, Bitmap oldValue, Bitmap newValue) {
//        if (oldValue != null) {
//            SoftReference<Bitmap> softReference = new SoftReference<Bitmap>(oldValue);
//            cacheMap.put(key, softReference);
//        }
//    }
//
//    public Map<String, SoftReference<Bitmap>> getCacheMap() {
//        return cacheMap;
//    }
//}