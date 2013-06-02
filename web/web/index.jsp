<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Open Data Web</title>
<!--        <script src="http://maps.google.com/maps/api/js?v=3.2&sensor=false&callback=googleMapsLoaded"></script>-->
<!--        <script type="text/javascript" src="OpenLayers-2.12/OpenLayers.debug.js"></script>
        <script type="text/javascript">
        </script>-->
        <script src="//ajax.googleapis.com/ajax/libs/jquery/1.10.0/jquery.min.js"></script>
        <script type="text/javascript">
//            function googleMapsLoaded()
//            {
                $(document).ready(function()
                {
//                    var map = new OpenLayers.Map('map')
//
//                    var wms = new OpenLayers.Layer.WMS( "OpenLayers WMS",
//                            "http://vmap0.tiles.osgeo.org/wms/vmap0",
//                            {layers: 'basic'} );
//                            
//                    map.addLayer(wms)
                    //http://demo.mapserver.org/cgi-bin/wms?

//                    var google = new OpenLayers.Layer.Google(
//                        "Google Hybrid",
//                        {
//                            type: google.maps.MapTypeId.HYBRID,
//                            numZoomLevels: 20
//                        })
//
//                    //var wms = new OpenLayers.Layer.WMS("Open Street Map", "http://demo.mapserver.org/cgi-bin/wms?")
//                    map.addLayer(google)
                            
                    $.ajax({
                        url: 'api/region/801/children/children/type/19/time/2010',
                        success: function(data)
                        {
                            $('#table').append('<table>')
                            $('#table').append('<tr>')
                            $('#table').append('<th>LGA</th>')
                            $('#table').append('<th>Unemployed Persons</th>')
                            $('#table').append('</tr>')
                            $.each(data, function(x, item)
                            {
                                $('#table').append('<tr>')
                                $('#table').append('<td>' + item.region.name + '</td>')
                                $('#table').append('<td>' + item.value + '</td>')
                                $('#table').append('</tr>')
                            })

                            $('#table').append('</table>')
//                            $.each(data, function(x, item)
//                            {
//                                $.getJSON(item.region.shape.jsonp + "&callback=?", null, function(data) {
//                                    
//                                    console.log('loading data for ' + item.region.name)
//                                    //console.log(data.features)
//                                    if (data.features)
//                                    {
//                                        $.each(data.features, function(x, feature) 
//                                        {
//                                            //console.log('feature', feature)
//                                            if (feature.geometry && feature.geometry.rings)
//                                            {
//                                                var name = item.region.name + '-' + feature.attributes.SLA_NAME
//                                                console.log('feature='+name)
//                                                var rings = feature.geometry.rings
//
//                                                $.each(rings, function(x, ring)
//                                                {
//                                                    var points = []
//                                                    $.each(ring, function(x, point)
//                                                    {
//                                                        console.log('loading', point)
//                                                        points.push(new OpenLayers.Geometry.Point(point[0], point[1]))
//                                                    })
//
//                                                    var ring = new OpenLayers.Geometry.LinearRing(points)
//                                                    var polygon = new OpenLayers.Geometry.Polygon([ring])
//
//                                                    var attributes = {name: name}
//
//                                                    var feature = new OpenLayers.Feature.Vector(polygon, attributes)
//                                                    var layer = new OpenLayers.Layer.Vector(name)
//                                                    layer.addFeatures([feature])
//                                                    map.addLayer(layer)
//                                                })
//                                            }
//                                        })
//                                    }
//                                })
//                            })
                        }
                    })
                })
//            }
        </script>
        <style type="text/css">
            h1
            {
                text-align: center;
            }
            
            #table
            {
                width: 500px;
                margin: 2em;
                margin-right:auto;
                margin-left: auto;
                border: #003a6b solid 1px;
            }
            
            #table td, 
            #table th
            {
                padding: 0.5em;
            }
            
            #table th
            {
                border-bottom: #003a6b solid 1px;
            }
            
            .olMap
            {
                width: 100%;
                height: 500px;
            }
        </style>
        
    </head>
    <body>
        <h1>Open Data Web</h1>
<!--        <div style="width:100%; height:500px; display: block;" id="map"></div>-->
        <div id="table"></div>
        <div style="font-size: small; font-align: center; display: block; width: 100%;">
            Data from <a href="http://stat.abs.gov.au/Index.aspx">stat.abs.gov.au</a>.
        </div>
        <!--
        <div style="font-size: small;">
            Some data © OpenStreetMap contributors, 
            <a href="http://www.openstreetmap.org/copyright">license</a>.
        </div>
        -->
    </body>
</html>
