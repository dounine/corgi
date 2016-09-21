<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE HTML>
<HTML xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>storage</title>
    <script src="${C}/public/js/lib/jquery/jquery.js"></script>
</head>
<style>
    * {
        margin: 0;
        padding: 0;
    }

    .progress {
        width: 100%;
        float: left;
    }

    .progress ul {
        width: 100%;
        float: left;
    }

    .progress ul li {
        list-style: none;
        width: 100%;
        float: left;
        height: 30px;
        line-height: 30px;
        text-indent: 10px;
        background: #CCCCCC;
        margin-top: 1px;
        position: relative;
    }

    .progress ul li .p1 {
        position: absolute;
        width: 100%;
        height: 100%;
        z-index: 2;
    }

    .progress ul li .p2 {
        position: absolute;
        width: 0%;
        height: 100%;
        background: #00b3ee;
    }

    .progress ul li p.finish {
        background: #01ff70;
    }
    input[type=file]{
        margin:10px;
    }
    input[type=button]{
        margin-left:10px;
    }
    #pendingBox{
        display: none;
    }
</style>
<body>

<div>
    <iframe name="newUploadTarget" style="display: none"></iframe>
    <form target="newUploadTarget" method="post" enctype="multipart/form-data">
        <input type="file" name="file" id="newFiles" multiple onchange="getFileSize(this.value)">
        <input type="button" onclick="submitForm()" value="上传">
        <input type="button" onclick="pauseForm()" value="暂停">
        <input type="button" onclick="resumeForm()" value="继续">
        <input type="button" onclick="cancelForm()" value="取消">
    </form>

    <div id="newProgress" class="progress">
        <ul>
        </ul>
    </div>
</div>
<div id="pendingBox">
    <iframe name="pendingUploadTarget" style="display: none"></iframe>
    <form target="pendingUploadTarget" method="post" enctype="multipart/form-data">
        <input type="file" name="file" id="pendingFiles" multiple onchange="getPendingFileSize(this.value)">
        <input type="button" onclick="pendingSubmitForm()" value="上传">
        <input type="button" onclick="pendingPauseForm()" value="暂停">
        <input type="button" onclick="pendingResumeForm()" value="继续">
        <input type="button" onclick="pendingCancelForm()" value="取消">
    </form>

    <div id="pendingProgress" class="progress">
        <ul>
        </ul>
    </div>
</div>


