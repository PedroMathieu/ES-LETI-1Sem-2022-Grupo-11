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
            if (data.error == "oops") {
                alert("Something wrong happened when finding a meeting. Please check your inputs")
                document.getElementById("meeting-form").style.display = "none";
            } else {
                document.getElementById("meeting-form").style.display = "none";
                document.getElementById("meetingDate").innerText = data.dayOfMeeting;
                document.getElementById("meetingTStart").innerText = data.timeslotStart;
                document.getElementById("meetingTEnd").innerText = data.timeslotEnd;

                if (document.getElementById("meeting-info-container").style.display == "none") {
                    document.getElementById("meeting-info-container").style.display = "block";
                } else {
                    document.getElementById("meeting-info-container").style.display = "none";
                }
            }
            
        },
        error: function(err) {
            console.log(err)
        }
    });
});
