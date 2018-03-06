var token = $('#_csrf').attr('content');
var header = $('#_csrf_header').attr('content');
$(document).ready(function () {
  $('#bucketName').change(function(){
      if($(this).val().length > 0){
          $('#bucketNameDiv').removeClass("has-error");
      }
  });
});
$(document).ajaxSend(function(e,xhr,options) {
   xhr.setRequestHeader(header, token);
});
var deleteBucketModal = $("#deleteBucket");
deleteBucketModal.on("show.bs.modal", function(e) {
    // 这里的btn就是触发元素，即你点击的删除按钮
    var btn = $(e.relatedTarget);
    var id = btn.data("id");
    $("#deleteBucketId").html(id);
        // do your work
});
var updateBucketModal = $("#updateBucket");
updateBucketModal.on("show.bs.modal", function(e){
    var btn = $(e.relatedTarget);
    var name = btn.data("name");
    var isPublic = btn.data("public");
    var id = btn.data("id");
    $('#bucketId').val(id);
    $('#newBucketName').val(name);
    $('input[name=updateBucketPublic][value='+isPublic+']').attr("checked",true);
});
function addBucket(){
    var bucketName = $('#bucketName').val();
    if (bucketName.length == 0){
        $('#bucketNameDiv').addClass("has-error");
    }else{
        var radios = document.getElementsByName('addBucketPublic');
        var isPublic = 1;
        for (var i = 0, length = radios.length; i < length; i++) {
            if (radios[i].checked) {
                // do whatever you want with the checked radio
                isPublic = radios[i].value;
                // only one radio can be logically checked, don't check the rest
                break;
            }
        }
        $.ajax({
            type: "POST",
            //the url where you want to sent the userName and password to
            url: "/bucket-manage/addBucket",
            contentType: 'application/json',
            async: false,
//            beforeSend: function(xhr){
//                xhr.setRequestHeader(header, token);
//            },
            //json object to sent to the authentication url
            data: JSON.stringify({ "name": bucketName, "isPublic" : isPublic }),
            success: function (data) {
                $("#addBucketError").hide();
                location.reload();
            },
            failure: function(errMsg) {
                $("#addBucketError").show();
            }
        });

    }
}

function deleteConfirm(){
    var deleteBucketId = $('#deleteBucketId').text();
    $.ajax({
        type: "DELETE",
        //the url where you want to sent the userName and password to
        url: "/bucket-manage/bucket?id="+deleteBucketId,
//        contentType: 'application/json',
        async: false,
    //            beforeSend: function(xhr){
    //                xhr.setRequestHeader(header, token);
    //            },
        //json object to sent to the authentication url
//        data: JSON.stringify({ "id": deleteBucketId}),
//        data: {"id":deleteBucketId},
        success: function (data) {
            location.reload();
        },
        failure: function(errMsg) {

        }
    });
}

function updateBucket(){
    var bucketName = $('#newBucketName').val();
    if (bucketName.length == 0){
        $('#newBucketNameDiv').addClass("has-error");
    }else{
        var radios = document.getElementsByName('updateBucketPublic');
        var isPublic = 1;
        var id = $('#bucketId').val();
        for (var i = 0, length = radios.length; i < length; i++) {
            if (radios[i].checked) {
                // do whatever you want with the checked radio
                isPublic = radios[i].value;
                // only one radio can be logically checked, don't check the rest
                break;
            }
        }
        $.ajax({
            type: "POST",
            //the url where you want to sent the userName and password to
            url: "/bucket-manage/updateBucket",
            contentType: 'application/json',
            async: false,
//            beforeSend: function(xhr){
//                xhr.setRequestHeader(header, token);
//            },
            //json object to sent to the authentication url
            data: JSON.stringify({"id": id, "name": bucketName, "isPublic" : isPublic }),
            success: function (data) {
                $("#updateBucketError").hide();
                location.reload();
            },
            failure: function(errMsg) {
                $("#updateBucketError").show();
            }
        });

    }
}

