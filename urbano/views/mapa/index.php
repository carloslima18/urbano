<?php
/**
 * Created by PhpStorm.
 * User: carlo
 * Date: 19/11/2018
 * Time: 00:45
 */
use yii\helpers\Html;
use yii\widgets\DetailView;


use dosamigos\google\maps\LatLng;
use dosamigos\google\maps\services\DirectionsWayPoint;
use dosamigos\google\maps\services\TravelMode;
use dosamigos\google\maps\overlays\PolylineOptions;
use dosamigos\google\maps\services\DirectionsRenderer;
use dosamigos\google\maps\services\DirectionsService;
use dosamigos\google\maps\overlays\InfoWindow;
use dosamigos\google\maps\overlays\Marker;
use dosamigos\google\maps\Map;

use dosamigos\google\maps\services\DirectionsRequest;
use dosamigos\google\maps\overlays\Polygon;
use dosamigos\google\maps\layers\BicyclingLayer;





?>


<div class="col-md-6">
    <?php


    $array_id = \app\models\Publicacaouser::find()->select('id')->asArray()->all();
    $array_cat = \app\models\Publicacaouser::find()->select('categoria')->asArray()->all();
    $array_nome = \app\models\Publicacaouser::find()->select('nome')->asArray()->all();
    //$array_fotos = \app\models\Publicacaouser::find()->select('img1')->asArray()->all();
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
        'zoom' => 7,
        'width' => '200%',
        'height' => 700,
        'containerOptions' => [
            'id' => 'artworkItemsMap'
        ]
    ]);


    foreach ($array_longitude as $place) {
        //$place['name'] = 'carlos teste';
        // Add markers
        //$name = (!empty($place['name'])) ? $place['name'] : Yii::t('frontend', 'Not set');



        if($u < count($array_longitude)){
            //$filename_path = md5(time().uniqid()).".jpg";
            //$decoded=base64_decode($array_fotos[$u]['img1']);
            //file_put_contents('C:/Users/carlo/Desktop/urbano/urbano/web/uploads/'.$filename_path,$decoded);
            //$ft = 'C:/Users/carlo/Desktop/urbano/urbano/web/uploads/'.$filename_path;
            $yy = $array_latitude[$u]['latitude'];
            $aa = $array_longitude[$u]['longitude'];
            $nome = $array_cat[$u]['categoria'];
            $nome_real = $array_nome[$u]['nome'];
            $nome_id = $array_id[$u]['id'];
            // $coord = new LatLng(['lat' => $yy, 'lng' => $aa]);


            $size = new \dosamigos\google\maps\Size([
                'height' => 5,'width' => 5
            ]);

           // $icon = new \dosamigos\google\maps\overlays\Icon([
           //     'url' => 'data:image/jpeg;base64,' . $array_fotos[$u]['img1'],
           //     'scaledSize' => $size
           // ]);


            $size = new \dosamigos\google\maps\Size([
                'height' => 5,'width' => 5
            ]);
            ?>
                <style>
                    p {color:blue;
                    font-size: 16px}
                </style>
            <?php
            $coord = new LatLng(['lat' => $yy, 'lng' => $aa]);

            $marker = new Marker([
                'title' => $nome_id,
                'position' => $coord,
                'label' => $nome,

                //'icon' => 'data:image/jpeg;base64,' . $array_fotos[$u]['img1'] //\yii\helpers\Url::to('@uploads/'.$filename_path)
            ]);

/*            $point = new \dosamigos\google\maps\Point([
                'x' => 0,'y' => 0
            ]);

*/
            $link = "http://orbeapp.com/web/publicacaouser/view?id=$nome_id";
            $marker->attachInfoWindow(
                new InfoWindow([
                    'content' => '<a href='.$link.'>'.$nome_real.'.</a>'
                ])
            );


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


