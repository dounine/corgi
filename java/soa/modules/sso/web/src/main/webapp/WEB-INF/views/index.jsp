<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE HTML>
<HTML xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>sso</title>
    <script src="${C}/public/js/lib/jquery/jquery.js"></script>
</head>
<style>
    *{
        margin: 0;
        padding:0;
    }
    #progress{
        width: 100%;
        float: left;
    }
    #progress ul{
        width: 100%;
        float: left;
    }
    #progress ul li{
        list-style: none;
        width: 100%;
        float: left;
        height:30px;
        line-height: 30px;
        text-indent:10px;
        background: #CCCCCC;
        margin-top:1px;
        position: relative;
    }
    #progress ul li .p1{
        position: absolute;
        width: 100%;
        height:100%;
        z-index: 2;
    }
    #progress ul li .p2{
        position: absolute;
        width: 0%;
        height:100%;
        background: #00b3ee;
    }
    #progress ul li p.pc{
        background: #01ff70;
    }
</style>
<body>
<iframe id="uploadinfo" name="uploadinfo" style="display: none"></iframe>
<form target="uploadinfo" method="post" action="upload" enctype="multipart/form-data">
    <input type="file" id="fileDemo" multiple onchange="getFileSize(this.value)" name="file">
    <input name="fnames" id="fname" value="" >
    <input type="submit" onclick="readProgress()" value="上传">
</form>

<div id="progress">
    <ul>
    </ul>
</div>

<script type="text/javascript">

    function countSeqReadCount(files,curSeq){
        var sum = 0;
        for(var i=0;i<curSeq;i++){
            var f = files[i];
            sum+=f.size;
        }
        return sum;
    }

    var $lis = null,files=null;
    function readProgress(){
        var index = 0;
        var progressInfo = setInterval(function () {
            $.get("uprogress",function(data){
                if(data){
                    var f = files[data.itemSeq-1];
                    if(!data.finish&&f){
                        var proCount = countSeqReadCount(files,index);//统计当前文件之前的所有文件大小
                        var precent = parseInt((((data.bytesRead - proCount)/f.size).toFixed(2)*100));//计算上传进度
                        var proPrecent = precent>=100?100:precent;//上传进度错误信息矫正
                        var $p = $lis.eq(index).find("p.p2");
                        var switchItem = data.itemSeq-1!=index;//是否切换到下个文件
                        if(proPrecent>=100||switchItem){
                            $p.addClass("pc").width("100%");
                        }else{
                            $p.width(proPrecent+"%");
                        }
                        index = data.itemSeq-1;
                    }else if(data.finish){
                        $lis.find("p.p2").addClass("pc").width("100%");
                        clearInterval(progressInfo);
                    }
                }
            });
        },100);
    }

    function getFileSize(val) {
        files  = $("#fileDemo")[0].files;
        var farr = [];
        var $progress = $("#progress ul").empty();
        for(var i =0;i<files.length;i++){
            var file = files[i];
            farr.push({"seq":i,"name":file.name,"size":file.size});
            $("<li><p class='p1'>"+file.name+"</p><p class='p2'></p></li>").appendTo($progress);
        }
        $("#fname").val(JSON.stringify(farr));
        $lis = $progress.find("li");
    }
</script>
</body>
</html>