$(function(){
	$("#publishBtn").click(publish);
});

function publish() {
	//隐藏弹出框
	$("#publishModal").modal("hide");
	//单击发布按钮之后发送AJAX请求，向服务端发送帖子内容
	var title = $("#recipient-name").val();
	var content = $("#message-text").val();
	//发送请求
	$.post(CONTEXT_PATH+"/discussPost/add",{"title":title,"content":content},
		function (data) {
			data = $.parseJSON(data);
			//填写提示数据
			$("#hintBody").text(data.msg);
			//展示提示框
			$("#hintModal").modal("show");
			//两秒后隐藏提示框
			setTimeout(function(){
				$("#hintModal").modal("hide");
			}, 2000);
			//刷新页面
			window.location.reload();
		});
}