/**
 * There are <a href="https://github.com/freebirdweij/cloudroom">CloudRoom</a> code generation
 */
package com.freebirdweij.cloudroom.sdn.rest.web;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.freebirdweij.cloudroom.common.config.Global;
import com.freebirdweij.cloudroom.common.exec.NetTelnet;
import com.freebirdweij.cloudroom.common.mapper.JsonMapper;
import com.freebirdweij.cloudroom.common.persistence.Page;
import com.freebirdweij.cloudroom.common.utils.Constants;
import com.freebirdweij.cloudroom.common.utils.DateUtils;
import com.freebirdweij.cloudroom.common.utils.PropertiesLoader;
import com.freebirdweij.cloudroom.common.utils.StringUtils;
import com.freebirdweij.cloudroom.common.web.BaseController;
import com.freebirdweij.cloudroom.modules.experts.entity.ExpertInfo;
import com.freebirdweij.cloudroom.modules.loginfo.entity.ExpertdbLog;
import com.freebirdweij.cloudroom.modules.loginfo.service.ExpertdbLogService;
import com.freebirdweij.cloudroom.modules.project.entity.ProjectInfo;
import com.freebirdweij.cloudroom.modules.project.service.ProjectInfoService;
import com.freebirdweij.cloudroom.modules.sys.entity.User;
import com.freebirdweij.cloudroom.modules.sys.utils.LogUtils;
import com.freebirdweij.cloudroom.modules.sys.utils.UserUtils;
import com.freebirdweij.cloudroom.sdn.rest.entity.FlowBean;
import com.freebirdweij.cloudroom.sdn.rest.entity.SwitchBean;
import com.freebirdweij.cloudroom.sdn.rest.service.FlowInfoService;

/**
 * 项目信息Controller
 * @author Cloudman
 * @version 2014-07-08
 */
@Controller
@RequestMapping(value = "${adminPath}/rest/super")
public class SuperManageController extends BaseController {

	@Autowired
	private FlowInfoService flowInfoService;
	
	@Autowired
	private ExpertdbLogService expertdbLogService;
	
	private static HashMap<String, SwitchBean> switches = new HashMap<String, SwitchBean>();
	
	private static String userlogin;
	
	private static String passlogin;
	
	private static String prompt;
	
	
	@RequiresPermissions("rest:flowlist:list")
	@RequestMapping(value = {"searchlist", ""})
	public String searchlist(FlowBean findBean,HttpServletRequest request, HttpServletResponse response, Model model) {
		
		PropertiesLoader propertiesLoader = new PropertiesLoader("cloudroom.properties");
		
		switches.clear();
		
		userlogin = propertiesLoader.getProperty("switch.default.userlogin");
		passlogin = propertiesLoader.getProperty("switch.default.passlogin");
		prompt = propertiesLoader.getProperty("switch.default.prompt");
		
		String total = propertiesLoader.getProperty("switch.total");
		if(total!=null&&!total.equals("0")){
			int tal = Integer.valueOf(total);
			for(int i=1;i<=tal;i++){
				String dpid = propertiesLoader.getProperty("switch."+i+".dpid");
				SwitchBean sbean = new SwitchBean();
				sbean.setSequence(String.valueOf(i));
				sbean.setDpid(dpid);
				sbean.setIp(propertiesLoader.getProperty("switch."+i+".ip"));
				sbean.setAddress(propertiesLoader.getProperty("switch."+i+".address"));
				sbean.setUsername(propertiesLoader.getProperty("switch."+i+".username"));
				sbean.setPassword(propertiesLoader.getProperty("switch."+i+".password"));
				sbean.setPrompt(propertiesLoader.getProperty("switch."+i+".prompt"));
				switches.put(dpid, sbean);
			}
		}
		
		JSONArray arr = new JSONArray();
		if(findBean!=null&&findBean.getS_ip()!=null&&!findBean.getS_ip().trim().equals("")){
			String urlbase = "http://"+findBean.getS_ip()+":"+findBean.getS_port();
			request.getSession().setAttribute("urlbase", urlbase);
			arr = flowInfoService.switches(urlbase);
		}else{
			String urlbase = "http://"+propertiesLoader.getProperty("controller.ip")+":"+propertiesLoader.getProperty("controller.port");
			request.getSession().setAttribute("urlbase", urlbase);
			arr = flowInfoService.switches(urlbase);
		}
		
		List<SwitchBean> list = new ArrayList<SwitchBean>();
		for(int i=0;i<arr.size();i++){
			String dpid = arr.getString(i);
			if(switches.containsKey(dpid)){
				list.add(switches.get(dpid));
			}else{
				SwitchBean sbean = new SwitchBean();
				sbean.setDpid(dpid);
				list.add(sbean);
			}
		}
		
		if(findBean==null)
		  findBean = new FlowBean();
		
        model.addAttribute("list", list);
        model.addAttribute("findBean", findBean);
		return "modules/restweb/super/searchList";
	}
	
