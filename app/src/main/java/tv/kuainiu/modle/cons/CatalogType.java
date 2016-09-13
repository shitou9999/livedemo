package tv.kuainiu.modle.cons;

/**
 * @author nanck on 2016/3/9.
 */
public enum CatalogType {
    CatalogTypeAll(0),
    CatalogTypeParseVideo(10),     //解盘
    CatalogTypeKnowledge(11),      //知识
    CatalogTypePredict(12),        //盘前
    CatalogTypeTips(13),           //心法
    CatalogTypeLecture(38),        //讲座
    CatalogTypeNews,               //新闻
    CatalogTypeParsing(54),        //盘中
    CatalogTypeParsed(55),         //盘后
    CatalogTypeProgram,            //节目
    CatalogTypeGDQCArticle(66),    //gdqc文章
    CatalogTypeGDQCVideo(64),      //gdqc视频
    CatalogTypeJQFBVideo(65),      //jqfb视频
    CatalogTypeJQFBArticle(68),    //jqfb视频


    CatalogNewsAll(0),
    CatalogNewsCeLueYanBao(16),     //策略研报
    CatalogNewsGeGuYanBao(17),      //个股研报
    CatalogNewsHangYeYanBao(18),    //行业研报
    CatalogNewsGuZhiQiHuo(19),      //股指期货
    CatalogNewsRongZiRongQuan(20),  //融资融券
    CatalogNewsGuXuanZhiYa(21),     //股轩质押
    CatalogNewsYeJiBaoGao(22),      //业绩报告
    CatalogNewsZhongGuoCaiJin(23),  //中国财经
    CatalogNewsQuanQiuCaiJing(24),  //全球财经

    ColumnTypeParsing(0),           //解盘视频
    ColumnTypeGDQC,                 //鼓动钱潮
    ColumnTypeJQFB,                 //金钱风暴

    ParseTypeAll(0),                //全部
    ParseTypeMorning,               //早间
    ParseTypeNoon,                  //午间
    ParseTypeNight;                 //晚间

    CatalogType() {
    }

    CatalogType(int type) {
        this.type = type;
    }

    private int type;

    public int type() {
        return this.type;
    }

}
