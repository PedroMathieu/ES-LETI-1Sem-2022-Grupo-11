/**
 * Generate hours in a day
 */
function generateHours() {
	console.log("Generating hours");
	let result = [];
	let startingHour = 0;
	addHalfAnHour = false;
	result.push(startingHour.toString());

	while (startingHour <= 23) {
		if (addHalfAnHour) {
			result.push(startingHour.toString() + ":30");
			addHalfAnHour = false;
			startingHour++;
			drawTimeBlock(result[result.length - 1]); // desenhar div da hora agora criada
			continue;
		}

		else {
			result.push(startingHour.toString() + ":00");
			addHalfAnHour = true;
			drawTimeBlock(result[result.length - 1]);
			continue;
		}
	}
	console.log(result);
}

/**
 * Draw the generated hour in the screen
 * @param {String} timeBlock 
 */

let prevTime

function drawTimeBlock(timeBlock) {
	let div = document.createElement("div")
	let button = document.createElement("button")

	button.innerText = timeBlock
	button.onclick = function () {
	}

	div.id = timeBlock
	div.classList.add("time")
	document.getElementById("scheduleContainer").appendChild(div)
	document.getElementById(timeBlock).appendChild(button)
	
	drawEvents(div, timeBlock);
}

function drawEvents(div, timeBlock) {
	if(timeBlock == undefined){
		return;
	}

	let table = document.createElement("table");
	let tableRow = document.createElement("tr");
	
	let div2 = document.createElement("div")
	let url = window.location.pathname;
	let urlParts = url.split("/");
	let owners = urlParts[3].split("-");

	console.log(owners)
	div2.id = `${timeBlock}_2`
	div2.classList.add("event")
	const colors = ["red", "green", "blue", "orange", "pink", "purple", "yellow", "gray", "brown", "black"];
	let parentContainer = document.getElementById('parent-container');

	owners.forEach( user => {
		const index = owners.indexOf(user);
  		const color = colors[index];

		  let tableCell = document.createElement("td");
		  tableCell.id = `${timeBlock}_${user}`
		  tableCell.classList.add("event")


		for (let i = 0; i < eventsFromServer[user]["events"].length; i++){
			let e = eventsFromServer[user]["events"][i];
			console.log(e)
			let realtime = timeBlock

			if(realtime.length != 5){
				realtime = "0" + timeBlock
			}

			if(e.time_start <= realtime &&  e.time_end > realtime){
				div2.innerText = e.owner + ": " + e.summary.split("-")[1]
				div2.style.backgroundColor = color;
				document.getElementById(timeBlock).appendChild(div2)
			}

			if(realtime == e.time_start){
				div2.innerText = e.owner + ": " + e.summary.split("-")[1]
				div2.style.backgroundColor = color;
				document.getElementById(timeBlock).appendChild(div2)
			}
		}

	
	})
	
}

window.onload = generateHours;