	@RequiresPermissions("rest:flowlist:list")
	@RequestMapping(value = {"execlist", ""})
	public String execlist(FlowBean findBean,HttpServletRequest request, HttpServletResponse response, Model model) {
		
		PropertiesLoader propertiesLoader = new PropertiesLoader("cloudroom.properties");
		
		switches.clear();
		
		userlogin = propertiesLoader.getProperty("switch.default.userlogin");
		passlogin = propertiesLoader.getProperty("switch.default.passlogin");
		prompt = propertiesLoader.getProperty("switch.default.prompt");
		
		String total = propertiesLoader.getProperty("switch.total");
		if(total!=null&&!total.equals("0")){
			int tal = Integer.valueOf(total);
			for(int i=1;i<=tal;i++){
				String dpid = propertiesLoader.getProperty("switch."+i+".dpid");
				SwitchBean sbean = new SwitchBean();
				sbean.setSequence(String.valueOf(i));
				sbean.setDpid(dpid);
				sbean.setIp(propertiesLoader.getProperty("switch."+i+".ip"));
				sbean.setAddress(propertiesLoader.getProperty("switch."+i+".address"));
				sbean.setUsername(propertiesLoader.getProperty("switch."+i+".username"));
				sbean.setPassword(propertiesLoader.getProperty("switch."+i+".password"));
				sbean.setPrompt(propertiesLoader.getProperty("switch."+i+".prompt"));
				switches.put(dpid, sbean);
			}
		}
		
		JSONArray arr = new JSONArray();
		if(findBean!=null&&findBean.getS_ip()!=null&&!findBean.getS_ip().trim().equals("")){
			String urlbase = "http://"+findBean.getS_ip()+":"+findBean.getS_port();
			request.getSession().setAttribute("urlbase", urlbase);
			arr = flowInfoService.switches(urlbase);
		}else{
			String urlbase = "http://"+propertiesLoader.getProperty("controller.ip")+":"+propertiesLoader.getProperty("controller.port");
			request.getSession().setAttribute("urlbase", urlbase);
			arr = flowInfoService.switches(urlbase);
		}
		
		List<SwitchBean> list = new ArrayList<SwitchBean>();
		for(int i=0;i<arr.size();i++){
			String dpid = arr.getString(i);
			if(switches.containsKey(dpid)){
				list.add(switches.get(dpid));
			}else{
				SwitchBean sbean = new SwitchBean();
				sbean.setDpid(dpid);
				list.add(sbean);
			}
		}
		
		if(findBean==null)
		  findBean = new FlowBean();
		
        model.addAttribute("list", list);
        model.addAttribute("findBean", findBean);
		return "modules/restweb/super/execList";
	}
	
