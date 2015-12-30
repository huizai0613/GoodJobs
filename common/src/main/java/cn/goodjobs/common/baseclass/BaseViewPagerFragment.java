package cn.goodjobs.common.baseclass;

import android.os.Bundle;

import org.json.JSONObject;

import cn.goodjobs.common.view.empty.EmptyLayout;

/**
 * Created by yexiangyu on 15/12/28.
 */
public abstract class BaseViewPagerFragment extends BaseFragment
{

    protected EmptyLayout errorLayout;

    /**
     * 创建一个计算Fragment页面的实例，将怒num作为一个参数。
     * （Create a new instance of
     * CountingFragment, providing "num" as an argument.）
     *
     * @param id
     */
    public static BaseViewPagerFragment newInstance(int id, Class<? extends BaseViewPagerFragment> clazz)
    {

        BaseViewPagerFragment f = null;
        try {
            f = clazz.newInstance();
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        Bundle args = new Bundle();
        args.putInt("ID", id);
        f.setArguments(args);
        return f;
    }



    protected abstract void getData();

    protected abstract void setData(JSONObject dataJson);

    @Override
    public void onSuccess(String tag, Object data)
    {
        super.onSuccess(tag, data);
        if (errorLayout != null) {
            errorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
        }
    }

    @Override
    public void onFailure(int statusCode, String tag)
    {
        super.onFailure(statusCode, tag);

        if (errorLayout != null) {
            errorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
        }
    }
}
