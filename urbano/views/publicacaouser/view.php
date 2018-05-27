<?php

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


/* @var $this yii\web\View */
/* @var $model app\models\Publicacaouser */

$this->title = $model->nome;
$this->params['breadcrumbs'][] = ['label' => 'Publicacaousers', 'url' => ['index']];
$this->params['breadcrumbs'][] = $this->title;
?>
<div class="publicacaouser-view">

    <h1><?= Html::encode($this->title) ?></h1>

    <p>
        <?= Html::a('Alterar', ['update', 'id' => $model->id], ['class' => 'btn btn-primary']) ?>
        <?= Html::a('Deletar', ['delete', 'id' => $model->id], [
            'class' => 'btn btn-danger',
            'data' => [
                'confirm' => 'Are you sure you want to delete this item?',
                'method' => 'post',
            ],
        ]) ?>
    </p>



    <?= DetailView::widget([
        'model' => $model,
        'attributes' => [
            //  'id',
            'nome:ntext',
            'redesocial:ntext',
            'endereco:ntext',
            'contato:ntext',
            'email:ntext',
            'atvexercida:ntext',
            'categoria:ntext',
            'aprovado:ntext',
         //   'latitude',
         //   'longitude',
            //  'geo_gps',

            //    [//new FileCache(['cachePath' => '@runtime/cache', //para salvar a foto em uploads

            /*  define('UPLOAD_DIR','C:/Users/carlo/Desktop/curso de ingles/ProjetoCulturalAps/cult/uploads/'),
              $data = base64_decode($model->img1),
              $file = UPLOAD_DIR . uniqid() . '.jpg',
              $success = file_put_contents($file, $data), */


            /* $filename_path = md5(time().uniqid()).".jpg",
             $decoded=base64_decode($model->img1),
             file_put_contents('C:/Users/carlo/Desktop/curso de ingles/ProjetoCulturalAps/cult/uploads/'.$filename_path,$decoded),
            */

            //  ],
            // ['label'=>'img1', 'value'=>'C:/Users/carlo/Desktop/curso de ingles/ProjetoCulturalAps/cult/uploads/'.$model->img1.'jpg'],


            /*//nao funciona mais éra para carrega a img a partir co caminho
                [   'attribute'=>'img2',
                    'value'=>Html::img('@web/uploads/teste.jpg', ['alt'=>'some', 'class'=>'thing']),
                    'format'=>['image',['width'=>'100','height'=>'100']],
                ],
                ['attribute'=>'img3',
                    'format'=>'raw',
                    'value'=>Html::img('@web/uploads/teste.jpg', ['alt'=>'some', 'class'=>'thing']),
                ],
            */

            [
                'attribute' => 'img1',
                'value' => 'data:image/jpeg;base64,' . $model->img1,
                'format' => ['image', ['width' => '200', 'height' => '200']]
            ],
            [
                'attribute' => 'img2',
                'value' => 'data:image/jpeg;base64,' . $model->img2,
                'format' => ['image', ['width' => '200', 'height' => '200']]
            ],
            [
                'attribute' => 'img3',
                'value' => 'data:image/jpeg;base64,' . $model->img3,
                'format' => ['image', ['width' => '200', 'height' => '200']]
            ],
            [
                'attribute' => 'img4',
                'value' => 'data:image/png;base64,' . $model->img4,
                'format' => ['image', ['width' => '200', 'height' => '200']]
            ],
        /*    'campo1:ntext',
            'campo2:ntext',
            'campo3:ntext',
            'campo4:ntext',
            'campo5:ntext',
            'campo6:ntext',
            'campo7:ntext',
            'campo8:ntext',
            'campo9:ntext',
            'campo10:ntext', */
        ],
    ]) ?>




</div>

<div class="col-md-6">
    <?php
    $coord = new LatLng(['lat' => $model->latitude, 'lng' => $model->longitude]);
    $map = new Map([
        'center' => $coord,
        'zoom' => 14,
    ]);

    // lets use the directions renderer
//    $home = new LatLng(['lat' => -16.347922, 'lng' => -48.9730085]);
//    $vaipassarpor = new LatLng(['lat' => -16.3277793, 'lng' => -48.9594287]);
   // $santo_domingo = new LatLng(['lat' => $model->latitude, 'lng' => $model->longitude]);

    // setup just one waypoint (Google allows a max of 8)
//    $waypoints = [
//        new DirectionsWayPoint(['location' => $vaipassarpor])
//    ];

 /*   $directionsRequest = new DirectionsRequest([
        'origin' => $home,
        'destination' => $coord,
        'waypoints' => $waypoints,
        'travelMode' => TravelMode::DRIVING
    ]); */

    // Lets configure the polyline that renders the direction
 /*   $polylineOptions = new PolylineOptions([
        'strokeColor' => '#FFAA00',
        'draggable' => true
    ]); */

    // Now the renderer
   /* $directionsRenderer = new DirectionsRenderer([
        'map' => $map->getName(),
        'polylineOptions' => $polylineOptions
    ]); */

    // Finally the directions service
  /*  $directionsService = new DirectionsService([
        'directionsRenderer' => $directionsRenderer,
       // 'directionsRequest' => $directionsRequest
    ]); */

    // Thats it, append the resulting script to the map
   // $map->appendScript($directionsService->getJs());

    // Lets add a marker now
    $marker = new Marker([
        'position' => $coord,
        'title' => 'My Home Town',
    ]);

    // Provide a shared InfoWindow to the marker
    $marker->attachInfoWindow(
        new InfoWindow([
            'content' => $model->nome
        ])
    );

    // Add marker to the map
    $map->addOverlay($marker);







    // Now lets write a polygon
    $coords = [
        new LatLng(['lat' => 25.774252, 'lng' => -80.190262]),
        new LatLng(['lat' => 18.466465, 'lng' => -66.118292]),
        new LatLng(['lat' => 32.321384, 'lng' => -64.75737]),
        new LatLng(['lat' => 25.774252, 'lng' => -80.190262])
    ];

    $polygon = new Polygon([
        'paths' => $coords
    ]);

    // Add a shared info window
    $polygon->attachInfoWindow(new InfoWindow([
        'content' => '<p>This is my super cool Polygon</p>'
    ]));

    // Add it now to the map
    $map->addOverlay($polygon);


    // Lets show the BicyclingLayer :)
    $bikeLayer = new BicyclingLayer(['map' => $map->getName()]);

    // Append its resulting script
    $map->appendScript($bikeLayer->getJs());

    // Display the map -finally :)
    echo $map->display();
    ?>

</div> <!-- end second col -->