	@RequiresPermissions("rest:flowlist:list")
	@RequestMapping(value = {"flowlist", ""})
	public String flowlist(FlowBean findBean,HttpServletRequest request, HttpServletResponse response, Model model) {
		
		PropertiesLoader propertiesLoader = new PropertiesLoader("cloudroom.properties");
		
		switches.clear();
		
		userlogin = propertiesLoader.getProperty("switch.default.userlogin");
		passlogin = propertiesLoader.getProperty("switch.default.passlogin");
		prompt = propertiesLoader.getProperty("switch.default.prompt");
		
		String total = propertiesLoader.getProperty("switch.total");
		if(total!=null&&!total.equals("0")){
			int tal = Integer.valueOf(total);
			for(int i=1;i<=tal;i++){
				String dpid = propertiesLoader.getProperty("switch."+i+".dpid");
				SwitchBean sbean = new SwitchBean();
				sbean.setSequence(String.valueOf(i));
				sbean.setDpid(dpid);
				sbean.setIp(propertiesLoader.getProperty("switch."+i+".ip"));
				sbean.setAddress(propertiesLoader.getProperty("switch."+i+".address"));
				sbean.setUsername(propertiesLoader.getProperty("switch."+i+".username"));
				sbean.setPassword(propertiesLoader.getProperty("switch."+i+".password"));
				sbean.setPrompt(propertiesLoader.getProperty("switch."+i+".prompt"));
				switches.put(dpid, sbean);
			}
		}
		
		JSONArray arr = new JSONArray();
		if(findBean!=null&&findBean.getS_ip()!=null&&!findBean.getS_ip().trim().equals("")){
			String urlbase = "http://"+findBean.getS_ip()+":"+findBean.getS_port();
			request.getSession().setAttribute("urlbase", urlbase);
			arr = flowInfoService.switches(urlbase);
		}else{
			String urlbase = "http://"+propertiesLoader.getProperty("controller.ip")+":"+propertiesLoader.getProperty("controller.port");
			request.getSession().setAttribute("urlbase", urlbase);
			arr = flowInfoService.switches(urlbase);
		}
		
		List<SwitchBean> list = new ArrayList<SwitchBean>();
		for(int i=0;i<arr.size();i++){
			String dpid = arr.getString(i);
			if(switches.containsKey(dpid)){
				list.add(switches.get(dpid));
			}else{
				SwitchBean sbean = new SwitchBean();
				sbean.setDpid(dpid);
				list.add(sbean);
			}
		}
		
		if(findBean==null)
		  findBean = new FlowBean();
		
        model.addAttribute("list", list);
        model.addAttribute("findBean", findBean);
		return "modules/restweb/super/flowList";
	}
	
	@RequiresPermissions("rest:flowlist:list")
	@RequestMapping(value = {"list", ""})
	public String list(String dpid,HttpServletRequest request, HttpServletResponse response, Model model) {
		String urlbase = (String) request.getSession().getAttribute("urlbase");
		JSONArray arr = flowInfoService.list(urlbase,dpid);
		FlowBean findBean = new FlowBean();
		findBean.setDpid(dpid);
		FlowBean addBean = new FlowBean();
		addBean.setDpid(dpid);
        model.addAttribute("arr", arr);
        model.addAttribute("findBean", findBean);
        model.addAttribute("addBean", addBean);
		return "modules/restweb/flowList";
	}

