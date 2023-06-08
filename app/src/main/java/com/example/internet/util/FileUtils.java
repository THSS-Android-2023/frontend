package com.example.internet.util;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

// GENERETED BY CHATGPT3
public class FileUtils {

    @SuppressLint("NewApi")
    public static String getPath(final Context context, final Uri uri) {
        // 小于API 19
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return getPathBeforeKitKat(context, uri);
        }

        // 大于等于API 19
        return getPathAfterKitKat(context, uri);
    }

    // 获取API 19之前的文件路径
    private static String getPathBeforeKitKat(Context context, Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            if (cursor.moveToFirst()) {
                return cursor.getString(column_index);
            }
            cursor.close();
        }
        return null;
    }

    // 获取API 19及之后的文件路径
//    @SuppressLint("NewApi")
//    private static String getPathAfterKitKat(Context context, Uri uri) {
//        String docId = DocumentsContract.getDocumentId(uri);
//        if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
//            String id = docId.split(":")[1];
//            String selection = MediaStore.Images.Media._ID + "=" + id;
//            return getDataColumn(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection, null);
//        } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
//            Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
//            return getDataColumn(context, contentUri, null, null);
//        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
//            return getDataColumn(context, uri, null, null);
//        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
//            return uri.getPath();
//        }
//        return null;
//    }
    public static String getPathAfterKitKat(Context context, Uri uri) {
        String docId = DocumentsContract.getDocumentId(uri);
        String[] split = docId.split(":");
        String type = split[0];
        if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
            String id = split[1];
            String selection = MediaStore.Images.Media._ID + "=" + id;
            return getDataColumn(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection, null);
        } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
            Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
            return getDataColumn(context, contentUri, null, null);
        } else if ("com.android.externalstorage.documents".equals(uri.getAuthority())) {
            String path = "/" + split[1];
            return Environment.getExternalStorageDirectory() + path;
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    // 获取数据列
    private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        String column = MediaStore.Images.Media.DATA;
        String[] projection = { column };
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }
}
