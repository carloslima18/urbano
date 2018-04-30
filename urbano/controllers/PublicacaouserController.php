<?php

namespace app\controllers;

use Yii;
use app\models\Publicacaouser;
use app\models\PublicacaouserSearch;
use yii\web\Controller;
use yii\web\NotFoundHttpException;
use yii\filters\VerbFilter;

/**
 * PublicacaouserController implements the CRUD actions for Publicacaouser model.
 */
class PublicacaouserController extends Controller
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
     * Lists all Publicacaouser models.
     * @return mixed
     */
    public function actionIndex()
    {
        $searchModel = new PublicacaouserSearch();
        $dataProvider = $searchModel->search(Yii::$app->request->queryParams);

        return $this->render('index', [
            'searchModel' => $searchModel,
            'dataProvider' => $dataProvider,
        ]);
    }

    /**
     * Displays a single Publicacaouser model.
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

    public function savefoto($image){
        define('UPLOAD_DIR','C:/Users/carlo/Desktop/curso de ingles/ProjetoCulturalAps/urbano/web/uploads/');
        $data1 = base64_decode($image);
        $file = UPLOAD_DIR . uniqid() . '.jpg';
        $success = file_put_contents($file, $data1);
        return $file;
    }
    /**
     * Creates a new Publicacaouser model.
     * If creation is successful, the browser will be redirected to the 'view' page.
     * @return mixed
     */
    public function actionCreate()
    {
        $model = new Publicacaouser();

        if ($model->load(Yii::$app->request->post())) {
             $model->img1 = $this->savefoto($model->img1);
            $model->img2 = $this->savefoto($model->img2);
            $model->img3 = $this->savefoto($model->img3);
            $model->img4 = $this->savefoto($model->img4);

             $model->save();

            /* define('UPLOAD_DIR','C:/Users/carlo/Desktop/curso de ingles/ProjetoCulturalAps/cult/uploads/'),
              $data = base64_decode($model->img1),
              $file = UPLOAD_DIR . uniqid() . '.jpg',
              $success = file_put_contents($file, $data),*/
            return $this->redirect(['view', 'id' => $model->id]);
        }

        return $this->render('create', [
            'model' => $model,
        ]);
    }

    /**
     * Updates an existing Publicacaouser model.
     * If update is successful, the browser will be redirected to the 'view' page.
     * @param integer $id
     * @return mixed
     * @throws NotFoundHttpException if the model cannot be found
     */
    public function actionUpdate($id)
    {
        $model = $this->findModel($id);

        if ($model->load(Yii::$app->request->post()) && $model->save()) {
            return $this->redirect(['view', 'id' => $model->id]);
        }

        return $this->render('update', [
            'model' => $model,
        ]);
    }

    /**
     * Deletes an existing Publicacaouser model.
     * If deletion is successful, the browser will be redirected to the 'index' page.
     * @param integer $id
     * @return mixed
     * @throws NotFoundHttpException if the model cannot be found
     */
    public function actionDelete($id)
    {
        $this->findModel($id)->delete();

        return $this->redirect(['index']);
    }

    /**
     * Finds the Publicacaouser model based on its primary key value.
     * If the model is not found, a 404 HTTP exception will be thrown.
     * @param integer $id
     * @return Publicacaouser the loaded model
     * @throws NotFoundHttpException if the model cannot be found
     */
    protected function findModel($id)
    {
        if (($model = Publicacaouser::findOne($id)) !== null) {
            return $model;
        }

        throw new NotFoundHttpException('The requested page does not exist.');
    }
}