	@RequiresPermissions("rest:flowlist:list")
	@RequestMapping(value = "add")
	public String add(HttpServletRequest request,FlowBean addBean, Model model) {
		String dpid = addBean.getDpid();
		JSONObject fto = new JSONObject();
		JSONObject match = new JSONObject();
		JSONArray actions = new JSONArray();
		if(addBean.getIn_port()!=null&&!addBean.getIn_port().trim().equals(""))
		    match.put("in_port", addBean.getIn_port());
		if(addBean.getDl_src()!=null&&!addBean.getDl_src().trim().equals(""))
		    match.put("dl_src", addBean.getDl_src());
		if(addBean.getDl_dst()!=null&&!addBean.getDl_dst().trim().equals(""))
		    match.put("dl_dst", addBean.getDl_dst());
		if(addBean.getDl_vlan()!=null&&!addBean.getDl_vlan().trim().equals(""))
		    match.put("dl_vlan", addBean.getDl_vlan());
		if(addBean.getNw_src()!=null&&!addBean.getNw_src().trim().equals(""))
		    match.put("nw_src", addBean.getNw_src());
		if(addBean.getNw_dst()!=null&&!addBean.getNw_dst().trim().equals(""))
		    match.put("nw_dst", addBean.getNw_dst());
		fto.put("match", match);
		
		fto.put("dpid", dpid);
		//fto.put("table_id", "0");
		
		if(addBean.getOUTPUT()!=null&&!addBean.getOUTPUT().trim().equals(""))
		    fto.put("out_port", addBean.getOUTPUT());
		if(addBean.getHard_timeout()!=null&&!addBean.getHard_timeout().trim().equals(""))
		    fto.put("hard_timeout", addBean.getHard_timeout());
		if(addBean.getIdle_timeout()!=null&&!addBean.getIdle_timeout().trim().equals(""))
		    fto.put("idle_timeout", addBean.getIdle_timeout());
		if(addBean.getPriority()!=null&&!addBean.getPriority().trim().equals(""))
		    fto.put("priority", addBean.getPriority());
		
		if(addBean.getOUTPUT()!=null&&!addBean.getOUTPUT().trim().equals("")){
			JSONObject ofto = new JSONObject();
			ofto.put("type", "OUTPUT");
			ofto.put("port", addBean.getOUTPUT());
			actions.add(ofto);
		}
		if(addBean.getSET_DL_SRC()!=null&&!addBean.getSET_DL_SRC().trim().equals("")){
			JSONObject ofto = new JSONObject();
			ofto.put("type", "SET_DL_SRC");
			ofto.put("dl_src", addBean.getSET_DL_SRC());
			actions.add(ofto);
		}
		if(addBean.getSET_DL_DST()!=null&&!addBean.getSET_DL_DST().trim().equals("")){
			JSONObject ofto = new JSONObject();
			ofto.put("type", "SET_DL_DST");
			ofto.put("dl_dst", addBean.getSET_DL_DST());
			actions.add(ofto);
		}
		if(addBean.getSET_VLAN_VID()!=null&&!addBean.getSET_VLAN_VID().trim().equals("")){
			JSONObject ofto = new JSONObject();
			ofto.put("type", "SET_VLAN_VID");
			ofto.put("vlan_vid", addBean.getSET_VLAN_VID());
			actions.add(ofto);
		}
		if(addBean.getSET_NW_SRC()!=null&&!addBean.getSET_NW_SRC().trim().equals("")){
			JSONObject ofto = new JSONObject();
			ofto.put("type", "SET_NW_SRC");
			ofto.put("nw_src", addBean.getSET_NW_SRC());
			actions.add(ofto);
		}
		if(addBean.getSET_NW_DST()!=null&&!addBean.getSET_NW_DST().trim().equals("")){
			JSONObject ofto = new JSONObject();
			ofto.put("type", "SET_NW_DST");
			ofto.put("nw_dst", addBean.getSET_NW_DST());
			actions.add(ofto);
		}
		fto.put("actions", actions);
		
		String urlbase = (String) request.getSession().getAttribute("urlbase");
		flowInfoService.add(fto,urlbase);
		
		JSONArray arr = flowInfoService.list(urlbase,dpid);
		FlowBean findBean = new FlowBean();
		findBean.setDpid(dpid);
		addBean = new FlowBean();
		addBean.setDpid(dpid);
        model.addAttribute("arr", arr);
        model.addAttribute("findBean", findBean);
        model.addAttribute("addBean", addBean);
		return "modules/restweb/flowList";
	}

	@RequiresPermissions("rest:flowlist:list")
	@RequestMapping(value = "saveCmd")
	public String saveCmd(HttpServletRequest request,FlowBean findBean, Model model, RedirectAttributes redirectAttributes) {
		String dpid = findBean.getDpid();
		
		String cmd = findBean.getMatch();
		if(cmd==null||cmd.trim().equals("")){
			addMessage(redirectAttributes, "不能保存空命令！");
			return "modules/restweb/super/searchForm";
		}
		
		if(!cmd.trim().startsWith("show")){
			cmd = "show "+cmd;
		}
		
		User ur = UserUtils.getUser();
		if (!ur.isAdmin()){
		}
		SwitchBean sbean = switches.get(dpid);
		String ip = sbean.getIp();
		int port = 23;
		String user = sbean.getUsername();
		String password = sbean.getPassword();
		String prompt = sbean.getPrompt();
		if(prompt!=null&&!prompt.equals("")){
			
		}else{
			prompt = this.prompt;
		}
		
		NetTelnet telnet = new NetTelnet(ip, port,userlogin,passlogin,prompt, user, password);
		telnet.sendCommand("\n");
		String r1 = telnet.sendCommand(cmd);
		System.out.println("显示结果");
		System.out.println(r1);
		telnet.disconnect();

		findBean = new FlowBean();
		findBean.setDpid(dpid);
		findBean.setMatch(cmd);
		findBean.setActions(r1);
		
        model.addAttribute("findBean", findBean);
		return "modules/restweb/super/searchForm";
	}

