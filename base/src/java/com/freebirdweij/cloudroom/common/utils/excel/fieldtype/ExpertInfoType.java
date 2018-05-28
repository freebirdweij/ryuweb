package com.freebirdweij.cloudroom.common.utils.excel.fieldtype;

import com.freebirdweij.cloudroom.modules.experts.entity.ExpertInfo;
import com.freebirdweij.cloudroom.modules.sys.entity.Office;
import com.freebirdweij.cloudroom.modules.sys.utils.UserUtils;

/**
 * 字段类型转换
 * @author Cloudman
 * @version 2014-03-10
 */
public class ExpertInfoType {

	/**
	 * 获取对象值（导入）
	 */
	public static Object getValue(String val) {
		ExpertInfo  expertInfo = new ExpertInfo();
		expertInfo.setName(val);
		return expertInfo;
	}

	/**
	 * 设置对象值（导出）
	 */
	public static String setValue(Object val) {
		if (val != null && ((ExpertInfo)val).getName() != null){
			return ((ExpertInfo)val).getName();
		}
		return "";
	}
}
