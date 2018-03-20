$(document).ready(function () {
    updateClusterStatus();
    updateVolumeStatus();
    setInterval(updateClusterStatus,5000);
    setInterval(updateVolumeStatus,10 * 60 * 1000); //前端每隔10分钟更新一次
});

function updateClusterStatus(){
    $.ajax({
        type: "GET",
        url: "/cluster-manage/clusterState",
        async: false,
        success: function (d) {
            var data = d['data'];
            var trs = "<tr><td>"+data['leader']['url']+"</td><td>"+data['leader']['active']+"</td><td>leader</td></tr>";
            for(j = 0,len=data['peers'].length; j < len; j++) {
                trs += "<tr><td>"+data['peers'][j]['url']+"</td><td>"+data['peers'][j]['active']+"</td><td>peer</td></tr>";
            }
            $("#statusTable > tbody > tr").remove();
            $('#statusTable > tbody:last-child').append(trs);
        },
        failure: function(errMsg) {

        }
    });
}
function updateVolumeStatus(){
    $("#volumeStatusTable > tbody > tr").remove();
    $.ajax({
        type: "GET",
        url: "/cluster-manage/volumeState",
        async: false,
        success: function (d) {
            var data = d['data'];
            var dataCenters = data["dataCenters"];
            for(j=0,len=dataCenters.length;j<len;j++){
                var dcName = dataCenters[j]['id'];
                for(i=0,l=dataCenters[j]['racks'].length;i<l;i++){
                    var rackName = dataCenters[j]['racks'][i]['id'];
                    var dataNodes = dataCenters[j]['racks'][i]['dataNodes'];
                    for(k=0,m=dataNodes.length;k<m;k++){
                        var trs = "<tr><td>"+dataNodes[k]['url']+"</td><td>"+dataNodes[k]['active']+"</td><td>"+dcName+"</td><td>"
                        +rackName+"</td><td>"+dataNodes[k]["dir"]+"</td><td>"+dataNodes[k]['max']+"</td><td>"+dataNodes[k]['free']+"</td><td>"
                        +dataNodes[k]['maxSize']['size']+dataNodes[k]['maxSize']['unit']+"</td><td>"+dataNodes[k]['freeSize']['size']+dataNodes[k]['freeSize']['unit']+"</td></tr>";
                        $('#volumeStatusTable > tbody:last-child').append(trs);
                    }
                }
            }
        },
        failure: function(errMsg) {

        }
    });
}