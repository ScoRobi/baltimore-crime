// Define the urls the the backend
var base_url = 'http://localhost:8080/core/map';
var data_url = base_url+'/data';
var bounds_url = data_url+'/bounds';

// Define the width and height of the map
var width = 1000,
	height = 500;

// Define the initial focus of the map mercator projection
var proj = d3.geo.mercator()
		.center([-76.6167, 39.2833])
		.scale(120000)
		.rotate([0,0]);

var path = d3.geo.path()
		.projection(proj);

// Determine the startup level of zoom into the map
var zoom = d3.behavior.zoom()
    .translate(proj.translate())
    .scale(proj.scale())
	.scaleExtent([proj.scale(), proj.scale()*1000])
	.on("zoom", zoom);

// Declare the Scalable Vector Graphics (SVG)
var svg = d3.select("#map").append("svg")
		.attr("width", width)
		.attr("height", height)
		.call(zoom);

// Scale the points on the map when zooming
function zoom() {
	proj.translate(d3.event.translate).scale(d3.event.scale);
	svg.selectAll("path").attr("d", path);
	circles
  		.attr("cx", function(d){return proj([d.longitude, d.latitude])[0];})
		.attr("cy", function(d){return proj([d.longitude, d.latitude])[1];});
}

// Initialize the svg variables
var borders = svg.append("g");
var crimeLocation = svg.append("g");
var tooltip = d3.select("body").append("div")
    .attr("class", "tooltip")
    .style("opacity", 1e-5)
	.style("background", "rgba(250,250,250,.7)");

// Add topography data to queue
queue()
	.defer(d3.json, "CSA_NSA_Tracts.topo.json")
	// .defer(d3.csv, "test_baltimore.csv")	// For Local Testing: <uncomment>
	.await(ready);

/**
 *  Application entry point.
 */
function ready(error, topology) {   // For Local Testing: (error, topology, csv) {

	// Build map of Baltimore City
	borders.selectAll("path")
		.data(topojson.object(topology, topology.objects.CSA_NSA_Tracts)
			.geometries)
		.enter()
		.append("path")
		.attr("d", path)
		.attr("class", "border")

	// Read data from backend
	rawCrimes = getData(null); 	// For Local Testing: rawCrimes = csv;

	// Read and set data bounds
	readDataBounds();

	// Apply filter when the apply button is clicked
	$("#apply-button").click(function(){
		getData(buildFilter());
	});

	// Reset filter when the reset button is clicked
	$("#clear-button").click(function(){
		clearFilter();
	});

}

/**
 *  Populate the map with crime data.
 */
function populate(crimes) {
	// Some of the crimes have 'null' for the weapon. This will populate weapon with 'UNKNOWN'.
	crimes.forEach(function(d){
		d.weapon = resolveWeaponName(d.weapon);
	});

	// Creat the visual representations for each crime point and tooltip
	crimeLocation.selectAll("circle").remove();
	circles = crimeLocation.selectAll("circle")
		.data(crimes).enter()
			.append("circle")
				.attr("cx", function(d){return proj([d.longitude, d.latitude])[0];})
				.attr("cy", function(d){return proj([d.longitude, d.latitude])[1];})
				.attr("r", 	function(d){return 2.25;}) //SR
				.attr("id", function(d){return "id" + d.id;})
			.style("fill", function(d){ return getCrimeColor(d.weapon); })
		.on("mouseover", function(d){
			d3.select(this)
				.attr("stroke", function(d){ return getCrimeColor(d.weapon); })
				.attr("stroke-width", 12)
				.attr("fill-opacity", 2);

			tooltip
			    .style("left", (d3.event.pageX + 5) + "px")
			    .style("top", (d3.event.pageY - 5) + "px")
			    .transition().duration(300)
			    .style("opacity", 1)
			    .style("display", "block")

			updateDetails(d);
			})
		.on("mouseout", function(d){
			d3.select(this)
				.attr("stroke", "")
				.attr("fill-opacity", function(d){return 1;})

			tooltip.transition().duration(700).style("opacity", 0);
		})
	;
}

/**
 *  Variable to define the details to be included in the tooltip.
 */