<script type="text/javascript">

    var maxPercents = [];//用于保存文件上传最大进度
    var xmlRequests = [];//文件分片上传请求
    var newTokens = [];//上传的所有文件token
    var $lis = null;
    var newFiles = null;//选择的所有文件信息(html5)

    var pendingFiles = null;//等待上传文件
    var pendingTokens = [];
    var readPendingFiles = null;//后台读取等待上传的文件

    window.onload = function () {
        var $ul = $("#pendingProgress ul");
        $.get("file/pending", function (data) {
            if (data) {
                var infos = data.data;
                if(infos.length>0){
                    readPendingFiles = infos;
                    $("#pendingBox").show();//等待上传文件
                    infos.forEach(function (file) {
                        var li = $("<li id='" + file.token + "' avgShar='" + file.avgShar + "' maxShar='" + file.maxShar + "'><p class='p1'>" + file.name + "</p><p class='p2'></p></li>").appendTo($ul);
                        if (file.progress && file.progress.percent > 0) {
                            li.find('p.p2').animate({
                                width: file.progress.percent + "%"
                            });
                        }
                    });
                }
            }
        });
    }

    /**
     * 取消上传
     */
    function cancelForm() {
        xmlRequests.forEach(function (data) {
            data.xmlRequest.abort();
        });
        newTokens.forEach(function (token) {
            $.ajax({
                url:"file/" + token + "/cancel",
                type:"DELETE"
            });
        });
        $("#newFiles").val("");
        $("#newProgress ul").empty();
    }

    /**
     * 暂停上传
     */
    function pauseForm() {
        xmlRequests.forEach(function (data) {
            data.xmlRequest.abort();
        });
        newTokens.forEach(function (token) {
            $.ajax({
                url:"file/" + token + "/pause",
                type:"PUT"
            });
        });
    }

    /**
     * 继续上传
     */
    function resumeForm() {
        newTokens.forEach(function (token) {
            $.get("file/" + token, function (rep) {
                $.each(newFiles, function (j, file) {
                    var data = rep.data;
                    if (data.name == file.name) {
                        var size = file.size;//文件大小
                        var sharCount = data.maxShar;//总片数
                        for (var index = 0; index < sharCount; index++) {
                            if (data.shars.indexOf(index + 1) == -1) {
                                postFile(file, index, data.token);
                            }
                        }
                    }
                });
            });
        })
    }

    /**
     * 文件上传
     * @param file
     * @param sharIndex
     * @param token
     * @param avgShar
     */
    function postFile(file, sharIndex, token, avgShar) {
        var size = file.size;//文件大小
        var start = sharIndex * avgShar;
        var end = Math.min(size, start + avgShar);
        var formData = new FormData();
        var shar = sharIndex + 1;
        var xhr = new XMLHttpRequest();
        xhr.open("POST", "file/" + token + "/" + shar, true);
        xhr.addEventListener("load", uploadComplete, false);
        formData.append("data", file.slice(start, end));//截取文件一部分
        xhr.send(formData);
        xmlRequests.push({"token": token, "xmlRequest": xhr});
    }

    function uploadComplete(evt) {
        var xhr = evt.target;
        if (xhr.readyState == 4 && xhr.status == 200) {
            var data = xhr.responseText;
            if (data) {
                data = JSON.parse(data);
                if (data.percent == 100) {
                    $lis.filter("[id=" + data.token + "]").find('p.p2').addClass('finish').width("100%");
                    removeFinishToken(data.token);//移除已经完成上传的文件token
                } else {
                    $lis.filter("[id=" + data.token + "]").find('p.p2').width(data.percent + "%");
                }
            }
        }
    }

    function removeFinishToken(token) {
        for (var i = 0, len = newTokens.length; i < len; i++) {
            if (token == newTokens[i]) {
                delete newTokens[i];
                delete newFiles[i];
            }
        }
    }


    function submitForm() {
        maxPercents = [];
        $.each(newFiles, function (i, file) {
            var size = file.size;//文件大小
            var oli = $lis.eq(i);
            var maxShar = parseInt(oli.attr("maxShar"));//总片数
            var token = oli.attr("id");
            var avgShar = parseInt(oli.attr("avgShar"));//平均每片大小
            setTimeout(function () {
                for (var index = 0; index < maxShar; index++) {
                    postFile(file, index, token, avgShar);
                }
            });
        });
    }

    function pendingSubmitForm() {
        $.each(pendingFiles, function (i, file) {
            var size = file.size;//文件大小
            var oli = $lis.eq(i);
            var maxShar = parseInt(oli.attr("maxShar"));//总片数
            var token = oli.attr("id");
            var avgShar = parseInt(oli.attr("avgShar"));//平均每片大小
            for (var index = 0; index < maxShar; index++) {
                postFile(file, index, token, avgShar);
            }
        });
    }
    
    function getPendingFileSize(val) {
        var inputFiles = $("#pendingFiles")[0].files;
        var $progress = $("#pendingProgress ul");

        $.each(inputFiles, function (i, file) {
            readPendingFiles.forEach(function (info) {
                if(info.name==file.name){
                    pendingFiles.push(file);
                }
            });
        });
    }

    function getFileSize(val) {
        newFiles = $("#newFiles")[0].files;
        var $progress = $("#newProgress ul").empty();
        $.each(newFiles, function (i, file) {
            $.ajax({
                url: "file/token",
                type: "POST",
                async: false,
                data: {"size": file.size, "name": file.name},
                success: function (data) {//创建文件上传事件ID
                    if (data) {
                        newTokens[i] = data.data.token;
                        $("<li id='" + newTokens[i] + "' avgShar='" + data.data.avgShar + "' maxShar='" + data.data.maxShar + "'><p class='p1'>" + file.name + "</p><p class='p2'></p></li>").appendTo($progress);
                    }
                }
            });
        });
        $lis = $progress.find("li");
    }
</script>
</body>
</html>