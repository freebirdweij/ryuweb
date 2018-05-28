<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>SDN交换机管理</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/dialog.jsp" %>
	<style type="text/css">.sort{color:#0663A2;cursor:pointer;}</style>
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
			$("#findSubmit").click(function(){
						$("#searchForm").attr("action","${ctx}/rest/switches");
						$("#searchForm").submit();
			});
			$("#btnImport").click(function(){
				$.jBox($("#importBox").html(), {title:"导入数据", buttons:{"关闭":true}, 
					bottomText:"导入文件不能超过5M，仅允许导入“xls”或“xlsx”格式文件！"});
			});
		});
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/rest/switches">流状态列表</a></li>
	</ul>
	<form:form id="searchForm" modelAttribute="findBean" action="${ctx}/rest/switches" method="post" class="breadcrumb form-search">
		<div style="margin-top:8px;">
			<label>控制器IP地址：</label><form:input path="s_ip" htmlEscape="false" maxlength="50" class="span2 required" pattern="\d{1,3}.\d{1,3}.\d{1,3}.\d{1,3}" placeholder="xxx.xxx.xxx.xxx"/>
			<label>端口号：</label><form:input path="s_port" htmlEscape="false" maxlength="50" class="span2" type="number"/>
			<input id="findSubmit" class="btn btn-primary" type="submit" value="查询"/>
		</div>
	</form:form>
	<tags:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed" style="table-layout:fixed;word-break: break-all; word-wrap: break-word;">
		<thead><tr><th>交换机</th><th>管理IP</th><th>位置</th></tr></thead>
		<tbody>
		<c:forEach items="${list}" var="sbean">
			<tr>
				<td><a href="${ctx}/rest/list?dpid=${sbean.dpid}" onclick="return confirmx('确认要进入管理页面吗？', this.href)">交换机dpid=${sbean.dpid}</a></td>
				<td>${sbean.ip}</td>
				<td>${sbean.address}</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
</body>
</html>