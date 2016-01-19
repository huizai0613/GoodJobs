package cn.goodjobs.common.constants;

/**
 * Created by wanggang on 2015/10/9 0009.
 */
public class URLS {
    // 公共数据
    public final static String API_COMMON_META = "common/meta";  // 公共数据
    public final static String FOOTER = "Ehr/Footer";  // 企业底部公司电话和地址
    public final static String API_JOB_Newindex = "/Info/Newindex";  // 招聘会首页
    public final static String API_JOB_Learning = "Info/Learning";  // 培训首页
    public final static String API_JOB_appoint = "info/learnappoint";  // 预约
    public final static String API_JOB_consult = "info/learnconsult";  // 资询电话
    public final static String API_JOB_NewsDetails = "info/newid";  // 资讯详情
    public final static String API_JOB_FairList = "Info/Newlist";  // 招聘会列表
    public final static String API_JOB_SearchChextended = "Job/Searchextended";  // 关键字匹配
    public final static String API_JOB_JobList = "Job/Joblist";  // 全职职位搜索Fs
    public final static String API_JOB_JobShow = "Job/Jobshow";  // 全职/校园职位详情
    public final static String API_JOB_favorite = "Job/Addfavorite";  // 全职/校园职位收藏
    public final static String API_JOB_apply = "Job/Addapply";  // 全职/校园职位投递简历
    public final static String API_JOB_Corpshow = "Job/Corpshow";  // 企业详情
    public final static String API_JOB_Searcher = "Job/Searcher";  // 获取搜索器
    public final static String API_JOB_Searcherdel = "Job/Searcherdel";  // 获取搜索器
    public final static String API_JOB_Joball = "Job/Joball";  // 公司所有职位
    //蓝领
    public final static String API_BLUEJOB_Index = "Blue/Index";  // 蓝领首页
    public final static String API_BLUEJOB_Jobshow = "Blue/Jobshow";  // 蓝领职位详情
    public final static String API_BLUEJOB_Joblist = "Blue/Joblist";  // 蓝领职位列表
    public final static String API_BLUEJOB_Addapply = "Blue/Addapply";  // 蓝领职位申请
    public final static String API_BLUEJOB_Addfavorite = "Blue/Addfavorite";  // 蓝领职位收藏
    public final static String API_BLUEJOB_Corpshow = "Blue/Corpshow";  // 蓝领企业详情
    public final static String API_BLUEJOB_Feedback = "Blue/Feedback";  // 蓝领职位投诉


    // 登录相关
    public final static String API_PERSON_LOGIN = "user/Login"; // 登录
    public final static String API_USER_LOGOUT = "user/logout"; // 退出登录

    // 个人中心
    public final static String API_PERSON = "user/index"; // 个人中心
    public final static String API_USER_OUTBOX = "user/outbox";
    public final static String API_USER_OUTBOXDEL = "user/outboxDel";
    public final static String API_USER_USERFAVORITE = "user/favorite";
    public final static String API_USER_USERFAVORITEDEL = "user/favoritedel";
    public final static String API_USER_CORPVIEWHISTORY = "user/corpviewhistory";
    public final static String API_USER_CORPVIEWHISTORYDEL = "user/corpviewhistorydel";
    public final static String API_USER_INBOX = "user/Inbox";
    public final static String API_USER_INBOX_MAILDETAIL = "user/Maildetail";
    public final static String API_USER_INBOXDEL = "user/Inboxdel";

