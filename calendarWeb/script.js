const daysTag = document.querySelector(".days"),
    currentDate = document.querySelector(".current-date"),
    prevNextIcon = document.querySelectorAll(".arrows span");
    let x = 0;
// getting new date, current year and month
let date = new Date(),
    currYear = date.getFullYear(),
    currMonth = date.getMonth();
// storing full name of all months in array
const months = ["Janeiro", "Fevereiro", "Março", "Abril", "Maio", "Junho", "Julho",
    "Agosto", "Setembro", "Outubro", "Novembro", "Dezembro"];
const renderCalendar = () => {
    let firstDayofMonth = new Date(currYear, currMonth, 1).getDay(), // getting first day of month
        lastDateofMonth = new Date(currYear, currMonth + 1, 0).getDate(), // getting last date of month
        lastDayofMonth = new Date(currYear, currMonth, lastDateofMonth).getDay(), // getting last day of month
        lastDateofLastMonth = new Date(currYear, currMonth, 0).getDate(); // getting last date of previous month
    let liTag = "";
    let day = 0;
    for (let i = firstDayofMonth; i > 0; i--) { // creating li of previous month last days
        liTag += `<button class="inactive">${lastDateofLastMonth - i + 1}</button>`;
    }
    for (let i = 1; i <= lastDateofMonth; i++) { // creating li of all days of current month
        // adding active class to li if the current day, month, and year matched
        day++;
        let isToday = i === date.getDate() && currMonth === new Date().getMonth()
            && currYear === new Date().getFullYear() ? "active" : "";
        liTag += `<button onclick="a(this)" id="${day}"class="${isToday}">${i}</button>`;
        //liTag += `<button onclick="getID(this);a()" id="${day}"class="${isToday}">${i}</button>`;
    }
    for (let i = lastDayofMonth; i < 6; i++) { // creating li of next month first days
        liTag += `<button class="inactive">${i - lastDayofMonth + 1}</button>`

    }
    currentDate.innerText = `${months[currMonth]} ${currYear}`; // passing current mon and yr as currentDate text
    daysTag.innerHTML = liTag;
}
    
 function getID(obj)
 {
    alert(obj.id);
 }

function a() {
    $(".arrows span").addClass("hideme")
    $("h1").addClass("hideme")
	$(".dia button").addClass("hideme")
    $(".container").removeClass("hideme")
    document.getElementById("voltar").style.visibility="visible"
    document.getElementById("voltar").style.cursor="pointer"
}   

function darkMode() {
    document.getElementById("pagina").style.backgroundColor="black"
    document.getElementById("prev").style.color="white"
    document.getElementById("next").style.color="white"
    document.getElementById("title").style.color="white"
    document.getElementById("dark-mode").style.color="white"
    document.getElementById("dark-mode").style.backgroundColor="black"
    document.getElementById("voltar").style.color="white"
    document.getElementById("voltar").style.backgroundColor="black"
  } 

  function lightMode() {
    document.getElementById("pagina").style.backgroundColor="white"
    document.getElementById("prev").style.color="black"
    document.getElementById("next").style.color="black"
    document.getElementById("title").style.color="black"
    document.getElementById("dark-mode").style.color="black"
    document.getElementById("dark-mode").style.backgroundColor="white"
    document.getElementById("voltar").style.color="black"
    document.getElementById("voltar").style.backgroundColor="white"
  } 

  function toggle(){
    x++;
    if(Math.abs(x % 2) == 1)
{
    document.getElementsByTagName('body')[0].classList.add(darkMode());
  }
  else if( x % 2 == 0){
    document.getElementsByTagName('body')[0].classList.add(lightMode())
  }
}

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