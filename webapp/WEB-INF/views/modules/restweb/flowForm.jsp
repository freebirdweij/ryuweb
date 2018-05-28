<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>项目录入</title>
	<%@include file="/WEB-INF/views/include/dialog.jsp" %>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
    jQuery(function(){        
        jQuery.validator.methods.compareDate = function(value, element, param) {
            //var startDate = jQuery(param).val() + ":00";补全yyyy-MM-dd HH:mm:ss格式
            //value = value + ":00";
            
            var startDate = jQuery(param).val();
            
            var date1 = new Date(Date.parse(startDate.replace("-", "/")));
            var date2 = new Date(Date.parse(value.replace("-", "/")));
            return date1 < date2;
        };
        
        jQuery("#inputForm").validate({
            focusInvalid:false,
            rules:{
                "prjBegin":{
                    required: true
                },
                "prjEnd": {
                    required: true,
                    compareDate: "#prjBegin"
                }
            },
            messages:{
                "prjBegin":{
                    required: "开始时间不能为空"
                },
                "prjEnd":{
                    required: "结束时间不能为空",
                    compareDate: "结束日期必须大于开始日期!"
                }
            }
        });
    });
		$(document).ready(function() {
			$("#loginName").focus();
			$("#inputForm").validate({
				rules: {
					loginName: {remote: "${ctx}/sys/checkLoginName?oldLoginName=" + encodeURIComponent('${user.loginName}')}
				},
				messages: {
					loginName: {remote: "用户登录名已存在"},
					confirmNewPassword: {equalTo: "输入与上面相同的密码"}
				},
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
		});
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active">项目信息</li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="projectInfo" action="${ctx}/project/save" method="post" class="form-horizontal">
		<tags:message content="${message}"/>
		<div class="control-group">
			<label class="control-label">项目编号:</label>
			<div class="controls">
						<form:input path="id" htmlEscape="false" maxlength="50"
							class="span3 required" readonly="true"/>
            </div>
		</div>
		<div class="control-group">
			<label class="control-label">项目名称:</label>
			<div class="controls">
						<form:input path="prjName" htmlEscape="false" maxlength="20"
							class="span4 required" />
          </div>	
		</div>
		<div class="control-group">
			<label class="control-label">项目类别:</label>
			<div class="controls">
				<form:select path="prjType" class="span2">
					<form:option value="" label="请选择"/>
					<form:options items="${fns:getDictList('sys_prjtype_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
            </div>
		</div>
		<div class="control-group">
			<label class="control-label">建设单位:</label>
			<div class="controls">
                <tags:treeselect id="unit" name="unit.id" value="${projectInfo.unit.id}" labelName="unit.name" labelValue="${projectInfo.unit.name}"
					title="公司" url="/sys/office/treeData?type=1" cssClass="required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">投资金额:</label>
			<div class="controls">
						<form:input path="prjMoney" htmlEscape="false" maxlength="20"
							class="span3 required number"/>
			</div>
		</div>
		<%-- <div class="control-group">
			<label class="control-label">项目级别:</label>
			<div class="controls">
				<form:select path="prjLevel" class="span2">
					<form:option value="" label="请选择"/>
					<form:options items="${fns:getDictList('sys_prjlevel_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
		</div> --%>
		<div class="control-group">
			<label class="control-label">项目年度:</label>
			<div class="controls">
				<form:input path="prjYear" maxlength="20"
						class="span2 input-small Wdate" value="2014" onclick="WdatePicker({dateFmt:'yyyy',isShowClear:false});" onpropertychange="checkBeginDate()"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">项目说明:</label>
			<div class="controls">
					<form:textarea path="prjNotes" rows="6" cols="30" htmlEscape="false" maxlength="100" class="span4 required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">项目状态:</label>
			<div class="controls">
				<form:select path="prjStatus" class="span2" disabled="true">
					<form:option value="" label="请选择"/>
					<form:options items="${fns:getDictList('sys_prjstatus_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</div>
		</div>
		<div class="form-actions">
			<%-- <a href="../../static/ckfinder/ckfinder.html?type=project&start=project:/${projectInfo.id}/">上传项目资料</a> --%>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="保存"/>&nbsp;
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>