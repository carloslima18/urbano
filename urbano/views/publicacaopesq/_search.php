<?php

use yii\helpers\Html;
use yii\widgets\ActiveForm;

/* @var $this yii\web\View */
/* @var $model app\models\PublicacaopesqSearch */
/* @var $form yii\widgets\ActiveForm */
?>

<div class="publicacaopesq-search">

    <?php $form = ActiveForm::begin([
        'action' => ['index'],
        'method' => 'get',
    ]); ?>

    <!--?= $form->field($model, 'id') ?-->

    <?= $form->field($model, 'nome') ?>

    <?= $form->field($model, 'redesocial') ?>

    <?= $form->field($model, 'endereco') ?>

    <?= $form->field($model, 'contato') ?>

    <?php // echo $form->field($model, 'email') ?>

    <?php // echo $form->field($model, 'atvexercida') ?>

    <?php // echo $form->field($model, 'categoria') ?>

    <?php // echo $form->field($model, 'anoinicio') ?>

    <?php // echo $form->field($model, 'cnpj') ?>

    <?php // echo $form->field($model, 'representacao') ?>

    <?php // echo $form->field($model, 'recurso') ?>

    <?php // echo $form->field($model, 'aprovado') ?>

    <?php // echo $form->field($model, 'latitude') ?>

    <?php // echo $form->field($model, 'longitude') ?>

    <?php // echo $form->field($model, 'geo_gps') ?>

    <?php // echo $form->field($model, 'pesquisador') ?>

    <?php // echo $form->field($model, 'img1') ?>

    <?php // echo $form->field($model, 'img2') ?>

    <?php // echo $form->field($model, 'img3') ?>

    <?php // echo $form->field($model, 'img4') ?>

    <?php // echo $form->field($model, 'campo1') ?>

    <?php // echo $form->field($model, 'campo2') ?>

    <?php // echo $form->field($model, 'campo3') ?>

    <?php // echo $form->field($model, 'campo4') ?>

    <?php // echo $form->field($model, 'campo5') ?>

    <?php // echo $form->field($model, 'campo6') ?>

    <?php // echo $form->field($model, 'campo7') ?>

    <?php // echo $form->field($model, 'campo8') ?>

    <?php // echo $form->field($model, 'campo9') ?>

    <?php // echo $form->field($model, 'campo10') ?>

    <?php // echo $form->field($model, 'campo11') ?>

    <?php // echo $form->field($model, 'campo12') ?>

    <?php // echo $form->field($model, 'campo13') ?>

    <?php // echo $form->field($model, 'campo14') ?>

    <?php // echo $form->field($model, 'campo15') ?>

    <?php // echo $form->field($model, 'campo16') ?>

    <?php // echo $form->field($model, 'campo17') ?>

    <?php // echo $form->field($model, 'campo18') ?>

    <?php // echo $form->field($model, 'campo19') ?>

    <?php // echo $form->field($model, 'campo20') ?>

    <div class="form-group">
        <?= Html::submitButton('Search', ['class' => 'btn btn-primary']) ?>
        <?= Html::resetButton('Reset', ['class' => 'btn btn-default']) ?>
    </div>

    <?php ActiveForm::end(); ?>

</div>
