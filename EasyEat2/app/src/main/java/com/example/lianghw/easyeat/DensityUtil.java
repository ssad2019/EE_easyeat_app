/**
 * 项目名称：易餐
 * 项目为系统分析与设计课程的课程实验项目
 * 整个项目为扫码点餐系统
 * 这部分是整个项目的手机客户端部分
 * github地址：https://github.com/ssad2019/EE_easyeat_app
 * 启动日期：2019.5.1
 */
package com.example.lianghw.easyeat;

import android.content.Context;

public class DensityUtil {
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     * @param context Context 传入上下文
     * @param flt_dpValue float 转换的dp值
     * @return px 转换后的px值
     */
    public static int dpToPx(Context context, float flt_dpValue) {
        final float flt_scale = context.getResources().getDisplayMetrics().density;
        return (int) (flt_dpValue * flt_scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     * @param context Context 传入上下文
     * @param flt_pxValue float 转换的px值
     * @return dp 转换后的dp值
     */
    public static int pxToDp(Context context, float flt_pxValue) {
        final float flt_scale = context.getResources().getDisplayMetrics().density;
        return (int) (flt_pxValue / flt_scale + 0.5f);
    }
}
