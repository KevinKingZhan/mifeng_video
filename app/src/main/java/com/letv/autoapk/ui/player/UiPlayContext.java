package com.letv.autoapk.ui.player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.lecloud.sdk.api.md.entity.action.ActionInfo;
import com.letv.autoapk.player.ISplayerController;

import android.os.Bundle;

class UiPlayContext {
	public static final int MODE_TOUCH = 0;
	public static final int MODE_MOVE = 1;
	public boolean panoNoticShowing;
	public boolean isPanoVideo;
	public int panoMode = MODE_TOUCH;

	/**
	 * 当前屏幕状态，默认竖屏
	 */
	private int screenOrientation = ISplayerController.SCREEN_ORIENTATION_PORTRAIT;

	/**
	 * 用户按下了暂停按钮
	 */
	private boolean isClickPauseByUser = false;

	/**
	 * 是否保存皮肤状态(比如seekbar的位置等等)
	 */
	private boolean saveInstanceState = false;

	/**
	 * 播放码率
	 */
	private ArrayList<RateTypeItem> rateTypeItems;
	/**
	 * 标题
	 */
	private String videoTitle;

	private String activityId;
	private ActionInfo actionInfo;
	private String multCurrentLiveId;
	/**
	 * 全屏时是否需要标题栏
	 */
	private boolean noWindowTitle = true;

	/**
	 * 全屏时返回按键是否直接退出播放页面
	 */
	private boolean isReturnback = true;
	/**
	 * 当前播放码率
	 */
	private String currentRateType;

	/**
	 * 是否监听网络变化
	 */
	private boolean useNetWorkNotice = true;

	/**
	 * 提示View是否正在展示
	 */
	private boolean isNotiveViewShowing = false;

	/**
	 * 是否在创建播放器的时候展示mediaController
	 */
	private boolean isShowMediaControllerOnStart = false;
	/**
	 * 是否在播放器提示错误的时候弹出mediaController
	 */
	private boolean isShowMediaControllerOnError = false;

	/**
	 * 锁定转屏
	 */
	private boolean lockFlag = false;

	/**
	 * gpc返回是否可以下载
	 */
	private boolean downloadable = false;

	/**
	 * 旋转屏幕的时候，控件延迟显示，0则表示直接显示,单位毫秒
	 */
	private int screenChangeShowDelay = 0;

	/**
	 * 播放器控制条，标题等的 显示时间，超过该时间将自动隐藏
	 */
	private int controllerHideTime = 8000;
	/**
	 * 是否正在播放广告
	 */
	private boolean isPlayingAd;
	/**
	 * 剧集列表
	 */
	private List<PlayVideoInfo> playlist;
	/**
	 * 剧集列表展示样式
	 */
	private int displayType;

	private boolean isShowDanmaku;
	/**
	 * 是否进入时移模式
	 */
	// private boolean isTimeShirtMode;
	private boolean enableTimeShift= true;
	/**
	 * 绘制开始
	 */
	private boolean renderingStart;
	/**
	 * 时移变量
	 */
	private int currentTimeShirtProgress = 0;
	private Bundle timeShirtData;
	/**
	 * 可以转屏
	 */
	private boolean enableAutoChangeScreen = true;
	/**
     * 登录状态
     */
	private boolean islogin;
	
	public boolean isIslogin() {
		return islogin;
	}

	public void setIslogin(boolean islogin) {
		this.islogin = islogin;
	}
	public boolean isEnableTimeShift() {
		return enableTimeShift;
	}

	public void setEnableTimeShift(boolean enableTimeShift) {
		this.enableTimeShift = enableTimeShift;
	}

	public boolean isEnableAutoChangeScreen() {
		return enableAutoChangeScreen;
	}

	public void setPlayList(List<PlayVideoInfo> list) {
		this.playlist = list;
	}

	public List<PlayVideoInfo> getPlayList() {
		return playlist;
	}

	public PlayVideoInfo getNext(String videoid, List<PlayVideoInfo> list) {
		if (list.size() < 1)
			return null;
		Iterator<PlayVideoInfo> it = list.iterator();
		while (it.hasNext()) {
			PlayVideoInfo playVideoInfo = (PlayVideoInfo) it.next();
			if (videoid.equals(playVideoInfo.getVideoId())) {
				if (it.hasNext()) {
					return it.next();
				}
				break;
			}
		}
		return list.get(0);
	}

