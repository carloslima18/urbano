<?php
use dosamigos\google\maps\LatLng;
use dosamigos\google\maps\overlays\Marker;
use dosamigos\google\maps\Map;
?>


<div>
    <?php

    $array_nome = \app\models\Publicacaouser::find()->select('nome')->asArray()->all();
    $array_fotos = \app\models\Publicacaouser::find()->select('img1')->asArray()->all();
    $array_latitude = \app\models\Publicacaouser::find()->select('latitude')->asArray()->all();
    $array_longitude = \app\models\Publicacaouser::find()->select('longitude')->asArray()->all();

    //$latitude = \yii\helpers\ArrayHelper::map(\app\models\Publicacaouser::find()->orderBy('latitude','longitude')->all(), 'latitude', 'latitude');
    //$longitude = \yii\helpers\ArrayHelper::map(\app\models\Publicacaouser::find()->orderBy('longitude')->all(), 'longitude', 'longitude');
    // Now lets write a polygon


    $coord = [];
    $u = 0;
    $i = count($array_longitude);
  /*  while($u < $i){
        $yy = $array[$u]['latitude'];
        $aa = $array1[$u]['longitude'];
       // $coord = new LatLng(['lat' => $yy, 'lng' => $aa]);
        $u++;
    }*/

    //-------------------------------------------


    // Setting a default center coordinates for proper initialization
    $center = new LatLng(['lat' => $array_latitude[$u]['latitude'], 'lng' => $array_longitude[$u]['longitude']]);
    $map = new Map([
        'center' => $center,
        'zoom' => 5,
        'width' => '100%',
        'height' => 400,
        'containerOptions' => [
            'id' => 'artworkItemsMap'
        ]
    ]);


    foreach ($array_longitude as $place) {
        //$place['name'] = 'carlos teste';
        // Add markers
        //$name = (!empty($place['name'])) ? $place['name'] : Yii::t('frontend', 'Not set');



        if($u < 8){
            $filename_path = md5(time().uniqid()).".jpg";
            $decoded=base64_decode($array_fotos[$u]['img1']);
            //file_put_contents('C:/Users/carlo/Desktop/urbano/urbano/web/uploads/'.$filename_path,$decoded);
            $ft = 'C:/Users/carlo/Desktop/urbano/urbano/web/uploads/'.$filename_path;
            $yy = $array_latitude[$u]['latitude'];
            $aa = $array_longitude[$u]['longitude'];
            $nome = $array_nome[$u]['nome'];
            // $coord = new LatLng(['lat' => $yy, 'lng' => $aa]);


            $size = new \dosamigos\google\maps\Size([
                'height' => 400,'width' => 400
            ]);

            $icon = new \dosamigos\google\maps\overlays\Icon([
                'url' => 'data:image/jpeg;base64,' . $array_fotos[$u]['img1'],
                'scaledSize' => $size
            ]);


            $size = new \dosamigos\google\maps\Size([
                'height' => 400,'width' => 400
            ]);
            $coord = new LatLng(['lat' => $yy, 'lng' => $aa]);
            $marker = new Marker([
                'title' => 'carlero',
                'position' => $coord,
                'icon' => $icon//'data:image/jpeg;base64,' . $array_fotos[$u]['img1'] //\yii\helpers\Url::to('@uploads/'.$filename_path)
            ]);


/*            $point = new \dosamigos\google\maps\Point([
                'x' => 0,'y' => 0
            ]);

*/


        }
        $u++;




        $map->addOverlay($marker);
    }
 //   $boundsCenter = LatLngBounds::getBoundsOfMarkers($map->getMarkers())->getCenterCoordinates();
  //  $map->setCenter($boundsCenter);

    echo $map->display();

    //$this->registerJs("itemsMap = document.getElementById('artworkItemsMap'); setTimeout( function(){ google.maps.event.trigger(itemsMap, 'resize'); }, 400);");
    ?>

</div> <!-- end second col -->
