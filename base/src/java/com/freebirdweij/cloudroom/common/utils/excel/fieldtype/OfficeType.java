/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/freebirdweij/cloudroom">CloudRoom</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.freebirdweij.cloudroom.common.utils.excel.fieldtype;

import com.freebirdweij.cloudroom.modules.sys.entity.Office;
import com.freebirdweij.cloudroom.modules.sys.utils.UserUtils;

/**
 * 字段类型转换
 * @author CloudRoom
 * @version 2013-03-10
 */
public class OfficeType {

	/**
	 * 获取对象值（导入）
	 */
	public static Object getValue(String val) {
		for (Office e : UserUtils.getOfficeList()){
			if (val.equals(e.getName())){
				return e;
			}
		}
		return null;
	}

	/**
	 * 设置对象值（导出）
	 */
	public static String setValue(Object val) {
		if (val != null && ((Office)val).getName() != null){
			return ((Office)val).getName();
		}
		return "";
	}
}
