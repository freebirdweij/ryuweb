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
			$("#btnSearch").click(function(){
				top.$.jBox.confirm("确认要在交换机上保存所有配置吗？","系统提示",function(v,h,f){
					if(v=="ok"){
						$("#inputForm").attr("action","${ctx}/rest/switchsave");
						$("#inputForm").submit();
					}
				},{buttonsFocus:1});
				top.$('.jbox-body .jbox-icon').css('top','55px');
			});
			$("#btnSearch").click(function(){
				$("#inputForm").attr("action","${ctx}/rest/super/show");
				$("#inputForm").submit();
	        });
			$("#btnSaveCmd").click(function(){
				$("#inputForm").attr("action","${ctx}/rest/super/saveCmd");
				$("#inputForm").submit();
            });
			$("#btnSelectCmd").click(function(){
				$("#inputForm").attr("action","${ctx}/rest/super/selectCmd");
				$("#inputForm").submit();
            });
			$("#btnSaveResult").click(function(){
				$("#inputForm").attr("action","${ctx}/rest/super/saveResult");
				$("#inputForm").submit();
            });
			$("#btnSelectResult").click(function(){
				$("#inputForm").attr("action","${ctx}/rest/super/selectResult");
				$("#inputForm").submit();
            });
		});
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active">查询交换机信息</li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="findBean" action="${ctx}/rest/super/show" method="post" class="form-horizontal">
		<input id="dpid" name="dpid" type="hidden" value="${findBean.dpid}"/>
		<tags:message content="${message}"/>
		<div class="control-group">
			<label class="control-label">查询命令:</label>
			<div class="controls">
					<form:textarea path="match" rows="30" cols="200" htmlEscape="false" maxlength="100" class="span4 required"/>
			</div>
		</div>
		<div class="form-actions">
			<input id="btnSearch" class="btn btn-primary" type="submit" value="查询"/>&nbsp;
			<input id="btnSaveCmd" class="btn btn-primary" type="submit" value="把命令保存起来"/>&nbsp;
			<input id="btnSelectCmd" class="btn btn-primary" type="submit" value="找回以前的命令"/>
		</div>
		<div class="control-group">
			<label class="control-label">返回结果:</label>
			<div class="controls">
				${findBean.actions}
			</div>
		</div>
		<div class="form-actions">
			<input id="btnSaveResult" class="btn btn-primary" type="submit" value="把结果保存起来"/>&nbsp;
			<input id="btnSelectResult" class="btn btn-primary" type="submit" value="查找以前的结果"/>
		</div>
	</form:form>
</body>
</html>