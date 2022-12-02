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
	let input = document.createElement("input")

	input.id = `${timeBlock}_1`

	input.classList.add("hide")
	button.innerText = timeBlock
	button.onclick = function () {
		var text = document.getElementById(input.id)
		text.classList.toggle("hide")
		text.classList.toggle("input")

	}

	div.id = timeBlock
	div.classList.add("time")
	document.getElementById("scheduleContainer").appendChild(div)
	document.getElementById(timeBlock).appendChild(button)
	document.getElementById(timeBlock).appendChild(input)
	drawEvents(div, timeBlock);
}

function drawEvents(div, timeBlock) {
	if(timeBlock == undefined){
		return
	}
	let div2 = document.createElement("div")
	let url = window.location.pathname;
	let urlParts = url.split("/");
	let owners = urlParts[3].split("-");

	console.log(owners)
	div2.id = timeBlock
	div2.classList.add("event")

	

	owners.forEach( user => {
		for (let i = 0; i < eventsFromServer[user]["events"].length; i++){
			let e = eventsFromServer[user]["events"][i];

			let realtime = timeBlock

			if(realtime.length != 5){
				realtime = "0" + timeBlock
			}

			if(e.time_start <= realtime &&  e.time_end > realtime){
				console.log("I was here1")
				document.getElementById(timeBlock).appendChild(div2)
			}

			if(realtime == e.time_start){
				document.getElementById(timeBlock).appendChild(div2)
			}
		}
	})
	
}

window.onload = generateHours;