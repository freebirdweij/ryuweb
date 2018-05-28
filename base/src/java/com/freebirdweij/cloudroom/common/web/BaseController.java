/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/freebirdweij/cloudroom">CloudRoom</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.freebirdweij.cloudroom.common.web;

import java.beans.PropertyEditorSupport;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.freebirdweij.cloudroom.common.beanvalidator.BeanValidators;
import com.freebirdweij.cloudroom.common.utils.Constants;
import com.freebirdweij.cloudroom.common.utils.DateUtils;
import com.freebirdweij.cloudroom.modules.expfetch.service.ProjectExpertService;
import com.freebirdweij.cloudroom.modules.expmanage.entity.ExpertConfirm;
import com.freebirdweij.cloudroom.modules.sys.entity.Office;

/**
 * 控制器支持类
 * @author CloudRoom
 * @version 2013-3-23
 */
public abstract class BaseController {

	/**
	 * 日志对象
	 */
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	/**
	 * 验证Bean实例对象
	 */
	@Autowired
	protected Validator validator;

	@Autowired
	public ProjectExpertService projectExpertService;
	
	/**
	 * 服务端参数有效性验证
	 * @param object 验证的实体对象
	 * @param groups 验证组
	 * @return 验证成功：返回true；严重失败：将错误信息添加到 message 中
	 */
	protected boolean beanValidator(Model model, Object object, Class<?>... groups) {
		try{
			BeanValidators.validateWithException(validator, object, groups);
		}catch(ConstraintViolationException ex){
			List<String> list = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
			list.add(0, "数据验证失败：");
			addMessage(model, list.toArray(new String[]{}));
			return false;
		}
		return true;
	}
	
	/**
	 * 服务端参数有效性验证
	 * @param object 验证的实体对象
	 * @param groups 验证组
	 * @return 验证成功：返回true；严重失败：将错误信息添加到 flash message 中
	 */
	protected boolean beanValidator(RedirectAttributes redirectAttributes, Object object, Class<?>... groups) {
		try{
			BeanValidators.validateWithException(validator, object, groups);
		}catch(ConstraintViolationException ex){
			List<String> list = BeanValidators.extractPropertyAndMessageAsList(ex, ": ");
			list.add(0, "数据验证失败：");
			addMessage(redirectAttributes, list.toArray(new String[]{}));
			return false;
		}
		return true;
	}
	
	/**
	 * 添加Model消息
	 * @param messages 消息
	 */
	protected void addMessage(Model model, String... messages) {
		StringBuilder sb = new StringBuilder();
		for (String message : messages){
			sb.append(message).append(messages.length>1?"<br/>":"");
		}
		model.addAttribute("message", sb.toString());
	}
	
	/**
	 * 添加Flash消息
     * @param messages 消息
	 */
	protected void addMessage(RedirectAttributes redirectAttributes, String... messages) {
		StringBuilder sb = new StringBuilder();
		for (String message : messages){
			sb.append(message).append(messages.length>1?"<br/>":"");
		}
		redirectAttributes.addFlashAttribute("message", sb.toString());
	}
	
