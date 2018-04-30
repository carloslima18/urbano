<?php

use yii\helpers\Html;

/* @var $this yii\web\View */
/* @var $model app\models\Avaliacaopubpesq */


$this->title = 'Alterar avl:'.$model->pubpesq->nome;
$this->params['breadcrumbs'][] = ['label' => 'Avaliação', 'url' => ['index']];
$this->params['breadcrumbs'][] = ['label' => $model->id, 'url' => ['view', 'id' => $model->id]];
$this->params['breadcrumbs'][] = 'Alterar';
?>
<div class="avaliacaopubpesq-update">

    <h1><?= Html::encode($this->title) ?></h1>

    <?= $this->render('_form', [
        'model' => $model,
    ]) ?>

</div>
