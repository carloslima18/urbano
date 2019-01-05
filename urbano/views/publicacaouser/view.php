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
                'height' => 50,'width' => 50
            ]);

            $icon = new \dosamigos\google\maps\overlays\Icon([
                'url' => 'data:image/jpeg;base64,' . $array_fotos[$u]['img1'],
                'scaledSize' => $size
            ]);


            $size = new \dosamigos\google\maps\Size([
                'height' => 100,'width' => 100
            ]);
            $coord = new LatLng(['lat' => $yy, 'lng' => $aa]);
            $marker = new Marker([
                'title' => $nome,
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