var printDetails = [
	{'var': 'district', 'print': 'District'},
	{'var': 'neighborhood', 'print': 'Neighborhood'},
	{'var': 'crimeCode', 'print': 'Crime Code'},
	{'var': 'weapon', 'print': 'Weapon'},
	{'var': 'crimeDate', 'print': 'Date'},
	{'var': 'crimeTime', 'print': 'Time'},
	{'var': 'latitude', 'print': 'Latitude'},
	{'var': 'longitude', 'print': 'Longitude'}];

/**
 *  Updates the tooltip with the current crime's details.
 */
function updateDetails(crime){
	tooltip.selectAll("div").remove();
	tooltip.selectAll("div").data(printDetails).enter()
		.append("div")
			.append('span')
				.text(function(d){return d.print + ": ";})				
				.attr("class", "boldDetail")
			.insert('span')
				.text(function(d){return crime[d.var];})
				.attr("class", "normalDetail");
}

/**
 *  Used to get data from the backend via a REST endpoint.
 */
function getData(filter){
	/**
	 *  The following block uses the POST endpoint with a filter in the body to query the data.
	 */
	$.ajax({
		headers: {
			'Accept': 'application/json',
			'Content-Type': 'application/json'
		},
		url: data_url,
		type: 'POST',
		crossDomain: true,
		// dataType: "json",
		data: filter == null ? null : JSON.stringify(filter),
		success: function (response) {
			response.forEach(function(d){
				d.weapon = resolveWeaponName(d.weapon);
			})
			return populate(response);
		},
		error: function (response) {

		}
	})
}

/**
 *  Color codes each crime based on weapon used.
 */
function getCrimeColor(weapon){
	if (weapon == "HANDS") {
		return "green";
	} else if (weapon == "KNIFE") {
		return "orange";
	} else if (weapon == "FIREARM") {
		return "red";
	} else { //"OTHER"
		return "black";
	}
}

/**
 *  Replaces null or invalid weapon names with "UNKNOWN".
 */
function resolveWeaponName(weapon){
	if (weapon == "HANDS") {
		return "HANDS";
	} else if (weapon == "KNIFE") {
		return "KNIFE";
	} else if (weapon == "FIREARM") {
		return "FIREARM";
	} else if (weapon == "OTHER") {
		return "OTHER";
	} else {
		return "UNKNOWN";
	}
}

/**
 *  Returns all filter options to their fully inclusive states and updates the data.
 */
function clearFilter() {

	// Reset District checkboxes
	$('#checkbox-central').prop("checked", true);
	$('#checkbox-eastern').prop("checked", true);
	$('#checkbox-northern').prop("checked", true);
	$('#checkbox-northwestern').prop("checked", true);
	$('#checkbox-southeastern').prop("checked", true);
	$('#checkbox-southern').prop("checked", true);
	$('#checkbox-southwestern').prop("checked", true);
	$('#checkbox-western').prop("checked", true);

	// Reset Weapon checkboxes
	$('#checkbox-hands').prop("checked", true);
	$('#checkbox-knife').prop("checked", true);
	$('#checkbox-firearm').prop("checked", true);
	$('#checkbox-other').prop("checked", true);

	// Reset CSV filters
	$('#locations').val("");
	$('#posts').val("");
	$('#neighborhoods').val("");
	$('#crimecodes').val("");

	// Reset Slider filters
	$( "#date-slider-range" ).slider( "values", 0, $( "#date-slider-range" ).slider("option", "min"));
	$( "#date-slider-range" ).slider( "values", 1, $( "#date-slider-range" ).slider("option", "max"));
	$( "#lat-slider-range" ).slider( "values", 0, $( "#lat-slider-range" ).slider("option", "min"));
	$( "#lat-slider-range" ).slider( "values", 1, $( "#lat-slider-range" ).slider("option", "max"));
	$( "#lon-slider-range" ).slider( "values", 0, $( "#lon-slider-range" ).slider("option", "min"));
	$( "#lon-slider-range" ).slider( "values", 1, $( "#lon-slider-range" ).slider("option", "max"));

	// Reset Spinbox filters
	$( "#time-start-spinner" ).val("12:00 AM");
	$( "#time-end-spinner" ).val("11:59 PM");

	return getData(null);
}

