package cn.goodjobs.common.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import cn.goodjobs.common.R;
import cn.goodjobs.common.baseclass.BaseActivity;
import cn.goodjobs.common.util.AlertDialogUtil;
import cn.goodjobs.common.util.IntentUtil;
import cn.goodjobs.common.util.LogUtil;
import cn.goodjobs.common.view.LoadingDialog;

/**
 * Created by zhuli on 2016/1/26.
 * 找回密码
 */
public class FoundPasswordActivity extends BaseActivity {
    private WebView wv;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_found;
    }

    @Override
    protected void initWeightClick() {

    }

    @Override
    protected void initWeight() {
        setTopTitle("找回密码");
        wv = (WebView) findViewById(R.id.wv_invite);
        LoadingDialog.showDialog(this);
        wv.loadUrl("http://m.goodjobs.cn/index.php/action/FindPw?for=app");//页面请求地址
        WebSettings webSettings = wv.getSettings();
        wv.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, final String url) {
                LogUtil.info(url);
                if (url.startsWith("tel:")) {
                    AlertDialogUtil.show(FoundPasswordActivity.this, "拨打号码", url.replaceAll("tel:", ""), true, "拨号", "取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            IntentUtil.toCallActivity(FoundPasswordActivity.this, url.replaceAll("tel:", ""));
                        }
                    }, null);
                } else if (url.endsWith("action/Login")) {
                    // 找回密码结束
                    FoundPasswordActivity.this.finish();
                } else {
                    view.loadUrl(url);
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
        webSettings.setNeedInitialFocus(true); //当webview调用requestFocus时为webview设置节点
        // http请求的时候，模拟为火狐的UA会造成实时公交那边的页面存在问题，所以模拟iPhone的ua来解决这个问题
        String user_agent =
                "Mozilla/5.0 (Macintosh; U; PPC Mac OS X; en) AppleWebKit/124 (KHTML, like Gecko) Safari/125.1";
        webSettings.setUserAgentString(user_agent);

        wv.requestFocus();
    }


    @Override
    protected void initData() {

    }
}
