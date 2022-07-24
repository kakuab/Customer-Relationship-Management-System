<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
String path = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
%>
<base href="<%=path%>">

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
    <script type="text/javascript" src="jquery/echars/echarts.min.js"></script>
    <script type="text/javascript">
        $(function () {
            $.ajax({
                url:"workbench/chart/transaction/queryCountOfTranGroupByStage.do",
                type:"post",
                dataType:"json",
                success:function (data) {
                    var myChart=echarts.init(document.getElementById("main"));
                    var option={
                        title:{
                            text: "交易统计图表",
                            subtext: "交易表中各个阶段的数量"
                        },
                        tooltip: {
                            trigger:'item',
                            formatter:"{a} <br/>{b} :{c}"
                        },
                        toolbox:{
                            feature:{
                                dataView: {readonly:false},
                                restore:{},
                                saveAsImage:{}
                            }
                        },
                        series:[
                            {
                                name:"数据量",
                                type:"funnel",
                                left:"10%",
                                width:"80%",
                                label:{
                                    formatter: '{b}'
                                },
                                labelLine:{
                                    show:true
                                },
                                itemStyle:{
                                    opacity:0.7
                                },
                                emphasis:{
                                    label: {
                                        position:"inside",
                                        formatter:"{b}: {c}"
                                    }
                                },
                                data:data
                            }
                        ]
                    };
                    myChart.setOption(option);
                }
            });
        });
    </script>
</head>
<body>

<div id="main" style="width: 600px; height: 400px"></div>
</body>
</html>
