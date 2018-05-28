package com.freebirdweij.cloudroom.modules.sys.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.freebirdweij.cloudroom.common.utils.Constants;
import com.freebirdweij.cloudroom.common.utils.Reflections;
import com.freebirdweij.cloudroom.modules.experts.entity.ExpertInfo;
import com.freebirdweij.cloudroom.modules.loginfo.entity.ExpertdbLog;
import com.freebirdweij.cloudroom.modules.project.entity.ProjectInfo;
import com.freebirdweij.cloudroom.modules.sys.entity.User;

public class LogUtils {

	public static ExpertdbLog getLogByExpert(ExpertInfo expertInfo,User user){
		if (expertInfo!=null&&StringUtils.isNotBlank(expertInfo.getName())){
			ExpertdbLog expertdbLog = new ExpertdbLog();
			expertdbLog.setObjectName(expertInfo.getName());
			expertdbLog.setObjectType(Constants.Log_Type_Expert);
			expertdbLog.setObjectUser(user);
			
			return expertdbLog;
		}
		return null;
	}
	
	public static ExpertdbLog getLogByProject(ProjectInfo projectInfo,User user){
		if (projectInfo!=null&&StringUtils.isNotBlank(projectInfo.getPrjName())){
			ExpertdbLog expertdbLog = new ExpertdbLog();
			expertdbLog.setObjectId(projectInfo.getId());
			expertdbLog.setObjectName(projectInfo.getPrjName());
			expertdbLog.setObjectType(Constants.Log_Type_Project);
			expertdbLog.setObjectUser(user);
			
			return expertdbLog;
		}
		return null;
	}
	
	public static String compareTwoBean(Object oldb,Object newb,Class<?> clas){
		StringBuffer strb = new StringBuffer();
		Method[] methods = clas.getDeclaredMethods();
		for(Method method:methods){
			  if(method.getName().startsWith("get")){
                  try {
                	  if(method.invoke(oldb)==null&&method.invoke(newb)!=null){
                		  if(method.invoke(newb) instanceof String){
      						strb.append("空").append("->").append(method.invoke(newb).toString()).append("|");						
                		  }else if(method.invoke(newb) instanceof Integer){
      						strb.append("空").append("->").append(method.invoke(newb).toString()).append("|");						
                		  }else if(method.invoke(newb) instanceof Date){
      						strb.append("空").append("->").append(method.invoke(newb).toString()).append("|");						
                		  }else if(method.invoke(newb) instanceof Double){
        						strb.append("空").append("->").append(method.invoke(newb).toString()).append("|");						
                		  }else if(method.invoke(newb) instanceof BigDecimal){
        						strb.append("空").append("->").append(method.invoke(newb).toString()).append("|");						
                		  }else{
                			  
                		  }
                		  
                	  }else if(method.invoke(oldb)!=null&&method.invoke(newb)==null){
                		  if(method.invoke(oldb) instanceof String){
      						strb.append(method.invoke(oldb).toString()).append("->").append("空").append("|");						
                		  }else if(method.invoke(oldb) instanceof Integer){
      						strb.append(method.invoke(oldb).toString()).append("->").append("空").append("|");						
                		  }else if(method.invoke(oldb) instanceof Date){
      						strb.append(method.invoke(oldb).toString()).append("->").append("空").append("|");						
                		  }else if(method.invoke(oldb) instanceof Double){
        					strb.append(method.invoke(oldb).toString()).append("->").append("空").append("|");						
                		  }else if(method.invoke(oldb) instanceof BigDecimal){
        					strb.append(method.invoke(oldb).toString()).append("->").append("空").append("|");						
                		  }else{
                			  
                		  }
                	  }else if(method.invoke(oldb)==null&&method.invoke(newb)==null){
                		  
                	  }else{
                		  if(method.invoke(oldb) instanceof String){
                			  if(!method.invoke(oldb).equals(method.invoke(newb)))
             			        strb.append(method.invoke(oldb).toString()).append("->").append(method.invoke(newb).toString()).append("|");						
                		  }else if(method.invoke(oldb) instanceof Integer){
                			  if(!method.invoke(oldb).equals(method.invoke(newb)))
             			        strb.append(method.invoke(oldb).toString()).append("->").append(method.invoke(newb).toString()).append("|");						
                		  }else if(method.invoke(oldb) instanceof Date){
                			  if(!method.invoke(oldb).equals(method.invoke(newb)))
             			        strb.append(method.invoke(oldb).toString()).append("->").append(method.invoke(newb).toString()).append("|");						
                		  }else if(method.invoke(oldb) instanceof Double){
                			  if(!method.invoke(oldb).equals(method.invoke(newb)))
             			        strb.append(method.invoke(oldb).toString()).append("->").append(method.invoke(newb).toString()).append("|");						
                		  }else if(method.invoke(oldb) instanceof BigDecimal){
                			  if(!method.invoke(oldb).equals(method.invoke(newb)))
             			        strb.append(method.invoke(oldb).toString()).append("->").append(method.invoke(newb).toString()).append("|");						
                		  }else{
                			  
                		  }
					  }
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
              }
		}
	return strb.toString();
	}

	public static ExpertdbLog getLogByCompareExpert(ExpertInfo oldexpert,ExpertInfo newexpert,User user){
		if (oldexpert!=null&&newexpert!=null){
			ExpertdbLog expertdbLog = new ExpertdbLog();
			expertdbLog.setObjectName(newexpert.getName());
			expertdbLog.setObjectType(Constants.Log_Type_Expert);
			expertdbLog.setObjectUser(user);
			
			StringBuffer strb = new StringBuffer();
			strb.append(Constants.Log_Function_ExpertEdit).append("修改了一位专家,具体变更如下：");
			strb.append(compareTwoBean(oldexpert,newexpert,ExpertInfo.class));
			expertdbLog.setOperation(strb.toString());
			
			return expertdbLog;
		}
		return null;
	}
	
	public static ExpertdbLog getLogByCompareProject(ProjectInfo oldproject,ProjectInfo newproject,User user){
		if (oldproject!=null&&newproject!=null){
			ExpertdbLog expertdbLog = new ExpertdbLog();
			expertdbLog.setObjectId(oldproject.getId());
			expertdbLog.setObjectName(newproject.getPrjName());
			expertdbLog.setObjectType(Constants.Log_Type_Project);
			expertdbLog.setObjectUser(user);
			
			StringBuffer strb = new StringBuffer();
			strb.append(Constants.Log_Function_ProjectEdit).append("修改了项目信息,具体变更如下：");
			strb.append(compareTwoBean(oldproject,newproject,ProjectInfo.class));
			expertdbLog.setOperation(strb.toString());
			
			return expertdbLog;
		}
		return null;
	}
}
