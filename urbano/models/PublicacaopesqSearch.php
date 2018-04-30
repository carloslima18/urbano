<?php

namespace app\models;

use Yii;
use yii\base\Model;
use yii\data\ActiveDataProvider;
use app\models\Publicacaopesq;

/**
 * PublicacaopesqSearch represents the model behind the search form of `app\models\Publicacaopesq`.
 */
class PublicacaopesqSearch extends Publicacaopesq
{
    /**
     * @inheritdoc
     */
    public function rules()
    {
        return [
            [['id', 'pesquisador'], 'integer'],
            [['nome', 'redesocial', 'endereco', 'contato', 'email', 'atvexercida', 'categoria', 'anoinicio', 'cnpj', 'representacao', 'recurso', 'aprovado', 'geo_gps', 'img1', 'img2', 'img3', 'img4', 'campo1', 'campo2', 'campo3', 'campo4', 'campo5', 'campo6', 'campo7', 'campo8', 'campo9', 'campo10', 'campo11', 'campo12', 'campo13', 'campo14', 'campo15', 'campo16', 'campo17', 'campo18', 'campo19', 'campo20'], 'safe'],
            [['latitude', 'longitude'], 'number'],
        ];
    }

    /**
     * @inheritdoc
     */
    public function scenarios()
    {
        // bypass scenarios() implementation in the parent class
        return Model::scenarios();
    }

    /**
     * Creates data provider instance with search query applied
     *
     * @param array $params
     *
     * @return ActiveDataProvider
     */
    public function search($params)
    {
        $query = Publicacaopesq::find();

        // add conditions that should always apply here

        $dataProvider = new ActiveDataProvider([
            'query' => $query,
            'pagination' => [
                'pageSize' => 2,
            ],
        ]);

        $this->load($params);

        if (!$this->validate()) {
            // uncomment the following line if you do not want to return any records when validation fails
            // $query->where('0=1');
            return $dataProvider;
        }

        // grid filtering conditions
        $query->andFilterWhere([
            'id' => $this->id,
            'latitude' => $this->latitude,
            'longitude' => $this->longitude,
            'pesquisador' => $this->pesquisador,
        ]);

        $query->andFilterWhere(['like', 'nome', $this->nome])
            ->andFilterWhere(['like', 'redesocial', $this->redesocial])
            ->andFilterWhere(['like', 'endereco', $this->endereco])
            ->andFilterWhere(['like', 'contato', $this->contato])
            ->andFilterWhere(['like', 'email', $this->email])
            ->andFilterWhere(['like', 'atvexercida', $this->atvexercida])
            ->andFilterWhere(['like', 'categoria', $this->categoria])
            ->andFilterWhere(['like', 'anoinicio', $this->anoinicio])
            ->andFilterWhere(['like', 'cnpj', $this->cnpj])
            ->andFilterWhere(['like', 'representacao', $this->representacao])
            ->andFilterWhere(['like', 'recurso', $this->recurso])
            ->andFilterWhere(['like', 'aprovado', $this->aprovado])
            ->andFilterWhere(['like', 'geo_gps', $this->geo_gps])
            ->andFilterWhere(['like', 'img1', $this->img1])
            ->andFilterWhere(['like', 'img2', $this->img2])
            ->andFilterWhere(['like', 'img3', $this->img3])
            ->andFilterWhere(['like', 'img4', $this->img4])
            ->andFilterWhere(['like', 'campo1', $this->campo1])
            ->andFilterWhere(['like', 'campo2', $this->campo2])
            ->andFilterWhere(['like', 'campo3', $this->campo3])
            ->andFilterWhere(['like', 'campo4', $this->campo4])
            ->andFilterWhere(['like', 'campo5', $this->campo5])
            ->andFilterWhere(['like', 'campo6', $this->campo6])
            ->andFilterWhere(['like', 'campo7', $this->campo7])
            ->andFilterWhere(['like', 'campo8', $this->campo8])
            ->andFilterWhere(['like', 'campo9', $this->campo9])
            ->andFilterWhere(['like', 'campo10', $this->campo10])
            ->andFilterWhere(['like', 'campo11', $this->campo11])
            ->andFilterWhere(['like', 'campo12', $this->campo12])
            ->andFilterWhere(['like', 'campo13', $this->campo13])
            ->andFilterWhere(['like', 'campo14', $this->campo14])
            ->andFilterWhere(['like', 'campo15', $this->campo15])
            ->andFilterWhere(['like', 'campo16', $this->campo16])
            ->andFilterWhere(['like', 'campo17', $this->campo17])
            ->andFilterWhere(['like', 'campo18', $this->campo18])
            ->andFilterWhere(['like', 'campo19', $this->campo19])
            ->andFilterWhere(['like', 'campo20', $this->campo20]);

        return $dataProvider;
    }
}