function search(){
    var path = $('#path').val();
    var keyword = $('#keyword').val();
    var id = $('#id').val();
    var param = "";
    if(path){
        param += "path="+path;
    }
    if(param){
        param += "&";
    }
    if(keyword){
        param += "keyword="+keyword;
    }

    window.location.href = "/bucket/"+id+"?"+param;
}


function addFolder(){
    var folderName = $('#folderName').val();
    var path = $('#path').val();
    path += folderName;
    var bucketId = $('#bucketId').val();
    if (folderName.length == 0){
        $('#folderNameDiv').addClass("has-error");
    }else{
//        $.post('/file-manage/createFolders', { "bucketId": bucketId, "path" : path},
//            function(returnedData){
//                 $("#addFolderError").hide();
//                 location.reload();
//        }).fail(function(){
//              $("#addFolderError").show();
//        });
        $.ajax({
            type: "POST",
            //the url where you want to sent the userName and password to
            url: "/file-manage/createFolders?bucketId="+bucketId+"&path="+path,
            async: false,
//            beforeSend: function(xhr){
//                xhr.setRequestHeader(header, token);
//            },
            //json object to sent to the authentication url
//            data: JSON.stringify({ "bucketId": bucketId, "path" : path }),
            success: function (data) {
                $("#addFolderError").hide();
                location.reload();
            },
            failure: function(errMsg) {
                $("#addFolderError").show();
            }
        });

    }
}

var deleteFolderModal = $("#deleteFolder");
deleteFolderModal.on("show.bs.modal", function(e) {
    // 这里的btn就是触发元素，即你点击的删除按钮
    var btn = $(e.relatedTarget);
    var id = btn.data("id");
    $("#deleteFolderId").html(id);
        // do your work
});

function deleteFolderConfirm(){
    var deleteFolderId = $('#deleteFolderId').text();
    $.ajax({
        type: "POST",
        //the url where you want to sent the userName and password to
        url: "/file-manage/removeFolders?ids="+deleteFolderId,
//        contentType: 'application/json',
        async: false,
    //            beforeSend: function(xhr){
    //                xhr.setRequestHeader(header, token);
    //            },
        //json object to sent to the authentication url
//        data: JSON.stringify({ "id": deleteBucketId}),
//        data: {"id":deleteBucketId},
        success: function (data) {
            location.reload();
        },
        failure: function(errMsg) {

        }
    });
}

var deleteFileModal = $("#deleteFile");
deleteFileModal.on("show.bs.modal", function(e) {
    // 这里的btn就是触发元素，即你点击的删除按钮
    var btn = $(e.relatedTarget);
    var id = btn.data("id");
    $("#deleteFileId").html(id);
        // do your work
});

function deleteFileConfirm(){
    var deleteFileId = $('#deleteFileId').text();
    $.ajax({
        type: "POST",
        //the url where you want to sent the userName and password to
        url: "/file-manage/removeFiles?ids="+deleteFileId,
//        contentType: 'application/json',
        async: false,
    //            beforeSend: function(xhr){
    //                xhr.setRequestHeader(header, token);
    //            },
        //json object to sent to the authentication url
//        data: JSON.stringify({ "id": deleteBucketId}),
//        data: {"id":deleteBucketId},
        success: function (data) {
            location.reload();
        },
        failure: function(errMsg) {

        }
    });
}

function uploadFile(){
    var formData = new FormData();
    formData.append('File', $('#file')[0].files[0]);
    formData.append('Bucket', $('#bucketName').val());
    formData.append('Path',$('#path').val()+$('#file')[0].files[0].name);
    $.ajax({
        url: '/file-manage/upload',
        type: 'POST',
        cache: false,
        data: formData,
        processData: false,
        contentType: false
    }).done(function(res) {
        location.reload();
    }).fail(function(res) {});
}