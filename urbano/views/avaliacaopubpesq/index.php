<?php

use yii\helpers\Html;
use yii\grid\GridView;

/* @var $this yii\web\View */
/* @var $searchModel app\models\AvaliacaopubpesqSearch */
/* @var $dataProvider yii\data\ActiveDataProvider */

$this->title = 'Avaliar publicação pesquisador';
$this->params['breadcrumbs'][] = $this->title;
?>
<div class="avaliacaopubpesq-index">

    <h1><?= Html::encode($this->title) ?></h1>
    <?php // echo $this->render('_search', ['model' => $searchModel]); ?>

    <p>
        <?= Html::a('Adicionar', ['create'], ['class' => 'btn btn-success']) ?>
    </p>

    <?= GridView::widget([
        'dataProvider' => $dataProvider,
        'filterModel' => $searchModel,
        'columns' => [
            ['class' => 'yii\grid\SerialColumn'],


            //'id',
            'nota',
            // 'idpubpesq',
            [
                'attribute'=>'idpubpesq',				//nome do campo prorpiamente dito,
                'value'=>function($m){//value e a função lambida, (para cada linha que vc ta processando para joga la na visualização, vc ta falando que para cada elemento chame de $m e retorne o valor (nome))
                    if($m->pubpesq->nome == null){
                        return "vazio";
                    }
                    return $m->pubpesq->nome;

                }
            ],

            ['class' => 'yii\grid\ActionColumn'],
        ],
    ]); ?>
</div>
