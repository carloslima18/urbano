<?php

use yii\helpers\Html;
use yii\grid\GridView;

/* @var $this yii\web\View */
/* @var $searchModel app\models\PublicacaouserSearch */
/* @var $dataProvider yii\data\ActiveDataProvider */

$this->title = 'Publicação de usuários';
$this->params['breadcrumbs'][] = $this->title;
?>
<div class="publicacaouser-index">

    <h1><?= Html::encode($this->title) ?></h1>
    <?php // echo $this->render('_search', ['model' => $searchModel]); ?>

    <p>
        <?= Html::a('Adicionar', ['create'], ['class' => 'btn btn-success']) ?>
    </p>

    <?php

    echo \kartik\export\ExportMenu::widget([
        'dataProvider'=>$dataProvider,
        'columns'=> [

            'nome:ntext',
            'redesocial:ntext',
            'endereco:ntext',
            'contato:ntext',

            ['class' => 'yii\grid\ActionColumn'],
        ],
    ]);

    echo GridView::widget([
        'dataProvider' => $dataProvider,
        'filterModel' => $searchModel,
        'columns' => [
            ['class' => 'yii\grid\SerialColumn'],

            //'id',
            'nome:ntext',
            'redesocial:ntext',
            'endereco:ntext',
            'contato:ntext',
            //'email:ntext',
            //'atvexercida:ntext',
            //'categoria:ntext',
            //'aprovado',
            //'latitude',
            //'longitude',
            //'geo_gps',
            //'img1:ntext',
            //'img2:ntext',
            //'img3:ntext',
            //'img4:ntext',
            //'campo1:ntext',
            //'campo2:ntext',
            //'campo3:ntext',
            //'campo4:ntext',
            //'campo5:ntext',
            //'campo6:ntext',
            //'campo7:ntext',
            //'campo8:ntext',
            //'campo9:ntext',
            //'campo10:ntext',

            ['class' => 'yii\grid\ActionColumn'],
        ],
    ]); ?>
</div>
