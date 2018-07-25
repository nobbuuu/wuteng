<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>轮播</title>


<#if ben == 1 >
     <link rel="stylesheet" href="http://120.24.234.123:8666/sunnet_ad/static/az/css/shutter.css" media="screen">
<#else>
    <link rel="stylesheet" href="../shutter.css">
</#if>

    <style>
        body{width:100%;heigth:auto;}
		.shutter-btn,shutter-desc{display:none;}
    </style>
</head>
<body>

<div class="shutter shutter1" >
    <div class="shutter-img">
        <#list imgPaths as obj >
            <a href="#" ><img src="${obj}" alt="#"></a>
        </#list>
    </div>
    <ul class="shutter-btn">
        <li class="next"></li>
    </ul>
    <div class="shutter-desc" style="display: none;">
    </div>
</div>

<#if ben == 1 >
    <script src="http://120.24.234.123:8666/sunnet_ad/static/jquery/jquery-1.8.3.min.js" type="text/javascript"></script>
    <script src="http://120.24.234.123:8666/sunnet_ad/static/az/js/shutter.js"></script>
    <script src="http://120.24.234.123:8666/sunnet_ad/static/az/js/velocity.js"></script>
<#else>
    <script src="../jquery.min.js"></script>
    <script src="../shutter.js"></script>
    <script src="../velocity.js"></script>
</#if>
<script>

    $(function () {
		  $('.shutter1').shutter({
				shutterW: '100', // 容器宽度
				shutterH: '100', // 容器高度
				isAutoPlay: true, // 是否自动播放
				playInterval: ${times}, // 自动播放时间
				duration: ${duration} ,//动画时间
				curDisplay: 0, // 当前显示页
				fullPage: true, // 是否全屏展示
				playType: ${showType} //播放效果
			});
    });

</script>

</body>
</html>