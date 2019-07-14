package cn.com.jgyhw.sqm.domain;

/**
 * 微信公众号菜单
 */
public class WxGzhMenu {
    /**
     * 标识
     */
    private String id;

    /**
     * 菜单层级
     */
    private Integer levelNum;

    /**
     * 菜单的响应动作类型
     */
    private String actionType;

    /**
     * 菜单标题名称
     */
    private String menuName;

    /**
     * 菜单KEY值，用于消息接口推送
     */
    private String menuKey;

    /**
     * 网页链接，用户点击菜单可打开链接
     */
    private String url;

    /**
     * media_id
     */
    private String mediaId;

    /**
     * 小程序AppId
     */
    private String appId;

    /**
     * 小程序页面路径
     */
    private String pagePath;

    /**
     * 上级父ID
     */
    private String parentId;

    /**
     * 排序号
     */
    private Integer orderNum;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public Integer getLevelNum() {
        return levelNum;
    }

    public void setLevelNum(Integer levelNum) {
        this.levelNum = levelNum;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType == null ? null : actionType.trim();
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName == null ? null : menuName.trim();
    }

    public String getMenuKey() {
        return menuKey;
    }

    public void setMenuKey(String menuKey) {
        this.menuKey = menuKey == null ? null : menuKey.trim();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId == null ? null : mediaId.trim();
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId == null ? null : appId.trim();
    }

    public String getPagePath() {
        return pagePath;
    }

    public void setPagePath(String pagePath) {
        this.pagePath = pagePath == null ? null : pagePath.trim();
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId == null ? null : parentId.trim();
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", levelNum=").append(levelNum);
        sb.append(", actionType=").append(actionType);
        sb.append(", menuName=").append(menuName);
        sb.append(", menuKey=").append(menuKey);
        sb.append(", url=").append(url);
        sb.append(", mediaId=").append(mediaId);
        sb.append(", appId=").append(appId);
        sb.append(", pagePath=").append(pagePath);
        sb.append(", parentId=").append(parentId);
        sb.append(", orderNum=").append(orderNum);
        sb.append("]");
        return sb.toString();
    }
}