    // 简历相关
    public final static String API_CV_RESUME = "cv/resume";
    public final static String API_CV_SHOW = "cv/cvshow";
    public final static String API_CV_INDEX = "cv/index";
    public final static String API_CV_BASICSAVE = "cv/basicsave";
    public final static String API_CV_BASIC = "cv/basic";
    public final static String API_CV_PERSON = "cv/personal";
    public final static String API_CV_SAVE_PERSON = "cv/personalsave";
    public final static String API_CV_SAVE_LINK = "cv/linksave";
    public final static String API_CV_LINK = "cv/link";
    public final static String API_CV_WILL = "cv/will";
    public final static String API_CV_SAVE_WILL = "cv/willsave";
    public final static String API_CV_WEXP = "cv/Explist";
    public final static String API_CV_SAVE_WEXP = "cv/expsave";
    public final static String API_CV_EDIT_WEXP = "cv/exp";
    public final static String API_CV_DEL_WEXP = "cv/expdel";
    public final static String API_CV_LANGUAGE = "cv/lanlist";
    public final static String API_CV_SAVE_LAN = "cv/lansave";
    public final static String API_CV_EDIT_LAN = "cv/lan";
    public final static String API_CV_DEL_LAN = "cv/landel";
    public final static String API_CV_EDUCATION = "cv/edulist";
    public final static String API_CV_SAVE_EDU = "cv/edusave";
    public final static String API_CV_EDIT_EDU = "cv/edu";
    public final static String API_CV_DEL_EDU = "cv/edudel";
    public final static String API_CV_TRAINING = "cv/trainlist";
    public final static String API_CV_SAVE_TRA = "cv/trainsave";
    public final static String API_CV_EDIT_TRA = "cv/train";
    public final static String API_CV_DEL_TRA = "cv/traindel";
    public final static String API_CV_STULIST = "cv/praclist";
    public final static String API_CV_SAVE_STU = "cv/pracsave";
    public final static String API_CV_EDIT_STU = "cv/prac";
    public final static String API_CV_DEL_STU = "cv/pracdel";
    public final static String API_USER_OPENUPDATE = "user/openupdate";
    public final static String API_USER_OPENSET = "user/openset";
    public final static String API_USER_OPENADD = "user/openadd";
    public final static String API_USER_OPENSEARCH = "user/opensearch";
    public final static String API_USER_OPENDEL = "user/opendel";
    public final static String API_IMG_AD = "common/ad";
    public final static String API_USER_PASSWORDEDIT = "user/passwordedit";
    public final static String API_USER_USERNAMEEDIT = "user/changenamesave";
    public final static String API_USER_MOBILECHECK = "user/mobilechecksms";
    public final static String API_USER_MOBILECHECK1 = "user/mobilecheck";
    public final static String API_USER_REGISTERSMS = "user/registersms";
    public final static String API_USER_REGISTERNEW = "user/registerNew";

    // 我的校园
    public final static String CAMPUS_INDEX = "user/campus"; // 我的校园首页
    public final static String CAMPUS_OUTBOX = "user/campusoutbox"; // 网申记录列表
    public final static String CAMPUS_TALK = "user/campuscareertalk"; // 关注的宣讲会
    public final static String CAMPUS_FAIR = "User/Campusjobfair"; // 关注的招聘会
    public final static String CAMPUS_MSG = "User/Campusoperation"; // 通知信列表
    public final static String CAMPUS_MSGSHOW = "User/Campusoperationshow"; // 通知信详情
    public final static String CAMPUS_MSGDEL = "User/Campusoperationdel"; // 通知信删除

    public final static String API_JOB_ParttimeJob = "Parttime/List";      // 兼职列表
    public final static String API_JOB_ParttimeShow = "Parttime/Show";      // 兼职详情

    public final static String API_JOB_HeadHunt = "Headhunt/List";        // 猎头列表
    public final static String API_JOB_AgentList = "Headhunt/Agentlist";   // 代理列表
    public final static String API_JOB_BonusList = "Headhunt/Bonuslist";  // 悬赏列表
    public final static String API_JOB_Show = "Headhunt/Show";      // 悬赏详情
    public final static String API_JOB_RecommendData = "Headhunt/Recommendedit";      //悬赏招聘推荐操作加载的数据
    public final static String API_JOB_Recommendsave = "Headhunt/Recommendsave";      //悬赏招聘推荐操作


    public final static String API_JOB_Jobfairlist = "Campus/Jobfairlist";      //宣讲会
    public final static String API_JOB_Jobfairshow = "Campus/Jobfairshow";      //宣讲会详情

