<?php

use yii\helpers\Html;
use yii\widgets\DetailView;

/* @var $this yii\web\View */
/* @var $model app\models\Pesquisador */

$this->title = $model->nome;
$this->params['breadcrumbs'][] = ['label' => 'Pesquisadors', 'url' => ['index']];
$this->params['breadcrumbs'][] = $this->title;
?>
<div class="pesquisador-view">

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
            'email:ntext',
           // 'senha:ntext',
            'campo1:ntext',
            'campo2:ntext',
          //  'campo3:ntext',
          //  'campo4:ntext',
          //  'campo5:ntext',
        ],
    ]) ?>

</div>