	@RequiresPermissions("rest:flowlist:list")
	@RequestMapping(value = "show")
	public String show(HttpServletRequest request,FlowBean findBean, Model model) {
		String dpid = findBean.getDpid();
		
		String cmd = findBean.getMatch();
		if(cmd==null||cmd.trim().equals("")){
			cmd = "show running-config\n";
		}
		
		if(!cmd.trim().startsWith("show")){
			cmd = "show "+cmd;
		}
		
		SwitchBean sbean = switches.get(dpid);
		String ip = sbean.getIp();
		int port = 23;
		String user = sbean.getUsername();
		String password = sbean.getPassword();
		String prompt = sbean.getPrompt();
		if(prompt!=null&&!prompt.equals("")){
			
		}else{
			prompt = this.prompt;
		}
		
		NetTelnet telnet = new NetTelnet(ip, port,userlogin,passlogin,prompt, user, password);
		telnet.sendCommand("\n");
		String r1 = telnet.sendCommand(cmd);
		System.out.println("显示结果");
		System.out.println(r1);
		telnet.disconnect();

		findBean = new FlowBean();
		findBean.setDpid(dpid);
		findBean.setMatch(cmd);
		findBean.setActions(r1);
		
        model.addAttribute("findBean", findBean);
		return "modules/restweb/super/searchForm";
	}

	@RequiresPermissions("rest:flowlist:list")
	@RequestMapping(value = "exec")
	public String exec(HttpServletRequest request,FlowBean findBean, Model model) {
		String dpid = findBean.getDpid();
		
		String cmd = findBean.getMatch();
		if(cmd==null||cmd.trim().equals("")){
			cmd = "show running-config\n";
		}
		
		if(cmd.trim().startsWith("ovs")){
			cmd = "show running-config\n";
		}
		
		SwitchBean sbean = switches.get(dpid);
		String ip = sbean.getIp();
		int port = 23;
		String user = sbean.getUsername();
		String password = sbean.getPassword();
		String prompt = sbean.getPrompt();
		if(prompt!=null&&!prompt.equals("")){
			
		}else{
			prompt = this.prompt;
		}
		
		NetTelnet telnet = new NetTelnet(ip, port,userlogin,passlogin,prompt, user, password);
		telnet.sendCommand("\n");
		String r1 = telnet.sendCommand(cmd);
		System.out.println("显示结果");
		System.out.println(r1);
		telnet.disconnect();

		findBean = new FlowBean();
		findBean.setDpid(dpid);
		findBean.setMatch(cmd);
		findBean.setActions(r1);
		
        model.addAttribute("findBean", findBean);
		return "modules/restweb/super/execForm";
	}

	@RequiresPermissions("rest:flowlist:list")
	@RequestMapping(value = "flow")
	public String flow(HttpServletRequest request,FlowBean findBean, Model model) {
		String dpid = findBean.getDpid();
		
		String cmd = findBean.getMatch();
		if(cmd==null||cmd.trim().equals("")){
			cmd = "show running-config\n";
		}
		
		SwitchBean sbean = switches.get(dpid);
		String ip = sbean.getIp();
		int port = 23;
		String user = sbean.getUsername();
		String password = sbean.getPassword();
		String prompt = sbean.getPrompt();
		if(prompt!=null&&!prompt.equals("")){
			
		}else{
			prompt = this.prompt;
		}
		
		NetTelnet telnet = new NetTelnet(ip, port,userlogin,passlogin,prompt, user, password);
		telnet.sendCommand("\n");
		String r1 = telnet.sendCommand(cmd);
		System.out.println("显示结果");
		System.out.println(r1);
		telnet.disconnect();

		findBean = new FlowBean();
		findBean.setDpid(dpid);
		findBean.setMatch(cmd);
		findBean.setActions(r1);
		
        model.addAttribute("findBean", findBean);
		return "modules/restweb/super/flowForm";
	}

