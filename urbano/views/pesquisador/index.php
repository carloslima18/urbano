<?php

use yii\helpers\Html;
use yii\grid\GridView;

/* @var $this yii\web\View */
/* @var $searchModel app\models\PesquisadorSearch */
/* @var $dataProvider yii\data\ActiveDataProvider */

$this->title = 'Pesquisador';
$this->params['breadcrumbs'][] = $this->title;
?>
<div class="pesquisador-index">

    <h1><?= Html::encode($this->title) ?></h1>
    <?php // echo $this->render('_search', ['model' => $searchModel]); ?>

    <p>
        <?= Html::a('Adicionar Pesquisador', ['create'], ['class' => 'btn btn-success']) ?>
    </p>

    <?= GridView::widget([
        'dataProvider' => $dataProvider,
        'filterModel' => $searchModel,
        'columns' => [
            ['class' => 'yii\grid\SerialColumn'],

            // 'id',
            'nome:ntext',
            'email:ntext',
            //'senha:ntext',
            'campo1:ntext',
            'campo2:ntext',
            //'campo3:ntext',
            //'campo4:ntext',
            //'campo5:ntext',

            ['class' => 'yii\grid\ActionColumn'],
        ],
    ]); ?>
</div>
