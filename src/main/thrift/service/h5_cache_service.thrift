namespace java com.qiyi.vrs.vis.h5.thrift.service
include "../model/cache_entity.thrift"

service TH5Service{
    /**
    * 根据奇谱id及类型得到更新后的缓存实体
    **/
    cache_entity.CacheEntity getCacheEntity(1:i64 id, 2:cache_entity.EntityType entityType),

    /**
    *   更新对应缓存实体
    **/
    bool updateCacheEntity(1:cache_entity.CacheEntity cacheEntity),
}
