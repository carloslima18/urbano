<?php

namespace app\modules\api\controllers;
use app\models\Publicacaouser;
use yii\rest\ActiveController;

/**
 * Default controller for the `api` module
 */
class DefaultController extends ActiveController
{
    public $modelClass = 'app\models\Publicacaouser';

    /**
     * Renders the index view for the module
     * @return string
     */
  /*  public function actionIndex()
    {
        return $this->render('index');
    } */
}