	public void setEnableAutoChangeScreen(boolean enableAutoChangeScreen) {
		this.enableAutoChangeScreen = enableAutoChangeScreen;
	}

	public boolean isClickPauseByUser() {
		return isClickPauseByUser;
	}

	public void setClickPauseByUser(boolean isClickPauseByUser) {
		this.isClickPauseByUser = isClickPauseByUser;
	}

	public int getScreenResolution() {
		int resolution = ISplayerController.SCREEN_ORIENTATION_PORTRAIT;
		switch (screenOrientation) {
		case ISplayerController.SCREEN_ORIENTATION_LANDSCAPE:
		case ISplayerController.SCREEN_ORIENTATION_REVERSE_LANDSCAPE:
		case ISplayerController.SCREEN_ORIENTATION_USER_LANDSCAPE:
			resolution = ISplayerController.SCREEN_ORIENTATION_LANDSCAPE;
			break;
		case ISplayerController.SCREEN_ORIENTATION_PORTRAIT:
		case ISplayerController.SCREEN_ORIENTATION_REVERSE_PORTRAIT:
		case ISplayerController.SCREEN_ORIENTATION_USER_PORTRAIT:
			resolution = ISplayerController.SCREEN_ORIENTATION_PORTRAIT;
			break;
		default:
			break;
		}
		return resolution;
	}

	public void setScreenResolution(int screenResolution) {
		this.screenOrientation = screenResolution;
	}

	public boolean isSaveInstanceState() {
		return saveInstanceState;
	}

	public void setSaveInstanceState(boolean saveInstanceState) {
		this.saveInstanceState = saveInstanceState;
	}

	public ArrayList<RateTypeItem> getRateTypeItems() {
		return rateTypeItems;
	}

	public void setRateTypeItems(String[] ratetype, int[] typeId) {
		rateTypeItems = new ArrayList<RateTypeItem>();
		for (int i = 0; i < ratetype.length; i++) {
			RateTypeItem rateTypeItem = new RateTypeItem();
			rateTypeItem.setName(ratetype[i]);
			rateTypeItems.add(rateTypeItem);
		}
	}

	public void setRateTypeItems(Map<String, String> ratetypes) {
		if (ratetypes != null) {
			if (rateTypeItems == null) {
				rateTypeItems = new ArrayList<RateTypeItem>();
			} else {
				rateTypeItems.clear();
			}
			Iterator<Entry<String, String>> iterator = ratetypes.entrySet()
					.iterator();
			while (iterator.hasNext()) {
				RateTypeItem item = new RateTypeItem();
				Entry<String, String> entry = iterator.next();
				item.setTypeId(entry.getKey());
				item.setName(entry.getValue());
				rateTypeItems.add(item);
			}
		}
	}

	public RateTypeItem getRateTypeItemById(String id) {
		if (rateTypeItems != null) {
			for (int i = 0; i < rateTypeItems.size(); i++) {
				RateTypeItem item = rateTypeItems.get(i);
				if (id .equals(item.getTypeId()) ) {
					return item;
				}
			}
		}
		return null;
	}

	public String getVideoTitle() {
		return videoTitle;
	}

	public void setVideoTitle(String videoTitle) {
		this.videoTitle = videoTitle;
	}

	public boolean isNoWindowTitle() {
		return noWindowTitle;
	}

	public boolean isShowDanmaku() {
		return isShowDanmaku;
	}

	public void setShowDanmaku(boolean isShowDanmaku) {
		this.isShowDanmaku = isShowDanmaku;
	}

	public void setNoWindowTitle(boolean noWindowTitle) {
		this.noWindowTitle = noWindowTitle;
	}

	public boolean isReturnback() {
		return isReturnback;
	}

	public void setReturnback(boolean isReturnback) {
		this.isReturnback = isReturnback;
	}
    public void setRenderingStart(boolean isStart){
		renderingStart = isStart;
	}
	public boolean isRenderingStart(){
		return renderingStart;
	}
	public String getCurrentRateType() {
		return currentRateType;
	}

	public void setCurrentRateType(String currentRateType) {
		this.currentRateType = currentRateType;
	}