/**
 *  Builds a filter based on the user input data.
 */
function buildFilter() {

	// Collect Checkbox filters
	var fDistricts = buildFilterForDistricts();
	var fWeapons = buildFilterForWeapons();

	// Collect CSV filters
	var fLocations = isBlank($('#locations').val()) ? null : $('#locations').val().split(',').toString();
	var fPosts = isBlank($('#posts').val()) ? null : $('#posts').val().split(',').toString();
	var fNeighborhoods = isBlank($('#neighborhoods').val()) ? null : $('#neighborhoods').val().split(',').toString();
	var fCrimeCodes = isBlank($('#crimecodes').val()) ? null : $('#crimecodes').val().split(',').toString();

	// Collect Date Slider filter
	var fDateRange = {
		startDate: "2016-06-" + $( "#date-slider-range" ).slider( "values", 0 ),
		endDate: "2016-06-" + $( "#date-slider-range" ).slider( "values", 1 )
	};

	// Collect Latitude Slider filter
	var fLatRange = {
		startLatitude: $( "#lat-slider-range" ).slider( "values", 0 ),
		endLatitude: $( "#lat-slider-range" ).slider( "values", 1 )
	};

	// Collect Longitude Slider filter
	var fLonRange = {
		startLongitude: $( "#lon-slider-range" ).slider( "values", 0 ),
		endLongitude: $( "#lon-slider-range" ).slider( "values", 1 )
	};

	// Collect Time Spinbox filters
	var fTimeRange = {
		startTime: builtFilterForTime($( "#time-start-spinner" )),
		endTime: builtFilterForTime($( "#time-end-spinner" ))
	};

	// Declare filter skeleton
	var filter = {
		districts: [],
		weapons: [],
		locations: [],
		posts: [],
		neighborhoods: [],
		crimeCodes: [],
		dateRanges: [],
		latitudeRanges: [],
		longitudeRanges: [],
		timeRanges: []
	};

	// Build into JSON array
	if (!isEmpty(fDistricts)) {
		filter.districts.push(fDistricts);
	}
	if (!isEmpty(fWeapons)) {
		filter.weapons.push(fWeapons);
	}
	if (!isEmpty(fLocations)) {
		filter.locations.push(fLocations);
	}
	if (!isEmpty(fPosts)) {
		filter.posts.push(fPosts);
	}
	if (!isEmpty(fNeighborhoods)) {
		filter.neighborhoods.push(fNeighborhoods);
	}
	if (!isEmpty(fCrimeCodes)) {
		filter.crimeCodes.push(fCrimeCodes);
	}
	if (!isEmpty(fDateRange)) {
		filter.dateRanges.push(fDateRange);
	}
	if (!isEmpty(fLatRange)) {
		filter.latitudeRanges.push(fLatRange);
	}
	if (!isEmpty(fLonRange)) {
		filter.longitudeRanges.push(fLonRange);
	}
	if (!isEmpty(fTimeRange)) {
		filter.timeRanges.push(fTimeRange);
	}

	// Return JSON array filter
	return filter;
}

/**
 *  Builds only the filter for districts based on the checkboxes.
 */
function buildFilterForDistricts() {
	var fdistricts = [];

	// Iterate through each checkbox and add to filter if checked
	if ($('#checkbox-central').is(":checked")) {
		fdistricts.push("CENTRAL");
	}
	if ($('#checkbox-eastern').is(":checked")) {
		fdistricts.push("EASTERN");
	}
	if ($('#checkbox-northeastern').is(":checked")) {
		fdistricts.push("NORTHEASTERN");
	}
	if ($('#checkbox-northern').is(":checked")) {
		fdistricts.push("NORTHERN");
	}
	if ($('#checkbox-northwestern').is(":checked")) {
		fdistricts.push("NORTHWESTERN");
	}
	if ($('#checkbox-southeastern').is(":checked")) {
		fdistricts.push("SOUTHEASTERN");
	}
	if ($('#checkbox-southern').is(":checked")) {
		fdistricts.push("SOUTHERN");
	}
	if ($('#checkbox-southwestern').is(":checked")) {
		fdistricts.push("SOUTHWESTERN");
	}
	if ($('#checkbox-western').is(":checked")) {
		fdistricts.push("WESTERN");
	}

	// Return no filter if all selected
	if (fdistricts.length == 9) {
		return null;
	}

	// Return selected items
	return fdistricts.toString();
}

