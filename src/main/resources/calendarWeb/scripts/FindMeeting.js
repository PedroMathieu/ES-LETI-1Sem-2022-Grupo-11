$("#meetingForm").submit(function(e) {
    e.preventDefault();

    let form = $(this);
    let actionUrl = form.attr("action");
    let toSend = $("#meetingForm").serializeArray();

    toSend.push({
        name: "users",
        value: getCheckedUsers().join(',')
    });

    $.ajax({
        type: "POST",
        url: actionUrl,
        data: toSend,
        success: function(data) {
            console.log(data);
        }
    });
});