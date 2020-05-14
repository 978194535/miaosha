package com.miaoshaproject.config;

import com.miaoshaoproject.service.Model.ItemModel;

public interface CacheLoadAble<T> {
    public T load();
}