/**
 *  Builds only the filter for weapons based on the checkboxes.
 */
function buildFilterForWeapons() {
	var fweapons = [];

	// Iterate through each checkbox and add to filter if checked
	if ($('#checkbox-hands').is(":checked")) {
		fweapons.push("HANDS");
	}
	if ($('#checkbox-knife').is(":checked")) {
		fweapons.push("KNIFE");
	}
	if ($('#checkbox-firearm').is(":checked")) {
		fweapons.push("FIREARM");
	}
	if ($('#checkbox-other').is(":checked")) {
		fweapons.push("OTHER");
	}
	if ($('#checkbox-unknown').is(":checked")) {
		fweapons.push("UNKNOWN");
	}

	// Return no filter if all selected
	if (fweapons.length == 5) {
		return null;
	}

	// Return selected items
	return fweapons.toString();
}

/**
 *  Formats and returns a filter string for time.
 */
function builtFilterForTime(timeString) {

	var timeSplit = timeString.val().split(' ');

	var time = timeSplit[0];
	var partOfDay = timeSplit[1];

	var hour = time.split(':')[0];
	var minute = time.split(':')[1];
	var second = "00";

	if (partOfDay == "AM" && parseInt(hour) == 12) {
		hour = "00";
	} else if (partOfDay == "PM") {
		hour = parseInt(hour) + 12;
	}

	return hour + ":" + minute + ":" + second;
}

function readDataBounds() {
	/**
	 *  The following block uses a GET endpoint to read the data bounds.
	 */
	$.ajax({
		headers: {
			'Accept': 'application/json',
		},
		url: bounds_url,
		type: 'GET',
		crossDomain: true,
		success: function (response) {

			// Set Slider Bounds
			$( "#date-slider-range" ).slider("option", "min", parseInt(response.minDate.split("-")[2]));
			$( "#date-slider-range" ).slider("option", "max", parseInt(response.maxDate.split("-")[2]));
			$( "#date-slider-range" ).slider("values", 0, parseInt(response.minDate.split("-")[2]));
			$( "#date-slider-range" ).slider("values", 1, parseInt(response.maxDate.split("-")[2]));
			$( "#date-range" ).val( "June " + $( "#date-slider-range" ).slider( "values", 0 ) +
				" - June " + $( "#date-slider-range" ).slider( "values", 1 ) );

			var minLat = parseFloat(response.minLatitude.substr(0, 6));
			var maxLat = parseFloat(response.maxLatitude.substr(0, 6)) + 0.001;
			$( "#lat-slider-range" ).slider("option", "min", minLat);
			$( "#lat-slider-range" ).slider("option", "max", maxLat);
			$( "#lat-slider-range" ).slider("values", 0, minLat);
			$( "#lat-slider-range" ).slider("values", 1, maxLat);
			$( "#lat-range" ).val($( "#lat-slider-range" ).slider( "values", 0 ) +
				" - " + $( "#lat-slider-range" ).slider( "values", 1 ) );

			var minLon = parseFloat(response.minLongitude.substr(0, 7));
			var maxLon = parseFloat(response.maxLongitude.substr(0, 7)) + 0.001;
			$( "#lon-slider-range" ).slider("option", "min", minLon);
			$( "#lon-slider-range" ).slider("option", "max", maxLon);
			$( "#lon-slider-range" ).slider("values", 0, minLon);
			$( "#lon-slider-range" ).slider("values", 1, maxLon);
			$( "#lon-range" ).val("(" + $( "#lon-slider-range" ).slider( "values", 0 ) +
				") - (" + $( "#lon-slider-range").slider( "values", 1 ) + ")");

			return;
		},
		error: function (response) {

		}
	})
}

function isBlank(str) {
	return (!str || /^\s*$/.test(str));
}

function isEmpty(array) {
	return (array == null || array.size == 0) ? true : false;
}

