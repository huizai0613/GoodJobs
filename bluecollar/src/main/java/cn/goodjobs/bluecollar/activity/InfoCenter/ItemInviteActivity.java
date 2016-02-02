package cn.goodjobs.bluecollar.activity.InfoCenter;

import android.content.Intent;
import android.net.Uri;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import cn.goodjobs.bluecollar.R;
import cn.goodjobs.common.baseclass.BaseActivity;
import cn.goodjobs.common.util.IntentUtil;
import cn.goodjobs.common.util.LogUtil;
import cn.goodjobs.common.view.LoadingDialog;

/**
 * Created by zhuli on 2016/1/18.
 * 我要招聘页面
 */
public class ItemInviteActivity extends BaseActivity {

    private WebView wv;
//    private String url = "http://m.chenjun.goodjobs.lab/index.php/module/Blue/action/Attend?for=app"; // 测试地址
    private String url = "http://m.goodjobs.cn/index.php/module/Blue/action/Attend?for=app"; // 真实地址

    @Override
    protected int getLayoutID() {
        return R.layout.activity_item_invite;
    }

    @Override
    protected void initWeightClick() {

    }

    @Override
    protected void initWeight() {
        setTopTitle("我要招聘");
        wv = (WebView) findViewById(R.id.wv_invite);
        LoadingDialog.showDialog(this);
        WebSettings webSettings = wv.getSettings();
        wv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                LogUtil.info(url);
                if (url.endsWith("for=app")) {
                    view.loadUrl(url);
                } else {
                    IntentUtil.toWebActivity(ItemInviteActivity.this, url);
                }
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                LoadingDialog.hide();
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
            }
        });

        wv.setDownloadListener(new DownloadListener() {

            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                //实现下载的代码
                Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }

        });

        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setSaveFormData(false);
        webSettings.setLoadsImagesAutomatically(true);
        //自适应屏幕
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setLoadWithOverviewMode(true);
        // http请求的时候，模拟为火狐的UA会造成实时公交那边的页面存在问题，所以模拟iPhone的ua来解决这个问题
        String user_agent =
                "Mozilla/5.0 (Macintosh; U; PPC Mac OS X; en) AppleWebKit/124 (KHTML, like Gecko) Safari/125.1";
        webSettings.setUserAgentString(user_agent);


        wv.loadUrl(url);//页面请求地址
    }


    @Override
    protected void initData() {

    }
}
