<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
String path = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
%>
<base href="<%=path%>">
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">

<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>
<link rel="stylesheet" type="text/css" href="jquery/bs_pagination-master/css/jquery.bs_pagination.min.css">
<script type="text/javascript" src="jquery/bs_pagination-master/js/jquery.bs_pagination.min.js"></script>
<script type="text/javascript" src="jquery/bs_pagination-master/localization/en.js"></script>
<script type="text/javascript">

	//创建市场活动
	$(function(){
		$("#closeActivityBtn").click(function () {
			$("#createActivityModal").modal("hide");
		});
		$("#createActivityBtn").click(function () {
			//初始化数据
			$("#createActivityFrom").get(0).reset();
			/*使用js控制模态窗口*/
			$("#createActivityModal").modal("show");
		});
		$("#saveActivityBtn").click(function () {
			//获取数据
			var owner = $.trim($("#create-marketActivityOwner").val());
			var name = $.trim($("#create-marketActivityName").val());
			var startDate = $("#create-startDate").val();
			var endDate = $("#create-endDate").val();
			var cost = $.trim($("#create-cost").val());
			var description= $.trim($("#create-description").val());

			if (owner == ""){
				alert("所有者不能为空");
				return
			}
			if(name == ""){
				alert("名称不能为空");
				return;
			}
			if (startDate != "" && endDate != ""){
				if (endDate < startDate){
					alert("结束时间不能比开始时间小");
					return;
				}
			}
			var regExp = /^(([1-9]\d*)|0)$/;
			if (!regExp.test(cost)){
				alert("成本只能为非负整数");
				return;
			}
			//发送请求
			$.ajax({
				url:"workbench/activity/insertActivity.do",
				data:{
					owner:owner,
					name:name,
					startDate:startDate,
					endDate:endDate,
					cost:cost,
					description:description
				},
				success:function (data) {
					//返回结果1表示成功创建
					if (data.code == "1"){
						//保存成功，关闭模态窗口
						$("#createActivityModal").modal("hide");
						//刷新市场活动，显示第一页数据，保持每页显示条数不变
						queryActivityByConditionForPage(1,$("#demo_page1").bs_pagination("getOption","rowsPerPage"));
					}else {
						//提示信息
						alert(data.message);
						//保存失败，不关闭模态窗口
						$("#createActivityModal").modal("show");
					}
				}
			})
		});

		$(".myDate").datetimepicker({
			language:"zh-CN",//语言
			format:"yyyy-mm-dd",//日期格式
			minView:"month",//可以选择的最小视图
			initialDate:new Date(),//初始化日期
			autoclose:true,//设置日期完成后，是否自动关闭
			todayBtn:true,//显示当前日期
			clearBtn:true//清空日期
		});
		//当页面加完成，显示数据
		queryActivityByConditionForPage(1,10);

		$("#queryBtn").click(function () {
			queryActivityByConditionForPage(1,$("#demo_page1").bs_pagination("getOption","rowsPerPage"));
		});

		$("#chckAll").click(function () {
			//如果按钮是全选状态，则列表中所有checked都选中
			$("#tBody input[type='checkbox']").prop("checked",this.checked);
		});

		/*$("#tBody input[type='checkbox']").click(function () {
			(this.checked.size()==this.checked("checkbox").size()).prop("checked",$("#chckAll").checked)
		});不用着用方式，要用下面这种方式*/
		$("#tBody").on("click","input[type='checkbox']",function () {
			if($("#tBody input[type='checkbox']").size()==$("#tBody input[type='checkbox']:checked").size()){
				$("#chckAll").prop("checked",true);
			}else {
				$("#chckAll").prop("checked",false);
			}
		})


			$("#deleteActivityBtn").click(function () {
				var checkId=$("#tBody input[type='checkbox']:checked");
				if (checkId.size()==0){
					alert("请选择要删除的市场活动");
					return
				}
				var id = "";
				$.each(checkId,function () {
					id+="id="+this.value+"&";
				})
				id = id.substr(0,id.length-1);
				if(window.confirm("您确定删除吗？")) {
					$.ajax({
						url: "workbench/activity/deleteActivityByIds.do",
						type: "post",
						data: id,
						dataType: "json",
						success: function (data) {
							if (data.code == "1") {
								queryActivityByConditionForPage(1, $("#demo_page1").bs_pagination("getOption", "rowsPerPage"));
							} else {
								alert(data.message);
							}
						}
					})
				}
			});

		//修改市场活动
		$("#editActivityBtn").click(function () {
			//获取选中的市场活动
			var checkId=$("#tBody input[type='checkbox']:checked");
			//判断是否选择了市场活动
			if (checkId.size()==0){
				alert("请选择需要更新的市场活动");
				return
			}
			if (checkId.size()>1){
				alert("每次最多选择一条市场活动");
				return;
			}
			//获取市场活动主键
			var id = checkId.val();
			$.ajax({
				url:"workbench/activity/queryActivityByID.do",
				type:"post",
				data:{
					id:id
				},
				dataType:"json",
				success:function (data) {
					$("#hiddenId").val(data.id);
					$("#edit-marketActivityOwner").val(data.owner);
					$("#edit-marketActivityName").val(data.name);
					$("#edit-startTime").val(data.startDate);
					$("#edit-endTime").val(data.endDate);
					$("#edit-cost").val(data.cost);
					$("#edit-describe").val(data.description);
					$("#editActivityModal").modal("show");

				}
			})
		});

		//更新市场活动
		$("#updateActivityBtn").click(function () {
			//收集参数
			var id=$("#hiddenId").val();
			var owner=$("#edit-marketActivityOwner").val();
			var name=$.trim($("#edit-marketActivityName").val());
			var startDate=$("#edit-startTime").val();
			var endDate=$("#edit-endTime").val();
			var cost=$.trim($("#edit-cost").val());
			var description=$.trim($("#edit-describe").val());

			//表单验证
			if (name==""){
				alert("名称不能为空");
				return
			}

			if (startDate !="" && endDate!=""){
				if (startDate>endDate){
					alert("开始日期不能大于结束日期");
					return;
				}
			}
			var regExp = /^(([1-9]\d*)|0)$/;
			if (!regExp.test(cost)){
				alert("成本必须是非负整数");
				return;
			}

			//发起请求
			$.ajax({
				url:"workbench/activity/updateActivityById.do",
				type:"post",
				data:{
					id:id,
					owner:owner,
					name:name,
					startDate:startDate,
					endDate:endDate,
					cost:cost,
					description:description
				},
				dataType:"json",
				success:function (data) {
					if (data.code=="1"){
						$("#editActivityModal").modal("hide");
						queryActivityByConditionForPage($("#demo_page1").bs_pagination("getOption", "currentPage"), $("#demo_page1").bs_pagination("getOption", "rowsPerPage"));
					}else {
						alert(data.message);
						$("#editActivityModal").modal("show");
					}

				}
			})
		});

		$("#exportActivityAllBtn").click(function () {
			window.location.href="workbench/activity/exportAllActivity.do";
		});

		//选择导出
		$("#exportActivityXzBtn").click(function () {
			var checkId=$("#tBody input[type='checkbox']:checked");
			if (checkId.size()==0){
				alert("请选择需要导出的市场活动");
				return
			}
			var id = "";
			$.each(checkId,function () {
				id+="id="+this.value+"&";
			})
			id=id.replace("id=","");
			id = id.substr(0,id.length-1);
			window.location.href="workbench/activity/selectExporActivitytById.do?id="+id+"";
		});

		//给导入添加单击事件
		$("#importActivityBtn").click(function () {
			var activityFileName=$("#activityFile").val();//获取文件名
			var suffix=activityFileName.substr(activityFileName.lastIndexOf(".")+1).toLocaleLowerCase();//截取文件格式
			if(suffix!="xls"){
				alert("只支持xls文件");
				return
			}
			var activityFile=$("#activityFile")[0].files[0];//获取文件
			if (activityFile.size>5*1024*1024){
				alert("文件不能大于5MB");
				return;
			}
			var formData=new FormData();
			formData.append("activityFile",activityFile);
			$.ajax({
				url:"workbench/activity/importActivityByList.do",
				data:formData,
				processData:false,
				contentType:false,
				type:"post",
				dataType:"json",
				success:function (data) {
					if (data.code=="1"){
						alert("成功导入"+data.retData+"记录");
						$("#importActivityModal").modal("hide");
						queryActivityByConditionForPage(1,$("#demo_page1").bs_pagination("getOption", "rowsPerPage"));
					}else {
						alert(data.message);
						$("#importActivityModal").modal("show");
					}
				}
			})
		});

		//入口函数结尾-------------------------------------------
	});


	function queryActivityByConditionForPage(pageNo,pageSize) {
		//收集数据
		var name = $("#query-name").val();
		var owner = $("#query-owner").val();
		var startTime = $("#query-startTime").val();
		var endTime = $("#query-endTime").val();
		var beginNo = pageNo;
		var pageSize = pageSize;

		$.ajax({
			url:"workbench/activity/selectActivityByConditionForPage.do",
			type:"post",
			dataType:"json",
			data:{
				name:name,
				owner:owner,
				startTime:startTime,
				endTime:endTime,
				beginNo:beginNo,
				pageSize:pageSize
			},
			success:function (data) {
				//显示总条数
				//$("#totalRows").text(data.totalRows);
				//显示市场活动列表
				//遍历activityList，接收所有行数据
				var html="";
				$.each(data.activityList,function (index,obj) {
					html+="<tr class=\"active\">";
					html+="		<td><input type=\"checkbox\" value='"+obj.id+"'/></td>";
					html+="		<td><a style=\"text-decoration: none; cursor: pointer;\" onclick=\"window.location.href='workbench/activity/detail.do?id="+obj.id+"';\">"+obj.name+"</a></td>";
					html+="<td>"+obj.owner+"</td>";
					html+="<td>"+obj.startDate+"</td>";
					html+="<td>"+obj.endDate+"</td>";
				})
				$("#tBody").html(html);
				$("#chckAll").prop("checked",false);
				//计算总页数
				var totalPages=1;
				if(data.totalRows%pageSize==0){
					totalPages = data.totalRows/pageSize;
				}else {
					totalPages = parseInt(data.totalRows/pageSize)+1;
				}

				$("#demo_page1").bs_pagination({
					currentPage:pageNo,//当前页码
					rowsPerPage:pageSize,//显示多少条数据
					totalRows:data.totalRows,//总条数
					totalPages:totalPages,//总页数
					visiblePageLinks: 5,//最多显示页数
					showGoToPage: true,
					showRowsPerPage: true,
					showRowsInfo: true,
					onChangePage:function (event,pageObj) {
						queryActivityByConditionForPage(pageObj.currentPage,pageObj.rowsPerPage);

					}
				})
			}
		});
	}
