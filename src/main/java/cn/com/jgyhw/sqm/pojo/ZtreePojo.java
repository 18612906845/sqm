package cn.com.jgyhw.sqm.pojo;

import java.util.Map;

/**
 * zTree树Pojo
 */
public class ZtreePojo {

    private String id;

    private String pId;

    private String pName;

    private String name;

    private int subMenuNum;

    private boolean isParent = true;

    private boolean open = false;

    private Map<String, Object> diy;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isParent() {
        return isParent;
    }

    public void setParent(boolean parent) {
        isParent = parent;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public Map<String, Object> getDiy() {
        return diy;
    }

    public void setDiy(Map<String, Object> diy) {
        this.diy = diy;
    }

    public int getSubMenuNum() {
        return subMenuNum;
    }

    public void setSubMenuNum(int subMenuNum) {
        this.subMenuNum = subMenuNum;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }
}
