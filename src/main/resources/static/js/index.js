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
    $('#uploadFileBtn').attr('disabled','disabled');
    var formData = new FormData();
    var f = $('#file')[0].files[0];
//    alert(f.size);
    // 超过50M分片
    if(f.size < 50 * 1024 * 1024){
        formData.append('File', f);
        formData.append('Bucket', $('#bucketName').val());
        formData.append('Path',$('#path').val()+$('#file')[0].files[0].name);
        upload(formData).then((res) => {
            console.log(res)
            location.reload()
        })
    }else{
        //超过100M 进行文件分片传输，默认50M一个分片
        formData.append('Bucket', $('#bucketName').val());
        formData.append('Path', $('#path').val()+$('#file')[0].files[0].name);
        formData.append('Size', f.size);
        initMultipartUpload(formData).then((res) => {
//            console.log(res);
            return res;
        }).then((res) => {
//            console.log(res.data)
//            Promise.all([upload1(1), upload2(2)]).then(res => {
//
//            })
            if(res.code != 0){
                alert(res.data);
            }else{
                var data = res.data;
                var uploadedChunkOffsets = new Array(data.uploadedChunks.length);
                for(j = 0,len=data.uploadedChunks.length; j < len; j++) {
                    uploadedChunkOffsets.push(data.uploadedChunks[j]);
                }
                //分割文件，再上传没有上传的分片
                var uploadPartFuncs = [];
                var start = 0;
                var maxSize = 50 * 1024 * 1024;
                while(start < f.size){
                    var end = (start + maxSize) > f.size?f.size:(start+maxSize);
                    var filePart = f.slice(start, end);
                    if(uploadedChunkOffsets.indexOf(start) == -1){
                        var partFormData = new FormData();
                        partFormData.append('Bucket', $('#bucketName').val());
                        partFormData.append('Path', $('#path').val()+$('#file')[0].files[0].name);
                        partFormData.append('FileId',data.fileId);
                        partFormData.append('Offset',start);
                        partFormData.append('Size', end - start);
                        partFormData.append('File', filePart);
                        uploadPartFuncs.push(uploadPart(partFormData));
                    }
                    start = end;
                }
                Promise.all(uploadPartFuncs).then(res => {
                    console.log(res)
                    formData.append('FileId',data.fileId);
                    completeMultipartUpload(formData).then((r) => {
                        console.log(r);
                        location.reload();
                    })
                })

            }
        })
    }
}



function upload(data) {
    return new Promise((resolve, reject) => {
        $.ajax({
            url: '/file-manage/upload',
            type: 'POST',
            cache: false,
            data: data,
            processData: false,
            contentType: false
        }).done(function(res) {
            resolve(res)
        }).fail(function(res) {
            reject(res)
        });
    })
}

function initMultipartUpload(data){
    return new Promise((resolve, reject) => {
        $.ajax({
            url: '/file-manage/initMultipartUpload',
            type: 'POST',
            cache: false,
            data: data,
            processData: false,
            contentType: false
        }).done(function(res) {
            resolve(res)
        }).fail(function(res) {
            reject(res)
        });
    })
}

function uploadPart(data){
    return new Promise((resolve, reject) => {
        $.ajax({
            url: '/file-manage/uploadPart',
            type: 'POST',
            cache: false,
            data: data,
            processData: false,
            contentType: false
        }).done(function(res) {
            resolve(res)
        }).fail(function(res) {
            reject(res)
        });
    })
}

function completeMultipartUpload(data){
    return new Promise((resolve, reject) => {
        $.ajax({
            url: '/file-manage/completeMultipartUpload',
            type: 'POST',
            cache: false,
            data: data,
            processData: false,
            contentType: false
        }).done(function(res) {
            resolve(res)
        }).fail(function(res) {
            reject(res)
        });
    })
}