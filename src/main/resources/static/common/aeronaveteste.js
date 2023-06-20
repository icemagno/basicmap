

var airplane = null;
var surfacEntity = null;

function processaAeronave( payload ){
	var thePosition = Cesium.Cartesian3.fromDegrees( payload.lon, payload.lat, 10000 );					

	plotaOnGround( payload );

	if( airplane != null ){
		airplane.position = thePosition;
		return;
	}
	
	airplane = new Cesium.Entity({
		position: thePosition,
		show: true,
		model: {
			uri: 'common/models/air.glb',
			minimumPixelSize : 128,
			maximumScale : 500,
		}
	});
	
	viewer.entities.add( airplane );
	viewer.trackedEntity = airplane;	
	
}


function plotaOnGround( payload ){
	var thePosition = Cesium.Cartesian3.fromDegrees( payload.lon, payload.lat, 5 );	
	
	if( surfacEntity != null ){
		surfacEntity.position = thePosition;
		return;
	}	
	
	let svgUrl = "common/img/arcanjo-tn.png";
	
	surfacEntity = new Cesium.Entity({
		name : "SURFACE_C",
		position: thePosition,
		billboard: {
			image: svgUrl,
            eyeOffset: new Cesium.Cartesian3(0.0, 0.0, 0.0),
            horizontalOrigin: Cesium.HorizontalOrigin.CENTER,
            verticalOrigin: Cesium.VerticalOrigin.BOTTOM,
            scaleByDistance : new Cesium.NearFarScalar(1.5e2, 0.3, 8.5e7, 0.03),
            disableDepthTestDistance : Number.POSITIVE_INFINITY, 
            heightReference : Cesium.HeightReference.CLAMP_TO_GROUND,
		}
	});	
	
	viewer.entities.add( surfacEntity );	
}