package com.exmart.qiyishow.tools;
/**
 * 常量类
 * @author ZhaoYe
 *
 */
public class Constant {
	
	public static final String OS = "2";//平台类型：0未知  1苹果  2安卓
	
	/****************Handler**************/
	public static final int MSG_USERID_FOUND = 1;
	
	public static final int MSG_LOGIN = 2;
	
	public static final int MSG_AUTH_CANCEL = 3;
	
	public static final int MSG_AUTH_ERROR= 4;
	
	public static final int MSG_AUTH_COMPLETE = 5;
	
	public static final String COUNT_OF_PAGE = "20";
	
	public static boolean isRefresh = false; //刷新头像
	
	
	
	/****************User SharedPreference**************/
	public static final String SPNAME = "info"; //用户文件名
	
	public static final String USERID = "userid"; //用户id
	
	public static final String NAME = "name"; //昵称
	
	public static final String PHOTO = "photo"; //头像
	
	public static final String SEX = "sex"; //性别
	
	public static final String PROVINCE = "province"; //省
	
	public static final String CITY = "city"; //市
	
	public static final String AREA = "area"; //省
	
	public static final String GUIDE = "guide" ;//是否显示引导页
	
	/****************系统 SharedPreference**************/
	public static final String OSNAME = "osinfo"; //系统文件名
	
	public static final String PLATFORM = "Platform";    //平台类型：0未知  1苹果  2安卓
	
	public static final String OSVERSION = "OSVersion";    //系统版本号
	
	public static final String APPVERSIONCODE = "AppVersionCode";   //软件版本号
	
	public static final String APPVERSIONNAME = "AppVersionName";   //软件版名称
	
	public static final String SCREENWIDTH = "ScreenWidth";  //屏幕宽度
	
	public static final String SCREEHEIGHT = "ScreeHeight";  //屏幕高度
	
	public static final String BD_USERID = "bd_userid"; //推送百度UserID
	
	public static final String BD_CHANNELID = "bd_channelId"; //推送百度ChannelID
	
	public static int SELECTED_VIDEO_SIZE_IN_MB = 20;
	
	
	/****************接口请求**************/
	
	public static final String URL = "http://121.41.23.1:8080/qyx/"; //接口地址
//	public static final String URL = "http://192.168.1.123:8080/qyx/";
	
	public static final String THREEUSERLOGIN =  URL+"ThreeUserLogin";
	
	public static final String SYSAPPINFO = URL + "SysAppInfo"; //版本更新
	
	public static final String VIDEOLIST = URL + "VideoList"; //视频列表
	
	public static final String VIDEODETAIL = URL + "VideoDetail"; //视频详情
	
	public static final String VIDEODCOMMENTS = URL + "VideoGetComment"; //视频评论
	
	public static final String ADDCOMMENT = URL + "VideoComment"; //添加评论
	
	public static final String VideoLIKE = URL + "VideoLike";//点赞
	
	public static final String VideoListByUser = URL + "VideoListByUser";//获取用户视频列表
	
	public static final String VIDEO_PUBLISH = URL + "VideoPublish";//发布视频
	
	public static final String VIDEO_REMOVE = URL + "VideoRemove";//删除视频
	
	public static final String USERUPDATEIMG = URL + "UserUpdateImg";//上传用户头像
	
	public static final String OTHER_REPORT = URL + "OtherReport";//反馈
	
	public static final String SETTING_PUSH = URL + "SettingPush";//推送设置
	
	public static final String VIDOE_REPORT = URL + "VideoReport";//举报视频
	
	public static final String USERINFO = URL + "UserInfo";//获取用户信息
	
	public static final String USER_UPDATE_INFO = URL + "UserUpdateInfo";//更新用户信息
	
	public static final String VIDEO_UPLOAD = URL + "VideoUpload";//视频上传
	
	public static final String VIDEO_IMAGE_UPLOAD = URL + "VideoImageUpload";//视频图片上传
	
	public static final String VIDEO_PLAY_COUNT = URL + "VideoCount";//视频播放次数
	
	public static final String USER_MESSAGE = URL + "UserMessage";// 用户消息
	
	public static final String TEMPLATE_TYPE = URL + "TypeTemplateDetail";//模版类型
	
	public static final String TEMPLATE_List = URL + "TemplateDetail";//模版类型列表
	
	
	public static final String EFFICACY_LIST = URL + "EfficiencyList";//模版类型列表
	public static final String EFFICACY_DETAIL = URL + "EfficiencyDetail";//模版类型列表
	
}