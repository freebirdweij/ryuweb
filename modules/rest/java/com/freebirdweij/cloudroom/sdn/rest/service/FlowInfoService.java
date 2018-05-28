/**
 * There are <a href="https://github.com/freebirdweij/cloudroom">CloudRoom</a> code generation
 */
package com.freebirdweij.cloudroom.sdn.rest.service;

import java.math.BigDecimal;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.freebirdweij.cloudroom.common.persistence.Page;
import com.freebirdweij.cloudroom.common.persistence.Parameter;
import com.freebirdweij.cloudroom.common.service.BaseService;
import com.freebirdweij.cloudroom.common.utils.Constants;
import com.freebirdweij.cloudroom.common.utils.DateUtils;
import com.freebirdweij.cloudroom.common.utils.StringUtils;
import com.freebirdweij.cloudroom.modules.expfetch.entity.ProjectExpert;
import com.freebirdweij.cloudroom.modules.expmanage.entity.ExpertConfirm;
import com.freebirdweij.cloudroom.modules.project.dao.ProjectInfoDao;
import com.freebirdweij.cloudroom.modules.project.entity.ProjectInfo;
import com.freebirdweij.cloudroom.modules.sys.entity.User;
import com.freebirdweij.cloudroom.modules.sys.utils.UserUtils;
import com.freebirdweij.cloudroom.sdn.rest.RYURestClient;

/**
 * 项目信息Service
 * @author Cloudman
 * @version 2014-07-08
 */
@Component
@Transactional(readOnly = true)
public class FlowInfoService extends BaseService {
	

	@Autowired
	private RYURestClient ryuclient;
	
	public boolean add(JSONObject fto,String urlbase) {
		return ryuclient.addFlowTableRow(fto,urlbase);
	}
	
	public JSONArray find(JSONObject fto,String urlbase,String dpid) {
		return ryuclient.getFlowTableRow(fto,urlbase,dpid);
	}
	
	public JSONArray list(String urlbase,String dpid) {
		return ryuclient.listFlowTableRows(urlbase,dpid);
	}
	
	public JSONArray switches(String urlbase) {
		return ryuclient.listSwitches(urlbase);
	}
	
	
	@Transactional(readOnly = false)
	public void save(ProjectInfo projectInfo) {
	}
	
	@Transactional(readOnly = false)
	public int update(ProjectInfo projectInfo){
		return 0;
	}
	
	@Transactional(readOnly = false)
	public boolean delete(JSONObject fto,String urlbase) {
		return ryuclient.deleteFlowTableRow(fto,urlbase);
	}
	
}
