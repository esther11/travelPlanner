function initialize() {
    var mapOptions = {
        //设置经纬度
        center: new google.maps.LatLng(-34.397, 150.644),
        zoom: 8,//地图的缩放度
        mapTypeId: google.maps.MapTypeId.ROADMAP
    };
    //创建一个地图
    var map = new google.maps.Map(document.getElementById("map_canvas"),
        mapOptions);
}


function openNav() {
    document.getElementById("panel").style.marginLeft = "0px";
}

function closeNav() {
    document.getElementById("panel").style.marginLeft = "-260px";
}
