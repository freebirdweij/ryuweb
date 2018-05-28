/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/freebirdweij/cloudroom">CloudRoom</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.freebirdweij.cloudroom.modules.sys.dao;

import java.util.List;

import com.freebirdweij.cloudroom.common.persistence.annotation.MyBatisDao;
import com.freebirdweij.cloudroom.modules.sys.entity.Dict;

/**
 * MyBatis字典DAO接口
 * @author CloudRoom
 * @version 2013-8-23
 */
@MyBatisDao
public interface MyBatisDictDao {
	
    Dict get(String id);
    
    List<Dict> find(Dict dict);
    
}
