/**
 * Created by bartek on 28.06.17.
 */


$(document).ready(function() {
    $('#summernote').summernote();
});


$(document).ready(function() {
    $("#submit").click(function () {
        var label = $('#label').val();
        var content = $('#summernote').summernote('code');
        $.ajax({
            type: "PUT",
            url: window.location.pathname,
            contentType: "application/json",
            data: JSON.stringify({"content": content, "label": label})
        });
        location.reload();
    });
});
