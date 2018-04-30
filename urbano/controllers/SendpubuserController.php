<?php
/**
 * Created by PhpStorm.
 * User: carlo
 * Date: 26/02/2018
 * Time: 16:00
 */

namespace app\controllers;
use app\models\Publicacaouser;
use Yii;
use yii\rest\ActiveController;
use yii\data\Pagination;


class SendpubuserController extends ActiveController
{
    private $format = 'json';
    public $modelClass = 'app\models\Publicacaouser';

    /**
     * @param $id
     * @return array
     */
    public function actions() {
        $actions = parent::actions();
        $actions['index']['prepareDataProvider'] = [$this, 'prepareDataProvider'];
        return $actions;
    }

    public function prepareDataProvider() {
        $searchModel = new \app\models\PublicacaouserSearch();
        return $searchModel->search(\Yii::$app->request->queryParams);
    }

  /*  public function savefoto($image){
        define('UPLOAD_DIR','C:/Users/carlo/Desktop/curso de ingles/ProjetoCulturalAps/urbano/web/uploads/');
        $data1 = base64_decode($image);
        $file = UPLOAD_DIR . uniqid() . '.jpg';
        $success = file_put_contents($file, $data1);
        return $file;
    } */
    /**
     * Creates a new Publicacaouser model.
     * If creation is successful, the browser will be redirected to the 'view' page.
     * @return mixed
     */


    /*public function createAction($id){
        \Yii::$app->response->format = \yii\web\Response::FORMAT_JSON;
        $pub = new Publicacaouser();
        $pub->scenario = Publicacaouser::SCENARIO_CREATE;
        $pub->attributes = \Yii::$app->request->post();

        if($pub->validate()){
            return array('status'=>true);
        }else{
            return array('status'=>false,'data'=>$pub->getErrors());
        }
    }*/

    /* public function actionIndex()
     {
         $query = Publicacaouser::find();
         $countQuery = clone $query;
         $pages = new Pagination(['totalCount' => $countQuery->count()]);
         $models = $query->offset($pages->offset)
             ->limit($pages->limit)
             ->all();

         return $this->render('index', [
             'models' => $models,
             'pages' => $pages,
         ]);
     }*/
}