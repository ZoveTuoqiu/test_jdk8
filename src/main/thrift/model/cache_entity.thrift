/*
* 缓存实体类相关
*
*/
namespace java com.qiyi.vrs.vis.h5.thrift.model

/**
* 缓存实体类型
**/
enum EntityType{
    ALBUM = 1;
    VIDEO = 2;
    RESOURCE = 3;
    LIVE_CHANNEL = 4;
    LIVE_PROGRAM= 5;
}

/**
* 缓存实体
**/
struct CacheEntity{
    //缓存key
    1: optional string key,
    //缓存内容
    2: optional string value,
    //缓存数据类型
    3: optional EntityType type,
    //是否不需要更新缓存
    4: optional bool noNeedUpdate,
}