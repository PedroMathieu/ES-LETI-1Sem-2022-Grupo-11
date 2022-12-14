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

/**
 * Generate Hours
 */
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


/**
 * Generate Events
 */
function drawEvents(div, timeBlock) {
	if(timeBlock == undefined){
		return;
	}
	
	let url = window.location.pathname;
	let urlParts = url.split("/");
	let owners = urlParts[3].split("-");
	let previousUsers = [];

	
	const colors = ["red", "green", "blue", "orange", "pink", "purple"];

	owners.forEach( user => {
		const index = owners.indexOf(user);
  		const color = colors[index];
		let text = ""

		for (let i = 0; i < eventsFromServer[user]["events"].length; i++){
			let e = eventsFromServer[user]["events"][i];
			let realtime = timeBlock
		
			if(realtime.length != 5) {
				realtime = "0" + timeBlock
			}
			
			if (e.time_start <= realtime &&  e.time_end > realtime) {

				if(document.getElementById(`${timeBlock + user}`) == null) {
					previousUsers.forEach(u => { 
						if (u == user) return 
						if (document.getElementById(`${timeBlock + u}`) == null ) {
							document.getElementById(timeBlock).appendChild(createDoc("div", "event", `${timeBlock + u}`,"", "#c9b4a5"))
						}
					})
					document.getElementById(timeBlock).appendChild(createDoc("div", "event", `${timeBlock + user}`,
					 text.concat(e.owner + ": " + e.summary.split("-")[1]), color))
				}
				
				else {
					let div = document.getElementById(`${timeBlock + user}`)
					div.innerText = div.innerText.concat(" /// ",  e.summary.split("-")[1])
				}
			}
		}		
		previousUsers.push(user)
	})
	
}

/**
 * Generate division
 */
function createDoc(tagName, event, id, innerText, color) { 
	let div2 = document.createElement(tagName)

	div2.classList.add(event)
	div2.id = id
	div2.innerText = innerText
	div2.style.backgroundColor = color

	return div2
}

window.onload = generateHours;