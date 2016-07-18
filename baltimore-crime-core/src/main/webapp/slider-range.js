/**
 * Created by Scott Robinson on 7/11/16.
 */

$( function() {
    $( "#date-slider-range" ).slider({
        range: true,
        min: 1,
        max: 18,
        values: [ 1, 18 ],
        step: 1,
        slide: function( event, ui ) {
            $( "#date-range" ).val( "June " + ui.values[ 0 ] + " - June " + ui.values[ 1 ] );
        }
    });
    $( "#date-range" ).val( "June " + $( "#date-slider-range" ).slider( "values", 0 ) +
        " - June " + $( "#date-slider-range" ).slider( "values", 1 ) );
} );

// $( function() {
//     $( "#time-slider-range" ).slider({
//         range: true,
//         min: 0,
//         max: 23.9999,
//         values: [ 0, 23.999 ],
//         step: 1,
//         slide: function( event, ui ) {
//             $( "#time-range" ).val(ui.values[ 0 ] + "H - " + ui.values[ 1 ] +"H" );
//         }
//     });
//     $( "#time-range" ).val($( "#time-slider-range" ).slider( "values", 0 ) +
//         "H - " + $( "#time-slider-range" ) + "H".slider( "values", 1 ) );
// } );

$( function() {
    $( "#lat-slider-range" ).slider({
        range: true,
        min: 39.21,
        max: 39.38,
        values: [ 39.21, 39.38 ],
        step: 0.005,
        slide: function( event, ui ) {
            $( "#lat-range" ).val(ui.values[ 0 ] + " - " + ui.values[ 1 ] );
        }
    });
    $( "#lat-range" ).val($( "#lat-slider-range" ).slider( "values", 0 ) +
        " - " + $( "#lat-slider-range" ).slider( "values", 1 ) );
} );

$( function() {
    $( "#lon-slider-range" ).slider({
        range: true,
        min: -76.720,
        max: -76.530,
        values: [ -76.720, -76.530 ],
        step: 0.005,
        slide: function( event, ui ) {
            $( "#lon-range" ).val("(" + ui.values[ 0 ] + ") - (" + ui.values[ 1 ] + ")");
        }
    });
    $( "#lon-range" ).val("(" + $( "#lon-slider-range" ).slider( "values", 0 ) +
        ") - (" + $( "#lon-slider-range").slider( "values", 1 ) + ")");
} );
