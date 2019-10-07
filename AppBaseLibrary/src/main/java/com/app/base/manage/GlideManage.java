package com.app.base.manage;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * @Description Glide管理类
 * @Author Zhenhui
 * @Time 2019/8/21 15:18
 */
public class GlideManage {

//    public static void init(@NonNull Context context) {
//        GlideBuilder builder = new GlideBuilder();
//        builder.setSourceExecutor()
//        Glide.init(context, builder);
//    }

    private static RequestBuilder<Bitmap> getBitmap(Context context, @DrawableRes int placeholderId, String url) {
        return Glide.with(context)
                .asBitmap()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .load(url)
                .placeholder(placeholderId);
    }

    private static RequestBuilder<Bitmap> getBitmap(Context context, @DrawableRes int placeholderId, @DrawableRes int id) {
        return Glide.with(context)
                .asBitmap()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .dontAnimate()
                .load(id)
                .placeholder(placeholderId);
    }

    public static void loadBitmap(ImageView imageView, @DrawableRes int placeholderId, String url) {
        Context context = imageView.getContext();
        getBitmap(context, placeholderId, url).into(imageView);
    }

    public static void loadBitmap(Context context, @DrawableRes int placeholderId, String url, Target target) {
//        BitmapImageViewTarget target = new BitmapImageViewTarget(imageView) {
//            @Override
//            protected void setResource(Bitmap resource) {
//                super.setResource(resource);
//
//            }
//        };
        getBitmap(context, placeholderId, url).into(target);
    }

    public static void loadCircleBitmap(ImageView imageView, int placeHolder, String url) {
        getBitmap(imageView.getContext(), placeHolder, url).circleCrop().into(imageView);
    }

    public static void loadBlurTransformationBitmap(ImageView imageView, @DrawableRes int placeHolder, @DrawableRes int id) {
        Context context = imageView.getContext();
        getBitmap(context, placeHolder, id)
                // 设置高斯模糊,模糊程度(最大25)  缩放比例
                .apply(RequestOptions.bitmapTransform(new BlurTransformation(15)))
                .into(imageView);
    }
}
