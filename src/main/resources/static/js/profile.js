$(function(){
	$(".follow-btn").click(follow);
});

function follow() {
	var btn = this;
	if($(btn).hasClass("btn-info")) {
		var userId = $(btn).prev().val();
		$.post(CONTEXT_PATH+"/follow/follow",{"userId":userId},function (data) {
			data = $.parseJSON(data);
			if (data.code  == 0){
				// 关注TA
				 $(btn).text("已关注").removeClass("btn-info").addClass("btn-secondary");
				 window.location.reload();
			}
		});
	} else {
		var userId = $(btn).prev().val();
		$.post(CONTEXT_PATH+"/follow/unfollow",{"userId":userId},function (data) {
			data = $.parseJSON(data);
			if (data.code  == 0){
				// 取消关注
				$(btn).text("关注TA").removeClass("btn-secondary").addClass("btn-info");
				window.location.reload();
			}
		});
	}
}