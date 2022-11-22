/**
* TODO: THIS JAVASCRIPT FILE NEEDS TO BE REFACTORED ASAP. CONTAINS BUGS AND IT COULD BE BETTER ORGANIZED
* Is there a better way to make the calendar work in javascript? Maybe a library?
* We should TRY to refactor the whole file structure (and front end structure) and also some bad practices,
* such as const and let misuse.
* 
* Ideas to get day calendar:
* - Build a new endpoint that sends the json event data in each response, embedded in the template
* - Build an endpoint that only renders a template and then use url params to make javascript requests
*/

const monthDays = document.querySelector(".days"),
    prevNextIcon = document.querySelectorAll(".month"),
    currentDate = document.querySelector(".current-date");
//Gets the current year and month
let date = new Date();
currMonth = date.getMonth();
currYear = date.getFullYear();
date.setDate(1);

const renderCalendar = () => {

    //Gets the last date of the current month
    const lastDay = new Date(
        date.getFullYear(),
        date.getMonth() + 1,
        0
    ).getDate();

    //Gets the last date of the previous month
    const prevLastDay = new Date(
        date.getFullYear(),
        date.getMonth(), 
        0
    ).getDate();

    // Day of the week the current month starts
    const firstDayIndex = date.getDay();

    // Day of the week the current month ends
    const lastDayIndex = new Date(
        date.getFullYear(),
        date.getMonth() + 1,
        0
    ).getDay();

    const nextDays = 6 - lastDayIndex;

    //Stores the full name of all months in this array
    const months =
        ["Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho",
            "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"];

    //Writes the month in HTML
    document.querySelector(".date h1").innerHTML = months[date.getMonth()];

    //Writes the date in HTML
    document.querySelector(".date p").innerHTML = date.getFullYear();
    let days = "";

    //Creates buttons of the previous month last days
    for (let x = firstDayIndex; x > 0; x--) {
        days += `<button id="${prevLastDay - x + 1}.prev" onclick="buildUrl(this)" class="prev-date">${prevLastDay - x + 1}</button>`;
    }

    //Creates buttons of the all the days of current month
    for (let i = 1; i <= lastDay; i++) {
        if (
            i === new Date().getDate() &&
            date.getMonth() && date.getFullYear() === new Date().getDate()
        ) {
            days += `<button onclick="buildURl(this)" id="${i}" class="today">${i}</button>`;
        } else {
            days += `<button onclick="buildUrl(this)" id="${i}">${i}</button>`;
        }
    }

    //Creates buttons of the first days in the next month
    for (let j = 1; j <= nextDays; j++)
        days += `<button id="${j}.next" onclick="buildUrl(this)" class="next-date">${j}</button>`;

    monthDays.innerHTML = days;
};

//If you click in the previous icon the month is decremented by 1
document.querySelector(".prev").addEventListener("click", () => {
    if (date.getMonth() != 0)
        date.setFullYear(date.getFullYear(), date.getMonth() - 1, 1);
    else 
        date.setFullYear(date.getFullYear() - 1, 11, 1);
    
    renderCalendar();
    requestNumberOfEventsThisMonth();
});

//If you click in the next icon the month is incremented by 1
document.querySelector(".next").addEventListener("click", () => {
    if (date.getMonth() != 11)
        date.setFullYear(date.getFullYear(), date.getMonth() + 1, 1);
    else 
        date.setFullYear(date.getFullYear() + 1, 0, 1);
    
    renderCalendar();
    requestNumberOfEventsThisMonth();
});

renderCalendar();

function getCheckedUsers() {
    let checked = []
    let checkboxes = document.querySelectorAll('input[type=checkbox]:checked')

    for (let i = 0; i < checkboxes.length; i++)
        checked.push(checkboxes[i].name)

    return checked;
}

function buildUrl(obj) {
    let checked = getCheckedUsers()
    let urlBuilder;
    let clickedDay = obj.id;
    let month = date.getMonth() + 1;
    let year = date.getFullYear();

    if (clickedDay.includes("prev")) {
        if (month == 1) {
            month = 12;
            year = year - 1;
        } else
            month = month - 1;

        clickedDay = clickedDay.replace(".prev", "");
    }
    
    if (clickedDay.includes("next")) {
        if (month == 12) {
            month = 1;
            year = year + 1;
        } else
            month = month + 1;

        clickedDay = clickedDay.replace(".next", "");
    }

    urlBuilder = "/personalCalendar/e/" + checked.join("-") + "/" + year + "/" + month + "/" + clickedDay;
    window.location.href = urlBuilder;
}

function getText() {
    let dte = currYear + "/" + date.getMonth() + "/" + date.getDate();
    document.getElementById("calDate").innerHTML = dte;
}