/**
 * Copyright &copy; 2012-2013 <a href="https://github.com/freebirdweij/cloudroom">CloudRoom</a> All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 */
package com.freebirdweij.cloudroom.common.web;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.ckfinder.connector.ConnectorServlet;
import com.ckfinder.connector.ServletContextFactory;
import com.ckfinder.connector.configuration.ConfigurationPathBuilder;
import com.freebirdweij.cloudroom.common.utils.FileUtils;
import com.freebirdweij.cloudroom.modules.sys.entity.User;
import com.freebirdweij.cloudroom.modules.sys.utils.UserUtils;

/**
 * CKFinderConnectorServlet
 * @author CloudRoom
 * @version 2013-01-15
 */
public class CKFinderConnectorServlet extends ConnectorServlet {
	
	private static final long serialVersionUID = 1L;

	@Autowired
	private ConfigurationPathBuilder pathBuilder;
	
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		prepareGetResponse(request, response, false);
		super.doGet(request, response);
	}
	

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		prepareGetResponse(request, response, true);
		super.doPost(request, response);
	}
	
	private void prepareGetResponse(final HttpServletRequest request,
			final HttpServletResponse response, final boolean post) throws ServletException {
		String command = request.getParameter("command");
		String type = request.getParameter("type");
		/*String test = null;
		try {
			request.setCharacterEncoding("gb2312");
			type = request.getParameter("type");
			test = new String(request.getParameter("type").getBytes("iso8859-1"));
			test = new String(request.getParameter("type").getBytes("iso8859-1"),"gbk");
		} catch (UnsupportedEncodingException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}*/
		String basedir = "";
		User user = UserUtils.getUser();

		boolean isRoot = SecurityUtils.getSubject().isPermitted("sys:ckfinder:attach");
		if(isRoot){
			basedir = "/userfiles/";
		}else{
			basedir = "/userfiles/files/"+user.getName()+"(登陆名-"+
					(user!=null?user.getLoginName():0)+")/";
		}
			
		//user.setRemarks(basedir);
		// 初始化时，如果startupPath文件夹不存在，则自动创建startupPath文件夹
		if ("Init".equals(command)){
			if (user!=null){
				String startupPath = request.getParameter("startupPath");// 当前文件夹可指定为模块名
				if (startupPath!=null){
					String[] ss = startupPath.split(":");
					if (ss.length==2){
						String path = basedir+ss[0]+ss[1];
						String realPath = request.getSession().getServletContext().getRealPath(path);
						FileUtils.createDirectory(realPath);
					}
				}
			}
		}
		// 快捷上传，自动创建当前文件夹，并上传到该路径
		else if ("QuickUpload".equals(command) && type!=null){
			if (user!=null){
				String currentFolder = request.getParameter("currentFolder");// 当前文件夹可指定为模块名
				String path = basedir+type+(currentFolder!=null?currentFolder:"");
				String realPath = request.getSession().getServletContext().getRealPath(path);
				FileUtils.createDirectory(realPath);
			}
		}
//		System.out.println("------------------------");
//		for (Object key : request.getParameterMap().keySet()){
//			System.out.println(key + ": " + request.getParameter(key.toString()));
//		}
	}
	
}
