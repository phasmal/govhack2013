<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Open Data Web</title><!--
        <script type="text/javascript" src="OpenLayers-2.12/OpenLayers.debug.js"></script>
        <script type="text/javascript">
            function onLoad()
            {
                var map = new OpenLayers.Map('map')
                //http://demo.mapserver.org/cgi-bin/wms?
                
                var wms = new OpenLayers.Layer.WMS(
                    "OpenLayers WMS",
                    "http://vmap0.tiles.osgeo.org/wms/vmap0",
                    {'layers':'basic'} );
                  
                //var wms = new OpenLayers.Layer.WMS("Open Street Map", "http://demo.mapserver.org/cgi-bin/wms?")
                map.addLayer(wms)

                OpenLayers.Request.GET({
                    url:"api/region/801/children/children/type/19/time/2010",
                    success: function()
                    {
                        
                        console.log(arguments)
                    },
                    
                })
            }
        </script>-->
        <script src="//ajax.googleapis.com/ajax/libs/jquery/1.10.0/jquery.min.js"></script>
        <script type="text/javascript">
            $.ajax({
                url: 'api/region/801/children/children/type/19/time/2010',
                success: function(data)
                {
                    $('#table').append('<table>')
                    $('#table').append('<tr>')
                    $('#table').append('<th>name</th>')
                    $('#table').append('<th>value</th>')
                    $('#table').append('</tr>')
                    $.each(data, function(x, item)
                    {
                        $('#table').append('<tr>')
                        $('#table').append('<td>' + item.region.name + '</td>')
                        $('#table').append('<td>' + item.value + '</td>')
                        $('#table').append('</tr>')
                    })
                    
                    $('#table').append('</table>')
                }
            })
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
        </style>
        
    </head>
    <body>
        <h1>Open Data Web</h1>
        <!--<div style="width:100%; height:500px; display: block;" id="map"></div>-->
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
