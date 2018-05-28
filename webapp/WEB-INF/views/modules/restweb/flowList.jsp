<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>SDN交换机管理</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/dialog.jsp" %>
	<style type="text/css">.sort{color:#0663A2;cursor:pointer;}</style>
    <script type="text/javascript" src="/static/json2/json2.min.js"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			// 表格排序
			var orderBy = $("#orderBy").val().split(" ");
			$("#contentTable th.sort").each(function(){
				if ($(this).hasClass(orderBy[0])){
					orderBy[1] = orderBy[1]&&orderBy[1].toUpperCase()=="DESC"?"down":"up";
					$(this).html($(this).html()+" <i class=\"icon icon-arrow-"+orderBy[1]+"\"></i>");
				}
			});
			$("#contentTable th.sort").click(function(){
				var order = $(this).attr("class").split(" ");
				var sort = $("#orderBy").val().split(" ");
				for(var i=0; i<order.length; i++){
					if (order[i] == "sort"){order = order[i+1]; break;}
				}
				if (order == sort[0]){
					sort = (sort[1]&&sort[1].toUpperCase()=="DESC"?"ASC":"DESC");
					$("#orderBy").val(order+" DESC"!=order+" "+sort?"":order+" "+sort);
				}else{
					$("#orderBy").val(order+" ASC");
				}
				page();
			});
			$("#addSubmit").click(function(){
				top.$.jBox.confirm("确认要增加流表数据吗？","系统提示",function(v,h,f){
					if(v=="ok"){
						$("#addForm").attr("action","${ctx}/rest/add");
						$("#addForm").submit();
					}
				},{buttonsFocus:1});
				top.$('.jbox-body .jbox-icon').css('top','55px');
			});
			$("#saveSubmit").click(function(){
				top.$.jBox.confirm("确认要在交换机上保存所有配置吗？","系统提示",function(v,h,f){
					if(v=="ok"){
						$("#addForm").attr("action","${ctx}/rest/switchsave");
						$("#addForm").submit();
					}
				},{buttonsFocus:1});
				top.$('.jbox-body .jbox-icon').css('top','55px');
			});
			$("#findSubmit").click(function(){
						$("#searchForm").attr("action","${ctx}/rest/find");
						$("#searchForm").submit();
			});
			$("#btnImport").click(function(){
				$.jBox($("#importBox").html(), {title:"导入数据", buttons:{"关闭":true}, 
					bottomText:"导入文件不能超过5M，仅允许导入“xls”或“xlsx”格式文件！"});
			});
		});
		
		function saveSubmit(){
			top.$.jBox.confirm("确认要在交换机上保存所有配置吗？","系统提示",function(v,h,f){
				if(v=="ok"){
					$("#addForm").attr("action","${ctx}/rest/switchsave");
					$("#addForm").submit();
				}
			},{buttonsFocus:1});
			top.$('.jbox-body .jbox-icon').css('top','55px');
	    	return true;
	    }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active">流状态列表</li>
	</ul>
	<form:form id="addForm" modelAttribute="addBean" action="${ctx}/rest/add" method="post" class="breadcrumb form-search">
		<input id="dpid" name="dpid" type="hidden" value="${addBean.dpid}"/>
		<div style="margin-top:8px;"><label>匹配项：</label>
			<label>入端口号：</label><form:input path="in_port" htmlEscape="false" maxlength="50" class="span2" type="number"/>
			<label>MAC源地址：</label><form:input path="dl_src" htmlEscape="false" maxlength="50" class="span3" pattern="[A-Fa-f0-9]{2}:[A-Fa-f0-9]{2}:[A-Fa-f0-9]{2}:[A-Fa-f0-9]{2}:[A-Fa-f0-9]{2}:[A-Fa-f0-9]{2}" placeholder="xx:xx:xx:xx:xx:xx"/>
			<label>MAC宿地址：</label><form:input path="dl_dst" htmlEscape="false" maxlength="50" class="span3" pattern="[A-Fa-f0-9]{2}:[A-Fa-f0-9]{2}:[A-Fa-f0-9]{2}:[A-Fa-f0-9]{2}:[A-Fa-f0-9]{2}:[A-Fa-f0-9]{2}" placeholder="xx:xx:xx:xx:xx:xx"/>
		</div>
		<div style="margin-top:8px;"><label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label>
			<label> &nbsp;VLAN号：</label><form:input path="dl_vlan" htmlEscape="false" maxlength="50" class="span2" type="number"/>
			<label>&nbsp;&nbsp;&nbsp;&nbsp;IP源地址：</label><form:input path="nw_src" htmlEscape="false" maxlength="50" class="span3" pattern="\d{1,3}.\d{1,3}.\d{1,3}.\d{1,3}" placeholder="xxx.xxx.xxx.xxx"/>
			<label>&nbsp;&nbsp;&nbsp;&nbsp;IP宿地址：</label><form:input path="nw_dst" htmlEscape="false" maxlength="50" class="span3" pattern="\d{1,3}.\d{1,3}.\d{1,3}.\d{1,3}" placeholder="xxx.xxx.xxx.xxx"/>
		</div>
		<div style="margin-top:8px;"><label>动作项：</label>
			<label>出端口号：</label><form:input path="OUTPUT" htmlEscape="false" maxlength="50" class="span2" type="number"/>
			<label>设置MAC源地址：</label><form:input path="SET_DL_SRC" htmlEscape="false" maxlength="50" class="span3" pattern="[A-Fa-f0-9]{2}:[A-Fa-f0-9]{2}:[A-Fa-f0-9]{2}:[A-Fa-f0-9]{2}:[A-Fa-f0-9]{2}:[A-Fa-f0-9]{2}" placeholder="xx:xx:xx:xx:xx:xx"/>
			<label>设置MAC宿地址：</label><form:input path="SET_DL_DST" htmlEscape="false" maxlength="50" class="span3" pattern="[A-Fa-f0-9]{2}:[A-Fa-f0-9]{2}:[A-Fa-f0-9]{2}:[A-Fa-f0-9]{2}:[A-Fa-f0-9]{2}:[A-Fa-f0-9]{2}" placeholder="xx:xx:xx:xx:xx:xx"/>
		</div>
		<div style="margin-top:8px;"><label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label>
			<label>设置VLAN号：</label><form:input path="SET_VLAN_VID" htmlEscape="false" maxlength="50" class="span2" type="number"/>
			<label>设置IP源地址：</label><form:input path="SET_NW_SRC" htmlEscape="false" maxlength="50" class="span3" pattern="\d{1,3}.\d{1,3}.\d{1,3}.\d{1,3}" placeholder="xxx.xxx.xxx.xxx"/>
			<label>设置IP宿地址：</label><form:input path="SET_NW_DST" htmlEscape="false" maxlength="50" class="span3" pattern="\d{1,3}.\d{1,3}.\d{1,3}.\d{1,3}" placeholder="xxx.xxx.xxx.xxx"/>
		</div>
		<div style="margin-top:8px;"><label>其他：</label>
			<label>空闲超时秒：</label><form:input path="idle_timeout" htmlEscape="false" maxlength="50" class="span2" type="number"/>
			<label>总体超时秒：</label><form:input path="hard_timeout" htmlEscape="false" maxlength="50" class="span3" type="number"/>
			<label>优先级：</label><form:input path="priority" htmlEscape="false" maxlength="50" class="span3" type="number"/>
			&nbsp;&nbsp;<input id="addSubmit" class="btn btn-primary" type="submit" value="增加"/>
		</div>
	</form:form>
	<form:form id="searchForm" modelAttribute="findBean" action="${ctx}/rest/find" method="post" class="breadcrumb form-search">
		<input id="dpid" name="dpid" type="hidden" value="${findBean.dpid}"/>
		<div style="margin-top:8px;"><label>匹配项：</label>
			<label>入端口号：</label><form:input path="in_port" htmlEscape="false" maxlength="50" class="span2" type="number"/>
			<label>MAC源地址：</label><form:input path="dl_src" htmlEscape="false" maxlength="50" class="span3" pattern="[A-Fa-f0-9]{2}:[A-Fa-f0-9]{2}:[A-Fa-f0-9]{2}:[A-Fa-f0-9]{2}:[A-Fa-f0-9]{2}:[A-Fa-f0-9]{2}" placeholder="xx:xx:xx:xx:xx:xx"/>
			<label>MAC宿地址：</label><form:input path="dl_dst" htmlEscape="false" maxlength="50" class="span3" pattern="[A-Fa-f0-9]{2}:[A-Fa-f0-9]{2}:[A-Fa-f0-9]{2}:[A-Fa-f0-9]{2}:[A-Fa-f0-9]{2}:[A-Fa-f0-9]{2}" placeholder="xx:xx:xx:xx:xx:xx"/>
		</div>
		<div style="margin-top:8px;"><label>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label>
			<label>&nbsp;VLAN号：</label><form:input path="dl_vlan" htmlEscape="false" maxlength="50" class="span2" type="number"/>
			<label>&nbsp;&nbsp;&nbsp;&nbsp;IP源地址：</label><form:input path="nw_src" htmlEscape="false" maxlength="50" class="span3" pattern="\d{1,3}.\d{1,3}.\d{1,3}.\d{1,3}" placeholder="xxx.xxx.xxx.xxx"/>
			<label>&nbsp;&nbsp;&nbsp;&nbsp;IP宿地址：</label><form:input path="nw_dst" htmlEscape="false" maxlength="50" class="span3" pattern="\d{1,3}.\d{1,3}.\d{1,3}.\d{1,3}" placeholder="xxx.xxx.xxx.xxx"/>
		</div>
		<div style="margin-top:8px;"><label>动作项：</label>
			<label>出端口号：</label><form:input path="OUTPUT" htmlEscape="false" maxlength="50" class="span2" type="number"/>
			&nbsp;&nbsp;<input id="findSubmit" class="btn btn-primary" type="submit" value="查询"/>
			<label>交换机dpid为：</label>${findBean.dpid}
		</div>
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed" style="table-layout:fixed;word-break: break-all; word-wrap: break-word;">
		<thead><tr><th>match</th><th>actions</th><th>idle_timeout</th><th>cookie</th><th>packet_count</th><th>hard_timeout</th><th>byte_count</th><th>length</th><th>duration_nsec</th><th>priority</th><th>duration_sec</th><th>table_id</th><th>flags</th><th>操作</th></tr></thead>
		<thead><tr><th>匹配项</th><th>动作</th><th>空闲超时</th><th>控制器项</th><th>包数量</th><th>固定超时</th><th>字节数</th><th>长度</th><th>存续纳秒</th><th>优先级</th><th>存续秒</th><th>表ID</th><th>端口失效标志</th><th>操作</th></tr></thead>
		<tbody>
		<c:forEach items="${arr}" var="flowInfo" varStatus="status">
			<tr>
				<td>${flowInfo.match}</td>
				<td>${flowInfo.actions}</td>
				<td>${flowInfo.idle_timeout}</td>
				<td>${flowInfo.cookie}</td>
				<td>${flowInfo.packet_count}</td>
				<td>${flowInfo.hard_timeout}</td>
				<td>${flowInfo.byte_count}</td>
				<td>${flowInfo.length}</td>
				<td>${flowInfo.duration_nsec}</td>
				<td>${flowInfo.priority}</td>
				<td>${flowInfo.duration_sec}</td>
				<td>${flowInfo.table_id}</td>
				<td>${flowInfo.flags}</td>
				<td>
					<a href="${ctx}/rest/delete?dpid=${findBean.dpid}&sequence=${status.index}" onclick="return confirmx('确认要删除该流项信息吗？', this.href)">删除</a>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
		<div class="form-actions">
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
			&nbsp;&nbsp;<input id="saveSubmit" class="btn btn-primary" type="button" onclick="saveSubmit()" value="在交换机上保存配置"/>
		</div>
</body>
</html>