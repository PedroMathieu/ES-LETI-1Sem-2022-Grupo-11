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
    const date = new Date();
    currMonth = date.getMonth(),
    currYear = date.getFullYear();


const renderCalendar = () => {
    date.setDate(1);

    const lastDay = new Date(
        date.getFullYear(),
        date.getMonth() + 1,
        0
    ).getDate();

    const prevLastDay = new Date(
        date.getFullYear(),
        date.getMonth(),
        0
    ).getDate();



    const firstDayIndex = date.getDay();

    const lastDayIndex = new Date(
        date.getFullYear(),
        date.getMonth() + 1,
        0
    ).getDay();

    const nextDays = 7 - lastDayIndex - 1;

    const months =
        ["Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho",
            "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"];


    document.querySelector(".date h1").innerHTML = months[date.getMonth()];

    document.querySelector(".date p").innerHTML = date.getFullYear();
    let days = "";

    
    for (let x = firstDayIndex; x > 0; x--) {
        days += `<button id="${prevLastDay - x + 1}" onclick="buildUrl(this)" class="prev-date">${prevLastDay - x + 1}</button>`;
    }

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

    for (let j = 1; j <= nextDays; j++) {
        days += `<button id="${j}" onclick="buildUrl(this)" class="next-date">${j}</button>`;
        monthDays.innerHTML = days;
    }
};

document.querySelector(".prev").addEventListener("click", () => {
    date.setMonth(date.getMonth() - 1);
    renderCalendar();
});

document.querySelector(".next").addEventListener("click", () => {
    date.setMonth(date.getMonth() + 1);
    renderCalendar();
});

renderCalendar();

prevNextIcon.forEach(icon => { // getting prev and next icons
    icon.addEventListener("click", () => { // adding click event on both icons
        // if clicked icon is previous icon then decrement current month by 1 else increment it by 1
        currMonth = icon.id === "prev" ? currMonth - 1 : currMonth + 1;
        if (currMonth < 0 || currMonth > 11) { // if current month is less than 0 or greater than 11
            // creating a new date of current year & month and pass it as date value
            date = new Date(currYear, currMonth);
            currYear = date.getFullYear(); // updating current year with new date year
            currMonth = date.getMonth(); // updating current month with new date month
        } else {
            date = new Date(); // pass the current date as date value
        }
        renderCalendar(); // calling renderCalendar function
    });
});

function buildUrl(obj) {
    let checked = []
    let checkboxes = document.querySelectorAll('input[type=checkbox]:checked')
    let urlBuilder;
    let clickedDay = obj.id; //TODO but first we need to refactor this whole file

    for (let i = 0; i < checkboxes.length; i++) {
        checked.push(checkboxes[i].name)
    }
 
    urlBuilder = "/personalCalendar/" + checked.join(";") + "/" + currYear + "/" + date.getMonth() + "/" + clickedDay
    console.log(urlBuilder)
    window.location.href= "CalendarDaily.html";
    let dte=  currYear + "/" + date.getMonth() + "/" + clickedDay;
    document.getElementById("calDate").innerHTML = "2";
}