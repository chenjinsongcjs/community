
    function like(btn, entityType, entityId, userId,postId) {
        $.post(CONTEXT_PATH+"/like",
            {"entityType":entityType,"entityId":entityId,"entityUserId":userId,"postId":postId},
            function (data) {
                data = $.parseJSON(data);
                var sta = data.likeStatus?"已赞":"赞"
                $(btn).children("b").text(sta);
                $(btn).children("i").text(data.likeCount);
            });
    }
    function topPost(btn,postId) {
        $.post(
            CONTEXT_PATH+"/top",
            {"postId":postId},
            function (data) {
                data = $.parseJSON(data);
                if (data.code != 0)
                    alert(data.msg);
                else
                    $(btn).addClass("disabled");
            }
        )
    }
    function updateStatus(btn,postId,status) {
    // alert("postId"+postId+"--> status"+status)
        $.post(
            CONTEXT_PATH+"/update/"+status,
            {"postId":postId},
            function (data) {
                data = $.parseJSON(data);
                if (data.code != 0)
                    alert(data.msg);
                else
                    $(btn).addClass("disabled");
            }
        )
    }