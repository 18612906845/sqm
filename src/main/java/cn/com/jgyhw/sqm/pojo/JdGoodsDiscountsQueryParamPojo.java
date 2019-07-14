package cn.com.jgyhw.sqm.pojo;

/**
 * Created by WangLei on 2019/2/23 0023 10:11
 *
 * 京东商品优惠查询参数Pojo
 */
public class JdGoodsDiscountsQueryParamPojo {

    private Long cid1; //一级类目id

    private Long cid2; //二级类目id

    private Long cid3; //三级类目id

    private int pageIndex; //页码

    private int pageSize; //每页数量，单页数最大30，默认20

    private String skuIds; //skuid集合(一次最多支持查询100个sku)，数组类型开发时记得加[]

    private String keyword; //关键词，字数同京东商品名称一致，目前未限制

    private String owner; //商品类型：自营[g]，POP[p]

    private String sortName; //排序字段(price：单价, commissionShare：佣金比例, commission：佣金， inOrderCount30Days：30天引单量， inOrderComm30Days：30天支出佣金)

    private String sort; //asc,desc升降序,默认降序

    private int isCoupon; //是否是优惠券商品，1：有优惠券，0：无优惠券

    private int isPG; //是否是拼购商品，1：拼购商品，0：非拼购商品

    private int isHot; //是否是爆款，1：爆款商品，0：非爆款商品

    public Long getCid1() {
        return cid1;
    }

    public void setCid1(Long cid1) {
        this.cid1 = cid1;
    }

    public Long getCid2() {
        return cid2;
    }

    public void setCid2(Long cid2) {
        this.cid2 = cid2;
    }

    public Long getCid3() {
        return cid3;
    }

    public void setCid3(Long cid3) {
        this.cid3 = cid3;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getSkuIds() {
        return skuIds;
    }

    public void setSkuIds(String skuIds) {
        this.skuIds = skuIds;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getSortName() {
        return sortName;
    }

    public void setSortName(String sortName) {
        this.sortName = sortName;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public int getIsCoupon() {
        return isCoupon;
    }

    public void setIsCoupon(int isCoupon) {
        this.isCoupon = isCoupon;
    }

    public int getIsPG() {
        return isPG;
    }

    public void setIsPG(int isPG) {
        this.isPG = isPG;
    }

    public int getIsHot() {
        return isHot;
    }

    public void setIsHot(int isHot) {
        this.isHot = isHot;
    }
}
