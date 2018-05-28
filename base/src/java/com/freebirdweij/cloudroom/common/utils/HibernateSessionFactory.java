package com.freebirdweij.cloudroom.common.utils;


import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.springframework.beans.factory.annotation.Autowired;

public class HibernateSessionFactory {
	
	@Autowired
	private SessionFactory sessionFactory;
	/*ServiceRegistry是Hibernate4.X新增接口，
	应用于Hibernate的配置或者服务等将统一向这个ServiceRegistry注册后才能生效。
	所以需要构建一个ServiceRegistry对象，将配置信息向它注册，然后Configuration对象根据从这个ServiceRegistry对象中获取配置信息生成SessionFactory。
	Hibernate4的配置入口不再是Configuration对象，而是ServiceRegistry对象，Configuration对象将通过ServiceRegistry对象获取配置信息。
	hibernate4 源码位置：org.hibernate.service.ServiceRegistryBuilder	具体可参看hibernate源码。以及API*/
	
	
	/*static {
		try {
			// 首先获取配置信息
			configuration =  new Configuration().configure();
			serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();
			
			// 创建Session Factory			
			sessionFactory = configuration.buildSessionFactory(serviceRegistry);
		} catch (HibernateException e) {
			// System.out.println("SessionFactory创建失败");
			e.printStackTrace();
		}
	}*/
	
	public Session getSession() {
		Session session = null;
		if(null==session || false==session.isOpen()){
			session = sessionFactory.openSession();
		}
		
		return session;
	}
	
	public static void closeSession(Session session){
		try {
			if(null!=session && session.isOpen())
				session.close();
		} catch (HibernateException e) {
			e.printStackTrace();
		}
	}
}

