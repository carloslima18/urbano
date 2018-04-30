<?php

namespace app\controllers;

use Yii;
use app\models\Pesquisador;
use app\models\PesquisadorSearch;
use yii\web\Controller;
use yii\web\NotFoundHttpException;
use yii\filters\VerbFilter;
use app\models\Publicacaopesq;

/**
 * PesquisadorController implements the CRUD actions for Pesquisador model.
 */
class PesquisadorController extends Controller
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
     * Lists all Pesquisador models.
     * @return mixed
     */
    public function actionIndex()
    {
        $searchModel = new PesquisadorSearch();
        $dataProvider = $searchModel->search(Yii::$app->request->queryParams);

        return $this->render('index', [
            'searchModel' => $searchModel,
            'dataProvider' => $dataProvider,
        ]);
    }

    /**
     * Displays a single Pesquisador model.
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

    /**
     * Creates a new Pesquisador model.
     * If creation is successful, the browser will be redirected to the 'view' page.
     * @return mixed
     */
    public function actionCreate()
    {
        $model = new Pesquisador();
        //https://github.com/yiisoft/yii2/blob/master/docs/guide/security-passwords.md#encryption-and-decryption
        if ($model->load(Yii::$app->request->post())) {
          //  $model->senha = MD5($model->senha);//Yii::$app->getSecurity()->generatePasswordHash($model->senha);
            if(!$model->save()){
                return $this->render('create', [
                    'model' => $model,
                ]);
            }
           //$model->senha = password_hash($model->senha, PASSWORD_DEFAULT);
            return $this->redirect(['view', 'id' => $model->id]);
        }

        return $this->render('create', [
            'model' => $model,
        ]);
    }

    public function sendEmail($model){
        Yii::$app->mailer->compose()
            ->setFrom('carlosficherfalqueiro@gmail.com')
            ->setTo($model->email)
            ->setSubject('URBANO')
            ->setTextBody('Senhor'.$model->nome.'venho informa que recebemos
            a solicitação de cadastro do senhor, e após a verificação,
            a equipe do URBANO confirmou a aprovação para o uso da nossa área de 
            pesquisador em nosso aplicativo.
            
            Seu loguin é formado pelo seu endereço de email, e sua senha será seu CPF.
            Obrigado!
            ')
            ->send();
    }

    /**
     * Updates an existing Pesquisador model.
     * If update is successful, the browser will be redirected to the 'view' page.
     * @param integer $id
     * @return mixed
     * @throws NotFoundHttpException if the model cannot be found
     */
    public function actionUpdate($id)
    {
        $model = $this->findModel($id);

        if ($model->load(Yii::$app->request->post()) && $model->save()) {

            if($model->campo2 == "S"){
                $this->sendEmail($model);
            }
            return $this->redirect(['view', 'id' => $model->id]);
        }

        return $this->render('update', [
            'model' => $model,
        ]);
    }



    /**
     * Deletes an existing Pesquisador model.
     * If deletion is successful, the browser will be redirected to the 'index' page.
     * @param integer $id
     * @return mixed
     * @throws NotFoundHttpException if the model cannot be found
     */
    public function actionDelete($id)
    {
        if(Publicacaopesq::find()->where(['pesquisador'=>$id])->all()){
            Yii::$app->session->setFlash('danger', "Há publicações vinculadas a este pesquisador");
            return $this->redirect(['index']);
        }
        $this->findModel($id)->delete();
        return $this->redirect(['index']);
    }

    /**
     * Finds the Pesquisador model based on its primary key value.
     * If the model is not found, a 404 HTTP exception will be thrown.
     * @param integer $id
     * @return Pesquisador the loaded model
     * @throws NotFoundHttpException if the model cannot be found
     */
    protected function findModel($id)
    {
        if (($model = Pesquisador::findOne($id)) !== null) {
            return $model;
        }

        throw new NotFoundHttpException('The requested page does not exist.');
    }
}
