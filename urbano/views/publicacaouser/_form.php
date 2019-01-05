<?php

use yii\helpers\Html;
use yii\widgets\ActiveForm;
use kartik\file\FileInput;

/* @var $this yii\web\View */
/* @var $model app\models\Publicacaouser */
/* @var $form yii\widgets\ActiveForm */
?>

<div class="publicacaouser-form">

    <?php $form = ActiveForm::begin(['options' => ['enctype' => 'multipart/form-data']]) ?>
    <!--?php $form = ActiveForm::begin(); ?-->

    <?= $form->field($model, 'nome')->textInput() ?>

    <?= $form->field($model, 'redesocial')->textInput() ?>

    <?= $form->field($model, 'endereco')->textInput() ?>

    <?= $form->field($model, 'contato')->textInput() ?>

    <?= $form->field($model, 'email')->textInput() ?>

    <?= $form->field($model, 'atvexercida')->textInput() ?>

    <?= $form->field($model, 'categoria')->textInput() ?>

    <?= $form->field($model, 'aprovado')->dropDownList([ 'S' => 'S', 'N' => 'N', ], ['prompt' => '']) ?>

    <!--?= $form->field($model, 'latitude')->textInput() ?-->

    <!--?= $form->field($model, 'longitude')->textInput() ?-->

    <!--?= $form->field($model, 'geo_gps')->textInput() ?-->

    <!--?= $form->field($model, 'img1')->textarea(['rows' => 6]) ?-->

    <!--?= $form->field($model, 'img2')->textarea(['rows' => 6]) ?-->

    <!--?= $form->field($model, 'img3')->textarea(['rows' => 6]) ?-->

    <!--?= $form->field($model, 'img4')->textarea(['rows' => 6]) ?-->

    <!--?= $form->field($model, 'campo1')->textarea(['rows' => 6]) ?>

    <!--?= $form->field($model, 'campo2')->textarea(['rows' => 6]) ?>

    <!--?= $form->field($model, 'campo3')->textarea(['rows' => 6]) ?>

    <!--?= $form->field($model, 'campo4')->textarea(['rows' => 6]) ?>

    <!--?= $form->field($model, 'campo5')->textarea(['rows' => 6]) ?>

    <!--?= $form->field($model, 'campo6')->textarea(['rows' => 6]) ?>

    <!--?= $form->field($model, 'campo7')->textarea(['rows' => 6]) ?>

    <!--?= $form->field($model, 'campo8')->textarea(['rows' => 6]) ?>

    <!--?= $form->field($model, 'campo9')->textarea(['rows' => 6]) ?>

    <!--?= $form->field($model, 'campo10')->textarea(['rows' => 6]) ?-->


    <?= $form->field($model, 'img1')->widget(FileInput::classname(),[
            'options' => ['accept' => 'image/*','multiple'=>true],
        ]
    ) ?>

    <?= $form->field($model, 'img2')->widget(FileInput::classname(),[
            'options' => ['accept' => 'image/*'],
        ]
    ) ?>

    <?= $form->field($model, 'img3')->widget(FileInput::classname(),[ //33028852
            'options' => ['accept' => 'image/*'],
        ]
    ) ?>

    <?= $form->field($model, 'img4')->widget(FileInput::classname(),[
            'options' => ['accept' => 'image/*'],
        ]
    ) ?>

    <!--https://github.com/kalyabin/yii2-select-google-map-location-->
    <?= $form->field($model, 'campo5' )->widget(\kalyabin\maplocation\SelectMapLocationWidget::className(), [
        'attributeLatitude' => 'latitude',
        'attributeLongitude' => 'longitude',
        'googleMapApiKey' => 'AIzaSyDpcX-GScOiQX3IxBAu_Drpet-YqjM8t0U',
        'draggable' => true,
        ]);  ?>


    <div class="form-group">
        <?= Html::submitButton('Salvar', ['class' => 'btn btn-success']) ?>
    </div>

    <?php ActiveForm::end(); ?>

</div>3