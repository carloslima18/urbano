<?php

use yii\helpers\Html;
use yii\widgets\ActiveForm;

/* @var $this yii\web\View */
/* @var $model app\models\Pesquisador */
/* @var $form yii\widgets\ActiveForm */
?>

<div class="pesquisador-form">

    <?php $form = ActiveForm::begin(); ?>

    <?= $form->field($model, 'nome')->textInput() ?>

    <?= $form->field($model, 'email')->textInput() ?>

    <?= $form->field($model, 'senha')->passwordInput() ?> <!-- a senha pode ser usada como o cpf -->

   <?= $form->field($model, 'campo1')->textInput() ?> <!--destinado para cpf-->

    <?= $form->field($model, 'campo2')->dropDownList([ 'S' => 'S', 'N' => 'N', ], ['prompt' => '']) ?>

    <!--?= $form->field($model, 'campo3')->textarea(['rows' => 6]) ?>

    <!--?= $form->field($model, 'campo4')->textarea(['rows' => 6]) ?>

    <!--?= $form->field($model, 'campo5')->textarea(['rows' => 6]) ?-->


    <div class="form-group">
        <?= Html::submitButton('Salvar', ['class' => 'btn btn-success']) ?>
    </div>

    <?php ActiveForm::end(); ?>

</div>
