package cn.com.jgyhw.sqm.pojo;

/**
 * Created by WangLei on 2019/5/26 0026 20:29
 *
 * 定时任务查询返回值Pojo
 */
public class TimingTaskQueryResultPojo {

    /**
     * 查询状态，true为正常
     */
    private boolean status = false;

    /**
     * 是否还有更多
     */
    private boolean isMore = true;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean isMore() {
        return isMore;
    }

    public void setMore(boolean more) {
        isMore = more;
    }
}
