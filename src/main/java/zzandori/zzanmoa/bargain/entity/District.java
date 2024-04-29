package zzandori.zzanmoa.bargain.entity;

public enum District {

    GANGNAM_GU("강남구", 680000),
    GANGDONG_GU("강동구", 740000),
    GANGSEO_GU("강서구", 500000),
    GWANAK_GU("관악구", 620000),
    GWANGJIN_GU("광진구", 215000),
    GURO_GU("구로구", 530000),
    NOWON_GU("노원구", 350000),
    DOBONG_GU("도봉구", 320000),
    DONGDAEMUN_GU("동대문구", 230000),
    MAPO_GU("마포구", 440000),
    SEO_DAE_MOON_GU("서대문구", 410000),
    SEOCHO_GU("서초구", 650000),
    SEONGDONG_GU("성동구", 200000),
    SEONGBUK_GU("성북구", 290000),
    SONGPA_GU("송파구", 710000),
    YANGCHEON_GU("양천구", 470000),
    YEONGDEUNGPO_GU("영등포구", 560000),
    YONGSAN_GU("용산구", 170000),
    EUNPYEONG_GU("은평구", 380000),
    JONGNO_GU("종로구", 110000),
    JUNG_GU("중구", 140000),
    JUNGNANG_GU("중랑구", 260000);

    private final String districtName;
    private final int districtId;

    District(String koreanName, int districtId) {
        this.districtName = koreanName;
        this.districtId = districtId;
    }

    public String getDistrictName() {
        return districtName;
    }

    public int getDistrictId() {
        return districtId;
    }

}
