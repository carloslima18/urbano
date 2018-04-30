<?php

use yii\helpers\Html;
use yii\helpers\Url;
use yii\widgets\DetailView;

/* @var $this yii\web\View */
/* @var $model app\models\Avaliacaopubpesq */

$this->title = $model->pubpesq->nome;
$this->params['breadcrumbs'][] = ['label' => 'Avaliação pub. pesquisador', 'url' => ['index']];
$this->params['breadcrumbs'][] = $this->title;
?>
<div class="avaliacaopubpesq-view">

    <h1><?= Html::encode($this->title) ?></h1>

    <p>
        <?= Html::a('Update', ['update', 'id' => $model->id], ['class' => 'btn btn-primary']) ?>
        <?= Html::a('Delete', ['delete', 'id' => $model->id], [
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
            //'id',
            'nota',
            //'idpubpesq',
            ['label'=>'pub pesquisador',
                'format'=>'raw',      								                                                                  //nome do campo do atributo       	//para nao fazer uma checagem, forma cru
                'value'=>Html::a($model->pubpesq->nome,			                                                                   //OBS:: crefitoFisioterapeuta -> app/models(propriedade usando como se fosse um metodo, metodos get e set's)------  crefito_Fisioterapeuta -> propriedade do banco de dados  ----- e quando nao tem essa forma adiciona um 0 no final. (tira o 0, e coloca s, e troca tbm no comentario inicial da propriedade)	//'value'=> $model=>idEdital!=null? Html::a($model=>idEdital->identificacao,
                    Url::to(['publicacaopesq/view','id'=>$model->pubpesq->id]))	                                                            	//link para redirecionar ao clik, para usar a Url, coloca:: USE yii\helpers\Url; e caso n ter coloque use yii\helpers\Html;
            ],
        ],
    ]) ?>

</div>