	public boolean isUseNetWorkNotice() {
		return useNetWorkNotice;
	}

	public void setUseNetWorkNotice(boolean useNetWorkNotice) {
		this.useNetWorkNotice = useNetWorkNotice;
	}

	public boolean isShowMediaControllerOnStart() {
		return isShowMediaControllerOnStart;
	}

	public void setShowMediaControllerOnStart(
			boolean isShowMediaControllerOnStart) {
		this.isShowMediaControllerOnStart = isShowMediaControllerOnStart;
	}

	public boolean isNotiveViewShowing() {
		return isNotiveViewShowing;
	}

	public void setNotiveViewShowing(boolean isNotiveViewShowing) {
		this.isNotiveViewShowing = isNotiveViewShowing;
	}

	public int getScreenChangeShowDelay() {
		return screenChangeShowDelay;
	}

	public void setScreenChangeShowDelay(int screenChangeShowDelay) {
		this.screenChangeShowDelay = screenChangeShowDelay;
	}

	public boolean isShowMediaControllerOnError() {
		return isShowMediaControllerOnError;
	}

	public void setShowMediaControllerOnError(
			boolean isShowMediaControllerOnError) {
		this.isShowMediaControllerOnError = isShowMediaControllerOnError;
	}

	public boolean isLockFlag() {
		return lockFlag;
	}

	public void setLockFlag(boolean lockFlag) {
		this.lockFlag = lockFlag;
	}

	public boolean isDownloadable() {
		return downloadable;
	}

	public void setDownloadable(boolean downloadable) {
		this.downloadable = downloadable;
	}

	public int getControllerHideTime() {
		return controllerHideTime;
	}

	public void setControllerHideTime(int controllerHideTime) {
		this.controllerHideTime = controllerHideTime;
	}

	public boolean isPlayingAd() {
		return isPlayingAd;
	}

	public void setIsPlayingAd(boolean isPlayingAd) {
		this.isPlayingAd = isPlayingAd;
	}

	public void setActionInfo(ActionInfo actionInfo) {
		this.actionInfo = actionInfo;
	}

	public ActionInfo getActionInfo() {
		return actionInfo;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public String getActivityId() {
		return activityId;
	}

	// public boolean isTimeShirtMode() {
	// return isTimeShirtMode;
	// }
	//
	// public void setTimeShirtMode(boolean isTimeShirtMode) {
	// this.isTimeShirtMode = isTimeShirtMode;
	// }

	public void setMultCurrentLiveId(String multCurrentLiveId) {
		this.multCurrentLiveId = multCurrentLiveId;
		notifyMultLiveStateChange();
	}

	public String getMultCurrentLiveId() {
		return multCurrentLiveId;
	}

	public interface MultLiveStateChangeCallback {
		void setCurrentMultLive(String liveId);
	}

	private List<MultLiveStateChangeCallback> multLiveStateChangeCallbacks = new ArrayList<MultLiveStateChangeCallback>();

	private void notifyMultLiveStateChange() {
		for (MultLiveStateChangeCallback callback : multLiveStateChangeCallbacks) {
			callback.setCurrentMultLive(multCurrentLiveId);
		}
	}

	public void registerMultLiveStateChangeListener(
			MultLiveStateChangeCallback callback) {
		if (!multLiveStateChangeCallbacks.contains(callback)) {
			multLiveStateChangeCallbacks.add(callback);
		}
	}

	public void unRegisterMultLiveChangeListener(
			MultLiveStateChangeCallback callback) {
		if (multLiveStateChangeCallbacks.contains(callback)) {
			multLiveStateChangeCallbacks.remove(callback);
		}
	}

	public Bundle getTimeShirtData() {
		return timeShirtData;
	}

	public void setTimeShirtData(Bundle timeShirtData) {
		this.timeShirtData = timeShirtData;
	}

	public int getCurrentTimeShirtProgress() {
		return currentTimeShirtProgress;
	}

	public void setCurrentTimeShirtProgress(int currentTimeShirtProgress) {
		this.currentTimeShirtProgress = currentTimeShirtProgress;
	}

	public int getDisplayType() {
		return displayType;
	}

	public void setDisplayType(int displayType) {
		this.displayType = displayType;
	}
}