    // 交友
    public final static String MAKEFRIEND_ADDPIC = "Friend/Dynamicpicsave";      //动态上传图片
    public final static String MAKEFRIEND_ADDTREND = "Friend/Dynamicsave";      //动态发布
    public final static String MAKEFRIEND_BASICINFO = "Friend/Basicshow";      //个人资料
    public final static String MAKEFRIEND_BASICSAVE = "Friend/Basicsave";      //个人资料保存
    public final static String MAKEFRIEND_BASICPICSAVE = "Friend/Basicpicsave";      //个人资料头像保存
    public final static String MAKEFRIEND_TRENDLIST = "Friend/Dynamiclist";      //交友首页,附近的动态
    public final static String MAKEFRIEND_TRENDFRIENDLIST = "Friend/Dynamicmylist";      //
    public final static String MAKEFRIEND_TRENDDETAIL = "Friend/Dynamicshow";      //动态详情
    public final static String MAKEFRIEND_TRENDCOMMENT = "Friend/Replylist";      //动态详情评论列表
    public final static String MAKEFRIEND_CITYLIST = "Friend/Cityfriendlist";      //交友首页，同城交友
    public final static String MAKEFRIEND_COMMENT = "Friend/Replysave";      //动态评论与回复
    public final static String MAKEFRIEND_FOLLOW = "Friend/Followsave";      //交友关注和取消关注操作
    public final static String MAKEFRIEND_LIKE = "Friend/Dynamiclike";      //交友动态点赞
    public final static String MAKEFRIEND_FRIEND = "Friend/Friendshow";      //个人主页详情
    public final static String MAKEFRIEND_LOOKLIST = "Friend/Followlist";      //关注列表
    public final static String MAKEFRIEND_FANSLIST = "Friend/Fanslist";      //粉丝列表
    public final static String MAKEFRIEND_SMSLIST = "Friend/Smslist";      //聊天列表
    public final static String MAKEFRIEND_SMSSHOW = "Friend/Smsshow";      //私信详情
    public final static String MAKEFRIEND_SMSSEND = "Friend/Smssend";      //交友私信发送操作

    //EventBus 相关
    public final static String JOB_search_login = "JOB_search_login";      // 职位搜索登陆事件

    public final static String API_JOB_Addapply = "Job/Addapply";      //职位申请
    public final static String API_JOB_Addfavorite = "Job/Addfavorite";      //职位收藏
    public final static String API_JOB_Jobfairfollow = "Campus/Jobfairfollow";      //宣讲会关注与取消关注

    public final static String API_JOB_CampusIndex = "Campus/Index";      //校园招聘首页
    public final static String API_JOB_CampusJoblist = "Campus/Joblist";      //校园招聘搜索


    //蓝领个人中心
    public final static String API_JOB_BlueMy = "blueuser/my";      //蓝领个人中心首页
    public final static String API_JOB_UserCorpviewhistory = "User/Corpviewhistory";      //蓝领个人中心谁看过我的简历
    public final static String API_JOB_UserCampusoperationdel = "User/Corpviewhistorydel";      //蓝领个人中心谁看过我的简历删除
    public final static String API_JOB_BlueBasicsave = "Blueuser/Basicsave";      //蓝领简历保存数据
    public final static String API_JOB_BlueBasic = "Blueuser/Basic";      //蓝领简历修改数据显示
    public final static String API_JOB_UserUpdate = "User/Update";      //蓝领简历刷新
    public final static String API_JOB_BlueuserEntrustcv = "Blueuser/Entrustcv";      //蓝领简历刷新
    public final static String API_JOB_BlueuserOutbox = "Blueuser/Outbox";      //蓝领职位申请列表
    public final static String API_JOB_BlueuserOutboxdel = "Blueuser/Outboxdel";      // 蓝领申请职位删除且不可恢复操作
    public final static String API_JOB_BlueuserFavorite = "Blueuser/Favorite";      // 收藏职位列表
    public final static String API_JOB_BlueuserFavoritedel = "Blueuser/Favoritedel";      // 收藏职位删除进入回收站操作
    public final static String API_JOB_CvPhotosave = "cv/photosave";      // 头像上传
    public final static String API_JOB_UserFeedback = "user/Feedback";      // 意见反馈
}
