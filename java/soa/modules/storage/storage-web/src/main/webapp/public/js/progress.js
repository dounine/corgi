/**
 * Created by huanghuanlai on 16/6/28.
 */
onmessage = function (data) {
        var ajax = new XMLHttpRequest();
        ajax.open("GET","/storage-web/large/"+data.data+"/progress");
        ajax.send(null);
}