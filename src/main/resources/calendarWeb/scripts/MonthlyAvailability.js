let jsonNumberOfEvents = {};
let meanNumberOfEvents = 0;
let dates = [];


function getRepresentedDaysEvents() {
	let daysDiv = document.getElementById("d");
	let daysElements = daysDiv.getElementsByTagName("button");
	let dates = [];

	for (let i = 0; i < daysElements.length; i++) {

		// For now, it only adds days of the represented month
		if (daysElements[i].className != "prev-date" && daysElements[i].className != "next-date")
		    dates.push(date.getFullYear() + "/" + (date.getMonth()+1) + "/" + daysElements[i].id);
	}

	return dates;
}

function getDayOfRepresentedDates(date) {
    let dateSplit = date.split("/");
    let day = dateSplit[dateSplit.length - 1].toString()
    return day;
}
    
function requestNumberOfEventsThisMonth() {
	console.log("getting number of events");
    dates = getRepresentedDaysEvents();
	let checked = getCheckedUsers();
	let jsonResponses = {};

	for (let i = 0; i < dates.length; i++) {
		fetch("/personalCalendar/n/" + checked.join("-") + "/" + dates[i])
		.then(res => res.json())
		.then(json => jsonResponses[getDayOfRepresentedDates(dates[i])] = json)
	}

    // For now we wait with javascript for all fetches to end. To ensure getMeanNumberOfEvents()
    // works correctly
    setTimeout(() => {
        jsonNumberOfEvents = jsonResponses;
        getMeanNumberOfEvents();
    }, 1000);
}

function drawAvailabilityOnCalendar() {
    let daysDiv = document.getElementById("d");
    let daysElements = daysDiv.getElementsByTagName("button");
    let checked = getCheckedUsers();

    for (let i = 0; i < daysElements.length; i++) {
        
        // Se o elemento pertence a um dia correspondente a este mes
        if (daysElements[i].className != "prev-date" && daysElements[i].className != "next-date") {
            let maxNumberOfEventsInDay = 0;
            
            // Obter o numero de maximo de events de cada utilizador selecionado nesse dia
            for (let j = 0; j < checked.length; j++) {
                console.log(Number(daysElements[i].innerHTML))
                maxNumberOfEventsInDay = maxNumberOfEventsInDay + 
                    jsonNumberOfEvents[Number(daysElements[i].innerHTML)][checked[j]];
            }
            
            // fazer a media de eventos desse dia para cada utilizador
            let dayMean = Math.round(maxNumberOfEventsInDay / (checked.length));
            
            /**
             * Decidir a cor baseado na comparacao com a media geral
             * A forma de decidir a cor pode ser alterada, tanto na parte
             * da representacao da cor. Como na metrica usada para atribuir
             * a cor (nao utilizar media, mas sim outra coisa)
             */
            if (dayMean > meanNumberOfEvents)
                daysElements[i].style.color = "#ffad99";
            else if (dayMean < meanNumberOfEvents)
                daysElements[i].style.color = "#99ff99";
            else
                daysElements[i].style.color = "#ffff4d";
        }
    }
}

function getMeanNumberOfEvents() {
    let checked = getCheckedUsers();
    let numberOfEventsSum = 0;
    
    for (let day in jsonNumberOfEvents) {
        for (let i = 0; i < checked.length; i++)
            numberOfEventsSum = numberOfEventsSum + jsonNumberOfEvents[day][checked[i]];
    }

    meanNumberOfEvents = Math.round(numberOfEventsSum / (dates.length * checked.length));
    drawAvailabilityOnCalendar();   
}

requestNumberOfEventsThisMonth();