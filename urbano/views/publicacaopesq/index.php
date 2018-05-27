<?php

use yii\helpers\Html;
use yii\grid\GridView;

/* @var $this yii\web\View */
/* @var $searchModel app\models\PublicacaopesqSearch */
/* @var $dataProvider yii\data\ActiveDataProvider */

$this->title = 'Publicação de pesquisadores';
$this->params['breadcrumbs'][] = $this->title;
?>
<div class="publicacaopesq-index">

    <h1><?= Html::encode($this->title) ?></h1>
    <?php // echo $this->render('_search', ['model' => $searchModel]); ?>

    <p>
        <?= Html::a('Adicionar', ['create'], ['class' => 'btn btn-success']) ?>
    </p>
    <?php

    echo \kartik\export\ExportMenu::widget([
        'dataProvider'=>$dataProvider,
        'columns'=> [
            //'id',
            'nome:ntext',
            'contato:ntext',
            'endereco:ntext',
            'email:ntext',
            'redesocial:ntext',
            'atvexercida:ntext',
            'categoria:ntext',
            'anoinicio:ntext',
            //'cnpj:ntext',
            //'representacao:ntext',
            //'recurso:ntext',
            'aprovado:ntext',

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
            'contato:ntext',
            'endereco:ntext',
            'email:ntext',
            'redesocial:ntext',
            'atvexercida:ntext',
            'categoria:ntext',
            'anoinicio:ntext',
            'cnpj:ntext',
            //'representacao:ntext',
            //'recurso:ntext',
             'aprovado:ntext',
            //'latitude',
            //'longitude',
            //'geo_gps',
            //'pesquisador',
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
            //'campo11:ntext',
            //'campo12:ntext',
            //'campo13:ntext',
            //'campo14:ntext',
            //'campo15:ntext',
            //'campo16:ntext',
            //'campo17:ntext',
            //'campo18:ntext',
            //'campo19:ntext',
            //'campo20:ntext',

            ['class' => 'yii\grid\ActionColumn'],
        ],
    ]); ?>
</div>