	/**
	 * 初始化数据绑定
	 * 1. 将所有传递进来的String进行HTML编码，防止XSS攻击
	 * 2. 将字段中Date类型转换为String类型
	 */
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		// String类型转换，将所有传递进来的String进行HTML编码，防止XSS攻击
		binder.registerCustomEditor(String.class, new PropertyEditorSupport() {
			@Override
			public void setAsText(String text) {
				setValue(text == null ? null : StringEscapeUtils.escapeHtml4(text.trim()));
			}
			@Override
			public String getAsText() {
				Object value = getValue();
				return value != null ? value.toString() : "";
			}
		});
		// Date 类型转换
		binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
			@Override
			public void setAsText(String text) {
				setValue(DateUtils.parseDate(text));
			}
		});
	}

	/**
	 * 用于抽取交投的一位专家
	 * @param techcnt
	 * @param ecomcnt
	 * @param om
	 */
	public ExpertConfirm getAExpertByJiaoTouMap(Byte techcnt, Byte ecomcnt, Map<String, Office> om) {
		ExpertConfirm jec = null; 
		if(techcnt==0&&ecomcnt>0){
			
	        //以下进行随机选取计算
			boolean retry = true;
			int redo = 5;
			int resSize =om.values().size(); 
			int ri = 0;
			if(1<resSize){
		        Random r=new Random();   
		        int n = resSize;  
			do{
		         ri = r.nextInt(n);
			jec = projectExpertService.findAExpertByUnitAndKind(om.values().toArray(new Office[resSize])[ri], Constants.Expert_Kind_Economic);
			if(jec!=null) retry = false;
			redo--;
			}while(retry&&redo>0);
			
			}else{
				jec = projectExpertService.findAExpertByUnitAndKind(om.values().toArray(new Office[resSize])[ri], Constants.Expert_Kind_Economic);
			}
	        
			
		}else if(ecomcnt==0&&techcnt>0){
			
	        //以下进行随机选取计算
			boolean retry = true;
			int redo = 5;
			int resSize =om.values().size(); 
			int ri = 0;
			if(1<resSize){
		        Random r=new Random();   
		        int n = resSize;  
			do{
		         ri = r.nextInt(n);
			jec = projectExpertService.findAExpertByUnitAndKind(om.values().toArray(new Office[resSize])[ri], Constants.Expert_Kind_Technical);
			if(jec!=null) retry = false;
			redo--;
			}while(retry&&redo>0);
			
			}else{
				jec = projectExpertService.findAExpertByUnitAndKind(om.values().toArray(new Office[resSize])[ri], Constants.Expert_Kind_Technical);
			}
	        
			
		}else if(ecomcnt>0&&techcnt>0){
			
	        //以下进行随机选取计算
			boolean retry = true;
			int redo = 5;
			int resSize =om.values().size(); 
			int ri = 0;
			if(1<resSize){
		        Random r=new Random();   
		        int n = resSize;  
			do{
		         ri = r.nextInt(n);
			jec = projectExpertService.findAExpertByUnitAndKind(om.values().toArray(new Office[resSize])[ri],null);
			if(jec!=null) retry = false;
			redo--;
			}while(retry&&redo>0);
			
			}else{
				jec = projectExpertService.findAExpertByUnitAndKind(om.values().toArray(new Office[resSize])[ri],null);
			}
	        
		}
		return jec;
	}
	
	/**
	 * 用于抽取交投的一位专家
	 * @param techcnt
	 * @param ecomcnt
	 * @param om
	 */
	public ExpertConfirm getAExpertByJiaoTouMapRemoveBefore(Byte techcnt, Byte ecomcnt, Map<String, Office> om,String resIds) {
		ExpertConfirm jec = null; 
		if(techcnt==0&&ecomcnt>0){
			
	        //以下进行随机选取计算
			boolean retry = true;
			int redo = 5;
			int resSize =om.values().size(); 
			int ri = 0;
			if(1<resSize){
		        Random r=new Random();   
		        int n = resSize;  
			do{
		         ri = r.nextInt(n);
			jec = projectExpertService.findAExpertByUnitAndKindRemoveSomeExperts(om.values().toArray(new Office[resSize])[ri], Constants.Expert_Kind_Economic,resIds);
			if(jec!=null) retry = false;
			redo--;
			}while(retry&&redo>0);
			
			}else{
				jec = projectExpertService.findAExpertByUnitAndKindRemoveSomeExperts(om.values().toArray(new Office[resSize])[ri], Constants.Expert_Kind_Economic,resIds);
			}
	        
			
		}else if(ecomcnt==0&&techcnt>0){
			
	        //以下进行随机选取计算
			boolean retry = true;
			int redo = 5;
			int resSize =om.values().size(); 
			int ri = 0;
			if(1<resSize){
		        Random r=new Random();   
		        int n = resSize;  
			do{
		         ri = r.nextInt(n);
			jec = projectExpertService.findAExpertByUnitAndKindRemoveSomeExperts(om.values().toArray(new Office[resSize])[ri], Constants.Expert_Kind_Technical,resIds);
			if(jec!=null) retry = false;
			redo--;
			}while(retry&&redo>0);
			
			}else{
				jec = projectExpertService.findAExpertByUnitAndKindRemoveSomeExperts(om.values().toArray(new Office[resSize])[ri], Constants.Expert_Kind_Technical,resIds);
			}
	        
			
		}else if(ecomcnt>0&&techcnt>0){
			
	        //以下进行随机选取计算
			boolean retry = true;
			int redo = 5;
			int resSize =om.values().size(); 
			int ri = 0;
			if(1<resSize){
		        Random r=new Random();   
		        int n = resSize;  
			do{
		         ri = r.nextInt(n);
			jec = projectExpertService.findAExpertByUnitAndKindRemoveSomeExperts(om.values().toArray(new Office[resSize])[ri],null,resIds);
			if(jec!=null) retry = false;
			redo--;
			}while(retry&&redo>0);
			
			}else{
				jec = projectExpertService.findAExpertByUnitAndKindRemoveSomeExperts(om.values().toArray(new Office[resSize])[ri],null,resIds);
			}
	        
		}
		return jec;
	}
	
}
