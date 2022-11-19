/**
 * Generate hours in a day
 */
function generateHours() {
	console.log("Generating hours");
	let result = [];
	let startingHour = 6;
	addHalfAnHour = true;
	result.push(startingHour.toString());

	while (startingHour <= 19) {
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
function drawTimeBlock(timeBlock) {
	let div = document.createElement("div");
	div.innerHTML = timeBlock;
	document.getElementById("main").appendChild(div);
}

window.onload = generateHours;