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
			drawTimeBlock(result[result.length -1]); // desenhar div da hora agora criada
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
	let input = document.createElement("textarea")

	input.id = `${timeBlock}_1`

	input.classList.add("hide")
	button.innerText = timeBlock
	button.onclick = function(){
		var text = document.getElementById(input.id)
		text.classList.toggle("hide")
		text.classList.toggle("input")

	}

	div.id = timeBlock
	div.classList.add("time")
	document.getElementById("scheduleContainer").appendChild(div)
	document.getElementById(timeBlock).appendChild(button)
	document.getElementById(timeBlock).appendChild(input)
}

window.onload = generateHours;