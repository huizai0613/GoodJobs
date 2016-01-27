package cn.goodjobs.common.constants;

/**
 * Created by wanggang on 2015/10/12 0012.
 * 常量
 */
public class Constant {

    public final static String LOGINDATA          =          "login_data"; //保存用户登录信息
    public final static String LOGINUSER          =          "login_user"; //保存用户名
    public final static String LOGINMEMBER        =          "login_member"; //保存会员名
    public final static String LOGINPASSWORD      =          "login_password"; //保存用户密码
    public final static String LOGINAUTO          =          "auto_login"; //是否自动登录
    public final static String LOGINTIME          =          "login_time"; //客户端最近一次登录时间（最近一次自动登录的时间）
    public final static String LOGINTIMECLIENT    =          "login_time_clint"; //手动点击登录的时间
    public final static String UPDATETIMECLIENT   =          "update_time_clint"; //更新数据时间
    public final static String FOOTDATA           =          "foot_data";

    public final static long sessionTimeout       =           2*3600*1000l; // 服务端session超时时间
    public final static long loginTimeout         =           30*24*3600*1000l; // 客户端超时时间，30天
    public final static long updateFootData       =           24*3600*1000l; // 1天更新一次数据
    public final static String COOKIE             =          "cookie"; //保存cookie

    public final static String TAG_HIDEDIALOG     =          "hidedialog"; //隐藏dialog

    public final static String SHARE_JOBDETAIL    =          "http://m.goodjobs.cn/job.php?jobID="; //分享职位连接地址
    public final static String SHARE_NEWS         =          "http://m.goodjobs.cn/index.php/module/Corp/action/NewId?newId="; //新安资讯分享
    public final static String SHARE_BLUE_JOBDETAIL=         "http://m.goodjobs.cn/blueJob.php?jobID="; //蓝领职位分享

    public enum module {
        ApplyJobs, // 求职端模块
        Lanling, // 蓝领模块
        Xiaoyuan, // 校园招聘
        Liepin, // 猎聘
        Jianzhi, // 兼职
    }
}