	@RequiresPermissions("rest:flowlist:list")
	@RequestMapping(value = "switchsave", method=RequestMethod.POST)
	public String switchsave(HttpServletRequest request,FlowBean addBean, Model model, RedirectAttributes redirectAttributes) {
		String dpid = addBean.getDpid();
		String urlbase = (String) request.getSession().getAttribute("urlbase");
		
		SwitchBean sbean = switches.get(dpid);
		String ip = sbean.getIp();
		int port = 23;
		String user = sbean.getUsername();
		String password = sbean.getPassword();
		String prompt = sbean.getPrompt();
		if(prompt!=null&&!prompt.equals("")){
			
		}else{
			prompt = this.prompt;
		}
		
		NetTelnet telnet = new NetTelnet(ip, port,userlogin,passlogin,prompt, user, password);
		telnet.sendCommand("\n");
		String r1 = telnet.sendCommand("ovs-ofctl write-startup-flows br0 -O openflow13");
		System.out.println("显示结果");
		System.out.println(r1);
		telnet.disconnect();

		JSONArray arr = flowInfoService.list(urlbase,dpid);
		FlowBean findBean = new FlowBean();
		findBean.setDpid(dpid);
		addBean = new FlowBean();
		addBean.setDpid(dpid);
		
		addMessage(redirectAttributes, "保存交换机配置成功！");
        model.addAttribute("arr", arr);
        model.addAttribute("findBean", findBean);
        model.addAttribute("addBean", addBean);
		return "modules/restweb/flowList";
	}
	
	@RequiresPermissions("rest:flowlist:list")
	@RequestMapping(value = "delete")
	public String delete(HttpServletRequest request,String dpid,String sequence, RedirectAttributes redirectAttributes, Model model) {
		String urlbase = (String) request.getSession().getAttribute("urlbase");
		JSONArray arr0 = flowInfoService.list(urlbase,dpid);
		
		JSONObject fto = arr0.optJSONObject(Integer.valueOf(sequence));
		
		JSONArray actions = fto.getJSONArray("actions");
		JSONArray actions1 = new JSONArray();
		for(int i=0; i<actions.size();i++){
			String action = actions.getString(i);
			if(!action.equals("")&&action.startsWith("OUTPUT")){
			JSONObject ofto = new JSONObject();
			ofto.put("type", "OUTPUT");
			ofto.put("port", action.substring("OUTPUT".length()+1));
			actions1.add(ofto);
		    }
			if(!action.equals("")&&action.startsWith("SET_DL_SRC")){
			JSONObject ofto = new JSONObject();
			ofto.put("type", "SET_DL_SRC");
			ofto.put("dl_src", action.substring("SET_DL_SRC".length()+1));
			actions1.add(ofto);
		    }
			if(!action.equals("")&&action.startsWith("SET_DL_DST")){
			JSONObject ofto = new JSONObject();
			ofto.put("type", "SET_DL_DST");
			ofto.put("dl_dst", action.substring("SET_DL_DST".length()+1));
			actions1.add(ofto);
		    }
			if(!action.equals("")&&action.startsWith("SET_VLAN_VID")){
			JSONObject ofto = new JSONObject();
			ofto.put("type", "SET_VLAN_VID");
			ofto.put("vlan_vid", action.substring("SET_VLAN_VID".length()+1));
			actions1.add(ofto);
		    }
			if(!action.equals("")&&action.startsWith("SET_NW_SRC")){
			JSONObject ofto = new JSONObject();
			ofto.put("type", "SET_NW_SRC");
			ofto.put("nw_src", action.substring("SET_NW_SRC".length()+1));
			actions1.add(ofto);
		    }
			if(!action.equals("")&&action.startsWith("SET_NW_DST")){
			JSONObject ofto = new JSONObject();
			ofto.put("type", "SET_NW_DST");
			ofto.put("nw_dst", action.substring("SET_NW_DST".length()+1));
			actions1.add(ofto);
		    }
		}
		fto.put("actions", actions1);
		fto.put("dpid", dpid);
		
		flowInfoService.delete(fto,urlbase);
		
		addMessage(redirectAttributes, "删除流项信息成功");
		
		JSONArray arr = flowInfoService.list(urlbase,dpid);
		FlowBean findBean = new FlowBean();
		findBean.setDpid(dpid);
		FlowBean addBean = new FlowBean();
		addBean.setDpid(dpid);
       model.addAttribute("arr", arr);
        model.addAttribute("findBean", findBean);
        model.addAttribute("addBean", addBean);
		return "modules/restweb/flowList";
	}

	@ResponseBody
	@RequestMapping(value = "checkProjectID")
	public String checkProjectID(String oldProjectId, String prjCode) {
		if (prjCode !=null && prjCode.equals(oldProjectId)) {
			return "true";
		} else if (prjCode !=null ) {
			return "true";
		}
		return "false";
	}

}
