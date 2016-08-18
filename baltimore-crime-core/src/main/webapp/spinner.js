
$( function() {

    /**
     * Time Start Spinner
     */
    $.widget( "ui.timespinner", $.ui.spinner, {
        options: {
            // seconds
            step: 60 * 1000,
            // hours
            page: 60
        },

        _parse: function( value ) {
            if ( typeof value === "string" ) {
                // already a timestamp
                if ( Number( value ) == value ) {
                    return Number( value );
                }
                return +Globalize.parseDate( value );
            }
            return value;
        },

        _format: function( value ) {
            return Globalize.format( new Date(value), "t" );
        }
    });

    $( "#time-start-spinner" ).timespinner();

    $( "#culture" ).on( "change", function() {
        var current = $( "#time-start-spinner" ).timespinner( "value" );
        Globalize.culture( $(this).val() );
        $( "#time-start-spinner" ).timespinner( "value", current );
        getData(buildFilter());

    });

    /**
     * Time End Spinner
     */
    $.widget( "ui.timespinner", $.ui.spinner, {
        options: {
            // seconds
            step: 60 * 1000,
            // hours
            page: 60
        },

        _parse: function( value ) {
            if ( typeof value === "string" ) {
                // already a timestamp
                if ( Number( value ) == value ) {
                    return Number( value );
                }
                return +Globalize.parseDate( value );
            }
            return value;
        },

        _format: function( value ) {
            return Globalize.format( new Date(value), "t" );
        }
    });

    $( "#time-end-spinner" ).timespinner();

    $( "#culture" ).on( "change", function() {
        var current = $( "#time-end-spinner" ).timespinner( "value" );
        Globalize.culture( $(this).val() );
        $( "#time-end-spinner" ).timespinner( "value", current );
    });

} );