</script>
</head>
<body>

	<!-- 创建市场活动的模态窗口 -->
	<div class="modal fade" id="createActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form id="createActivityFrom" class="form-horizontal" role="form">
					
						<div class="form-group">
							<label for="create-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-marketActivityOwner">
								  <c:forEach items="${userList}" var="user">
									  <option value="${user.id}">${user.name}</option>
								  </c:forEach>
								</select>
							</div>
                            <label for="create-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-marketActivityName">
                            </div>
						</div>
						
						<div class="form-group">
							<label for="create-startDate" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control myDate" id="create-startDate" readonly>
							</div>
							<label for="create-endDate" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control myDate" id="create-endDate" readonly>
							</div>
						</div>
                        <div class="form-group">

                            <label for="create-cost" class="col-sm-2 control-label">成本</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-cost">
                            </div>
                        </div>
						<div class="form-group">
							<label for="create-description" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-description"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" id="closeActivityBtn">关闭</button>
					<button type="button" class="btn btn-primary" id="saveActivityBtn">保存</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 修改市场活动的模态窗口 -->
	<div class="modal fade" id="editActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" role="form">
					<input type="hidden" id="hiddenId">
						<div class="form-group">
							<label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: #ff0000;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-marketActivityOwner">
									<c:forEach items="${userList}" var="user">
										<option value="${user.id}">${user.name}</option>
									</c:forEach>
								</select>
							</div>
                            <label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-marketActivityName" value="发传单">
                            </div>
						</div>

						<div class="form-group">
							<label for="edit-startTime" class="col-sm-2 control-label myDate">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control myDate" id="edit-startTime" value="2020-10-10" readonly>
							</div>
							<label for="edit-endTime" class="col-sm-2 control-label myDate">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control myDate" id="edit-endTime" value="2020-10-20" readonly>
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-cost" class="col-sm-2 control-label">成本</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-cost" value="5,000">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="edit-describe">市场活动Marketing，是指品牌主办或参与的展览会议与公关市场活动，包括自行主办的各类研讨会、客户交流会、演示会、新产品发布会、体验会、答谢会、年会和出席参加并布展或演讲的展览会、研讨会、行业交流会、颁奖典礼等</textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="updateActivityBtn">更新</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 导入市场活动的模态窗口 -->
    <div class="modal fade" id="importActivityModal" role="dialog">
        <div class="modal-dialog" role="document" style="width: 85%;">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal">
                        <span aria-hidden="true">×</span>
                    </button>
                    <h4 class="modal-title" id="myModalLabel">导入市场活动</h4>
                </div>
                <div class="modal-body" style="height: 350px;">
                    <div style="position: relative;top: 20px; left: 50px;">
                        请选择要上传的文件：<small style="color: gray;">[仅支持.xls]</small>
                    </div>
                    <div style="position: relative;top: 40px; left: 50px;">
                        <input type="file" id="activityFile">
                    </div>
                    <div style="position: relative; width: 400px; height: 320px; left: 45% ; top: -40px;" >
                        <h3>重要提示</h3>
                        <ul>
                            <li>操作仅针对Excel，仅支持后缀名为XLS的文件。</li>
                            <li>给定文件的第一行将视为字段名。</li>
                            <li>请确认您的文件大小不超过5MB。</li>
                            <li>日期值以文本形式保存，必须符合yyyy-MM-dd格式。</li>
                            <li>日期时间以文本形式保存，必须符合yyyy-MM-dd HH:mm:ss的格式。</li>
                            <li>默认情况下，字符编码是UTF-8 (统一码)，请确保您导入的文件使用的是正确的字符编码方式。</li>
                            <li>建议您在导入真实数据之前用测试文件测试文件导入功能。</li>
                        </ul>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
                    <button id="importActivityBtn" type="button" class="btn btn-primary">导入</button>
                </div>
            </div>
        </div>
    </div>
	
	
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>市场活动列表</h3>
			</div>
		</div>
	</div>
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">
		
			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input class="form-control" type="text" id="query-name">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" id="query-owner">
				    </div>
				  </div>


				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">开始日期</div>
					  <input class="form-control" type="text" id="query-startTime" />
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">结束日期</div>
					  <input class="form-control" type="text" id="query-endTime">
				    </div>
				  </div>
				  
				  <button type="button" class="btn btn-default" id="queryBtn">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button type="button" class="btn btn-primary" id="createActivityBtn"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" id="editActivityBtn"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger" id="deleteActivityBtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				<div class="btn-group" style="position: relative; top: 18%;">
                    <button type="button" class="btn btn-default" data-toggle="modal" data-target="#importActivityModal" ><span class="glyphicon glyphicon-import"></span> 上传列表数据（导入）</button>
                    <button id="exportActivityAllBtn" type="button" class="btn btn-default"><span class="glyphicon glyphicon-export"></span> 下载列表数据（批量导出）</button>
                    <button id="exportActivityXzBtn" type="button" class="btn btn-default"><span class="glyphicon glyphicon-export"></span> 下载列表数据（选择导出）</button>
                </div>
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="chckAll"/></td>
							<td>名称</td>
                            <td>所有者</td>
							<td>开始日期</td>
							<td>结束日期</td>
						</tr>
					</thead>
					<tbody id="tBody">
						<%--<tr class="active">
							<td><input type="checkbox" /></td>
							<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.jsp';">发传单</a></td>
                            <td>zhangsan</td>
							<td>2020-10-10</td>
							<td>2020-10-20</td>
						</tr>--%>

					</tbody>
				</table>
				<div id="demo_page1"></div>
			</div>
			
			<%--<div style="height: 50px; position: relative;top: 30px;">
				<div>
					<button type="button" class="btn btn-default" style="cursor: default;">共<b id="totalRows">50</b>条记录</button>
				</div>
				<div class="btn-group" style="position: relative;top: -34px; left: 110px;">
					<button type="button" class="btn btn-default" style="cursor: default;">显示</button>
					<div class="btn-group">
						<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
							10
							<span class="caret"></span>
						</button>
						<ul class="dropdown-menu" role="menu">
							<li><a href="#">20</a></li>
							<li><a href="#">30</a></li>
						</ul>
					</div>
					<button type="button" class="btn btn-default" style="cursor: default;">条/页</button>
				</div>
				<div style="position: relative;top: -88px; left: 285px;">
					<nav>
						<ul class="pagination">
							<li class="disabled"><a href="#">首页</a></li>
							<li class="disabled"><a href="#">上一页</a></li>
							<li class="active"><a href="#">1</a></li>
							<li><a href="#">2</a></li>
							<li><a href="#">3</a></li>
							<li><a href="#">4</a></li>
							<li><a href="#">5</a></li>
							<li><a href="#">下一页</a></li>
							<li class="disabled"><a href="#">末页</a></li>
						</ul>
					</nav>
				</div>
			</div>--%>
			
		</div>
		
	</div>
</body>
</html>