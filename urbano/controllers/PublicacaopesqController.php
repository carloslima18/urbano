<?php

namespace app\controllers;

use app\models\Avaliacaopubpesq;
use Yii;
use app\models\Publicacaopesq;
use app\models\PublicacaopesqSearch;
use yii\web\Controller;
use yii\web\NotFoundHttpException;
use yii\filters\VerbFilter;
use yii\web\UploadedFile;

/**
 * PublicacaopesqController implements the CRUD actions for Publicacaopesq model.
 */
class PublicacaopesqController extends Controller
{
    /**
     * @inheritdoc
     */
    public function behaviors()
    {
        return [
            'verbs' => [
                'class' => VerbFilter::className(),
                'actions' => [
                    'delete' => ['POST'],
                ],
            ],
            'ghost-access'=> [
                'class' => 'webvimark\modules\UserManagement\components\GhostAccessControl',
            ],
        ];
    }

    /**
     * Lists all Publicacaopesq models.
     * @return mixed
     */
    public function actionIndex()
    {
        $searchModel = new PublicacaopesqSearch();
        $dataProvider = $searchModel->search(Yii::$app->request->queryParams);

        return $this->render('index', [
            'searchModel' => $searchModel,
            'dataProvider' => $dataProvider,
        ]);
    }

    /**
     * Displays a single Publicacaopesq model.
     * @param integer $id
     * @return mixed
     * @throws NotFoundHttpException if the model cannot be found
     */
    public function actionView($id)
    {
        return $this->render('view', [
            'model' => $this->findModel($id),
        ]);
    }

    public function getphotos($model,$oldAttributes){
        $path1 =  UploadedFile::getInstance($model, 'img1');
        $path2 =  UploadedFile::getInstance($model, 'img2');
        $path3 =  UploadedFile::getInstance($model, 'img3');
        $path4 =  UploadedFile::getInstance($model, 'img4');

        //$type = pathinfo($path, PATHINFO_EXTENSION);


        if($path1 != null && $path1->tempName != null) {
            $model->img1 = (string)base64_encode(file_get_contents($path1->tempName));
        }
        if($path2 != null && $path2->tempName != null) {
            $data2 = file_get_contents($path2->tempName);
            $img2 = base64_encode($data2);
            $model->img2 = (string)$img2;
        }
        if($path3 != null && $path3->tempName != null) {
            $data3 = file_get_contents($path3->tempName);
            $img3 = base64_encode($data3);
            $model->img3 = (string)$img3;
        }
        if($path4 != null && $path4->tempName != null) {
            $data4 = file_get_contents($path4->tempName);
            $img4 = base64_encode($data4);
            $model->img4 = (string)$img4;
        }
        return $model;
    }



    /**
     * Creates a new Publicacaopesq model.
     * If creation is successful, the browser will be redirected to the 'view' page.
     * @return mixed
     */
    public function actionCreate(){
        $model = new Publicacaopesq();

        $model->latitude = -16.3364897;
        $model->longitude = -48.9413023;

        $oldAttributes = $model->oldAttributes;
        if ($model->load(Yii::$app->request->post())) {
            if(Yii::$app->request->isPost){
                $model =$this->getphotos($model);
                if($model->img1 == null){
                    $model->img1 = $oldAttributes['img1'];
                }
                if($model->img2 == null){
                    $model->img2 = $oldAttributes['img2'];
                }
                if($model->img3 == null){
                    $model->img3 = $oldAttributes['img3'];
                }
                if($model->img4 == null){
                    $model->img4 = $oldAttributes['img4'];
                }
            }
            if($model->save()){
                return $this->redirect(['view', 'id' => $model->id]);
            }else{
                return $this->render('create', [
                    'model' => $model,
                ]);
            }
        }

        return $this->render('create', [
            'model' => $model,
        ]);
    }

    /**
     * Updates an existing Publicacaopesq model.
     * If update is successful, the browser will be redirected to the 'view' page.
     * @param integer $id
     * @return mixed
     * @throws NotFoundHttpException if the model cannot be found
     */
    public function actionUpdate($id)
    {
        $model = $this->findModel($id);
        $oldAttributes = $model->oldAttributes;
        if ($model->load(Yii::$app->request->post())) {
            if(Yii::$app->request->isPost){
                $model = $this->getphotos($model,$oldAttributes);
                if($model->img1 == null){
                    $model->img1 = $oldAttributes['img1'];
                }
                if($model->img2 == null){
                    $model->img2 = $oldAttributes['img2'];
                }
                if($model->img3 == null){
                    $model->img3 = $oldAttributes['img3'];
                }
                if($model->img4 == null){
                    $model->img4 = $oldAttributes['img4'];
                }
            }
            if($model->save()){
                return $this->redirect(['view', 'id' => $model->id]);
            }else{
                return $this->render('update', [
                    'model' => $model,
                ]);
            }
        }

        return $this->render('update', [
            'model' => $model,
        ]);
    }

    /**
     * Deletes an existing Publicacaopesq model.
     * If deletion is successful, the browser will be redirected to the 'index' page.
     * @param integer $id
     * @return mixed
     * @throws NotFoundHttpException if the model cannot be found
     */
    public function actionDelete($id)
    {
        if (Avaliacaopubpesq::find()->where(['idpubpesq'=>$id])->all()){
            Yii::$app->session->setFlash('danger', "Esta publicação contem avaliação");
            return $this->redirect(['index']);
        }

        $this->findModel($id)->delete();

        return $this->redirect(['index']);
    }

    /**
     * Finds the Publicacaopesq model based on its primary key value.
     * If the model is not found, a 404 HTTP exception will be thrown.
     * @param integer $id
     * @return Publicacaopesq the loaded model
     * @throws NotFoundHttpException if the model cannot be found
     */
    protected function findModel($id)
    {
        if (($model = Publicacaopesq::findOne($id)) !== null) {
            return $model;
        }

        throw new NotFoundHttpException('The requested page does not exist.');
    }
}
